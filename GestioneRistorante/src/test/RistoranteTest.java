package test;

import entity.EntityRistorante;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RistoranteTest {
    private EntityRistorante ristorante;

    @Before
    public void setUp() throws Exception {
        ristorante  = new EntityRistorante();
    }

    @After
    public void tearDown() throws Exception {
        ristorante = null;
    }

    @Test
    public void testCostruttoreConParametri () {
        int numTavoli = 100;
        String nome = "ristorante";
        double costoCoperto = 3;
        int idRistorante = 1;

        ristorante = new EntityRistorante(idRistorante, nome, numTavoli, costoCoperto);
        assertEquals(idRistorante, ristorante.getIdRistorante());
        assertEquals(nome, ristorante.getNome());
        assertEquals(numTavoli, ristorante.getNumeroTavoli());
        assertEquals(costoCoperto, ristorante.getCostoCoperto(), 0.0001);

    }

    @Test
    public void testCaricaDaDatabase(){
        ristorante = new EntityRistorante(1);
        System.out.println(ristorante);
        assertEquals(1, ristorante.getIdRistorante());
    }

    @Test
    public void testSalvaModificheInDatabase(){
        ristorante = new EntityRistorante(1, "ristorante", 100, 3);
        int i = ristorante.scriviSuDB(1);
        assertEquals(i,1);//MODIFICHE EFFETTUATE CON SUCCESSO
    }

    @Test
    public void testAggiungiRistoranteInDatabase(){
        ristorante = new EntityRistorante("Da Zia Giuseppina", 50, 2);
        int i = ristorante.scriviSuDB(0);
        assertEquals(i,0);//RISTORNATE AGGIUNTO CON SUCCESSO
    }

    @Test
    public void testEliminaRistoranteDaDatabase(){
        ristorante = new EntityRistorante(1);
        int i = ristorante.eliminaDaDB();
        assertEquals(i,1);//RISTORNATE ELIMINATE CON SUCCESSO
    } /// se si elimina il ristorante per sempio 1 e si aggiunge un nuovo ristrante automaticamente non viene più riassegnato il 1

    @Test
    public void testInizializzaTavoli(){
        ristorante = new EntityRistorante(1);
        int[] postiPerTavolo = new int[ristorante.getNumeroTavoli()];
        boolean valore  = ristorante.inizializzaTavoli(postiPerTavolo);
        assertTrue(valore);//vero se tutto è andato a buon fine
    }

}
