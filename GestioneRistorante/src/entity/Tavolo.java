package entity;

/**
 * Classe che rappresenta un tavolo del ristorante
 */
public class Tavolo {
    private int idTavolo;
    private int numeroTavolo;
    private int maxPosti;
    private boolean occupato;

    // Costruttori
    public Tavolo() {
    }

    public Tavolo(int idTavolo, int numeroTavolo, int maxPosti, boolean occupato) {
        this.idTavolo = idTavolo;
        this.numeroTavolo = numeroTavolo;
        this.maxPosti = maxPosti;
        this.occupato = occupato;
    }

    // Getters e setters
    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getNumeroTavolo() {
        return numeroTavolo;
    }

    public void setNumeroTavolo(int numeroTavolo) {
        this.numeroTavolo = numeroTavolo;
    }

    public int getMaxPosti() {
        return maxPosti;
    }

    public void setMaxPosti(int maxPosti) {
        this.maxPosti = maxPosti;
    }

    public boolean isOccupato() {
        return occupato;
    }

    public void setOccupato(boolean occupato) {
        this.occupato = occupato;
    }

    @Override
    public String toString() {
        return "Tavolo " + numeroTavolo + " (max " + maxPosti + " posti)" +
                (occupato ? " - Occupato" : " - Libero");
    }
}
