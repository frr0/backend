# 🎉 PROGRESSO! Errore ORA-02291 - Foreign Key Ruolo

## ✅ Buone Notizie!

Le tabelle sono **corrette** ora! (`GIOCATORI`, `NAZIONI`). L'errore è cambiato da "table not found" a "foreign key constraint".

## 🔍 Il Problema Attuale

```
ORA-02291: integrity constraint (GIOCATORE_RUOLO_FK) violated - parent key not found
```

**Causa**: Stai inserendo un valore per `ID_RUOLO_ABITUALE_FK` che **non esiste** nella tabella `ZS_RUOLI`.

### Possibili Scenari:

#### Scenario 1: Hai selezionato un ruolo che non esiste
- Form dice: Ruolo = "Attaccante" (valore 4)
- Database: ID_REC = 4 non esiste in ZS_RUOLI

#### Scenario 2: Il campo è NOT NULL ma inserisci NULL
- Form: Nessun ruolo selezionato
- Codice inserisce: NULL
- Database: La colonna ID_RUOLO_ABITUALE_FK è NOT NULL

## ✅ Modifiche Applicate

Ho aggiunto:
1. **Logging esteso** per debug
2. **Gestione robusta** del ruolo (controlla se vuoto, "0", NULL)
3. **Rollback** in caso di errore
4. **Messaggi di errore chiari** per capire quale FK fallisce

## 🧪 Test con Logging

### Passo 1: Rebuild e Riavvia
```
1. Stop Tomcat
2. Build > Rebuild Project
3. Start Tomcat
```

### Passo 2: Prova a Salvare
Vai a: `http://localhost:8080/Backend/listaGiocatori`

#### Test A: SENZA Ruolo (lascia vuoto)
```
Nome:            Mario
Cognome:         Rossi
Data Nascita:    2000-01-15
Alias:           (vuoto)
Numero Maglia:   (vuoto)
Ruolo:           (NON selezionare nulla - lascia il default)
Nazione:         Italia
Città:           (vuoto)
```

**Clicca Salva** e guarda i **log di Tomcat** nella console di IntelliJ.

Dovresti vedere:
```
DEBUG: Nessun ruolo selezionato
DEBUG: Tentativo INSERT - Ruolo: null, Nazione: Italia, Città: null
DEBUG: Inserimento ruolo NULL
```

**Risultato Atteso**:
- ✅ Se il campo è NULLABLE → Dovrebbe salvare
- ❌ Se il campo è NOT NULL → Errore ORA-01400 (cannot insert NULL)

#### Test B: CON Ruolo Attaccante
```
Ruolo: Attaccante (valore 4)
(altri campi come sopra)
```

**Clicca Salva** e guarda i log:
```
DEBUG: Ruolo selezionato: 4
DEBUG: Tentativo INSERT - Ruolo: 4, Nazione: Italia, Città: null
DEBUG: Inserimento ruolo ID: 4
```

**Risultato Atteso**:
- ✅ Se ID_REC = 4 esiste in ZS_RUOLI → Dovrebbe salvare
- ❌ Se ID_REC = 4 NON esiste → Errore ORA-02291 (il tuo errore attuale)

## 🔧 Soluzioni

### Soluzione 1: Verifica i ruoli nel database

Esegui questa query in SQL Developer:
```sql
SELECT ID_REC, DESCRIZIONE FROM ZS_RUOLI ORDER BY ID_REC;
```

**Se vedi**:
```
ID_REC | DESCRIZIONE
-------|---------------
1      | Portiere
2      | Difensore
3      | Centrocampista
4      | Attaccante
```
→ **OK, i ruoli ci sono!** Il problema è altrove (vedi Soluzione 3)

**Se vedi**:
```
ID_REC | DESCRIZIONE
-------|---------------
101    | Portiere
102    | Difensore
...
```
→ **Gli ID non matchano!** Il form usa 1,2,3,4 ma il DB ha altri ID.

**Se vedi**:
```
no rows selected
```
→ **I ruoli non esistono!** Devi inserirli.

### Soluzione 2: Inserisci i ruoli nel database

```sql
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (1, 'Portiere');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (2, 'Difensore');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (3, 'Centrocampista');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (4, 'Attaccante');
COMMIT;
```

### Soluzione 3: Modifica il form per usare gli ID corretti

Se i ruoli nel DB hanno ID diversi (es: 101, 102, 103, 104), devi cambiare il form HTML:

**In GiocatoriServlet.java**, cambia:
```java
wr.println("<option value=\"1\">Portiere</option>");
wr.println("<option value=\"2\">Difensore</option>");
wr.println("<option value=\"3\">Centrocampista</option>");
wr.println("<option value=\"4\">Attaccante</option>");
```

Con gli ID reali che hai nel database:
```java
wr.println("<option value=\"101\">Portiere</option>");  // Usa ID reale
wr.println("<option value=\"102\">Difensore</option>");
// ...
```

**In giocatori.html**, fai lo stesso:
```html
<option value="101">Portiere</option>
<option value="102">Difensore</option>
...
```

### Soluzione 4: Lascia il ruolo vuoto per ora

Per testare il resto del sistema, **NON selezionare nessun ruolo** nel form.

Se il campo è NULLABLE, dovrebbe funzionare.

Se il campo è NOT NULL, devi prima inserire i ruoli nel DB (Soluzione 2).

## 🎯 Prossimi Passi

### Passo 1: Riavvia con il nuovo codice
```
Stop Tomcat > Rebuild > Start Tomcat
```

### Passo 2: Guarda i log
Apri la console di IntelliJ e cerca le righe che iniziano con `DEBUG:`

### Passo 3: Verifica i ruoli nel DB
```sql
SELECT * FROM ZS_RUOLI;
```

### Passo 4: Testa
- **Prima** prova SENZA selezionare ruolo
- **Poi** prova CON ruolo (dopo aver verificato che esista)

## 📊 Come Leggere i Log

Nella console di IntelliJ, cerca:

```
DEBUG: Ruolo selezionato: 4
DEBUG: Tentativo INSERT - Ruolo: 4, Nazione: Italia, Città: null
DEBUG: Inserimento ruolo ID: 4
```

Se vedi questo ma poi l'errore ORA-02291, significa che:
- Il form invia 4
- Il codice riceve 4
- Ma nel database non esiste ID_REC = 4 in ZS_RUOLI

## ✅ Quando Funzionerà

Vedrai nei log:
```
DEBUG: Nessun ruolo selezionato (o: Ruolo selezionato: X)
DEBUG: Tentativo INSERT - Ruolo: null (o: 1/2/3/4), Nazione: Italia, Città: null
DEBUG: Inserimento ruolo NULL (o: ID: X)
DEBUG: INSERT completato con successo
```

E la pagina si ricaricherà mostrando il giocatore nella tabella!

## 🆘 Se Ancora Errore

Inviami:
1. **Log completi** dalla console (le righe DEBUG)
2. **Risultato della query**: `SELECT * FROM ZS_RUOLI;`
3. **Quale ruolo hai selezionato** nel form (o "nessuno")

Così posso capire esattamente dove sta il problema!

---

**AZIONE ORA**: Rebuild + Start + Test + Guarda i Log!

