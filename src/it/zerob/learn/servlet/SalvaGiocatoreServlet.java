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

        try {
            connessione = DriverManager.getConnection("jdbc:oracle:thin:@franellucci-zb:1521:XE", "ZEROBSPORTS", "zrbpwdzerobsports");
            connessione.setAutoCommit(false);
            if("insertOrUpdate".equalsIgnoreCase(operazione)){
                // insert o update
                Integer idRec;
                String nome = request.getParameter("NOME");
                String cognome = request.getParameter("COGNOME");
                String dataNascita = request.getParameter("DATA_NASCITA");
                String alias = request.getParameter("ALIAS");
                String numeroMaglia = request.getParameter("NUMERO_MAGLIA");
                String nazioneDiNascita = request.getParameter("NAZIONE_NASCITA");
                String cittaDiNascita = request.getParameter("CITTA_NASCITA");
                // Ruolo rimosso completamente

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
                    // INSERT (SENZA RUOLO)
                    System.out.println("DEBUG: Esecuzione INSERT per nuovo giocatore");

                    String insertGiocatore = "INSERT INTO GIOCATORI (NOME, COGNOME, DATA_DI_NASCITA, ALIAS, NUMERO_MAGLIA_ABITUALE, ID_NAZIONE_NASCITA_FK, ID_CITTA_NASCITA_FK) " +
                            "VALUES (?, ?, ?, ?, ?, (SELECT ID_REC FROM NAZIONI WHERE LOWER(NOME) = LOWER(?)), (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)))";

                    System.out.println("DEBUG: Query INSERT: " + insertGiocatore);

                    PreparedStatement queryInsert = connessione.prepareStatement(insertGiocatore);
                    queryInsert.setString(1, nome);
                    queryInsert.setString(2, cognome);
                    queryInsert.setDate(3, new Date(sdf.parse(dataNascita).getTime()));
                    queryInsert.setString(4, alias);
                    if (numeroMaglia != null && !numeroMaglia.isEmpty()) {
                        queryInsert.setInt(5, Integer.parseInt(numeroMaglia));
                        System.out.println("DEBUG: Numero maglia: " + numeroMaglia);
                    } else {
                        queryInsert.setNull(5, Types.INTEGER);
                        System.out.println("DEBUG: Numero maglia: NULL");
                    }
                    queryInsert.setString(6, nazioneDiNascita);
                    queryInsert.setString(7, cittaDiNascita);

                    System.out.println("DEBUG: Esecuzione executeUpdate...");
                    int rowsInserted = queryInsert.executeUpdate();
                    System.out.println("DEBUG: Righe inserite: " + rowsInserted);

                    System.out.println("DEBUG: Esecuzione commit...");
                    connessione.commit();
                    System.out.println("DEBUG: COMMIT COMPLETATO CON SUCCESSO!");

                    queryInsert.close();
                }else{
                    // UPDATE (senza ruolo)
                    idRec = Integer.parseInt(request.getParameter("ID_REC"));
                    System.out.println("DEBUG: Aggiornamento giocatore ID: " + idRec);
                    String updateGiocatore = "UPDATE GIOCATORI SET NOME = ?, COGNOME = ?, DATA_DI_NASCITA = ?, ALIAS = ?, NUMERO_MAGLIA_ABITUALE = ?, " +
                            "ID_NAZIONE_NASCITA_FK = (SELECT ID_REC FROM NAZIONI WHERE LOWER(NOME) = LOWER(?)), " +
                            "ID_CITTA_NASCITA_FK = (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)) " +
                            "WHERE ID_REC = ?";
                    PreparedStatement queryUpdate = connessione.prepareStatement(updateGiocatore);
                    queryUpdate.setString(1, nome);
                    queryUpdate.setString(2, cognome);
                    queryUpdate.setDate(3, new Date(sdf.parse(dataNascita).getTime()));
                    queryUpdate.setString(4, alias);
                    if (numeroMaglia != null && !numeroMaglia.isEmpty()) {
                        queryUpdate.setInt(5, Integer.parseInt(numeroMaglia));
                    } else {
                        queryUpdate.setNull(5, Types.INTEGER);
                    }
                    queryUpdate.setString(6, nazioneDiNascita);
                    queryUpdate.setString(7, cittaDiNascita);
                    queryUpdate.setInt(8, idRec);

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

            // Reindirizza alla pagina giocatori dopo il salvataggio
            // Se usi giocatori.html (statico), reindirizza lì
            // Se usi listaGiocatori (servlet), reindirizza lì
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("listaGiocatori")) {
                response.sendRedirect("listaGiocatori");
            } else {
                response.sendRedirect("giocatori.html");
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