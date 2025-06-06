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
import DTO.DTOMenuFissoCuoco;
import DTO.DTOOrdine;
import DTO.DTOPietanzaCuoco;
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

    public DBOrdine(int idTavolo, int numPersone, int idRistorante, String stato) {
        this.idTavolo = idTavolo;
        this.numPersone = numPersone;
        this.dataOrdine = new Date(); // Imposta la data corrente
        this.stato = stato;
        this.idRistorante = idRistorante;
        this.costoTotale = 0.0; // Inizializza il costo totale a 0
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
     * Salva un ordine nel database: se l'ID è 0 inserisce un nuovo record,
     * altrimenti aggiorna un record esistente
     * 
     * @return l'ID dell'ordine inserito/aggiornato o -1 in caso di errore
     */
    public int salvaInDB() {
        try {
            String query;

            // Se l'ID è 0 creiamo un nuovo ordine
            if (this.idOrdine == 0) {
                // Insert di un nuovo ordine con auto-incremento dell'ID
                query = String.format(Locale.US,
                        "INSERT INTO ordine (id_tavolo, num_persone, stato, data_ordine, id_ristorante, costo_totale) "
                                +
                                "VALUES (%d, %d, '%s', '%s', %d, %.2f)",
                        this.idTavolo, this.numPersone, this.stato,
                        new java.sql.Timestamp(this.dataOrdine.getTime()),
                        this.idRistorante, this.costoTotale);

                System.out.println("Query di inserimento: " + query);

                // Otteniamo l'ID generato
                Integer generatedId = insertAndGetId(query);
                if (generatedId != null && generatedId > 0) {
                    System.out.println("Nuovo ordine inserito con ID: " + generatedId);
                    return generatedId;
                } else {
                    System.err.println("Errore: Nessun ID generato dopo l'inserimento dell'ordine");
                    return -1;
                }
            } else if (this.idOrdine > 0) {
                // Verifichiamo prima se l'ordine esiste
                String checkQuery = "SELECT COUNT(*) FROM ordine WHERE id_ordine = " + this.idOrdine;
                ResultSet checkRs = DBConnection.selectQuery(checkQuery);

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    // Esiste, quindi aggiorniamo
                    query = String.format(Locale.US,
                            "UPDATE ordine SET id_tavolo = %d, num_persone = %d, stato = '%s', " +
                                    "data_ordine = '%s', id_ristorante = %d, costo_totale = %.2f " +
                                    "WHERE id_ordine = %d",
                            this.idTavolo, this.numPersone, this.stato,
                            new java.sql.Timestamp(this.dataOrdine.getTime()),
                            this.idRistorante, this.costoTotale, this.idOrdine);

                    System.out.println("Query di aggiornamento: " + query);

                    int affectedRows = DBConnection.updateQuery(query);
                    if (affectedRows > 0) {
                        System.out.println("Ordine aggiornato con successo (ID: " + this.idOrdine + ")");
                        return this.idOrdine;
                    } else {
                        System.err.println("Errore: Nessuna riga aggiornata per l'ordine con ID: " + this.idOrdine);
                        return -1;
                    }
                } else {
                    // Id negativo o non presente, genero un errore
                    System.err.println("Errore: Impossibile inserire l'ordine con ID: " + this.idOrdine);
                    return -1;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel salvataggio dell'ordine nel database: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        System.out.println("Errore: ID ordine non valido");
        return -1; // In caso di errore generico
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
    public ArrayList<DBOrdine> getOrdiniPerStato(String statoFiltro) {
        ArrayList<DBOrdine> listaOrdini = new ArrayList<>();
        String query = "SELECT * FROM ordine WHERE stato = '" + statoFiltro + "' ORDER BY data_ordine DESC";
        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                DBOrdine ordine = new DBOrdine();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdTavolo(rs.getInt("id_tavolo"));
                ordine.setNumPersone(rs.getInt("num_persone"));
                ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                ordine.setStato(rs.getString("stato"));
                ordine.setCostoTotale(rs.getDouble("costo_totale"));
                listaOrdini.add(ordine);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini per stato: " + e.getMessage());
        }

        return listaOrdini;
    }

    public ArrayList<DTOPietanzaCuoco> getPietanzeDaOrdine() {
        ArrayList<DTOPietanzaCuoco> pietanzeDAoRDINE = new ArrayList<>();
        String query = String.format(
                "SELECT p.nome, SUM(dop.quantita) AS quantita_totale " +
                        "FROM dettaglio_ordine_pietanza dop " +
                        "JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                        "WHERE dop.id_ordine = %d " +
                        "GROUP BY p.nome",
                this.idOrdine);

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                String nome = rs.getString("nome");
                int quantita = rs.getInt("quantita_totale");
                pietanzeDAoRDINE.add(new DTOPietanzaCuoco(nome, quantita));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini per stato: " + e.getMessage());
        }

        return pietanzeDAoRDINE;
    }

    public ArrayList<DTOMenuFissoCuoco> getMenuFissiDaOrdine() {
        ArrayList<DTOMenuFissoCuoco> menuFissiDaOrdine = new ArrayList<>();

        String query = String.format(
                "SELECT m.nome, SUM(dop.quantita) AS quantita_totale " +
                        "FROM dettaglio_ordine_pietanza dop " +
                        "JOIN menu_fisso m ON dop.id_menu = m.id_menu " +
                        "WHERE dop.id_ordine = %d AND dop.id_menu IS NOT NULL " +
                        "GROUP BY m.nome",
                this.idOrdine);

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                String nome = rs.getString("nome");
                int quantita = rs.getInt("quantita_totale");
                menuFissiDaOrdine.add(new DTOMenuFissoCuoco(nome, quantita));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei menu fissi: " + e.getMessage());
        }

        return menuFissiDaOrdine;
    }

    /**
     * Recupera l'ultimo per un tavolo specifico
     * 
     * @param idTavolo l'ID del tavolo
     */
    public void getOrdinePerTavolo(int idTavolo) { // da eliminare probabilmente
        DBOrdine ordine = new DBOrdine();
        String query = "SELECT * FROM ordine WHERE id_tavolo = " + idTavolo + " ORDER BY data_ordine DESC LIMIT 1";

        try {
            ResultSet rs = DBConnection.selectQuery(query);

            if (rs.next()) {
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdTavolo(rs.getInt("id_tavolo"));
                ordine.setNumPersone(rs.getInt("num_persone"));
                ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                ordine.setStato(rs.getString("stato"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ordini per tavolo: " + e.getMessage());
        }

    }

    public static int getIDOrdineByTavolo(int idTavolo) {
        String query = "SELECT id_ordine FROM ordine WHERE id_tavolo = " + idTavolo
                + " ORDER BY data_ordine DESC LIMIT 1";
        int id_ordine = 0;
        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                id_ordine = rs.getInt("id_ordine");
            } else {
                id_ordine = -1; // Se non trovato, impostiamo a -1
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dell'ID ordine per tavolo: " + e.getMessage());
        }
        return id_ordine;
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

    public int aggiornaCosto(double costoTotale) {
        this.costoTotale = costoTotale;
        String query = String.format(Locale.US,
                "UPDATE ordine SET costo_totale = %.2f WHERE id_ordine = %d",
                this.costoTotale, this.idOrdine);

        try {
            int result = DBConnection.updateQuery(query);
            return result; // restituisce il numero di righe aggiornate
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore durante l'aggiornamento del costo totale: " + e.getMessage());
            return -1;
        }
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
