package Exception;

/**
 * Eccezione lanciata quando si verifica un errore di query al database
 */
public class QueryDBException extends DatabaseException {
    private String query;

    public QueryDBException(String message, String query) {
        super(message);
        this.query = query;
    }

    public QueryDBException(String message, String query, Throwable cause) {
        super(message, cause);
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
