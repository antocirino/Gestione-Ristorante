package DTO;

public class DTOMenuFissoCuoco {
    private String nome;
    private int quantita;

    public DTOMenuFissoCuoco() {
        // Costruttore vuoto
    }

    public DTOMenuFissoCuoco(String nome, int quantita) {
        this.nome = nome;
        this.quantita = quantita;
    }

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
    @Override
    public String toString() {
        return "DTOMenuFissoCuoco{" +
                "nome='" + nome + '\'' +
                ", quantita=" + quantita +
                '}';
    }


}
