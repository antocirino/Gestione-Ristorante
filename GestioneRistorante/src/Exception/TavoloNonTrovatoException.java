package Exception;

/**
 * Eccezione lanciata quando un tavolo non è trovato
 */
public class TavoloNonTrovatoException extends EntitaNonTrovataException {
    public TavoloNonTrovatoException(Object identificativo) {
        super("Tavolo", identificativo);
    }
}