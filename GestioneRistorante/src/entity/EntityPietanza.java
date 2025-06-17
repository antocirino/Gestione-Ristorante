package entity;

import java.util.ArrayList;
import java.util.List;

import DTO.DTOIngredienteCuoco;
import DTO.DTOIngredientiRicettaPietanza;
import DTO.DTOPietanza;
import database.DBPietanza;

/**
 * Classe che rappresenta una pietanza del menu
 */
public class EntityPietanza {
    private int idPietanza;
    private String nome;
    private double prezzo;
    private int idCategoria;
    private String nomeCategoria;
    private boolean disponibile;
    private List<Object> ingredienti;

    // Costruttori
    /**
     * Costruttore vuoto
     */
    public EntityPietanza() {
        this.ingredienti = new ArrayList<Object>();
        this.disponibile = true;
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param idPietanza    ID della pietanza (0 per auto-incremento)
     * @param nome          Nome della pietanza
     * @param prezzo        Prezzo della pietanza
     * @param idCategoria   ID della categoria a cui appartiene la pietanza
     * @param nomeCategoria Nome della categoria a cui appartiene la pietanza
     */
    public EntityPietanza(int idPietanza, String nome, double prezzo, int idCategoria, String nomeCategoria) {
        this.idPietanza = idPietanza;
        this.nome = nome;
        this.prezzo = prezzo;
        this.idCategoria = idCategoria;
        this.disponibile = true;
        this.ingredienti = new ArrayList<Object>();
        this.nomeCategoria = nomeCategoria;
    }

    /**
     * Costruttore che carica una pietanza dal database per ID
     * 
     * @param idPietanza ID della pietanza da caricare
     */
    public EntityPietanza(int idPietanza) {
        DBPietanza pietanza = new DBPietanza(idPietanza);

        this.idPietanza = idPietanza;
        this.nome = pietanza.getNome();
        this.prezzo = pietanza.getPrezzo();
        this.idCategoria = pietanza.getIdCategoria();
        this.nomeCategoria = pietanza.getNomeCategoria();
        this.disponibile = pietanza.isDisponibile();
        this.ingredienti = new ArrayList<Object>();
    }

    /**
     * Salva la pietanza nel database
     * 
     * @param idPietanza ID della pietanza (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int idPietanza) {
        DBPietanza p = new DBPietanza(); // DAO

        p.setIdPietanza(idPietanza);
        p.setNome(this.nome);
        p.setPrezzo(this.prezzo);
        p.setIdCategoria(this.idCategoria);
        p.setDisponibile(this.disponibile);

        int i = p.salvaInDB();

        return i;
    }

    /**
     * Recupera tutte le pietanze dal database
     * 
     * @return ArrayList di oggetti DTOPietanza con tutte le pietanze
     */
    public static ArrayList<DTOPietanza> getAllPietanze() {
        ArrayList<DTOPietanza> pietanze = new ArrayList<>();
        DBPietanza dbPietanza = new DBPietanza();
        ArrayList<DBPietanza> listaPietanze = dbPietanza.getTuttePietanze();

        for (DBPietanza p : listaPietanze) {
            DTOPietanza dto = new DTOPietanza();
            dto.setIdPietanza(p.getIdPietanza());
            dto.setNome(p.getNome());
            dto.setPrezzo(p.getPrezzo());
            dto.setIdCategoria(p.getIdCategoria());
            dto.setNomeCategoria(p.getNomeCategoria());
            dto.setDisponibile(p.isDisponibile());
            pietanze.add(dto);
        }

        return pietanze;
    }

    /**
     * Recupera le pietanze per categoria
     * 
     * @param idCategoria ID della categoria per cui filtrare
     * @return ArrayList di oggetti Pietanza della categoria specificata
     */
    public static ArrayList<DTOPietanza> getPietanzePerCategoria(int idCategoria) {
        ArrayList<DTOPietanza> listaPietanze = new ArrayList<>();
        DBPietanza dbPietanza = new DBPietanza();
        List<DBPietanza> listaDBPietanze = dbPietanza.getPietanzePerCategoria(idCategoria);
        for (DBPietanza p : listaDBPietanze) {
            DTOPietanza dto = new DTOPietanza();
            dto.setIdPietanza(p.getIdPietanza());
            dto.setNome(p.getNome());
            dto.setPrezzo(p.getPrezzo());
            dto.setIdCategoria(p.getIdCategoria());
            dto.setNomeCategoria(p.getNomeCategoria());
            dto.setDisponibile(p.isDisponibile());
            listaPietanze.add(dto);
        }
        return listaPietanze;
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

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public List<Object> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(List<Object> ingredienti) {
        this.ingredienti = ingredienti;
    }

    public void aggiungiIngrediente(Object ingrediente) {
        this.ingredienti.add(ingrediente);
    }

    /**
     * Recupera la ricetta associata alla pietanza
     * 
     * @return Ricetta associata o null se non esiste
     */
    public EntityRicetta getRicetta() {
        return EntityRicetta.getRicettaByPietanza(this.idPietanza);
    }

    /**
     * Verifica se la pietanza è disponibile per essere ordinata
     * Controlla prima la variabile disponibile, se false ritorna false
     * Se disponibile=true, verifica la disponibilità degli ingredienti
     * 
     * @return true se la pietanza è disponibile, false altrimenti
     */
    public boolean isDisponibilePerOrdine(int quantitaPietanze) {
        EntityRicetta ricetta = EntityRicetta.getRicettaByPietanza(this.idPietanza);
        if (ricetta == null)
            return false;

        return ricetta.isRicettaEseguibile(quantitaPietanze);
    }

    /**
     * Prenota gli ingredienti necessari per preparare la pietanza
     * 
     * @param quantitaPietanze La quantità di pietanze da preparare
     * @return true se la prenotazione è avvenuta con successo, false altrimenti
     */
    public boolean prenotaIngredienti(int quantitaPietanze) {
        EntityRicetta ricetta = EntityRicetta.getRicettaByPietanza(this.idPietanza);
        if (ricetta == null)
            return false;
        return ricetta.prenotaIngredienti(quantitaPietanze);
    }

    /**
     * Aggiorna lo stato di disponibilità della pietanza nel database
     * 
     * @param disponibile nuovo stato di disponibilità
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaDisponibilita(boolean disponibile) {
        try {
            this.disponibile = disponibile;
            DBPietanza p = new DBPietanza(this.idPietanza);
            p.setDisponibile(disponibile);
            int result = p.aggiornaDisponibilita(disponibile);

            if (result > 0) {
                return true;
            } else {
                System.err.println("Errore nell'aggiornamento della disponibilità di " + this.nome);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento della disponibilità: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fornisce tutte le informazioni necessarie per la preparazione della pietanza
     * 
     * @return DTOIngredientiRicettaPietanza
     */
    public DTOIngredientiRicettaPietanza getIngredientiRicettaPietanza() {
        EntityRicetta ricetta = EntityRicetta.getRicettaByPietanza(this.idPietanza);
        DTOIngredientiRicettaPietanza dto = new DTOIngredientiRicettaPietanza();

        if (ricetta != null) {
            dto.setNome_ricetta(ricetta.getNome());
            dto.setNome_pietanza(this.nome);
            dto.setDescrizione(ricetta.getDescrizione());
            dto.setTempoPreparazione(ricetta.getTempoPreparazione());
            dto.setIstruzioni(ricetta.getIstruzioni());

            ArrayList<DTOIngredienteCuoco> listaIngredienti = new ArrayList<>();
            ArrayList<EntityRicettaIngrediente> ingredientiRicetta = EntityRicettaIngrediente
                    .getIngredientiPerRicetta(ricetta.getIdRicetta());

            for (EntityRicettaIngrediente eri : ingredientiRicetta) {
                EntityIngrediente ingr = new EntityIngrediente(eri.getIdIngrediente());
                DTO.DTOIngredienteCuoco dtoIngr = new DTO.DTOIngredienteCuoco(
                        ingr.getNome(),
                        eri.getQuantita(),
                        ingr.getUnitaMisura());
                listaIngredienti.add(dtoIngr);
            }
            dto.setIngredienti(listaIngredienti);
        }
        return dto;
    }

    /**
     * Recupera l'ID della pietanza a partire dal nome
     * 
     * @param nomePietanza Nome della pietanza da cercare
     * @return ID della pietanza o -1 se non trovata
     */
    public static int getIdPietanzaByNome(String nomePietanza) {
        return DBPietanza.getIdByNome(nomePietanza);
    }

    @Override
    public String toString() {
        return nome + " (" + prezzo + "€)";
    }

}
