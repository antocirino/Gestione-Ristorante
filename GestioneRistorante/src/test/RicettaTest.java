package test;

import entity.EntityRicetta;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RicettaTest {

    EntityRicetta ricetta;
    @Before
    public void setUp() throws Exception {
        ricetta = new EntityRicetta();
    }

    @After
    public void tearDown() throws Exception {
        ricetta = null;
    }

    @Test
    public void testCostruttore() {
        int idRicetta = 1;
        ricetta = new EntityRicetta(idRicetta);
        assertEquals(idRicetta, ricetta.getIdRicetta());
        System.out.println(ricetta);

    }

    @Test
    public void testScriviSuDB(){
        int id_ricetta = 60;
        ricetta = new EntityRicetta("ricetta_di_prova", "molto buona", 2, 20, "molto facile");
        int i = ricetta.scriviSuDB(id_ricetta);
        assertEquals(i,1);
    }

    @Test
    public void testAggiungiIngrediente(){
        int idRicetta = 1;

        ricetta = new EntityRicetta(idRicetta);
        int id_ingredinte = 2;
        String nomeIngrediente = "farina";
        String unitaMisura = "kg";
        float quantita = 2;
        ricetta.aggiungiIngrediente(id_ingredinte, nomeIngrediente, unitaMisura, quantita);

    }

    @Test
    public void testElimaIngredinte(){
        int idRicetta = 1;
        ricetta = new EntityRicetta(idRicetta);
        int id_ingredinte = 2;
        boolean valore = ricetta.rimuoviIngrediente(id_ingredinte);
        assertTrue(valore);
    }

    @Test
    public void testGetTutteRicette() {
        ArrayList<EntityRicetta> ricette = EntityRicetta.getTutteRicette();
        assertNotEquals(0, ricette.size());
        System.out.println(ricette);
    }


}
