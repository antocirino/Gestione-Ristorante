#!/bin/bash

# Script per riavviare l'applicazione con GUI attiva
# Questo script riavvia un container già esistente e configura XQuartz e le variabili d'ambiente

# Colori per i messaggi
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Riavvio dell'ambiente per la GUI in Docker${NC}"

# 1. Verifica che XQuartz sia installato
if ! [ -d "/Applications/Utilities/XQuartz.app" ]; then
    echo -e "${RED}XQuartz non è installato! Installalo con:${NC}"
    echo "brew install --cask xquartz"
    echo "oppure scaricalo da https://www.xquartz.org/"
    echo "Quindi esegui nuovamente questo script."
    exit 1
fi

echo -e "${GREEN}✓ XQuartz trovato${NC}"

# 2. Avvia XQuartz se non è già in esecuzione
if ! ps -e | grep -q XQuartz; then
    echo -e "${YELLOW}Avvio XQuartz...${NC}"
    open -a XQuartz
    
    # Attendi che XQuartz sia completamente avviato
    echo "Attendi l'avvio completo di XQuartz (10 secondi)..."
    sleep 10
    echo -e "${GREEN}✓ XQuartz avviato${NC}"
else
    echo -e "${GREEN}✓ XQuartz è già in esecuzione${NC}"
fi

# 3. Configura le preferenze di XQuartz
echo -e "${YELLOW}Configurazione delle preferenze XQuartz...${NC}"
defaults write org.xquartz.X11 nolisten_tcp 0
defaults write org.xquartz.X11 app_to_run /usr/bin/true
defaults write org.xquartz.X11 no_auth 1
echo -e "${GREEN}✓ Preferenze XQuartz configurate${NC}"

# 4. Abilita le connessioni da client remoti
echo -e "${YELLOW}Configurazione degli host X11 autorizzati...${NC}"
xhost +localhost
xhost +127.0.0.1

# 5. Configura l'IP corretto per Docker
echo -e "${YELLOW}Configuro l'IP del Docker host...${NC}"
export DISPLAY=:0
export IP=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')
if [ -z "$IP" ]; then
    # Prova con altre interfacce se en0 non ha un indirizzo IP
    export IP=$(ifconfig | grep "inet " | grep -v 127.0.0.1 | awk '{print $2}' | head -n 1)
fi

if [ -z "$IP" ]; then
    echo -e "${RED}Impossibile determinare l'indirizzo IP!${NC}"
    exit 1
fi

echo -e "${GREEN}Il tuo indirizzo IP è: $IP${NC}"
xhost +$IP
echo -e "${GREEN}✓ Host X11 configurati${NC}"

# 6. Aggiorna la variabile DISPLAY nel container
echo -e "${YELLOW}Riavvio del container con la nuova configurazione DISPLAY...${NC}"

# Verifica se il container esiste
if ! docker ps -a | grep -q ristorante-app; then
    echo -e "${RED}Il container 'ristorante-app' non esiste!${NC}"
    echo "Esegui prima docker compose up -d per creare il container."
    exit 1
fi

# Aggiorna la variabile DISPLAY nel container e riavvia
docker stop ristorante-app
docker rm ristorante-app

docker compose up -d

# Attendi che il container sia attivo
echo -e "${YELLOW}Attendo che il container sia pronto...${NC}"
sleep 5

echo -e "${GREEN}✓ Configurazione completata${NC}"
echo -e "${YELLOW}Ora dovresti vedere l'interfaccia grafica dell'applicazione.${NC}"
echo "Se l'interfaccia non appare, puoi verificare lo stato con:"
echo -e "${YELLOW}docker logs ristorante-app${NC}"
