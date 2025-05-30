# 🚀 Guida Rapida al Sistema di Gestione Ristorante

## Requisiti

- **macOS**:

  - Docker Desktop
  - XQuartz (per l'interfaccia grafica)

- **Linux**:

  - Docker Engine
  - Docker Compose

- **Windows**:
  - Docker Desktop
  - VcXsrv Windows X Server (per l'interfaccia grafica)

## Installazione e Avvio Rapido su macOS

1. Clona il repository:

   ```
   git clone https://github.com/GGCIRILLO/Gestione-Ristorante.git
   cd Gestione-Ristorante
   ```

2. **Metodo 1**: Avvio rapido con ricostruzione completa (per prima installazione):

   ```zsh
   chmod +x avvia-app-rebuild.sh
   ./avvia-app-rebuild.sh
   ```

   Lo script si occuperà automaticamente di:

   - Configurare X11 per la GUI
   - Fermare container esistenti
   - Ricostruire completamente i container (elimina dati esistenti)
   - Mostrare i log dell'applicazione

3. **Metodo 2**: Avvio con controlli dell'ambiente (consigliato per nuove installazioni):

   ```zsh
   chmod +x start-macos.sh
   ./start-macos.sh
   ```

   Lo script si occuperà automaticamente di:

   - Controllare che Docker e XQuartz siano installati
   - Compilare l'applicazione Java se necessario
   - Configurare X11
   - Avviare i container Docker

## Avvio Manuale (per utenti avanzati)

1. Compila l'applicazione (solo la prima volta o dopo modifiche):

   ```zsh
   chmod +x compile.sh
   ./compile.sh
   ```

2. Configura X11 (solo per macOS, necessario dopo ogni riavvio di XQuartz):

   ```zsh
   chmod +x setup-x11-macos.sh
   ./setup-x11-macos.sh
   ```

3. Gestione dei container:

   ```zsh
   # Avvio normale (mantiene i dati)
   ./docker-utils.sh start

   # Riavvio dei container
   ./docker-utils.sh restart

   # Ricostruzione completa (elimina i dati)
   ./docker-utils.sh rebuild
   ```

## Utilizzo degli Strumenti

Lo script `docker-utils.sh` offre diversi comandi utili:

- **Avvio containers**: `./docker-utils.sh start`
- **Arresto containers**: `./docker-utils.sh stop`
- **Riavvio containers**: `./docker-utils.sh restart`
- **Status containers**: `./docker-utils.sh status`
- **Log applicazione**: `./docker-utils.sh logs`
- **Log database**: `./docker-utils.sh db-logs`
- **Console MySQL**: `./docker-utils.sh db-console`

## Accesso a phpMyAdmin

- URL: http://localhost:8081
- Username: root
- Password: password

## Variabili d'ambiente

Il progetto utilizza le seguenti variabili d'ambiente definite nel file `.env`:

### Database MySQL

- `MYSQL_ROOT_PASSWORD`: Password dell'utente root del database
- `MYSQL_DATABASE`: Nome del database
- `MYSQL_USER`: Nome dell'utente aggiuntivo (non utilizzato nella connessione attuale)
- `MYSQL_PASSWORD`: Password dell'utente aggiuntivo

### Connessione Java al database

- `DATABASE_URL`: URL JDBC per la connessione al database
- `DATABASE_USER`: Utente per la connessione al database (impostato su root)
- `DATABASE_PASSWORD`: Password per la connessione al database

### Porte

- `PHPMYADMIN_PORT`: Porta per accedere a phpMyAdmin
- `JAVA_APP_PORT`: Porta esposta per l'applicazione Java

## Risoluzione dei Problemi

1. **Interfaccia grafica non visibile**:

   - Su macOS, assicurati che XQuartz sia in esecuzione
   - Riconfigura X11: `./setup-x11-macos.sh` seguito da `./docker-utils.sh restart`
   - Verifica lo stato di X11: `xhost`
   - Controlla i log dell'applicazione: `./docker-utils.sh logs`

2. **Problemi di connessione al database**:

   - Controlla i log del database: `./docker-utils.sh db-logs`
   - Verifica lo stato del container: `./docker-utils.sh status`
   - Se il problema persiste, prova a ricostruire i container: `./avvia-app-rebuild.sh`
   - Controlla che la connessione usi `root` come utente del database

3. **Errori durante la compilazione**:
   - Verifica di avere installato JDK 11 o superiore: `java -version`
   - Controlla eventuali errori nei sorgenti Java
   - Verifica che il driver MySQL sia stato scaricato correttamente in `GestioneRistorante/bin/jars/`

## Note per gli Sviluppatori

- I file sorgente Java si trovano in: `./GestioneRistorante/src/`
- Gli script SQL per il database si trovano in: `./SQL/init/`
- Le modifiche ai file `.java` richiedono la ricompilazione con `./compile.sh`
