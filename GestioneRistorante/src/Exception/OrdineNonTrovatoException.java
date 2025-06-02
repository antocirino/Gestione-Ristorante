package Exception;

/**
 * Eccezione lanciata quando un ordine non è trovato
 */
public class OrdineNonTrovatoException extends EntitaNonTrovataException {
    public OrdineNonTrovatoException(Object identificativo) {
        super("Ordine", identificativo);
    }
}
