package it.zerob.learn.dao;

import oracle.jdbc.driver.OracleDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuoliDAO {

    private static final String CONNECTION_STRING = "jdbc:oracle:thin:@franellucci-zb:1521:XE";
    private static final String DB_USER = "ZEROBSPORTS";
    private static final String DB_PASSWORD = "zrbpwdzerobsports";

    public List<Map<String, Object>> getAllRuoli() throws SQLException {
        List<Map<String, Object>> ruoli = new ArrayList<>();
        String sql = "SELECT * FROM ZS_RUOLI ORDER BY NOME";

        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            // Driver already registered, ignore
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
             PreparedStatement query = connection.prepareStatement(sql);
             ResultSet rs = query.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("ID_REC", rs.getInt("ID_REC"));
                record.put("NOME", rs.getString("NOME"));
                ruoli.add(record);
            }
        }
        return ruoli;
    }
}