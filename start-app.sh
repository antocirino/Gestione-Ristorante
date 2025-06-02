#!/bin/bash

# Script di avvio per l'applicazione Java Swing nel container Docker

# Attende che il database sia disponibile
echo "Attesa connessione al database..."
RETRIES=60  # Aumentiamo il numero di tentativi

# Tentiamo prima con netcat
if command -v nc &> /dev/null; then
    echo "Utilizzo netcat per verificare la disponibilità del database..."
    until [ $RETRIES -eq 0 ] || nc -z db 3306; do
        echo "Attesa per il database MySQL, rimanenti tentativi: $RETRIES..."
        RETRIES=$((RETRIES-1))
        sleep 2  # Aumentiamo il tempo di attesa tra i tentativi
    done
else
    # Fallback se netcat non è disponibile
    echo "Netcat non disponibile, attesa di 30 secondi per sicurezza..."
    sleep 30
    RETRIES=1  # Impostazione per evitare errori
fi

if [ $RETRIES -eq 0 ]; then
    echo "Errore: il database MySQL non è disponibile dopo l'attesa."
    echo "Verifico lo stato della rete:"
    ping -c 3 db || echo "Impossibile raggiungere il container del database"
    exit 1
fi

echo "Database MySQL disponibile, avvio applicazione..."

# Avvio dell'applicazione Java
# -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel è opzionale, per un look più moderno
# -Dsun.java2d.xrender=false può risolvere problemi di rendering in Docker
# -Djava.awt.headless=false è necessario per le applicazioni GUI in Docker
java -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel \
     -Dsun.java2d.xrender=false \
     -Djava.awt.headless=false \
     -cp "bin:bin/jars/*" boundary.FirstForm
