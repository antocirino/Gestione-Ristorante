package entity;

import java.util.ArrayList;

import DTO.DTOIngrediente;
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
     * Recupera tutti gli ingredienti sotto la soglia di riordino
     * 
     * @return ArrayList di oggetti DTOIngrediente
     */
    public static ArrayList<DTOIngrediente> getIngredientiEsauriti() {
        ArrayList<DTOIngrediente> listaIngredienti = new ArrayList<>();
        ArrayList<DBIngrediente> lista_ingredienti = DBIngrediente.getIngredientiEsauriti();
        for (DBIngrediente o : lista_ingredienti) {
            DTOIngrediente dto = new DTOIngrediente();
            dto.setIdIngrediente(o.getIdIngrediente());
            dto.setNome(o.getNome());
            dto.setQuantitaDisponibile(o.getQuantitaDisponibile());
            dto.setUnitaMisura(o.getUnitaMisura());
            dto.setSogliaRiordino(o.getSogliaRiordino());
            listaIngredienti.add(dto);
        }
        return listaIngredienti;
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

    @Override
    public String toString() {
        return nome + " (" + quantitaDisponibile + " " + unitaMisura + ")";
    }
}
