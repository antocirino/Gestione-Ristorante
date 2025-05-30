package Exception;

/**
 * Classe base per le eccezioni personalizzate del sistema di gestione
 * ristorante
 */
public class RistoranteException extends Exception {

    public RistoranteException(String message) {
        super(message);
    }

    public RistoranteException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * Eccezione lanciata quando un ingrediente non è disponibile in quantità
 * sufficiente
 */
class IngredienteNonDisponibileException extends RistoranteException {

    private String nomeIngrediente;
    private float quantitaRichiesta;
    private float quantitaDisponibile;

    public IngredienteNonDisponibileException(String nomeIngrediente, float quantitaRichiesta,
            float quantitaDisponibile) {
        super("Ingrediente '" + nomeIngrediente + "' non disponibile: richiesti " +
                quantitaRichiesta + " ma disponibili solo " + quantitaDisponibile);

        this.nomeIngrediente = nomeIngrediente;
        this.quantitaRichiesta = quantitaRichiesta;
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public float getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public float getQuantitaDisponibile() {
        return quantitaDisponibile;
    }
}

/**
 * Eccezione lanciata quando un tavolo è già occupato
 */
class TavoloOccupatoException extends RistoranteException {

    private int numeroTavolo;

    public TavoloOccupatoException(int numeroTavolo) {
        super("Il tavolo numero " + numeroTavolo + " è già occupato");
        this.numeroTavolo = numeroTavolo;
    }

    public int getNumeroTavolo() {
        return numeroTavolo;
    }
}

/**
 * Eccezione lanciata quando un tavolo ha un numero di posti insufficiente
 */
class PostiInsufficientiException extends RistoranteException {

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

/**
 * Eccezione lanciata quando si verifica un errore di database
 */
class DatabaseException extends RistoranteException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
