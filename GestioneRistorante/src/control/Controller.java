package control;

import java.util.ArrayList;
import java.nio.file.Path;


import DTO.DTOCategoriaPietanza;
import DTO.DTOIngrediente;
import DTO.DTOIngredientiRicettaPietanza;
import DTO.DTOMenuFisso;
import DTO.DTOMenuFissoCuoco;
import DTO.DTOOrdine;
import DTO.DTOPietanza;
import DTO.DTOPietanzaCuoco;
import DTO.DTOTavolo;
import entity.EntityCategoriaPietanza;
import entity.EntityIngrediente;
import entity.EntityMenuFisso;
import entity.EntityOrdine;
import entity.EntityPietanza;
import entity.EntityRistorante;
import entity.EntityTavolo;

/**
 * Controller principale del sistema che implementa il pattern Singleton.
 * Agisce come intermediario tra le classi boundary e il database, gestendo
 * tutte le operazioni di business logic e l'accesso ai dati.
 */
public class Controller {

    private static Controller instance = null;

    private Controller() {
    };

    /**
     * Metodo per ottenere l'istanza singleton del controller
     * 
     * @return Istanza singleton del controller
     */
    public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    ///// PIETANZE////////////////////////////////////////////////////////////////////
    /**
     * Recupera tutte le pietanze dal database
     * 
     * @return Lista di tutte le pietanze
     */
    public static ArrayList<DTOPietanza> getAllPietanze() {
        ArrayList<DTOPietanza> dto_pietanze_liste = new ArrayList<>();

        dto_pietanze_liste = EntityPietanza.getAllPietanze();
        System.out.println("Pietanze recuperate: " + dto_pietanze_liste.size());
        System.out.println("Pietanze: " + dto_pietanze_liste);
        return dto_pietanze_liste;
    }

    /**
     * Recupera le pietanze filtrate per categoria
     * 
     * @param idCategoria ID della categoria per filtrare le pietanze
     * @return Lista di oggetti Pietanza appartenenti alla categoria specificata
     */
    public static ArrayList<DTOPietanza> getPietanzeByCategoria(int idCategoria) {

        ArrayList<DTOPietanza> dto_pietanze_liste = new ArrayList<>();

        dto_pietanze_liste = EntityPietanza.getPietanzePerCategoria(idCategoria);
        System.out.println("Pietanze recuperate: " + dto_pietanze_liste.size());
        System.out.println("Pietanze: " + dto_pietanze_liste);

        return dto_pietanze_liste;

    }

    ///// TAVOLI////////////////////////////////////////////////////////////////////
    /**
     * Recupera tutti i tavoli dal database
     * 
     * @return Lista di oggetti DTOTavolo contenente tutti i tavoli
     */
    public static ArrayList<DTOTavolo> getAllTavoli() {

        ArrayList<DTOTavolo> dto_tavoli_liste = new ArrayList<>();

        dto_tavoli_liste = EntityTavolo.getAllTavoli();
        System.out.println("Tavoli recuperati: " + dto_tavoli_liste.size());
        System.out.println("Tavoli: " + dto_tavoli_liste);

        return dto_tavoli_liste;

    }

    /**
     * Recupera i tavoli in base al loro stato (es. "libero", "occupato")
     * 
     * @param stato Stato del tavolo da filtrare
     * @return Lista di oggetti DTOTavolo con lo stato specificato
     */
    public static ArrayList<DTOTavolo> getTavoliByStato(String stato) {

        ArrayList<DTOTavolo> dto_tavoli_liste = new ArrayList<>();

        dto_tavoli_liste = EntityTavolo.getTavoliByStato(stato);
        System.out.println("Tavoli recuperati: " + dto_tavoli_liste.size());
        System.out.println("Tavoli: " + dto_tavoli_liste);

        System.out.println("Tavoli recuperati: " + dto_tavoli_liste.size());
        System.out.println("Tavoli: " + dto_tavoli_liste);

        return dto_tavoli_liste;

    }

    ///// MENUFISSI////////////////////////////////////////////////////////////////////
    /**
     * Recupera tutti i menu fissi dal database
     * 
     * @return Lista di oggetti DTOMenuFisso contenente tutti i menu fissi
     */
    public static ArrayList<DTOMenuFisso> getTuttiMenuFissi() {
        ArrayList<DTOMenuFisso> dto_menu_fissi_liste = new ArrayList<>();

        dto_menu_fissi_liste = EntityMenuFisso.getTuttiMenuFissi();
        // Stampa per debug
        System.out.println("Menu fissi recuperati: " + dto_menu_fissi_liste.size());
        System.out.println("Menu fissi: " + dto_menu_fissi_liste);

        return dto_menu_fissi_liste;
    }

    ///// CATEGORIAPIETANZA////////////////////////////////////////////////////////////////////
    /**
     * Recupera tutte le categorie di pietanze dal database
     * 
     * @return Lista di oggetti DTOCategoriaPietanza contenente tutte le categorie
     */
    public static ArrayList<DTOCategoriaPietanza> getCategoriePietanze() {
        ArrayList<DTOCategoriaPietanza> dto_categorie_liste = new ArrayList<>();

        dto_categorie_liste = EntityCategoriaPietanza.getTutteCategorie();
        // Stampa per debug
        System.out.println("Categorie pietanze recuperate: " + dto_categorie_liste.size());
        System.out.println("Categorie pietanze: " + dto_categorie_liste);

        return dto_categorie_liste;

    }

    ///// ORDINE////////////////////////////////////////////////////////////////////
    /**
     * Recupera tutti gli ordini dal database in base al loro stato
     * 
     * @return Lista di oggetti DTOOrdine contenente tutti gli ordini
     */
    public static ArrayList<DTOOrdine> getOrdiniByStato(String stato) {
        ArrayList<DTOOrdine> dto_ordini_liste = new ArrayList<>();

        dto_ordini_liste = EntityOrdine.getOrdiniPerStato(stato);
        System.out.println("Ordini recuperati: " + dto_ordini_liste.size());
        System.out.println("Ordini: " + dto_ordini_liste);

        return dto_ordini_liste;

    }

    /**
     * Aggiunge una pietanza all'ordine specificato
     * 
     * @param idOrdine   ID dell'ordine a cui aggiungere la pietanza
     * @param idPietanza ID della pietanza da aggiungere
     * @param quantita   Quantità della pietanza da aggiungere
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public static boolean aggiungiPietanzaAllOrdine(int idOrdine, int idPietanza, int quantita) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        boolean result = ordine.aggiungiPietanza(idPietanza, quantita);
        if (result) {
            System.out.println("Pietanza aggiunta all'ordine con ID: " + idOrdine);
        } else {
            System.err.println("Errore nell'aggiunta della pietanza all'ordine con ID: " + idOrdine);
        }
        return result;
    }

    /**
     * Aggiunge un menu fisso all'ordine specificato
     * 
     * @param idOrdine    ID dell'ordine a cui aggiungere il menu fisso
     * @param idMenuFisso ID del menu fisso da aggiungere
     * @param quantita    Quantità del menu fisso da aggiungere
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public static boolean aggiungiMenuFisso(int idOrdine, int idMenuFisso, int quantita) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        boolean result = ordine.aggiungiMenuFisso(idOrdine, idMenuFisso, quantita);
        if (result) {
            System.out.println("Menu fisso aggiunto all'ordine con ID: " + idOrdine);
        } else {
            System.err.println("Errore nell'aggiunta del menu fisso all'ordine con ID: " + idOrdine);
        }
        return result;
    }

    /**
     * Crea un nuovo ordine per un tavolo specifico
     * 
     * @param num_persone Numero di persone per l'ordine
     * @param id_tavolo   ID del tavolo associato all'ordine
     * @param stato       Stato iniziale dell'ordine (es. "in_attesa")
     * @return L'oggetto EntityOrdine creato
     */
    public static EntityOrdine CreaOrdine(int num_persone, int id_tavolo, String stato) {
        EntityOrdine ordine = new EntityOrdine();
        ordine = ordine.creaOrdine(id_tavolo, num_persone, 1, stato);
        EntityTavolo tavolo = new EntityTavolo(id_tavolo);
        String statoTavolo = "occupato"; // Imposta lo stato del tavolo a "occupato"
        tavolo.setStato(statoTavolo);
        // Salva lo stato del tavolo nel database
        tavolo.aggiornaStato(statoTavolo);

        return ordine;
    }

    /**
     * Conferma un ordine specifico, impostando il suo stato a "in_attesa"
     * 
     * @param idOrdine ID dell'ordine da confermare
     * @param idTavolo ID del tavolo associato all'ordine
     */
    public static void ConfermaOrdine(int idOrdine, int idTavolo) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ordine.aggiornaStato("in_attesa");
    }

    /**
     * Recupera le pietanze associate a un ordine specifico
     * 
     * @param idOrdine ID dell'ordine di cui recuperare le pietanze
     * @return Lista di oggetti DTOPietanzaCuoco associati all'ordine
     */
    public static ArrayList<DTOPietanzaCuoco> getPietanzeDaOrdine(int idOrdine) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ArrayList<DTOPietanzaCuoco> pietanzeDAoRDINE = ordine.getPietanzeDaOrdine();
        ;
        return pietanzeDAoRDINE;
    }

    public static ArrayList<DTOMenuFissoCuoco> getMenuFissiDaOrdine(int idOrdine) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ArrayList<DTOMenuFissoCuoco> menuFissiDAoRDINE = ordine.getMenuFissiDaOrdine();
        return menuFissiDAoRDINE;
    }

    /**
     * Aggiorna lo stato di un ordine
     * 
     * @param idOrdine ID dell'ordine da aggiornare
     * @param stato    Nuovo stato dell'ordine
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public static boolean aggiornaStatoOrdine(int idOrdine, String stato) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        int result = ordine.aggiornaStato(stato);
        boolean isStatoValido = false;
        if (result >= 0) {
            System.out.println("Stato dell'ordine con ID " + idOrdine + " aggiornato a: " + stato);
            isStatoValido = true;
        } else {
            System.err.println("Errore nell'aggiornamento dello stato dell'ordine con ID: " + idOrdine);

        }
        return isStatoValido;
    }

    /**
     * Recupera l'ordine associato a un tavolo specifico
     * 
     * @param id_tavolo ID del tavolo di cui recuperare l'ordine
     * @return DTOOrdine contenente i dettagli dell'ordine, o null se non esiste
     */
    public static DTOOrdine getOrdineByTavolo(int id_tavolo) {
        DTOOrdine dtoOrdine = EntityOrdine.getOrdinePerTavolo(id_tavolo);
        if (dtoOrdine == null) {
            System.err.println("Nessun ordine trovato per il tavolo specificato.");
        } else {
            System.out.println("Ordine recuperato: " + dtoOrdine);
        }
        return dtoOrdine;
    }

    /**
     * Genera un file PDF con il conto per un tavolo specifico.
     * Recupera le informazioni dell'ordine associato al tavolo e genera un PDF
     * utilizzando la classe ContoPdfExporter.
     * 
     * @param idTavolo ID del tavolo per cui generare il conto
     * @return Path del file PDF generato
     * @throws Exception se si verifica un errore durante la generazione del PDF o
     *                   se non esiste un ordine per il tavolo specificato
     */
    public static Path generaPdfConto(int idTavolo) throws Exception {
        // Recupera l'ordine per il tavolo
        DTOOrdine ordine = EntityOrdine.getOrdinePerTavolo(idTavolo);

        if (ordine == null) {
            throw new Exception("Nessun ordine trovato per il tavolo " + idTavolo);
        }

        // Genera il PDF del conto utilizzando ContoPdfExporter
        try {
            Path pdfPath = ContoPdfExporter.generaPdf(ordine);
            System.out.println("PDF del conto generato con successo: " + pdfPath);
            return pdfPath;
        } catch (Exception e) {
            System.err.println("Errore durante la generazione del PDF: " + e.getMessage());
            throw new Exception("Impossibile generare il PDF del conto: " + e.getMessage());
        }
    }

    /////////////// INGREDIENTE////////////////////////////////////////////////////////
    /**
     * Recupera gli ingredienti esauriti dal database
     * 
     * @return Lista di DTOIngrediente contenente gli ingredienti esauriti
     */
    public static ArrayList<DTOIngrediente> generaReport() {
        ArrayList<DTOIngrediente> dto_ingredienti_liste = EntityIngrediente.getIngredientiEsauriti();
        return dto_ingredienti_liste;
    }

    /////////////// GENERALI////////////////////////////////////////////////////////
    /**
     * Registra il pagamento di un ordine e libera il tavolo associato
     * 
     * @param idTavolo ID del tavolo associato all'ordine da pagare
     * @return true se il pagamento è stato registrato con successo, false
     *         altrimenti
     */
    public static boolean registraPagamentoOrdine(int idTavolo) {
        DTOOrdine dtoOrdine = EntityOrdine.getOrdinePerTavolo(idTavolo);
        int idOrdine = dtoOrdine.getIdOrdine();

        EntityOrdine ordine = new EntityOrdine(idOrdine);
        String statoOrdine = "pagato";

        if (ordine.aggiornaStato(statoOrdine) < 0) {
            return false;
        }

        EntityTavolo tavolo = new EntityTavolo(idTavolo);
        String statoTavolo = "libero"; // Imposta lo stato del tavolo a "libero"
        if (tavolo.aggiornaStato(statoTavolo) < 0) {
            return false;
        }
        System.out.println("Pagamento registrato per l'ordine con ID: " + idOrdine + " e tavolo con ID: " + idTavolo
                + "aggiornato a 'pagato' e 'libero' rispettivamente.");
        return true;

    }

    /**
     * Recupera i dettagli di preparazione di una pietanza (ingredienti e
     * istruzioni)
     * 
     * @param nome_pietanza Nome della pietanza di cui recuperare i dettagli
     * @return DTOIngredientiRicettaPietanza contenente gli ingredienti e le
     *         istruzioni di preparazione, o null se la pietanza non esiste
     */
    public static DTOIngredientiRicettaPietanza getDettagliPreparazionePietanza(String nome_pietanza) {
        int id_pietanza = EntityPietanza.getIdPietanzaByNome(nome_pietanza);
        if (id_pietanza == -1) {
            System.err.println("Pietanza non trovata: " + nome_pietanza);
            return null;
        }
        EntityPietanza pietanza = new EntityPietanza(id_pietanza);
        DTOIngredientiRicettaPietanza dto = pietanza.getIngredientiRicettaPietanza();
        if (dto == null) {
            System.err.println("Dettagli di preparazione non trovati per la pietanza: " + nome_pietanza);
            return null;
        }
        return dto;
    }

    /**
     * Recupera il costo del coperto impostato per il ristorante
     * 
     * @return il costo del coperto
     */
    public static double getCostoCoperto() {
        // Utilizziamo EntityRistorante per recuperare l'informazione
        EntityRistorante ristorante = new EntityRistorante(1); // Abbiamo un solo ristorante
        return ristorante.getCostoCoperto();
    }
}
