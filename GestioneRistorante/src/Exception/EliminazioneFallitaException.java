package Exception;

/**
 * Eccezione lanciata quando l'eliminazione di un'entità fallisce
 */
public class EliminazioneFallitaException extends DatabaseException {
    private String tipoEntita;
    private Object identificativo;

    public EliminazioneFallitaException(String tipoEntita, Object identificativo, String message) {
        super("Impossibile eliminare l'entità di tipo '" + tipoEntita + "' con identificativo '" +
                identificativo + "': " + message);
        this.tipoEntita = tipoEntita;
        this.identificativo = identificativo;
    }

    public EliminazioneFallitaException(String tipoEntita, Object identificativo, String message, Throwable cause) {
        super("Impossibile eliminare l'entità di tipo '" + tipoEntita + "' con identificativo '" +
                identificativo + "': " + message, cause);
        this.tipoEntita = tipoEntita;
        this.identificativo = identificativo;
    }

    public String getTipoEntita() {
        return tipoEntita;
    }

    public Object getIdentificativo() {
        return identificativo;
    }
}