package test;

import entity.EntityComposizioneMenu;
import entity.EntityPietanza;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ComposizioneMenuTest {
    private EntityComposizioneMenu composizionemenu;

    @Before
    public void setUp() throws Exception {
        composizionemenu = new EntityComposizioneMenu();
    }

    @After
    public void tearDown() throws Exception {
        composizionemenu = null;
    }

    @Test
    public void testCostruttore(){
        composizionemenu = new EntityComposizioneMenu(7, 1);
        assertEquals(7, composizionemenu.getIdMenu());
        assertEquals(1, composizionemenu.getIdPietanza());
        System.out.println(composizionemenu);
    }

    @Test
    public void testScrivisuDBelElementoNonEsistenteInDB(){
        composizionemenu = new EntityComposizioneMenu(1, 7);
        int i = composizionemenu.scriviSuDB();
        assertEquals(i,1);
    }

    @Test
    public void testScrivisuDBElementoEsistenteInDB(){
        composizionemenu = new EntityComposizioneMenu(1, 1);
        int i = composizionemenu.scriviSuDB();
        assertEquals(i,0);
    }

    @Test
    public void testScrividuDBConErorri(){
        composizionemenu = new EntityComposizioneMenu(100, 1);
        int i = composizionemenu.scriviSuDB();
        assertEquals(i,-1);
    }

    @Test
    public void testEliminaElementoDaDB(){
        composizionemenu = new EntityComposizioneMenu(1, 1);
        int i = composizionemenu.eliminaDaDB();
        assertEquals(i,1);
    }

    @Test
    public void testEliminaElementoDaDBConErrore(){
        composizionemenu = new EntityComposizioneMenu(100, 1);
        int i = composizionemenu.eliminaDaDB();
        assertEquals(i,0);
    }

    @Test
    public void testGetPietanzaDaMenu(){
        int idMenu = 1;
        ArrayList<EntityPietanza> array = EntityComposizioneMenu.getPietanzeByMenu(idMenu);
        System.out.println(array);
        int quantitaPietanzePresentiNelMenuConIdMenu = 5;
        assertEquals(quantitaPietanzePresentiNelMenuConIdMenu, array.size());
    }

    @Test
    public void testGetMenuDaPietanza(){
        int idPietanza = 1;
        ArrayList<Integer> array = EntityComposizioneMenu.getMenuByPietanza(idPietanza);
        System.out.println(array);
        assertEquals(0, array.size());
    }

    @Test
    public void testGetComposizioneMenu(){
        int idMenu = 1;
        ArrayList<Map<String, Object>> array = EntityComposizioneMenu.getComposizioneMenuAsMap(idMenu);
        System.out.println(array);
        int quantitaPietanzePresentiNelMenuConIdMenu = 5;
        assertEquals(quantitaPietanzePresentiNelMenuConIdMenu, array.size());
    }


}
