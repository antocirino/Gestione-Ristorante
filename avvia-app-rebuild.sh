#!/bin/zsh

# Script per avviare rapidamente l'applicazione Gestione Ristorante con X11 per GUI

# Colori per i messaggi
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Avvio del Sistema di Gestione Ristorante ===${NC}"

# 1. Configura X11
echo -e "${YELLOW}Configurazione X11 per macOS...${NC}"
chmod +x setup-x11-macos.sh
./setup-x11-macos.sh

# 2. Ferma eventuali container in esecuzione
echo -e "${YELLOW}Fermo eventuali container in esecuzione...${NC}"
chmod +x docker-utils.sh
./docker-utils.sh stop

# 3. Ricostruisci i container --> elimina anche i volumi (attenzione: rimuove i dati)
echo -e "${YELLOW}Ricostruzione dei container...${NC}"
./docker-utils.sh rebuild

# 4. Mostra info di accesso
echo -e "${GREEN}Sistema avviato!${NC}"
echo -e "- phpMyAdmin: ${BLUE}http://localhost:8081${NC} (utente: root, password: password)"
echo -e "- Per visualizzare i log: ${YELLOW}./docker-utils.sh logs${NC}"
echo -e "- Per fermare l'applicazione: ${YELLOW}./docker-utils.sh stop${NC}"

# 5. Mostra i log in tempo reale
echo -e "${YELLOW}Mostro i log dell'applicazione Java (premi CTRL+C per interrompere)${NC}"
docker logs ristorante-app -f
