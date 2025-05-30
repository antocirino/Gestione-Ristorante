#!/bin/bash

# Script per configurare X11 su macOS per l'uso con Docker
# Questo script deve essere eseguito prima di avviare i container Docker

# Controlla se XQuartz è installato
if ! [ -d "/Applications/Utilities/XQuartz.app" ]; then
    echo "XQuartz non sembra essere installato. Installalo con:"
    echo "brew install --cask xquartz"
    echo "oppure scaricalo da https://www.xquartz.org/"
    exit 1
fi

# Avvia XQuartz se non è già in esecuzione
if ! ps -e | grep -q XQuartz; then
    echo "Avvio XQuartz..."
    open -a XQuartz
    
    # Attendi che XQuartz sia completamente avviato
    echo "Attendi l'avvio completo di XQuartz (10 secondi)..."
    sleep 10
fi

# Configura le preferenze di XQuartz per accettare connessioni da rete
# Questo crea/modifica il file di preferenze
defaults write org.xquartz.X11 nolisten_tcp 0
defaults write org.xquartz.X11 app_to_run /usr/bin/true

# Abilita le connessioni da client remoti
echo "Configurazione degli host X11 autorizzati..."
xhost +localhost
xhost +127.0.0.1

echo "Configuro l'IP del Docker host..."
export DISPLAY=:0
export IP=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')
echo "Il tuo indirizzo IP è: $IP"
xhost +$IP

echo "X11 configurato correttamente per Docker."
echo "Puoi ora avviare i container con 'docker compose up -d'."
echo "Ricorda che dovrai eseguire questo script ogni volta che riavvii il computer o XQuartz."
