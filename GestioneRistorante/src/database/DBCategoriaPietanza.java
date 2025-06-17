package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import CFG.DBConnection;

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
     * @throws EntitaDuplicataException se l'ID della categoria non esiste nel
     *                                  database
     */
    public DBCategoriaPietanza(int idCategoria) {
        this.idCategoria = idCategoria;
        caricaDaDB(); // Carico i dati della categoria
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
     * Recupera tutte le categorie pietanze dal database
     * 
     * @return ArrayList di oggetti CategoriaPietanza
     */
    public ArrayList<DBCategoriaPietanza> getTutteCategorie() {
        ArrayList<DBCategoriaPietanza> listaCategorie = new ArrayList<>();
        String query = "SELECT * FROM categoria_pietanza ORDER BY nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                DBCategoriaPietanza categoria = new DBCategoriaPietanza();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));
                listaCategorie.add(categoria);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle categorie: " + e.getMessage());
        }

        return listaCategorie;
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
