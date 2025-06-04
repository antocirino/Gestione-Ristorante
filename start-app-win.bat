@echo off
echo Avvio applicazione Gestione Ristorante su Windows...

REM Cerca la classe FirstForm compilata in vari percorsi possibili
set FOUND=0

REM Definisci i possibili percorsi della classe compilata
if exist "GestioneRistorante\bin\boundary\FirstForm.class" (
    echo Trovato FirstForm in GestioneRistorante\bin\boundary
    set CLASSPATH=GestioneRistorante\bin;GestioneRistorante\lib\*
    set FOUND=1
) else if exist "bin\boundary\FirstForm.class" (
    echo Trovato FirstForm in bin\boundary
    set CLASSPATH=bin;lib\*
    set FOUND=1
) else if exist "GestioneRistorante\out\production\GestioneRistorante\boundary\FirstForm.class" (
    echo Trovato FirstForm in GestioneRistorante\out\production\GestioneRistorante\boundary
    set CLASSPATH=GestioneRistorante\out\production\GestioneRistorante;GestioneRistorante\lib\*
    set FOUND=1
) else if exist "out\production\GestioneRistorante\boundary\FirstForm.class" (
    echo Trovato FirstForm in out\production\GestioneRistorante\boundary
    set CLASSPATH=out\production\GestioneRistorante;lib\*
    set FOUND=1
)

if %FOUND%==0 (
    echo ERRORE: Impossibile trovare la classe FirstForm compilata!
    echo Verifica di aver compilato il progetto.
    echo Esegui prima il comando di compilazione.
    pause
    exit /b 1
)

echo Utilizzo classpath: %CLASSPATH%

REM Avvio dell'applicazione Java con il package corretto
java -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel ^
     -Dsun.java2d.xrender=false ^
     -Djava.awt.headless=false ^
     -cp "%CLASSPATH%" boundary.FirstForm

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Errore durante l'avvio dell'applicazione.
    echo Verifica che Java sia installato correttamente.
    echo.
    echo Versione Java in uso:
    java -version
)

pause
