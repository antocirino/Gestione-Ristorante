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

    public static EntityOrdine CreaOrdine(int idOrdine, int num_persone, int id_tavolo, String stato){
        EntityOrdine ordine = new EntityOrdine();
        ordine = ordine.creaOrdine(idOrdine, num_persone, id_tavolo, stato);
        EntityTavolo tavolo = new EntityTavolo(id_tavolo);
        String statoTavolo = "occupato"; // Imposta lo stato del tavolo a "occupato"
        tavolo.setStato(statoTavolo);

        return ordine;
    }

    public static void ConfermaOrdine(int idOrdine, int idTavolo){
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        ordine.aggiornaStato("in attesa");


    }

    public static ArrayList<DTOPietanzaCuoco> getPietanzeDaOrdine(int idOrdine){
        ArrayList<DTOPietanzaCuoco> pietanzeDAoRDINE = new ArrayList<>();
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        return pietanzeDAoRDINE = ordine.getPietanzeDaOrdine();
    }

    public static ArrayList<DTOMenuFissoCuoco> getMenuFissiDaOrdine(int idOrdine){
        ArrayList<DTOMenuFissoCuoco> menuFissiDAoRDINE = new ArrayList<>();
        EntityOrdine ordine = new EntityOrdine(idOrdine);
        return menuFissiDAoRDINE = ordine.getMenuFissiDaOrdine();
    }

     /////////////// INGREDIENTE//////////////////////////////////////////////////////// 
    public static ArrayList<DTOIngrediente> generaReport(){
        ArrayList<DTOIngrediente> dto_ingredienti_liste = EntityIngrediente.getIngredientiEsauriti();
        return dto_ingredienti_liste;
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

    /// ACHTUNG --> VALUTARE SE ELIMINARE QUESTO METODO
    /**
     * 
     * Recupera tutti i menu fissi dal database
     * 
     * @return Mappa con chiave l'ID del menu e valore un oggetto Map con le
     *         informazioni del menu
     * 
     * 
     * 
     *         public Map<Integer, Map<String, Object>> getMenuFissi() {
     *         Map<Integer, Map<String, Object>> menuFissi = new HashMap<>();
     * 
     *         try {
     *         String query = "SELECT id_menu, nome, prezzo, descrizione FROM
     *         menu_fisso ORDER BY nome";
     *         PreparedStatement pstmt = connection.prepareStatement(query);
     *         ResultSet rs = pstmt.executeQuery();
     * 
     *         while (rs.next()) {
     *         int idMenu = rs.getInt("id_menu");
     *         Map<String, Object> menuInfo = new HashMap<>();
     *         menuInfo.put("nome", rs.getString("nome"));
     *         menuInfo.put("prezzo", rs.getDouble("prezzo"));
     *         menuInfo.put("descrizione", rs.getString("descrizione"));
     * 
     *         menuFissi.put(idMenu, menuInfo);
     *         }
     * 
     *         rs.close();
     *         pstmt.close();
     *         } catch (SQLException e) {
     *         System.err.println("Errore nel recupero dei menu fissi: " +
     *         e.getMessage());
     *         }
     * 
     *         return menuFissi;
     *         }
     * 
     */

    /**
     * Inserisce un nuovo ordine nel database
     * 
     * @param idTavolo       ID del tavolo che ha effettuato l'ordine
     * @param elementiOrdine Lista di elementi dell'ordine (può contenere pietanze o
     *                       menu fissi)
     * @param note           Note aggiuntive per l'ordine
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    public boolean insertOrdine(int idTavolo, List<Map<String, Object>> elementiOrdine, String note) {
        try {
            // Prima inserisco l'ordine
            connection.setAutoCommit(false);

            String queryOrdine = "INSERT INTO ordine (id_tavolo, stato, note, timestamp) VALUES (?, 'in attesa', ?, NOW())";
            PreparedStatement pstmtOrdine = connection.prepareStatement(queryOrdine, Statement.RETURN_GENERATED_KEYS);
            pstmtOrdine.setInt(1, idTavolo);
            pstmtOrdine.setString(2, note);
            pstmtOrdine.executeUpdate();

            // Ottengo l'ID dell'ordine appena inserito
            ResultSet rsOrdine = pstmtOrdine.getGeneratedKeys();
            if (!rsOrdine.next()) {
                connection.rollback();
                return false;
            }

            int idOrdine = rsOrdine.getInt(1);

            // Ora inserisco i dettagli dell'ordine
            String queryPietanza = "INSERT INTO dettaglio_ordine (id_ordine, id_pietanza, quantita) VALUES (?, ?, ?)";
            PreparedStatement pstmtPietanza = connection.prepareStatement(queryPietanza);

            String queryMenu = "INSERT INTO dettaglio_menu_ordine (id_ordine, id_menu, quantita) VALUES (?, ?, ?)";
            PreparedStatement pstmtMenu = connection.prepareStatement(queryMenu);

            for (Map<String, Object> elemento : elementiOrdine) {
                boolean isMenu = (Boolean) elemento.get("isMenu");
                int id = (Integer) elemento.get("id");
                int quantita = (Integer) elemento.get("quantita");

                if (isMenu) {
                    pstmtMenu.setInt(1, idOrdine);
                    pstmtMenu.setInt(2, id);
                    pstmtMenu.setInt(3, quantita);
                    pstmtMenu.executeUpdate();
                } else {
                    pstmtPietanza.setInt(1, idOrdine);
                    pstmtPietanza.setInt(2, id);
                    pstmtPietanza.setInt(3, quantita);
                    pstmtPietanza.executeUpdate();
                }
            }

            // Aggiorno lo stato del tavolo a "occupato" se non lo è già
            String queryTavolo = "UPDATE tavolo SET stato = 'occupato' WHERE id_tavolo = ? AND stato <> 'occupato'";
            PreparedStatement pstmtTavolo = connection.prepareStatement(queryTavolo);
            pstmtTavolo.setInt(1, idTavolo);
            pstmtTavolo.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Errore nel rollback della transazione: " + ex.getMessage());
            }

            System.err.println("Errore nell'inserimento dell'ordine: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera le informazioni di un conto per un determinato tavolo
     * 
     * @param idTavolo ID del tavolo di cui calcolare il conto
     * @return Map con le informazioni del conto (dettagli, totale, etc.)
     */
    public Map<String, Object> getContoTavolo(int idTavolo) {
        Map<String, Object> risultato = new HashMap<>();
        StringBuilder dettagli = new StringBuilder();
        double totale = 0.0;
        int numeroPersone = 0;
        double costoCoperto = 0.0;

        try {
            // Verifica se il tavolo esiste ed è occupato
            String queryTavolo = "SELECT stato FROM tavolo WHERE id_tavolo = ?";
            PreparedStatement stmtTavolo = connection.prepareStatement(queryTavolo);
            stmtTavolo.setInt(1, idTavolo);
            ResultSet rsTavolo = stmtTavolo.executeQuery();

            if (!rsTavolo.next() || !rsTavolo.getString("stato").equals("occupato")) {
                risultato.put("errore", "Il tavolo non esiste o non è occupato");
                return risultato;
            }

            // Recupera l'ultimo ordine per questo tavolo
            String queryOrdine = "SELECT id_ordine, num_persone, data_ordine FROM ordine " +
                    "WHERE id_tavolo = ? AND stato = 'in_attesa' " +
                    "ORDER BY data_ordine DESC LIMIT 1";
            PreparedStatement stmtOrdine = connection.prepareStatement(queryOrdine);
            stmtOrdine.setInt(1, idTavolo);
            ResultSet rsOrdine = stmtOrdine.executeQuery();

            if (!rsOrdine.next()) {
                risultato.put("errore", "Nessun ordine in attesa per questo tavolo");
                return risultato;
            }

            int idOrdine = rsOrdine.getInt("id_ordine");
            numeroPersone = rsOrdine.getInt("num_persone");

            // Recupera il prezzo del coperto
            String queryCoperto = "SELECT prezzo FROM coperto LIMIT 1";
            PreparedStatement stmtCoperto = connection.prepareStatement(queryCoperto);
            ResultSet rsCoperto = stmtCoperto.executeQuery();

            if (rsCoperto.next()) {
                costoCoperto = rsCoperto.getDouble("prezzo");
            }

            double totaleCoperti = costoCoperto * numeroPersone;
            totale += totaleCoperti;

            dettagli.append("RISTORANTE EXAMPLE\n");
            dettagli.append("===================\n\n");
            dettagli.append("Tavolo: ").append(idTavolo).append("\n");
            dettagli.append("Data: ").append(rsOrdine.getString("data_ordine")).append("\n");
            dettagli.append("Numero persone: ").append(numeroPersone).append("\n\n");
            dettagli.append("DETTAGLIO ORDINE:\n");
            dettagli.append("-----------------\n\n");
            dettagli.append(
                    String.format("%-4s %-30s %-10s %-10s %-10s\n", "Q.tà", "Descrizione", "Prezzo", "Totale", ""));

            // Recupera le pietanze ordinate
            String queryPietanze = "SELECT p.nome, dop.quantita, p.prezzo " +
                    "FROM dettaglio_ordine_pietanza dop " +
                    "JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                    "WHERE dop.id_ordine = ?";
            PreparedStatement stmtPietanze = connection.prepareStatement(queryPietanze);
            stmtPietanze.setInt(1, idOrdine);
            ResultSet rsPietanze = stmtPietanze.executeQuery();

            while (rsPietanze.next()) {
                String nome = rsPietanze.getString("nome");
                int quantita = rsPietanze.getInt("quantita");
                double prezzo = rsPietanze.getDouble("prezzo");
                double subtotale = quantita * prezzo;
                totale += subtotale;

                dettagli.append(String.format("%-4d %-30s €%-9.2f €%-9.2f\n",
                        quantita, nome, prezzo, subtotale));
            }

            // Recupera i menu ordinati
            String queryMenus = "SELECT m.nome, dom.quantita, m.prezzo " +
                    "FROM dettaglio_ordine_menu dom " +
                    "JOIN menu_fisso m ON dom.id_menu = m.id_menu " +
                    "WHERE dom.id_ordine = ?";
            PreparedStatement stmtMenus = connection.prepareStatement(queryMenus);
            stmtMenus.setInt(1, idOrdine);
            ResultSet rsMenus = stmtMenus.executeQuery();

            while (rsMenus.next()) {
                String nome = rsMenus.getString("nome") + " (menu)";
                int quantita = rsMenus.getInt("quantita");
                double prezzo = rsMenus.getDouble("prezzo");
                double subtotale = quantita * prezzo;
                totale += subtotale;

                dettagli.append(String.format("%-4d %-30s €%-9.2f €%-9.2f\n",
                        quantita, nome, prezzo, subtotale));
            }

            // Aggiungi coperti
            dettagli.append("\n");
            dettagli.append(String.format("%-4d %-30s €%-9.2f €%-9.2f\n",
                    numeroPersone, "Coperto", costoCoperto, totaleCoperti));

            dettagli.append("\n-----------------\n");
            dettagli.append(String.format("%-45s €%-9.2f\n", "TOTALE", totale));

            // Chiusura delle risorse
            rsMenus.close();
            stmtMenus.close();
            rsPietanze.close();
            stmtPietanze.close();
            rsCoperto.close();
            stmtCoperto.close();
            rsOrdine.close();
            stmtOrdine.close();
            rsTavolo.close();
            stmtTavolo.close();

            // Salvataggio dei risultati
            risultato.put("dettagli", dettagli.toString());
            risultato.put("totale", totale);
            risultato.put("numeroPersone", numeroPersone);
            risultato.put("idOrdine", idOrdine);
            risultato.put("success", true);

        } catch (SQLException e) {
            System.err.println("Errore nel recupero del conto: " + e.getMessage());
            risultato.put("errore", "Errore nel recupero del conto: " + e.getMessage());
            risultato.put("success", false);
        }

        return risultato;
    }

    /**
     * Registra il pagamento di un ordine e libera il tavolo
     * 
     * @param idTavolo ID del tavolo che ha pagato
     * @return true se il pagamento è stato registrato con successo, false
     *         altrimenti
     */
    public boolean registraPagamentoOrdine(int idTavolo) {
        try {
            // Trova l'ordine da pagare
            String queryTrovaOrdine = "SELECT id_ordine FROM ordine WHERE id_tavolo = ? AND stato = 'in_attesa' " +
                    "ORDER BY data_ordine DESC LIMIT 1";
            PreparedStatement stmtTrovaOrdine = connection.prepareStatement(queryTrovaOrdine);
            stmtTrovaOrdine.setInt(1, idTavolo);
            ResultSet rsTrovaOrdine = stmtTrovaOrdine.executeQuery();

            if (!rsTrovaOrdine.next()) {
                System.err.println("Nessun ordine in attesa da pagare per il tavolo " + idTavolo);
                return false;
            }

            int idOrdine = rsTrovaOrdine.getInt("id_ordine");

            connection.setAutoCommit(false);

            // Aggiorna lo stato dell'ordine a pagato
            String queryAggiornaOrdine = "UPDATE ordine SET stato = 'pagato' WHERE id_ordine = ?";
            PreparedStatement stmtAggiornaOrdine = connection.prepareStatement(queryAggiornaOrdine);
            stmtAggiornaOrdine.setInt(1, idOrdine);
            stmtAggiornaOrdine.executeUpdate();

            // Libera il tavolo
            String queryTavolo = "UPDATE tavolo SET stato = 'libero' WHERE id_tavolo = ?";
            PreparedStatement stmtTavolo = connection.prepareStatement(queryTavolo);
            stmtTavolo.setInt(1, idTavolo);
            stmtTavolo.executeUpdate();

            connection.commit();

            // Chiusura delle risorse
            stmtTavolo.close();
            stmtAggiornaOrdine.close();
            rsTrovaOrdine.close();
            stmtTrovaOrdine.close();

            connection.setAutoCommit(true);

            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Errore nel rollback della transazione: " + ex.getMessage());
            }

            System.err.println("Errore nel registrare il pagamento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Stampa il conto di un tavolo (simulato)
     * 
     * @param idTavolo ID del tavolo di cui stampare il conto
     * @return true se la stampa è simulata con successo, false altrimenti
     */
    public boolean stampaConto(int idTavolo) {
        try {
            // Verifica se esiste un ordine attivo
            String queryTrovaOrdine = "SELECT id_ordine FROM ordine WHERE id_tavolo = ? AND stato = 'in_attesa' " +
                    "ORDER BY data_ordine DESC LIMIT 1";
            PreparedStatement stmtTrovaOrdine = connection.prepareStatement(queryTrovaOrdine);
            stmtTrovaOrdine.setInt(1, idTavolo);
            ResultSet rsTrovaOrdine = stmtTrovaOrdine.executeQuery();

            boolean result = rsTrovaOrdine.next();

            rsTrovaOrdine.close();
            stmtTrovaOrdine.close();

            // In una implementazione reale, qui ci sarebbe la logica per inviare i dati
            // alla stampante
            return result;
        } catch (SQLException e) {
            System.err.println("Errore nella stampa del conto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera tutti gli ordini attivi con un determinato stato
     * 
     * @param stato Stato degli ordini da recuperare
     * @return Lista di ordini come Map<String, Object>
     */
    /*
     * public List<Map<String, Object>> getOrdiniByStato(String stato) {
     * List<Map<String, Object>> ordini = new ArrayList<>();
     * 
     * try {
     * String query =
     * "SELECT o.id_ordine, o.id_tavolo, o.data_ordine, o.stato, t.numero AS numero_tavolo "
     * +
     * "FROM ordine o JOIN tavolo t ON o.id_tavolo = t.id_tavolo " +
     * "WHERE o.stato = ? " +
     * "ORDER BY o.data_ordine";
     * PreparedStatement pstmt = connection.prepareStatement(query);
     * pstmt.setString(1, stato);
     * ResultSet rs = pstmt.executeQuery();
     * 
     * while (rs.next()) {
     * Map<String, Object> ordine = new HashMap<>();
     * ordine.put("idOrdine", rs.getInt("id_ordine"));
     * ordine.put("idTavolo", rs.getInt("id_tavolo"));
     * ordine.put("numeroTavolo", rs.getInt("numero_tavolo"));
     * ordine.put("dataOrdine", rs.getTimestamp("data_ordine"));
     * ordine.put("stato", rs.getString("stato"));
     * ordini.add(ordine);
     * }
     * 
     * rs.close();
     * pstmt.close();
     * } catch (SQLException e) {
     * System.err.println("Errore nel recupero degli ordini: " + e.getMessage());
     * }
     * 
     * return ordini;
     * }
     */

    /**
     * Recupera i dettagli di un ordine specifico
     * 
     * @param idOrdine ID dell'ordine di cui recuperare i dettagli
     * @return Lista di dettagli come Map<String, Object>
     */
    public List<Map<String, Object>> getDettagliOrdine(int idOrdine) {
        List<Map<String, Object>> dettagli = new ArrayList<>();

        try {
            // Recupera i dettagli delle pietanze
            String queryPietanze = "SELECT dop.id_dettaglio, p.id_pietanza, p.nome, dop.quantita, p.id_categoria, cp.nome AS nome_categoria "
                    +
                    "FROM dettaglio_ordine_pietanza dop " +
                    "JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                    "JOIN categoria_pietanza cp ON p.id_categoria = cp.id_categoria " +
                    "WHERE dop.id_ordine = ? " +
                    "ORDER BY cp.nome, p.nome";
            PreparedStatement pstmtPietanze = connection.prepareStatement(queryPietanze);
            pstmtPietanze.setInt(1, idOrdine);
            ResultSet rsPietanze = pstmtPietanze.executeQuery();

            while (rsPietanze.next()) {
                Map<String, Object> dettaglio = new HashMap<>();
                dettaglio.put("idDettaglio", rsPietanze.getInt("id_dettaglio"));
                dettaglio.put("idPietanza", rsPietanze.getInt("id_pietanza"));
                dettaglio.put("nome", rsPietanze.getString("nome"));
                dettaglio.put("quantita", rsPietanze.getInt("quantita"));
                dettaglio.put("categoria", rsPietanze.getString("nome_categoria"));
                dettaglio.put("tipo", "Pietanza");
                dettagli.add(dettaglio);
            }

            rsPietanze.close();
            pstmtPietanze.close();

            // Recupera i dettagli dei menu fissi
            String queryMenus = "SELECT dom.id_dettaglio, m.id_menu, m.nome, dom.quantita, 'Menu Fisso' AS categoria " +
                    "FROM dettaglio_ordine_menu dom " +
                    "JOIN menu_fisso m ON dom.id_menu = m.id_menu " +
                    "WHERE dom.id_ordine = ? " +
                    "ORDER BY m.nome";
            PreparedStatement pstmtMenus = connection.prepareStatement(queryMenus);
            pstmtMenus.setInt(1, idOrdine);
            ResultSet rsMenus = pstmtMenus.executeQuery();

            while (rsMenus.next()) {
                Map<String, Object> dettaglio = new HashMap<>();
                dettaglio.put("idDettaglio", rsMenus.getInt("id_dettaglio"));
                dettaglio.put("idMenu", rsMenus.getInt("id_menu"));
                dettaglio.put("nome", rsMenus.getString("nome"));
                dettaglio.put("quantita", rsMenus.getInt("quantita"));
                dettaglio.put("categoria", rsMenus.getString("categoria"));
                dettaglio.put("tipo", "Menu");
                dettagli.add(dettaglio);
            }

            rsMenus.close();
            pstmtMenus.close();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei dettagli dell'ordine: " + e.getMessage());
        }

        return dettagli;
    }

    /**
     * Recupera gli ingredienti di una pietanza
     * 
     * @param idPietanza ID della pietanza
     * @return Lista di ingredienti come Map<String, Object>
     */
    public List<Map<String, Object>> getIngredientiPietanza(int idPietanza) {
        List<Map<String, Object>> ingredienti = new ArrayList<>();

        try {
            String query = "SELECT i.id_ingrediente, i.nome, i.unita_misura, r.quantita " +
                    "FROM ricetta r " +
                    "JOIN ingrediente i ON r.id_ingrediente = i.id_ingrediente " +
                    "WHERE r.id_pietanza = ? " +
                    "ORDER BY i.nome";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, idPietanza);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> ingrediente = new HashMap<>();
                ingrediente.put("idIngrediente", rs.getInt("id_ingrediente"));
                ingrediente.put("nome", rs.getString("nome"));
                ingrediente.put("unitaMisura", rs.getString("unita_misura"));
                ingrediente.put("quantita", rs.getDouble("quantita"));
                ingredienti.add(ingrediente);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli ingredienti: " + e.getMessage());
        }

        return ingredienti;
    }

    // Altri metodi possono essere aggiunti secondo le necessità applicative
}
