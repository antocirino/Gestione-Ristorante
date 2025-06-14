@echo off
setlocal enabledelayedexpansion
title Compilazione Gestione Ristorante
color 0B

echo ================================================
echo       COMPILAZIONE GESTIONE RISTORANTE
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

echo Compilazione dell'applicazione utilizzando Docker...
echo.

cd "%~dp0"

rem Crea una cartella temporanea per la compilazione
if not exist "tmp" mkdir "tmp"

rem Esegue la compilazione all'interno del container Docker
docker run --rm -v "%CD%:/app" -w /app openjdk:11 javac -encoding UTF-8 -d tmp -cp "GestioneRistorante/bin/jars/*:." GestioneRistorante/src/boundary/FirstForm.java GestioneRistorante/src/boundary/*.java GestioneRistorante/src/control/*.java GestioneRistorante/src/DAO/*.java GestioneRistorante/src/DTO/*.java GestioneRistorante/src/entity/*.java

if errorlevel 1 (
    echo.
    echo ERRORE: La compilazione è fallita.
    echo Verifica i messaggi di errore sopra.
    echo.
) else (
    echo.
    echo Compilazione completata con successo!
    echo.
    
    rem Copia i file compilati nella directory di output corretta
    if not exist "out\production\Gestione-Ristorante" mkdir "out\production\Gestione-Ristorante"
    xcopy /E /Y tmp\* "out\production\Gestione-Ristorante\"
    
    rem Copia i file di risorse
    if exist "GestioneRistorante\src\resources" (
        echo Copia dei file di risorse in corso...
        xcopy /E /Y "GestioneRistorante\src\resources" "out\production\Gestione-Ristorante\resources\"
        echo File di risorse copiati.
    ) else (
        echo Nessuna risorsa da copiare.
    )
    
    rem Rimuove la cartella temporanea
    rmdir /S /Q tmp
)

echo.
echo Operazione completata.
echo.
pause
