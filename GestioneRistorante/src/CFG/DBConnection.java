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

    // Parametri di connessione
    public static String url = "jdbc:mysql://ristorante-db:3306/ristorante";
    public static String username = "root";
    public static String password = "password";
    public static String driver = "com.mysql.cj.jdbc.Driver";
    public static final String db_name = "ristorante";

    /**
     * Restituisce una connessione al database MySQL
     * 
     * @return Connection oggetto di connessione al database
     * @throws SQLException se si verifica un errore durante la connessione
     */
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

    /**
     * Chiude la connessione al database se è aperta
     * 
     * @param connection la connessione da chiudere
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }

    /**
     * Esegue una query di selezione e restituisce un ResultSet
     * 
     * @param query la query SQL da eseguire
     * @return ResultSet contenente i risultati della query
     * @throws ClassNotFoundException se il driver JDBC non è trovato
     * @throws SQLException           se si verifica un errore durante l'esecuzione
     *                                della query
     */
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

    /**
     * Esegue una query di aggiornamento (INSERT, UPDATE, DELETE) e restituisce il
     * numero di righe modificate
     * 
     * @param query la query SQL da eseguire
     * @return il numero di righe modificate dalla query
     * @throws ClassNotFoundException se il driver JDBC non è trovato
     * @throws SQLException           se si verifica un errore durante l'esecuzione
     *                                della query
     */
    public static int updateQuery(String query) throws ClassNotFoundException, SQLException {

        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        int ret = statement.executeUpdate(query);
        conn.close();
        return ret;
    }

    /**
     * Esegue una query di aggiornamento e restituisce la chiave generata
     * 
     * @param query la query SQL da eseguire
     * @return l'ID generato dalla query, o null se non è stato generato alcun ID
     * @throws ClassNotFoundException se il driver JDBC non è trovato
     * @throws SQLException           se si verifica un errore durante l'esecuzione
     *                                della query
     */
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
