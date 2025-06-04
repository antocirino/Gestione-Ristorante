package DTO;

import java.util.Date;

public class DTOOrdine {
    //ATTRIBUTI
    private int idOrdine;
    private int idTavolo;
    private int numPersone;
    private Date dataOrdine;
    private String stato; // in_attesa, in_preparazione, pronto, consegnato, pagato
    private double costoTotale; 

    // GETTER E SETTER
    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getNumPersone() {
        return numPersone;
    }

    public void setNumPersone(int numPersone) {
        this.numPersone = numPersone;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public double getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(double costoTotale) {
        this.costoTotale = costoTotale;
    }

    // toString
    @Override
    public String toString() {
        return "DTOOrdine{" +
                "idOrdine=" + idOrdine +
                ", idTavolo=" + idTavolo +
                ", numPersone=" + numPersone +
                ", dataOrdine=" + dataOrdine +
                ", stato='" + stato + '\'' +
                ", costoTotale=" + costoTotale +
                '}';
    }
}
