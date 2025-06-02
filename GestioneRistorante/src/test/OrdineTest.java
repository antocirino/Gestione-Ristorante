package test;

import entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
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
    public void testCostruttore(){
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
    public void testScriviSuDB(){
        int id_ordine = 1;
        int id_tavolo = 1;
        int num_persone = 4;
        String stato = "in_attesa";
        ordine = new EntityOrdine(id_ordine, id_tavolo, num_persone, stato);
        int i = ordine.scriviSuDB();
        assertEquals(i,1);
    }

    @Test
    public void testCostruttoreDaDatabase(){
        int id_ordine = 1;
        ordine = new EntityOrdine(id_ordine);
        System.out.println(ordine);
        assertEquals(id_ordine, ordine.getIdOrdine());

    }

    @Test
    public void testCalcolaConto(){
        int idOrdine = 10;
        EntityOrdine ordine = new EntityOrdine(idOrdine, 1, 5, "in_attesa");
        ordine.scriviSuDB();
        EntityPietanza pietanza = new EntityPietanza(190, "Pizza Margherita", 7.5, 2);
        pietanza.scriviSuDB(190);
        int quantita = 1;
        EntityDettaglioOrdinePietanza dettaglioOrdinePietanza = new EntityDettaglioOrdinePietanza(idOrdine, pietanza, quantita);
        int id_dettaglio_ordine_pietanza = 0;//aggiunge automaticamente l'id
        int val = dettaglioOrdinePietanza.scriviSuDB(id_dettaglio_ordine_pietanza);

        EntityRistorante ristorante = new EntityRistorante(1);
        double costoCoperto = ristorante.getCostoCoperto();
        boolean includiCoperto = true;
        double costo = ordine.calcolaConto(includiCoperto);
        System.out.println("il costo totale è di" + costo);
        double costoTotale= 50.0 + costoCoperto*5;
        assertEquals(costo, 17.50);
    }
}
