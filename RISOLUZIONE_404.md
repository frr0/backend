# 🔧 Risoluzione Errore 404 - URL Corretti

## ✅ Correzioni Applicate

1. **Aggiunta annotazione @WebServlet** a GiocatoriServlet
2. **Pulito web.xml** (rimosse configurazioni duplicate)
3. **Verificato che SalvaGiocatoreServlet** ha già l'annotazione

## 📍 URL Corretti da Usare

### Context Path
Il tuo progetto si chiama **Backend**, quindi tutti gli URL iniziano con:
```
http://localhost:8080/Backend/
```

### URL delle Servlet

#### 1. Lista Giocatori (HTML generato da servlet)
```
http://localhost:8080/Backend/listaGiocatori
```
**Cosa fa**: Genera l'intera pagina HTML con la tabella popolata dal database

#### 2. Pagina HTML Statica
```
http://localhost:8080/Backend/giocatori.html
```
**Cosa fa**: Mostra la pagina HTML statica con form per salvare giocatori

#### 3. Salva Giocatore (usata dal form)
```
http://localhost:8080/Backend/salvaGiocatore
```
**Cosa fa**: Riceve POST dal form e salva nel database
(Non aprire questo URL direttamente nel browser - è chiamato dal form)

## 🧪 Test Passo per Passo

### Test 1: Verifica che Tomcat sia avviato
1. In IntelliJ, clicca il pulsante **Run** (triangolo verde)
2. Aspetta che nella console veda: `Server startup in [xxx] milliseconds`

### Test 2: Pagina HTML statica
1. Vai a: `http://localhost:8080/Backend/giocatori.html`
2. Dovresti vedere il form per inserire giocatori
3. La tabella sarà vuota (è normale, è HTML statico)

### Test 3: Salva un giocatore
1. Nella pagina `giocatori.html`, compila il form:
   - **Nome**: Mario
   - **Cognome**: Rossi
   - **Data Nascita**: 1990-05-15
   - **Nazione Nascita**: Italia
   - (altri campi opzionali)
2. Clicca **Salva**
3. Dovresti essere reindirizzato di nuovo a `giocatori.html`

### Test 4: Verifica nel database
```sql
SELECT * FROM GIOCATORE ORDER BY ID_REC DESC;
```
Dovresti vedere il giocatore appena inserito.

### Test 5: Lista generata da servlet
1. Vai a: `http://localhost:8080/Backend/listaGiocatori`
2. Dovresti vedere la stessa interfaccia ma con la tabella popolata dal database

## 🐛 Troubleshooting

### Errore 404 persiste
**Causa 1**: Tomcat non ha ricaricato l'applicazione
- **Soluzione**: In IntelliJ, clicca **Redeploy** (icona con due frecce circolari)
- Oppure: **Stop** e poi **Run** di nuovo

**Causa 2**: Porta sbagliata
- Verifica in IntelliJ Tomcat configuration quale porta usa
- Potrebbe essere 8080, 9090, o altra

**Causa 3**: Context path diverso
- In IntelliJ, vai su: **Run > Edit Configurations > Tomcat > Deployment**
- Verifica il "Application context"
- Se è `/` usa solo `http://localhost:8080/giocatori.html`
- Se è `/Backend` usa `http://localhost:8080/Backend/giocatori.html`

### Errore 500 invece di 404
Guarda i log di Tomcat nella console di IntelliJ:
- **SQLException**: Problema di connessione al database
- **ClassNotFoundException**: Driver Oracle non trovato
- **NullPointerException**: Parametro mancante nel form

### Form non salva nulla
1. **Verifica i nomi dei campi** nel form HTML:
   ```html
   <input type="text" name="NOME" />        ✅ Corretto
   <input type="text" name="nome" />        ❌ Sbagliato (minuscolo)
   ```

2. **Verifica la servlet** riceva i parametri:
   Aggiungi temporaneamente in SalvaGiocatoreServlet:
   ```java
   System.out.println("NOME ricevuto: " + request.getParameter("NOME"));
   System.out.println("COGNOME ricevuto: " + request.getParameter("COGNOME"));
   ```

3. **Controlla il database**:
   - Nazioni/Città esistono? Le query cercano per nome
   - Ruoli esistono nella tabella ZS_RUOLI?

## 🎯 Differenza tra i due approcci

### Approccio 1: giocatori.html (Statico + Servlet per CRUD)
```
http://localhost:8080/Backend/giocatori.html
```
✅ Più moderno e flessibile
✅ Separazione tra presentazione e logica
❌ Tabella vuota all'inizio (devi implementare caricamento AJAX)

### Approccio 2: listaGiocatori (Servlet genera tutto)
```
http://localhost:8080/Backend/listaGiocatori
```
✅ Tutto in un posto
✅ Tabella già popolata
❌ Meno flessibile
❌ Mescola presentazione e logica

## 📝 Prossimi Passi

Per rendere **giocatori.html** completamente funzionale:

1. **Implementare caricamento AJAX** dei giocatori
2. **Implementare DELETE** (click sull'icona cestino)
3. **Implementare UPDATE** (click sulla riga per editare)

Oppure semplicemente usa **listaGiocatori** che è già completo!

## 🔍 Verifica Configurazione IntelliJ

1. **Run > Edit Configurations**
2. Seleziona la configurazione Tomcat
3. Verifica:
   - **Server tab**: Porta HTTP (es: 8080)
   - **Deployment tab**: 
     - Artifact: `Backend:war exploded`
     - Application context: `/Backend` (o `/`)
4. Se modifichi qualcosa, clicca **Apply** e rilancia

## ✅ Checklist Rapida

- [ ] Tomcat è avviato?
- [ ] Vedi "Server startup" nei log?
- [ ] URL include il context path? (`/Backend/`)
- [ ] L'applicazione è deployata? (vedi Deployment tab in Tomcat config)
- [ ] Hai fatto Redeploy dopo le modifiche?
- [ ] Il database Oracle è raggiungibile?

Se tutto è OK, dovresti vedere la pagina!

