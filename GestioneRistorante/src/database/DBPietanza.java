package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import CFG.DBConnection;
import entity.Pietanza;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'pietanza' nel
 * database
 */
public class DBPietanza {
    // Attributi che mappano le colonne della tabella
    private int idPietanza;
    private String nome;
    private double prezzo;
    private int idCategoria;
    private boolean disponibile;
    private String nomeCategoria; // Campo aggiuntivo per memorizzare il nome della categoria

    /**
     * Costruttore che carica una pietanza dal database tramite il suo ID
     * 
     * @param idPietanza l'ID della pietanza da caricare
     */
    public DBPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti pietanza
     */
    public DBPietanza() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati della pietanza dal database
     */
    public void caricaDaDB() {
        String query = "SELECT p.*, c.nome as nome_categoria " +
                "FROM pietanza p " +
                "JOIN categoria_pietanza c ON p.id_categoria = c.id_categoria " +
                "WHERE p.id_pietanza = " + this.idPietanza;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.prezzo = rs.getDouble("prezzo");
                this.idCategoria = rs.getInt("id_categoria");
                this.disponibile = rs.getBoolean("disponibile");
                this.nomeCategoria = rs.getString("nome_categoria");
            } else {
                throw new SQLException("Pietanza non trovata con ID: " + this.idPietanza);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento della pietanza: " + e.getMessage());
        }
    }

    /**
     * Salva le informazioni della pietanza nel database
     * 
     * @param idPietanza l'ID della pietanza (se nuova) o 0 per auto-incremento
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB(int idPietanza) {
        int ret = 0;

        String query;
        if (idPietanza == 0) {
            // Insert di una nuova pietanza con auto-incremento dell'ID
            query = String.format(Locale.US,
                    "INSERT INTO pietanza (nome, prezzo, id_categoria, disponibile) " +
                            "VALUES ('%s', %.2f, %d, %b)",
                    this.nome, this.prezzo, this.idCategoria, this.disponibile);
        } else {
            // Insert con ID specificato
            query = String.format(Locale.US,
                    "INSERT INTO pietanza (id_pietanza, nome, prezzo, id_categoria, disponibile) " +
                            "VALUES (%d, '%s', %.2f, %d, %b)",
                    idPietanza, this.nome, this.prezzo, this.idCategoria, this.disponibile);
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
     * Aggiorna lo stato di disponibilità di una pietanza nel database
     * 
     * @param disponibile nuovo stato di disponibilità
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaDisponibilita(boolean disponibile) {
        try {
            String query = "UPDATE pietanza SET disponibile = " + (disponibile ? "TRUE" : "FALSE") +
                    " WHERE id_pietanza = " + this.idPietanza;

            System.out.println("Esecuzione query: " + query);
            int result = DBConnection.updateQuery(query);

            if (result > 0) {
                System.out.println("Stato della pietanza #" + this.idPietanza + " aggiornato nel database.");
            } else {
                System.err.println("Nessuna riga aggiornata per la pietanza #" + this.idPietanza);
            }

            return result;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nell'aggiornamento della disponibilità: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Recupera tutte le pietanze dal database
     * 
     * @return ArrayList di oggetti Pietanza
     */
    public ArrayList<Pietanza> getTuttePietanze() {
        ArrayList<Pietanza> listaPietanze = new ArrayList<>();
        String query = "SELECT p.*, c.nome as nome_categoria " +
                "FROM pietanza p " +
                "JOIN categoria_pietanza c ON p.id_categoria = c.id_categoria " +
                "ORDER BY p.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                Pietanza pietanza = new Pietanza(
                        rs.getInt("id_pietanza"),
                        rs.getString("nome"),
                        rs.getDouble("prezzo"),
                        rs.getInt("id_categoria"));
                pietanza.setDisponibile(rs.getBoolean("disponibile"));
                pietanza.setNomeCategoria(rs.getString("nome_categoria"));
                listaPietanze.add(pietanza);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle pietanze: " + e.getMessage());
        }

        return listaPietanze;
    }

    /**
     * Recupera le pietanze filtrate per categoria
     * 
     * @param idCategoria ID della categoria per cui filtrare
     * @return ArrayList di oggetti Pietanza della categoria specificata
     */
    public ArrayList<Pietanza> getPietanzePerCategoria(int idCategoria) {
        ArrayList<Pietanza> listaPietanze = new ArrayList<>();
        String query = "SELECT p.*, c.nome as nome_categoria " +
                "FROM pietanza p " +
                "JOIN categoria_pietanza c ON p.id_categoria = c.id_categoria " +
                "WHERE p.id_categoria = " + idCategoria + " " +
                "ORDER BY p.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                Pietanza pietanza = new Pietanza(
                        rs.getInt("id_pietanza"),
                        rs.getString("nome"),
                        rs.getDouble("prezzo"),
                        rs.getInt("id_categoria"));
                pietanza.setDisponibile(rs.getBoolean("disponibile"));
                pietanza.setNomeCategoria(rs.getString("nome_categoria"));
                listaPietanze.add(pietanza);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle pietanze per categoria: " + e.getMessage());
        }

        return listaPietanze;
    }

    // Getters e setters
    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
