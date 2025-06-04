package entity;

import java.util.ArrayList;
import java.util.Map;

import database.DBComposizioneMenu;

/**
 * Classe che rappresenta un'associazione tra un menu fisso e una pietanza
 */
public class EntityComposizioneMenu {
    private int idMenu;
    private int idPietanza;
    private EntityPietanza pietanza;
    private EntityMenuFisso menuFisso;

    /**
     * Costruttore vuoto
     */
    public EntityComposizioneMenu() {
    }

    /**
     * Costruttore con attributi principali
     * 
     * @param idMenu     ID del menu fisso
     * @param idPietanza ID della pietanza
     */
    public EntityComposizioneMenu(int idMenu, int idPietanza) {
        DBComposizioneMenu c = new DBComposizioneMenu();
        c.setIdMenu(idMenu);
        c.setIdPietanza(idPietanza);
    }

    /**
     * Costruttore con oggetti MenuFisso e Pietanza
     * 
     * @param menuFisso Menu fisso
     * @param pietanza  Pietanza
     */
    public EntityComposizioneMenu(EntityMenuFisso menuFisso, EntityPietanza pietanza) {
        this.menuFisso = menuFisso;
        this.pietanza = pietanza;
        this.idMenu = menuFisso.getIdMenu();
        this.idPietanza = pietanza.getIdPietanza();
    }

    /**
     * Salva l'associazione menu-pietanza nel database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int scriviSuDB() {
        DBComposizioneMenu c = new DBComposizioneMenu(); // DAO

        c.setIdMenu(this.idMenu);
        c.setIdPietanza(this.idPietanza);

        return c.salvaInDB();
    }

    /**
     * Elimina un'associazione menu-pietanza dal database
     * 
     * @return il numero di righe modificate o -1 in caso di errore
     */
    public int eliminaDaDB() {
        DBComposizioneMenu c = new DBComposizioneMenu();
        c.setIdMenu(this.idMenu);
        c.setIdPietanza(this.idPietanza);
        return c.eliminaDaDB();
    }

    /**
     * Recupera tutte le pietanze associate a un menu
     * 
     * @param idMenu ID del menu
     * @return ArrayList di oggetti Pietanza
     */
    // public static ArrayList<EntityPietanza> getPietanzeByMenu(int idMenu) {
    // DBComposizioneMenu c = new DBComposizioneMenu();
    // return c.getPietanzeByMenu(idMenu);
    // }

    /**
     * Recupera tutti i menu che contengono una specifica pietanza
     * 
     * @param idPietanza ID della pietanza
     * @return ArrayList di ID dei menu
     */
    public static ArrayList<Integer> getMenuByPietanza(int idPietanza) {
        DBComposizioneMenu c = new DBComposizioneMenu();
        return c.getMenuByPietanza(idPietanza);
    }

    /**
     * Recupera la composizione di un menu come mappa
     * 
     * @param idMenu ID del menu
     * @return ArrayList di mappe con gli attributi delle pietanze nel menu
     */
    public static ArrayList<Map<String, Object>> getComposizioneMenuAsMap(int idMenu) {
        DBComposizioneMenu c = new DBComposizioneMenu();
        return c.getComposizioneMenuAsMap(idMenu);
    }

    // Getters e setters
    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getIdPietanza() {
        return idPietanza;
    }

    public void setIdPietanza(int idPietanza) {
        this.idPietanza = idPietanza;
    }

    public EntityPietanza getPietanza() {
        if (this.pietanza == null && this.idPietanza > 0) {
            this.pietanza = new EntityPietanza(this.idPietanza);
        }
        return pietanza;
    }

    public void setPietanza(EntityPietanza pietanza) {
        this.pietanza = pietanza;
        if (pietanza != null) {
            this.idPietanza = pietanza.getIdPietanza();
        }
    }

    public EntityMenuFisso getMenuFisso() {
        if (this.menuFisso == null && this.idMenu > 0) {
            this.menuFisso = new EntityMenuFisso(this.idMenu);
        }
        return menuFisso;
    }

    public void setMenuFisso(EntityMenuFisso menuFisso) {
        this.menuFisso = menuFisso;
        if (menuFisso != null) {
            this.idMenu = menuFisso.getIdMenu();
        }
    }

    @Override
    public String toString() {
        String nomeMenu = (menuFisso != null) ? menuFisso.getNome() : "Menu #" + idMenu;
        String nomePietanza = (pietanza != null) ? pietanza.getNome() : "Pietanza #" + idPietanza;
        return nomeMenu + " - " + nomePietanza;
    }
}
