package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import CFG.DBConnection;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'ingrediente' nel
 * database
 */
public class DBIngrediente {
    // ATTRIBUTI
    private int idIngrediente;
    private String nome;
    private float quantitaDisponibile;
    private String unitaMisura;
    private float sogliaRiordino;

    public DBIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
        caricaDaDB();
    }

    public DBIngrediente() {
        // Costruttore vuoto per la creazione di una lista di ingredienti
    }

    /**
     * Costruttore per creare un ingrediente con i suoi attributi
     * 
     * @param nome                Nome dell'ingrediente
     * @param quantitaDisponibile Quantità disponibile dell'ingrediente
     * @param unitaMisura         Unità di misura dell'ingrediente
     * @param sogliaRiordino      Soglia di riordino dell'ingrediente
     */
    public DBIngrediente(int id, String nome, float quantitaDisponibile, String unitaMisura, float sogliaRiordino) {
        this.idIngrediente = id;
        this.nome = nome;
        this.quantitaDisponibile = quantitaDisponibile;
        this.unitaMisura = unitaMisura;
        this.sogliaRiordino = sogliaRiordino;
    }

    /**
     * Metodo per caricare un ingrediente dal database tramite il suo ID
     */
    public void caricaDaDB() {

        // 1. definisco la query
        String query = "SELECT * FROM ingrediente WHERE id_ingrediente = " + this.idIngrediente;

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.quantitaDisponibile = rs.getFloat("quantita_disponibile");
                this.unitaMisura = rs.getString("unita_misura");
                this.sogliaRiordino = rs.getFloat("soglia_riordino");
            } else {
                throw new SQLException("Ingrediente non trovato con ID: " + this.idIngrediente);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore durante il caricamento dell'ingrediente: " + e.getMessage());
        }

    }

    /**
     * Metodo per salvare un ingrediente nel database
     * 
     * @param id_ingrediente ID dell'ingrediente da salvare
     * @return 0 se l'inserimento è andato a buon fine, -1 in caso di errore
     */
    public int salvaInDB(int id_ingrediente) {

        int ret = 0;

        String query = String.format(Locale.US,
                "INSERT INTO ingrediente (id_ingrediente, nome, quantita_disponibile, unita_misura, soglia_riordino) " +
                        "VALUES (%d, '%s', %.2f, '%s', %.2f)",
                id_ingrediente, this.nome, this.quantitaDisponibile, this.unitaMisura, this.sogliaRiordino);

        try {

            ret = DBConnection.updateQuery(query);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            ret = -1; // per segnalare l'errore di scrittura
        }

        return ret;
    }

    /**
     * Metodo per ottenere gli ingredienti da riordinare
     * 
     * @return ArrayList di ingredienti con scorte minori o uguali alla soglia di
     *         riordino
     */
    public static ArrayList<DBIngrediente> getIngredientiEsauriti() {
        // metodo per ottenere gli ingredienti con scorte minore della quantità di
        // soglia
        ArrayList<DBIngrediente> lista = new ArrayList<>();
        String query = "SELECT * FROM ingrediente";

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                float quantita = rs.getFloat("quantita_disponibile");
                float soglia = rs.getFloat("soglia_riordino");
                if (quantita <= soglia) {
                    DBIngrediente ing = new DBIngrediente();
                    ing.setIdIngrediente(rs.getInt("id_ingrediente"));
                    ing.setNome(rs.getString("nome"));
                    ing.setQuantitaDisponibile(quantita);
                    ing.setUnitaMisura(rs.getString("unita_misura"));
                    ing.setSogliaRiordino(soglia);

                    lista.add(ing);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nel recupero degli ingredienti esauriti: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Aggiorna la quantità di un ingrediente nel database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int aggiornaQuantita() {
        try {
            String query = "UPDATE ingrediente SET quantita_disponibile = " + this.quantitaDisponibile +
                    " WHERE id_ingrediente = " + this.idIngrediente;

            return DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nell'aggiornamento della quantità: " + e.getMessage());
            return -1;
        }
    }

    // Getters e setters
    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(float quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public float getSogliaRiordino() {
        return sogliaRiordino;
    }

    public void setSogliaRiordino(float sogliaRiordino) {
        this.sogliaRiordino = sogliaRiordino;
    }

    public boolean daSottoScorta() {
        return quantitaDisponibile <= sogliaRiordino;
    }

    @Override
    public String toString() {
        return nome + " (" + quantitaDisponibile + " " + unitaMisura + ")";
    }

}