# Guida al Testing e alla Configurazione dell'Ambiente di Sviluppo

Questa guida completa spiega come compilare ed eseguire i test delle classi nel progetto **Gestione-Ristorante**, oltre a fornire istruzioni per configurare correttamente l'ambiente di sviluppo IntelliJ IDEA.

---

## 1. Compilazione del Progetto

Dalla cartella principale del progetto (**Gestione-Ristorante**), esegui il comando:

```sh
chmod +x compile.sh
./compile.sh
```

Segui le istruzioni che appariranno a terminale per completare la compilazione.

---

## 2. Esecuzione dei Test da Terminale

Dopo la compilazione, entra nella cartella `GestioneRistorante`:

```sh
cd GestioneRistorante
```

Esegui il programma principale con il comando:

```sh
java -cp bin:bin/jars/mysql-connector-j-8.0.33.jar entity.main
```

**Note importanti:**

- Assicurati che il database sia avviato e accessibile prima di eseguire i test.
- Se necessario, imposta le variabili d'ambiente `DATABASE_USER` e `DATABASE_PASSWORD` con le credenziali corrette del database.

---

## 3. Configurazione di IntelliJ IDEA

### 3.1 Gestione File JAR in IntelliJ IDEA

Questa sezione spiega come aggiungere correttamente file JAR esterni (es. driver MySQL) al progetto in IntelliJ IDEA, in modo che vengano inclusi automaticamente nel classpath durante la compilazione ed esecuzione.

#### Passaggi per aggiungere file JAR come dipendenze nel progetto

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

### 3.2 Configurazione per l'esecuzione dei test in IntelliJ IDEA

Per eseguire i test all'interno dell'IDE:

1. **Configura una nuova Run Configuration**

   - Clicca su **Run > Edit Configurations...**
   - Clicca sul **+** e seleziona **Application**
   - Compila i seguenti campi:
     - **Name**: GestioneRistorante Test
     - **Main class**: entity.main
     - **Working directory**: percorso alla cartella principale del progetto
   - In **VM options**, aggiungi le variabili d'ambiente necessarie (opzionale):
     ```
     -DDATABASE_USER=root -DDATABASE_PASSWORD=password
     ```
   - Nella sezione **Dependencies**, assicurati che tutti i JAR necessari siano inclusi

2. **Esecuzione del test**
   - Seleziona la configurazione appena creata dal menu a tendina in alto a destra
   - Clicca sul pulsante di esecuzione (▶️) o premi Shift+F10

---

## 4. Suggerimenti per il Debug

- Utilizza i breakpoint di IntelliJ IDEA per fermare l'esecuzione in punti specifici del codice
- Controlla i log di Docker per eventuali problemi relativi al database:
  ```sh
  ./docker-utils.sh db-logs
  ```
- Per problemi di connessione al database, verifica che le credenziali e l'URL di connessione siano corretti nel file `.env`
