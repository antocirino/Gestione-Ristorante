package entity;

import java.util.ArrayList;
import java.util.Map;

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

    /**
     * Recupera tutti i ristoranti dal database
     * 
     * @return ArrayList di oggetti Ristorante
     */
    public static ArrayList<EntityRistorante> getTuttiRistoranti() {
        DBRistorante r = new DBRistorante();
        return r.getTuttiRistoranti();
    }

    /**
     * Recupera un ristorante dal database come mappa
     * 
     * @param idRistorante ID del ristorante da recuperare
     * @return Mappa con gli attributi del ristorante
     */
    public static Map<String, Object> getRistoranteAsMap(int idRistorante) {
        DBRistorante r = new DBRistorante();
        return r.getRistoranteAsMap(idRistorante);
    }

    /**
     * Crea un nuovo menu per il ristorante
     * 
     * @param nomePietanze Lista dei nomi delle pietanze
     * @param prezzi       Lista dei prezzi delle pietanze
     * @param categorie    Lista delle categorie delle pietanze
     * @return true se il menu è stato creato con successo, false altrimenti
     */
    public boolean creaMenu(ArrayList<String> nomePietanze, ArrayList<Double> prezzi, ArrayList<Integer> categorie) {
        try {
            // Verifica che tutte le liste abbiano la stessa dimensione
            if (nomePietanze == null || prezzi == null || categorie == null) {
                System.err.println("Errore: Parametri null nella creazione del menu");
                return false;
            }

            if (nomePietanze.size() != prezzi.size() || nomePietanze.size() != categorie.size()) {
                System.err.println("Errore: Le liste fornite hanno dimensioni diverse");
                System.err.println("Nomi pietanze: " + nomePietanze.size() +
                        ", Prezzi: " + prezzi.size() +
                        ", Categorie: " + categorie.size());
                return false;
            }

            if (nomePietanze.isEmpty()) {
                System.err.println("Errore: Non ci sono pietanze da aggiungere al menu");
                return false;
            }

            System.out.println("Creazione menu con " + nomePietanze.size() + " pietanze...");
            boolean result = true;
            ArrayList<Integer> pietanzeCreate = new ArrayList<>();

            // Crea ogni pietanza del menu
            for (int i = 0; i < nomePietanze.size(); i++) {
                String nome = nomePietanze.get(i);
                Double prezzo = prezzi.get(i);
                Integer categoria = categorie.get(i);

                // Validazione dei dati
                if (nome == null || nome.trim().isEmpty()) {
                    System.err.println("Errore: Nome pietanza vuoto all'indice " + i);
                    continue;
                }

                if (prezzo == null || prezzo <= 0) {
                    System.err.println("Errore: Prezzo non valido per " + nome + ": " + prezzo);
                    continue;
                }

                if (categoria == null || categoria <= 0) {
                    System.err.println("Errore: Categoria non valida per " + nome + ": " + categoria);
                    continue;
                }

                // Crea e salva la pietanza
                EntityPietanza p = new EntityPietanza();
                p.setNome(nome);
                p.setPrezzo(prezzo);
                p.setIdCategoria(categoria);
                p.setDisponibile(true);

                int idPietanza = p.scriviSuDB(0);
                if (idPietanza > 0) {
                    System.out.println("Aggiunta pietanza: " + nome + " (" + prezzo + "€)");
                    pietanzeCreate.add(idPietanza);
                } else {
                    System.err.println("Errore: Impossibile salvare la pietanza: " + nome);
                    result = false;
                }
            }

            if (pietanzeCreate.isEmpty()) {
                System.err.println("Nessuna pietanza è stata creata con successo");
                return false;
            }

            // Collega le pietanze al ristorante attraverso la tabella menu
            DBRistorante dbr = new DBRistorante(this.idRistorante);
            boolean linkResult = dbr.collegaPietanzeAlMenu(this.idRistorante);

            if (!linkResult) {
                System.err.println("Errore nel collegamento delle pietanze al menu del ristorante");
            } else {
                System.out.println("Menu creato con successo con " + pietanzeCreate.size() + " pietanze");
            }

            return result && linkResult;
        } catch (Exception e) {
            System.err.println("Errore durante la creazione del menu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Genera un report degli ingredienti sotto la soglia di riordino
     * 
     * @return Lista di ingredienti da riordinare
     */
    public ArrayList<EntityIngrediente> generaReportIngredienti() {
        return EntityIngrediente.getIngredientiSottoSoglia();
    }

    /**
     * Visualizza tutti gli ordini attivi
     * 
     * @return Lista degli ordini attivi
     */
    public ArrayList<EntityOrdine> visualizzaOrdini() {
        ArrayList<EntityOrdine> ordiniAttivi = new ArrayList<>();
        ArrayList<EntityOrdine> tuttiOrdini = EntityOrdine.getTuttiOrdini();

        // Filtra solo gli ordini che non sono stati pagati
        for (EntityOrdine o : tuttiOrdini) {
            if (!o.getStato().equals("pagato")) {
                ordiniAttivi.add(o);
            }
        }

        return ordiniAttivi;
    }

    /**
     * Preleva gli ordini pronti dalla cucina
     * 
     * @return Lista degli ordini pronti da servire
     */
    public ArrayList<EntityOrdine> prelevaOrdini() {
        ArrayList<EntityOrdine> ordiniPronti = EntityOrdine.getOrdiniByStato("pronto");
        for (EntityOrdine o : ordiniPronti) {
            o.aggiornaStato("consegnato");
        }
        return ordiniPronti;
    }

    /**
     * Seleziona un tavolo per operazioni come il calcolo del conto
     * 
     * @param idTavolo ID del tavolo da selezionare
     * @return Il tavolo selezionato o null se non esiste
     */
    public EntityTavolo selezionaTavolo(int idTavolo) {
        try {
            return new EntityTavolo(idTavolo);
        } catch (Exception e) {
            System.err.println("Errore nella selezione del tavolo: " + e.getMessage());
            return null;
        }
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
