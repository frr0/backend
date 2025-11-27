# 🔧 ISTRUZIONI URGENTI - Ricompila e Test

## ⚠️ IMPORTANTE: Ora le modifiche sono salvate!

Ho corretto definitivamente i nomi delle tabelle nella query INSERT:

### ✅ CORRETTO (ora):
```java
INSERT INTO GIOCATORI (NOME, COGNOME, DATA_DI_NASCITA, ALIAS, 
                       NUMERO_MAGLIA_ABITUALE, ...) 
VALUES (..., (SELECT ID_REC FROM NAZIONI WHERE LOWER(NOME) = LOWER(?)), ...)
```

### ❌ SBAGLIATO (prima):
```java
INSERT INTO GIOCATORE (NOME, COGNOME, DATA_DI_NASCITA, ALIAS, 
                       NUMERO_MAGLIA_ATTUALE, ...) 
VALUES (..., (SELECT ID_REC FROM ZS_NAZIONE WHERE LOWER(NOME) = LOWER(?)), ...)
```

## 🚨 PASSI OBBLIGATORI (Segui nell'ordine)

### Passo 1: STOP Tomcat ⛔
1. In IntelliJ, clicca il pulsante **STOP** (quadrato rosso)
2. **ASPETTA** che nella console veda: `Server shutdown completed`
3. Se non si ferma, clicca di nuovo STOP e aspetta

### Passo 2: CLEAN del Progetto 🧹
1. Menu: **Build > Clean Project**
2. Aspetta che finisca (vedi barra progresso in basso)

### Passo 3: REBUILD del Progetto 🔨
1. Menu: **Build > Rebuild Project** (o Ctrl+Shift+F9)
2. Aspetta che finisca completamente
3. Verifica nella finestra Build che non ci siano errori (solo warning è OK)

### Passo 4: START Tomcat ▶️
1. Clicca il pulsante **RUN** (triangolo verde)
2. Aspetta il messaggio: `Server startup in [xxx] milliseconds`
3. Verifica che non ci siano errori rossi nella console

### Passo 5: TEST nel Browser 🌐
1. Apri: `http://localhost:8080/Backend/listaGiocatori`
2. Compila il form:
   ```
   Nome:            Mario
   Cognome:         Rossi
   Data Nascita:    2000-01-15
   Alias:           (lascia vuoto o metti "Super Mario")
   Numero Maglia:   (lascia vuoto o metti 10)
   Ruolo:           (lascia vuoto o seleziona Attaccante)
   Nazione Nascita: Italia
   Città Nascita:   Roma
   ```
3. Clicca **SALVA**
4. ✅ **Dovrebbe funzionare!**

## ❓ Se Vedi Ancora Errore ORA-00942

Significa che Tomcat sta ancora usando la vecchia versione.

### Soluzione DRASTICA:

#### Opzione A: Redeploy Forzato
1. Stop Tomcat
2. Menu: **Run > Edit Configurations**
3. Seleziona la configurazione Tomcat
4. Tab **Deployment**
5. Clicca l'artifact `Backend:war exploded`
6. Clicca **Remove** (meno)
7. Clicca **Add** (più) > **Artifact** > `Backend:war exploded`
8. Clicca **Apply** e **OK**
9. Run Tomcat

#### Opzione B: Clean Tomcat
1. Stop Tomcat
2. Vai nella cartella:
   ```
   C:\Users\FrancescoRanellucci\.IntelliJIdea2024.x\system\tomcat\
   ```
3. Elimina tutte le cartelle dentro
4. Torna a IntelliJ
5. Build > Rebuild Project
6. Run Tomcat

#### Opzione C: Verifica la Classe Compilata
1. Vai a: `Backend\target\classes\it\zerob\learn\servlet\`
2. Elimina `SalvaGiocatoreServlet.class`
3. Torna a IntelliJ
4. Build > Rebuild Project
5. Verifica che il file `.class` sia stato ricreato
6. Run Tomcat

## 🔍 Come Verificare che la Ricompilazione Sia Avvenuta

Guarda i log di Tomcat all'avvio. Dovresti vedere:
```
[INFO] Deploying web application archive [...]Backend[...]
[INFO] Deployment of web application archive [...]Backend[...] has finished in [xxx] ms
```

Se vedi:
```
[WARNING] A context path must either be an empty string or start with a '/'...
```
È normale, ignoralo.

## 🐛 Altri Errori Possibili

### Errore: "ORA-01400: cannot insert NULL into..."
**Causa**: La nazione "Italia" non esiste nel DB
**Soluzione**: Esegui in SQL Developer:
```sql
INSERT INTO NAZIONI (ID_REC, NOME) VALUES (NAZIONI_SEQ.NEXTVAL, 'Italia');
COMMIT;
```
(Oppure verifica quale sequence usare per ID_REC)

### Errore: "ORA-02291: integrity constraint violated"
**Causa**: Foreign key constraint fallita
**Soluzione**: Verifica che nazione/città/ruolo esistano:
```sql
SELECT * FROM NAZIONI WHERE LOWER(NOME) = 'italia';
SELECT * FROM ZS_CITTA WHERE LOWER(NOME) = 'roma';
SELECT * FROM ZS_RUOLI WHERE ID_REC = 4;
```

### Errore: "ORA-00001: unique constraint violated"
**Causa**: Stai tentando di inserire un ID_REC duplicato
**Soluzione**: Rimuovi il campo ID_REC dal form (deve essere auto-generato)

## ✅ Checklist Finale

- [ ] Stop Tomcat eseguito
- [ ] Clean Project eseguito
- [ ] Rebuild Project eseguito (senza errori)
- [ ] Start Tomcat eseguito
- [ ] Vedo "Server startup" nei log
- [ ] Nessun errore rosso nella console
- [ ] Browser aperto su /listaGiocatori
- [ ] Form compilato con tutti i campi obbligatori
- [ ] Nazione "Italia" esiste nel database
- [ ] Clicco Salva

## 🎉 Risultato Atteso

Dopo aver cliccato Salva:
1. La pagina si ricarica
2. Vedi il giocatore "Mario Rossi" nella tabella
3. Nessun errore 500

Se funziona, sei a posto! 🎊

## 📞 Se Ancora Non Funziona

Inviami:
1. Il messaggio di errore COMPLETO (copia-incolla)
2. Il risultato di questa query SQL:
   ```sql
   SELECT table_name FROM user_tables WHERE table_name LIKE '%GIOC%';
   SELECT table_name FROM user_tables WHERE table_name LIKE '%NAZ%';
   ```
3. Screenshot della console di Tomcat quando parte

---

**IMPORTANTE**: Segui i passi NELL'ORDINE indicato!

