package Repo;

import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlRepository<T extends Entity> implements Repository<T> {
    private final Connection connection;

    private final String entityName;

    public SqlRepository(Connection connection, String entityClass) {
        this.connection = connection;
        this.entityName = entityClass;
    }

    @Override
    public void add(T entity) throws SQLException {
        if (entity instanceof Produs) {
            addEnt((Produs) entity);
        }
    }

    @Override
    public Optional<T> findById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }

        // Căutăm în funcție de tipul de entitate
        String sql = "SELECT * FROM " + entityName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (id == 1) {  // Presupunem că "Produs" este tabelul 1
                    Produs ent = new Produs(rs.getInt("id"), rs.getString("denumire"), rs.getFloat("pret"), rs.getInt("stoc"));
                    return Optional.of((T) ent);
                }
            }
        } catch (SQLException e) {
            // Aruncă o excepție runtime personalizată sau o excepție mai generală
            throw new RuntimeException("Database error during findById", e);
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        String tableName = entityName;

        String sql = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (tableName.equals("produse")) {
                    Produs ent = new Produs(
                            rs.getInt("id"),
                            rs.getString("denumire"),
                            rs.getFloat("pret"),
                            rs.getInt("stoc")
                    );
                    entities.add((T) ent);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll", e);
        }
        return entities;
    }

    private Produs findEntById(int entId) throws SQLException {
        String sql = "SELECT * FROM produse WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Produs(
                        rs.getInt("id"),
                        rs.getString("denumire"),
                        rs.getFloat("pret"),
                        rs.getInt("stoc")
                );
            }
        }
        throw new SQLException("Produsul cu ID-ul " + entId + " nu a fost găsit.");
    }

    @Override
    public void update(T entity) throws SQLException {
        if (entity instanceof Produs) {
            updateEnt((Produs) entity);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM " + entityName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private void addEnt(Produs ent) throws SQLException {
        String sql = "INSERT INTO produse (denumire, pret, stoc) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(2, ent.getDenumire());
            stmt.setFloat(3, ent.getPret());
            stmt.setInt(4, ent.getStoc());
            stmt.executeUpdate();
        }
    }

    private void updateEnt(Produs ent) throws SQLException {
        String sql = "UPDATE produse SET denumire = ?, pret = ?, stoc = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ent.getDenumire());
            stmt.setFloat(2, ent.getPret());
            stmt.setInt(3, ent.getStoc());
            stmt.setInt(4, ent.getId());
            stmt.executeUpdate();
        }
    }

}
