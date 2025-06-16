# Esecuzione dell'Interfaccia Grafica in Docker

Questo documento descrive come eseguire correttamente l'interfaccia grafica Java Swing all'interno di Docker, in base al sistema operativo utilizzato.

## Prerequisiti

- Docker e Docker Compose installati
- Progetto compilato con `./compile.sh`

## Configurazione per macOS

Su macOS, è necessario configurare X11 per permettere alle applicazioni Docker di visualizzare finestre grafiche:

1. **Installare XQuartz**

   ```bash
   brew install --cask xquartz
   ```

2. **Avvio rapido (ricostruendo da zero)**

   Per un avvio completo che ricostruisce i container (attenzione: questo elimina i dati esistenti):

   ```bash
   ./avvia-app-rebuild.sh
   ```

3. **Avvio normale (mantiene i dati esistenti)**

   Per avviare i container esistenti senza ricostruirli:

   ```bash
   ./docker-utils.sh start
   ```

4. **Solo configurazione X11** (se hai problemi con la GUI)

   ```bash
   ./setup-x11-macos.sh
   ./docker-utils.sh restart
   ```

## Configurazione per Windows

Su Windows, è necessario usare un server X11:

1. **Installare VcXsrv Windows X Server**

   Scaricare e installare da: https://sourceforge.net/projects/vcxsrv/

2. **Avviare VcXsrv** con le seguenti impostazioni:

   - Multiple windows
   - Display number: 0
   - Start no client
   - Disabilita "Native opengl"
   - Abilita "Disable access control" (per permettere la connessione dal container)
   - Lascia VcXsrv in esecuzione durante tutto l'utilizzo dell'applicazione.

3. **Opzioni di avvio**
   - seguire i passi 2 e 3 della sezione macOS per avviare l'applicazione.

## Risoluzione dei problemi

Se l'interfaccia grafica non viene visualizzata:

1. **Verificare che X11 sia configurato correttamente**

   - XQuartz in esecuzione e `xhost +` attivato

2. **Controllare i log dell'applicazione**

   ```bash
   ./docker-utils.sh logs
   ```

3. **Provare l'esecuzione in modalità headless**

   Se non è possibile visualizzare l'interfaccia grafica, è possibile modificare l'applicazione per supportare una modalità headless (senza interfaccia grafica, ad esempio con API REST o interfaccia a riga di comando).

## Risoluzione dei problemi Windows specifici

Se l'interfaccia grafica non viene visualizzata su Windows:

1. **Verificare le impostazioni di XLaunch**

   - Assicurarsi che sia stato avviato con "Disable access control"
   - Provare a riavviare XLaunch

2. **Verificare il firewall di Windows**

   - Assicurarsi che il firewall non blocchi le connessioni a VcXsrv
   - Aggiungere VcXsrv alle eccezioni del firewall

3. **Problemi di Java**

   - Verificare che la variabile JAVA_HOME sia impostata correttamente
   - Provare con `java -version` per assicurarsi che Java sia installato correttamente

4. **Provare con il debug**
   - Modificare lo script aggiungendo `-Dawt.debug=true` alle opzioni Java
