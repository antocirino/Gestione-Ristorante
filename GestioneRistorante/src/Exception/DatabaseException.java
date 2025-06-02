package Exception;

/**
 * Eccezione lanciata quando si verifica un errore di database
 */
public class DatabaseException extends RistoranteException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
