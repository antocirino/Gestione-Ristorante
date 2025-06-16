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

# Scarica SVG Salamander per le icone SVG
SVG_SALAMANDER_JAR="svg-salamander-1.1.4.jar"
if [ ! -f "$JDBC_DIR/$SVG_SALAMANDER_JAR" ]; then
  echo -e "${YELLOW}Scaricamento della libreria SVG Salamander per le icone...${NC}"
  curl -L -o "$JDBC_DIR/$SVG_SALAMANDER_JAR" "https://github.com/blackears/svgSalamander/releases/download/v1.1.4/svgSalamander-1.1.4.jar"
  if [ $? -eq 0 ]; then
    echo -e "${GREEN}SVG Salamander scaricato con successo.${NC}"
  else
    echo -e "${RED}Errore durante il download di SVG Salamander.${NC}"
    # Non è critico, quindi continuiamo
  fi
else
  echo "SVG Salamander già presente."
fi

# Scarica OpenPDF per l'esportazione dei conti in PDF
# Versione 1.3.30 è stabile e compatibile con Java 11
OPENPDF_VERSION="1.3.30"
OPENPDF_JAR="openpdf-$OPENPDF_VERSION.jar"

# Rimuovi eventuali versioni precedenti che potrebbero essere corrotte
echo -e "${YELLOW}Rimuovo eventuali versioni precedenti di OpenPDF...${NC}"
find "$JDBC_DIR" -name "openpdf-*.jar" -type f -delete

echo -e "${YELLOW}Scaricamento della libreria OpenPDF per l'esportazione in PDF...${NC}"
curl -L -o "$JDBC_DIR/$OPENPDF_JAR" "https://repo1.maven.org/maven2/com/github/librepdf/openpdf/$OPENPDF_VERSION/openpdf-$OPENPDF_VERSION.jar"
if [ $? -eq 0 ]; then
  echo -e "${GREEN}OpenPDF scaricato con successo.${NC}"
else
  echo -e "${RED}Errore durante il download di OpenPDF.${NC}"
  exit 1
fi

# Scarica le dipendenze di OpenPDF
echo -e "${YELLOW}Scaricamento delle dipendenze di OpenPDF...${NC}"

# Dipendenza: BouncyCastle PKIX compatibile con Java 11 e OpenPDF 1.3.30
BCPKIX_VERSION="1.69"
BCPKIX_JAR="bcpkix-jdk15on-$BCPKIX_VERSION.jar"

# Rimuovi eventuali versioni precedenti che potrebbero essere corrotte
echo -e "${YELLOW}Rimuovo eventuali versioni precedenti di BouncyCastle PKIX...${NC}"
find "$JDBC_DIR" -name "bcpkix-*.jar" -type f -delete

echo -e "${YELLOW}Scaricamento di BouncyCastle PKIX...${NC}"
curl -L -o "$JDBC_DIR/$BCPKIX_JAR" "https://repo1.maven.org/maven2/org/bouncycastle/bcpkix-jdk15on/$BCPKIX_VERSION/bcpkix-jdk15on-$BCPKIX_VERSION.jar"
if [ $? -eq 0 ]; then
  echo -e "${GREEN}BouncyCastle PKIX scaricato con successo.${NC}"
else
  echo -e "${RED}Errore durante il download di BouncyCastle PKIX.${NC}"
  exit 1
fi

# Dipendenza: BouncyCastle Provider compatibile con Java 11
BCPROV_VERSION="1.69"
BCPROV_JAR="bcprov-jdk15on-$BCPROV_VERSION.jar"

# Rimuovi eventuali versioni precedenti che potrebbero essere corrotte
echo -e "${YELLOW}Rimuovo eventuali versioni precedenti di BouncyCastle Provider...${NC}"
find "$JDBC_DIR" -name "bcprov-*.jar" -type f -delete

echo -e "${YELLOW}Scaricamento di BouncyCastle Provider...${NC}"
curl -L -o "$JDBC_DIR/$BCPROV_JAR" "https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/$BCPROV_VERSION/bcprov-jdk15on-$BCPROV_VERSION.jar"
if [ $? -eq 0 ]; then
  echo -e "${GREEN}BouncyCastle Provider scaricato con successo.${NC}"
else
  echo -e "${RED}Errore durante il download di BouncyCastle Provider.${NC}"
  exit 1
fi

# Dipendenza: BouncyCastle Util compatibile con Java 11
# Per OpenPDF 1.3.30, utilizziamo la versione disponibile 1.69
BCUTIL_VERSION="1.69"
BCUTIL_JAR="bcutil-jdk15on-$BCUTIL_VERSION.jar"

# Rimuovi eventuali versioni precedenti che potrebbero essere corrotte
echo -e "${YELLOW}Rimuovo eventuali versioni precedenti di BouncyCastle Util...${NC}"
find "$JDBC_DIR" -name "bcutil-*.jar" -type f -delete

echo -e "${YELLOW}Scaricamento di BouncyCastle Util...${NC}"
curl -L -o "$JDBC_DIR/$BCUTIL_JAR" "https://repo1.maven.org/maven2/org/bouncycastle/bcutil-jdk15on/$BCUTIL_VERSION/bcutil-jdk15on-$BCUTIL_VERSION.jar"
if [ $? -eq 0 ]; then
  echo -e "${GREEN}BouncyCastle Util scaricato con successo.${NC}"
else
  echo -e "${RED}Errore durante il download di BouncyCastle Util.${NC}"
  exit 1
fi

# Verifica che i file JAR siano stati scaricati correttamente
echo -e "${YELLOW}Verifico la presenza di tutte le librerie necessarie...${NC}"
for jar_file in "$OPENPDF_JAR" "$BCPKIX_JAR" "$BCPROV_JAR" "$BCUTIL_JAR"; do
  if [ ! -f "$JDBC_DIR/$jar_file" ] || [ ! -s "$JDBC_DIR/$jar_file" ]; then
    echo -e "${RED}Errore: Il file $jar_file non esiste o è vuoto.${NC}"
    exit 1
  else
    echo -e "${GREEN}File $jar_file verificato con successo.${NC}"
  fi
done

# Compila l'applicazione (escludendo i test per ora)
echo -e "${YELLOW}Compilazione dell'applicazione (escludendo i test)...${NC}"
javac -encoding UTF-8 -d GestioneRistorante/bin -cp "$JDBC_DIR/*" $(find GestioneRistorante/src -name "*.java" | grep -v "/test/")

# Copia le risorse (icone SVG) nella cartella bin
echo -e "${YELLOW}Copia delle risorse...${NC}"
if [ -d "GestioneRistorante/src/resources" ]; then
  cp -r GestioneRistorante/src/resources/* GestioneRistorante/bin/resources/
  echo "Risorse copiate con successo."
else
  echo "Cartella resources non trovata, le icone potrebbero non essere disponibili."
fi

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
