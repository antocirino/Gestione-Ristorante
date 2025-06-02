package Exception;

/**
 * Eccezione lanciata quando un prodotto del menu non è trovato
 */
public class ProdottoMenuNonTrovatoException extends EntitaNonTrovataException {
    public ProdottoMenuNonTrovatoException(Object identificativo) {
        super("ProdottoMenu", identificativo);
    }
}
