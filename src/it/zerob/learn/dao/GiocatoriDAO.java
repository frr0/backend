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
                                giocatoreIterato.setNumeroMagliaAbituale(rs.getInt(i));
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

}