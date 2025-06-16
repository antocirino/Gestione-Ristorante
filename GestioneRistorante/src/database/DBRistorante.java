package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import CFG.DBConnection;
import entity.EntityRistorante;

/**
 * Classe DAO per gestire l'accesso ai dati della tabella 'ristorante' nel
 * database
 */
public class DBRistorante {
    // Attributi che mappano le colonne della tabella
    private int idRistorante;
    private String nome;
    private int numeroTavoli;
    private double costoCoperto;

    /**
     * Costruttore che carica un ristorante dal database tramite il suo ID
     * 
     * @param idRistorante l'ID del ristorante da caricare
     */
    public DBRistorante(int idRistorante) {
        this.idRistorante = idRistorante;
        caricaDaDB();
    }

    /**
     * Costruttore vuoto per creare nuovi oggetti ristorante
     */
    public DBRistorante() {
        // Costruttore vuoto
    }

    /**
     * Carica i dati del ristorante dal database
     */
    public void caricaDaDB() {
        String query = "SELECT * FROM ristorante WHERE id_ristorante = " + this.idRistorante;

        System.out.println(query); // Per debug

        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.numeroTavoli = rs.getInt("numero_tavoli");
                this.costoCoperto = rs.getDouble("costo_coperto");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel caricamento del ristorante dal database: " + e.getMessage());
        }
    }

    /**
     * Salva un ristorante nel database
     * 
     * @param idRistorante ID del ristorante (0 per auto-incremento)
     * @return l'ID del ristorante se il salvataggio è avvenuto con successo, -1
     *         altrimenti
     */
    public int salvaInDB(int idRistorante) {
        try {
            String query;
            if (idRistorante == 0) {
                // Inserimento nuovo ristorante
                query = "INSERT INTO ristorante (nome, numero_tavoli, costo_coperto) VALUES ('"
                        + this.nome + "', " + this.numeroTavoli + ", " + this.costoCoperto + ")";
            } else {
                // Aggiornamento ristorante esistente
                query = "UPDATE ristorante SET nome = '" + this.nome + "', numero_tavoli = " + this.numeroTavoli
                        + ", costo_coperto = " + this.costoCoperto + " WHERE id_ristorante = " + idRistorante;
            }

            System.out.println(query); // Per debug

            int affectedRows = DBConnection.updateQuery(query);

            if (affectedRows > 0 && idRistorante == 0) {
                // Recupero l'ID del nuovo ristorante inserito
                ResultSet rs = DBConnection.selectQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } else if (affectedRows > 0) {
                return idRistorante;
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel salvataggio del ristorante nel database: " + e.getMessage());
        }
        return -1; // Errore
    }

    /**
     * Elimina un ristorante dal database
     * 
     * @param idRistorante ID del ristorante da eliminare
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int eliminaDaDB(int idRistorante) {
        try {
            String query = "DELETE FROM ristorante WHERE id_ristorante = " + idRistorante;
            System.out.println(query); // Per debug
            return DBConnection.updateQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nell'eliminazione del ristorante dal database: " + e.getMessage());
        }
        return -1; // Errore
    }

    /**
     * Recupera tutti i ristoranti dal database
     * 
     * @return ArrayList di oggetti Ristorante
     */
    public ArrayList<EntityRistorante> getTuttiRistoranti() {
        ArrayList<EntityRistorante> listaRistoranti = new ArrayList<>();
        try {
            String query = "SELECT * FROM ristorante";
            ResultSet rs = DBConnection.selectQuery(query);

            while (rs.next()) {
                EntityRistorante ristorante = new EntityRistorante(
                        rs.getInt("id_ristorante"),
                        rs.getString("nome"),
                        rs.getInt("numero_tavoli"),
                        rs.getDouble("costo_coperto"));
                listaRistoranti.add(ristorante);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero dei ristoranti: " + e.getMessage());
        }
        return listaRistoranti;
    }


    // Getters e setters
    public int getIdRistorante() {
        return idRistorante;
    }

    public void setIdRistorante(int idRistorante) {
        this.idRistorante = idRistorante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroTavoli() {
        return numeroTavoli;
    }

    public void setNumeroTavoli(int numeroTavoli) {
        this.numeroTavoli = numeroTavoli;
    }

    public double getCostoCoperto() {
        return costoCoperto;
    }

    public void setCostoCoperto(double costoCoperto) {
        this.costoCoperto = costoCoperto;
    }
}
