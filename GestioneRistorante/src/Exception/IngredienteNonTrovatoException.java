package Exception;

/**
 * Eccezione lanciata quando un ingrediente non è trovato
 */
public class IngredienteNonTrovatoException extends EntitaNonTrovataException {
    public IngredienteNonTrovatoException(Object identificativo) {
        super("Ingrediente", identificativo);
    }
}
