#!/bin/bash

# Utility per gestire i container Docker del progetto Ristorante

# Colori per i messaggi
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

function show_help {
  echo -e "${BLUE}========= Sistema di Gestione Ristorante - Docker Utility ==========${NC}"
  echo "Utilizzo: $0 [comando]"
  echo ""
  echo "Comandi disponibili:"
  echo "  start       - Avvia tutti i container"
  echo "  stop        - Ferma tutti i container"
  echo "  restart     - Riavvia tutti i container"
  echo "  status      - Mostra lo stato dei container"
  echo "  logs        - Mostra i log dell'applicazione Java"
  echo "  db-logs     - Mostra i log del database"
  echo "  db-console  - Apre la console MySQL nel container del database"
  echo "  rebuild     - Ricostruisce e avvia i container (attenzione: rimuove i dati)"
  echo "  help        - Mostra questo messaggio di aiuto"
  echo ""
}

function check_docker {
  if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker non è installato. Installalo prima di continuare.${NC}"
    exit 1
  fi
  
  # Nelle versioni più recenti, docker-compose è disponibile come subcommand (docker compose)
  # quindi non abbiamo bisogno di verificare se esiste separatamente
}

# Controlla che Docker sia installato
check_docker

# Analizza il comando
case "$1" in
  start)
    echo -e "${YELLOW}Avvio dei container...${NC}"
    docker compose up -d
    ;;
    
  stop)
    echo -e "${YELLOW}Arresto dei container...${NC}"
    docker compose down
    ;;
    
  restart)
    echo -e "${YELLOW}Riavvio dei container...${NC}"
    docker compose down
    docker compose up -d
    ;;
    
  status)
    echo -e "${YELLOW}Stato dei container:${NC}"
    docker compose ps
    ;;
    
  logs)
    echo -e "${YELLOW}Log dell'applicazione Java:${NC}"
    docker compose logs -f app
    ;;
    
  db-logs)
    echo -e "${YELLOW}Log del database MySQL:${NC}"
    docker compose logs -f db
    ;;
    
  db-console)
    echo -e "${YELLOW}Apertura console MySQL...${NC}"
    source .env 2> /dev/null || true
    docker compose exec db mysql -u${MYSQL_USER:-root} -p${MYSQL_ROOT_PASSWORD:-password} ${MYSQL_DATABASE:-ristorante}
    ;;
    
  rebuild)
    echo -e "${RED}⚠️  Attenzione: questa operazione rimuoverà tutti i dati esistenti del database.${NC}"
    read -p "Sei sicuro di voler continuare? (s/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Ss]$ ]]; then
      echo -e "${YELLOW}Rimozione dei container e ricostruzione...${NC}"
      docker compose down -v
      docker compose build --no-cache
      docker compose up -d
    else
      echo -e "${BLUE}Operazione annullata.${NC}"
    fi
    ;;
    
  help|*)
    show_help
    ;;
esac
