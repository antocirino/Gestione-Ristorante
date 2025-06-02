# TODO - Gestione Ristorante

## Modifiche da Apportare

1. **ComposizioneMenu Dao e Entity da modificare**
    - Rivedere la struttura delle classi DAO e Entity per ComposizioneMenu
    - Verificare la corretta implementazione dei metodi di accesso ai dati
    - Controllare la consistenza delle relazioni con altre entità

2. **Classe Ordine da verificare**
    - **Problema prioritario**: Il calcolo del conto non sembra funzionare correttamente
    - Controllare il metodo `calcolaConto()` nella classe `EntityOrdine`
    - Verificare la logica di calcolo del totale includendo:
        - Costo delle pietanze ordinate
        - Costo del coperto per persona
        - Eventuali sconti o maggiorazioni
    - Rivedere i test unitari per assicurarsi che i valori attesi siano corretti
    - Nel test `testCalcolaConto()`, il valore atteso è 17.50 ma sembra non corrispondere al calcolo effettivo
