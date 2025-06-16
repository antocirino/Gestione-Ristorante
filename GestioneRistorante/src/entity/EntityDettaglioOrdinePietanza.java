package entity;

import java.util.ArrayList;

import database.DBDettaglioOrdinePietanza;

/**
 * Classe che rappresenta un dettaglio di un ordine di pietanza
 */
public class EntityDettaglioOrdinePietanza {
    private int idDettaglio;
    private int idOrdine;
    private int idPietanza;
    private int quantita;
    private EntityPietanza pietanza;
    private boolean parteDiMenu; // Flag per indicare se la pietanza fa parte di un menu fisso
    private int idMenu; // ID del menu fisso di cui fa parte (se applicabile)

    /**
     * Costruttore vuoto
     */
    public EntityDettaglioOrdinePietanza() {
        this.parteDiMenu = false;
        this.idMenu = 0;
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param idOrdine   ID dell'ordine
     * @param idPietanza ID della pietanza
     * @param quantita   Quantità ordinata
     */
    public EntityDettaglioOrdinePietanza(int idOrdine, int idPietanza, int quantita) {
        this.idOrdine = idOrdine;
        this.idPietanza = idPietanza;
        this.quantita = quantita;
        this.parteDiMenu = false;
        this.idMenu = 0;
    }

    /**
     * Costruttore con attributi principali e pietanza
     * 
     * @param idOrdine ID dell'ordine
     * @param pietanza Oggetto pietanza
     * @param quantita Quantità ordinata
     */
    public EntityDettaglioOrdinePietanza(int idOrdine, EntityPietanza pietanza, int quantita) {
        this.idOrdine = idOrdine;
        this.pietanza = pietanza;
        this.idPietanza = pietanza.getIdPietanza();
        this.quantita = quantita;
        this.parteDiMenu = false;
        this.idMenu = 0;
    }

    /**
     * Costruttore che carica un dettaglio ordine dal database per ID
     * 
     * @param idDettaglio ID del dettaglio ordine da caricare
     */
    public EntityDettaglioOrdinePietanza(int idDettaglio) {
        DBDettaglioOrdinePietanza dettaglio = new DBDettaglioOrdinePietanza(idDettaglio);

        this.idDettaglio = idDettaglio;
        this.idOrdine = dettaglio.getIdOrdine();
        this.idPietanza = dettaglio.getIdPietanza();
        this.quantita = dettaglio.getQuantita();
        this.pietanza = new EntityPietanza(this.idPietanza);
        this.parteDiMenu = dettaglio.isParteDiMenu();
        this.idMenu = dettaglio.getIdMenu();
    }

    /**
     * Salva il dettaglio ordine nel database utilizzando INSERT ... ON CONFLICT
     * Se esiste già un dettaglio con lo stesso id_ordine, id_pietanza,
     * parte_di_menu e id_menu,
     * aggiorna la quantità sommandola a quella esistente.
     * 
     * @return l'ID del dettaglio ordine se il salvataggio è avvenuto con successo,
     *         -1 in caso di errore
     */
    public int scriviSuDB() {
        DBDettaglioOrdinePietanza d = new DBDettaglioOrdinePietanza(); // DAO

        d.setIdOrdine(this.idOrdine);
        d.setIdPietanza(this.idPietanza);
        d.setQuantita(this.quantita);
        d.setParteDiMenu(this.parteDiMenu);
        d.setIdMenu(this.idMenu);

        int result = d.salvaConOnConflict();
        System.out.println("DettaglioOrdinePietanza (ON CONFLICT): id_dettaglio " + result);

        // Aggiorna l'ID se l'operazione è riuscita
        if (result > 0) {
            this.idDettaglio = result;
        }

        return result;
    }

    /**
     * Elimina un dettaglio ordine dal database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int eliminaDaDB() {
        DBDettaglioOrdinePietanza d = new DBDettaglioOrdinePietanza();
        return d.eliminaDaDB(this.idDettaglio);
    }

    /**
     * Recupera tutti i dettagli di un ordine dal database
     * 
     * @param idOrdine ID dell'ordine
     * @return ArrayList di oggetti DettaglioOrdinePietanza
     */
    public static ArrayList<EntityDettaglioOrdinePietanza> getDettagliOrdine(int idOrdine) {
        DBDettaglioOrdinePietanza d = new DBDettaglioOrdinePietanza();
        return d.getDettagliOrdine(idOrdine);
    }

    // Getters e setters
    public int getIdDettaglio() {
        return idDettaglio;
    }

    public void setIdDettaglio(int idDettaglio) {
        this.idDettaglio = idDettaglio;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public EntityPietanza getPietanza() {
        if (this.pietanza == null && this.idPietanza > 0) {
            this.pietanza = new EntityPietanza(this.idPietanza);
        }
        return pietanza;
    }

    public void setPietanza(EntityPietanza pietanza) {
        this.pietanza = pietanza;
        if (pietanza != null) {
            this.idPietanza = pietanza.getIdPietanza();
        }
    }

    /**
     * Verifica se questo dettaglio fa parte di un menu fisso
     * 
     * @return true se fa parte di un menu fisso, false altrimenti
     */
    public boolean isParteDiMenu() {
        return parteDiMenu;
    }

    /**
     * Imposta se questo dettaglio fa parte di un menu fisso
     * 
     * @param parteDiMenu true se fa parte di un menu fisso, false altrimenti
     */
    public void setParteDiMenu(boolean parteDiMenu) {
        this.parteDiMenu = parteDiMenu;
    }

    /**
     * Ottiene l'ID del menu fisso di cui fa parte questo dettaglio
     * 
     * @return l'ID del menu fisso o 0 se non fa parte di un menu fisso
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * Imposta l'ID del menu fisso di cui fa parte questo dettaglio
     * 
     * @param idMenu l'ID del menu fisso o 0 se non fa parte di un menu fisso
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    @Override
    public String toString() {
        String nomePietanza = (pietanza != null) ? pietanza.getNome() : "Pietanza #" + idPietanza;
        return nomePietanza + " x " + quantita;
    }
}
