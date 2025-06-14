package control;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import CFG.DBConnection;
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
     * @return Lista di oggetti Tavolo
     */
    public static ArrayList<DTOTavolo> getAllTavoli() {

        ArrayList<DTOTavolo> dto_tavoli_liste = new ArrayList<>();

        dto_tavoli_liste = EntityTavolo.getAllTavoli();
        System.out.println("Tavoli recuperati: " + dto_tavoli_liste.size());
        System.out.println("Tavoli: " + dto_tavoli_liste);

        return dto_tavoli_liste;

    }

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
     * Recuperare tutti i menu fissi dal database
     * 
     * @return
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
     * @return Lista di categorie come Map<Integer, String> (id, nome)
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
     * Aggiorna lo stato di un ordine
     * 
     * @param idOrdine   ID dell'ordine da aggiornare
     * @param nuovoStato Nuovo stato dell'ordine
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public static boolean updateStatoOrdine(int idOrdine, String nuovoStato) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        int result = ordine.aggiornaStato(nuovoStato);
        if (result == 0) {
            System.err.println("Errore nell'aggiornamento dello stato dell'ordine con ID: " + idOrdine);
            return false;
        }
        System.out.println("Stato dell'ordine con ID " + idOrdine + " aggiornato a: " + nuovoStato);
        return true;

    }

    /**
     * Recupera tutti gli ordini dal database IN BASE ALLO STATO
     * 
     * @return Lista di oggetti Ordine
     */
    public static ArrayList<DTOOrdine> getOrdiniByStato(String stato) {
        ArrayList<DTOOrdine> dto_ordini_liste = new ArrayList<>();

        dto_ordini_liste = EntityOrdine.getOrdiniPerStato(stato);
        System.out.println("Ordini recuperati: " + dto_ordini_liste.size());
        System.out.println("Ordini: " + dto_ordini_liste);

        return dto_ordini_liste;

    }

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

    public static void ConfermaOrdine(int idOrdine, int idTavolo) {
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ordine.aggiornaStato("in_attesa");
    }

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

    public static DTOOrdine getOrdineByTavolo(int id_tavolo) {
        DTOOrdine dtoOrdine = EntityOrdine.getOrdinePerTavolo(id_tavolo);
        if (dtoOrdine == null) {
            System.err.println("Nessun ordine trovato per il tavolo specificato.");
        } else {
            System.out.println("Ordine recuperato: " + dtoOrdine);
        }
        return dtoOrdine;
    }

    /////////////// INGREDIENTE////////////////////////////////////////////////////////
    public static ArrayList<DTOIngrediente> generaReport() {
        ArrayList<DTOIngrediente> dto_ingredienti_liste = EntityIngrediente.getIngredientiEsauriti();
        return dto_ingredienti_liste;
    }

    /////////////// GENERALI////////////////////////////////////////////////////////

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
    /////////////// ACHTUNG/////////////////////////////////////////////////////
    /////////////// ANCORA DA
    /////////////// MODIFICARE/////////////////////////////////////////////////////

    private Connection connection;

    // Connessione al database
    /**
     * Testa la connessione al database
     * 
     * @return true se la connessione è stabilita correttamente, false altrimenti
     */
    public boolean testDatabaseConnection() {
        try {
            System.out.println("Testing database connection...");
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is null or closed, creating a new connection.");
                connection = DBConnection.getConnection();
                System.out.println("New connection created: " + connection);
            }

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tavolo");

            System.out.println("Executing test query on tavolo table...");
            boolean result = rs.next();

            rs.close();
            stmt.close();

            System.out.println("Database connection test successful: " + result);

            return result;
        } catch (SQLException e) {
            System.err.println("Errore durante il test della connessione al database: " + e.getMessage());
            return false;
        }
    }

}
