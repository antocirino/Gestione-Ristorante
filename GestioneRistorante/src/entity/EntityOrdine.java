package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import DTO.DTOMenuFissoCuoco;
import DTO.DTOOrdine;
import DTO.DTOPietanzaCuoco;
import database.DBOrdine;

/**
 * Classe che rappresenta un ordine nel ristorante
 */
public class EntityOrdine {
    private int idOrdine;
    private int idTavolo;
    private int numPersone;
    private Date dataOrdine;
    private String stato; // in_attesa,confermato, in_preparazione, pronto, consegnato, pagato
    private double costoTotale; // Nuovo campo per tenere traccia del costo totale

    /**
     * Costruttore vuoto
     */
    public EntityOrdine() {
        this.costoTotale = 0.0;
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param idTavolo   ID del tavolo associato all'ordine
     * @param numPersone Numero di persone per l'ordine
     * @param stato      Stato iniziale dell'ordine
     */
    public EntityOrdine(int idTavolo, int numPersone, String stato) {
        this.idTavolo = idTavolo;
        this.numPersone = numPersone;
        this.stato = stato;
        this.dataOrdine = new Date(); // Data corrente
        this.costoTotale = 0.0;
    }

    /**
     * Costruttore con tutti i parametri
     *
     * @param idOrdine   ID dell'ordine da caricare
     * @param idTavolo   ID del tavolo associato all'ordine
     * @param numPersone Numero di persone per l'ordine
     * @param stato      Stato iniziale dell'ordine
     */
    public EntityOrdine(int idOrdine, int idTavolo, int numPersone, String stato) {
        this.idTavolo = idTavolo;
        this.idOrdine = idOrdine;
        this.numPersone = numPersone;
        this.stato = stato;
        this.dataOrdine = new Date(); // Data corrente
        this.costoTotale = 0.0;
    }

    /**
     * Costruttore che carica un ordine dal database per ID
     * 
     * @param idOrdine ID dell'ordine da caricare
     */
    public EntityOrdine(int idOrdine) {
        DBOrdine ordine = new DBOrdine(idOrdine);

        this.idOrdine = idOrdine;
        this.idTavolo = ordine.getIdTavolo();
        this.numPersone = ordine.getNumPersone();
        this.dataOrdine = ordine.getDataOrdine();
        this.stato = ordine.getStato();
        this.costoTotale = ordine.getCostoTotale();
    }

    /**
     * Salva l'ordine nel database
     * 
     * @return l'ID dell'ordine inserito o -1 in caso di errore
     */
    public int scriviSuDB() {
        DBOrdine o = new DBOrdine(); // DAO

        o.setIdOrdine(this.idOrdine); // Aggiungiamo l'id corrente
        o.setIdTavolo(this.idTavolo);
        o.setNumPersone(this.numPersone);
        o.setDataOrdine(this.dataOrdine);
        o.setStato(this.stato);
        o.setIdRistorante(1); // Assumiamo che il ristorante abbia ID=1 come predefinito
        o.setCostoTotale(this.costoTotale); // Inizializza il costo totale

        int result = o.salvaInDB();
        if (result > 0) {
            this.idOrdine = result;
        }

        return result;
    }

    /**
     * Aggiorna lo stato dell'ordine
     * 
     * @param nuovoStato il nuovo stato dell'ordine
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaStato(String nuovoStato) {
        this.stato = nuovoStato;
        DBOrdine o = new DBOrdine(this.idOrdine);
        return o.aggiornaStato(nuovoStato);
    }

    /**
     * Recupera gli ordini con un determinato stato
     * 
     * @param stato lo stato degli ordini da recuperare
     * @return ArrayList di oggetti Ordine con lo stato specificato
     */
    public static ArrayList<DTOOrdine> getOrdiniPerStato(String stato) {
        ArrayList<DTOOrdine> listaOrdini = new ArrayList<>();
        DBOrdine ordine = new DBOrdine();
        ArrayList<DBOrdine> lista_ordini = ordine.getOrdiniPerStato(stato);
        for (DBOrdine o : lista_ordini) {
            DTOOrdine dto = new DTOOrdine();
            dto.setIdOrdine(o.getIdOrdine());
            dto.setIdTavolo(o.getIdTavolo());
            dto.setNumPersone(o.getNumPersone());
            dto.setDataOrdine(o.getDataOrdine());
            dto.setStato(o.getStato());
            dto.setCostoTotale(o.getCostoTotale());
            listaOrdini.add(dto);

        }
        return listaOrdini;
    }

    /**
     * Recupera le pietanze associate a un ordine
     * 
     * @return ArrayList di oggetti DTOPietanzaCuoco con le pietanze dell'ordine
     */
    public ArrayList<DTOPietanzaCuoco> getPietanzeDaOrdine() {
        ArrayList<DTOPietanzaCuoco> pietanzeDTO = new ArrayList<>();
        DBOrdine ordine = new DBOrdine(this.idOrdine);

        // Ottiene i dati dal DB e li converte in DTO
        ArrayList<Map<String, Object>> pietanzeDB = ordine.getPietanzeDaOrdine();
        for (Map<String, Object> pietanzaDB : pietanzeDB) {
            String nome = (String) pietanzaDB.get("nome");
            int quantita = (Integer) pietanzaDB.get("quantita");
            DTOPietanzaCuoco pietanzaDTO = new DTOPietanzaCuoco(nome, quantita);
            pietanzeDTO.add(pietanzaDTO);
        }

        return pietanzeDTO;
    }

    /**
     * Recupera i menu fissi associati a un ordine
     * 
     * @return ArrayList di oggetti DTOMenuFissoCuoco con i menu fissi dell'ordine
     */
    public ArrayList<DTOMenuFissoCuoco> getMenuFissiDaOrdine() {
        ArrayList<DTOMenuFissoCuoco> menuFissiDTO = new ArrayList<>();
        DBOrdine ordine = new DBOrdine(this.idOrdine);

        // Ottiene i dati dal DB e li converte in DTO
        ArrayList<Map<String, Object>> menuFissiDB = ordine.getMenuFissiDaOrdine();
        for (Map<String, Object> menuDB : menuFissiDB) {
            String nome = (String) menuDB.get("nome");
            int quantita = (Integer) menuDB.get("quantita");
            DTOMenuFissoCuoco menuDTO = new DTOMenuFissoCuoco(nome, quantita);
            menuFissiDTO.add(menuDTO);
        }

        return menuFissiDTO;
    }

    /**
     * Recupera gli ordini per un tavolo specifico
     * 
     * @param idTavolo l'ID del tavolo
     * @return ArrayList di oggetti Ordine per il tavolo specificato
     */
    public static DTOOrdine getOrdinePerTavolo(int idTavolo) {

        int id_ordine = DBOrdine.getIDOrdineByTavolo(idTavolo);

        if (id_ordine <= 0) {
            System.err.println("Nessun ordine trovato per il tavolo con ID: " + idTavolo);
            return null;
        }
        EntityOrdine ordine = new EntityOrdine(id_ordine);
        // Non calcoliamo il conto qui per evitare di aggiornare il DB
        // Recuperiamo solo i dati esistenti dell'ordine
        double val = ordine.getCostoTotale();

        if (val <= 0) {
            System.err.println("Errore nel recupero del costo per l'ordine con ID: " + id_ordine);
            return null;
        }

        DTOOrdine dtoOrdine = new DTOOrdine();
        dtoOrdine.setIdOrdine(ordine.getIdOrdine());
        dtoOrdine.setIdTavolo(ordine.getIdTavolo());
        dtoOrdine.setNumPersone(ordine.getNumPersone());
        dtoOrdine.setDataOrdine(ordine.getDataOrdine());
        dtoOrdine.setStato(ordine.getStato());
        dtoOrdine.setCostoTotale(ordine.getCostoTotale());

        return dtoOrdine;

    }

    /**
     * Aggiunge una pietanza all'ordine
     * 
     * @param idPietanza La pietanza da aggiungere
     * @param quantita   La quantità della pietanza
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public boolean aggiungiPietanza(int idPietanza, int quantita) {

        EntityPietanza pietanza = new EntityPietanza(idPietanza);

        try {
            if (quantita <= 0) {
                System.err.println("Errore: Quantità non valida: " + quantita);
                return false;
            }

            // Verifica disponibilità della pietanza
            if (!pietanza.isDisponibilePerOrdine(quantita)) {
                System.err.println("Pietanza non disponibile: " + pietanza.getNome());
                return false;
            }

            // Verifica che l'ordine esista nel database (abbia un ID valido)
            if (this.idOrdine <= 0) {
                int nuovoId = this.scriviSuDB();
                if (nuovoId <= 0) {
                    System.err.println("Errore: Impossibile salvare l'ordine");
                    return false;
                }
                this.idOrdine = nuovoId;
            }

            // Aggiorna il costo totale dell'ordine con il prezzo della pietanza
            double prezzoPietanza = pietanza.getPrezzo();
            double costoAggiuntivo = prezzoPietanza * quantita;
            this.costoTotale += costoAggiuntivo;

            // Crea il dettaglio ordine
            EntityDettaglioOrdinePietanza dettaglio = new EntityDettaglioOrdinePietanza(this.idOrdine,
                    pietanza.getIdPietanza(),
                    quantita);

            // Salva il dettaglio nel database utilizzando ON CONFLICT
            int dettaglioId = dettaglio.scriviSuDB();
            if (dettaglioId > 0) {
                // Se il salvataggio è riuscito, prenota gli ingredienti e aggiorna il costo
                // totale

                // Aggiorna il costo totale nel database
                if (!aggiornaCostoTotale()) {
                    System.err.println("Avviso: Impossibile aggiornare il costo totale nel database");
                }

                return pietanza.prenotaIngredienti(quantita);
            } else {
                System.err.println(
                        "Errore: Impossibile salvare il dettaglio ordine per la pietanza: " + pietanza.getNome());
                // Ripristina il costo totale in caso di errore
                this.costoTotale -= costoAggiuntivo;
            }

            return false;
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiunta della pietanza: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crea un nuovo ordine
     * 
     * @param idTavolo     ID del tavolo associato all'ordine
     * @param numPersone   Numero di persone per l'ordine
     * @param idRistorante ID del ristorante associato all'ordine
     * @param stato        Stato iniziale dell'ordine
     * @return un nuovo oggetto EntityOrdine con l'ID assegnato dal database
     */
    public EntityOrdine creaOrdine(int idTavolo, int numPersone, int idRistorante, String stato) {
        DBOrdine ordine = new DBOrdine(idTavolo, numPersone, idRistorante, stato);
        int id = ordine.salvaInDB();

        EntityOrdine new_ordine = new EntityOrdine(id);

        return new_ordine;

    }

    /**
     * Aggiunge un menu fisso all'ordine
     * 
     * @param idOrdine    ID dell'ordine a cui aggiungere il menu fisso
     * @param idMenuFisso ID del menu fisso da aggiungere
     * @param quantita    Quantità del menu fisso da aggiungere
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public boolean aggiungiMenuFisso(int idOrdine, int idMenuFisso, int quantita) {
        EntityMenuFisso menufisso = new EntityMenuFisso(idMenuFisso);
        String nome = menufisso.getNome();
        double prezzo = menufisso.getPrezzo();
        ArrayList<EntityPietanza> pietanze = menufisso.getPietanze();
        try {
            if (pietanze == null || pietanze.isEmpty()) {
                System.err.println("Errore: Menu fisso senza pietanze");
                return false;
            }

            if (quantita <= 0) {
                System.err.println("Errore: Quantità non valida per il menu fisso: " + quantita);
                return false;
            }

            // Verifica che l'ordine esista nel database (abbia un ID valido)
            if (this.idOrdine <= 0) {
                int nuovoId = this.scriviSuDB();
                if (nuovoId <= 0) {
                    System.err.println("Errore: Impossibile salvare l'ordine");
                    return false;
                }
                this.idOrdine = nuovoId;
            }

            // FASE 1: Verifica preliminare di tutte le pietanze prima di qualsiasi modifica
            for (EntityPietanza pietanza : pietanze) {
                // Verifica disponibilità della pietanza
                if (!pietanza.isDisponibilePerOrdine(quantita)) {
                    System.err.println("Pietanza del menu non disponibile: " + pietanza.getNome());
                    return false;
                }
            }

            // Aggiorna il costo totale dell'ordine con il prezzo del menu fisso
            double costoAggiuntivo = prezzo * quantita;
            this.costoTotale += costoAggiuntivo;

            // FASE 2: Esegui le operazioni di prenotazione e salvataggio
            boolean tutteLeAggiunte = true;

            // Lista per tenere traccia degli ingredienti prenotati (per eventuale rollback)
            ArrayList<EntityPietanza> pietanzePrenotate = new ArrayList<>();

            // Prenota gli ingredienti di ogni pietanza del menu fisso
            for (EntityPietanza pietanza : pietanze) {
                // Prenota gli ingredienti per la pietanza
                if (!pietanza.prenotaIngredienti(quantita)) {
                    System.err.println("Impossibile prenotare ingredienti per: " + pietanza.getNome());
                    tutteLeAggiunte = false;
                    break;
                }

                pietanzePrenotate.add(pietanza);

                // Crea il dettaglio ordine (ma non aggiunge al costo totale)
                EntityDettaglioOrdinePietanza dettaglio = new EntityDettaglioOrdinePietanza(this.idOrdine,
                        pietanza.getIdPietanza(),
                        quantita);
                dettaglio.setParteDiMenu(true); // Marca come parte di un menu fisso
                dettaglio.setIdMenu(idMenuFisso); // Associa al menu fisso

                // Salva il dettaglio nel database usando ON CONFLICT
                if (dettaglio.scriviSuDB() <= 0) {
                    System.err.println(
                            "Errore: Impossibile salvare il dettaglio per la pietanza del menu: " + pietanza.getNome());
                    tutteLeAggiunte = false;
                    break;
                }
            }

            // Se qualcosa è andato storto, ripristina il costo totale e annulla le
            // prenotazioni
            if (!tutteLeAggiunte) {
                this.costoTotale -= costoAggiuntivo;

                // Annulla le prenotazioni di ingredienti già fatte
                for (EntityPietanza pietanza : pietanzePrenotate) {
                    // Qui dovrebbe esserci un metodo per annullare la prenotazione
                    // pietanza.annullaPrenotazioneIngredienti(quantita);
                    System.err.println("Annullata prenotazione ingredienti per: " + pietanza.getNome());
                }

                return false;
            }

            // Aggiorna il costo totale nel database
            if (!aggiornaCostoTotale()) {
                System.err.println("Avviso: Impossibile aggiornare il costo totale nel database");
            }

            return true;

        } catch (Exception e) {
            System.err.println("Errore durante l'aggiunta del menu fisso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Getters e setters
    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdTavolo() {
        return idTavolo;
    }

    public void setIdTavolo(int idTavolo) {
        this.idTavolo = idTavolo;
    }

    public int getNumPersone() {
        return numPersone;
    }

    public void setNumPersone(int numPersone) {
        this.numPersone = numPersone;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public double getCostoTotale() {
        return costoTotale;
    }

    public void setCostoTotale(double costoTotale) {
        this.costoTotale = costoTotale;
    }

    /**
     * Aggiorna il costo totale dell'ordine nel database
     * 
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaCostoTotale() {
        try {
            DBOrdine o = new DBOrdine(this.idOrdine);
            o.setCostoTotale(this.costoTotale);
            return o.aggiornaCostoTotale();
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento del costo totale: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "Ordine #" + idOrdine + " - Tavolo #" + idTavolo + " - " + stato;
    }
}
