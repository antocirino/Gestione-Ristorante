=================================================
GESTIONE RISTORANTE - GUIDA PER UTENTI WINDOWS
=================================================

Questa guida spiega come utilizzare gli script forniti per avviare e gestire 
l'applicazione Gestione Ristorante su sistemi Windows.

REQUISITI PRELIMINARI:
---------------------
- Java Development Kit (JDK) 8 o superiore installato
- Variabile d'ambiente JAVA_HOME configurata correttamente
- Variabile PATH che include %JAVA_HOME%\bin

SCRIPT DISPONIBILI:
------------------
1. avvia_applicazione.bat - Avvia l'applicazione Gestione Ristorante
2. compila_progetto.bat - Compila i sorgenti Java del progetto
3. crea_database.bat - Inizializza o resetta il database

ISTRUZIONI D'USO:
----------------
1. Per avviare l'applicazione:
   - Fare doppio clic su "avvia_applicazione.bat"
   - Lo script verificherà se il progetto è compilato. In caso contrario, 
     lo compilerà automaticamente e poi avvierà l'applicazione.

2. Per compilare il progetto manualmente:
   - Fare doppio clic su "compila_progetto.bat"
   - Lo script eliminerà eventuali file compilati precedenti, ricompilerà
     il codice sorgente e copierà le risorse necessarie.

3. Per creare o resettare il database:
   - Fare doppio clic su "crea_database.bat"
   - ATTENZIONE: Questa operazione eliminerà tutti i dati esistenti nel database
   - Lo script compilerà il progetto se necessario e poi inizializzerà il database

RISOLUZIONE PROBLEMI:
--------------------
- Se gli script non funzionano, verificare che Java sia installato correttamente
  eseguendo "java -version" in un prompt dei comandi.
- Verificare che la struttura delle directory del progetto non sia stata modificata.
- In caso di errori durante la compilazione, controllare che tutti i file sorgente
  siano presenti e che le dipendenze (jar) siano nella directory corretta.
- Se l'applicazione non si avvia, controllare che il database sia stato inizializzato
  correttamente eseguendo lo script "crea_database.bat".

Per ulteriori informazioni, consultare la documentazione del progetto.
