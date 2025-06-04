package entity;

import java.util.ArrayList;

import database.DBIngrediente;

/**
 * Classe che rappresenta un ingrediente del magazzino
 */
public class EntityIngrediente {
    private int idIngrediente;
    private String nome;
    private float quantitaDisponibile;
    private String unitaMisura;
    private float sogliaRiordino;

    // Costruttori
    public EntityIngrediente() {
    }

    /**
     * Costruttore che carica un ingrediente dal database per ID
     * 
     * @param idIngrediente ID dell'ingrediente da caricare
     */
    public EntityIngrediente(int idIngrediente) {

        DBIngrediente ingrediente = new DBIngrediente(idIngrediente);

        this.nome = ingrediente.getNome();
        this.quantitaDisponibile = ingrediente.getQuantitaDisponibile();
        this.unitaMisura = ingrediente.getUnitaMisura();
        this.sogliaRiordino = ingrediente.getSogliaRiordino();
        this.idIngrediente = idIngrediente;
    }

    /**
     * Salva l'ingrediente nel database
     * 
     * @param id_ingrediente ID dell'ingrediente (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int id_ingrediente) {

        DBIngrediente s = new DBIngrediente(); // DAO

        s.setNome(this.nome);
        s.setQuantitaDisponibile(this.quantitaDisponibile);
        s.setUnitaMisura(this.unitaMisura);
        s.setSogliaRiordino(this.sogliaRiordino);

        int i = s.salvaInDB(id_ingrediente);

        return i;
    }

    public static ArrayList<EntityIngrediente> getIngredientiEsauriti() {
        return DBIngrediente.getIngredientiEsauriti();
    }

    /**
     * Recupera tutti gli ingredienti sotto la soglia di riordino
     * 
     * @return ArrayList di oggetti Ingrediente sotto soglia
     */
    public static ArrayList<EntityIngrediente> getIngredientiSottoSoglia() {
        return DBIngrediente.getIngredientiSottoSoglia();
    }

    /**
     * Prenota una quantità di ingrediente per l'utilizzo in una ricetta
     * 
     * @param quantita Quantità da prenotare
     * @return true se la prenotazione è avvenuta con successo, false altrimenti
     */
    public boolean prenotaIngrediente(float quantita) {
        if (this.quantitaDisponibile < quantita) {
            return false;
        }

        // Aggiorna la quantità disponibile
        this.quantitaDisponibile -= quantita;

        // Aggiorna il database
        DBIngrediente dbIngrediente = new DBIngrediente(this.idIngrediente);
        dbIngrediente.setQuantitaDisponibile(this.quantitaDisponibile);
        return dbIngrediente.aggiornaQuantita() > 0;
    }

    /**
     * Verifica se l'ingrediente è disponibile nella quantità richiesta
     * 
     * @param quantita Quantità richiesta
     * @return true se la quantità è disponibile, false altrimenti
     */
    public boolean isDisponibile(float quantita) {
        return this.quantitaDisponibile >= quantita;
    }

    // Getters e setters
    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(float quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public float getSogliaRiordino() {
        return sogliaRiordino;
    }

    public void setSogliaRiordino(float sogliaRiordino) {
        this.sogliaRiordino = sogliaRiordino;
    }

    public boolean daSottoScorta() {
        return quantitaDisponibile <= sogliaRiordino;
    }

    @Override
    public String toString() {
        return nome + " (" + quantitaDisponibile + " " + unitaMisura + ")";
    }
}
