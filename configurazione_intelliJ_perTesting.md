# Guida per Gestire File JAR in IntelliJ IDEA

Questa guida spiega come aggiungere correttamente file JAR esterni (es. driver MySQL) al progetto in IntelliJ IDEA, in modo che vengano inclusi automaticamente nel classpath durante la compilazione ed esecuzione.

---

## Passaggi per aggiungere file JAR come dipendenze nel progetto

1. **Prepara la cartella dei JAR**

   - Crea una cartella nel tuo progetto per contenere i file JAR, ad esempio `jars` o `lib`.
   - Copia al suo interno i file JAR necessari, ad esempio `mysql-connector-j-8.0.33.jar`.

2. **Aggiungi il JAR come dipendenza nel modulo**

   - Apri IntelliJ IDEA.
   - Vai su **File > Project Structure...** (oppure usa la scorciatoia `Cmd + ;` su Mac o `Ctrl + Alt + Shift + S` su Windows/Linux).
   - Seleziona **Modules** nella barra laterale.
   - Seleziona il modulo del tuo progetto (di solito ha lo stesso nome del progetto).
   - Vai alla scheda **Dependencies**.
   - Clicca sul pulsante **+** in alto a destra e scegli **JARs or directories...**.
   - Naviga nella cartella `jars` (o `lib`) e seleziona il file JAR desiderato.
   - Conferma l'aggiunta.
   - Assicurati che la dipendenza sia impostata come **Compile** (default).

3. **Applica e salva**

   - Clicca su **Apply** e poi su **OK** per chiudere la finestra di configurazione.
   
4. **Verifica**

   - Ora IntelliJ includerà automaticamente il file JAR nel classpath per esecuzione, compilazione e test.
   - Non sarà più necessario specificare manualmente il classpath da terminale.

---
