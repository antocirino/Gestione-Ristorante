-- Script per l'inserimento di dati di esempio
USE `ristorante`;

-- Inserimento ingredienti
INSERT INTO `ingrediente` (`nome`, `quantita_disponibile`, `unita_misura`, `soglia_riordino`) VALUES
('Farina', 10000, 'g', 2000),
('Uova', 60, 'pz', 10),
('Pomodoro', 5000, 'g', 1000),
('Mozzarella', 3000, 'g', 500),
('Carne macinata', 4000, 'g', 800),
('Basilico', 100, 'g', 20),
('Aglio', 500, 'g', 100),
('Olio extravergine', 5000, 'ml', 1000),
('Sale', 2000, 'g', 500),
('Pepe', 500, 'g', 100),
('Parmigiano', 2000, 'g', 500),
('Patate', 10000, 'g', 2000),
('Zucchine', 3000, 'g', 500),
('Melanzane', 3000, 'g', 500),
('Cipolla', 2000, 'g', 400),
('Carota', 2000, 'g', 400),
('Sedano', 1000, 'g', 200),
('Vino bianco', 3000, 'ml', 500),
('Pesce spada', 5000, 'g', 1000),
('Acqua', 50000, 'ml', 10000),
('Zucchero', 3000, 'g', 500),
('Latte', 5000, 'ml', 1000),
('Burro', 2000, 'g', 400),
('Cioccolato', 2000, 'g', 400),
('Mela', 3000, 'g', 500),
('Pera', 3000, 'g', 500),
('Banana', 3000, 'g', 500),
('Fragola', 2000, 'g', 400),
('Caffè', 2000, 'g', 400);

-- Inserimento pietanze
INSERT INTO `pietanza` (`nome`, `prezzo`, `id_categoria`) VALUES
-- Antipasti
('Bruschetta al pomodoro', 5.00, 1),
('Tagliere di salumi e formaggi', 10.00, 1),
('Carpaccio di manzo', 12.00, 1),

-- Primi
('Spaghetti al pomodoro', 9.00, 2),
('Lasagna alla bolognese', 12.00, 2),
('Risotto ai funghi', 14.00, 2),
('Ravioli ricotta e spinaci', 13.00, 2),

-- Secondi
('Bistecca alla fiorentina', 22.00, 3),
('Pesce spada alla griglia', 18.00, 3),
('Scaloppine al limone', 16.00, 3),
('Pollo arrosto', 15.00, 3),

-- Contorni
('Patate al forno', 5.00, 4),
('Insalata mista', 4.50, 4),
('Verdure grigliate', 6.00, 4),

-- Frutta
('Macedonia di frutta', 5.00, 5),
('Frutta di stagione', 4.00, 5),

-- Dolci
('Tiramisù', 6.00, 6),
('Panna cotta', 5.00, 6),
('Cheesecake', 6.00, 6),

-- Bevande
('Acqua naturale', 2.00, 7),
('Acqua frizzante', 2.00, 7),
('Coca Cola', 3.00, 7),
('Vino della casa (rosso)', 10.00, 7),
('Vino della casa (bianco)', 10.00, 7),
('Caffè', 1.50, 7);

-- Inserimento ricette (relazioni pietanza-ingredienti)
INSERT INTO `ricetta` (`id_pietanza`, `id_ingrediente`, `quantita`) VALUES
-- Bruschetta al pomodoro (id_pietanza=1)
(1, 3, 200),  -- Pomodoro
(1, 6, 10),   -- Basilico
(1, 7, 5),    -- Aglio
(1, 8, 15),   -- Olio extravergine
(1, 9, 3),    -- Sale

-- Spaghetti al pomodoro (id_pietanza=4)
(4, 1, 120),  -- Farina (per la pasta)
(4, 2, 1),    -- Uova (per la pasta)
(4, 3, 150),  -- Pomodoro
(4, 6, 5),    -- Basilico
(4, 8, 10),   -- Olio
(4, 9, 3),    -- Sale

-- Lasagna alla bolognese (id_pietanza=5)
(5, 1, 200),  -- Farina (per la pasta)
(5, 2, 2),    -- Uova (per la pasta)
(5, 3, 150),  -- Pomodoro
(5, 5, 200),  -- Carne macinata
(5, 11, 50),  -- Parmigiano
(5, 15, 30),  -- Cipolla
(5, 16, 30),  -- Carota
(5, 17, 30),  -- Sedano
(5, 9, 3),    -- Sale
(5, 10, 1),   -- Pepe

-- Pesce spada alla griglia (id_pietanza=9)
(9, 19, 200), -- Pesce spada
(9, 8, 10),   -- Olio
(9, 9, 2),    -- Sale
(9, 10, 1);   -- Pepe

-- Menu fissi
INSERT INTO `menu_fisso` (`nome`, `prezzo`, `descrizione`) VALUES
('Menu Vegetariano', 25.00, 'Menu completo vegetariano'),
('Menu Bambini', 15.00, 'Menu adatto ai più piccoli'),
('Menu Pesce', 30.00, 'Menu a base di pesce'),
('Menu di Terra', 28.00, 'Menu con piatti tradizionali di terra');

-- Composizione Menu Fissi
-- Menu Vegetariano
INSERT INTO `composizione_menu` (`id_menu`, `id_pietanza`) VALUES
(1, 1),  -- Bruschetta
(1, 4),  -- Spaghetti al pomodoro
(1, 13), -- Verdure grigliate
(1, 16), -- Frutta di stagione
(1, 17); -- Tiramisù

-- Menu Bambini
INSERT INTO `composizione_menu` (`id_menu`, `id_pietanza`) VALUES
(2, 4),  -- Spaghetti al pomodoro
(2, 11), -- Pollo arrosto
(2, 12), -- Patate al forno
(2, 16), -- Frutta di stagione
(2, 18); -- Panna cotta

-- Menu Pesce
INSERT INTO `composizione_menu` (`id_menu`, `id_pietanza`) VALUES
(3, 3),  -- Carpaccio di manzo
(3, 6),  -- Risotto ai funghi
(3, 9),  -- Pesce spada alla griglia
(3, 13), -- Verdure grigliate
(3, 15), -- Macedonia di frutta
(3, 19); -- Cheesecake

-- Menu di Terra
INSERT INTO `composizione_menu` (`id_menu`, `id_pietanza`) VALUES
(4, 2),  -- Tagliere di salumi
(4, 5),  -- Lasagna alla bolognese
(4, 8),  -- Bistecca alla fiorentina
(4, 12), -- Patate al forno
(4, 16), -- Frutta di stagione
(4, 17); -- Tiramisù
