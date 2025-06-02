package Exception;

/**
 * Eccezione lanciata quando un'operazione DAO non è supportata
 */
public class OperazioneNonSupportataException extends RistoranteException {
    private String operazione;
    private String motivo;

    public OperazioneNonSupportataException(String operazione, String motivo) {
        super("L'operazione '" + operazione + "' non è supportata: " + motivo);
        this.operazione = operazione;
        this.motivo = motivo;
    }

    public String getOperazione() {
        return operazione;
    }

    public String getMotivo() {
        return motivo;
    }
}
