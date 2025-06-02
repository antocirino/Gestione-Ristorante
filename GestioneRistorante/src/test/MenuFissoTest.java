package test;

import entity.EntityMenuFisso;
import entity.EntityPietanza;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MenuFissoTest {

    private EntityMenuFisso menuFisso;

    @Before
    public void setUp() throws Exception {
        menuFisso = new EntityMenuFisso();
    }

    @After
    public void tearDown() throws Exception {
        menuFisso = null;
    }

    @Test
    public void testCostruttore(){
        int id_menu = 1;
        menuFisso = new EntityMenuFisso(id_menu);
        assertEquals(1, menuFisso.getIdMenu());
        System.out.println(menuFisso);
    }

    @Test
    public void testScriviSuDB(){
        int id_menuFisso = 9;
        String nome = "Menu di prova";
        double prezzo = 50.0;
        String descrizione = "per palati fini";
        menuFisso = new EntityMenuFisso(nome, prezzo, descrizione);
        int i = menuFisso.scriviSuDB(id_menuFisso);
        assertEquals(i,1);
    }

    @Test
    public void testAggiungiPietanza(){
        int id_pietanza = 1;
        int id_menuFisso = 5;
        EntityPietanza pietanza = new EntityPietanza(id_pietanza);
        menuFisso = new EntityMenuFisso(id_menuFisso);
        menuFisso.aggiungiPietanza(pietanza);

        assertTrue(menuFisso.getPietanze().contains(pietanza));
        System.out.println(menuFisso);
    }


}
