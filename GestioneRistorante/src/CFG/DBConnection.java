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
        // Recupera i parametri dalle variabili d'ambiente 
        this.url = System.getenv("DATABASE_URL");
        this.username = System.getenv("DATABASE_USER");
        this.password = System.getenv("DATABASE_PASSWORD");

        // Se le variabili d'ambiente non sono impostate, usa i valori di default per il
        // container Docker
        if (this.url == null) {
            this.url = "jdbc:mysql://ristorante-db:3306/ristorante";
        }
        if (this.username == null) {
            this.username = "root";
        }
        if (this.password == null) {
            this.password = "password";
        }

        // Per l'esecuzione locale (fuori dal container) prova questi valori 
        if (this.url.contains("ristorante-db") && !isHostReachable()) {
            this.url = "jdbc:mysql://localhost:3306/ristorante";
        }
    }

    private boolean isHostReachable() {
        try {
            // Prova a creare una connessione per vedere se l'host è raggiungibile
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            // Se c'è un errore di connessione, l'host potrebbe non essere raggiungibile
            return false;
        }
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

    /**
     * Restituisce le informazioni sulla connessione corrente per il debug
     */
    public String getConnectionInfo() {
        return "URL: " + url + ", Username: " + username + ", Password: " + (password != null ? "******" : "null");
    }
}
