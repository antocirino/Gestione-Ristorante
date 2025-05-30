FROM openjdk:11

# Installa le dipendenze necessarie per X11, interfaccia grafica e netcat per il controllo di connettività
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxext6 \
    libfreetype6 \
    libxft2 \
    x11-apps \
    xauth \
    netcat \
    && rm -rf /var/lib/apt/lists/*

# Imposta le variabili d'ambiente per il display
ENV DISPLAY=host.docker.internal:0
ENV LIBGL_ALWAYS_INDIRECT=1
ENV QT_X11_NO_MITSHM=1

WORKDIR /app

# Crea le directory necessarie
RUN mkdir -p ./bin ./src ./lib ./config

# Copia i file JDBC driver
COPY ./GestioneRistorante/bin/jars/*.jar ./lib/

# Copia il codice sorgente compilato
COPY ./GestioneRistorante/bin/boundary ./bin/boundary/
COPY ./GestioneRistorante/bin/CFG ./bin/CFG/
COPY ./GestioneRistorante/bin/control ./bin/control/
COPY ./GestioneRistorante/bin/database ./bin/database/
COPY ./GestioneRistorante/bin/entity ./bin/entity/
COPY ./GestioneRistorante/bin/Exception ./bin/Exception/
COPY ./GestioneRistorante/bin/resources ./bin/resources/

# Copia lo script di avvio
COPY ./start-app.sh ./
RUN chmod +x ./start-app.sh

# Esporre la porta su cui funzionerà l'applicazione
EXPOSE 8080

# Comando per eseguire l'applicazione con X11 forwarding
ENTRYPOINT ["./start-app.sh"]
