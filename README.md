# 🍽️ Sistema di Gestione per Ristorante

> Progetto sviluppato per il corso di Ingegneria del Software.  
> Linguaggio di programmazione utilizzato: **Java**  
> Database: **Relazionale (MySQL)**
> Architettura: **BCED (Boundary Control Entity Database)**
> Containerizzazione: **Docker**

---

## 📋 Descrizione del Progetto

Questo sistema software ha l'obiettivo di supportare digitalmente la gestione di un ristorante, facilitando le attività quotidiane del personale e migliorando l'efficienza complessiva del servizio. Il sistema consente la raccolta digitale degli ordini da parte dei camerieri, la verifica della disponibilità degli ingredienti in magazzino, il supporto alla cucina nella preparazione dei piatti, la gestione del conto da parte del cassiere, e l'analisi delle scorte da parte del direttore.

## 🐳 Docker e Containerizzazione

Il progetto è completamente dockerizzato per facilitare l'installazione e garantire lo stesso ambiente di esecuzione su qualsiasi piattaforma. Include:

- Container per l'applicazione Java con interfaccia Swing
- Container per il database MySQL
- Container per phpMyAdmin (per la gestione web del database)

### Variabili d'ambiente

Il sistema utilizza variabili d'ambiente definite nel file `.env` per configurare l'applicazione:

```
# Database
DATABASE_URL=jdbc:mysql://db:3306/ristorante
DATABASE_USER=root
DATABASE_PASSWORD=password

# Porte
JAVA_APP_PORT=8090
PHPMYADMIN_PORT=8081
```

Puoi modificare queste variabili nel file `.env` per personalizzare la configurazione.

---

## 🚀 Come Eseguire il Progetto

### Metodo 1: Avvio Rapido con Ricostruzione (consigliato per prima installazione) **_macOS_**

```zsh
chmod +x avvia-app-rebuild.sh
./avvia-app-rebuild.sh
```

Questo script si occuperà di:

- Configurare X11 per la visualizzazione dell'interfaccia grafica
- Fermare eventuali container in esecuzione
- Ricostruire i container (elimina anche i dati esistenti)
- Mostrare i log dell'applicazione

### Metodo 1: Avvio Rapido con Ricostruzione (consigliato per prima installazione) **_Windows_**

1. **Compila l'applicazione Java** (usa Git Bash o WSL):

   ```sh
   chmod +x compile.sh
   ./compile.sh
   ```

2. **Costruisci e avvia i container**:
   ```sh
   docker compose build --no-cache
   docker compose up -d
   ```
   Se tutto è configurato correttamente, la GUI Java Swing apparirà automaticamente sul desktop di Windows tramite VcXsrv.

### Metodo 2: Avvio Manuale Passo-Passo

#### 1. Compilazione dell'applicazione

Prima di avviare i container, compila l'applicazione:

```zsh
chmod +x compile.sh
./compile.sh
```

### 2. Configurazione X11 per l'interfaccia grafica (solo per macOS)

```zsh
chmod +x setup-x11-macos.sh
./setup-x11-macos.sh
```

### 3. Avvio dei container Docker

```zsh
chmod +x docker-utils.sh
./docker-utils.sh start
```

Questo comando avvierà:

- Un container MySQL con il database ristorante
- Un container per l'applicazione Java con GUI
- Un container phpMyAdmin per gestire facilmente il database

### 4. Gestione del sistema

```zsh
# Per vedere i log dell'applicazione
./docker-utils.sh logs

# Per riavviare i container (mantenendo i dati)
./docker-utils.sh restart

# Per vedere lo stato dei container
./docker-utils.sh status

# Per accedere alla console MySQL
./docker-utils.sh db-console
```

### 5. Accesso a phpMyAdmin

Per gestire il database tramite interfaccia web, accedi a:

- URL: http://localhost:8081
- Username: root
- Password: password

### 6. Arresto dei container

```zsh
./docker-utils.sh stop
```

Per rimuovere anche i volumi (dati del database):

```zsh
./docker-utils.sh clean
```

## ⚠️ Risoluzione dei problemi comuni

### 1. Interfaccia grafica non visibile

Se l'interfaccia grafica non appare:

```zsh
# Riconfigurare X11 e riavviare i container
./setup-x11-macos.sh
./docker-utils.sh restart

# Controllare i log per eventuali errori
./docker-utils.sh logs
```

Su **_macOS_** se l'interfaccia grafica non appare ma i container si avviano correttamente:

```zsh
export DISPLAY=:0
```

### 2. Problemi di connessione al database

```zsh
# Controlla i log del database
./docker-utils.sh db-logs

# Verifica se il container database è in salute
./docker-utils.sh status

# In caso di persistenti problemi, ricostruisci i container
./avvia-app-rebuild.sh
```

### 3. Per maggiori dettagli sulla configurazione della GUI

Consulta il file di documentazione specifico:

```zsh
less GUI-Docker.md
```

Per una guida rapida all'utilizzo:

```zsh
less QUICKSTART.md
```

## 🧠 Autori

- **[Matteo Adaggio](https://github.com/matteoadaggio)**
- **[Alessandro Cioffi](https://github.com/MisterCioffi)**
- **[Luigi Cirillo](https://github.com/GGCIRILLO)**
- **[Antonio Cirino](https://github.com/antocirino)**
