package CFG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe per la gestione della connessione al database
 */
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    // Parametri di connessione
    private String url;
    private String username;
    private String password;

    private DBConnection() {
        // Recupera i parametri dalle variabili d'ambiente o usa valori di default
        this.url = System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL")
                : "jdbc:mysql://localhost:3306/ristorante";
        this.username = System.getenv("DATABASE_USER") != null ? System.getenv("DATABASE_USER") : "root";
        this.password = System.getenv("DATABASE_PASSWORD") != null ? System.getenv("DATABASE_PASSWORD") : "password";
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carica il driver JDBC di MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                System.err.println("Driver MySQL non trovato: " + e.getMessage());
                throw new SQLException("Driver MySQL non trovato", e);
            } catch (SQLException e) {
                System.err.println("Errore durante la connessione al database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}
