package Exception;

/**
 * Eccezione lanciata quando un'entità non viene trovata nel database
 */
public class EntitaNonTrovataException extends RistoranteException {
    private String tipoEntita;
    private Object identificativo;

    public EntitaNonTrovataException(String tipoEntita, Object identificativo) {
        super("L'entità di tipo '" + tipoEntita + "' con identificativo '" + identificativo + "' non è stata trovata");
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
