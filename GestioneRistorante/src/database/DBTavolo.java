package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import CFG.DBConnection;
import entity.Tavolo;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'tavolo' nel database
 */
public class DBTavolo {
    // Attributi che mappano le colonne della tabella
    private int idTavolo;
    private int numero; // Il numero del tavolo visualizzato ai clienti
    private int maxPosti;
    private String stato;
    private int idRistorante;

    /**
     * Costruttore che carica un tavolo dal database tramite il suo ID
     * 
     * @param idTavolo l'ID del tavolo da caricare
     */
    public DBTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti tavolo
     */
    public DBTavolo() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati del tavolo dal database
     */
    public void caricaDaDB() {
        // Definisco la query
        String query = "SELECT * FROM tavolo WHERE id_tavolo = " + this.idTavolo;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.numero = rs.getInt("numero");
                this.maxPosti = rs.getInt("max_posti");
                this.stato = rs.getString("stato");
                this.idRistorante = rs.getInt("id_ristorante");
            } else {
                throw new SQLException("Tavolo non trovato con ID: " + this.idTavolo);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento del tavolo: " + e.getMessage());
        }
    }

    /**
     * Salva le informazioni del tavolo nel database
     * 
     * @param idTavolo l'ID del tavolo (se nuovo) o 0 per auto-incremento
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB(int idTavolo) {
        int ret = 0;

        String query;
        if (idTavolo == 0) {
            // Insert di un nuovo tavolo con auto-incremento dell'ID
            query = String.format(Locale.US,
                    "INSERT INTO tavolo (numero, max_posti, stato, id_ristorante) " +
                            "VALUES (%d, %d, '%s', %d)",
                    this.numero, this.maxPosti, this.stato, this.idRistorante);
        } else {
            // Insert con ID specificato
            query = String.format(Locale.US,
                    "INSERT INTO tavolo (id_tavolo, numero, max_posti, stato, id_ristorante) " +
                            "VALUES (%d, %d, %d, '%s', %d)",
                    idTavolo, this.numero, this.maxPosti, this.stato, this.idRistorante);
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
     * Aggiorna lo stato di un tavolo nel database
     * 
     * @param nuovoStato il nuovo stato del tavolo ('libero' o 'occupato')
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaStato(String nuovoStato) {
        int ret = 0;

        String query = String.format(Locale.US,
                "UPDATE tavolo SET stato = '%s' WHERE id_tavolo = %d",
                nuovoStato, this.idTavolo);

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
     * Recupera tutti i tavoli dal database
     * 
     * @return ArrayList di oggetti Tavolo
     */
    public ArrayList<Tavolo> getTuttiTavoli() {
        ArrayList<Tavolo> listaTavoli = new ArrayList<>();
        String query = "SELECT * FROM tavolo ORDER BY numero";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                Tavolo tavolo = new Tavolo();
                tavolo.setIdTavolo(rs.getInt("id_tavolo"));
                tavolo.setNumero(rs.getInt("numero"));
                tavolo.setMaxPosti(rs.getInt("max_posti"));
                tavolo.setStato(rs.getString("stato"));
                tavolo.setIdRistorante(rs.getInt("id_ristorante"));
                listaTavoli.add(tavolo);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei tavoli: " + e.getMessage());
        }

        return listaTavoli;
    }

    /**
     * Recupera i tavoli filtrati per stato
     * 
     * @param statoFiltro lo stato per cui filtrare ('libero' o 'occupato')
     * @return ArrayList di oggetti Tavolo che corrispondono al filtro
     */
    public ArrayList<Tavolo> getTavoliPerStato(String statoFiltro) {
        ArrayList<Tavolo> listaTavoli = new ArrayList<>();
        String query = "SELECT * FROM tavolo WHERE stato = '" + statoFiltro + "' ORDER BY numero";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                Tavolo tavolo = new Tavolo();
                tavolo.setIdTavolo(rs.getInt("id_tavolo"));
                tavolo.setNumero(rs.getInt("numero"));
                tavolo.setMaxPosti(rs.getInt("max_posti"));
                tavolo.setStato(rs.getString("stato"));
                tavolo.setIdRistorante(rs.getInt("id_ristorante"));
                listaTavoli.add(tavolo);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei tavoli per stato: " + e.getMessage());
        }

        return listaTavoli;
    }

    // Getters e setters
    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getMaxPosti() {
        return maxPosti;
    }

    public void setMaxPosti(int maxPosti) {
        this.maxPosti = maxPosti;
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

    public boolean isOccupato() {
        return "occupato".equalsIgnoreCase(this.stato);
    }
}
