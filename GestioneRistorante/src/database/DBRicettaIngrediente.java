package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import CFG.DBConnection;

public class DBRicettaIngrediente {
    // Attributi che mappano le colonne della tabella
    private int idRicetta;
    private int idIngrediente;
    private float quantita;

    // Costruttori
    public DBRicettaIngrediente() {
        // Vuoto
    }

    public DBRicettaIngrediente(int idRicetta, int idIngrediente, float quantita) {
        this.idRicetta = idRicetta;
        this.idIngrediente = idIngrediente;
        this.quantita = quantita;
    }

    public DBRicettaIngrediente(int idRicetta, int idIngrediente) {
        this.idRicetta = idRicetta;
        this.idIngrediente = idIngrediente;
        caricaDaDB();
    }

    /**
     * Recupera tutti gli ingredienti di una ricetta
     */
    public static ArrayList<DBRicettaIngrediente> getIngredientiPerRicetta(int idRicetta) {
        ArrayList<DBRicettaIngrediente> lista = new ArrayList<>();
        String query = "SELECT * FROM ricetta_ingrediente WHERE id_ricetta = " + idRicetta;
        try {
            ResultSet rs = DBConnection.selectQuery(query);
            while (rs.next()) {
                DBRicettaIngrediente ri = new DBRicettaIngrediente(
                    rs.getInt("id_ricetta"),
                    rs.getInt("id_ingrediente"),
                    rs.getFloat("quantita")
                );
                lista.add(ri);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel recupero ingredienti ricetta: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Carica la quantità dal database per questa coppia idRicetta-idIngrediente
     */
    private void caricaDaDB() {
        String query = String.format(
            "SELECT quantita FROM ricetta_ingrediente WHERE id_ricetta = %d AND id_ingrediente = %d",
            this.idRicetta, this.idIngrediente);
        try {
            ResultSet rs = DBConnection.selectQuery(query);
            if (rs.next()) {
                this.quantita = rs.getFloat("quantita");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Errore nel caricamento di ricetta_ingrediente: " + e.getMessage());
        }
    }

    // Getter e Setter
    public int getIdRicetta() {
        return idRicetta;
    }

    public void setIdRicetta(int idRicetta) {
        this.idRicetta = idRicetta;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public float getQuantita() {
        return quantita;
    }

    public void setQuantita(float quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return "DBRicettaIngrediente{" +
                "idRicetta=" + idRicetta +
                ", idIngrediente=" + idIngrediente +
                ", quantita=" + quantita +
                '}';
    }
}
