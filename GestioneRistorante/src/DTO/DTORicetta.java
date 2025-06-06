package DTO;

import java.util.ArrayList;
import entity.EntityRicetta.IngredienteQuantita;


public class DTORicetta {
    private String nome;
    private String descrizione;
    private int tempoPreparazione; // in minuti
    private String istruzioni;
    private ArrayList<IngredienteQuantita> ingredienti;

    // Costruttore
    public DTORicetta(String nome, String descrizione, int tempoPreparazione, String istruzioni, ArrayList<IngredienteQuantita> ingredienti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.tempoPreparazione = tempoPreparazione;
        this.istruzioni = istruzioni;
        this.ingredienti = ingredienti;
    }
    public DTORicetta() {
        this.ingredienti = new ArrayList<>();
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(int tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }

    public String getIstruzioni() {
        return istruzioni;
    }

    public void setIstruzioni(String istruzioni) {
        this.istruzioni = istruzioni;
    }

    public ArrayList<IngredienteQuantita> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(ArrayList<IngredienteQuantita> ingredienti) {
        this.ingredienti = ingredienti;
    }

    // toString
    @Override
    public String toString() {
        return "DTORicetta{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", tempoPreparazione=" + tempoPreparazione +
                ", istruzioni='" + istruzioni + '\'' +
                ", ingredienti=" + ingredienti +
                '}';
    }
}

