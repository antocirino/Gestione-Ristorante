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

#Pulisi dati qui 

# Dati casuali per le pietanze
nomi_antipasti = [
    "Bruschetta pomodoro e basilico", "Antipasto misto", "Burrata con pomodorini",
    "Carpaccio di manzo","Insalata caprese", "Insalata di mare"
]

nomi_primi = [
    "Risotto ai funghi porcini", "Lasagne alla bolognese",
    "Tagliatelle al ragu", "Penne all arrabbiata", "Ravioli ricotta e spinaci", 
    "Orecchiette alle cime di rapa", "Fettuccine Alfredo", "Linguine allo scoglio"
]

nomi_secondi = [
    "Tagliata di manzo","Filetto di branzino", "Cotoletta alla milanese", 
    "Scaloppine al limone", "Polpo alla griglia", "Arrosto di vitello",
    "Ossobuco alla milanese", "Salsiccia e friarielli", "Frittura di pesce",
    "Parmigiana di melanzane"
]

nomi_contorni = [
    "Patate al forno", "Verdure grigliate", "Insalata mista",
    "Funghi trifolati","Patatine fritte", "Friarielli", "Melanzane a funghetto", 
    "Pure di patate"
]

nomi_dolci = [
    "Tiramisu", "Panna cotta", "Cannolo siciliano",
    "Baba al rum", "Sfogliatella", "Crostata di frutta", 
    "Torta al cioccolato", "Tagliata di frutta",
]

nomi_bevande = [
    "Acqua naturale", "Acqua frizzante", "Coca Cola",
    "Vino rosso", "Vino bianco",
    "Birra alla spina", "Caffe", "Amaro", 
    "Limoncello", "Grappa", "Prosecco", 
]


# Riempimento del dizionario
ingredient_list = [
    ("Pomodoro", "kg", 6.0, 10.0),
    ("Farina", "kg", 20.0, 5.0),
    ("Sale", "kg", 2.0, 1.0),
    ("Olio d oliva", "litri", 10.0, 2.0),
    ("Aglio", "kg", 3.0, 0.5),
    ("Basilico", "mazzetti", 20.0, 5.0),
    ("Mozzarella", "kg", 15.0, 3.0),
    ("Parmigiano", "kg", 10.0, 2.0),
    ("Pecorino", "kg", 8.0, 1.5),
    ("Guanciale", "kg", 5.0, 1.0),
    ("Pancetta", "kg", 5.0, 1.0),
    ("Uova", "unita", 100.0, 20.0),
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
    ("Caffe", "kg", 3.0, 0.5),
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
    ("Pinoli", "kg", 2.0, 0.5),
    ("Frutta fresca", "kg", 20.0, 2.0),
    ("Acqua naturale", "litri", 50.0, 10.0),
    ("Acqua frizzante", "litri", 50.0, 10.0),
    ("Coca Cola", "litri", 30.0, 5.0),
    ("Prosecco", "litri", 30.0, 5.0),
    ("Limoncello", "litri", 15.0, 3.0),
    ("Grappa", "litri", 10.0, 2.0),
    ("Birra alla spina", "litri", 60.0, 15.0),
    ("Amaro", "litri", 10.0, 2.0)
]
# Dati per gli ingredienti - mappa con nome come chiave e dizionario con id, quantità, unità e soglia
ingredienti_comuni = {}

for i, (nome, unita, quantita, soglia) in enumerate(ingredient_list, 1):
    ingredienti_comuni[nome] = {
        "id": i,
        "quantita_disponibile": quantita,
        "unita_misura": unita,
        "soglia_riordino": soglia
    }

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
    for nome, info in ingredienti_comuni.items():
        values.append(f"('{nome}', {info['quantita_disponibile']}, '{info['unita_misura']}', {info['soglia_riordino']})")
    
    sql += ",\n".join(values) + ";\n\n"
    return sql

def genera_dati_pietanze():
    """Genera dati per la tabella pietanza"""
    sql = "-- Dati generati per la tabella pietanza\n"
    sql += "INSERT INTO `pietanza` (`nome`, `prezzo`, `id_categoria`, `disponibile`) VALUES\n"
    
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
        values.append(f"('{nome}', {prezzo}, 1, 1)")
    
    # Categoria 2: Primi
    for nome in nomi_primi:
        nome = nome.replace("'", " ")  
        prezzo = round(random.uniform(9.0, 14.0), 2)
        values.append(f"('{nome}', {prezzo}, 2, 1)")
    
    # Categoria 3: Secondi
    for nome in nomi_secondi:
        nome = nome.replace("'", " ")  
        prezzo = round(random.uniform(14.0, 22.0), 2)
        values.append(f"('{nome}', {prezzo}, 3, 1)")
    
    # Categoria 4: Contorni
    for nome in nomi_contorni:
        nome = nome.replace("'", " ")
        prezzo = round(random.uniform(4.0, 7.0), 2)
        values.append(f"('{nome}', {prezzo}, 4, 1)")
    
    # Categoria 6: Dolci
    for nome in nomi_dolci:
        nome = nome.replace("'", " ")
        prezzo = round(random.uniform(5.0, 8.0), 2)
        values.append(f"('{nome}', {prezzo}, 6, 1)")
    
    # Categoria 7: Bevande
    for nome in nomi_bevande:
        nome = nome.replace("'", " ")
        prezzo = round(random.uniform(2.0, 5.0), 2)
        values.append(f"('{nome}', {prezzo}, 7, 1)")
    
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
        "Dolci": {},
        "Bevande": {}
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
        ("Menu Degustazione", 35.00, "Un percorso gastronomico che vi fara scoprire i sapori della nostra tradizione"),
        ("Menu di Terra", 30.00, "Una selezione di piatti a base di carne e prodotti dell orto"),
        ("Menu di Mare", 40.00, "I migliori sapori del mare mediterraneo"),
        ("Menu Vegetariano", 25.00, "Piatti gustosi preparati solo con ingredienti vegetali"),
        ("Menu Bambini", 15.00, "Piatti semplici adatti ai piu piccoli")
    ]
    
    values_menu = []
    for nome, prezzo, descrizione in menu_fissi:
        values_menu.append(f"('{nome}', {prezzo}, '{descrizione}')")
    
    sql_menu += ",\n".join(values_menu) + ";\n\n"
    
    # Composizione menu
    sql_comp = "-- Dati generati per la tabella composizione_menu\n"
    sql_comp += "INSERT INTO `composizione_menu` (`id_menu`, `id_pietanza`) VALUES\n"
    
    values_comp = []
    
    # Mapping corretto dei piatti dopo le modifiche (spostamento parmigiana negli secondi):
    # Antipasti (ID 1-6):
    # 1=Bruschetta, 2=Antipasto misto, 3=Burrata pomodorini, 
    # 4=Carpaccio, 5=Insalata caprese, 6=Insalata di mare
    
    # Primi (ID 7-14):
    # 7=Risotto funghi, 8=Lasagne, 9=Tagliatelle ragù, 10=Penne arrabbiata,
    # 11=Ravioli ricotta, 12=Orecchiette, 13=Fettuccine, 14=Linguine scoglio
    
    # Secondi (ID 15-24):
    # 15=Tagliata manzo, 16=Filetto branzino, 17=Cotoletta,
    # 18=Scaloppine, 19=Polpo, 20=Arrosto, 21=Ossobuco, 22=Salsiccia, 
    # 23=Frittura, 24=Parmigiana
    
    # Contorni (ID 25-32):
    # 25=Patate forno, 26=Verdure grigliate, 27=Insalata mista,
    # 28=Funghi, 29=Patatine, 30=Friarielli, 31=Melanzane, 32=Purè
    
    # Dolci (ID 33-40):
    # 33=Tiramisù, 34=Panna cotta, 35=Cannolo, 36=Babà, 
    # 37=Sfogliatella, 38=Crostata, 39=Torta cioccolato, 40=Tagliata frutta
    
    # Menu 1: Degustazione (deve contenere 2 primi)
    values_comp.extend([
        "(1, 3)",  # Antipasto: Burrata con pomodorini
        "(1, 7)",  # Primo 1: Risotto ai funghi porcini
        "(1, 9)",  # Primo 2: Tagliatelle al ragù
        "(1, 15)", # Secondo: Tagliata di manzo
        "(1, 25)", # Contorno: Patate al forno
        "(1, 33)", # Dolce: Tiramisù
        "(1, 40)"  # Frutta: Tagliata di frutta
    ])
    
    # Menu 2: di Terra (solo piatti con carne o verdure)
    values_comp.extend([
        "(2, 4)",  # Antipasto: Carpaccio di manzo
        "(2, 9)",  # Primo: Tagliatelle al ragù
        "(2, 20)", # Secondo: Arrosto di vitello
        "(2, 26)", # Contorno: Verdure grigliate
        "(2, 38)", # Dolce: Crostata
        "(2, 40)"  # Frutta: Tagliata di frutta
    ])
    
    # Menu 3: di Mare (solo piatti con pesce o verdure)
    values_comp.extend([
        "(3, 6)",  # Antipasto: Insalata di mare
        "(3, 14)", # Primo: Linguine allo scoglio
        "(3, 16)", # Secondo: Filetto di branzino
        "(3, 27)", # Contorno: Insalata mista
        "(3, 34)", # Dolce: Panna cotta
        "(3, 40)"  # Frutta: Tagliata di frutta
    ])
    
    # Menu 4: Vegetariano (solo piatti vegetariani)
    values_comp.extend([
        "(4, 5)",  # Antipasto: Insalata caprese
        "(4, 11)", # Primo: Ravioli ricotta e spinaci
        "(4, 24)", # Secondo: Parmigiana di melanzane
        "(4, 28)", # Contorno: Funghi trifolati
        "(4, 38)", # Dolce: Crostata di frutta
        "(4, 40)"  # Frutta: Tagliata di frutta
    ])
    
    # Menu 5: Bambini (specifici piatti richiesti)
    values_comp.extend([
        "(5, 1)",  # Antipasto: Bruschetta
        "(5, 10)", # Primo RICHIESTO: Penne all'arrabbiata
        "(5, 17)", # Secondo RICHIESTO: Cotoletta alla milanese
        "(5, 29)", # Contorno: Patatine fritte
        "(5, 39)", # Dolce: Torta al cioccolato
        "(5, 40)"  # Frutta: Tagliata di frutta
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
    
    
    return sql_ordini + sql_dettaglio


def main():
    """Funzione principale"""
    print(f"Generazione dati di esempio per il database del ristorante...")
    print(f"Lo script leggerà lo schema da: {SCHEMA_FILE}")
    print(f"E scriverà i dati generati in: {OUTPUT_FILE}")
        
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
    sql_output += genera_dati_ricette()
    sql_output += genera_dati_menu_fisso()
    # sql_output += genera_dati_ordini(num_ristoranti) --> inizialmente vuoto, non abbiamo ordini
    
    sql_output += "SET FOREIGN_KEY_CHECKS = 1;\n"
    
    # Scrivi il file
    with open(OUTPUT_FILE, 'w') as file:
        file.write(sql_output)
    
    print(f"Generazione dati completata! File salvato in: {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
