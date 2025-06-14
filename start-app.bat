@echo off
setlocal enabledelayedexpansion
title Avvio Gestione Ristorante
color 0A

echo ================================================
echo           AVVIO GESTIONE RISTORANTE
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

rem Imposta il percorso corrente alla directory dello script
cd /d "%~dp0"

echo Imposta la variabile DISPLAY per X11
set DISPLAY=172.31.16.1:0.0
echo DISPLAY impostato a: %DISPLAY%
echo.

echo Avvio dell'applicazione tramite Docker...
echo.

rem Verifica se il container è in esecuzione
docker ps | find "gestione-ristorante" > nul
if errorlevel 1 (
    echo Il container gestione-ristorante non è in esecuzione, avvio...
    docker-compose up -d
    echo.
)

rem Esegue l'applicazione all'interno del container Docker
docker exec -e DISPLAY=%DISPLAY% gestione-ristorante java -cp "/app/out/production/Gestione-Ristorante:/app/GestioneRistorante/bin/jars/*" -Djava.awt.headless=false boundary.FirstForm

if errorlevel 1 (
    echo.
    echo ERRORE: Si è verificato un problema durante l'esecuzione dell'applicazione.
    echo.
    echo Verifica:
    echo 1. Che XLaunch sia in esecuzione e configurato con "Disable access control"
    echo 2. Che nessun firewall stia bloccando la connessione a XLaunch
    echo 3. Che il container Docker sia avviato correttamente
    echo.
    
    rem Debug info
    echo Informazioni di debug:
    echo - DISPLAY: %DISPLAY%
    echo - Container in esecuzione:
    docker ps
    echo.
)

echo.
echo Applicazione terminata.
echo.
pause
