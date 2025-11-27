# Guida: Come usare le Servlet invece del JavaScript

## Problema Iniziale
Il codice non funzionava perché:
1. **Tutto l'HTML era commentato** → la pagina non si vedeva
2. **Il JavaScript era commentato** → ma anche se decommentato, avrebbe causato problemi
3. **Il form non veniva inviato alla servlet** → perché il JS intercettava il submit con `event.preventDefault()`

## Soluzione Implementata

### File modificati:

#### 1. **giocatori.html** - Decommentato
- Ora il form è visibile e funzionante
- Il form ha `action="salvaGiocatore"` e `method="POST"`
- Quando premi "Salva", il browser invia i dati alla servlet

#### 2. **SalvaGiocatoreServlet.java** - Corretto
Correzioni applicate:
- ✅ Formato data corretto: `yyyy-MM-dd` (non `YYYY-MM-DD`)
- ✅ Nomi parametri corretti per matchare il form HTML
- ✅ Gestione `NULL` per campi opzionali (numero maglia, ruolo)
- ✅ Redirect a `giocatori.html` dopo il salvataggio
- ✅ Implementato UPDATE (prima c'era solo INSERT)

#### 3. **web.xml** - Modificato
- Rimosso mapping di `/giocatori.html` a GiocatoriServlet
- Ora `giocatori.html` è un file statico normale
- GiocatoriServlet disponibile su `/listaGiocatori` (se necessario in futuro)

#### 4. **giocatori-servlet.js** - Creato (nuovo file)
JavaScript MINIMALE che:
- ✅ Fa la ricerca nella tabella (funzione `ricercaGiocatori()`)
- ✅ Valida il form prima dell'invio (opzionale)
- ❌ NON intercetta il submit (lascia che vada alla servlet)
- ❌ NON gestisce INSERT/UPDATE/DELETE (lo fa la servlet)

## Differenze tra i due approcci

### ❌ JavaScript Vecchio (giocatori.js - COMMENTATO)
```javascript
function handlerFormGiocatoriSubmitButtonClick(event){
    event.preventDefault(); // ← Blocca l'invio alla servlet!
    
    // Gestisce tutto lato client:
    aggiungiRigaTableGiocatori(valori);  // Solo nel DOM, non nel DB!
    aggiornaRigaTableGiocatori(valori);   // Solo nel DOM, non nel DB!
}
```
**Risultato**: I dati NON vengono salvati nel database Oracle.

### ✅ JavaScript Nuovo (giocatori-servlet.js - ATTIVO)
```javascript
function validateFormGiocatori(event) {
    // Valida i campi
    if (!isValid) {
        event.preventDefault(); // Blocca SOLO se ci sono errori
        alert("Errori...");
    }
    // Se tutto OK, il form viene inviato alla servlet
}
```
**Risultato**: I dati vengono salvati nel database Oracle tramite la servlet.

## Flusso di esecuzione con le servlet

1. **Utente compila il form** in `giocatori.html`
2. **Utente clicca "Salva"**
3. **JavaScript valida i campi** (opzionale)
4. **Browser invia richiesta POST** a `/salvaGiocatore`
5. **SalvaGiocatoreServlet riceve i dati**
   - Estrae i parametri dalla request
   - Inserisce/Aggiorna nel database Oracle
   - Fa commit della transazione
6. **Servlet reindirizza** a `giocatori.html`
7. **Utente vede la pagina aggiornata**

## Come caricare i dati nella tabella?

### Opzione A: Usare GiocatoriServlet (già implementata)
Cambia l'URL nel browser da:
```
http://localhost:8080/Backend/giocatori.html
```
a:
```
http://localhost:8080/Backend/listaGiocatori
```

La servlet genererà l'HTML con i dati dal database.

### Opzione B: Modificare giocatori.html per caricare dati via AJAX
Aggiungi nel nuovo JavaScript:
```javascript
document.addEventListener("DOMContentLoaded", function() {
    // Carica giocatori dal database tramite servlet
    fetch('listaGiocatori?json=true')
        .then(response => response.json())
        .then(giocatori => {
            // Popola la tabella con i dati
        });
});
```

## Quale JavaScript ti serve?

### ✅ JavaScript NECESSARIO per le servlet:
- **Ricerca nella tabella** (`ricercaGiocatori()`) - già implementato
- **Validazione form** (opzionale) - già implementato
- **Funzioni di utilità** (`isInt()`, `formatDate()`) - già nel tag `<script>` in HTML

### ❌ JavaScript NON NECESSARIO (fa tutto la servlet):
- ❌ `handlerFormGiocatoriSubmitButtonClick()` - blocca il submit
- ❌ `aggiungiRigaTableGiocatori()` - aggiunge solo nel DOM
- ❌ `aggiornaRigaTableGiocatori()` - aggiorna solo nel DOM
- ❌ `handlerTableGiocatoriDeleteButtunClick()` - elimina solo nel DOM
- ❌ `handlerTableGiocatoriRowClick()` - per editing inline
- ❌ `caricaGiocatori()` da JSON file

## Test della soluzione

1. **Avvia il server** Tomcat
2. **Vai a** `http://localhost:8080/Backend/giocatori.html`
3. **Compila il form** con i dati di un nuovo giocatore
4. **Clicca "Salva"**
5. **Verifica nel database Oracle** che il record sia stato inserito:
   ```sql
   SELECT * FROM GIOCATORE ORDER BY ID_REC DESC;
   ```

## Risoluzione problemi

### La servlet non viene chiamata
- Verifica che il form abbia `action="salvaGiocatore"`
- Verifica che `@WebServlet("/salvaGiocatore")` sia presente in SalvaGiocatoreServlet
- Controlla i log di Tomcat per errori

### Errore di parsing della data
- Verifica che il campo sia `<input type="date" name="DATA_NASCITA" />`
- Il formato HTML5 date è sempre `yyyy-MM-dd`

### Nazione/Città non trovate
- Le query usano `LOWER(NOME) = LOWER(?)` per case-insensitive
- Verifica che i nomi nel database matchino (es: "Italia" vs "ITALIA")

### Dopo il salvataggio non vedo i dati
- La tabella in `giocatori.html` è statica (vuota)
- Devi ricaricare i dati dal database:
  - Opzione 1: Usa `/listaGiocatori` invece di `giocatori.html`
  - Opzione 2: Implementa caricamento AJAX (vedi Opzione B sopra)

## Prossimi passi

Se vuoi un'esperienza più moderna (AJAX senza page reload):

1. **Modificare SalvaGiocatoreServlet** per restituire JSON invece di redirect
2. **Aggiungere AJAX** nel JavaScript per inviare dati senza ricaricare la pagina
3. **Implementare DELETE** (attualmente TODO nella servlet)
4. **Implementare caricamento dinamico** della tabella dal database

Ma per ora, **la soluzione attuale funziona** con il pattern servlet classico!

