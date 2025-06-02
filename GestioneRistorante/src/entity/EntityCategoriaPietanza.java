package entity;

import java.util.ArrayList;
import java.util.Map;

import database.DBCategoriaPietanza;

/**
 * Classe che rappresenta una categoria di pietanze nel ristorante
 */
public class EntityCategoriaPietanza {
    private int idCategoria;
    private String nome;

    /**
     * Costruttore vuoto
     */
    public EntityCategoriaPietanza() {
    }

    /**
     * Costruttore con nome
     * 
     * @param nome Nome della categoria
     */
    public EntityCategoriaPietanza(String nome) {
        this.nome = nome;
    }

    /**
     * Costruttore che carica una categoria dal database per ID
     * 
     * @param idCategoria ID della categoria da caricare
     */
    public EntityCategoriaPietanza(int idCategoria) {
        DBCategoriaPietanza categoria = new DBCategoriaPietanza(idCategoria);

        this.idCategoria = idCategoria;
        this.nome = categoria.getNome();
    }

    /**
     * Salva la categoria nel database
     * 
     * @param idCategoria ID della categoria (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int idCategoria) {
        DBCategoriaPietanza c = new DBCategoriaPietanza(); // DAO

        c.setNome(this.nome);

        int result = c.salvaInDB(idCategoria);

        return result;
    }

    /**
     * Aggiorna la categoria nel database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaSuDB() {
        if (this.idCategoria <= 0) {
            return -1; // Non possiamo aggiornare senza un ID valido
        }

        DBCategoriaPietanza c = new DBCategoriaPietanza(this.idCategoria); // DAO
        c.setNome(this.nome);

        return c.aggiornaNelDB();
    }

    /**
     * Recupera tutte le categorie pietanze dal database
     * 
     * @return ArrayList di oggetti CategoriaPietanza
     */
    public static ArrayList<EntityCategoriaPietanza> getTutteCategorie() {
        DBCategoriaPietanza categoria = new DBCategoriaPietanza();
        return categoria.getTutteCategorie();
    }

    /**
     * Recupera tutte le categorie come mappa ID->nome
     * 
     * @return Map con ID categoria come chiave e nome come valore
     */
    public static Map<Integer, String> getCategorieMap() {
        DBCategoriaPietanza categoria = new DBCategoriaPietanza();
        return categoria.getCategorieMap();
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

    @Override
    public String toString() {
        return nome;
    }
}
