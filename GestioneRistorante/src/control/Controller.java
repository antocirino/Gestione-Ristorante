package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CFG.DBConnection;
import DTO.DTOCategoriaPietanza;
import DTO.DTOIngrediente;
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
        boolean result = ordine.aggiungiMenuFisso(idOrdine, idMenuFisso,quantita);
        if (result) {
            System.out.println("Menu fisso aggiunto all'ordine con ID: " + idOrdine);
        } else {
            System.err.println("Errore nell'aggiunta del menu fisso all'ordine con ID: " + idOrdine);
        }
        return result;
    }

    public static EntityOrdine CreaOrdine(int num_persone, int id_tavolo, String stato){
        EntityOrdine ordine = new EntityOrdine();
        ordine = ordine.creaOrdine(id_tavolo, num_persone, 1, stato);
        EntityTavolo tavolo = new EntityTavolo(id_tavolo);
        String statoTavolo = "occupato"; // Imposta lo stato del tavolo a "occupato"
        tavolo.setStato(statoTavolo);
        // Salva lo stato del tavolo nel database
        tavolo.aggiornaStato(statoTavolo);

        return ordine;
    }

    public static void ConfermaOrdine(int idOrdine, int idTavolo){
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ordine.aggiornaStato("in_attesa");
    }

    public static ArrayList<DTOPietanzaCuoco> getPietanzeDaOrdine(int idOrdine){
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ArrayList<DTOPietanzaCuoco> pietanzeDAoRDINE = ordine.getPietanzeDaOrdine();;
        return pietanzeDAoRDINE;
    }

    public static ArrayList<DTOMenuFissoCuoco> getMenuFissiDaOrdine(int idOrdine){
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ArrayList<DTOMenuFissoCuoco> menuFissiDAoRDINE = ordine.getMenuFissiDaOrdine();
        return menuFissiDAoRDINE ;
    }

    public static boolean aggiornaStatoOrdine(int idOrdine, String stato){
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

    public static DTOOrdine getOrdineByTavolo(int id_tavolo){
        DTOOrdine dtoOrdine = EntityOrdine.getOrdinePerTavolo(id_tavolo);
        if (dtoOrdine == null) {
            System.err.println("Nessun ordine trovato per il tavolo specificato.");
        } else {
            System.out.println("Ordine recuperato: " + dtoOrdine);
        }
        return dtoOrdine;
    }

     /////////////// INGREDIENTE//////////////////////////////////////////////////////// 
    public static ArrayList<DTOIngrediente> generaReport(){
        ArrayList<DTOIngrediente> dto_ingredienti_liste = EntityIngrediente.getIngredientiEsauriti();
        return dto_ingredienti_liste;
    }

    /////////////// GENERALI//////////////////////////////////////////////////////// 

    public static boolean registraPagamentoOrdine(int idTavolo){
        DTOOrdine dtoOrdine = EntityOrdine.getOrdinePerTavolo(idTavolo);
        int idOrdine = dtoOrdine.getIdOrdine();

        EntityOrdine ordine = new EntityOrdine(idOrdine);
        String statoOrdine = "pagato";
        
        if(ordine.aggiornaStato(statoOrdine) < 0){
            return false;
        }

        EntityTavolo tavolo = new EntityTavolo(idTavolo);
        String statoTavolo = "libero"; // Imposta lo stato del tavolo a "libero"
        if(tavolo.aggiornaStato(statoTavolo) < 0){
            return false;
        }
        System.out.println("Pagamento registrato per l'ordine con ID: " + idOrdine + " e tavolo con ID: " + idTavolo + "aggiornato a 'pagato' e 'libero' rispettivamente.");
        return true;

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

    /**
     * Recupera i dettagli di preparazione di una pietanza (ingredienti e istruzioni)
     * 
     * @param nomePietanza Nome della pietanza di cui recuperare i dettagli
     * @return Map contenente gli ingredienti e le istruzioni di preparazione, o null se la pietanza non esiste
     */
    public static Map<String, Object> getDettagliPreparazionePietanza(String nomePietanza) {
        Map<String, Object> risultato = new HashMap<>();
        List<Map<String, Object>> ingredienti = new ArrayList<>();
        
        try {
            Connection conn = DBConnection.getConnection();
            
            // Ottieni l'ID della pietanza dal nome
            String queryPietanza = "SELECT id_pietanza FROM pietanza WHERE nome = ?";
            PreparedStatement stmtPietanza = conn.prepareStatement(queryPietanza);
            stmtPietanza.setString(1, nomePietanza);
            ResultSet rsPietanza = stmtPietanza.executeQuery();
            
            if (!rsPietanza.next()) {
                // Pietanza non trovata
                rsPietanza.close();
                stmtPietanza.close();
                conn.close();
                return null;
            }
            
            int idPietanza = rsPietanza.getInt("id_pietanza");
            risultato.put("idPietanza", idPietanza);
            risultato.put("nomePietanza", nomePietanza);
            
            // Query per ottenere gli ingredienti
            String queryIngredienti = 
                "SELECT i.nome AS ingrediente, ri.quantita, i.unita_misura " +
                "FROM pietanza p " +
                "INNER JOIN ricetta r ON p.id_pietanza = r.id_pietanza " +
                "INNER JOIN ricetta_ingrediente ri ON r.id_ricetta = ri.id_ricetta " +
                "INNER JOIN ingrediente i ON ri.id_ingrediente = i.id_ingrediente " +
                "WHERE p.id_pietanza = ? " +
                "ORDER BY i.nome";
                
            PreparedStatement stmtIngredienti = conn.prepareStatement(queryIngredienti);
            stmtIngredienti.setInt(1, idPietanza);
            ResultSet rsIngredienti = stmtIngredienti.executeQuery();
            
            while (rsIngredienti.next()) {
                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nome", rsIngredienti.getString("ingrediente"));
                ingrediente.put("quantita", rsIngredienti.getDouble("quantita"));
                ingrediente.put("unitaMisura", rsIngredienti.getString("unita_misura"));
                ingredienti.add(ingrediente);
            }
            
            risultato.put("ingredienti", ingredienti);
            
            // Query per ottenere le istruzioni
            String queryIstruzioni = "SELECT istruzioni FROM ricetta WHERE id_pietanza = ?";
            PreparedStatement stmtIstruzioni = conn.prepareStatement(queryIstruzioni);
            stmtIstruzioni.setInt(1, idPietanza);
            ResultSet rsIstruzioni = stmtIstruzioni.executeQuery();
            
            if (rsIstruzioni.next()) {
                risultato.put("istruzioni", rsIstruzioni.getString("istruzioni"));
            } else {
                risultato.put("istruzioni", "Nessuna istruzione disponibile.");
            }
            
            // Chiudi le risorse
            rsIstruzioni.close();
            stmtIstruzioni.close();
            rsIngredienti.close();
            stmtIngredienti.close();
            rsPietanza.close();
            stmtPietanza.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            risultato.put("errore", "Errore durante il recupero dei dettagli: " + e.getMessage());
        }
        
        return risultato;
    }

    /**
     * Recupera i dettagli di preparazione di un menu fisso (ingredienti e istruzioni)
     * 
     * @param nomeMenu Nome del menu fisso di cui recuperare i dettagli
     * @return Map contenente gli ingredienti e le istruzioni di preparazione, o null se il menu non esiste
     */
    public static Map<String, Object> getDettagliPreparazioneMenu(String nomeMenu) {
        Map<String, Object> risultato = new HashMap<>();
        List<Map<String, Object>> ingredienti = new ArrayList<>();
        List<Map<String, Object>> istruzioniPietanze = new ArrayList<>();
        
        try {
            Connection conn = DBConnection.getConnection();
            
            // Ottieni l'ID del menu dal nome
            String queryMenu = "SELECT id_menu FROM menu_fisso WHERE nome = ?";
            PreparedStatement stmtMenu = conn.prepareStatement(queryMenu);
            stmtMenu.setString(1, nomeMenu);
            ResultSet rsMenu = stmtMenu.executeQuery();
            
            if (!rsMenu.next()) {
                // Menu non trovato
                rsMenu.close();
                stmtMenu.close();
                conn.close();
                return null;
            }
            
            int idMenu = rsMenu.getInt("id_menu");
            risultato.put("idMenu", idMenu);
            risultato.put("nomeMenu", nomeMenu);
            
            // Query per ottenere gli ingredienti
            String queryIngredienti = 
                "SELECT " +
                "    i.nome AS ingrediente, " +
                "    SUM(ri.quantita) AS quantita_totale, " +
                "    i.unita_misura " +
                "FROM " +
                "    menu_fisso mf " +
                "INNER JOIN composizione_menu cm ON mf.id_menu = cm.id_menu " +
                "INNER JOIN pietanza p ON cm.id_pietanza = p.id_pietanza " +
                "INNER JOIN ricetta r ON p.id_pietanza = r.id_pietanza " +
                "INNER JOIN ricetta_ingrediente ri ON r.id_ricetta = ri.id_ricetta " +
                "INNER JOIN ingrediente i ON ri.id_ingrediente = i.id_ingrediente " +
                "WHERE " +
                "    mf.id_menu = ? " +
                "GROUP BY " +
                "    i.nome, i.unita_misura " +
                "ORDER BY " +
                "    i.nome";
                
            PreparedStatement stmtIngredienti = conn.prepareStatement(queryIngredienti);
            stmtIngredienti.setInt(1, idMenu);
            ResultSet rsIngredienti = stmtIngredienti.executeQuery();
            
            while (rsIngredienti.next()) {
                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("nome", rsIngredienti.getString("ingrediente"));
                ingrediente.put("quantita", rsIngredienti.getDouble("quantita_totale"));
                ingrediente.put("unitaMisura", rsIngredienti.getString("unita_misura"));
                ingredienti.add(ingrediente);
            }
            
            risultato.put("ingredienti", ingredienti);
            
            // Query per ottenere le istruzioni di ogni pietanza nel menu
            String queryIstruzioni = 
                "SELECT " +
                "    p.nome AS nome_pietanza, " +
                "    r.istruzioni " +
                "FROM " +
                "    menu_fisso mf " +
                "INNER JOIN composizione_menu cm ON mf.id_menu = cm.id_menu " +
                "INNER JOIN pietanza p ON cm.id_pietanza = p.id_pietanza " +
                "INNER JOIN ricetta r ON p.id_pietanza = r.id_pietanza " +
                "WHERE " +
                "    mf.id_menu = ? " +
                "ORDER BY " +
                "    cm.id_pietanza";
                
            PreparedStatement stmtIstruzioni = conn.prepareStatement(queryIstruzioni);
            stmtIstruzioni.setInt(1, idMenu);
            ResultSet rsIstruzioni = stmtIstruzioni.executeQuery();
            
            while (rsIstruzioni.next()) {
                Map<String, Object> istruzionePietanza = new HashMap<>();
                istruzionePietanza.put("nomePietanza", rsIstruzioni.getString("nome_pietanza"));
                istruzionePietanza.put("istruzioni", rsIstruzioni.getString("istruzioni"));
                istruzioniPietanze.add(istruzionePietanza);
            }
            
            risultato.put("istruzioniPietanze", istruzioniPietanze);
            
            // Chiudi le risorse
            rsIstruzioni.close();
            stmtIstruzioni.close();
            rsIngredienti.close();
            stmtIngredienti.close();
            rsMenu.close();
            stmtMenu.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            risultato.put("errore", "Errore durante il recupero dei dettagli: " + e.getMessage());
        }
        
        return risultato;
    }
}
