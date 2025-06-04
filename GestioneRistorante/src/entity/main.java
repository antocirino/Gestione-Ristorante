package entity;

public class main {
    public static void main(String[] args){
        System.out.println("TEST INGREDIENTE DA DATABASE");

        //TEST INGREDIENTE DA DATABASE
        EntityIngrediente ingrediente = new EntityIngrediente(30);
        System.out.println("Nome: " + ingrediente.getNome());
        System.out.println("Quantità Disponibile: " + ingrediente.getQuantitaDisponibile());
        System.out.println("Unità di Misura: " + ingrediente.getUnitaMisura());
        System.out.println("Soglia di Riordino: " + ingrediente.getSogliaRiordino());
        System.out.println("Da Sotto Scorta: " + ingrediente.daSottoScorta());
        System.out.println("ID Ingrediente: " + ingrediente.getIdIngrediente());
        System.out.println("Test completato con successo!");
            
        
        // TEST SCRITTURA INGREDIENTE SU DATABASE
        System.out.println("TEST INSERIMENTO INGREDIENTE SU DATABASE");
        EntityIngrediente ingrediente1 = new EntityIngrediente();
        ingrediente1.setNome("Farina00");
        ingrediente1.setQuantitaDisponibile(24);
        ingrediente1.setUnitaMisura("kg");
        ingrediente1.setSogliaRiordino(30);
        int val = ingrediente1.scriviSuDB(70); // 0 per inserimento nuovo ingrediente
        if (val == 1) {
            System.out.println("Ingrediente salvato con successo nel database!");
        } else {
            System.out.println("Errore durante il salvataggio dell'ingrediente nel database.");
        }

        //TEST INGREDIENTI CON SCORTE BASSE DA DATABASE
        System.out.println("TEST INGREDIENTI ESURITI DA DATABASE");
        EntityIngrediente ingrediente2 = new EntityIngrediente();
        for (EntityIngrediente ing : ingrediente2.getIngredientiEsauriti()) {
            System.out.println("ID: " + ing.getIdIngrediente() + ", Nome: " + ing.getNome() + ", Quantità Disponibile: " + ing.getQuantitaDisponibile() + ", Unità di Misura: " + ing.getUnitaMisura() + ", Soglia di Riordino: " + ing.getSogliaRiordino());
        }
        System.out.println("Test completato con successo!");
 
    }
}    

