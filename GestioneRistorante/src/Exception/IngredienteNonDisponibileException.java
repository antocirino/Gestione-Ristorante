package Exception;

/**
 * Eccezione lanciata quando un ingrediente non è disponibile in quantità
 * sufficiente
 */
public class IngredienteNonDisponibileException extends RistoranteException {

    private String nomeIngrediente;
    private float quantitaRichiesta;
    private float quantitaDisponibile;

    public IngredienteNonDisponibileException(String nomeIngrediente, float quantitaRichiesta,
            float quantitaDisponibile) {
        super("Ingrediente '" + nomeIngrediente + "' non disponibile: richiesti " +
                quantitaRichiesta + " ma disponibili solo " + quantitaDisponibile);

        this.nomeIngrediente = nomeIngrediente;
        this.quantitaRichiesta = quantitaRichiesta;
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public float getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public float getQuantitaDisponibile() {
        return quantitaDisponibile;
    }
}
