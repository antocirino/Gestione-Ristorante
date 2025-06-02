package Exception;

/**
 * Eccezione lanciata quando si verifica un errore di connessione al database
 */
public class ConnessioneDBException extends DatabaseException {
    public ConnessioneDBException(String message) {
        super(message);
    }

    public ConnessioneDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
