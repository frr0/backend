package it.zerob.learn.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/salvaGiocatore")
public class SalvaGiocatoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.salvaGiocatore(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.salvaGiocatore(req, resp);
    }

    protected void salvaGiocatore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operazione = request.getParameter("operation");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Connection connessione = null;
        boolean isErrore = false;
        String errorMessage = "";

        try {
            connessione = DriverManager.getConnection("jdbc:oracle:thin:@franellucci-zb:1521:XE", "ZEROBSPORTS", "zrbpwdzerobsports");
            connessione.setAutoCommit(false);
            if("insertOrUpdate".equalsIgnoreCase(operazione)){
                // insert o update
                Integer idRec;
                String nome = request.getParameter("NOME");
                String cognome = request.getParameter("COGNOME");
                String dataNascita = request.getParameter("DATA_DI_NASCITA");
                String alias = request.getParameter("ALIAS");
                String numeroMaglia = request.getParameter("NUMERO_MAGLIA_ABITUALE");
                String nazioneDiNascita = request.getParameter("NAZIONE_NASCITA");
                String cittaDiNascita = request.getParameter("CITTA_NASCITA");
                String ruoloAbituale = request.getParameter("RUOLO_ABITUALE");

                System.out.println("DEBUG: Parametri ricevuti - Nome: " + nome + ", Cognome: " + cognome + ", Nazione: " + nazioneDiNascita);

                // Validazione campi obbligatori
                if (nome == null || nome.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il campo Nome è obbligatorio");
                    return;
                }
                if (cognome == null || cognome.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il campo Cognome è obbligatorio");
                    return;
                }
                if (dataNascita == null || dataNascita.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il campo Data Nascita è obbligatorio");
                    return;
                }
                if (nazioneDiNascita == null || nazioneDiNascita.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il campo Nazione Nascita è obbligatorio");
                    return;
                }

                if (request.getParameter("ID_REC") == null || "".equalsIgnoreCase(request.getParameter("ID_REC"))) {
                    // INSERT
                    PreparedStatement insertPersonaStmt = connessione.prepareStatement("INSERT INTO ZS_PERSONE(ID_REC, NOME, COGNOME, DATA_DI_NASCITA, ID_NAZIONE_NASCITA_FK, ID_CITTA_NASCITA_FK) VALUES (SEQ_ZEROBSPORTS.NEXTVAL, ?, ?, ?, (SELECT ID_REC FROM ZS_NAZIONI WHERE LOWER(NOME) = LOWER(?)), (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)))");
                    insertPersonaStmt.setString(1, nome);
                    insertPersonaStmt.setString(2, cognome);
                    insertPersonaStmt.setDate(3, new java.sql.Date(sdf.parse(dataNascita).getTime()));
                    insertPersonaStmt.setString(4, nazioneDiNascita.toLowerCase());
                    insertPersonaStmt.setString(5, cittaDiNascita.toLowerCase());

                    int results = insertPersonaStmt.executeUpdate();

                    if (results == 1) {
                        // 1. Recupero dell'ID generato (curval)
                        Statement getCurrVal = connessione.prepareStatement("SELECT SEQ_ZEROBSPORTS.currval FROM DUAL");
                        ResultSet rsGetCurrVal = ((PreparedStatement) getCurrVal).executeQuery();

                        Integer idPersonaFK = null;
                        if (rsGetCurrVal.next()) {
                            idPersonaFK = rsGetCurrVal.getInt(1);
                        }
                        getCurrVal.close();

                        // 2. Preparazione dell'inserimento in ZS_GIOCATORI
                        PreparedStatement insertGiocatoreStmt = connessione.prepareStatement("INSERT INTO ZS_GIOCATORI (ID_REC, ID_PERSONA_FK, ALIAS, ID_RUOLO_ABITUALE_FK, NUMERO_MAGLIA_ABITUALE) " +
                                "VALUES (SEQ_ZEROBSPORTS.nextval, ?, ?, ?, ?)");

                        insertGiocatoreStmt.setInt(1, idPersonaFK);
                        insertGiocatoreStmt.setString(2, alias);
                        insertGiocatoreStmt.setInt(3, Integer.parseInt(ruoloAbituale));
                        insertGiocatoreStmt.setInt(4, Integer.parseInt(numeroMaglia));

                        results = insertGiocatoreStmt.executeUpdate();

                        // 3. Gestione della transazione
                        if (results == 1) {
                            connessione.commit();
                            insertGiocatoreStmt.close();
                            insertPersonaStmt.close();
                        } else {
                            isErrore = true;
                            errorMessage = "Errore durante l'inserimento del giocatore";
                            connessione.rollback();
                            insertGiocatoreStmt.close();
                            insertPersonaStmt.close();
                        }
                    } else { // if (results == 1) fallisce per insertPersonaStmt
                        isErrore = true;
                        errorMessage = "Errore durante l'inserimento del giocatore";
                        connessione.rollback();
                    }
                }else{
                    // UPDATE
                    idRec = Integer.parseInt(request.getParameter("ID_REC"));
                    System.out.println("DEBUG: Aggiornamento giocatore ID: " + idRec);
                    String updateGiocatore = "UPDATE GIOCATORI SET NOME = ?, COGNOME = ?, DATA_DI_NASCITA = ?, ALIAS = ?, NUMERO_MAGLIA_ABITUALE = ?, " +
                            "ID_NAZIONE_NASCITA_FK = (SELECT ID_REC FROM NAZIONI WHERE LOWER(NOME) = LOWER(?)), " +
                            "ID_CITTA_NASCITA_FK = (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)), " +
                            "RUOLO_ABITUALE = ? " +
                            "WHERE ID_REC = ?";
                    PreparedStatement queryUpdate = connessione.prepareStatement(updateGiocatore);
                    queryUpdate.setString(1, nome);
                    queryUpdate.setString(2, cognome);
                    queryUpdate.setDate(3, new java.sql.Date(sdf.parse(dataNascita).getTime()));
                    queryUpdate.setString(4, alias);
                    if (numeroMaglia != null && !numeroMaglia.isEmpty()) {
                        queryUpdate.setInt(5, Integer.parseInt(numeroMaglia));
                    } else {
                        queryUpdate.setNull(5, Types.INTEGER);
                    }
                    queryUpdate.setString(6, nazioneDiNascita);
                    queryUpdate.setString(7, cittaDiNascita);
                    queryUpdate.setString(8, ruoloAbituale);
                    queryUpdate.setInt(9, idRec);

                    queryUpdate.executeUpdate();
                    connessione.commit();
                    queryUpdate.close();
                    System.out.println("DEBUG: UPDATE completato e committato con successo");
                }
            }else{
                //delete
                // TODO: implementare delete
            }
            connessione.close();

            if (isErrore) {
                throw new ServletException(errorMessage);
            }

            // Reindirizza alla pagina giocatori dopo il salvataggio
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("listaGiocatori")) {
                response.sendRedirect("listaGiocatori");
            } else {
                response.sendRedirect("giocatori.jsp");
            }

        } catch (SQLException e) {
            System.err.println("ERRORE SQL: " + e.getMessage());
            e.printStackTrace();
            if (connessione != null) {
                try {
                    connessione.rollback();
                    System.out.println("DEBUG: Rollback eseguito");
                } catch (SQLException ex) {
                    System.err.println("ERRORE durante rollback: " + ex.getMessage());
                }
            }

            // Gestione errore più user-friendly
            if (e.getMessage().contains("GIOCATORE_NAZIONE_FK")) {
                throw new RuntimeException("Errore: La nazione '" + request.getParameter("NAZIONE_NASCITA") + "' non esiste nel database.", e);
            } else if (e.getMessage().contains("GIOCATORE_CITTA_FK")) {
                throw new RuntimeException("Errore: La città '" + request.getParameter("CITTA_NASCITA") + "' non esiste nel database.", e);
            } else {
                throw new RuntimeException("Errore durante il salvataggio: " + e.getMessage(), e);
            }
        } catch (ParseException e) {
            System.err.println("ERRORE PARSING DATA: " + e.getMessage());
            throw new RuntimeException("Errore: formato data non valido. Usa il formato YYYY-MM-DD", e);
        }

    }
}