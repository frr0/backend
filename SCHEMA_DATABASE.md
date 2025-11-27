# 📋 Riferimento Rapido - Schema Database

Questo documento contiene i nomi corretti di tabelle e colonne basati sul codice funzionante di **GiocatoriServlet.java**.

## 🗂️ Tabelle del Database

| Tabella | Descrizione |
|---------|-------------|
| `GIOCATORI` | Tabella principale dei giocatori |
| `NAZIONI` | Tabella delle nazioni |
| `ZS_CITTA` | Tabella delle città |
| `ZS_RUOLI` | Tabella dei ruoli (Portiere, Difensore, ecc.) |

## 📊 Struttura Tabella GIOCATORI

| Nome Colonna | Tipo | Nullable | Descrizione |
|--------------|------|----------|-------------|
| `ID_REC` | NUMBER | NO | Primary Key |
| `NOME` | VARCHAR2 | NO | Nome giocatore |
| `COGNOME` | VARCHAR2 | NO | Cognome giocatore |
| `DATA_DI_NASCITA` | DATE | NO | Data di nascita |
| `ALIAS` | VARCHAR2 | SI | Soprannome |
| `NUMERO_MAGLIA_ABITUALE` | NUMBER | SI | Numero maglia abituale (1-99) |
| `ID_NAZIONE_NASCITA_FK` | NUMBER | NO | Foreign Key → NAZIONI.ID_REC |
| `ID_CITTA_NASCITA_FK` | NUMBER | SI | Foreign Key → ZS_CITTA.ID_REC |
| `ID_RUOLO_ABITUALE_FK` | NUMBER | SI | Foreign Key → ZS_RUOLI.ID_REC |

## 📊 Struttura Tabella NAZIONI

| Nome Colonna | Tipo | Descrizione |
|--------------|------|-------------|
| `ID_REC` | NUMBER | Primary Key |
| `NOME` | VARCHAR2 | Nome nazione (es: Italia, Francia) |

## 📊 Struttura Tabella ZS_CITTA

| Nome Colonna | Tipo | Descrizione |
|--------------|------|-------------|
| `ID_REC` | NUMBER | Primary Key |
| `NOME` | VARCHAR2 | Nome città (es: Roma, Milano) |

## 📊 Struttura Tabella ZS_RUOLI

| Nome Colonna | Tipo | Descrizione |
|--------------|------|-------------|
| `ID_REC` | NUMBER | Primary Key |
| `DESCRIZIONE` | VARCHAR2 | Descrizione ruolo |

### Valori Ruoli (da codice form):
| ID_REC | DESCRIZIONE |
|--------|-------------|
| 1 | Portiere |
| 2 | Difensore |
| 3 | Centrocampista |
| 4 | Attaccante |

## 🔗 Query SQL di Riferimento

### SELECT Giocatori (da GiocatoriServlet):
```sql
SELECT 
    GIOCATORI.ID_REC, 
    GIOCATORI.NOME, 
    GIOCATORI.COGNOME, 
    TO_CHAR(GIOCATORI.DATA_DI_NASCITA, 'DD-MM-YYYY') AS DATA_NASCITA, 
    GIOCATORI.ALIAS, 
    NVL(TO_CHAR(GIOCATORI.NUMERO_MAGLIA_ABITUALE), ' ') AS NUM_MAGLIA, 
    NVL(ZS_RUOLI.DESCRIZIONE, 'Sconosciuto') AS RUOLO, 
    NVL(NAZIONI.NOME, 'Sconosciuta') AS NAZIONE_NASCITA, 
    NVL(ZS_CITTA.NOME, 'Sconosciuta') AS CITTA_NASCITA 
FROM GIOCATORI 
LEFT JOIN NAZIONI ON GIOCATORI.ID_NAZIONE_NASCITA_FK = NAZIONI.ID_REC 
LEFT JOIN ZS_CITTA ON ZS_CITTA.ID_REC = GIOCATORI.ID_CITTA_NASCITA_FK 
LEFT JOIN ZS_RUOLI ON GIOCATORI.ID_RUOLO_ABITUALE_FK = ZS_RUOLI.ID_REC 
WHERE ROWNUM <= 100 
ORDER BY GIOCATORI.COGNOME
```

### INSERT Giocatore (da SalvaGiocatoreServlet):
```sql
INSERT INTO GIOCATORI (
    NOME, 
    COGNOME, 
    DATA_DI_NASCITA, 
    ALIAS, 
    NUMERO_MAGLIA_ABITUALE, 
    ID_NAZIONE_NASCITA_FK, 
    ID_CITTA_NASCITA_FK, 
    ID_RUOLO_ABITUALE_FK
) VALUES (
    ?, 
    ?, 
    ?, 
    ?, 
    ?, 
    (SELECT ID_REC FROM NAZIONI WHERE LOWER(NOME) = LOWER(?)), 
    (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)), 
    ?
)
```

### UPDATE Giocatore (da SalvaGiocatoreServlet):
```sql
UPDATE GIOCATORI SET 
    NOME = ?, 
    COGNOME = ?, 
    DATA_DI_NASCITA = ?, 
    ALIAS = ?, 
    NUMERO_MAGLIA_ABITUALE = ?, 
    ID_NAZIONE_NASCITA_FK = (SELECT ID_REC FROM NAZIONI WHERE LOWER(NOME) = LOWER(?)), 
    ID_CITTA_NASCITA_FK = (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)), 
    ID_RUOLO_ABITUALE_FK = ? 
WHERE ID_REC = ?
```

## 🎯 Parametri Form HTML

| Nome Input HTML | Colonna Database | Tipo Java | Obbligatorio |
|-----------------|------------------|-----------|--------------|
| `ID_REC` | ID_REC | Integer | - (readonly) |
| `NOME` | NOME | String | ✅ SI |
| `COGNOME` | COGNOME | String | ✅ SI |
| `DATA_NASCITA` | DATA_DI_NASCITA | Date | ✅ SI |
| `ALIAS` | ALIAS | String | NO |
| `NUMERO_MAGLIA` | NUMERO_MAGLIA_ABITUALE | Integer | NO |
| `RUOLO_ABITUALE` | ID_RUOLO_ABITUALE_FK | Integer | NO |
| `NAZIONE_NASCITA` | ID_NAZIONE_NASCITA_FK | String→ID | ✅ SI |
| `CITTA_NASCITA` | ID_CITTA_NASCITA_FK | String→ID | NO |

**Nota**: NAZIONE_NASCITA e CITTA_NASCITA vengono inviati come stringhe (nomi), ma la servlet li converte in ID tramite subquery.

## ⚠️ Note Importanti

### Case Sensitivity
Le query usano `LOWER()` per confronti case-insensitive:
```sql
WHERE LOWER(NOME) = LOWER(?)
```
Quindi "Italia", "ITALIA", "italia" funzionano tutti.

### Valori NULL
- Se NUMERO_MAGLIA è vuoto → viene inserito NULL
- Se RUOLO_ABITUALE non è selezionato → viene inserito NULL
- Se CITTA_NASCITA è vuota → viene inserito NULL

### Foreign Keys
Le subquery cercano l'ID tramite il nome:
- Se la nazione "Italia" non esiste in NAZIONI → restituisce NULL → errore se campo NOT NULL
- Stessa cosa per le città

## 🔧 Query di Verifica

### Verifica esistenza nazioni:
```sql
SELECT ID_REC, NOME FROM NAZIONI ORDER BY NOME;
```

### Verifica esistenza città:
```sql
SELECT ID_REC, NOME FROM ZS_CITTA ORDER BY NOME;
```

### Verifica esistenza ruoli:
```sql
SELECT ID_REC, DESCRIZIONE FROM ZS_RUOLI ORDER BY ID_REC;
```

### Inserisci nazioni di test (se non esistono):
```sql
INSERT INTO NAZIONI (NOME) VALUES ('Italia');
INSERT INTO NAZIONI (NOME) VALUES ('Francia');
INSERT INTO NAZIONI (NOME) VALUES ('Spagna');
INSERT INTO NAZIONI (NOME) VALUES ('Germania');
COMMIT;
```

### Inserisci città di test (se non esistono):
```sql
INSERT INTO ZS_CITTA (NOME) VALUES ('Roma');
INSERT INTO ZS_CITTA (NOME) VALUES ('Milano');
INSERT INTO ZS_CITTA (NOME) VALUES ('Napoli');
COMMIT;
```

### Inserisci ruoli di test (se non esistono):
```sql
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (1, 'Portiere');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (2, 'Difensore');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (3, 'Centrocampista');
INSERT INTO ZS_RUOLI (ID_REC, DESCRIZIONE) VALUES (4, 'Attaccante');
COMMIT;
```

## 📅 Formato Date

### Java → Database:
```java
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Date dataSql = new Date(sdf.parse(dataNascita).getTime());
```

### Database → HTML:
```sql
TO_CHAR(DATA_DI_NASCITA, 'DD-MM-YYYY')  -- Output: 15-01-2000
```

### HTML5 Input Date:
```html
<input type="date" name="DATA_NASCITA" />
```
Il browser invia sempre nel formato: `yyyy-MM-dd` (es: 2000-01-15)

---

**Ultima modifica**: Basato sul codice funzionante di GiocatoriServlet.java  
**Versione**: 1.0

