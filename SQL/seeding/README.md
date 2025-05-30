# Script di Generazione Dati per il Database Ristorante

Questa cartella contiene script per generare dati di esempio da utilizzare nel database del sistema di gestione ristorante.

## Script Disponibili

### `generate_sample_data.py`

Questo script Python legge lo schema del database dal file `01-schema.sql` e genera un file SQL con dati casuali per tutte le tabelle principali del database.

#### Caratteristiche:

- Genera dati realistici per il ristorante, tavoli, pietanze, ingredienti, ricette, menu fissi, e ordini
- Crea relazioni coerenti tra le entità
- Integra automaticamente con la struttura del database esistente
- Genera stati degli ordini in base all'orario di creazione
- Crea menù completi con tutte le pietanze disponibili

#### Come utilizzare lo script:

1. Assicurati di avere Python installato sul tuo sistema
2. Posizionati nella cartella `SQL/seeding`
3. Esegui il comando:

```bash
./generate_sample_data.py
```

Il file di output verrà generato in `SQL/init/02-sample-data-generated.sql`

## Personalizzazione

È possibile personalizzare la generazione dei dati modificando lo script `generate_sample_data.py`:

- Cambia il valore della variabile `num_ristoranti` per generare dati per più ristoranti

## Importazione dei Dati Generati

Per importare i dati generati nel tuo database:

1. Assicurati che il database sia già stato creato con la struttura definita in `01-schema.sql`
2. Importa il file generato usando:

```bash
mysql -u username -p ristorante < /path/to/SQL/init/02-sample-data-generated.sql
```

Oppure tramite phpMyAdmin o un altro strumento di gestione del database.

> Nota: Se dovessi runnare nuovamente lo script, probabilmente al momento del caricamento dei dati ci saranno degli errori dovuti agli apostrofi. Basta rimuoverli dal file SQL.
