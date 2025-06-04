@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo      COMPILAZIONE GESTIONE RISTORANTE (Windows)
echo ===================================================
echo.

REM Verifica se java è disponibile
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERRORE: Java non trovato nel PATH.
    echo Assicurati che Java sia installato e configurato correttamente.
    pause
    exit /b 1
)

REM Mostra la versione di Java in uso
echo Versione Java in uso:
java -version
echo.

REM Definizione delle cartelle del progetto
set MAIN_DIR=GestioneRistorante
set SRC_DIR=%MAIN_DIR%\src
set BIN_DIR=%MAIN_DIR%\bin
set LIB_DIR=%MAIN_DIR%\lib

echo Struttura del progetto:
echo - Directory principale: %MAIN_DIR%
echo - Directory sorgenti: %SRC_DIR%
echo - Directory compilati: %BIN_DIR%
echo - Directory librerie: %LIB_DIR%
echo.

REM Verifica che le cartelle esistano
if not exist "%SRC_DIR%" (
    echo ERRORE: Directory dei sorgenti %SRC_DIR% non trovata.
    echo Verifica che la struttura del progetto sia corretta.
    pause
    exit /b 1
)

if not exist "%BIN_DIR%" (
    echo Creazione directory bin in %BIN_DIR%...
    mkdir "%BIN_DIR%"
)

REM Verifica contenuto directory sorgenti
echo Verifica contenuto directory sorgenti...
dir "%SRC_DIR%" /b

REM Crea la struttura di directory in bin
mkdir "%BIN_DIR%\boundary" 2>nul
mkdir "%BIN_DIR%\CFG" 2>nul
mkdir "%BIN_DIR%\control" 2>nul
mkdir "%BIN_DIR%\database" 2>nul
mkdir "%BIN_DIR%\DTO" 2>nul
mkdir "%BIN_DIR%\entity" 2>nul
mkdir "%BIN_DIR%\Exception" 2>nul
mkdir "%BIN_DIR%\test" 2>nul
mkdir "%BIN_DIR%\resources" 2>nul

REM Costruisci il classpath includendo tutte le librerie JAR
set CP=.
if exist "%LIB_DIR%" (
    for %%f in ("%LIB_DIR%\*.jar") do (
        set CP=!CP!;"%%f"
    )
    echo Classpath configurato con le librerie in %LIB_DIR%
) else (
    echo AVVISO: Directory delle librerie %LIB_DIR% non trovata, la compilazione potrebbe fallire.
)

echo.
echo Compilazione in corso...
echo.

REM Se FirstForm.java è nella directory principale src ma deve essere in boundary,
REM dobbiamo prima muoverlo
if exist "%SRC_DIR%\FirstForm.java" (
    echo FirstForm.java trovato nella directory principale, verifica se appartiene a boundary...
    findstr /C:"package boundary" "%SRC_DIR%\FirstForm.java" > nul
    if !ERRORLEVEL! EQU 0 (
        echo FirstForm.java appartiene al package boundary, spostamento in corso...
        move "%SRC_DIR%\FirstForm.java" "%SRC_DIR%\boundary\" > nul
        echo FirstForm.java spostato correttamente in %SRC_DIR%\boundary\
    )
)

REM Compila ogni file singolarmente invece di usare wildcard (*.java) che può causare problemi su Windows
echo Compilazione CFG...
for %%f in ("%SRC_DIR%\CFG\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione Exception...
for %%f in ("%SRC_DIR%\Exception\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione entity...
for %%f in ("%SRC_DIR%\entity\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione database...
for %%f in ("%SRC_DIR%\database\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione DTO...
for %%f in ("%SRC_DIR%\DTO\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione control...
for %%f in ("%SRC_DIR%\control\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione boundary...
for %%f in ("%SRC_DIR%\boundary\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo Compilazione test...
for %%f in ("%SRC_DIR%\test\*.java") do (
    javac -d "%BIN_DIR%" -cp "%CP%;%BIN_DIR%" "%%f"
)

echo.
echo Verifica dei file compilati...

REM Controlla se ci sono classi nella directory bin
dir /s /b "%BIN_DIR%\*.class" > classlist.txt
findstr /r /c:"\.class$" classlist.txt > nul
if %ERRORLEVEL% EQU 0 (
    echo File .class trovati in %BIN_DIR%. La compilazione è avvenuta con successo.
    
    REM Cerca FirstForm.class
    findstr /i /c:"FirstForm.class" classlist.txt > nul
    if %ERRORLEVEL% EQU 0 (
        echo FirstForm.class trovato. L'applicazione può essere avviata.
    ) else (
        echo AVVISO: FirstForm.class non trovato. L'avvio dell'applicazione potrebbe fallire.
    )
) else (
    echo ERRORE: Nessun file .class trovato in %BIN_DIR%.
    echo La compilazione non è avvenuta correttamente.
)

REM Rimuovi il file temporaneo
del classlist.txt

echo.
echo Compilazione completata. Ora puoi avviare l'applicazione con 'start-app-win.bat'
pause
