package Exception;

/**
 * Eccezione lanciata quando si tenta di inserire un'entità già esistente
 */
public class EntitaDuplicataException extends RistoranteException {
    private String tipoEntita;
    private Object identificativo;

    public EntitaDuplicataException(String tipoEntita, Object identificativo) {
        super("L'entità di tipo '" + tipoEntita + "' con identificativo '" + identificativo + "' esiste già");
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
