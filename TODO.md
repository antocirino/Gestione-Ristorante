### TODO

# Alta priorità

- in questo momento si può fare un ordine mettendo un  umero di persone maggiore della capienza del tavolo.
- AGGIUNGRE CHE QUANDO SI PAGA, LO STATO DEL TAVOLO DIVENTI LIBERO
- SEMBREREBBE CHE NON si possibile aggiungere più volte lo stesso menù all'ordine: da indivudare la causa.
- veirfica disponibilita ingredienti e prenota
- controlla script e eliman i non necessari
- aggiorna query per agginugere pietanza all'ordine con onConflict do update

# Entity/DAO

- Aggiustare classi EntityComposizoneMenu e DBComposizioneMenu perché hanno attributi differrenti?
- Correzione costruttore EntityComposizioneMenu, deve lavorare sull'oggetto DBCompoisizioneMenu

# DIAGRAMMI

-Aggiustare diagramma BCED aggiungendo le classi ricavate dalla traduzione diagramma delle classi --> diagramma relazionale

# LATEX

- Aggiungere spiegazione traduzione diagramma delle classi --> diagramma relazionale
- Aggiungere diagrammi mancanti

# SUPER ACHTUNG

- Nel diagramma di sequenza di "GeneraReport" abbiamo messo l'EntityReport e ReportDAO" ma non nel database.

# COMPILAZIONE

- Correggere i file -bat per avvio su Windows
