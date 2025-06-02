package test;

import entity.EntityTavolo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class TavoloTest {

    private EntityTavolo tavolo;

    @Before
    public void setUp() throws Exception {
        EntityTavolo tavolo  = new EntityTavolo();
    }

    @After
    public void tearDown() throws Exception {
        tavolo = null;
    }

    @Test
    public void testInsertAProduct() {
    System.out.println("ciao");
    assertNull(tavolo);
    }

    @Test
    public void testCostruttoreConParametri () {
        tavolo = new EntityTavolo(1, 3, "libero", 1);
        assertEquals(1, tavolo.getIdTavolo());
        assertEquals(3, tavolo.getMaxPosti());
        assertEquals("libero", tavolo.getStato());
        assertEquals(1, tavolo.getIdRistorante());
    }

    @Test
    public void testPrelevaDaDatabase () {
        tavolo = new EntityTavolo(15);
        System.out.println(tavolo);

        assertEquals(15, tavolo.getIdTavolo());
    }

    @Test
    public void testSalvaInDatabaseDuplicato () {
        tavolo = new EntityTavolo(15, 3, "libero", 1);
        int i = tavolo.scriviSuDB(15);
        assertEquals(i,-1);
    }

    @Test
    public void testSalvaInDatabase () {
        //dovrebbe ritornare 1 se l'inserimento è andato a buon fine, altrimenti -1
        tavolo = new EntityTavolo(0, 10, "libero", 1);
        int i = tavolo.scriviSuDB(0);
        System.out.println(i);
        assertEquals(i,1);
    }

    @Test
    public void testAggiornaStato(){
        tavolo = new EntityTavolo(2);
        System.out.println(tavolo);
        tavolo.aggiornaStato("occupato");
        System.out.println(tavolo);
        assertEquals("occupato", tavolo.getStato());

        tavolo = new EntityTavolo(2);
        System.out.println(tavolo);
        assertEquals("occupato", tavolo.getStato());

    }






}

