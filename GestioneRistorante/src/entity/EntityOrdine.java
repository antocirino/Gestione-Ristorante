package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DBOrdine;

/**
 * Classe che rappresenta un ordine nel ristorante
 */
public class EntityOrdine {
    private int idOrdine;
    private int idTavolo;
    private int numPersone;
    private Date dataOrdine;
    private String stato; // in_attesa, in_preparazione, pronto, consegnato, pagato
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

        o.setIdTavolo(this.idTavolo);
        o.setNumPersone(this.numPersone);
        o.setStato(this.stato);
        o.setIdRistorante(1); // Assumiamo che il ristorante abbia ID=1 come predefinito
        o.setCostoTotale(this.costoTotale); // Inizializza il costo totale

        int nuovoId = o.salvaInDB();
        if (nuovoId > 0) {
            this.idOrdine = nuovoId;
        }

        return nuovoId;
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
     * Recupera tutti gli ordini dal database
     * 
     * @return ArrayList di oggetti Ordine
     */
    public static ArrayList<EntityOrdine> getTuttiOrdini() {
        DBOrdine ordine = new DBOrdine();
        return ordine.getTuttiOrdini();
    }

    /**
     * Recupera gli ordini con un determinato stato
     * 
     * @param stato lo stato degli ordini da recuperare
     * @return ArrayList di oggetti Ordine con lo stato specificato
     */
    public static ArrayList<EntityOrdine> getOrdiniPerStato(String stato) {
        DBOrdine ordine = new DBOrdine();
        return ordine.getOrdiniPerStato(stato);
    }

    /**
     * Recupera gli ordini con un determinato stato
     * 
     * @param stato lo stato degli ordini da recuperare
     * @return ArrayList di oggetti Ordine con lo stato specificato
     */
    public static ArrayList<EntityOrdine> getOrdiniByStato(String stato) {
        DBOrdine ordine = new DBOrdine();
        return ordine.getOrdiniByStato(stato);
    }

    /**
     * Recupera gli ordini per un tavolo specifico
     * 
     * @param idTavolo l'ID del tavolo
     * @return ArrayList di oggetti Ordine per il tavolo specificato
     */
    public static ArrayList<EntityOrdine> getOrdiniPerTavolo(int idTavolo) {
        DBOrdine ordine = new DBOrdine();
        return ordine.getOrdiniPerTavolo(idTavolo);
    }

    /**
     * Aggiunge una pietanza all'ordine
     * 
     * @param pietanza La pietanza da aggiungere
     * @param quantita La quantità della pietanza
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public boolean aggiungiPietanza(EntityPietanza pietanza, int quantita) {
        try {
            if (pietanza == null) {
                System.err.println("Errore: Pietanza null");
                return false;
            }

            if (quantita <= 0) {
                System.err.println("Errore: Quantità non valida: " + quantita);
                return false;
            }

            // Verifica disponibilità della pietanza
            if (!pietanza.isDisponibilePerOrdine()) {
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
                System.out.println("Creato nuovo ordine con ID: " + nuovoId);
            }

            // Aggiorna il costo totale dell'ordine con il prezzo della pietanza
            double prezzoPietanza = pietanza.getPrezzo();
            double costoAggiuntivo = prezzoPietanza * quantita;
            this.costoTotale += costoAggiuntivo;

            // Controlla se la pietanza è già presente nell'ordine
            ArrayList<EntityDettaglioOrdinePietanza> dettagliEsistenti = EntityDettaglioOrdinePietanza
                    .getDettagliOrdine(this.idOrdine);
            for (EntityDettaglioOrdinePietanza dop : dettagliEsistenti) {
                if (dop.getIdPietanza() == pietanza.getIdPietanza()) {
                    // Aggiorna la quantità invece di creare un nuovo dettaglio
                    int nuovaQuantita = dop.getQuantita() + quantita;
                    dop.setQuantita(nuovaQuantita);
                    if (dop.scriviSuDB(dop.getIdDettaglio()) > 0) {
                        // Aggiorna il costo totale nel database
                        if (!aggiornaCostoTotale()) {
                            System.err.println("Avviso: Impossibile aggiornare il costo totale nel database");
                        }
                        return pietanza.prenotaIngredienti(quantita);
                    }
                    // Ripristina il costo totale in caso di errore
                    this.costoTotale -= costoAggiuntivo;
                    return false;
                }
            }

            // Crea il dettaglio ordine
            EntityDettaglioOrdinePietanza dettaglio = new EntityDettaglioOrdinePietanza(this.idOrdine, pietanza.getIdPietanza(),
                    quantita);

            // Salva il dettaglio nel database
            int dettaglioId = dettaglio.scriviSuDB(0);
            if (dettaglioId > 0) {
                // Se il salvataggio è riuscito, prenota gli ingredienti e aggiorna il costo
                // totale
                System.out.println("Aggiunta pietanza: " + pietanza.getNome() + " x" + quantita +
                        " all'ordine #" + this.idOrdine +
                        " (+" + String.format("%.2f", costoAggiuntivo) + "€)");

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
     * Aggiunge un menu fisso all'ordine. Un menu fisso è composto da diverse
     * pietanze
     * a un prezzo fisso. Questo metodo aggiunge tutte le pietanze del menu ma
     * considera solo il prezzo complessivo del menu, non la somma dei prezzi delle
     * singole pietanze.
     * 
     * @param menuId   ID del menu fisso
     * @param nome     Nome del menu fisso
     * @param prezzo   Prezzo complessivo del menu fisso
     * @param pietanze Lista delle pietanze incluse nel menu fisso
     * @param quantita Quantità di menu fissi da aggiungere
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public boolean aggiungiMenuFisso(int menuId, String nome, double prezzo, ArrayList<EntityPietanza> pietanze,
            int quantita) {
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
                System.out.println("Creato nuovo ordine con ID: " + nuovoId);
            }

            // Aggiorna il costo totale dell'ordine con il prezzo del menu fisso
            double costoAggiuntivo = prezzo * quantita;
            this.costoTotale += costoAggiuntivo;

            // Flag per tracciare se tutte le pietanze sono state aggiunte con successo
            boolean tutteLeAggiunte = true;

            // Aggiungi ogni pietanza del menu senza considerare il suo prezzo individuale
            for (EntityPietanza pietanza : pietanze) {
                // Verifica disponibilità della pietanza
                if (!pietanza.isDisponibilePerOrdine()) {
                    System.err.println("Pietanza del menu non disponibile: " + pietanza.getNome());
                    tutteLeAggiunte = false;
                    break;
                }

                // Prenota gli ingredienti per la pietanza
                if (!pietanza.prenotaIngredienti(quantita)) {
                    System.err.println("Impossibile prenotare ingredienti per: " + pietanza.getNome());
                    tutteLeAggiunte = false;
                    break;
                }

                // Crea il dettaglio ordine (ma non aggiunge al costo totale)
                EntityDettaglioOrdinePietanza dettaglio = new EntityDettaglioOrdinePietanza(this.idOrdine, pietanza.getIdPietanza(),
                        quantita);
                dettaglio.setParteDiMenu(true); // Marca come parte di un menu fisso
                dettaglio.setIdMenu(menuId); // Associa al menu fisso

                // Salva il dettaglio nel database
                if (dettaglio.scriviSuDB(0) <= 0) {
                    System.err.println(
                            "Errore: Impossibile salvare il dettaglio per la pietanza del menu: " + pietanza.getNome());
                    tutteLeAggiunte = false;
                    break;
                }
            }

            // Se qualcosa è andato storto, ripristina il costo totale
            if (!tutteLeAggiunte) {
                this.costoTotale -= costoAggiuntivo;
                return false;
            }

            // Aggiorna il costo totale nel database
            if (!aggiornaCostoTotale()) {
                System.err.println("Avviso: Impossibile aggiornare il costo totale nel database");
            }

            System.out.println("Aggiunto menu fisso: " + nome + " x" + quantita +
                    " all'ordine #" + this.idOrdine +
                    " (+" + String.format("%.2f", costoAggiuntivo) + "€)");
            return true;

        } catch (Exception e) {
            System.err.println("Errore durante l'aggiunta del menu fisso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcola il totale del conto per l'ordine
     * 
     * @param includiCoperto se true include il costo del coperto nel totale
     * @return il totale del conto
     */
    public double calcolaConto(boolean includiCoperto) {
        double totale = this.costoTotale; // Utilizziamo direttamente il costoTotale memorizzato
        double sconto = 0.0;

        try {
            // Recupera tutti i dettagli dell'ordine per visualizzazione
            ArrayList<EntityDettaglioOrdinePietanza> dettagli = EntityDettaglioOrdinePietanza.getDettagliOrdine(this.idOrdine);

            if (dettagli.isEmpty()) {
                System.out.println("Nessun dettaglio trovato per l'ordine #" + this.idOrdine);
                return 0.0;
            }

            // Stampa i dettagli per verifica (il costo è già memorizzato in costoTotale)
            System.out.println("Dettagli del conto per ordine #" + this.idOrdine + ":");

            // Raggruppa i dettagli per menu fissi per una visualizzazione migliore
            Map<Integer, List<EntityDettaglioOrdinePietanza>> menuItemsMap = new HashMap<>();

            for (EntityDettaglioOrdinePietanza dettaglio : dettagli) {
                EntityPietanza pietanza = dettaglio.getPietanza();
                int quantita = dettaglio.getQuantita();

                if (dettaglio.isParteDiMenu()) {
                    // Raggruppa gli elementi di menu fissi
                    int menuId = dettaglio.getIdMenu();
                    if (!menuItemsMap.containsKey(menuId)) {
                        menuItemsMap.put(menuId, new ArrayList<>());
                    }
                    menuItemsMap.get(menuId).add(dettaglio);
                } else {
                    // Pietanze normali (non parte di menu fissi)
                    double prezzoUnitario = pietanza.getPrezzo();
                    double subtotale = prezzoUnitario * quantita;

                    System.out.println("- " + pietanza.getNome() + " x" + quantita +
                            " (" + String.format("%.2f", prezzoUnitario) + "€ x " + quantita +
                            ") = " + String.format("%.2f", subtotale) + "€");
                }
            }

            // Stampa i menu fissi raggruppati
            for (Map.Entry<Integer, List<EntityDettaglioOrdinePietanza>> entry : menuItemsMap.entrySet()) {
                int menuId = entry.getKey();
                List<EntityDettaglioOrdinePietanza> menuItems = entry.getValue();

                if (!menuItems.isEmpty()) {
                    // Recupera informazioni sul menu dal database
                    String nomeMenu = "Menu Fisso #" + menuId;
                    double prezzoMenu = 0.0;

                    // Recupera informazioni sul menu dal DAO
                    DBOrdine dbOrdine = new DBOrdine();
                    Map<String, Object> infoMenu = dbOrdine.getInfoMenuFisso(menuId);
                    if (infoMenu != null) {
                        nomeMenu = (String) infoMenu.get("nome");
                        prezzoMenu = (Double) infoMenu.get("prezzo");
                    }

                    // Determina la quantità di menu dal primo elemento
                    int quantitaMenu = menuItems.get(0).getQuantita();
                    double subtotaleMenu = prezzoMenu * quantitaMenu;

                    System.out.println("--- " + nomeMenu + " x" + quantitaMenu +
                            " (" + String.format("%.2f", prezzoMenu) + "€ x " +
                            quantitaMenu + ") = " +
                            String.format("%.2f", subtotaleMenu) + "€");

                    // Stampa le pietanze incluse nel menu
                    for (EntityDettaglioOrdinePietanza item : menuItems) {
                        EntityPietanza pietanza = item.getPietanza();
                        System.out.println("    • " + pietanza.getNome() + " (incluso nel menu)");
                    }
                    System.out.println();
                }
            }

            System.out.println("Subtotale: " + String.format("%.2f", totale) + "€");

            // Controlla se applicare sconti in base al numero di persone o al totale
            if (this.numPersone > 8) {
                // Sconto del 10% per gruppi numerosi
                sconto = totale * 0.10;
                System.out.println("Sconto gruppo (10%): -" + String.format("%.2f", sconto) + "€");
                totale -= sconto;
            } else if (totale > 100) {
                // Sconto del 5% per conti superiori a 100€
                sconto = totale * 0.05;
                System.out.println("Sconto per importo elevato (5%): -" + String.format("%.2f", sconto) + "€");
                totale -= sconto;
            }

            // Aggiungi il costo del coperto se richiesto
            if (includiCoperto) {
                // Recupera il costo del coperto dal ristorante
                double costoCoperto = 2.0; // Valore di default

                // Recupera il costo del coperto dal DAO
                DBOrdine dbOrdine = new DBOrdine();
                costoCoperto = dbOrdine.getCostoCoperto();

                double totaleCoperto = costoCoperto * this.numPersone;
                System.out.println(
                        "Coperto: " + costoCoperto + "€ x " + this.numPersone + " persone = " + totaleCoperto + "€");
                totale += totaleCoperto;
            }

            System.out.println("TOTALE CONTO: " + String.format("%.2f", totale) + "€");
            return totale;
        } catch (Exception e) {
            System.err.println("Errore durante il calcolo del conto: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
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
