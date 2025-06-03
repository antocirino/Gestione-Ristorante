package DTO;

public class DTOTavolo {
    //ATTRIBUTI
    private int idTavolo;
    private int maxPosti;
    private String stato;
    //non mostriamo l'idRistorante in DTO nell'interfaccia, ma lo usiamo per le operazioni di database

    //COSTRUTTORE
    public DTOTavolo(int idTavolo, int maxPosti, String stato) {
        this.idTavolo = idTavolo;
        this.maxPosti = maxPosti;
        this.stato = stato;
    }
    // GETTER E SETTER
    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getMaxPosti() {
        return maxPosti;
    }

    public void setMaxPosti(int maxPosti) {
        this.maxPosti = maxPosti;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    // toString
    @Override
    public String toString() {
        return "DTOTavolo{" +
                "idTavolo=" + idTavolo +
                ", maxPosti=" + maxPosti +
                ", stato='" + stato + '\'' +
                '}';
    }
    public boolean isOccupato() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOccupato'");
    }
}
