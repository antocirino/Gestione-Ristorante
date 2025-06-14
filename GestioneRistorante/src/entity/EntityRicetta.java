package entity;

import java.util.ArrayList;
import java.util.List;

import database.DBRicetta;

/**
 * Classe che rappresenta una ricetta nel sistema di gestione ristorante
 */
public class EntityRicetta {
    private int idRicetta;
    private String nome;
    private String descrizione;
    private int idPietanza;
    private int tempoPreparazione; // in minuti
    private String istruzioni;
    private List<IngredienteQuantita> ingredienti;

    /**
     * Costruttore vuoto
     */
    public EntityRicetta() {
        this.ingredienti = new ArrayList<>();
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param nome              Nome della ricetta
     * @param descrizione       Descrizione della ricetta
     * @param idPietanza        ID della pietanza associata
     * @param tempoPreparazione Tempo di preparazione in minuti
     * @param istruzioni        Istruzioni per la preparazione
     */
    public EntityRicetta(String nome, String descrizione, int idPietanza, int tempoPreparazione, String istruzioni) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.idPietanza = idPietanza;
        this.tempoPreparazione = tempoPreparazione;
        this.istruzioni = istruzioni;
        this.ingredienti = new ArrayList<>();
    }

    /**
     * Costruttore che carica una ricetta dal database per ID
     * 
     * @param idRicetta ID della ricetta da caricare
     */
    public EntityRicetta(int idRicetta) {
        DBRicetta ricetta = new DBRicetta(idRicetta);

        this.idRicetta = idRicetta;
        this.nome = ricetta.getNome();
        this.descrizione = ricetta.getDescrizione();
        this.idPietanza = ricetta.getIdPietanza();
        this.tempoPreparazione = ricetta.getTempoPreparazione();
        this.istruzioni = ricetta.getIstruzioni();

        // Carica gli ingredienti
        this.ingredienti = new ArrayList<>();
        ArrayList<Object[]> ingredientiDB = ricetta.getIngredientiRicetta();
        for (Object[] ingrediente : ingredientiDB) {
            int idIngrediente = (Integer) ingrediente[0];
            String nomeIngrediente = (String) ingrediente[1];
            String unitaMisura = (String) ingrediente[2];
            float quantita = (Float) ingrediente[3];

            this.ingredienti.add(new IngredienteQuantita(idIngrediente, nomeIngrediente, unitaMisura, quantita));
        }
    }

    /**
     * Salva la ricetta nel database
     * 
     * @param idRicetta ID della ricetta (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int idRicetta) {
        DBRicetta r = new DBRicetta(); // DAO

        r.setNome(this.nome);
        r.setDescrizione(this.descrizione);
        r.setIdPietanza(this.idPietanza);
        r.setTempoPreparazione(this.tempoPreparazione);
        r.setIstruzioni(this.istruzioni);

        int result = r.salvaInDB(idRicetta);

        // Se il salvataggio è andato a buon fine e abbiamo ingredienti da aggiungere
        if (result > 0 && ingredienti != null && !ingredienti.isEmpty()) {
            // Assumiamo che l'ID sia stato impostato nella tabella ricetta
            // e che questo ID sia stato restituito
            int nuovoIdRicetta = idRicetta == 0 ? result : idRicetta;
            r.setIdRicetta(nuovoIdRicetta);

            // Aggiungiamo gli ingredienti alla ricetta
            for (IngredienteQuantita iq : ingredienti) {
                r.aggiungiIngrediente(iq.getIdIngrediente(), iq.getQuantita());
            }
        }

        return result;
    }

    /**
     * Aggiunge un ingrediente alla ricetta
     * 
     * @param idIngrediente   ID dell'ingrediente
     * @param nomeIngrediente Nome dell'ingrediente
     * @param unitaMisura     Unità di misura
     * @param quantita        Quantità necessaria
     */
    public void aggiungiIngrediente(int idIngrediente, String nomeIngrediente, String unitaMisura, float quantita) {
        // Aggiungiamo alla lista locale
        this.ingredienti.add(new IngredienteQuantita(idIngrediente, nomeIngrediente, unitaMisura, quantita));

        // Se abbiamo un ID valido, aggiorniamo anche il database
        if (this.idRicetta > 0) {
            DBRicetta r = new DBRicetta(this.idRicetta);
            r.aggiungiIngrediente(idIngrediente, quantita);
        }
    }

    /**
     * Rimuove un ingrediente dalla ricetta
     * 
     * @param idIngrediente ID dell'ingrediente da rimuovere
     * @return true se l'ingrediente è stato rimosso, false altrimenti
     */
    public boolean rimuoviIngrediente(int idIngrediente) {
        boolean rimosso = false;

        // Rimuoviamo dalla lista locale
        for (int i = 0; i < ingredienti.size(); i++) {
            if (ingredienti.get(i).getIdIngrediente() == idIngrediente) {
                ingredienti.remove(i);
                rimosso = true;
                break;
            }
        }

        // Se abbiamo un ID valido e l'ingrediente è stato rimosso, aggiorniamo anche il
        // database
        if (rimosso && this.idRicetta > 0) {
            DBRicetta r = new DBRicetta(this.idRicetta);
            r.rimuoviIngrediente(idIngrediente);
        }

        return rimosso;
    }

    /**
     * Recupera tutte le ricette dal database
     * 
     * @return ArrayList di oggetti Ricetta
     */
    public static ArrayList<EntityRicetta> getTutteRicette() {
        DBRicetta ricetta = new DBRicetta();
        return ricetta.getTutteRicette();
    }

    /**
     * Recupera la ricetta per una specifica pietanza
     * 
     * @param idPietanza l'ID della pietanza
     * @return Ricetta associata alla pietanza o null se non esiste
     */
    public static EntityRicetta getRicettaByPietanza(int idPietanza) {
        DBRicetta ricetta = new DBRicetta();
        return ricetta.getRicettaByPietanza(idPietanza);
    }

    public boolean isRicettaEseguibile(int quantitaPietanze) {
        ArrayList<EntityRicettaIngrediente> lista = EntityRicettaIngrediente.getIngredientiPerRicetta(this.idRicetta);
        if (lista == null || lista.isEmpty()) {
            return false; // Nessun ingrediente trovato, non eseguibile
        }

        // Controlla se tutti gli ingredienti sono disponibili
        for (EntityRicettaIngrediente ri : lista) {
            EntityIngrediente ingr = new EntityIngrediente(ri.getIdIngrediente());

            if (ingr == null || ingr.getQuantitaDisponibile() < ri.getQuantita()*quantitaPietanze) {
                return false;
            }
        }
        return true;
    }

    public boolean prenotaIngredienti(int quantitaPietanze) {
        
        ArrayList<EntityRicettaIngrediente> lista = EntityRicettaIngrediente.getIngredientiPerRicetta(this.idRicetta);
        if (lista == null || lista.isEmpty()) {
            return false; // Nessun ingrediente trovato, non eseguibile
        }

        for (EntityRicettaIngrediente ri : lista) {
            EntityIngrediente ingr = new EntityIngrediente(ri.getIdIngrediente());
            float new_qta = ri.getQuantita() * quantitaPietanze;
            
            ingr.prenotaIngrediente(new_qta);
        }
        return true;
}

// Classe interna per rappresentare un ingrediente con la sua quantità
public class IngredienteQuantita {
    private int idIngrediente;
    private String nomeIngrediente;
    private String unitaMisura;
    private float quantita;

    public IngredienteQuantita(int idIngrediente, String nomeIngrediente, String unitaMisura, float quantita) {
        this.idIngrediente = idIngrediente;
        this.nomeIngrediente = nomeIngrediente;
        this.unitaMisura = unitaMisura;
        this.quantita = quantita;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public float getQuantita() {
        return quantita;
    }

    public void setQuantita(float quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return nomeIngrediente + ": " + quantita + " " + unitaMisura;
    }

    }

    // Getters e setters
    public int getIdRicetta() {
        return idRicetta;
    }

    public void setIdRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public int getTempoPreparazione() {
        return tempoPreparazione;
    }

    public void setTempoPreparazione(int tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }

    public String getIstruzioni() {
        return istruzioni;
    }

    public void setIstruzioni(String istruzioni) {
        this.istruzioni = istruzioni;
    }

    public List<IngredienteQuantita> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(List<IngredienteQuantita> ingredienti) {
        this.ingredienti = ingredienti;
    }

    @Override
    public String toString() {
        return nome + " (Tempo di preparazione: " + tempoPreparazione + " min)";
    }
}
