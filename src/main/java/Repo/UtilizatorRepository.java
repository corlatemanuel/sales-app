package Repo;

import Entity.*;
import java.sql.*;

public class UtilizatorRepository {
    private final Connection conn;

    public UtilizatorRepository(Connection conn) {
        this.conn = conn;
    }

    public Utilizator autentifica(String username, String parola) throws SQLException {
        String sql = "SELECT * FROM utilizatori WHERE username = ? AND parola = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, parola);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilizator(rs.getInt("id"), rs.getString("username"), rs.getString("parola"));
            }
            return null;
        }
    }

    public void inregistreaza(String username, String parola) throws SQLException {
        String sql = "INSERT INTO utilizatori (username, parola) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, parola);
            stmt.executeUpdate();
        }
    }
}

