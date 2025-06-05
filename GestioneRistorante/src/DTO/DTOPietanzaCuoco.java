package DTO;

public class DTOPietanzaCuoco {
    
    private String nome;
    private int quantita;

    // Costruttore vuoto
    public DTOPietanzaCuoco() {
    }

    // Costruttore completo
    public DTOPietanzaCuoco(String nome, int quantita) {
        this.nome = nome;
        this.quantita = quantita;
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    // toString ridefinito
    @Override
    public String toString() {
        return "DTOPietanzaCuoco{" +
                "nome='" + nome + '\'' +
                ", quantita=" + quantita +
                '}';
    }
}
