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

## Configurazione per Linux

Su Linux, l'integrazione è più semplice:

1. **Permettere connessioni X11 locali**

   ```bash
   xhost +local:docker
   ```

2. **Modifica del docker-compose.yml** (già configurato)

   Assicurarsi che nel servizio `app` ci sia:

   ```yaml
   volumes:
     - /tmp/.X11-unix:/tmp/.X11-unix
   environment:
     - DISPLAY=${DISPLAY}
   ```

3. **Avviare i container**

   ```bash
   ./docker-utils.sh start
   ```

## Configurazione per Windows

Su Windows, è necessario usare un server X11:

1. **Installare VcXsrv Windows X Server**

   Scaricare e installare da: https://sourceforge.net/projects/vcxsrv/

2. **Avviare VcXsrv** con le seguenti impostazioni:

   - Multiple windows
   - Display number: 0
   - Start no client
   - **Importante**: Disabilitare "Native opengl" e abilitare "Disable access control"

3. **Trovare l'indirizzo IP**

   ```bash
   ipconfig
   ```

4. **Impostare la variabile DISPLAY nel .env**

   ```
   DISPLAY=your-windows-ip:0.0
   ```

5. **Avviare i container**

   ```bash
   ./docker-utils.sh start
   ```

## Risoluzione dei problemi

Se l'interfaccia grafica non viene visualizzata:

1. **Verificare che X11 sia configurato correttamente**

   - Su macOS: XQuartz in esecuzione e `xhost +` attivato
   - Su Linux: `xhost +local:docker` eseguito
   - Su Windows: VcXsrv in esecuzione con "Disable access control" attivo

2. **Controllare i log dell'applicazione**

   ```bash
   ./docker-utils.sh logs
   ```

3. **Provare l'esecuzione in modalità headless**

   Se non è possibile visualizzare l'interfaccia grafica, è possibile modificare l'applicazione per supportare una modalità headless (senza interfaccia grafica, ad esempio con API REST o interfaccia a riga di comando).
