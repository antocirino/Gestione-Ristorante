package Exception;

/**
 * Eccezione lanciata quando un tavolo è già occupato
 */
public class TavoloOccupatoException extends RistoranteException {

    private int numeroTavolo;

    public TavoloOccupatoException(int numeroTavolo) {
        super("Il tavolo numero " + numeroTavolo + " è già occupato");
        this.numeroTavolo = numeroTavolo;
    }

    public int getNumeroTavolo() {
        return numeroTavolo;
    }
}
