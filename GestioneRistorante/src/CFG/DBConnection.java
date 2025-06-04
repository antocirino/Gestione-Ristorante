package CFG;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe per la gestione della connessione al database
 */
public class DBConnection {
    // private static DBConnection instance;
    // private Connection connection;

    // Parametri di connessione
    // public static String url = "jdbc:mysql://db:3306/ristorante";
    public static String url = "jdbc:mysql://ristorante-db:3306/ristorante";
    // public static String url = "jdbc:mysql://localhost:3306";
    public static String username = "root";
    public static String password = "password";
    public static String driver = "com.mysql.cj.jdbc.Driver";
    public static final String db_name = "ristorante";

    public static Connection getConnection() throws SQLException {

        Connection connection = null;
        if (connection == null || connection.isClosed()) {
            try {
                // Carica il driver JDBC di MySQL
                Class.forName(driver);

                // Aggiungi proprietà per migliorare il debugging e la stabilità
                java.util.Properties props = new java.util.Properties();
                props.setProperty("user", username);
                props.setProperty("password", password);
                props.setProperty("connectTimeout", "5000"); // 5 secondi di timeout
                props.setProperty("autoReconnect", "true");

                connection = DriverManager.getConnection(url, props);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL non trovato", e);
            } catch (SQLException e) {
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connessione chiusa correttamente");
                }
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

    //////
    ///
    ///
    public static ResultSet selectQuery(String query) throws ClassNotFoundException, SQLException {

        // .1 creo la connessione

        Connection conn = getConnection();

        // 2. creo lo statement
        Statement statment = conn.createStatement();

        // eseguo la query che ho fornito come input
        ResultSet ret = statment.executeQuery(query); // "SELECT * from STUDENTI where .... "

        // ci ritorna il result set
        return ret;
    }

    public static int updateQuery(String query) throws ClassNotFoundException, SQLException {

        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        int ret = statement.executeUpdate(query);
        conn.close();
        return ret;
    }

    public static Integer updateQueryReturnGeneratedKey(String query) throws ClassNotFoundException, SQLException {
        Integer ret = null;

        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            ret = rs.getInt(1);
        }

        conn.close();

        return ret;
    }

}
