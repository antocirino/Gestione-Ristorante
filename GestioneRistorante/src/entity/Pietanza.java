package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta una pietanza del menu
 */
public class Pietanza {
    private int idPietanza;
    private String nome;
    private double prezzo;
    private int idCategoria;
    private String nomeCategoria;
    private List<IngredienteRicetta> ingredienti;

    // Costruttori
    public Pietanza() {
        this.ingredienti = new ArrayList<>();
    }

    public Pietanza(int idPietanza, String nome, double prezzo, int idCategoria) {
        this.idPietanza = idPietanza;
        this.nome = nome;
        this.prezzo = prezzo;
        this.idCategoria = idCategoria;
        this.ingredienti = new ArrayList<>();
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

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public List<IngredienteRicetta> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(List<IngredienteRicetta> ingredienti) {
        this.ingredienti = ingredienti;
    }

    public void aggiungiIngrediente(IngredienteRicetta ingrediente) {
        this.ingredienti.add(ingrediente);
    }

    @Override
    public String toString() {
        return nome + " (" + prezzo + "€)";
    }

    /**
     * Classe interna che rappresenta un ingrediente di una ricetta
     */
    public static class IngredienteRicetta {
        private Ingrediente ingrediente;
        private float quantita;

        public IngredienteRicetta(Ingrediente ingrediente, float quantita) {
            this.ingrediente = ingrediente;
            this.quantita = quantita;
        }

        public Ingrediente getIngrediente() {
            return ingrediente;
        }

        public void setIngrediente(Ingrediente ingrediente) {
            this.ingrediente = ingrediente;
        }

        public float getQuantita() {
            return quantita;
        }

        public void setQuantita(float quantita) {
            this.quantita = quantita;
        }
    }
}
