package DTO;

import java.util.ArrayList;

public class DTOIngredientiRicettaPietanza {
    private String nome_ricetta;
    private String nome_pietanza;
    private String descrizione;
    private int tempoPreparazione; // in minuti
    private String istruzioni;
    private ArrayList<DTOIngredienteCuoco> ingredienti;

    // Costruttore vuoto
    public DTOIngredientiRicettaPietanza() {
        this.ingredienti = new ArrayList<>();
    }

    // Costruttore con parametri
    public DTOIngredientiRicettaPietanza(String nome_ricetta, String nome_pietanza, String descrizione, int tempoPreparazione, String istruzioni, ArrayList<DTOIngredienteCuoco> ingredienti) {
        this.nome_ricetta = nome_ricetta;
        this.nome_pietanza = nome_pietanza;
        this.descrizione = descrizione;
        this.tempoPreparazione = tempoPreparazione;
        this.istruzioni = istruzioni;
        this.ingredienti = ingredienti;
    }

    // Getter e Setter
    public String getNome_ricetta() {
        return nome_ricetta;
    }

    public void setNome_ricetta(String nome_ricetta) {
        this.nome_ricetta = nome_ricetta;
    }

    public String getNome_pietanza() {
        return nome_pietanza;
    }

    public void setNome_pietanza(String nome_pietanza) {
        this.nome_pietanza = nome_pietanza;
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

    public ArrayList<DTOIngredienteCuoco> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(ArrayList<DTOIngredienteCuoco> ingredienti) {
        this.ingredienti = ingredienti;
    }

    // toString
    @Override
    public String toString() {
        return "DTOIngredientiRicettaPietanza{" +
                "nome_ricetta='" + nome_ricetta + '\'' +
                ", nome_pietanza='" + nome_pietanza + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", tempoPreparazione=" + tempoPreparazione +
                ", istruzioni='" + istruzioni + '\'' +
                ", ingredienti=" + ingredienti +
                '}';
    }
}
