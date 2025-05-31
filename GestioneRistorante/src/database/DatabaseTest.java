package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import CFG.DBConnection;

/**
 * Classe per testare la connessione al database
 */
public class DatabaseTest {

    public static void main(String[] args) {
        testConnection();
    }

    public static boolean testConnection() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Stampa le informazioni di connessione per debug
            DBConnection dbInstance = DBConnection.getInstance();
            System.out.println("Tentativo di connessione con: " + dbInstance.getConnectionInfo());

            // Ottieni la connessione
            conn = dbInstance.getConnection();
            System.out.println("Connessione al database stabilita con successo!");

            // Esegui una query di test
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tavolo");

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Numero di tavoli nel database: " + count);
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Errore durante il test della connessione al database: " + e.getMessage());
            return false;
        } finally {
            // Chiudi le risorse
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                // Non chiudiamo la connessione qui perché è gestita dal singleton
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            }
        }
    }
}
