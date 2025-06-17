package test;

import entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrdineTest {

    private EntityOrdine ordine;

    @Before
    public void setUp() throws Exception {
        ordine = new EntityOrdine();
    }

    @After
    public void tearDown() throws Exception {
        ordine = null;
    }

    @Test
    public void testCostruttore() {
        int id_tavolo = 1;
        int num_persone = 4;
        String stato = "in_attesa";
        ordine = new EntityOrdine(id_tavolo, num_persone, stato);
        assertEquals(id_tavolo, ordine.getIdTavolo());
        assertEquals(num_persone, ordine.getNumPersone());
        assertEquals(stato, ordine.getStato());
        System.out.println(ordine);
    }

    @Test
    public void testScriviSuDB() {
        int id_ordine = 1;
        int id_tavolo = 1;
        int num_persone = 4;
        String stato = "in_attesa";
        ordine = new EntityOrdine(id_ordine, id_tavolo, num_persone, stato);
        System.out.println("creato ordine con ID: " + id_ordine + " tavolo: " + id_tavolo + " persone: " + num_persone
                + " stato: " + stato);
        int i = ordine.scriviSuDB();
        System.out.println("Risultato scriviSuDB: " + i);
        assertEquals("L'ID dell'ordine deve essere aggiornato", i, ordine.getIdOrdine());
    }

    @Test
    public void testCostruttoreDaDatabase() {
        int id_ordine = 1;
        ordine = new EntityOrdine(id_ordine);
        System.out.println(ordine);
        assertEquals(id_ordine, ordine.getIdOrdine());

    }

    @Test
    public void testAggiornaStato() {
        int id_ordine = 100; // Supponiamo che l'ordine con ID 100 non esista nel database
        String nuovo_stato = "in_corso";
        ordine = new EntityOrdine(id_ordine);
        int i = ordine.aggiornaStato(nuovo_stato);
        assertEquals(i, 0);
        assertEquals(nuovo_stato, ordine.getStato());
    }
}
