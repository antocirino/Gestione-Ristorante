package Exception;

/**
 * Eccezione lanciata quando il salvataggio di un'entità fallisce
 */
public class SalvataggioFallitoException extends DatabaseException {
    private String tipoEntita;

    public SalvataggioFallitoException(String tipoEntita, String message) {
        super("Impossibile salvare l'entità di tipo '" + tipoEntita + "': " + message);
        this.tipoEntita = tipoEntita;
    }

    public SalvataggioFallitoException(String tipoEntita, String message, Throwable cause) {
        super("Impossibile salvare l'entità di tipo '" + tipoEntita + "': " + message, cause);
        this.tipoEntita = tipoEntita;
    }

    public String getTipoEntita() {
        return tipoEntita;
    }
}