@echo off
title Avvio GUI Gestione Ristorante
color 0A

echo ================================================
echo      AVVIO GUI GESTIONE RISTORANTE (WINDOWS)
echo ================================================
echo.

rem Imposta la variabile DISPLAY per puntare al server X11 locale
set DISPLAY=127.0.0.1:0.0
echo DISPLAY impostato a: %DISPLAY%
echo.

rem Imposta il percorso corrente alla directory dello script
cd /d "%~dp0"

echo Avvio dell'applicazione in corso...
echo.

rem Esegue l'applicazione con il classpath che include il percorso corretto dei JAR
java -cp "out\production\Gestione-Ristorante;GestioneRistorante\bin\jars\*" -Djava.awt.headless=false boundary.FirstForm

if errorlevel 1 (
    echo.
    echo ERRORE: Si è verificato un problema durante l'esecuzione dell'applicazione.
    echo.
    echo Verifica:
    echo 1. Che XLaunch sia in esecuzione e configurato con "Disable access control"
    echo 2. Che nessun firewall stia bloccando la connessione a XLaunch
    echo 3. Che tutti i JAR necessari siano presenti nel classpath
    echo.
    
    rem Debug info
    echo Informazioni di debug:
    echo - DISPLAY: %DISPLAY%
    echo - Directory corrente: %CD%
    echo - File .class esistenti:
    dir "out\production\Gestione-Ristorante\boundary\FirstForm.class" 2>nul || echo FirstForm.class non trovato!
    echo.
    
    rem Log degli errori
    echo Per maggiori dettagli, prova ad avviare l'applicazione con log di debug:
    echo java -cp "out\production\Gestione-Ristorante;GestioneRistorante\bin\jars\*" -Djava.awt.headless=false -Dawt.debug=true -Dsun.awt.debuggraphics=true boundary.FirstForm
)

echo.
echo Applicazione terminata.
echo.
pause
