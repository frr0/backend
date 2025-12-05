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
                // Estrai parametri
                String nome = request.getParameter("NOME");
                String cognome = request.getParameter("COGNOME");
                String dataNascita = request.getParameter("DATA_DI_NASCITA");
                String alias = request.getParameter("ALIAS");
                String numeroMaglia = request.getParameter("NUMERO_MAGLIA_ABITUALE");
                String ruoloAbituale = request.getParameter("RUOLO_ABITUALE");
                String nazioneDiNascita = request.getParameter("NAZIONE_NASCITA");
                String cittaDiNascita = request.getParameter("CITTA_NASCITA");
                String idRecParam = request.getParameter("ID_REC");

                System.out.println("DEBUG: Parametri ricevuti - Nome: " + nome + ", Cognome: " + cognome + ", Data: " + dataNascita);

                // Validazione campi obbligatori
                if (nome == null || nome.trim().isEmpty()) {
                    throw new RuntimeException("Il campo Nome è obbligatorio");
                }
                if (cognome == null || cognome.trim().isEmpty()) {
                    throw new RuntimeException("Il campo Cognome è obbligatorio");
                }
                if (dataNascita == null || dataNascita.trim().isEmpty()) {
                    throw new RuntimeException("Il campo Data Nascita è obbligatorio");
                }
                if (nazioneDiNascita == null || nazioneDiNascita.trim().isEmpty()) {
                    throw new RuntimeException("Il campo Nazione Nascita è obbligatorio");
                }

                if (idRecParam == null || idRecParam.trim().isEmpty()) {
                    // INSERT
                    String insertGiocatore = "INSERT INTO VIEW_GIOCATORI (NOME, COGNOME, DATA_NASCITA, ALIAS, NUMERO_MAGLIA_ABITUALE, RUOLO_ABITUALE, NAZIONE_NASCITA, CITTA_NASCITA) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = connessione.prepareStatement(insertGiocatore);
                    pstmt.setString(1, nome);
                    pstmt.setString(2, cognome);
                    pstmt.setDate(3, new java.sql.Date(sdf.parse(dataNascita).getTime()));
                    pstmt.setString(4, alias != null ? alias : "");
                    if (numeroMaglia != null && !numeroMaglia.trim().isEmpty()) {
                        pstmt.setInt(5, Integer.parseInt(numeroMaglia));
                    } else {
                        pstmt.setNull(5, Types.INTEGER);
                    }
                    pstmt.setString(6, ruoloAbituale != null ? ruoloAbituale : "");
                    pstmt.setString(7, nazioneDiNascita);
                    pstmt.setString(8, cittaDiNascita != null ? cittaDiNascita : "");

                    int results = pstmt.executeUpdate();
                    pstmt.close();

                    if (results == 1) {
                        connessione.commit();
                        System.out.println("DEBUG: INSERT completato con successo");
                    } else {
                        connessione.rollback();
                        throw new RuntimeException("Errore durante l'inserimento del giocatore");
                    }
                } else {
                    // UPDATE
                    Integer idRec = Integer.parseInt(idRecParam);
                    System.out.println("DEBUG: Aggiornamento giocatore ID: " + idRec);

                    String updateGiocatore = "UPDATE VIEW_GIOCATORI SET NOME = ?, COGNOME = ?, DATA_NASCITA = ?, ALIAS = ?, " +
                            "NUMERO_MAGLIA_ABITUALE = ?, RUOLO_ABITUALE = ?, NAZIONE_NASCITA = ?, CITTA_NASCITA = ? WHERE ID_REC = ?";
                    PreparedStatement pstmt = connessione.prepareStatement(updateGiocatore);
                    pstmt.setString(1, nome);
                    pstmt.setString(2, cognome);
                    pstmt.setDate(3, new java.sql.Date(sdf.parse(dataNascita).getTime()));
                    pstmt.setString(4, alias != null ? alias : "");
                    if (numeroMaglia != null && !numeroMaglia.trim().isEmpty()) {
                        pstmt.setInt(5, Integer.parseInt(numeroMaglia));
                    } else {
                        pstmt.setNull(5, Types.INTEGER);
                    }
                    pstmt.setString(6, ruoloAbituale != null ? ruoloAbituale : "");
                    pstmt.setString(7, nazioneDiNascita);
                    pstmt.setString(8, cittaDiNascita != null ? cittaDiNascita : "");
                    pstmt.setInt(9, idRec);

                    int results = pstmt.executeUpdate();
                    pstmt.close();

                    if (results >= 0) {
                        connessione.commit();
                        System.out.println("DEBUG: UPDATE completato con successo");
                    } else {
                        connessione.rollback();
                        throw new RuntimeException("Errore durante l'aggiornamento del giocatore");
                    }
                }
            } else {
                //delete
                // TODO: implementare delete
            }

            connessione.close();

            // Reindirizza alla pagina giocatori dopo il salvataggio
            response.sendRedirect(request.getContextPath() + "/listaGiocatori");

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
            if (e.getMessage().contains("nazione")) {
                throw new RuntimeException("Errore: La nazione '" + request.getParameter("NAZIONE_NASCITA") + "' non esiste nel database.", e);
            } else if (e.getMessage().contains("citta")) {
                throw new RuntimeException("Errore: La città '" + request.getParameter("CITTA_NASCITA") + "' non esiste nel database.", e);
            } else {
                throw new RuntimeException("Errore SQL durante il salvataggio: " + e.getMessage(), e);
            }
        } catch (ParseException e) {
            System.err.println("ERRORE PARSING DATA: " + e.getMessage());
            throw new RuntimeException("Errore: formato data non valido. Usa il formato YYYY-MM-DD", e);
        } finally {
            if (connessione != null) {
                try {
                    connessione.close();
                } catch (SQLException e) {
                    System.err.println("Errore chiusura connessione: " + e.getMessage());
                }
            }
        }

    }
}