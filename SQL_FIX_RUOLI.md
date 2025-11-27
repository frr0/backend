# 🔧 Script SQL per Risolvere il Problema dei Ruoli

## 🔍 Verifica Situazione Attuale

### 1. Verifica quali ruoli esistono nel database:
```sql
SELECT ID_REC, DESCRIZIONE FROM ZS_RUOLI ORDER BY ID_REC;
```

### 2. Verifica la struttura della tabella:
```sql
DESC ZS_RUOLI;
```

### 3. Verifica se esiste una sequence per ID_REC:
```sql
SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE '%RUOL%';
```

---

## ✅ Soluzione 1: Inserisci i Ruoli con ID 1,2,3,4

Se la tabella è vuota o ha ID diversi, esegui:

```sql
-- Elimina ruoli esistenti (OPZIONALE - solo se vuoi ricominciare da zero)
-- DELETE FROM ZS_RUOLI;
-- COMMIT;

-- Inserisci i ruoli con gli ID che il form si aspetta
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (1, 'Portiere');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (2, 'Difensore');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (3, 'Centrocampista');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (4, 'Attaccante');
COMMIT;
```

### Verifica inserimento:
```sql
SELECT ID_REC, DESCRIZIONE FROM ZS_RUOLI ORDER BY ID_REC;
```

**Risultato atteso:**
```
ID_REC | DESCRIZIONE
-------|---------------
     1 | Portiere
     2 | Difensore
     3 | Centrocampista
     4 | Attaccante
```

---

## ✅ Soluzione 2: Se gli ID sono diversi (es: 101, 102, 103, 104)

### Opzione A: Cambia il form per usare gli ID reali

Verifica quali ID hai:
```sql
SELECT ID_REC, DESCRIZIONE FROM ZS_RUOLI ORDER BY ID_REC;
```

Supponi che il risultato sia:
```
ID_REC | DESCRIZIONE
-------|---------------
   101 | Portiere
   102 | Difensore
   103 | Centrocampista
   104 | Attaccante
```

Allora devi modificare **GiocatoriServlet.java** e **giocatori.html**:

```java
// In GiocatoriServlet.java, riga ~189:
wr.println("<option value=\"\">(Nessuno)</option>");
wr.println("<option value=\"101\">Portiere</option>");      // Usa ID reale
wr.println("<option value=\"102\">Difensore</option>");     // Usa ID reale
wr.println("<option value=\"103\">Centrocampista</option>"); // Usa ID reale
wr.println("<option value=\"104\">Attaccante</option>");    // Usa ID reale
```

### Opzione B: Aggiorna gli ID nel database

Se preferisci usare 1,2,3,4, aggiorna:
```sql
UPDATE ZS_RUOLI SET ID_REC = 1 WHERE DESCRIZIONE = 'Portiere';
UPDATE ZS_RUOLI SET ID_REC = 2 WHERE DESCRIZIONE = 'Difensore';
UPDATE ZS_RUOLI SET ID_REC = 3 WHERE DESCRIZIONE = 'Centrocampista';
UPDATE ZS_RUOLI SET ID_REC = 4 WHERE DESCRIZIONE = 'Attaccante';
COMMIT;
```

**ATTENZIONE**: Questo potrebbe fallire se ci sono giocatori già esistenti che referenziano i vecchi ID.

---

## ✅ Soluzione 3: Salva SENZA Ruolo (Temporanea)

Ho già modificato il form per includere un'opzione `(Nessuno)` con valore vuoto.

### Test:
1. **Rebuild & Restart** Tomcat
2. Vai a: `http://localhost:8080/Backend/listaGiocatori`
3. Compila:
   ```
   Nome:            Mario
   Cognome:         Rossi
   Data Nascita:    2000-01-15
   Ruolo:           (Nessuno)  ← Seleziona questa opzione!
   Nazione:         Italia
   ```
4. Clicca **Salva**
5. ✅ **Dovrebbe funzionare!**

---

## 🔍 Diagnostica Avanzata

### Verifica il constraint FK:
```sql
SELECT 
    constraint_name, 
    r_constraint_name, 
    delete_rule
FROM user_constraints 
WHERE constraint_name = 'GIOCATORE_RUOLO_FK';
```

### Verifica quale colonna referenzia:
```sql
SELECT 
    column_name, 
    position
FROM user_cons_columns 
WHERE constraint_name = 'GIOCATORE_RUOLO_FK'
ORDER BY position;
```

### Verifica i giocatori esistenti:
```sql
SELECT 
    ID_REC, 
    NOME, 
    COGNOME, 
    ID_RUOLO_ABITUALE_FK 
FROM GIOCATORI 
WHERE ID_RUOLO_ABITUALE_FK IS NOT NULL;
```

Se ci sono giocatori con ID_RUOLO_ABITUALE_FK non NULL, verifica che questi ID esistano in ZS_RUOLI:
```sql
SELECT DISTINCT ID_RUOLO_ABITUALE_FK 
FROM GIOCATORI 
WHERE ID_RUOLO_ABITUALE_FK IS NOT NULL
AND ID_RUOLO_ABITUALE_FK NOT IN (SELECT ID_REC FROM ZS_RUOLI);
```

---

## 📝 Riepilogo delle Modifiche Fatte

### 1. GiocatoriServlet.java
Aggiunto `<option value="">(Nessuno)</option>` nel select del ruolo.

### 2. giocatori.html
Aggiunto `<option value="">(Nessuno)</option>` nel select del ruolo.

### 3. SalvaGiocatoreServlet.java
Già gestisce correttamente i valori vuoti:
```java
if (ruoloAbituale != null) {
    queryInsert.setInt(8, ruoloAbituale);
} else {
    queryInsert.setNull(8, Types.INTEGER);  // Inserisce NULL se vuoto
}
```

---

## 🎯 Azione Immediata

**PASSO 1**: Esegui in SQL Developer:
```sql
SELECT ID_REC, DESCRIZIONE FROM ZS_RUOLI ORDER BY ID_REC;
```

**PASSO 2**: In base al risultato:
- **Se vuoto**: Esegui le INSERT sopra (Soluzione 1)
- **Se ID diversi**: Usa Soluzione 2
- **Per test rapido**: Usa Soluzione 3 (senza ruolo)

**PASSO 3**: Rebuild & Restart Tomcat

**PASSO 4**: Test con ruolo `(Nessuno)` selezionato

---

## ✅ Quando Funzionerà

Vedrai il giocatore salvato nella tabella SENZA errori!

Se selezioni `(Nessuno)` come ruolo, il campo `ID_RUOLO_ABITUALE_FK` sarà NULL nel database, che è perfettamente valido.

---

**ESEGUI ORA**: La query di verifica dei ruoli e dimmi il risultato!

