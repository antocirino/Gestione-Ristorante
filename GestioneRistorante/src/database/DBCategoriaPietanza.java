package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import CFG.DBConnection;
import entity.CategoriaPietanza;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'categoria_pietanza'
 * nel database
 */
public class DBCategoriaPietanza {
    // Attributi che mappano le colonne della tabella
    private int idCategoria;
    private String nome;

    /**
     * Costruttore che carica una categoria dal database tramite il suo ID
     * 
     * @param idCategoria l'ID della categoria da caricare
     */
    public DBCategoriaPietanza(int idCategoria) {
        this.idCategoria = idCategoria;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti categoria
     */
    public DBCategoriaPietanza() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati della categoria dal database
     */
    public void caricaDaDB() {
        // Definisco la query
        String query = "SELECT * FROM categoria_pietanza WHERE id_categoria = " + this.idCategoria;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
            } else {
                throw new SQLException("Categoria pietanza non trovata con ID: " + this.idCategoria);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento della categoria: " + e.getMessage());
        }
    }

    /**
     * Salva le informazioni della categoria nel database
     * 
     * @param idCategoria l'ID della categoria (se nuova) o 0 per auto-incremento
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB(int idCategoria) {
        int ret = 0;

        String query;
        if (idCategoria == 0) {
            // Insert di una nuova categoria con auto-incremento dell'ID
            query = String.format(Locale.US,
                    "INSERT INTO categoria_pietanza (nome) VALUES ('%s')",
                    this.nome);
        } else {
            // Insert con ID specificato
            query = String.format(Locale.US,
                    "INSERT INTO categoria_pietanza (id_categoria, nome) VALUES (%d, '%s')",
                    idCategoria, this.nome);
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
     * Aggiorna le informazioni della categoria nel database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaNelDB() {
        int ret = 0;

        String query = String.format(Locale.US,
                "UPDATE categoria_pietanza SET nome = '%s' WHERE id_categoria = %d",
                this.nome, this.idCategoria);

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
     * Recupera tutte le categorie pietanze dal database
     * 
     * @return ArrayList di oggetti CategoriaPietanza
     */
    public ArrayList<CategoriaPietanza> getTutteCategorie() {
        ArrayList<CategoriaPietanza> listaCategorie = new ArrayList<>();
        String query = "SELECT * FROM categoria_pietanza ORDER BY nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                CategoriaPietanza categoria = new CategoriaPietanza();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                listaCategorie.add(categoria);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle categorie: " + e.getMessage());
        }

        return listaCategorie;
    }

    /**
     * Recupera tutte le categorie come mappa ID->nome
     * 
     * @return Map con ID categoria come chiave e nome come valore
     */
    public Map<Integer, String> getCategorieMap() {
        Map<Integer, String> mappaCategorie = new HashMap<>();
        String query = "SELECT id_categoria, nome FROM categoria_pietanza ORDER BY nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                mappaCategorie.put(rs.getInt("id_categoria"), rs.getString("nome"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero della mappa categorie: " + e.getMessage());
        }

        return mappaCategorie;
    }

    // Getters e setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
