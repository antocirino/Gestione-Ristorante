package DTO;

public class DTOIngrediente {
    private int idIngrediente;
    private String nome;
    private float quantitaDisponibile;
    private String unitaMisura;
    private float sogliaRiordino;

    // Costruttore vuoto
    public DTOIngrediente() {
    }

    // Costruttore completo
    public DTOIngrediente(int idIngrediente, String nome, float quantitaDisponibile, String unitaMisura, float sogliaRiordino) {
        this.idIngrediente = idIngrediente;
        this.nome = nome;
        this.quantitaDisponibile = quantitaDisponibile;
        this.unitaMisura = unitaMisura;
        this.sogliaRiordino = sogliaRiordino;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(float quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public float getSogliaRiordino() {
        return sogliaRiordino;
    }

    public void setSogliaRiordino(float sogliaRiordino) {
        this.sogliaRiordino = sogliaRiordino;
    }

    @Override
    public String toString() {
        return "DTOIngrediente{" +
                "idIngrediente=" + idIngrediente +
                ", nome='" + nome + '\'' +
                ", quantitaDisponibile=" + quantitaDisponibile +
                ", unitaMisura='" + unitaMisura + '\'' +
                ", sogliaRiordino=" + sogliaRiordino +
                '}';
    }
}
