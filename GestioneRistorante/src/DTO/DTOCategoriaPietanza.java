package DTO;

public class DTOCategoriaPietanza {
    private int idCategoria;
    private String nome;

    /**
     * Costruttore vuoto
     */
    public DTOCategoriaPietanza() {
        // Costruttore vuoto
    }

    /**
     * Costruttore con parametri
     * 
     * @param idCategoria ID della categoria
     * @param nome        Nome della categoria
     */
    public DTOCategoriaPietanza(int idCategoria, String nome) {
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

    // Getters e Setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "DTOCategoriaPietanza{" +
                "idCategoria=" + idCategoria +
                ", nome='" + nome + '\'' +
                '}';
    }
}
