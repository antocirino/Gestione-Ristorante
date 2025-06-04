package test;

import entity.EntityDettaglioOrdinePietanza;
import entity.EntityMenuFisso;
import entity.EntityOrdine;
import entity.EntityPietanza;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DettaglioOrdinePietanzaTest {

        private EntityDettaglioOrdinePietanza dettaglioOrdinePietanza;

        @Before
        public void setUp() throws Exception {
            dettaglioOrdinePietanza = new EntityDettaglioOrdinePietanza();
        }

        @After
        public void tearDown() throws Exception {
           dettaglioOrdinePietanza = null;
        }

        @Test
        public void testCostruttore(){
            int id_dettaglio_ordine_pietanza = 1;
            dettaglioOrdinePietanza = new EntityDettaglioOrdinePietanza(id_dettaglio_ordine_pietanza);
            assertEquals(1, dettaglioOrdinePietanza.getIdDettaglio());
            System.out.println(dettaglioOrdinePietanza);
        }

        @Test
    public void testScriviSuDB(){
            int idOrdine = 9;
            EntityOrdine ordine = new EntityOrdine(idOrdine, 1, 5, "in_attesa");
            ordine.scriviSuDB();
            EntityPietanza pietanza = new EntityPietanza(160, "Pizza Margherita", 7.5, 2);
            pietanza.scriviSuDB(160);
            int quantita = 3;
            dettaglioOrdinePietanza = new EntityDettaglioOrdinePietanza(idOrdine, pietanza, quantita);
            int id_dettaglio_ordine_pietanza = 0;//aggiunge automaticamente l'id
            int val = dettaglioOrdinePietanza.scriviSuDB(id_dettaglio_ordine_pietanza);
            assertEquals(val,0);//0 tutto è andato a buon fine
        }
}
