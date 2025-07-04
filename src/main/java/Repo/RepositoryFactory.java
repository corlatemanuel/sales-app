package Repo;

import Entity.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RepositoryFactory {

    public static Repository<Produs> createRepository(String repositoryType) {

        if ("sql".equalsIgnoreCase(repositoryType)) {
            return createSqlRepository("jdbc:sqlite:data.sqlite");
        } else {
            throw new IllegalArgumentException("Tip de repository necunoscut: " + repositoryType);
        }
    }

    private static Repository<Produs> createSqlRepository(String DbUrl) {
        try {
            Connection connection = createSqlConnection(DbUrl);
            return new SqlRepository<Produs>(connection, "produse");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Nu s-a putut crea conexiunea la baza de date pentru produse", e);
        }
    }

    private static Connection createSqlConnection(String url) throws SQLException {
        Connection connection = null;
        try {
            // Încărcați driver-ul SQLite JDBC
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(url);

            System.out.println("Conexiune stabilită cu succes!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Eroare la conexiune: " + e.getMessage());
        }
        createTables(connection);
        return connection;
    }

    public static Connection getSqlConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:data.sqlite");
        } catch (SQLException e) {
            throw new RuntimeException("Nu s-a putut obține conexiunea SQL", e);
        }
    }

    public static void createTables(Connection connection) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS produse (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "denumire TEXT," +
                "pret FLOAT," +
                "stoc INTEGER" +
                ");";

        String createUsers = "CREATE TABLE IF NOT EXISTS utilizatori (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "parola TEXT NOT NULL" +
                ");";

        String createComenzi = "CREATE TABLE IF NOT EXISTS comenzi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nume TEXT, " +
                "adresa TEXT, " +
                "telefon TEXT, " +
                "data_plasare TEXT, " +
                "detalii TEXT" +  // JSON simplu sau concatenare text
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTable);  // produse
            stmt.execute(createUsers);  // utilizatori
            stmt.execute(createComenzi);  // comenzi
        }
        catch (SQLException e) {
            System.out.println("Eroare la crearea tabelului: " + e.getMessage());
        }
    }

    private static void ensureFileExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Fișierul " + fileName + " a fost creat.");
                }
            } catch (IOException e) {
                System.err.println("Eroare la crearea fișierului: " + fileName);
                e.printStackTrace();
            }
        }
    }
}
