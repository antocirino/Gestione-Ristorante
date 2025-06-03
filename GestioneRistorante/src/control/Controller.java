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
import DTO.DTOPietanza;
import entity.EntityPietanza;
import entity.EntityTavolo;

/**
 * Controller principale del sistema che implementa il pattern Singleton.
 * Agisce come intermediario tra le classi boundary e il database, gestendo
 * tutte le operazioni di business logic e l'accesso ai dati.
 */
public class Controller {

    /**
     * Recupera tutte le pietanze dal database
     * 
     * @return Lista di tutte le pietanze
     */
    public List<DTOPietanza> getAllPietanze() {
        List<DTOPietanza> dto_pietanze_liste = new ArrayList<>();

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
    public List<DTOPietanza> getPietanzeByCategoria(int idCategoria) {
        
        List<DTOPietanza> dto_pietanze_liste = new ArrayList<>();

        dto_pietanze_liste = EntityPietanza.getPietanzePerCategoria(idCategoria);
        System.out.println("Pietanze recuperate: " + dto_pietanze_liste.size());
        System.out.println("Pietanze: " + dto_pietanze_liste);
        
        return dto_pietanze_liste;
           
    }


    // Connessione al database
    private Connection connection;
    /**
     * Testa la connessione al database
     * 
     * @return true se la connessione è stabilita correttamente, false altrimenti
     */
    public boolean testDatabaseConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                ;
                connection = DBConnection.getConnection();
            }

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tavolo");

            boolean result = rs.next();

            rs.close();
            stmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Errore durante il test della connessione al database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera tutte le categorie di pietanze dal database
     * 
     * @return Lista di categorie come Map<Integer, String> (id, nome)
     */
    public Map<Integer, String> getCategoriePietanze() {
        Map<Integer, String> categorie = new HashMap<>();

        try {
            String query = "SELECT id_categoria, nome FROM categoria ORDER BY nome";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_categoria");
                String nome = rs.getString("nome");
                categorie.put(id, nome);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero delle categorie: " + e.getMessage());
        }

        return categorie;
    }


    /**
     * Recupera tutti i menu fissi dal database
     * 
     * @return Mappa con chiave l'ID del menu e valore un oggetto Map con le
     *         informazioni del menu
     */
    public Map<Integer, Map<String, Object>> getMenuFissi() {
        Map<Integer, Map<String, Object>> menuFissi = new HashMap<>();

        try {
            String query = "SELECT id_menu, nome, prezzo, descrizione FROM menu_fisso ORDER BY nome";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int idMenu = rs.getInt("id_menu");
                Map<String, Object> menuInfo = new HashMap<>();
                menuInfo.put("nome", rs.getString("nome"));
                menuInfo.put("prezzo", rs.getDouble("prezzo"));
                menuInfo.put("descrizione", rs.getString("descrizione"));

                menuFissi.put(idMenu, menuInfo);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei menu fissi: " + e.getMessage());
        }

        return menuFissi;
    }

    /**
     * Recupera tutti i tavoli dal database
     * 
     * @return Lista di oggetti Tavolo
     */
    public List<EntityTavolo> getAllTavoli() {
        List<EntityTavolo> tavoli = new ArrayList<>();

        try {
            String query = "SELECT id_tavolo,max_posti, stato, id_ristorante FROM tavolo ORDER BY id_tavolo";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String stato = rs.getString("stato");
                EntityTavolo tavolo = new EntityTavolo(
                        rs.getInt("id_tavolo"),
                        rs.getInt("max_posti"),
                        stato,
                        rs.getInt("id_ristorante"));
                tavoli.add(tavolo);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei tavoli: " + e.getMessage());
        }

        return tavoli;
    }

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
    public List<Map<String, Object>> getOrdiniByStato(String stato) {
        List<Map<String, Object>> ordini = new ArrayList<>();

        try {
            String query = "SELECT o.id_ordine, o.id_tavolo, o.data_ordine, o.stato, t.numero AS numero_tavolo " +
                    "FROM ordine o JOIN tavolo t ON o.id_tavolo = t.id_tavolo " +
                    "WHERE o.stato = ? " +
                    "ORDER BY o.data_ordine";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, stato);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> ordine = new HashMap<>();
                ordine.put("idOrdine", rs.getInt("id_ordine"));
                ordine.put("idTavolo", rs.getInt("id_tavolo"));
                ordine.put("numeroTavolo", rs.getInt("numero_tavolo"));
                ordine.put("dataOrdine", rs.getTimestamp("data_ordine"));
                ordine.put("stato", rs.getString("stato"));
                ordini.add(ordine);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli ordini: " + e.getMessage());
        }

        return ordini;
    }

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
     * Aggiorna lo stato di un ordine
     * 
     * @param idOrdine   ID dell'ordine da aggiornare
     * @param nuovoStato Nuovo stato dell'ordine
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean updateStatoOrdine(int idOrdine, String nuovoStato) {
        try {
            String query = "UPDATE ordine SET stato = ? WHERE id_ordine = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, nuovoStato);
            pstmt.setInt(2, idOrdine);
            int result = pstmt.executeUpdate();

            pstmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento dello stato dell'ordine: " + e.getMessage());
            return false;
        }
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
