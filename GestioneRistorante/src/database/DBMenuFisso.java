package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import CFG.DBConnection;
import entity.EntityMenuFisso;
import entity.EntityPietanza;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'menu_fisso' nel
 * database
 */
public class DBMenuFisso {
    // Attributi che mappano le colonne della tabella
    private int idMenu;
    private String nome;
    private double prezzo;
    private String descrizione;

    /**
     * Costruttore che carica un menu fisso dal database tramite il suo ID
     * 
     * @param idMenu l'ID del menu fisso da caricare
     */
    public DBMenuFisso(int idMenu) {
        this.idMenu = idMenu;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti menu fisso
     */
    public DBMenuFisso() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati del menu fisso dal database
     */
    public void caricaDaDB() {
        String query = "SELECT * FROM menu_fisso WHERE id_menu = " + this.idMenu;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.prezzo = rs.getDouble("prezzo");
                this.descrizione = rs.getString("descrizione");
            } else {
                throw new SQLException("Menu fisso non trovato con ID: " + this.idMenu);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento del menu fisso: " + e.getMessage());
        }
    }

    /**
     * Salva le informazioni del menu fisso nel database
     * 
     * @param idMenu l'ID del menu fisso (se nuovo) o 0 per auto-incremento
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB(int idMenu) {
        int ret = 0;

        String query;
        if (idMenu == 0) {
            // Insert di un nuovo menu fisso con auto-incremento dell'ID
            query = String.format(Locale.US,
                    "INSERT INTO menu_fisso (nome, prezzo, descrizione) " +
                            "VALUES ('%s', %.2f, '%s')",
                    this.nome, this.prezzo, this.descrizione);
        } else {
            // Insert con ID specificato
            query = String.format(Locale.US,
                    "INSERT INTO menu_fisso (id_menu, nome, prezzo, descrizione) " +
                            "VALUES (%d, '%s', %.2f, '%s')",
                    idMenu, this.nome, this.prezzo, this.descrizione);
        }

        System.out.println(query);
        try {
            ret = DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            ret = -1; // Segnala errore di scrittura
        }

        return ret;
    }

    /**
     * Aggiunge una pietanza al menu fisso
     * 
     * @param idPietanza l'ID della pietanza da aggiungere
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiungiPietanza(int idPietanza) {
        int ret = 0;

        String query = String.format(Locale.US,
                "INSERT INTO composizione_menu (id_menu, id_pietanza) VALUES (%d, %d)",
                this.idMenu, idPietanza);

        System.out.println(query);
        try {
            ret = DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            ret = -1; // Segnala errore di aggiornamento
        }

        return ret;
    }

    /**
     * Rimuove una pietanza dal menu fisso
     * 
     * @param idPietanza l'ID della pietanza da rimuovere
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int rimuoviPietanza(int idPietanza) {
        int ret = 0;

        String query = String.format(Locale.US,
                "DELETE FROM composizione_menu WHERE id_menu = %d AND id_pietanza = %d",
                this.idMenu, idPietanza);

        System.out.println(query);
        try {
            ret = DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            ret = -1; // Segnala errore di aggiornamento
        }

        return ret;
    }

    /**
     * Recupera le pietanze che compongono questo menu fisso
     * 
     * @return Lista di oggetti Pietanza che compongono il menu
     */
    public ArrayList<EntityPietanza> getPietanze() {
        ArrayList<EntityPietanza> pietanze = new ArrayList<>();
        String query = "SELECT p.* FROM pietanza p " +
                "JOIN composizione_menu cm ON p.id_pietanza = cm.id_pietanza " +
                "WHERE cm.id_menu = " + this.idMenu + " " +
                "ORDER BY p.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityPietanza pietanza = new EntityPietanza(
                        rs.getInt("id_pietanza"),
                        rs.getString("nome"),
                        rs.getDouble("prezzo"),
                        rs.getInt("id_categoria"));
                pietanze.add(pietanza);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle pietanze del menu: " + e.getMessage());
        }

        return pietanze;
    }

    /**
     * Recupera tutti i menu fissi dal database
     * 
     * @return ArrayList di oggetti MenuFisso
     */
    public ArrayList<EntityMenuFisso> getTuttiMenuFissi() {
        ArrayList<EntityMenuFisso> listaMenu = new ArrayList<>();
        String query = "SELECT * FROM menu_fisso ORDER BY nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityMenuFisso menu = new EntityMenuFisso();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setNome(rs.getString("nome"));
                menu.setPrezzo(rs.getDouble("prezzo"));
                menu.setDescrizione(rs.getString("descrizione"));
                listaMenu.add(menu);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei menu fissi: " + e.getMessage());
        }

        return listaMenu;
    }

    /**
     * Recupera tutti i menu fissi dal database e restituisce una mappa
     * 
     * @return Mappa con chiave l'ID del menu e valore un oggetto con le
     *         informazioni del menu
     */
    public Map<Integer, Map<String, Object>> getMenuFissiMap() {
        Map<Integer, Map<String, Object>> menuMap = new HashMap<>();
        String query = "SELECT * FROM menu_fisso ORDER BY nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                int idMenu = rs.getInt("id_menu");
                Map<String, Object> menuInfo = new HashMap<>();
                menuInfo.put("nome", rs.getString("nome"));
                menuInfo.put("prezzo", rs.getDouble("prezzo"));
                menuInfo.put("descrizione", rs.getString("descrizione"));

                menuMap.put(idMenu, menuInfo);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei menu fissi: " + e.getMessage());
        }

        return menuMap;
    }

    // Getters e setters
    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
