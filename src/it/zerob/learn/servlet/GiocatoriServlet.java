package it.zerob.learn.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import oracle.jdbc.driver.OracleDriver;

@WebServlet(name = "GiocatoriServlet", urlPatterns = {"/listaGiocatori"})
public class GiocatoriServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wr = response.getWriter();

        // INIZIO TRADUZIONE HTML
        wr.println("<!DOCTYPE html>");
        wr.println("<html>");

        wr.println("<head>");
        wr.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        wr.println("<title>ZeroBSport - Giocatori</title>");

        // Source di stile e icone
        wr.println("<link rel=\"stylesheet\" href=\"css/stile.css\" />");
        wr.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" />");

        // Script JS nell'head
        wr.println("<script type=\"text/javascript\">");
        wr.println("function formatDate(d) {");
        wr.println("    function pad(s) { return (s < 10) ? \"0\" + s : s; }");
        wr.println("    return [pad(d.getDate()), pad(d.getMonth() + 1), pad(d.getFullYear())].join(\"-\");");
        wr.println("}");
        wr.println("function isInt(value) {");
        wr.println("    try {");
        wr.println("        if (isNaN(value)) { return false; }");
        wr.println("        else { var x = parseFloat(value); return Math.floor(value) === x; }");
        wr.println("    } catch (e) { return false; }");
        wr.println("}");
        wr.println("</script>");
        wr.println("</head>");

        // Body con onload
        wr.println("<body\">");
        wr.println("<div class=\"wrapper\">");

        // Intestazione
        wr.println("<div id=\"top\">");
        wr.println("<img id=\"logo\" src=\"pallone.png\">");
        wr.println("<h1>ZeroBSport</h1>");
        wr.println("<p>Analytic Sports Data - Back Office</p>");
        wr.println("</div>");

        wr.println("<div class=\"wrapper\">");

        // Menu a sinistra
        wr.println("<div id=\"menubar\">");
        wr.println("<ul id=\"menulist\">");
        wr.println("<li class=\"menuitem active\"><i class=\"fa fa-male\"></i>Giocatori</li>");
        wr.println("<li class=\"menuitem\"><i class=\"fa fa-users\"></i>Squadre</li>");
        wr.println("<li class=\"menuitem\"><i class=\"fa fa-globe\"></i>Nazioni</li>");
        wr.println("<li class=\"menuitem\"><i class=\"fa fa-trophy\"></i>Competizioni</li>");
        wr.println("<li class=\"menuitem\"><i class=\"fa fa-user-secret\"></i>Tecnici</li>");
        wr.println("</ul>");
        wr.println("</div>");

        // Area principale
        wr.println("<div id=\"main\">");
        wr.println("<div id=\"mainHeader\">");
        wr.println("<h1>Gestione Giocatori</h1>");
        wr.println("<div>");
        wr.println("<input type=\"text\" id=\"searchFieldGiocatori\" />");
        wr.println("<input type=\"button\" value=\"Cerca\" class=\"btn\" onclick=\"ricercaGiocatori()\" />");
        wr.println("</div>");
        wr.println("</div>");

        // Tabella Giocatori
        wr.println("<table id=\"tableGiocatori\">");
        wr.println("<thead>");
        wr.println("<tr>");
        wr.println("<th data-index=\"ID_REC\">ID_REC</th>");
        wr.println("<th data-index=\"NOME\">Nome</th>");
        wr.println("<th data-index=\"COGNOME\">Cognome</th>");
        wr.println("<th data-index=\"DATA_DI_NASCITA\">Data Nascita</th>");
        wr.println("<th data-index=\"ALIAS\">Alias</th>");
        wr.println("<th data-index=\"numero_maglia_abituale\">N° Maglia</th>");
        wr.println("<th data-index=\"RUOLO_ABITUALE\">Ruolo Abituale</th>");
        wr.println("<th data-index=\"NAZIONE_NASCITA\">Nazione Nascita</th>");
        wr.println("<th data-index=\"CITTA_NASCITA\">Città Nascita</th>");
        wr.println("<th></th>");
        wr.println("</tr>");
        wr.println("</thead>");

        // --- INIZIO CODICE ESTRATTO DALL'IMMAGINE ---
        // Qui inseriamo il codice SQL esattamente nel punto del tbody come nell'immagine

        wr.println("<tbody id=\"data\">");

        try {
            DriverManager.registerDriver(new OracleDriver());
//            Connection connessione = DriverManager.getConnection("jdbc:oracle:thin:@172.30.1.110:1521:orcl", "ZEROBSPORTS", "zrbpwdzerobsports");
//            Connection connessione = DriverManager.getConnection("jdbc:oracle:thin:@//franellucci-zb:1521/XE", "ZEROBSPORTS", "zrbpwdzerobsports");
            Connection connessione = DriverManager.getConnection("jdbc:oracle:thin:@franellucci-zb:1521:XE", "ZEROBSPORTS", "zrbpwdzerobsports");
//            private static final String CONNECTION_STRING = "jdbc:oracle:thin:@//franellucci-zb:1521/XEPDB1";
//            private static final String DB_USER = "ZEROBSPORTS";
//            private static final String DB_PASSWORD = "zrbpwdzerobsports";

            String querySelectGiocatori = "SELECT GIOCATORI.ID_REC, " +
                    "GIOCATORI.NOME, " +
                    "GIOCATORI.COGNOME, " +
                    "TO_CHAR(GIOCATORI.DATA_DI_NASCITA, 'DD-MM-YYYY') AS DATA_NASCITA, " +
                    "GIOCATORI.ALIAS, " +
                    "NVL(TO_CHAR(GIOCATORI.NUMERO_MAGLIA_ABITUALE), ' ') AS NUM_MAGLIA, " +
                    "NVL(ZS_RUOLI.DESCRIZIONE, 'Sconosciuto') AS RUOLO, " +
                    "NVL(NAZIONI.NOME, 'Sconosciuta') AS NAZIONE_NASCITA, " +
                    "NVL(ZS_CITTA.NOME, 'Sconosciuta') AS CITTA_NASCITA " +
                    "FROM GIOCATORI LEFT JOIN NAZIONI ON GIOCATORI.ID_NAZIONE_NASCITA_FK = NAZIONI.ID_REC LEFT JOIN ZS_CITTA ON ZS_CITTA.ID_REC = GIOCATORI.ID_CITTA_NASCITA_FK " +
                    "LEFT JOIN ZS_RUOLI ON GIOCATORI.ID_RUOLO_ABITUALE_FK = ZS_RUOLI.ID_REC " +
                    "ORDER BY GIOCATORI.ID_REC DESC FETCH FIRST 100 ROWS ONLY";

            PreparedStatement query = connessione.prepareStatement(querySelectGiocatori);
            ResultSet queryResult = query.executeQuery();

            while (queryResult.next()) {

                wr.println("<tr>");
                wr.println("<td>"+queryResult.getInt("ID_REC")+"</td>");
                wr.println("<td>"+queryResult.getString("NOME")+"</td>");
                wr.println("<td>"+queryResult.getString("COGNOME")+"</td>");
                wr.println("<td>"+queryResult.getString("DATA_NASCITA")+"</td>");
                wr.println("<td>"+queryResult.getString("ALIAS")+"</td>");
                wr.println("<td>"+queryResult.getString("NUM_MAGLIA")+"</td>");
                wr.println("<td>"+queryResult.getString("RUOLO")+"</td>");
                wr.println("<td>"+queryResult.getString("CITTA_NASCITA")+"</td>");
                wr.println("<td>"+queryResult.getString("NAZIONE_NASCITA")+"</td>"); // Riga duplicata nell'originale
//                wr.println("<td>"+queryResult.getString("AZIONI")+"</td>"); // Riga duplicata nell'originale
                wr.println("</tr>");

            }
            queryResult.close();
            query.close();

            connessione.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // --- FINE CODICE ESTRATTO DALL'IMMAGINE ---

        wr.println("</tbody>");
        wr.println("</table>");
        wr.println("</div>"); // Chiude div main

        // Sidebar Form
        wr.println("<div id=\"sidebar\">");
        wr.println("<h3>Form Giocatore</h3>");
        wr.println("<form action=\"salvaGiocatore\" method=\"POST\" id=\"formGiocatori\">");
        wr.println("<input type=\"hidden\" name=\"operation\" value=\"insertOrUpdate\" />");
        wr.println("<label for=\"ID_REC\">ID</label>");
        wr.println("<input type=\"text\" name=\"ID_REC\" readonly />");
        wr.println("<label for=\"NOME\">Nome</label>");
        wr.println("<input type=\"text\" name=\"NOME\" />");
        wr.println("<label for=\"COGNOME\">Cognome</label>");
        wr.println("<input type=\"text\" name=\"COGNOME\" />");
        wr.println("<label for=\"DATA_NASCITA\">Data Nascita:</label>");
        wr.println("<input type=\"date\" name=\"DATA_NASCITA\" />");
        wr.println("<label for=\"ALIAS\">Alias:</label>");
        wr.println("<input type=\"text\" name=\"ALIAS\" />");
        wr.println("<label FOR=\"NUMERO_MAGLIA\">N° Maglia:</label>");
        wr.println("<input type=\"number\" name=\"NUMERO_MAGLIA\" step=\"1\">");
        wr.println("<label for=\"NAZIONE_NASCITA\">Nazione Nascita:</label>");
        wr.println("<input type=\"text\" name=\"NAZIONE_NASCITA\" />");
        wr.println("<label for=\"CITTA_NASCITA\">Città Nascita:</label>");
        wr.println("<input type=\"text\" name=\"CITTA_NASCITA\" />");

        wr.println("<input type=\"submit\" value=\"Salva\" class=\"btn\" />");
        wr.println("<input type=\"reset\" value=\"Cancella\" class=\"btn\" />");
        wr.println("</form>");
        wr.println("</div>"); // Chiude sidebar

        wr.println("</div>"); // Chiude wrapper interno
        wr.println("<div id=\"bottom\">ZeroBSport - Copyright © 2018 ZeroB s.r.l. - All Rights Reserved</div>");
        wr.println("</div>"); // Chiude wrapper esterno

        wr.println("<script type=\"text/javascript\" src=\"js/giocatori.js\"></script>");
        wr.println("</body>");
        wr.println("</html>");
    }
}