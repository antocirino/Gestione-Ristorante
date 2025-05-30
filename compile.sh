#!/bin/bash

# Script per compilare l'applicazione Java per il ristorante

# Colori per i messaggi
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Inizializzazione compilazione per il Sistema di Gestione Ristorante${NC}"

# Creazione delle cartelle necessarie
echo "Creazione delle cartelle di progetto..."
mkdir -p GestioneRistorante/bin/{boundary,CFG,control,database,entity,Exception,resources}

# Scarica librerie necessarie
JDBC_DIR="GestioneRistorante/bin/jars"
mkdir -p $JDBC_DIR

# Scarica il driver MySQL JDBC se non presente
MYSQL_DRIVER="mysql-connector-j-8.0.33.jar"
if [ ! -f "$JDBC_DIR/$MYSQL_DRIVER" ]; then
  echo -e "${YELLOW}Scaricamento del driver MySQL JDBC...${NC}"
  curl -L -o "$JDBC_DIR/$MYSQL_DRIVER" "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar"
  if [ $? -eq 0 ]; then
    echo -e "${GREEN}Driver MySQL scaricato con successo.${NC}"
  else
    echo -e "${RED}Errore durante il download del driver MySQL.${NC}"
    exit 1
  fi
else
  echo "Driver MySQL già presente."
fi

# Scarica FlatLaf per un'interfaccia moderna (opzionale)
FLATLAF_VERSION="3.2.5"
FLATLAF_JAR="flatlaf-$FLATLAF_VERSION.jar"
if [ ! -f "$JDBC_DIR/$FLATLAF_JAR" ]; then
  echo -e "${YELLOW}Scaricamento della libreria FlatLaf per UI moderna...${NC}"
  curl -L -o "$JDBC_DIR/$FLATLAF_JAR" "https://repo1.maven.org/maven2/com/formdev/flatlaf/$FLATLAF_VERSION/flatlaf-$FLATLAF_VERSION.jar"
  if [ $? -eq 0 ]; then
    echo -e "${GREEN}FlatLaf scaricato con successo.${NC}"
  else
    echo -e "${RED}Errore durante il download di FlatLaf.${NC}"
    # Non è critico, quindi continuiamo
  fi
else
  echo "FlatLaf già presente."
fi

# Compila l'applicazione
echo -e "${YELLOW}Compilazione dell'applicazione...${NC}"
javac -d GestioneRistorante/bin -cp "$JDBC_DIR/*" $(find GestioneRistorante/src -name "*.java")

# Verifica se la compilazione ha avuto successo
if [ $? -eq 0 ]; then
  echo -e "${GREEN}Compilazione completata con successo!${NC}"
  
  # Rendi eseguibili gli script
  chmod +x start-app.sh
  chmod +x setup-x11-macos.sh
  
  echo -e "${YELLOW}Puoi ora avviare i container con:${NC}"
  echo "docker compose up -d"
  
  # Se siamo su macOS, suggerisci di configurare X11
  if [[ "$OSTYPE" == "darwin"* ]]; then
    echo -e "${YELLOW}Su macOS, esegui prima:${NC}"
    echo "./setup-x11-macos.sh"
  fi
else
  echo -e "${RED}Errore durante la compilazione.${NC}"
  exit 1
fi
