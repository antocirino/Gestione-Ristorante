package entity;

import java.util.ArrayList;

import database.DBIngrediente;

/**
 * Classe che rappresenta un ingrediente del magazzino
 */
public class Ingrediente {
    private int idIngrediente;
    private String nome;
    private float quantitaDisponibile;
    private String unitaMisura;
    private float sogliaRiordino;

    // Costruttori
    public Ingrediente() {
    }

    public Ingrediente(int idIngrediente) {

        DBIngrediente ingrediente = new DBIngrediente(idIngrediente);

        this.nome = ingrediente.getNome();
        this.quantitaDisponibile = ingrediente.getQuantitaDisponibile();
        this.unitaMisura = ingrediente.getUnitaMisura();
        this.sogliaRiordino = ingrediente.getSogliaRiordino();
        this.idIngrediente = idIngrediente;
    }


    public int scriviSuDB(int id_ingrediente) {
		
		DBIngrediente s= new DBIngrediente(); //DAO
		
		s.setNome(this.nome);
		s.setQuantitaDisponibile(this.quantitaDisponibile);
        s.setUnitaMisura(this.unitaMisura);
        s.setSogliaRiordino(this.sogliaRiordino);

		int i = s.salvaInDB(id_ingrediente);
		
		return i;
	}

    public static ArrayList<Ingrediente> getIngredientiEsauriti() {
        DBIngrediente ingrediente = new DBIngrediente();   
        return ingrediente.getIngredientiEsauriti();
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
