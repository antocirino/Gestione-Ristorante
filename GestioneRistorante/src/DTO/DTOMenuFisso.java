package DTO;

import java.util.ArrayList;

public class DTOMenuFisso {

    // ATTRIBUTI
    private int idMenu;
    private String nome;
    private double prezzo;
    private String descrizione;
    private ArrayList<DTOPietanza> pietanze;

    // GETTER E SETTER

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public ArrayList<DTOPietanza> getPietanze() {
        return pietanze;
    }

    public void setPietanze(ArrayList<DTOPietanza> pietanze) {
        this.pietanze = pietanze;
    }

    // METODO toString
    @Override
    public String toString() {
        return "{" +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", descrizione='" + descrizione + '\'' +
                ", pietanze=" + pietanze +
                '}';
    }
}
