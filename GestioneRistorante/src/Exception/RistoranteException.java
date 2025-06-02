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