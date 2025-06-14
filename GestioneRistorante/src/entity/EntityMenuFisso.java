package entity;

import java.util.ArrayList;
import java.util.Map;

import DTO.DTOMenuFisso;
import DTO.DTOPietanza;
import database.DBMenuFisso;

/**
 * Classe che rappresenta un menu fisso del ristorante
 */
public class EntityMenuFisso {
    private int idMenu;
    private String nome;
    private double prezzo;
    private String descrizione;
    private ArrayList<EntityPietanza> pietanze;

    /**
     * Costruttore vuoto
     */
    public EntityMenuFisso() {
        this.pietanze = new ArrayList<>();
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param nome        Nome del menu fisso
     * @param prezzo      Prezzo del menu fisso
     * @param descrizione Descrizione del menu fisso
     */
    public EntityMenuFisso(String nome, double prezzo, String descrizione) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.pietanze = new ArrayList<>();
    }

    /**
     * Costruttore che carica un menu fisso dal database per ID
     * 
     * @param idMenu ID del menu fisso da caricare
     */
    public EntityMenuFisso(int idMenu) {
        DBMenuFisso menu = new DBMenuFisso(idMenu);

        this.idMenu = idMenu;
        this.nome = menu.getNome();
        this.prezzo = menu.getPrezzo();
        this.descrizione = menu.getDescrizione();
        this.pietanze = menu.getPietanze();
    }

    /**
     * Salva il menu fisso nel database
     * 
     * @param idMenu ID del menu fisso (0 per auto-incremento)
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB(int idMenu) {
        DBMenuFisso m = new DBMenuFisso(); // DAO

        m.setNome(this.nome);
        m.setPrezzo(this.prezzo);
        m.setDescrizione(this.descrizione);

        int result = m.salvaInDB(idMenu);

        // Se il salvataggio è andato a buon fine e abbiamo pietanze da aggiungere
        if (result > 0 && pietanze != null && !pietanze.isEmpty()) {
            // Assumiamo che l'ID sia stato impostato nella tabella menu_fisso
            // e che questo ID sia stato restituito
            int nuovoIdMenu = idMenu == 0 ? result : idMenu;
            m.setIdMenu(nuovoIdMenu);

            // Aggiungiamo le pietanze al menu
            for (EntityPietanza p : pietanze) {
                m.aggiungiPietanza(p.getIdPietanza());
            }
        }

        return result;
    }

    /**
     * Aggiunge una pietanza al menu fisso
     * 
     * @param pietanza La pietanza da aggiungere
     */
    public void aggiungiPietanza(EntityPietanza pietanza) {
        // Aggiungiamo alla lista locale
        this.pietanze.add(pietanza);

        // Se abbiamo un ID valido, aggiorniamo anche il database
        if (this.idMenu > 0) {
            DBMenuFisso m = new DBMenuFisso(this.idMenu);
            m.aggiungiPietanza(pietanza.getIdPietanza());
        }
    }

    /**
     * Rimuove una pietanza dal menu fisso
     * 
     * @param pietanza La pietanza da rimuovere
     * @return true se la pietanza è stata rimossa, false altrimenti
     */
    public boolean rimuoviPietanza(EntityPietanza pietanza) {
        // Rimuoviamo dalla lista locale
        boolean rimossa = this.pietanze.remove(pietanza);

        // Se abbiamo un ID valido e la pietanza è stata rimossa, aggiorniamo anche il
        // database
        if (rimossa && this.idMenu > 0) {
            DBMenuFisso m = new DBMenuFisso(this.idMenu);
            m.rimuoviPietanza(pietanza.getIdPietanza());
        }

        return rimossa;
    }

    /**
     * Recupera tutti i menu fissi dal database
     * 
     * @return ArrayList di oggetti MenuFisso
     */
    public static ArrayList<DTOMenuFisso> getTuttiMenuFissi() {

        ArrayList<DTOMenuFisso> listaMenu = new ArrayList<>();
        DBMenuFisso menu = new DBMenuFisso();
        ArrayList<DBMenuFisso> menuFissi = menu.getTuttiMenuFissi();

        for (DBMenuFisso m : menuFissi) {
            DTOMenuFisso dto = new DTOMenuFisso();
            dto.setIdMenu(m.getIdMenu());
            dto.setNome(m.getNome());
            dto.setPrezzo(m.getPrezzo());
            dto.setDescrizione(m.getDescrizione());

            dto.setPietanze(new ArrayList<>());
            // Aggiungiamo le pietanze del menu
            for (EntityPietanza p : m.getPietanze()) {
                DTOPietanza dtoPietanza = new DTOPietanza();
                dtoPietanza.setIdPietanza(p.getIdPietanza());
                dtoPietanza.setNome(p.getNome());
                dtoPietanza.setPrezzo(p.getPrezzo());
                dtoPietanza.setIdCategoria(p.getIdCategoria());
                dtoPietanza.setNomeCategoria(p.getNomeCategoria());
                dtoPietanza.setDisponibile(p.isDisponibile());
                dto.getPietanze().add(dtoPietanza);
            }
            listaMenu.add(dto);
        }

        return listaMenu;
    }

    /**
     * Recupera tutti i menu fissi come mappa
     * 
     * @return Mappa con chiave l'ID del menu e valore un oggetto con le
     *         informazioni del menu
     */
    public static Map<Integer, Map<String, Object>> getMenuFissiMap() {
        DBMenuFisso menu = new DBMenuFisso();
        return menu.getMenuFissiMap();
    }

    // Getters e setters
    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public ArrayList<EntityPietanza> getPietanze() {
        return pietanze;
    }

    public void setPietanze(ArrayList<EntityPietanza> pietanze) {
        this.pietanze = pietanze;
    }

    @Override
    public String toString() {
        return nome + " (" + prezzo + "€)";
    }
}
