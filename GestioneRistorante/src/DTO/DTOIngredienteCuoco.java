package DTO;

public class DTOIngredienteCuoco {
    
    private String nome;
    private float qta_necessaria;
    private String unitaMisura;

    // Costruttore vuoto
    public DTOIngredienteCuoco() {
    }

    // Costruttore con parametri
    public DTOIngredienteCuoco(String nome, float qta_necessaria, String unitaMisura) {
        this.nome = nome;
        this.qta_necessaria = qta_necessaria;
        this.unitaMisura = unitaMisura;
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getQta_necessaria() {
        return qta_necessaria;
    }

    public void setQta_necessaria(float qta_necessaria) {
        this.qta_necessaria = qta_necessaria;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    // toString
    @Override
    public String toString() {
        return "DTOIngredienteCuoco{" +
                "nome='" + nome + '\'' +
                ", qta_necessaria=" + qta_necessaria +
                ", unitaMisura='" + unitaMisura + '\'' +
                '}';
    }
}
