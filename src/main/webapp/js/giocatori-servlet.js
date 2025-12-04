// JavaScript MINIMALE per supportare le servlet
// Questo file NON intercetta il submit del form, lascia che vada alla servlet

// Funzione di ricerca giocatori nella tabella
function ricercaGiocatori(){
    var valoreDaRicercare = document.getElementById("searchFieldGiocatori").value.toLowerCase();
    var rows = document.getElementById("tableGiocatori").tBodies[0].querySelectorAll("tr");

    for(var i = 0; i < rows.length; i++){
        var rowContainsSearchTerm = false;
        var cells = rows[i].querySelectorAll("td");
        for(var j = 0; j < cells.length; j++){
            if(cells[j].innerText.toLowerCase().indexOf(valoreDaRicercare) !== -1){
                rowContainsSearchTerm = true;
                break;
            }
        }

        if(!valoreDaRicercare || rowContainsSearchTerm){
            rows[i].style.display = "";
        } else {
            rows[i].style.display = "none";
        }
    }
}

// ========== NUOVA FUNZIONALITÀ: EDITING ==========

// Funzione per popolare il form quando si clicca su una riga
function handleRowClick(event) {
    var target = event.target;

    // Se ha cliccato sull'icona del cestino, non fare nulla (è per eliminare)
    if (target.tagName === 'I' || target.closest('i')) {
        return;
    }

    // Trova la riga (TR)
    var row = target.tagName === 'TR' ? target : target.closest('tr');
    if (!row) return;

    // Rimuovi evidenziazione dalle altre righe
    var allRows = document.getElementById("tableGiocatori").tBodies[0].querySelectorAll("tr");
    for (var i = 0; i < allRows.length; i++) {
        allRows[i].classList.remove("selected");
    }

    // Evidenzia la riga selezionata
    row.classList.add("selected");

    // Estrai i dati dalle celle
    var cells = row.querySelectorAll("td");
    var table = document.getElementById("tableGiocatori");
    var headers = table.querySelectorAll("thead th");

    // Mappa dei dati
    var data = {};
    for (var i = 0; i < headers.length && i < cells.length; i++) {
        var fieldName = headers[i].getAttribute("data-index");
        if (fieldName) {
            data[fieldName] = cells[i].innerText.trim();
        }
    }

    console.log("DEBUG: Dati estratti dalla riga:", data);

    // Popola il form
    var form = document.getElementById("formGiocatori");
    if (!form) {
        console.error("Form non trovato!");
        return;
    }

    // ID_REC
    if (form.ID_REC) {
        form.ID_REC.value = data.ID_REC || "";
    }

    // NOME
    if (form.NOME) {
        form.NOME.value = data.NOME || "";
    }

    // COGNOME
    if (form.COGNOME) {
        form.COGNOME.value = data.COGNOME || "";
    }

    // DATA_DI_NASCITA - Converti da DD-MM-YYYY a YYYY-MM-DD
    if (form.DATA_DI_NASCITA && data.DATA_DI_NASCITA) {
        var dataParts = data.DATA_DI_NASCITA.split("-");
        if (dataParts.length === 3) {
            var day = dataParts[0];
            var month = dataParts[1];
            var year = dataParts[2];
            // Gestisci anno a 2 cifre
            if (year.length === 2) {
                year = (parseInt(year) < 30 ? "20" : "19") + year;
            }
            form.DATA_DI_NASCITA.value = year + "-" + month + "-" + day;
        }
    }

    // ALIAS
    if (form.ALIAS) {
        form.ALIAS.value = data.ALIAS || "";
    }

    // NUMERO_MAGLIA_ABITUALE
    if (form.NUMERO_MAGLIA_ABITUALE) {
        var numMaglia = data.NUMERO_MAGLIA_ABITUALE || "";
        // Rimuovi spazi e controlla se è un numero
        numMaglia = numMaglia.trim();
        if (numMaglia && !isNaN(numMaglia)) {
            form.NUMERO_MAGLIA_ABITUALE.value = numMaglia;
        } else {
            form.NUMERO_MAGLIA_ABITUALE.value = "";
        }
    }

    // NAZIONE_NASCITA
    if (form.NAZIONE_NASCITA) {
        form.NAZIONE_NASCITA.value = data.NAZIONE_NASCITA || "";
    }

    // CITTA_NASCITA
    if (form.CITTA_NASCITA) {
        form.CITTA_NASCITA.value = data.CITTA_NASCITA || "";
    }

    console.log("DEBUG: Form popolato con successo");
}

// Aggiungi event listener alla tabella quando la pagina è caricata
document.addEventListener("DOMContentLoaded", function() {
    var tbody = document.querySelector("#tableGiocatori tbody");
    if (tbody) {
        tbody.addEventListener("click", handleRowClick);
        console.log("DEBUG: Event listener aggiunto alla tabella");
    } else {
        console.error("Tbody della tabella non trovato!");
    }
});

// Validazione base del form (opzionale)
// Questa funzione viene chiamata quando il form è inviato
function validateFormGiocatori(event) {
    var form = document.getElementById("formGiocatori");
    var isValid = true;
    var errors = [];

    console.log("DEBUG: Validazione form in corso...", form);

    // Valida Nome
    if (!form.NOME.value || form.NOME.value.trim() === "") {
        errors.push("Il campo Nome è obbligatorio");
        isValid = false;
    }

    // Valida Cognome
    if (!form.COGNOME.value || form.COGNOME.value.trim() === "") {
        errors.push("Il campo Cognome è obbligatorio");
        isValid = false;
    }

    // Valida Data Nascita
    if (!form.DATA_DI_NASCITA.value || form.DATA_DI_NASCITA.value.trim() === "") {
        errors.push("Il campo Data Nascita è obbligatorio");
        isValid = false;
    }

    // Valida Nazione Nascita
    if (!form.NAZIONE_NASCITA.value || form.NAZIONE_NASCITA.value.trim() === "") {
        errors.push("Il campo Nazione Nascita è obbligatorio");
        isValid = false;
    }

    // Valida Numero Maglia (se presente)
    if (form.NUMERO_MAGLIA_ABITUALE.value && form.NUMERO_MAGLIA_ABITUALE.value !== "") {
        var numeroMaglia = parseInt(form.NUMERO_MAGLIA_ABITUALE.value);
        if (isNaN(numeroMaglia) || numeroMaglia < 1 || numeroMaglia > 99) {
            errors.push("Il Numero Maglia deve essere tra 1 e 99");
            isValid = false;
        }
    }

    if (!isValid) {
        event.preventDefault(); // Blocca il submit solo se ci sono errori
        console.error("DEBUG: Errori di validazione:", errors);
        alert("Errori di validazione:\n" + errors.join("\n"));
    } else {
        console.log("DEBUG: Validazione completata con successo. Submitting form to: ", form.action);
    }

    return isValid;
}

// Aggiungi event listener al form quando la pagina è caricata
document.addEventListener("DOMContentLoaded", function() {
    var form = document.getElementById("formGiocatori");
    if (form) {
        form.addEventListener("submit", validateFormGiocatori);
    }
});

