package entity;

import java.util.ArrayList;

import DTO.DTOCategoriaPietanza;
import database.DBCategoriaPietanza;

/**
 * Classe che rappresenta una categoria di pietanze nel ristorante
 */
public class EntityCategoriaPietanza {
    private int idCategoria;
    private String nome;

    /**
     * Recupera tutte le categorie pietanze dal database
     * 
     * @return ArrayList di oggetti CategoriaPietanza
     */
    public static ArrayList<DTOCategoriaPietanza> getTutteCategorie() {
        DBCategoriaPietanza categoria = new DBCategoriaPietanza();
        ArrayList<DTOCategoriaPietanza> categorie = new ArrayList<>();
        ArrayList<DBCategoriaPietanza> dbCategorie = categoria.getTutteCategorie();

        for (DBCategoriaPietanza dbCategoria : dbCategorie) {
            DTOCategoriaPietanza dto = new DTOCategoriaPietanza();
            dto.setIdCategoria(dbCategoria.getIdCategoria());
            dto.setNome(dbCategoria.getNome());
            categorie.add(dto);
        }

        return categorie;

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
