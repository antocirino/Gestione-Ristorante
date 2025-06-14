@echo off
setlocal enabledelayedexpansion
title Ricostruzione Completa Gestione Ristorante
color 0A

echo ================================================
echo   RICOSTRUZIONE COMPLETA GESTIONE RISTORANTE
echo ================================================
echo.

rem Verifica se Docker è in esecuzione
docker info >nul 2>&1
if errorlevel 1 (
    echo ERRORE: Docker non è in esecuzione o non è installato.
    echo Avvia Docker Desktop e riprova.
    pause
    exit /b 1
)

echo Configurazione X11 per Windows...
set DISPLAY=127.0.0.1:0.0
echo DISPLAY impostato a: %DISPLAY%
echo.

echo Fermo eventuali container in esecuzione...
docker-compose down
echo.

echo Ricostruzione completa dei container (i dati esistenti saranno eliminati)...
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
echo.

echo Sistema avviato!
echo - phpMyAdmin: http://localhost:8081 (utente: root, password: password)
echo - Per visualizzare i log: docker-compose logs -f
echo - Per fermare l'applicazione: docker-compose down
echo.

echo Mostro i log dell'applicazione Java (premi CTRL+C per interrompere)
docker logs ristorante-app -f

pause
