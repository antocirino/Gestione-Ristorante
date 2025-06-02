package test;

import entity.EntityPietanza;
import entity.EntityRistorante;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PietanzaTest {

    private EntityPietanza pietanza;

    @Before
    public void setUp() throws Exception {
        pietanza = new EntityPietanza(1, "Pizza Margherita", 7.5, 2);

    }

    @After
    public void tearDown() throws Exception {
        pietanza = null;
    }

    @Test
    public void testCostruttoreEGetter() {
        assertEquals(1, pietanza.getIdPietanza());
        assertEquals("Pizza Margherita", pietanza.getNome());
        assertEquals(7.5, pietanza.getPrezzo(), 0.001);
        assertEquals(2, pietanza.getIdCategoria());
        assertTrue(pietanza.isDisponibile());
        assertEquals(0, pietanza.getIngredienti().size());
    }

    @Test
    public void testSetters() {
        pietanza.setNome("Lasagna");
        pietanza.setPrezzo(8.0);
        pietanza.setIdCategoria(3);
        pietanza.setDisponibile(false);
        pietanza.setIngredienti(new ArrayList<>());

        assertEquals("Lasagna", pietanza.getNome());
        assertEquals(8.0, pietanza.getPrezzo(), 0.001);
        assertEquals(3, pietanza.getIdCategoria());
        assertFalse(pietanza.isDisponibile());
    }

    @Test
    public void testAggiungiIngrediente() {
        Object fintoIngrediente = new Object();
        pietanza.aggiungiIngrediente(fintoIngrediente);
        assertEquals(1, pietanza.getIngredienti().size());
        assertSame(fintoIngrediente, pietanza.getIngredienti().get(0));
    }

    @Test
    public void testToString() {
        String s = pietanza.toString();
        assertTrue(s.contains("Pizza Margherita"));
        assertTrue(s.contains("7.5"));
    }

    @Test
    public void testAggiornaDisponibilità(){
        pietanza = new EntityPietanza(1);
        pietanza.aggiornaDisponibilita(false);
        System.out.println(pietanza);
        assertEquals(pietanza.isDisponibile(),false);
    }

    @Test
    public void prenotaIngredienti(){
        pietanza = new EntityPietanza(2);
        System.out.println(pietanza);
        boolean verifia = pietanza.prenotaIngredienti(1);
        assertTrue(verifia);
    }

}





