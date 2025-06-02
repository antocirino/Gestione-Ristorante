package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import CFG.DBConnection;
import entity.EntityRicetta;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'ricetta' nel database
 */
public class DBRicetta {
    // Attributi che mappano le colonne della tabella
    private int idRicetta;
    private String nome;
    private String descrizione;
    private int idPietanza;
    private int tempoPreparazione;
    private String istruzioni;

    /**
     * Costruttore che carica una ricetta dal database tramite il suo ID
     * 
     * @param idRicetta l'ID della ricetta da caricare
     */
    public DBRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti ricetta
     */
    public DBRicetta() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati della ricetta dal database dal suo ID
     */
    public void caricaDaDB() {
        // Definisco la query
        String query = "SELECT * FROM ricetta WHERE id_ricetta = " + this.idRicetta;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.descrizione = rs.getString("descrizione");
                this.idPietanza = rs.getInt("id_pietanza");
                this.tempoPreparazione = rs.getInt("tempo_preparazione");
                this.istruzioni = rs.getString("istruzioni");
            } else {
                throw new SQLException("EntityRicetta non trovata con ID: " + this.idRicetta);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento della ricetta: " + e.getMessage());
        }
    }

    /**
     * Salva le informazioni della ricetta nel database
     * 
     * @param idRicetta l'ID della ricetta (se nuova) o 0 per auto-incremento
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB(int idRicetta) {
        int ret = 0;

        String query;
        if (idRicetta == 0) {
            // Insert di una nuova ricetta con auto-incremento dell'ID
            query = String.format(Locale.US,
                    "INSERT INTO ricetta (nome, descrizione, id_pietanza, tempo_preparazione, istruzioni) " +
                            "VALUES ('%s', '%s', %d, %d, '%s')",
                    this.nome, this.descrizione, this.idPietanza, this.tempoPreparazione, this.istruzioni);
        } else {
            // Insert con ID specificato
            query = String.format(Locale.US,
                    "INSERT INTO ricetta (id_ricetta, nome, descrizione, id_pietanza, tempo_preparazione, istruzioni) "
                            +
                            "VALUES (%d, '%s', '%s', %d, %d, '%s')",
                    idRicetta, this.nome, this.descrizione, this.idPietanza, this.tempoPreparazione, this.istruzioni);
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
     * Aggiunge un ingrediente alla ricetta
     * 
     * @param idIngrediente l'ID dell'ingrediente da aggiungere
     * @param quantita      la quantità dell'ingrediente
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiungiIngrediente(int idIngrediente, float quantita) {
        int ret = 0;

        String query = String.format(Locale.US,
                "INSERT INTO ricetta_ingrediente (id_ricetta, id_ingrediente, quantita) VALUES (%d, %d, %.2f)",
                this.idRicetta, idIngrediente, quantita);

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
     * Rimuove un ingrediente dalla ricetta
     * 
     * @param idIngrediente l'ID dell'ingrediente da rimuovere
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int rimuoviIngrediente(int idIngrediente) {
        int ret = 0;

        String query = String.format(Locale.US,
                "DELETE FROM ricetta_ingrediente WHERE id_ricetta = %d AND id_ingrediente = %d",
                this.idRicetta, idIngrediente);

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
     * Recupera gli ingredienti che compongono questa ricetta
     * 
     * @return ArrayList di oggetti con informazioni sugli ingredienti
     */
    public ArrayList<Object[]> getIngredientiRicetta() {
        ArrayList<Object[]> ingredienti = new ArrayList<>();
        String query = "SELECT ri.id_ingrediente, i.nome, i.unita_misura, ri.quantita " +
                "FROM ricetta_ingrediente ri " +
                "JOIN ingrediente i ON ri.id_ingrediente = i.id_ingrediente " +
                "WHERE ri.id_ricetta = " + this.idRicetta + " " +
                "ORDER BY i.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                Object[] ingrediente = new Object[4];
                ingrediente[0] = rs.getInt("id_ingrediente");
                ingrediente[1] = rs.getString("nome");
                ingrediente[2] = rs.getString("unita_misura");
                ingrediente[3] = rs.getFloat("quantita");
                ingredienti.add(ingrediente);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero degli ingredienti della ricetta: " + e.getMessage());
        }

        return ingredienti;
    }

    /**
     * Recupera tutte le ricette dal database
     * 
     * @return ArrayList di oggetti Ricetta
     */
    public ArrayList<EntityRicetta> getTutteRicette() {
        ArrayList<EntityRicetta> listaRicette = new ArrayList<>();
        String query = "SELECT r.*, p.nome as nome_pietanza " +
                "FROM ricetta r " +
                "JOIN pietanza p ON r.id_pietanza = p.id_pietanza " +
                "ORDER BY r.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                EntityRicetta ricetta = new EntityRicetta();
                ricetta.setIdRicetta(rs.getInt("id_ricetta"));
                ricetta.setNome(rs.getString("nome"));
                ricetta.setDescrizione(rs.getString("descrizione"));
                ricetta.setIdPietanza(rs.getInt("id_pietanza"));
                ricetta.setTempoPreparazione(rs.getInt("tempo_preparazione"));
                ricetta.setIstruzioni(rs.getString("istruzioni"));
                listaRicette.add(ricetta);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle ricette: " + e.getMessage());
        }

        return listaRicette;
    }

    /**
     * Recupera la ricetta per una specifica pietanza
     * 
     * @param idPietanza l'ID della pietanza
     * @return Ricetta associata alla pietanza o null se non esiste
     */
    public EntityRicetta getRicettaByPietanza(int idPietanza) {
        EntityRicetta ricetta = null;
        String query = "SELECT * FROM ricetta WHERE id_pietanza = " + idPietanza;

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                ricetta = new EntityRicetta();
                ricetta.setIdRicetta(rs.getInt("id_ricetta"));
                ricetta.setNome(rs.getString("nome"));
                ricetta.setDescrizione(rs.getString("descrizione"));
                ricetta.setIdPietanza(idPietanza);
                ricetta.setTempoPreparazione(rs.getInt("tempo_preparazione"));
                ricetta.setIstruzioni(rs.getString("istruzioni"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero della ricetta per pietanza: " + e.getMessage());
        }

        return ricetta;
    }

    // Getters e setters
    public int getIdRicetta() {
        return idRicetta;
    }

    public void setIdRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public int getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(int tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }

    public String getIstruzioni() {
        return istruzioni;
    }

    public void setIstruzioni(String istruzioni) {
        this.istruzioni = istruzioni;
    }
}
