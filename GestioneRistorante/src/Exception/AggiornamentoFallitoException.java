package Exception;

/**
 * Eccezione lanciata quando l'aggiornamento di un'entità fallisce
 */
public class AggiornamentoFallitoException extends DatabaseException {
    private String tipoEntita;
    private Object identificativo;

    public AggiornamentoFallitoException(String tipoEntita, Object identificativo, String message) {
        super("Impossibile aggiornare l'entità di tipo '" + tipoEntita + "' con identificativo '" +
                identificativo + "': " + message);
        this.tipoEntita = tipoEntita;
        this.identificativo = identificativo;
    }

    public AggiornamentoFallitoException(String tipoEntita, Object identificativo, String message, Throwable cause) {
        super("Impossibile aggiornare l'entità di tipo '" + tipoEntita + "' con identificativo '" +
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