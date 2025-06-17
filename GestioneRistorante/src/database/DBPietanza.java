package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import CFG.DBConnection;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'pietanza' nel
 * database
 */
public class DBPietanza {
    // Attributi che mappano le colonne della tabella
    private int idPietanza;
    private String nome;
    private double prezzo;
    private int idCategoria;
    private boolean disponibile;
    private String nomeCategoria; // Campo aggiuntivo per memorizzare il nome della categoria

    /**
     * Costruttore che carica una pietanza dal database tramite il suo ID
     * 
     * @param idPietanza l'ID della pietanza da caricare
     */
    public DBPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti pietanza
     */
    public DBPietanza() {
        // Costruttore vuoto
    }

    /**
     * Costruttore per creare una nuova pietanza con i dati specificati
     * 
     * @param nome        Nome della pietanza
     * @param prezzo      Prezzo della pietanza
     * @param idCategoria ID della categoria a cui appartiene la pietanza
     */
    public DBPietanza(int idPietanza, String nome, double prezzo, String Categoria) {
        this.idPietanza = idPietanza;
        this.nome = nome;
        this.prezzo = prezzo;
        this.nomeCategoria = Categoria; // Inizializza il nome della categoria
    }

    /**
     * Costruttore per creare una nuova pietanza con i dati specificati
     * 
     * @param idPietanza    ID della pietanza
     * @param nome          Nome della pietanza
     * @param prezzo        Prezzo della pietanza
     * @param idCategoria   ID della categoria a cui appartiene la pietanza
     * @param disponibile   Stato di disponibilità della pietanza
     * @param nomeCategoria Nome della categoria della pietanza
     */
    public DBPietanza(int idPietanza, String nome, double prezzo, int idCategoria, boolean disponibile,
            String nomeCategoria) {
        this.idPietanza = idPietanza;
        this.nome = nome;
        this.prezzo = prezzo;
        this.idCategoria = idCategoria;
        this.disponibile = disponibile; // Imposta disponibile a true per default
        this.nomeCategoria = nomeCategoria; // Inizializza il nome della categoria come vuoto
    }

    /**
     * Carica i dati della pietanza dal database
     */
    public void caricaDaDB() {
        String query = "SELECT p.*, c.nome as nome_categoria " +
                "FROM pietanza p " +
                "JOIN categoria_pietanza c ON p.id_categoria = c.id_categoria " +
                "WHERE p.id_pietanza = " + this.idPietanza;

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.prezzo = rs.getDouble("prezzo");
                this.idCategoria = rs.getInt("id_categoria");
                this.disponibile = rs.getBoolean("disponibile");
                this.nomeCategoria = rs.getString("nome_categoria");
            } else {
                throw new SQLException("Pietanza non trovata con ID: " + this.idPietanza);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento della pietanza: " + e.getMessage());
        }
    }

    /**
     * Salva le informazioni della pietanza nel database
     * 
     * @param idPietanza l'ID della pietanza (se nuova) o 0 per auto-incremento
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int salvaInDB() {
        int ret = 0;

        String query;
        if (this.idPietanza == 0) {
            // Insert di una nuova pietanza con auto-incremento dell'ID
            query = String.format(Locale.US,
                    "INSERT INTO pietanza (nome, prezzo, id_categoria, disponibile) " +
                            "VALUES ('%s', %.2f, %d, %b)",
                    this.nome, this.prezzo, this.idCategoria, this.disponibile);

        } else {
            // Insert con ID specificato
            query = String.format(Locale.US,
                    "INSERT INTO pietanza (id_pietanza, nome, prezzo, id_categoria, disponibile) " +
                            "VALUES (%d, '%s', %.2f, %d, %b)",
                    idPietanza, this.nome, this.prezzo, this.idCategoria, this.disponibile);
        }

        try {
            ret = DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            ret = -1; // Segnala errore di scrittura
        }

        return ret;
    }

    /**
     * Aggiorna lo stato di disponibilità di una pietanza nel database
     * 
     * @param disponibile nuovo stato di disponibilità
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaDisponibilita(boolean disponibile) {
        try {
            String query = "UPDATE pietanza SET disponibile = " + (disponibile ? "TRUE" : "FALSE") +
                    " WHERE id_pietanza = " + this.idPietanza;

            int result = DBConnection.updateQuery(query);

            if (result < 0) {
                System.err.println("Nessuna riga aggiornata per la pietanza #" + this.idPietanza);
            }

            return result;
        } catch (ClassNotFoundException |

                SQLException e) {
            System.err.println("Errore nell'aggiornamento della disponibilità: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Recupera tutte le pietanze dal database
     * 
     * @return ArrayList di oggetti Pietanza
     */
    public ArrayList<DBPietanza> getTuttePietanze() {
        ArrayList<DBPietanza> listaPietanze = new ArrayList<>();

        String query = "SELECT p.*, c.nome as nome_categoria " +
                "FROM pietanza p " +
                "JOIN categoria_pietanza c ON p.id_categoria = c.id_categoria " +
                "ORDER BY p.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                DBPietanza pietanza = new DBPietanza(
                        rs.getInt("id_pietanza"),
                        rs.getString("nome"),
                        rs.getDouble("prezzo"),
                        rs.getInt("id_categoria"),
                        rs.getBoolean("disponibile"),
                        rs.getString("nome_categoria"));

                listaPietanze.add(pietanza);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle pietanze: " + e.getMessage());
        }

        return listaPietanze;
    }

    /**
     * Recupera le pietanze filtrate per categoria
     * 
     * @param idCategoria ID della categoria per cui filtrare
     * @return ArrayList di oggetti Pietanza della categoria specificata
     */
    public ArrayList<DBPietanza> getPietanzePerCategoria(int idCategoria) {
        ArrayList<DBPietanza> listaPietanze = new ArrayList<>();
        String query = "SELECT p.*, c.nome as nome_categoria " +
                "FROM pietanza p " +
                "JOIN categoria_pietanza c ON p.id_categoria = c.id_categoria " +
                "WHERE p.id_categoria = " + idCategoria + " " +
                "ORDER BY p.nome";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                DBPietanza pietanza = new DBPietanza(
                        rs.getInt("id_pietanza"),
                        rs.getString("nome"),
                        rs.getDouble("prezzo"),
                        rs.getInt("id_categoria"),
                        rs.getBoolean("disponibile"),
                        rs.getString("nome_categoria"));
                listaPietanze.add(pietanza);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero delle pietanze per categoria: " + e.getMessage());
        }

        return listaPietanze;
    }

    /**
     * Recupera l'ID di una pietanza dato il suo nome
     * 
     * @param nomePietanza Nome della pietanza
     * @return ID della pietanza, o -1 se non trovata
     */
    public static int getIdByNome(String nomePietanza) {
        String query = "SELECT id_pietanza FROM pietanza WHERE nome = '" + nomePietanza.replace("'", "''") + "'";
        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                return rs.getInt("id_pietanza");
            } else {
                return -1; // Non trovato
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero id_pietanza per nome: " + e.getMessage());
            return -1;
        }
    }

    // Getters e setters
    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
