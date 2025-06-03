package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import CFG.DBConnection;
import entity.EntityOrdine;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'ordine' nel database
 */
public class DBOrdine {
    // Attributi che mappano le colonne della tabella
    private int idOrdine;
    private int idTavolo;
    private int numPersone;
    private Date dataOrdine;
    private String stato;
    private int idRistorante;
    private double costoTotale; // Nuovo campo per tenere traccia del costo totale

    /**
     * Costruttore che carica un ordine dal database tramite il suo ID
     * 
     * @param idOrdine l'ID dell'ordine da caricare
     */
    public DBOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti ordine
     */
    public DBOrdine() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati dell'ordine dal database
     */
    public void caricaDaDB() {
        String query = "SELECT * FROM ordine WHERE id_ordine = " + this.idOrdine;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.idTavolo = rs.getInt("id_tavolo");
                this.numPersone = rs.getInt("num_persone");
                this.dataOrdine = rs.getTimestamp("data_ordine");
                this.stato = rs.getString("stato");
                this.idRistorante = rs.getInt("id_ristorante");
                this.costoTotale = rs.getDouble("costo_totale"); // Carica il costo totale
            } else {
                throw new SQLException("Ordine non trovato con ID: " + this.idOrdine);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento dell'ordine: " + e.getMessage());
        }
    }

    /**
     * Salva un nuovo ordine nel database e restituisce l'ID generato
     * 
     * @return l'ID dell'ordine inserito o -1 in caso di errore
     */
    public int salvaInDB() {
        int nuovoId = -1;

        String query = String.format(Locale.US,
                "INSERT INTO ordine (id_tavolo, num_persone, stato, data_ordine, id_ristorante, costo_totale) " +
                        "VALUES (%d, %d, '%s', '%s', %d, %.2f)",
                this.idTavolo, this.numPersone, this.stato,
                new java.sql.Timestamp(this.dataOrdine.getTime()),
                this.idRistorante, this.costoTotale);

        System.out.println(query);
        try {
            nuovoId = insertAndGetId(query);
            System.out.println("Nuovo ordine inserito con ID: " + nuovoId);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return nuovoId;
    }

    /**
     * Metodo helper per inserire un ordine e ottenere l'ID generato
     * 
     * @param query la query di inserimento
     * @return l'ID generato o -1 in caso di errore
     */
    private static int insertAndGetId(String query) throws ClassNotFoundException, SQLException {
        int generatedKey = -1;
        java.sql.Connection connection = DBConnection.getConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }

        return generatedKey;
    }

    /**
     * Aggiorna lo stato di un ordine
     * 
     * @param nuovoStato il nuovo stato dell'ordine
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaStato(String nuovoStato) {
        int ret = 0;

        String query = String.format(Locale.US,
                "UPDATE ordine SET stato = '%s' WHERE id_ordine = %d",
                nuovoStato, this.idOrdine);

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
     * Aggiorna il costo totale dell'ordine nel database
     * 
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaCostoTotale() {
        try {
            String query = String.format(Locale.US,
                    "UPDATE ordine SET costo_totale = %.2f WHERE id_ordine = %d",
                    this.costoTotale, this.idOrdine);

            System.out.println("Aggiornamento costo totale: " + query);
            int result = DBConnection.updateQuery(query);

            return result > 0;
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento del costo totale nel database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera tutti gli ordini dal database
     * 
     * @return ArrayList di oggetti Ordine
     */
    public ArrayList<EntityOrdine> getTuttiOrdini() {
        ArrayList<EntityOrdine> listaOrdini = new ArrayList<>();
        String query = "SELECT * FROM ordine ORDER BY data_ordine DESC";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityOrdine ordine = new EntityOrdine();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdTavolo(rs.getInt("id_tavolo"));
                ordine.setNumPersone(rs.getInt("num_persone"));
                ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                ordine.setStato(rs.getString("stato"));
                listaOrdini.add(ordine);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini: " + e.getMessage());
        }

        return listaOrdini;
    }

    /**
     * Recupera gli ordini filtrati per stato
     * 
     * @param statoFiltro lo stato per cui filtrare
     * @return ArrayList di oggetti Ordine che corrispondono al filtro
     */
    public ArrayList<EntityOrdine> getOrdiniPerStato(String statoFiltro) {
        ArrayList<EntityOrdine> listaOrdini = new ArrayList<>();
        String query = "SELECT * FROM ordine WHERE stato = '" + statoFiltro + "' ORDER BY data_ordine DESC";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityOrdine ordine = new EntityOrdine();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdTavolo(rs.getInt("id_tavolo"));
                ordine.setNumPersone(rs.getInt("num_persone"));
                ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                ordine.setStato(rs.getString("stato"));
                listaOrdini.add(ordine);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini per stato: " + e.getMessage());
        }

        return listaOrdini;
    }

    /**
     * Recupera gli ordini per un tavolo specifico
     * 
     * @param idTavolo l'ID del tavolo
     * @return ArrayList di oggetti Ordine per il tavolo specificato
     */
    public ArrayList<EntityOrdine> getOrdiniPerTavolo(int idTavolo) {
        ArrayList<EntityOrdine> listaOrdini = new ArrayList<>();
        String query = "SELECT * FROM ordine WHERE id_tavolo = " + idTavolo + " ORDER BY data_ordine DESC";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityOrdine ordine = new EntityOrdine();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdTavolo(rs.getInt("id_tavolo"));
                ordine.setNumPersone(rs.getInt("num_persone"));
                ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                ordine.setStato(rs.getString("stato"));
                listaOrdini.add(ordine);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini per tavolo: " + e.getMessage());
        }

        return listaOrdini;
    }

    /**
     * Recupera gli ordini con un determinato stato
     * 
     * @param stato lo stato degli ordini da recuperare
     * @return ArrayList di oggetti Ordine con lo stato specificato
     */
    public ArrayList<EntityOrdine> getOrdiniByStato(String stato) {
        ArrayList<EntityOrdine> listaOrdini = new ArrayList<>();
        String query = "SELECT * FROM ordine WHERE stato = '" + stato + "' ORDER BY data_ordine DESC";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityOrdine ordine = new EntityOrdine();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdTavolo(rs.getInt("id_tavolo"));
                ordine.setNumPersone(rs.getInt("num_persone"));
                ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                ordine.setStato(rs.getString("stato"));

                listaOrdini.add(ordine);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini per stato: " + e.getMessage());
        }

        return listaOrdini;
    }

    /**
     * Recupera informazioni su un menu fisso dal database
     * 
     * @param idMenu ID del menu fisso
     * @return Map con nome e prezzo del menu, o null se non trovato
     */
    public Map<String, Object> getInfoMenuFisso(int idMenu) {
        Map<String, Object> infoMenu = null;
        try {
            String query = "SELECT nome, prezzo FROM menu_fisso WHERE id_menu = " + idMenu;
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                infoMenu = new HashMap<>();
                infoMenu.put("nome", rs.getString("nome"));
                infoMenu.put("prezzo", rs.getDouble("prezzo"));
            }
        } catch (Exception e) {
            System.err.println("Errore nel recupero dei dettagli del menu: " + e.getMessage());
        }
        return infoMenu;
    }

    /**
     * Recupera il costo coperto dal database
     * 
     * @return il costo del coperto o un valore di default (2.0)
     */
    public double getCostoCoperto() {
        double costoCoperto = 2.0; // Valore di default
        try {
            String query = "SELECT costo_coperto FROM ristorante LIMIT 1";
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                costoCoperto = rs.getDouble("costo_coperto");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero del costo coperto: " + e.getMessage());
        }
        return costoCoperto;
    }

    // Getters e setters
    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getNumPersone() {
        return numPersone;
    }

    public void setNumPersone(int numPersone) {
        this.numPersone = numPersone;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getIdRistorante() {
        return idRistorante;
    }

    public void setIdRistorante(int idRistorante) {
        this.idRistorante = idRistorante;
    }

    public double getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(double costoTotale) {
        this.costoTotale = costoTotale;
    }
}
