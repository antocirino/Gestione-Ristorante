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

    // @Test
    // public void testCalcolaConto() {
    //     int idOrdine = 10;
    //     EntityOrdine ordine = new EntityOrdine(idOrdine, 1, 5, "in_attesa");
    //     ordine.scriviSuDB();
    //     EntityPietanza pietanza1 = new EntityPietanza(190, "Pizza Margherita", 7.5, 2);
    //     EntityPietanza pietanza2 = new EntityPietanza(191, "Spaghetti Carbonara", 8.0, 1);
    //     EntityPietanza pietanza3 = new EntityPietanza(192, "Tiramisu", 5.0, 1);
    //     pietanza1.scriviSuDB(0); // Usa 0 per generare un nuovo ID
    //     pietanza2.scriviSuDB(0);
    //     pietanza3.scriviSuDB(0);
    //     System.out.println("Pietanza inserita con ID: " + pietanza1.getIdPietanza());
    //     System.out.println("Pietanza inserita con ID: " + pietanza2.getIdPietanza());
    //     System.out.println("Pietanza inserita con ID: " + pietanza3.getIdPietanza());
    //     int quantita = 1;
    //     boolean risultatoAggiunta = ordine.aggiungiPietanza(pietanza1, quantita);
    //     boolean risultatoAggiunta2 = ordine.aggiungiPietanza(pietanza2, quantita);
    //     boolean risultatoAggiunta3 = ordine.aggiungiPietanza(pietanza3, quantita);
    //     System.out.println("Risultato aggiunta pietanza 1: " + risultatoAggiunta);
    //     System.out.println("Risultato aggiunta pietanza 2: " + risultatoAggiunta2);
    //     System.out.println("Risultato aggiunta pietanza 3: " + risultatoAggiunta3);
    //     System.out.println("Costo totale dell'ordine dopo aggiunta: " + ordine.getCostoTotale());
    //     EntityRistorante ristorante = new EntityRistorante(1);
    //     double costoCoperto = ristorante.getCostoCoperto();
    //     System.out.println("Costo coperto per persona: " + costoCoperto);
    //     double costoPrevistoSenzaCoperto = pietanza1.getPrezzo() * quantita;
    //     costoPrevistoSenzaCoperto += pietanza2.getPrezzo() * quantita;
    //     costoPrevistoSenzaCoperto += pietanza3.getPrezzo() * quantita;
    //     System.out.println("Costo previsto senza coperto: " + costoPrevistoSenzaCoperto);
    //     double costoPrevistoConCoperto = costoPrevistoSenzaCoperto + (costoCoperto * ordine.getNumPersone());
    //     System.out.println("Costo previsto con coperto: " + costoPrevistoConCoperto);
    //     boolean includiCoperto = true;
    //     double costoEffettivo = ordine.calcolaConto(includiCoperto);
    //     System.out.println("Costo effettivo calcolato: " + costoEffettivo);
    //     assertEquals(33.00, costoEffettivo, 0);
    // }
}
