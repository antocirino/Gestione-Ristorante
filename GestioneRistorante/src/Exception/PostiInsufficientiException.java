package Exception;

/**
 * Eccezione lanciata quando un tavolo ha un numero di posti insufficiente
 */
public class PostiInsufficientiException extends RistoranteException {

    private int numeroTavolo;
    private int postiRichiesti;
    private int postiDisponibili;

    public PostiInsufficientiException(int numeroTavolo, int postiRichiesti, int postiDisponibili) {
        super("Il tavolo numero " + numeroTavolo + " ha solo " + postiDisponibili +
                " posti, ma ne sono stati richiesti " + postiRichiesti);

        this.numeroTavolo = numeroTavolo;
        this.postiRichiesti = postiRichiesti;
        this.postiDisponibili = postiDisponibili;
    }

    public int getNumeroTavolo() {
        return numeroTavolo;
    }

    public int getPostiRichiesti() {
        return postiRichiesti;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }
}
