<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        import="java.util.List, java.util.ArrayList, java.util.Map, java.util.HashMap, java.sql.*, oracle.jdbc.driver.OracleDriver, java.text.SimpleDateFormat, java.util.Date"
%>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ZeroBSport - Giocatori</title>

    <link rel="stylesheet" href="css/stile.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />

    <script>
        function deleteRow(icon) {
            var row = icon.parentNode.parentNode;
            row.parentNode.removeChild(row);
        }
    </script>

    <%!
        String connectionString = "jdbc:oracle:thin:@franellucci-zb:1521:XE";

        private Object getValue(Map<String, Object> giocatore, String fieldName) {
            Object value = giocatore.get(fieldName);

            if (value == null) {
                return "";
            } else {
                return value;
            }
        }
    %>

    <%
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            Connection conn = null;
            boolean isErrore = false;
            String errorMessage = "";
            try {
                DriverManager.registerDriver(new OracleDriver());
                conn = DriverManager.getConnection(connectionString, "ZEROBSPORTS", "zrbpwdzerobsports");
                conn.setAutoCommit(false);

                String idRec = request.getParameter("ID_REC");
                String nome = request.getParameter("NOME");
                String cognome = request.getParameter("COGNOME");
                String dataNascitaStr = request.getParameter("DATA_DI_NASCITA");
                String alias = request.getParameter("ALIAS");
                String numeroMagliaStr = request.getParameter("NUMERO_MAGLIA_ABITUALE");
                String ruoloAbitualeId = request.getParameter("RUOLO_ABITUALE");
                String nazioneNascita = request.getParameter("NAZIONE_NASCITA");
                String cittaNascita = request.getParameter("CITTA_NASCITA");

                if (idRec == null || idRec.isEmpty()) {
                    // INSERT
                    PreparedStatement insertPersonaStmt = conn.prepareStatement("INSERT INTO ZS_PERSONE(ID_REC, NOME, COGNOME, DATA_DI_NASCITA, ID_NAZIONE_NASCITA_FK, ID_CITTA_NASCITA_FK) VALUES (SEQ_ZEROBSPORTS.NEXTVAL, ?, ?, ?, (SELECT ID_REC FROM ZS_NAZIONI WHERE LOWER(NOME) = LOWER(?)), (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)))");
                    insertPersonaStmt.setString(1, nome);
                    insertPersonaStmt.setString(2, cognome);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataNascita = sdf.parse(dataNascitaStr);
                    insertPersonaStmt.setDate(3, new java.sql.Date(dataNascita.getTime()));
                    insertPersonaStmt.setString(4, nazioneNascita);
                    insertPersonaStmt.setString(5, cittaNascita);

                    int results = insertPersonaStmt.executeUpdate();

                    if (results == 1) {
                        Statement getCurrVal = conn.createStatement();
                        ResultSet rsGetCurrVal = getCurrVal.executeQuery("SELECT SEQ_ZEROBSPORTS.currval FROM DUAL");
                        Integer idPersonaFK = null;
                        if (rsGetCurrVal.next()) {
                            idPersonaFK = rsGetCurrVal.getInt(1);
                        }
                        rsGetCurrVal.close();
                        getCurrVal.close();

                        PreparedStatement insertGiocatoreStmt = conn.prepareStatement("INSERT INTO ZS_GIOCATORI (ID_REC, ID_PERSONA_FK, ALIAS, ID_RUOLO_ABITUALE_FK, NUMERO_MAGLIA_ABITUALE) VALUES (SEQ_ZEROBSPORTS.nextval, ?, ?, ?, ?)");
                        insertGiocatoreStmt.setInt(1, idPersonaFK);
                        insertGiocatoreStmt.setString(2, alias);
                        if (ruoloAbitualeId != null && !ruoloAbitualeId.isEmpty()) {
                            insertGiocatoreStmt.setInt(3, Integer.parseInt(ruoloAbitualeId));
                        } else {
                            insertGiocatoreStmt.setNull(3, Types.INTEGER);
                        }
                        if (numeroMagliaStr != null && !numeroMagliaStr.isEmpty()) {
                            insertGiocatoreStmt.setInt(4, Integer.parseInt(numeroMagliaStr));
                        } else {
                            insertGiocatoreStmt.setNull(4, Types.INTEGER);
                        }

                        results = insertGiocatoreStmt.executeUpdate();

                        if (results == 1) {
                            conn.commit();
                        } else {
                            isErrore = true;
                            errorMessage = "Errore durante l'inserimento del giocatore";
                            conn.rollback();
                        }
                        insertGiocatoreStmt.close();
                    } else {
                        isErrore = true;
                        errorMessage = "Errore durante l'inserimento della persona";
                        conn.rollback();
                    }
                    insertPersonaStmt.close();
                } else {
                    // UPDATE
                    PreparedStatement updatePersonaStmt = conn.prepareStatement("UPDATE ZS_PERSONE SET NOME = ?, COGNOME = ?, DATA_DI_NASCITA = ?, ID_NAZIONE_NASCITA_FK = (SELECT ID_REC FROM ZS_NAZIONI WHERE LOWER(NOME) = LOWER(?)), ID_CITTA_NASCITA_FK = (SELECT ID_REC FROM ZS_CITTA WHERE LOWER(NOME) = LOWER(?)) WHERE ID_REC = ?");
                    updatePersonaStmt.setString(1, nome);
                    updatePersonaStmt.setString(2, cognome);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataNascita = sdf.parse(dataNascitaStr);
                    updatePersonaStmt.setDate(3, new java.sql.Date(dataNascita.getTime()));
                    updatePersonaStmt.setString(4, nazioneNascita);
                    updatePersonaStmt.setString(5, cittaNascita);
                    updatePersonaStmt.setInt(6, Integer.parseInt(idRec));
                    int results = updatePersonaStmt.executeUpdate();

                    if (results == 1) {
                        PreparedStatement updateGiocatoreStmt = conn.prepareStatement("UPDATE ZS_GIOCATORI SET ALIAS = ?, ID_RUOLO_ABITUALE_FK = ?, NUMERO_MAGLIA_ABITUALE = ? WHERE ID_PERSONA_FK = ?");
                        updateGiocatoreStmt.setString(1, alias);
                        if (ruoloAbitualeId != null && !ruoloAbitualeId.isEmpty()) {
                            updateGiocatoreStmt.setInt(2, Integer.parseInt(ruoloAbitualeId));
                        } else {
                            updateGiocatoreStmt.setNull(2, Types.INTEGER);
                        }
                        if (numeroMagliaStr != null && !numeroMagliaStr.isEmpty()) {
                            updateGiocatoreStmt.setInt(3, Integer.parseInt(numeroMagliaStr));
                        } else {
                            updateGiocatoreStmt.setNull(3, Types.INTEGER);
                        }
                        updateGiocatoreStmt.setInt(4, Integer.parseInt(idRec));
                        results = updateGiocatoreStmt.executeUpdate();

                        if (results == 1) {
                            conn.commit();
                        } else {
                            isErrore = true;
                            errorMessage = "Errore durante l'aggiornamento del giocatore";
                            conn.rollback();
                        }
                        updateGiocatoreStmt.close();
                    } else {
                        isErrore = true;
                        errorMessage = "Errore durante l'aggiornamento della persona";
                        conn.rollback();
                    }
                    updatePersonaStmt.close();
                }

            } catch (Exception e) {
                isErrore = true;
                errorMessage = e.getMessage();
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        // ignore
                    }
                }
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        // ignore
                    }
                }
            }

            if (isErrore) {
                // Gestione dell'errore, ad esempio mostrando un messaggio
                // Potresti anche fare un forward a una pagina di errore
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMessage);
                return;
            } else {
                response.sendRedirect("giocatori.jsp");
                return;
            }
        }

        List<Map<String, Object>> giocatori = new ArrayList<>();
        List<Map<String, Object>> ruoli = new ArrayList<>();
        Connection connection = null;
        PreparedStatement query = null;
        ResultSet rs = null;

        try{
            DriverManager.registerDriver(new OracleDriver());
            connection = DriverManager.getConnection(connectionString, "ZEROBSPORTS", "zrbpwdzerobsports");
            query = connection.prepareStatement("SELECT * FROM VIEW_GIOCATORI WHERE ROWNUM <= 100 ORDER BY COGNOME");
            rs = query.executeQuery();

            while(rs.next()){
                Map<String, Object> record = new HashMap<>();
                int colCount = rs.getMetaData().getColumnCount();
                for(int i = 1; i <= colCount; i++){
                    record.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
                }
                giocatori.add(record);
            }
            rs.close();
            query.close();

            query = connection.prepareStatement("SELECT * FROM ZS_RUOLI ORDER BY NOME");
            rs = query.executeQuery();
            while(rs.next()){
                Map<String, Object> record = new HashMap<>();
                record.put("ID_REC", rs.getInt("ID_REC"));
                record.put("NOME", rs.getString("NOME"));
                ruoli.add(record);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (query != null) query.close(); } catch (SQLException ignore) {}
            try { if (connection != null) connection.close(); } catch (SQLException ignore) {}
        }
    %>
</head>

<body>
<div class="wrapper">

    <div id="top">
        <img id="logo" src="pallone.png">
        <h1>ZeroBSport</h1>
        <p>Analytic Sports Data - Back Office</p>
    </div>
    <div class="wrapper">
        <div id="menubar">
            <ul id="menulist">
                <li class="menuitem active"><i class="fa fa-male"></i>Giocatori</li>
                <li class="menuitem"><i class="fa fa-users"></i>Squadre</li>
                <li class="menuitem"><i class="fa fa-globe"></i>Nazioni</li>
                <li class="menuitem"><i class="fa fa-trophy"></i>Competizioni</li>
                <li class="menuitem"><i class="fa fa-user-secret"></i>Tecnici</li>
            </ul>
        </div>

        <div id="main">
            <div id="mainHeader">
                <h1>Gestione Giocatori</h1>
                <div>
                    <input type="text" id="searchFieldGiocatori" />
                    <input type="button" value="Cerca" class="btn" onclick="ricercaGiocatori()" />
                </div>
            </div>
            <table id="tableGiocatori">
                <thead>
                <tr>
                    <th data-index="ID_REC">ID_REC</th>
                    <th data-index="NOME">Nome</th>
                    <th data-index="COGNOME">Cognome</th>
                    <th data-index="DATA_DI_NASCITA">Data Nascita</th>
                    <th data-index="ALIAS">Alias</th>
                    <th data-index="NUMERO_MAGLIA_ABITUALE">N° Maglia</th>
                    <th data-index="RUOLO_ABITUALE">Ruolo Abituale</th>
                    <th data-index="NAZIONE_NASCITA">Nazione Nascita</th>
                    <th data-index="CITTA_NASCITA">Città Nascita</th>
                    <th></th>
                </tr>
                </thead>
                <tbody id="data">
                <%
                    for (int i = 0; i < giocatori.size(); i++) {
                        Map<String, Object> giocatoreIterato = giocatori.get(i);
                %>
                <tr>
                    <td data-index="ID_REC"><%= getValue(giocatoreIterato, "ID_REC") %></td>
                    <td data-index="NOME"><%= getValue(giocatoreIterato, "NOME") %></td>
                    <td data-index="COGNOME"><%= getValue(giocatoreIterato, "COGNOME") %></td>
                    <td data-index="DATA_DI_NASCITA"><%= getValue(giocatoreIterato, "DATA_DI_NASCITA") %></td>
                    <td data-index="ALIAS"><%= getValue(giocatoreIterato, "ALIAS") %></td>
                    <td data-index="NUMERO_MAGLIA_ABITUALE"><%= getValue(giocatoreIterato, "NUMERO_MAGLIA_ABITUALE") %></td>
                    <td data-index="RUOLO_ABITUALE"><%= getValue(giocatoreIterato, "RUOLO_ABITUALE") %></td>
                    <td data-index="NAZIONE_NASCITA"><%= getValue(giocatoreIterato, "NAZIONE_NASCITA") %></td>
                    <td data-index="CITTA_NASCITA"><%= getValue(giocatoreIterato, "CITTA_NASCITA") %></td>
                    <td><i class="fa fa-trash" onclick="deleteRow(this)"></i></td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <div id="sidebar">
            <h3>Form Giocatore</h3>
            <form action="giocatori.jsp" method="POST" id="formGiocatori">
                <input type="hidden" name="operation" value="insertOrUpdate" />
                <label for="ID_REC">ID</label>
                <input type="text" id="ID_REC" name="ID_REC" readonly />
                <label for="NOME">Nome</label>
                <input type="text" id="NOME" name="NOME" />
                <label for="COGNOME">Cognome</label>
                <input type="text" id="COGNOME" name="COGNOME" />
                <label for="DATA_DI_NASCITA">Data Nascita:</label>
                <input type="date" id="DATA_DI_NASCITA" name="DATA_DI_NASCITA" />
                <label for="ALIAS">Alias:</label>
                <input type="text" id="ALIAS" name="ALIAS" />
                <label FOR="NUMERO_MAGLIA_ABITUALE">N° Maglia:</label>
                <input type="number" id="NUMERO_MAGLIA_ABITUALE" name="NUMERO_MAGLIA_ABITUALE" step="1">
                <label for="RUOLO_ABITUALE">Ruolo Abituale:</label>
                <select id="RUOLO_ABITUALE" name="RUOLO_ABITUALE">
                    <option value="">Seleziona un ruolo</option>
                    <% for (Map<String, Object> ruolo : ruoli) { %>
                        <option value="<%= ruolo.get("ID_REC") %>"><%= ruolo.get("NOME") %></option>
                    <% } %>
                </select>
                <label for="NAZIONE_NASCITA">Nazione Nascita:</label>
                <input type="text" id="NAZIONE_NASCITA" name="NAZIONE_NASCITA" />
                <label for="CITTA_NASCITA">Città Nascita:</label>
                <input type="text" id="CITTA_NASCITA" name="CITTA_NASCITA" />

                <input type="submit" value="Salva" class="btn" />
                <input type="reset" value="Cancella" class="btn" />
            </form>
        </div>
    </div>
    <div id="bottom">ZeroBSport - Copyright © 2018 ZeroB s.r.l. - All Rights Reserved</div>
</div>
<script src="js/giocatori.js"></script>
</body>

</html>