#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Script per generare dati di esempio per il database del ristorante.
Questo script legge lo schema SQL e genera dati casuali per popolare le tabelle.
"""

import random
import datetime
import os
import json

# Percorsi file
SCHEMA_FILE = os.path.join('..', 'init', '01-schema.sql')
OUTPUT_FILE = os.path.join('..', 'init', '02-sample-data-generated.sql')
RICETTE_FILE = os.path.join('.', 'ricette.json')



# Dati casuali per le pietanze
nomi_antipasti = [
    "Bruschetta pomodoro e basilico", "Carpaccio di manzo", "Antipasto misto",
    "Insalata caprese", "Crostini ai funghi", "Affettati misti", "Olive all ascolana",
    "Insalata di mare", "Parmigiana di melanzane", "Burrata con pomodorini", 
]

nomi_primi = [
    "Spaghetti alla carbonara", "Risotto ai funghi porcini", "Lasagne alla bolognese",
    "Tagliatelle al ragù", "Penne all arrabbiata", "Ravioli ricotta e spinaci", 
    "Linguine allo scoglio", "Gnocchi al pesto", "Orecchiette alle cime di rapa",
    "Bucatini all amatriciana", "Fettuccine Alfredo", "Spaghetti aglio e olio",
]

nomi_secondi = [
    "Tagliata di manzo", "Scaloppine al limone", "Filetto di branzino",
    "Cotoletta alla milanese", "Baccalà alla vicentina", "Bistecca fiorentina",
    "Polpo alla griglia", "Arrosto di vitello", "Ossobuco alla milanese", 
    "Salsiccia e friarielli", "Frittura di pesce", "Spezzatino di manzo",
]

nomi_contorni = [
    "Patate al forno", "Insalata mista", "Verdure grigliate",
    "Friarielli", "Funghi trifolati", "Melanzane a funghetto", 
    "Patatine fritte", "Purè di patate", "Carciofi alla romana"
]

nomi_dolci = [
    "Tiramisù", "Panna cotta", "Cannolo siciliano",
    "Babà al rum", "Sfogliatella", "Profiteroles", 
    "Crostata di frutta", "Torta al cioccolato"
]

nomi_bevande = [
    "Acqua naturale", "Acqua frizzante", "Coca Cola",
    "Aranciata", "Vino rosso della casa", "Vino bianco della casa",
    "Birra alla spina", "Sprite", "Caffè", "Amaro", 
    "Limoncello", "Grappa", "Prosecco", "Vino rosato della casa",
]

# Dati per gli ingredienti
ingredienti_comuni = [
    ("Pomodoro", "kg", 50.0, 10.0),
    ("Farina", "kg", 30.0, 5.0),
    ("Sale", "kg", 5.0, 1.0),
    ("Olio d oliva", "litri", 10.0, 2.0),
    ("Aglio", "kg", 3.0, 0.5),
    ("Basilico", "mazzetti", 20.0, 5.0),
    ("Mozzarella", "kg", 15.0, 3.0),
    ("Parmigiano", "kg", 10.0, 2.0),
    ("Pecorino", "kg", 8.0, 1.5),
    ("Guanciale", "kg", 5.0, 1.0),
    ("Pancetta", "kg", 5.0, 1.0),
    ("Uova", "unità", 100.0, 20.0),
    ("Pepe nero", "kg", 2.0, 0.3),
    ("Peperoncino", "kg", 1.0, 0.2),
    ("Funghi porcini", "kg", 8.0, 1.5),
    ("Riso Carnaroli", "kg", 15.0, 3.0),
    ("Pasta all uovo", "kg", 10.0, 2.0),
    ("Pasta di semola", "kg", 20.0, 4.0),
    ("Carne macinata", "kg", 15.0, 3.0),
    ("Sedano", "kg", 5.0, 1.0),
    ("Carote", "kg", 8.0, 1.5),
    ("Cipolla", "kg", 10.0, 2.0),
    ("Vino bianco", "litri", 15.0, 3.0),
    ("Vino rosso", "litri", 15.0, 3.0),
    ("Burro", "kg", 5.0, 1.0),
    ("Latte", "litri", 10.0, 2.0),
    ("Panna", "litri", 5.0, 1.0),
    ("Zucchero", "kg", 8.0, 1.5),
    ("Mascarpone", "kg", 5.0, 1.0),
    ("Caffè", "kg", 3.0, 0.5),
    ("Filetto di manzo", "kg", 10.0, 2.0),
    ("Bistecca", "kg", 15.0, 3.0),
    ("Branzino", "kg", 8.0, 1.5),
    ("Calamari", "kg", 7.0, 1.2),
    ("Gamberi", "kg", 6.0, 1.0),
    ("Melanzane", "kg", 12.0, 2.5),
    ("Zucchine", "kg", 12.0, 2.5),
    ("Patate", "kg", 25.0, 5.0),
    ("Insalata", "kg", 10.0, 2.0),
    ("Pomodorini", "kg", 10.0, 2.0),
    ("Rucola", "kg", 5.0, 1.0),
    ("Peperoni", "kg", 10.0, 2.0),
    ("Fagiolini", "kg", 8.0, 1.5),
    ("Cavolfiore", "kg", 6.0, 1.0),
    ("Broccoli", "kg", 6.0, 1.0),
    ("Carciofi", "kg", 5.0, 1.0),
    ("Frutta di stagione", "kg", 20.0, 4.0),
    ("Gelato", "kg", 10.0, 2.0),
    ("Cioccolato", "kg", 5.0, 1.0),
    ("Noci", "kg", 3.0, 0.5),
    ("Mandorle", "kg", 3.0, 0.5),
    ("Pistacchi", "kg", 3.0, 0.5),
    ("Vaniglia", "kg", 1.0, 0.2),
    ("Limoni", "kg", 10.0, 2.0),
    ("Arance", "kg", 10.0, 2.0),
    ("Cozze", "kg", 8.0, 1.5),
    ("Vongole", "kg", 8.0, 1.5),
    ("Sgombro", "kg", 6.0, 1.0),
    ("Tonno fresco", "kg", 7.0, 1.2),
    ("Salmone", "kg", 7.0, 1.2),
    ("Pollo", "kg", 15.0, 3.0),
    ("Coniglio", "kg", 10.0, 2.0),
    ("Stinco di maiale", "kg", 12.0, 2.5),
    ("Salsiccia", "kg", 10.0, 2.0),
    ("Rum", "litri", 5.0, 1.0),
    ("Friarielli", "kg", 8.0, 1.5),
    ("Olive", "kg", 5.0, 1.0),
    ("Pinoli", "kg", 2.0, 0.5)
]

def leggi_schema():
    """Legge lo schema SQL per comprendere le tabelle"""
    with open(SCHEMA_FILE, 'r') as file:
        return file.read()

def genera_dati_categorie():
    """Genera dati per la tabella categoria_pietanza"""
    sql = "-- Dati generati per la tabella categoria_pietanza\n"
    sql += "INSERT INTO `categoria_pietanza` (`nome`) VALUES\n"
    
    categorie = [
        "Antipasti", 
        "Primi", 
        "Secondi", 
        "Contorni", 
        "Insalate", 
        "Dolci", 
        "Bevande"
    ]
    
    values = []
    for categoria in categorie:
        values.append(f"('{categoria}')")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def genera_dati_ristorante(num_ristoranti=1):
    """Genera dati per la tabella ristorante"""
    sql = "-- Dati generati per la tabella ristorante\n"
    sql += "INSERT INTO `ristorante` (`nome`, `numero_tavoli`, `costo_coperto`) VALUES\n"
    
    values = []
    for _ in range(num_ristoranti):
        nome = "La Dolce Vita"
        numero_tavoli = 15 
        costo_coperto = 2.5
        values.append(f"('{nome}', {numero_tavoli}, {costo_coperto})")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def genera_dati_tavoli(num_ristoranti=1):
    """Genera dati per la tabella tavolo"""
    sql = "-- Dati generati per la tabella tavolo\n"
    sql += "INSERT INTO `tavolo` (`max_posti`, `stato`, `id_ristorante`) VALUES\n"
    
    values = []
    for id_ristorante in range(1, num_ristoranti + 1):
        num_tavoli = 15
        for _ in range(num_tavoli):
            max_posti = random.choice([2, 2, 4, 4, 4, 6, 6, 8])
            stato = 'libero'
            values.append(f"({max_posti}, '{stato}', {id_ristorante})")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def genera_dati_ingredienti():
    """Genera dati per la tabella ingrediente"""
    sql = "-- Dati generati per la tabella ingrediente\n"
    sql += "INSERT INTO `ingrediente` (`nome`, `quantita_disponibile`, `unita_misura`, `soglia_riordino`) VALUES\n"
    
    values = []
    for nome, unita_misura, quantita, soglia in ingredienti_comuni:
        values.append(f"('{nome}', {quantita}, '{unita_misura}', {soglia})")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def genera_dati_pietanze():
    """Genera dati per la tabella pietanza"""
    sql = "-- Dati generati per la tabella pietanza\n"
    sql += "INSERT INTO `pietanza` (`nome`, `prezzo`, `id_categoria`) VALUES\n"
    
    values = []
    
    # Mappa delle categorie alle loro ID nel database
    # Nota: Gli ID corrispondono alle posizioni delle categorie inserite nella funzione genera_dati_categorie()
    # Categoria 1 = Antipasti
    # Categoria 2 = Primi
    # Categoria 3 = Secondi
    # Categoria 4 = Contorni
    # Categoria 5 = Insalate
    # Categoria 6 = Dolci
    # Categoria 7 = Bevande
    
    # Categoria 1: Antipasti
    for nome in nomi_antipasti:
        nome = nome.replace("'", " ")  
        prezzo = round(random.uniform(6.0, 12.0), 2)
        values.append(f"('{nome}', {prezzo}, 1)")
    
    # Categoria 2: Primi
    for nome in nomi_primi:
        nome = nome.replace("'", " ")  
        prezzo = round(random.uniform(9.0, 14.0), 2)
        values.append(f"('{nome}', {prezzo}, 2)")
    
    # Categoria 3: Secondi
    for nome in nomi_secondi:
        nome = nome.replace("'", " ")  
        prezzo = round(random.uniform(14.0, 22.0), 2)
        values.append(f"('{nome}', {prezzo}, 3)")
    
    # Categoria 4: Contorni
    for nome in nomi_contorni:
        nome = nome.replace("'", " ")
        prezzo = round(random.uniform(4.0, 7.0), 2)
        values.append(f"('{nome}', {prezzo}, 4)")
    
    # Categoria 6: Dolci
    for nome in nomi_dolci:
        nome = nome.replace("'", " ")
        prezzo = round(random.uniform(5.0, 8.0), 2)
        values.append(f"('{nome}', {prezzo}, 6)")
    
    # Categoria 7: Bevande
    for nome in nomi_bevande:
        nome = nome.replace("'", " ")
        prezzo = round(random.uniform(2.0, 5.0), 2)
        values.append(f"('{nome}', {prezzo}, 7)")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql


def genera_dati_menu(num_ristoranti=1):
    """Genera dati per la tabella menu"""
    sql = "-- Dati generati per la tabella menu\n"
    sql += "INSERT INTO `menu` (`id_ristorante`, `id_pietanza`, `disponibile`) VALUES\n"
    
    values = []
    for id_ristorante in range(1, num_ristoranti + 1):
        num_pietanze = len(nomi_antipasti + nomi_primi + nomi_secondi + nomi_contorni + nomi_dolci + nomi_bevande)
        for id_pietanza in range(1, num_pietanze + 1):
            disponibile = random.choice([1, 1, 1, 1, 1, 0])  # 5/6 di probabilità che sia disponibile
            values.append(f"({id_ristorante}, {id_pietanza}, {disponibile})")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def genera_dati_ricette():
    """Genera dati per la tabella ricetta e ricetta_ingrediente"""
    
    # Carichiamo i dati delle ricette dal file JSON
    try:
        with open(RICETTE_FILE, 'r') as file:
            ricette_data = json.load(file)
    except Exception as e:
        print(f"Errore nel caricamento del file delle ricette: {e}")
        return ""
    
    sql_ricette = "-- Dati generati per la tabella ricetta\n"
    sql_ricette += "INSERT INTO `ricetta` (`nome`, `descrizione`, `id_pietanza`, `tempo_preparazione`, `istruzioni`) VALUES\n"
    
    values_ricette = []
    ricette_mappings = {}  # per mappare nome ricetta -> id ricetta
    id_ricetta = 1
    
    # Mappa delle categorie alle loro ID nel database
    # Queste ID corrispondono alle categorie generate in genera_dati_categorie()
    cat_map = {
        "Antipasti": 1,  # Categoria 1 = Antipasti
        "Primi": 2,      # Categoria 2 = Primi
        "Secondi": 3,    # Categoria 3 = Secondi
        "Contorni": 4,   # Categoria 4 = Contorni
        "Dolci": 6       # Categoria 6 = Dolci (la 5 è per Insalate)
    }
    
    # Genera un mapping di nomi pietanze -> id_pietanza
    pietanza_index = {
        "Antipasti": {},
        "Primi": {},
        "Secondi": {},
        "Contorni": {},
        "Dolci": {}
    }
    
    # Prendiamo gli id delle pietanze basate sui nomi in ogni categoria
    offset = 1
    for categoria, piatti in ricette_data.items():
        for i, nome_piatto in enumerate(piatti.keys()):
            pietanza_index[categoria][nome_piatto] = offset + i
        offset += len(pietanza_index[categoria])
    
    # Generiamo le ricette per ogni categoria
    for categoria, piatti in ricette_data.items():
        for nome_piatto, dettagli in piatti.items():
            id_pietanza = pietanza_index[categoria][nome_piatto]
            descrizione = dettagli["descrizione"].replace("'", " ")
            tempo = dettagli["tempo_preparazione"]
            istruzioni = dettagli["istruzioni"].replace("'", " ")
            
            values_ricette.append(f"('{nome_piatto}', '{descrizione}', {id_pietanza}, {tempo}, '{istruzioni}')")
            ricette_mappings[nome_piatto] = id_ricetta
            id_ricetta += 1
    
    sql_ricette += ",\n".join(values_ricette) + ";\n\n"
    
    # Generiamo le relazioni ricetta-ingredienti
    sql_ri = "-- Dati generati per la tabella ricetta_ingrediente\n"
    sql_ri += "INSERT INTO `ricetta_ingrediente` (`id_ricetta`, `id_ingrediente`, `quantita`) VALUES\n"
    
    values_ri = []
    
    # Per ogni ricetta, aggiungiamo gli ingredienti
    for categoria, piatti in ricette_data.items():
        for nome_piatto, dettagli in piatti.items():
            id_ricetta = ricette_mappings[nome_piatto]
            
            for ingrediente in dettagli["ingredienti"]:
                id_ingrediente = ingrediente["id"]
                quantita = ingrediente["quantita"]
                values_ri.append(f"({id_ricetta}, {id_ingrediente}, {quantita})")
    
    sql_ri += ",\n".join(values_ri) + ";\n\n"
    
    return sql_ricette + sql_ri

def genera_dati_menu_fisso():
    """Genera dati per la tabella menu_fisso e composizione_menu"""
    sql_menu = "-- Dati generati per la tabella menu_fisso\n"
    sql_menu += "INSERT INTO `menu_fisso` (`nome`, `prezzo`, `descrizione`) VALUES\n"
    
    menu_fissi = [
        ("Menu Degustazione", 35.00, "Un percorso gastronomico che vi farà scoprire i sapori della nostra tradizione"),
        ("Menu di Terra", 30.00, "Una selezione di piatti a base di carne e prodotti dell'orto"),
        ("Menu di Mare", 40.00, "I migliori sapori del mare mediterraneo"),
        ("Menu Vegetariano", 25.00, "Piatti gustosi preparati solo con ingredienti vegetali"),
        ("Menu Bambini", 15.00, "Piatti semplici adatti ai più piccoli")
    ]
    
    values_menu = []
    for nome, prezzo, descrizione in menu_fissi:
        values_menu.append(f"('{nome}', {prezzo}, '{descrizione}')")
    
    sql_menu += ",\n".join(values_menu) + ";\n\n"
    
    # Composizione menu
    sql_comp = "-- Dati generati per la tabella composizione_menu\n"
    sql_comp += "INSERT INTO `composizione_menu` (`id_menu`, `id_pietanza`) VALUES\n"
    
    values_comp = []
    
    # Menu 1: Degustazione
    values_comp.extend([
        "(1, 1)",  # Un antipasto
        "(1, 11)", # Un primo
        "(1, 21)", # Un secondo
        "(1, 31)", # Un contorno
        "(1, 41)"  # Un dolce
    ])
    
    # Menu 2: di Terra
    values_comp.extend([
        "(2, 2)",  # Antipasto terra
        "(2, 12)", # Primo terra
        "(2, 22)", # Secondo terra
        "(2, 32)", # Contorno
        "(2, 42)"  # Dolce
    ])
    
    # Menu 3: di Mare
    values_comp.extend([
        "(3, 8)",  # Antipasto mare
        "(3, 17)", # Primo mare
        "(3, 23)", # Secondo mare
        "(3, 33)", # Contorno
        "(3, 43)"  # Dolce
    ])
    
    # Menu 4: Vegetariano
    values_comp.extend([
        "(4, 4)",  # Antipasto veg
        "(4, 16)", # Primo veg
        "(4, 31)", # Contorno 1
        "(4, 33)", # Contorno 2
        "(4, 44)"  # Dolce
    ])
    
    # Menu 5: Bambini
    values_comp.extend([
        "(5, 10)", # Antipasto semplice
        "(5, 15)", # Pasta semplice
        "(5, 36)", # Patatine
        "(5, 45)"  # Gelato
    ])
    
    sql_comp += ",\n".join(values_comp) + ";\n\n"
    
    return sql_menu + sql_comp

def genera_dati_ordini(num_ristoranti=1):
    """Genera dati per la tabella ordine e dettagli ordine"""
    now = datetime.datetime.now()
    
    sql_ordini = "-- Dati generati per la tabella ordine\n"
    sql_ordini += "INSERT INTO `ordine` (`id_tavolo`, `num_persone`, `data_ordine`, `stato`, `id_ristorante`) VALUES\n"
    
    values_ordini = []
    
    # Generiamo alcuni ordini per ogni ristorante
    for id_ristorante in range(1, num_ristoranti + 1):
        num_ordini = random.randint(5, 15)
        for i in range(num_ordini):
            id_tavolo = random.randint(1, 10)  # Assumiamo che ci siano almeno 10 tavoli
            num_persone = random.randint(1, 8)
            
            # Data casuale nelle ultime 12 ore
            minuti_fa = random.randint(0, 12 * 60)
            data_ordine = now - datetime.timedelta(minutes=minuti_fa)
            data_str = data_ordine.strftime("%Y-%m-%d %H:%M:%S")
            
            # Stato basato sul tempo trascorso
            if minuti_fa < 15:
                stato = "in_attesa"
            elif minuti_fa < 45:
                stato = "in_preparazione"
            elif minuti_fa < 75:
                stato = "pronto"
            elif minuti_fa < 90:
                stato = "consegnato"
            else:
                stato = "pagato"
            
            values_ordini.append(f"({id_tavolo}, {num_persone}, '{data_str}', '{stato}', {id_ristorante})")
    
    sql_ordini += ",\n".join(values_ordini) + ";\n\n"
    
    sql_dettaglio = "-- Dati generati per la tabella dettaglio_ordine_pietanza\n"
    sql_dettaglio += "INSERT INTO `dettaglio_ordine_pietanza` (`id_ordine`, `id_pietanza`, `quantita`) VALUES\n"
    
    values_dettaglio = []
    
    # Per ogni ordine, aggiungiamo alcune pietanze
    num_ordini = len(values_ordini)
    for id_ordine in range(1, num_ordini + 1):
        num_pietanze = random.randint(2, 8)  # Numero di pietanze diverse nell'ordine
        pietanze_selezionate = random.sample(range(1, 60), num_pietanze)  # Assumiamo che ci siano fino a 60 pietanze
        
        for id_pietanza in pietanze_selezionate:
            quantita = random.randint(1, 3)
            values_dettaglio.append(f"({id_ordine}, {id_pietanza}, {quantita})")
    
    sql_dettaglio += ",\n".join(values_dettaglio) + ";\n\n"
    
    # Aggiungiamo anche alcuni ordini di menu fissi
    sql_dettaglio_menu = "-- Dati generati per la tabella dettaglio_ordine_menu\n"
    sql_dettaglio_menu += "INSERT INTO `dettaglio_ordine_menu` (`id_ordine`, `id_menu`, `quantita`) VALUES\n"
    
    values_dettaglio_menu = []
    
    # Solo per alcuni ordini (30% circa), aggiungiamo menu fissi
    for id_ordine in range(1, num_ordini + 1):
        if random.random() < 0.3:
            id_menu = random.randint(1, 5)  # Assumiamo che ci siano 5 menu fissi
            quantita = random.randint(1, 2)
            values_dettaglio_menu.append(f"({id_ordine}, {id_menu}, {quantita})")
    
    if values_dettaglio_menu:
        sql_dettaglio_menu += ",\n".join(values_dettaglio_menu) + ";\n\n"
    else:
        sql_dettaglio_menu = ""
    
    return sql_ordini + sql_dettaglio + sql_dettaglio_menu

def genera_dati_utenti():
    """Genera dati per la tabella utente"""
    sql = "-- Dati generati per la tabella utente\n"
    sql += "INSERT INTO `utente` (`username`, `password`, `nome`, `cognome`, `ruolo`) VALUES\n"
    
    utenti = [
        ("cameriere1", "password", "Mario", "Rossi", "cameriere"),
        ("cameriere2", "password", "Giuseppe", "Verdi", "cameriere"),
        ("cameriere3", "password", "Laura", "Bianchi", "cameriere"),
        ("cuoco1", "password", "Antonio", "Ferrari", "cuoco"),
        ("cuoco2", "password", "Giulia", "Romano", "cuoco"),
        ("cassiere1", "password", "Marco", "Esposito", "cassiere"),
        ("direttore1", "password", "Francesca", "Marino", "direttore")
    ]
    
    values = []
    for username, password, nome, cognome, ruolo in utenti:
        values.append(f"('{username}', '{password}', '{nome}', '{cognome}', '{ruolo}')")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def main():
    """Funzione principale"""
    print(f"Generazione dati di esempio per il database del ristorante...")
    print(f"Lo script leggerà lo schema da: {SCHEMA_FILE}")
    print(f"E scriverà i dati generati in: {OUTPUT_FILE}")
    
    schema = leggi_schema()
    
    num_ristoranti = 1  # Possiamo cambiare questo valore per generare più ristoranti
    
    # Generiamo tutti i dati
    sql_output = "-- File generato automaticamente da generate_sample_data.py\n"
    sql_output += f"-- Data generazione: {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n\n"
    sql_output += "SET FOREIGN_KEY_CHECKS = 0;\n\n"
    
    sql_output += genera_dati_ristorante(num_ristoranti)
    sql_output += genera_dati_tavoli(num_ristoranti)
    sql_output += genera_dati_ingredienti()
    sql_output += genera_dati_categorie()
    sql_output += genera_dati_pietanze()
    sql_output += genera_dati_menu(num_ristoranti)
    sql_output += genera_dati_ricette()
    sql_output += genera_dati_menu_fisso()
    sql_output += genera_dati_ordini(num_ristoranti)
    sql_output += genera_dati_utenti()
    
    sql_output += "SET FOREIGN_KEY_CHECKS = 1;\n"
    
    # Scrivi il file
    with open(OUTPUT_FILE, 'w') as file:
        file.write(sql_output)
    
    print(f"Generazione dati completata! File salvato in: {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
