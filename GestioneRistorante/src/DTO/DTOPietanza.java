package DTO;

import java.util.List;

public class DTOPietanza {
    //ATTRIBUTI
    private int idPietanza;
    private String nome;
    private double prezzo;
    private int idCategoria;
    private String nomeCategoria;
    private boolean disponibile;
    private List<Object> ingredienti;

    // GETTER E SETTER
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

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public List<Object> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(List<Object> ingredienti) {
        this.ingredienti = ingredienti;
    }

    // toString
    @Override
    public String toString() {
        return "DTOPietanza{" +
                "idPietanza=" + idPietanza +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", idCategoria=" + idCategoria +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", disponibile=" + disponibile +
                ", ingredienti=" + ingredienti +
                '}';
    }
}
