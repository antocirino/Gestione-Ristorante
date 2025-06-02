package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import CFG.DBConnection;
import entity.EntityPietanza;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'composizione_menu'
 * nel database
 */
public class DBComposizioneMenu {
    // Attributi che mappano le colonne della tabella
    private int idMenu;
    private int idPietanza;

    // Attributi aggiuntivi per join
    private String nomePietanza;
    private double prezzoPietanza;

    /**
     * Costruttore con chiave primaria composta
     * 
     * @param idMenu     ID del menu
     * @param idPietanza ID della pietanza
     */
    public DBComposizioneMenu(int idMenu, int idPietanza) {
        this.idMenu = idMenu;
        this.idPietanza = idPietanza;
        caricaDaDB(); // Carico i dati della composizione menu
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti composizione menu
     */
    public DBComposizioneMenu() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati della composizione menu dal database
     */
    public void caricaDaDB() {
        String query = "SELECT c.*, p.nome as nome_pietanza, p.prezzo as prezzo_pietanza " +
                "FROM composizione_menu c " +
                "JOIN pietanza p ON c.id_pietanza = p.id_pietanza " +
                "WHERE c.id_menu = " + this.idMenu + " AND c.id_pietanza = " + this.idPietanza;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                System.out.println("DEBUG.");
                this.nomePietanza = rs.getString("nome_pietanza");
                this.prezzoPietanza = rs.getDouble("prezzo_pietanza");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel caricamento della composizione menu dal database: " + e.getMessage());
        }
    }

    /**
     * Inserisce una nuova associazione menu-pietanza nel database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB() {
        try {
            // Verifica se l'associazione esiste già
            String checkQuery = "SELECT COUNT(*) FROM composizione_menu WHERE id_menu = " + this.idMenu +
                    " AND id_pietanza = " + this.idPietanza;
            ResultSet rs = DBConnection.selectQuery(checkQuery);
            if (rs.next() && rs.getInt(1) > 0) {
                // L'associazione esiste già, niente da fare
                System.out.println("L'associazione menu-pietanza esiste già. Nessuna modifica necessaria.");
                return 0;
            }

            // Inserimento nuova associazione
            String query = "INSERT INTO composizione_menu (id_menu, id_pietanza) VALUES (" +
                    this.idMenu + ", " + this.idPietanza + ")";

            System.out.println(query); // Per debug
            return DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel salvataggio della composizione menu nel database: " + e.getMessage());
        }
        return -1; // Errore
    }

    /**
     * Elimina un'associazione menu-pietanza dal database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int eliminaDaDB() {
        try {
            String query = "DELETE FROM composizione_menu WHERE id_menu = " + this.idMenu +
                    " AND id_pietanza = " + this.idPietanza;
            System.out.println(query); // Per debug
            return DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nell'eliminazione della composizione menu dal database: " + e.getMessage());

        }
        return -1; // Errore
    }

    /**
     * Recupera tutte le pietanze associate a un menu
     * 
     * @param idMenu ID del menu
     * @return ArrayList di oggetti Pietanza
     */
    public ArrayList<EntityPietanza> getPietanzeByMenu(int idMenu) {
        ArrayList<EntityPietanza> listaPietanze = new ArrayList<>();
        try {
            String query = "SELECT p.* FROM pietanza p " +
                    "JOIN composizione_menu c ON p.id_pietanza = c.id_pietanza " +
                    "WHERE c.id_menu = " + idMenu;
            ResultSet rs = DBConnection.selectQuery(query);

            while (rs.next()) {
                EntityPietanza pietanza = new EntityPietanza(
                        rs.getInt("id_pietanza"),
                        rs.getString("nome"),
                        rs.getDouble("prezzo"),
                        rs.getInt("id_categoria"));
                pietanza.setDisponibile(rs.getBoolean("disponibile"));
                listaPietanze.add(pietanza);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle pietanze del menu: " + e.getMessage());
        }
        return listaPietanze;
    }

    /**
     * Recupera tutti i menu che contengono una specifica pietanza
     * 
     * @param idPietanza ID della pietanza
     * @return ArrayList di ID dei menu
     */
    public ArrayList<Integer> getMenuByPietanza(int idPietanza) {
        ArrayList<Integer> listaIdMenu = new ArrayList<>();
        try {
            String query = "SELECT id_menu FROM composizione_menu WHERE id_pietanza = " + idPietanza;
            ResultSet rs = DBConnection.selectQuery(query);

            while (rs.next()) {
                listaIdMenu.add(rs.getInt("id_menu"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei menu contenenti la pietanza: " + e.getMessage());
        }
        return listaIdMenu;
    }

    /**
     * Recupera la composizione di un menu come mappa
     * 
     * @param idMenu ID del menu
     * @return ArrayList di mappe con gli attributi delle pietanze nel menu
     */
    public ArrayList<Map<String, Object>> getComposizioneMenuAsMap(int idMenu) {
        ArrayList<Map<String, Object>> composizione = new ArrayList<>();
        try {
            String query = "SELECT c.*, p.nome as nome_pietanza, p.prezzo as prezzo_pietanza " +
                    "FROM composizione_menu c " +
                    "JOIN pietanza p ON c.id_pietanza = p.id_pietanza " +
                    "WHERE c.id_menu = " + idMenu;
            ResultSet rs = DBConnection.selectQuery(query);

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id_menu", rs.getInt("id_menu"));
                item.put("id_pietanza", rs.getInt("id_pietanza"));
                item.put("nome_pietanza", rs.getString("nome_pietanza"));
                item.put("prezzo_pietanza", rs.getDouble("prezzo_pietanza"));

                composizione.add(item);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero della composizione del menu: " + e.getMessage());
        }
        return composizione;
    }

    // Getters e setters
    public int getIdMenu() {
        return idMenu;
    }


    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public String getNomePietanza() {
        return nomePietanza;
    }

    public double getPrezzoPietanza() {
        return prezzoPietanza;
    }
}
