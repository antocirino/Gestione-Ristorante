package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import CFG.DBConnection;
import entity.EntityDettaglioOrdinePietanza;
import entity.EntityPietanza;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella
 * 'dettaglio_ordine_pietanza' nel database
 */
public class DBDettaglioOrdinePietanza {
    // Attributi che mappano le colonne della tabella
    private int idDettaglio;
    private int idOrdine;
    private int idPietanza;
    private int quantita;
    private boolean parteDiMenu; // Flag per indicare se la pietanza fa parte di un menu fisso
    private int idMenu; // ID del menu fisso di cui fa parte (se applicabile)

    // Attributi aggiuntivi per join
    private String nomePietanza;
    private double prezzoPietanza;

    /**
     * Costruttore che carica un dettaglio ordine dal database tramite il suo ID
     * 
     * @param idDettaglio l'ID del dettaglio ordine da caricare
     */
    public DBDettaglioOrdinePietanza(int idDettaglio) {
        this.idDettaglio = idDettaglio;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti dettaglio ordine
     */
    public DBDettaglioOrdinePietanza() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati del dettaglio ordine dal database
     */
    public void caricaDaDB() {
        String query = "SELECT d.*, p.nome as nome_pietanza, p.prezzo as prezzo_pietanza " +
                "FROM dettaglio_ordine_pietanza d " +
                "JOIN pietanza p ON d.id_pietanza = p.id_pietanza " +
                "WHERE d.id_dettaglio = " + this.idDettaglio;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.idOrdine = rs.getInt("id_ordine");
                this.idPietanza = rs.getInt("id_pietanza");
                this.quantita = rs.getInt("quantita");
                this.nomePietanza = rs.getString("nome_pietanza");
                this.prezzoPietanza = rs.getDouble("prezzo_pietanza");
                this.parteDiMenu = rs.getBoolean("parte_di_menu");
                this.idMenu = rs.getInt("id_menu");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel caricamento del dettaglio ordine dal database: " + e.getMessage());
        }
    }

    /**
     * Salva un dettaglio ordine nel database
     * 
     * @param idDettaglio ID del dettaglio ordine (0 per auto-incremento)
     * @return l'ID del dettaglio ordine se il salvataggio è avvenuto con successo,
     *         -1 altrimenti
     */
    public int salvaInDB(int idDettaglio) {
        try {
            String query;
            if (idDettaglio == 0) {
                // Inserimento nuovo dettaglio ordine
                query = "INSERT INTO dettaglio_ordine_pietanza (id_ordine, id_pietanza, quantita, parte_di_menu, id_menu) VALUES ("
                        + this.idOrdine + ", " + this.idPietanza + ", " + this.quantita
                        + ", " + (this.parteDiMenu ? "TRUE" : "FALSE")
                        + ", " + this.idMenu + ")";

                System.out.println("Query di inserimento: " + query);

                // Utilizziamo il metodo updateQueryReturnGeneratedKey invece di updateQuery
                // per ottenere l'ID generato automaticamente
                Integer generatedId = DBConnection.updateQueryReturnGeneratedKey(query);

                if (generatedId != null && generatedId > 0) {
                    System.out.println("Dettaglio ordine inserito con ID generato: " + generatedId);
                    return generatedId;
                } else {
                    System.err.println("Errore: Nessun ID generato dopo l'inserimento del dettaglio ordine");
                    return -1;
                }
            } else {
                // Aggiornamento dettaglio ordine esistente
                query = "UPDATE dettaglio_ordine_pietanza SET id_ordine = " + this.idOrdine
                        + ", id_pietanza = " + this.idPietanza
                        + ", quantita = " + this.quantita
                        + ", parte_di_menu = " + (this.parteDiMenu ? "TRUE" : "FALSE")
                        + ", id_menu = " + this.idMenu
                        + " WHERE id_dettaglio = " + idDettaglio;

                System.out.println("Query di aggiornamento: " + query);

                int affectedRows = DBConnection.updateQuery(query);
                if (affectedRows > 0) {
                    System.out.println("Dettaglio ordine aggiornato con successo (ID: " + idDettaglio + ")");
                    return idDettaglio;
                } else {
                    System.err
                            .println("Errore: Nessuna riga aggiornata per il dettaglio ordine con ID: " + idDettaglio);
                    return -1;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel salvataggio del dettaglio ordine nel database: " + e.getMessage());
            e.printStackTrace();
            return -1; // Errore
        }
    }

    /**
     * Elimina un dettaglio ordine dal database
     * 
     * @param idDettaglio ID del dettaglio ordine da eliminare
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int eliminaDaDB(int idDettaglio) {
        try {
            String query = "DELETE FROM dettaglio_ordine_pietanza WHERE id_dettaglio = " + idDettaglio;
            System.out.println(query); // Per debug
            return DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nell'eliminazione del dettaglio ordine dal database: " + e.getMessage());
        }
        return -1; // Errore
    }

    /**
     * Recupera tutti i dettagli di un ordine dal database
     * 
     * @param idOrdine ID dell'ordine
     * @return ArrayList di oggetti DettaglioOrdinePietanza
     */
    public ArrayList<EntityDettaglioOrdinePietanza> getDettagliOrdine(int idOrdine) {
        ArrayList<EntityDettaglioOrdinePietanza> listaDettagli = new ArrayList<>();
        try {
            String query = "SELECT d.*, p.nome as nome_pietanza, p.prezzo as prezzo_pietanza " +
                    "FROM dettaglio_ordine_pietanza d " +
                    "JOIN pietanza p ON d.id_pietanza = p.id_pietanza " +
                    "WHERE d.id_ordine = " + idOrdine;
            ResultSet rs = DBConnection.selectQuery(query);

            while (rs.next()) {
                EntityDettaglioOrdinePietanza dettaglio = new EntityDettaglioOrdinePietanza();
                dettaglio.setIdDettaglio(rs.getInt("id_dettaglio"));
                dettaglio.setIdOrdine(rs.getInt("id_ordine"));
                dettaglio.setIdPietanza(rs.getInt("id_pietanza"));
                dettaglio.setQuantita(rs.getInt("quantita"));
                dettaglio.setParteDiMenu(rs.getBoolean("parte_di_menu"));
                dettaglio.setIdMenu(rs.getInt("id_menu"));

                EntityPietanza pietanza = new EntityPietanza(rs.getInt("id_pietanza"));
                dettaglio.setPietanza(pietanza);

                listaDettagli.add(dettaglio);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei dettagli dell'ordine: " + e.getMessage());
        }
        return listaDettagli;
    }

    /**
     * Recupera i dettagli di un ordine come mappa
     * 
     * @param idOrdine ID dell'ordine
     * @return ArrayList di mappe con gli attributi dei dettagli
     */
    public ArrayList<Map<String, Object>> getDettagliOrdineAsMap(int idOrdine) {
        ArrayList<Map<String, Object>> listaDettagli = new ArrayList<>();
        try {
            String query = "SELECT d.*, p.nome as nome_pietanza, p.prezzo as prezzo_pietanza " +
                    "FROM dettaglio_ordine_pietanza d " +
                    "JOIN pietanza p ON d.id_pietanza = p.id_pietanza " +
                    "WHERE d.id_ordine = " + idOrdine;
            ResultSet rs = DBConnection.selectQuery(query);

            while (rs.next()) {
                Map<String, Object> dettaglio = new HashMap<>();
                dettaglio.put("id_dettaglio", rs.getInt("id_dettaglio"));
                dettaglio.put("id_ordine", rs.getInt("id_ordine"));
                dettaglio.put("id_pietanza", rs.getInt("id_pietanza"));
                dettaglio.put("quantita", rs.getInt("quantita"));
                dettaglio.put("nome_pietanza", rs.getString("nome_pietanza"));
                dettaglio.put("prezzo_pietanza", rs.getDouble("prezzo_pietanza"));
                dettaglio.put("parte_di_menu", rs.getBoolean("parte_di_menu"));
                dettaglio.put("id_menu", rs.getInt("id_menu"));

                listaDettagli.add(dettaglio);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei dettagli dell'ordine: " + e.getMessage());
        }
        return listaDettagli;
    }

    /**
     * Salva un dettaglio ordine nel database usando INSERT ... ON CONFLICT
     * Se il dettaglio esiste già (stesso id_ordine, id_pietanza, parte_di_menu e
     * id_menu),
     * aggiorna solo la quantità sommandola a quella esistente.
     * 
     * @return l'ID del dettaglio ordine se il salvataggio è avvenuto con successo,
     *         -1 altrimenti
     */
    public int salvaConOnConflict() {
        try {
            // Query che utilizza ON CONFLICT per gestire i duplicati
            String query = "INSERT INTO dettaglio_ordine_pietanza (id_ordine, id_pietanza, quantita, parte_di_menu, id_menu) "
                    + "VALUES (" + this.idOrdine + ", " + this.idPietanza + ", " + this.quantita + ", "
                    + (this.parteDiMenu ? "TRUE" : "FALSE") + ", " + this.idMenu + ") "
                    + "ON DUPLICATE KEY UPDATE quantita = quantita + VALUES(quantita)";

            System.out.println("Query con ON DUPLICATE KEY: " + query);

            // Eseguiamo la query e verifichiamo il risultato
            int affectedRows = DBConnection.updateQuery(query);

            if (affectedRows > 0) {
                // Se la query ha avuto successo, recuperiamo l'ID del dettaglio
                String selectQuery = "SELECT id_dettaglio FROM dettaglio_ordine_pietanza "
                        + "WHERE id_ordine = " + this.idOrdine
                        + " AND id_pietanza = " + this.idPietanza
                        + " AND parte_di_menu = " + (this.parteDiMenu ? "TRUE" : "FALSE")
                        + " AND id_menu = " + this.idMenu;

                ResultSet rs = DBConnection.selectQuery(selectQuery);
                if (rs.next()) {
                    int dettaglioId = rs.getInt("id_dettaglio");
                    System.out.println("Dettaglio ordine inserito/aggiornato con ID: " + dettaglioId);
                    return dettaglioId;
                }
            }

            System.err.println("Errore nell'inserimento o aggiornamento del dettaglio ordine");
            return -1;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel salvataggio del dettaglio ordine con ON DUPLICATE KEY: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    // Getters e setters
    public int getIdDettaglio() {
        return idDettaglio;
    }

    public void setIdDettaglio(int idDettaglio) {
        this.idDettaglio = idDettaglio;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getNomePietanza() {
        return nomePietanza;
    }

    public void setNomePietanza(String nomePietanza) {
        this.nomePietanza = nomePietanza;
    }

    public double getPrezzoPietanza() {
        return prezzoPietanza;
    }

    public void setPrezzoPietanza(double prezzoPietanza) {
        this.prezzoPietanza = prezzoPietanza;
    }

    public boolean isParteDiMenu() {
        return parteDiMenu;
    }

    public void setParteDiMenu(boolean parteDiMenu) {
        this.parteDiMenu = parteDiMenu;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }
}
