package entity;

import java.util.ArrayList;

import DTO.DTOTavolo;
import database.DBTavolo;

/**
 * Classe che rappresenta un tavolo del ristorante
 */
public class EntityTavolo {
    private int idTavolo;
    private int maxPosti;
    private String stato;
    private int idRistorante;

    // Costruttori
    public EntityTavolo() {
    }

    public EntityTavolo(int idTavolo, int maxPosti, String stato, int idRistorante) {
        this.idTavolo = idTavolo;
        this.maxPosti = maxPosti;
        this.stato = stato;
        this.idRistorante = idRistorante;
    }

    /**
     * Costruttore che carica un tavolo dal database tramite il suo ID
     * 
     * @param idTavolo l'ID del tavolo da caricare
     */
    public EntityTavolo(int idTavolo) {
        DBTavolo tavolo = new DBTavolo(idTavolo);

        this.idTavolo = idTavolo;
        this.maxPosti = tavolo.getMaxPosti();
        this.stato = tavolo.getStato();
        this.idRistorante = tavolo.getIdRistorante();
    }

    /**
     * Salva il tavolo nel database
     * 
     * @param idTavolo ID del tavolo (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int idTavolo) {
        DBTavolo t = new DBTavolo(); // DAO

        t.setMaxPosti(this.maxPosti);
        t.setStato(this.stato);
        t.setIdRistorante(this.idRistorante);

        int result = t.salvaInDB(idTavolo);

        // Aggiorna l'ID se si tratta di un nuovo tavolo
        if (idTavolo == 0 && result > 0) {
            this.idTavolo = result;
        }

        return result;
    }

    /**
     * Aggiorna lo stato del tavolo
     * 
     * @param stato nuovo stato del tavolo ('libero' o 'occupato')
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaStato(String stato) {
        DBTavolo t = new DBTavolo(this.idTavolo);
        this.stato = stato;
        return t.aggiornaStato(stato);
    }

    /**
     * Recupera tutti i tavoli dal database
     * @return
     */
    public static ArrayList<DTOTavolo> getAllTavoli() {
        
        DBTavolo dbTavolo = new DBTavolo();
        ArrayList<DBTavolo> listaDBTavoli = dbTavolo.getAllTavoli();
        
        ArrayList<DTOTavolo> listaTavoli = new ArrayList<>();

        for (DBTavolo db : listaDBTavoli) {
            DTOTavolo tavolo = new DTOTavolo(
                    db.getIdTavolo(),
                    db.getMaxPosti(),
                    db.getStato()
            );
            listaTavoli.add(tavolo);
        }

        return listaTavoli;
    }

    public static ArrayList<DTOTavolo> getTavoliByStato(String stato) {
        DBTavolo dbTavolo = new DBTavolo();
        ArrayList<DBTavolo> listaDBTavoli = dbTavolo.getTavoliPerStato(stato);
        
        ArrayList<DTOTavolo> listaTavoli = new ArrayList<>();

        for (DBTavolo db : listaDBTavoli) {
            DTOTavolo tavolo = new DTOTavolo(
                    db.getIdTavolo(),
                    db.getMaxPosti(),
                    db.getStato()
            );
            listaTavoli.add(tavolo);
        }
        return listaTavoli;
    }


    // Getters e setters
    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getMaxPosti() {
        return maxPosti;
    }

    public void setMaxPosti(int maxPosti) {
        this.maxPosti = maxPosti;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getIdRistorante() {
        return idRistorante;
    }

    public void setIdRistorante(int idRistorante) {
        this.idRistorante = idRistorante;
    }

    /**
     * Verifica se il tavolo è occupato
     * 
     * @return true se il tavolo è occupato, false altrimenti
     */
    public boolean isOccupato() {
        return "occupato".equalsIgnoreCase(this.stato);
    }

    @Override
    public String toString() {
        return "Tavolo " + " (max " + maxPosti + " posti)" +
                (isOccupato() ? " - Occupato" : " - Libero");
    }
}
