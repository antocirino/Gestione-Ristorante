package test;

import control.Controller;
import entity.EntityOrdine;


import org.junit.Test;

public class ControllerTest {

    @Test
    public void testControllerFunctionality() {
        var x = Controller.getCategoriePietanze();
        System.out.println("x: " + x);
    }

    @Test
    public void testCreaOrdine(){
        EntityOrdine ordine = Controller.CreaOrdine(1, 4, 1, "In attesa");
        
    }
}
