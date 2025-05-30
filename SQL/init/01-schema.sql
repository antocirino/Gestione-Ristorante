-- Script di inizializzazione del database
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Creazione del database
CREATE DATABASE IF NOT EXISTS `ristorante` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `ristorante`;

-- Tabella Tavolo
CREATE TABLE IF NOT EXISTS `tavolo` (
  `id_tavolo` INT NOT NULL AUTO_INCREMENT,
  `numero_tavolo` INT NOT NULL,
  `max_posti` INT NOT NULL,
  `stato` ENUM('libero', 'occupato') NOT NULL DEFAULT 'libero',
  PRIMARY KEY (`id_tavolo`),
  UNIQUE KEY `unique_numero_tavolo` (`numero_tavolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Ingrediente
CREATE TABLE IF NOT EXISTS `ingrediente` (
  `id_ingrediente` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `quantita_disponibile` FLOAT NOT NULL,
  `unita_misura` VARCHAR(20) NOT NULL,
  `soglia_riordino` FLOAT NOT NULL,
  PRIMARY KEY (`id_ingrediente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Categoria Pietanza
CREATE TABLE IF NOT EXISTS `categoria_pietanza` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `unique_nome_categoria` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Pietanza
CREATE TABLE IF NOT EXISTS `pietanza` (
  `id_pietanza` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `prezzo` DECIMAL(10,2) NOT NULL,
  `id_categoria` INT NOT NULL,
  PRIMARY KEY (`id_pietanza`),
  FOREIGN KEY (`id_categoria`) REFERENCES `categoria_pietanza` (`id_categoria`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Ricetta (relazione tra pietanza e ingredienti)
CREATE TABLE IF NOT EXISTS `ricetta` (
  `id_pietanza` INT NOT NULL,
  `id_ingrediente` INT NOT NULL,
  `quantita` FLOAT NOT NULL,
  PRIMARY KEY (`id_pietanza`, `id_ingrediente`),
  FOREIGN KEY (`id_pietanza`) REFERENCES `pietanza` (`id_pietanza`) ON DELETE CASCADE,
  FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id_ingrediente`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Menu Fisso
CREATE TABLE IF NOT EXISTS `menu_fisso` (
  `id_menu` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `prezzo` DECIMAL(10,2) NOT NULL,
  `descrizione` TEXT,
  PRIMARY KEY (`id_menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Composizione Menu Fisso
CREATE TABLE IF NOT EXISTS `composizione_menu` (
  `id_menu` INT NOT NULL,
  `id_pietanza` INT NOT NULL,
  PRIMARY KEY (`id_menu`, `id_pietanza`),
  FOREIGN KEY (`id_menu`) REFERENCES `menu_fisso` (`id_menu`) ON DELETE CASCADE,
  FOREIGN KEY (`id_pietanza`) REFERENCES `pietanza` (`id_pietanza`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Ordine
CREATE TABLE IF NOT EXISTS `ordine` (
  `id_ordine` INT NOT NULL AUTO_INCREMENT,
  `id_tavolo` INT NOT NULL,
  `num_persone` INT NOT NULL,
  `data_ordine` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stato` ENUM('in_attesa', 'in_preparazione', 'pronto', 'consegnato', 'pagato') NOT NULL DEFAULT 'in_attesa',
  PRIMARY KEY (`id_ordine`),
  FOREIGN KEY (`id_tavolo`) REFERENCES `tavolo` (`id_tavolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Dettaglio Ordine (per pietanze singole)
CREATE TABLE IF NOT EXISTS `dettaglio_ordine_pietanza` (
  `id_dettaglio` INT NOT NULL AUTO_INCREMENT,
  `id_ordine` INT NOT NULL,
  `id_pietanza` INT NOT NULL,
  `quantita` INT NOT NULL DEFAULT 1,
  `note` TEXT,
  PRIMARY KEY (`id_dettaglio`),
  FOREIGN KEY (`id_ordine`) REFERENCES `ordine` (`id_ordine`) ON DELETE CASCADE,
  FOREIGN KEY (`id_pietanza`) REFERENCES `pietanza` (`id_pietanza`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Dettaglio Ordine per Menu Fissi
CREATE TABLE IF NOT EXISTS `dettaglio_ordine_menu` (
  `id_dettaglio` INT NOT NULL AUTO_INCREMENT,
  `id_ordine` INT NOT NULL,
  `id_menu` INT NOT NULL,
  `quantita` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_dettaglio`),
  FOREIGN KEY (`id_ordine`) REFERENCES `ordine` (`id_ordine`) ON DELETE CASCADE,
  FOREIGN KEY (`id_menu`) REFERENCES `menu_fisso` (`id_menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Utente (per camerieri, cuochi, cassieri, direttore)
CREATE TABLE IF NOT EXISTS `utente` (
  `id_utente` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `nome` VARCHAR(100) NOT NULL,
  `cognome` VARCHAR(100) NOT NULL,
  `ruolo` ENUM('cameriere', 'cuoco', 'cassiere', 'direttore') NOT NULL,
  PRIMARY KEY (`id_utente`),
  UNIQUE KEY `unique_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella Configurazione (per parametri del sistema come costo coperto)
CREATE TABLE IF NOT EXISTS `configurazione` (
  `chiave` VARCHAR(50) NOT NULL,
  `valore` VARCHAR(255) NOT NULL,
  `descrizione` TEXT,
  PRIMARY KEY (`chiave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inserimento configurazione iniziale
INSERT INTO `configurazione` (`chiave`, `valore`, `descrizione`) VALUES
('costo_coperto', '2.00', 'Costo del coperto per persona');

-- Inserimento categorie pietanze
INSERT INTO `categoria_pietanza` (`nome`) VALUES
('Antipasto'),
('Primo'),
('Secondo'),
('Contorno'),
('Frutta'),
('Dolce'),
('Bevanda');

-- Dati di esempio per i tavoli
INSERT INTO `tavolo` (`numero_tavolo`, `max_posti`, `stato`) VALUES
(1, 4, 'libero'),
(2, 6, 'libero'),
(3, 2, 'libero'),
(4, 8, 'libero'),
(5, 4, 'libero'),
(6, 6, 'libero');

-- Dati di esempio per gli utenti
INSERT INTO `utente` (`username`, `password`, `nome`, `cognome`, `ruolo`) VALUES
('cameriere1', 'password', 'Mario', 'Rossi', 'cameriere'),
('cuoco1', 'password', 'Chef', 'Antonino', 'cuoco'),
('cassiere1', 'password', 'Giulia', 'Verdi', 'cassiere'),
('direttore1', 'password', 'Paolo', 'Bianchi', 'direttore');

SET FOREIGN_KEY_CHECKS = 1;
