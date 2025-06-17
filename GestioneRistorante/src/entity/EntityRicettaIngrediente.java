package entity;

import database.DBRicettaIngrediente;
import java.util.ArrayList;

/**
 * Classe che rappresenta l'associazione tra una ricetta e i suoi ingredienti e
 * la quantità di ciascun ingrediente
 */
public class EntityRicettaIngrediente {
    // Attributi
    private int idRicetta;
    private int idIngrediente;
    private float quantita;

    // Costruttori

    /**
     * Costruttore vuoto
     */
    public EntityRicettaIngrediente() {
        // Costruttore vuoto
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param idRicetta     ID della ricetta (0 per auto-incremento)
     * @param idIngrediente ID dell'ingrediente (0 per auto-incremento)
     * @param quantita      Quantità dell'ingrediente nella ricetta
     */
    public EntityRicettaIngrediente(int idRicetta, int idIngrediente, float quantita) {
        this.idRicetta = idRicetta;
        this.idIngrediente = idIngrediente;
        this.quantita = quantita;
    }

    // Getter e Setter
    public int getIdRicetta() {
        return idRicetta;
    }

    public void setIdRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public float getQuantita() {
        return quantita;
    }

    public void setQuantita(float quantita) {
        this.quantita = quantita;
    }

    /**
     * Recupera tutti gli ingredienti di una ricetta
     * 
     * @param idRicetta ID della ricetta di cui recuperare gli ingredienti
     * @return Lista di EntityRicettaIngrediente associati alla ricetta
     */
    public static ArrayList<EntityRicettaIngrediente> getIngredientiPerRicetta(int idRicetta) {
        ArrayList<EntityRicettaIngrediente> listaEntity = new ArrayList<>();
        ArrayList<DBRicettaIngrediente> listaDB = DBRicettaIngrediente.getIngredientiPerRicetta(idRicetta);

        for (DBRicettaIngrediente dbItem : listaDB) {
            EntityRicettaIngrediente entityItem = new EntityRicettaIngrediente(
                    dbItem.getIdRicetta(),
                    dbItem.getIdIngrediente(),
                    dbItem.getQuantita());
            listaEntity.add(entityItem);
        }

        return listaEntity;
    }

    // toString
    @Override
    public String toString() {
        return "EntityRicettaIngrediente{" +
                "idRicetta=" + idRicetta +
                ", idIngrediente=" + idIngrediente +
                ", quantita=" + quantita +
                '}';
    }
}