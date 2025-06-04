package test;

import control.Controller;

import org.junit.Test;

public class ControllerTest {

    @Test
    public void testControllerFunctionality() {
        var x = Controller.getCategoriePietanze();
        System.out.println("x: " + x);
    }
}
