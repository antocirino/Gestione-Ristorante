# Guida ai Test delle Classi

Questa guida spiega come compilare ed eseguire i test delle classi nel progetto **Gestione-Ristorante**.

---

## 1. Compilazione

Dalla cartella principale del progetto (**Gestione-Ristorante**), esegui il comando:

```sh
./compile.sh
```

Segui le istruzioni che appariranno a terminale per completare la compilazione.

---

## 2. Esecuzione dei Test

Dopo la compilazione, entra nella cartella `GestioneRistorante`:

```sh
cd GestioneRistorante
```

Esegui il programma principale con il comando:

```sh
java -cp bin:bin/jars/mysql-connector-j-8.0.33.jar entity.main
```

---

## Note

- Assicurati che il database sia avviato e accessibile prima di eseguire i test.
- Se necessario, imposta le variabili d’ambiente `DATABASE_USER` e `DATABASE_PASSWORD` con le credenziali corrette del database.
