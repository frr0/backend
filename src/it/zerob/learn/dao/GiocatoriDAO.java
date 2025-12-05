package it.zerob.learn.dao;

import it.zerob.learn.model.Giocatore;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GiocatoriDAO {

    private static final String CONNECTION_STRING = "jdbc:oracle:thin:@franellucci-zb:1521:XE";
    private static final String DB_USER = "ZEROBSPORTS";
    private static final String DB_PASSWORD = "zrbpwdzerobsports";

    public List<Giocatore> getAllGiocatoriSortByidRecDesc() throws SQLException {

        List<Giocatore> giocatori = new ArrayList<>();
        String sql = "SELECT * FROM VIEW_GIOCATORI WHERE ROWNUM <= 100 ORDER BY COGNOME";

        System.out.println("DEBUG GiocatoriDAO: Connessione al database...");

        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            // Driver already registered, ignore
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
             PreparedStatement query = connection.prepareStatement(sql);
             ResultSet rs = query.executeQuery()) {

            System.out.println("DEBUG GiocatoriDAO: Connessione OK, esecuzione query: " + sql);

            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();

            System.out.println("DEBUG GiocatoriDAO: Numero colonne: " + columnCount);

            while (rs.next()) {
                Giocatore giocatoreIterato = new Giocatore();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = md.getColumnName(i);
                    switch (columnName) {
                        case "ID_REC":
                            giocatoreIterato.setIdRec(rs.getInt(i));
                            break;
                        case "NOME":
                            giocatoreIterato.setNome(rs.getString(i));
                            break;
                        case "COGNOME":
                            giocatoreIterato.setCognome(rs.getString(i));
                            break;
                        case "DATA_NASCITA":
                            giocatoreIterato.setDataNascita(rs.getString(i));
                            break;
                        case "ALIAS":
                            giocatoreIterato.setAlias(rs.getString(i));
                            break;
                        case "NUMERO_MAGLIA_ABITUALE":
                            if (rs.getObject(i) != null) {
                                giocatoreIterato.setNumeroMagliaAbituale(rs.getBigDecimal(i));
                            }
                            break;
                        case "RUOLO":
                            giocatoreIterato.setRuoloAbituale(rs.getString(i));
                            break;
                        case "NAZIONE_NASCITA":
                            giocatoreIterato.setNazioneNascita(rs.getString(i));
                            break;
                        case "CITTA_NASCITA":
                            giocatoreIterato.setCittaNascita(rs.getString(i));
                            break;
                    }
                }
                giocatori.add(giocatoreIterato);
            }
        }
        return giocatori;
    }

    public Giocatore insertGiocatore(Giocatore giocatore) throws SQLException {
        String sql = "INSERT INTO VIEW_GIOCATORI (NOME, COGNOME, DATA_NASCITA, ALIAS, NUMERO_MAGLIA_ABITUALE, NAZIONE_NASCITA, CITTA_NASCITA, RUOLO) " +
                     "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, ?)";

        System.out.println("DEBUG GiocatoriDAO: Inserimento nuovo giocatore...");

        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            // Driver already registered, ignore
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql, new String[]{"ID_REC"})) {

            pstmt.setString(1, giocatore.getNome());
            pstmt.setString(2, giocatore.getCognome());
            pstmt.setString(3, giocatore.getDataNascita());
            pstmt.setString(4, giocatore.getAlias());

            if (giocatore.getNumeroMagliaAbituale() != null) {
                pstmt.setBigDecimal(5, giocatore.getNumeroMagliaAbituale());
            } else {
                pstmt.setNull(5, java.sql.Types.NUMERIC);
            }

            pstmt.setString(6, giocatore.getNazioneNascita());
            pstmt.setString(7, giocatore.getCittaNascita());
            pstmt.setString(8, giocatore.getRuoloAbituale());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        giocatore.setIdRec(rs.getInt(1));
                    }
                }
                System.out.println("DEBUG GiocatoriDAO: Giocatore inserito con ID: " + giocatore.getIdRec());
            }
        }

        return giocatore;
    }

    public Giocatore updateGiocatore(Giocatore giocatore) throws SQLException {
        String sql = "UPDATE VIEW_GIOCATORI SET NOME = ?, COGNOME = ?, DATA_NASCITA = TO_DATE(?, 'YYYY-MM-DD'), " +
                     "ALIAS = ?, NUMERO_MAGLIA_ABITUALE = ?, NAZIONE_NASCITA = ?, CITTA_NASCITA = ?, RUOLO = ? " +
                     "WHERE ID_REC = ?";

        System.out.println("DEBUG GiocatoriDAO: Aggiornamento giocatore ID: " + giocatore.getIdRec());

        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            // Driver already registered, ignore
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, giocatore.getNome());
            pstmt.setString(2, giocatore.getCognome());
            pstmt.setString(3, giocatore.getDataNascita());
            pstmt.setString(4, giocatore.getAlias());

            if (giocatore.getNumeroMagliaAbituale() != null) {
                pstmt.setBigDecimal(5, giocatore.getNumeroMagliaAbituale());
            } else {
                pstmt.setNull(5, java.sql.Types.NUMERIC);
            }

            pstmt.setString(6, giocatore.getNazioneNascita());
            pstmt.setString(7, giocatore.getCittaNascita());
            pstmt.setString(8, giocatore.getRuoloAbituale());
            pstmt.setInt(9, giocatore.getIdRec());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("DEBUG GiocatoriDAO: Giocatore aggiornato con successo");
            } else {
                throw new SQLException("Nessun giocatore trovato con ID: " + giocatore.getIdRec());
            }
        }

        return giocatore;
    }

}