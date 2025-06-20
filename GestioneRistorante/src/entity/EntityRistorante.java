package entity;


import database.DBRistorante;

/**
 * Classe che rappresenta un ristorante
 */
public class EntityRistorante {
    private int idRistorante;
    private String nome;
    private int numeroTavoli;
    private double costoCoperto;

    /**
     * Costruttore vuoto
     */
    public EntityRistorante() {
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param nome         Nome del ristorante
     * @param numeroTavoli Numero di tavoli disponibili
     * @param costoCoperto Costo del coperto
     */
    public EntityRistorante(String nome, int numeroTavoli, double costoCoperto) {
        this.nome = nome;
        this.numeroTavoli = numeroTavoli;
        this.costoCoperto = costoCoperto;
    }

    /**
     * Costruttore completo
     * 
     * @param idRistorante ID del ristorante
     * @param nome         Nome del ristorante
     * @param numeroTavoli Numero di tavoli disponibili
     * @param costoCoperto Costo del coperto
     */
    public EntityRistorante(int idRistorante, String nome, int numeroTavoli, double costoCoperto) {
        this.idRistorante = idRistorante;
        this.nome = nome;
        this.numeroTavoli = numeroTavoli;
        this.costoCoperto = costoCoperto;
    }

    /**
     * Costruttore che carica un ristorante dal database per ID
     * 
     * @param idRistorante ID del ristorante da caricare
     */
    public EntityRistorante(int idRistorante) {
        DBRistorante ristorante = new DBRistorante(idRistorante);

        this.idRistorante = idRistorante;
        this.nome = ristorante.getNome();
        this.numeroTavoli = ristorante.getNumeroTavoli();
        this.costoCoperto = ristorante.getCostoCoperto();
    }

    /**
     * Salva il ristorante nel database
     * 
     * @param idRistorante ID del ristorante (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int idRistorante) {
        DBRistorante r = new DBRistorante(); // DAO

        r.setNome(this.nome);
        r.setNumeroTavoli(this.numeroTavoli);
        r.setCostoCoperto(this.costoCoperto);

        int result = r.salvaInDB(idRistorante);

        // Aggiorna l'ID se si tratta di un nuovo ristorante
        if (idRistorante == 0 && result > 0) {
            this.idRistorante = result;
        }

        return result;
    }

    /**
     * Inizializza i tavoli del ristorante
     * 
     * @param postiPerTavolo Array con il numero di posti per ciascun tavolo
     * @return true se l'inizializzazione è avvenuta con successo
     */
    public boolean inizializzaTavoli(int[] postiPerTavolo) {
        if (postiPerTavolo.length != this.numeroTavoli) {
            return false;
        }

        boolean success = true;
        for (int i = 0; i < postiPerTavolo.length; i++) {
            EntityTavolo t = new EntityTavolo();
            t.setMaxPosti(postiPerTavolo[i]);
            t.setStato("libero");
            t.setIdRistorante(this.idRistorante);
            t.setIdRistorante(i + 1); // Numero del tavolo (1-based)

            if (t.scriviSuDB(0) <= 0) {
                success = false;
            }
        }
        return success;
    }

    /**
     * Elimina un ristorante dal database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int eliminaDaDB() {
        DBRistorante r = new DBRistorante();
        return r.eliminaDaDB(this.idRistorante);
    }

  
    // Getters e setters
    public int getIdRistorante() {
        return idRistorante;
    }

    public void setIdRistorante(int idRistorante) {
        this.idRistorante = idRistorante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroTavoli() {
        return numeroTavoli;
    }

    public void setNumeroTavoli(int numeroTavoli) {
        this.numeroTavoli = numeroTavoli;
    }

    public double getCostoCoperto() {
        return costoCoperto;
    }

    public void setCostoCoperto(double costoCoperto) {
        this.costoCoperto = costoCoperto;
    }

    @Override
    public String toString() {
        return nome + " (Tavoli: " + numeroTavoli + ", Coperto: " + costoCoperto + "€)";
    }
}
