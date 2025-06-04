package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import javax.swing.Timer; // Import specifico per evitare ambiguità
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import CFG.DBConnection;

// Importa la libreria SVG Salamander
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

/**
 * Schermata per il cuoco che permette di visualizzare gli ordini
 * e cambiarne lo stato (in preparazione, pronto, consegnato)
 */
public class CuocoForm extends JFrame {
    private JPanel mainPanel;
    private JTable ordiniTable;
    private JTable dettagliTable;
    private JButton iniziaPreparazioneButton;
    private JButton segnaCompletatoButton;
    private JButton segnaConsegnatoButton;
    private JButton visualizzaIngredientiButton;
    private JButton indietroButton;

    // Colori e font moderni - coerenti con il design system
    private Color primaryColor = new Color(41, 128, 185);     // Blu principale
    private Color accentColor = new Color(52, 152, 219);      // Blu accent
    private Color textColor = new Color(44, 62, 80);          // Grigio scuro
    private Color lightColor = new Color(236, 240, 241);      // Grigio chiaro
    private Color successColor = new Color(39, 174, 96);      // Verde
    private Color warningColor = new Color(230, 126, 34);     // Arancione
    private Color dangerColor = new Color(192, 57, 43);       // Rosso
    private Color infoColor = new Color(52, 73, 94);          // Grigio scuro per header
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
    private Font regularFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 14);

    // Enums per gli stati degli ordini
    private enum StatoOrdine {
        IN_ATTESA("in_attesa", "In attesa"),
        IN_PREPARAZIONE("in_preparazione", "In preparazione"),
        PRONTO("pronto", "Pronto"),
        CONSEGNATO("consegnato", "Consegnato"),
        PAGATO("pagato", "Pagato");

        private final String codice;
        private final String descrizione;

        StatoOrdine(String codice, String descrizione) {
            this.codice = codice;
            this.descrizione = descrizione;
        }

        public String getCodice() {
            return codice;
        }

        public String getDescrizione() {
            return descrizione;
        }

        public static StatoOrdine fromCodice(String codice) {
            for (StatoOrdine stato : StatoOrdine.values()) {
                if (stato.getCodice().equals(codice)) {
                    return stato;
                }
            }
            return IN_ATTESA; // Default
        }
    }

    public CuocoForm() {
        setTitle("Gestione Ristorante - Cuoco");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(lightColor);

        // Pannello superiore con titolo moderno
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel("GESTIONE CUCINA - CUOCO");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        
        // Aggiunge l'icona SVG al titolo
        ImageIcon svgIcon = loadSVGIcon("restaurant_menu.svg", 32, 32);
        JLabel iconLabel = new JLabel(svgIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(primaryColor);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Pannello centrale diviso in due parti con stile moderno
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        splitPane.setBackground(lightColor);

        // Pannello superiore con la lista degli ordini
        JPanel ordiniPanel = createModernPanel();
        ordiniPanel.setLayout(new BorderLayout(0, 15));
        
        JLabel ordiniTitleLabel = new JLabel("Ordini da Preparare");
        ordiniTitleLabel.setFont(headerFont);
        ordiniTitleLabel.setForeground(textColor);
        ordiniTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        DefaultTableModel ordiniTableModel = new DefaultTableModel(
                new String[] { "ID Ordine", "Tavolo", "Persone", "Ora", "Stato" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordiniTable = new JTable(ordiniTableModel);
        styleTable(ordiniTable);
        ordiniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordiniTable.getColumnModel().getColumn(0).setMaxWidth(80);
        ordiniTable.getColumnModel().getColumn(1).setMaxWidth(80);
        ordiniTable.getColumnModel().getColumn(2).setMaxWidth(80);
        ordiniTable.getColumnModel().getColumn(3).setMaxWidth(100);

        JScrollPane ordiniScrollPane = new JScrollPane(ordiniTable);
        styleScrollPane(ordiniScrollPane);
        
        ordiniPanel.add(ordiniTitleLabel, BorderLayout.NORTH);
        ordiniPanel.add(ordiniScrollPane, BorderLayout.CENTER);

        // Pannello inferiore con i dettagli dell'ordine
        JPanel dettagliPanel = createModernPanel();
        dettagliPanel.setLayout(new BorderLayout(0, 15));
        
        JLabel dettagliTitleLabel = new JLabel("Dettaglio Ordine Selezionato");
        dettagliTitleLabel.setFont(headerFont);
        dettagliTitleLabel.setForeground(textColor);
        dettagliTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        DefaultTableModel dettagliTableModel = new DefaultTableModel(
                new String[] { "Tipo", "Nome", "Quantità", "Note" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dettagliTable = new JTable(dettagliTableModel);
        styleTable(dettagliTable);
        dettagliTable.getColumnModel().getColumn(0).setMaxWidth(100);
        dettagliTable.getColumnModel().getColumn(2).setMaxWidth(80);
        
        JScrollPane dettagliScrollPane = new JScrollPane(dettagliTable);
        styleScrollPane(dettagliScrollPane);
        
        dettagliPanel.add(dettagliTitleLabel, BorderLayout.NORTH);
        dettagliPanel.add(dettagliScrollPane, BorderLayout.CENTER);

        // Pannello pulsanti per la gestione degli ordini
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        buttonsPanel.setBackground(lightColor);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        iniziaPreparazioneButton = createStyledButton("⏱️ Inizia Preparazione", warningColor);
        segnaCompletatoButton = createStyledButton("✅ Segna come Pronto", successColor);
        segnaConsegnatoButton = createStyledButton("🚚 Segna come Consegnato", primaryColor);
        visualizzaIngredientiButton = createStyledButton("🥕 Visualizza Ingredienti", infoColor);

        buttonsPanel.add(iniziaPreparazioneButton);
        buttonsPanel.add(segnaCompletatoButton);
        buttonsPanel.add(segnaConsegnatoButton);
        buttonsPanel.add(visualizzaIngredientiButton);

        // Pannello pulsanti in fondo con stile moderno
        JPanel bottomButtonPanel = new JPanel(new BorderLayout());
        bottomButtonPanel.setBackground(new Color(245, 247, 250));
        bottomButtonPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        indietroButton = createStyledButton("← Indietro", dangerColor);
        
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftButtonPanel.setBackground(new Color(245, 247, 250));
        leftButtonPanel.add(indietroButton);
        
        bottomButtonPanel.add(leftButtonPanel, BorderLayout.WEST);

        // Assemblaggio pannelli
        splitPane.setTopComponent(ordiniPanel);
        splitPane.setBottomComponent(dettagliPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);

        // Event listeners
        ordiniTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                caricaDettagliOrdine();
                aggiornaStatoButton();
            }
        });

        iniziaPreparazioneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiaStatoOrdine(StatoOrdine.IN_PREPARAZIONE);
            }
        });

        segnaCompletatoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiaStatoOrdine(StatoOrdine.PRONTO);
            }
        });

        segnaConsegnatoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cambiaStatoOrdine(StatoOrdine.CONSEGNATO);
            }
        });

        visualizzaIngredientiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizzaIngredienti();
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Chiude la finestra generando un evento di chiusura
                CuocoForm.this.dispatchEvent(new java.awt.event.WindowEvent(
                    CuocoForm.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });

        // Carica i dati iniziali
        caricaOrdini();
        aggiornaStatoButton();

        // Imposta un timer per aggiornare gli ordini ogni 30 secondi
        Timer timer = new Timer(30000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                caricaOrdini();
            }
        });
        timer.start();
    }

    /**
     * Carica gli ordini da preparare
     */
    private void caricaOrdini() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT o.id_ordine, t.id_tavolo, o.num_persone, o.data_ordine, o.stato " +
                    "FROM ordine o " +
                    "JOIN tavolo t ON o.id_tavolo = t.id_tavolo " +
                    "WHERE o.stato IN ('in_attesa', 'in_preparazione', 'pronto') " +
                    "ORDER BY FIELD(o.stato, 'in_attesa', 'in_preparazione', 'pronto'), o.data_ordine";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Salvo la selezione corrente
            int selectedRow = ordiniTable.getSelectedRow();
            int idOrdineSelezionato = -1;
            if (selectedRow != -1) {
                idOrdineSelezionato = Integer.parseInt(ordiniTable.getValueAt(selectedRow, 0).toString());
            }

            // Svuoto la tabella ordini
            DefaultTableModel model = (DefaultTableModel) ordiniTable.getModel();
            model.setRowCount(0);

            // Popolo la tabella ordini
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            int newSelectedRow = -1;
            int rowIndex = 0;

            while (rs.next()) {
                int idOrdine = rs.getInt("id_ordine");
                int idTavolo = rs.getInt("id_tavolo");
                int numPersone = rs.getInt("num_persone");
                java.sql.Timestamp timestamp = rs.getTimestamp("data_ordine");
                String ora = sdf.format(timestamp);
                String stato = rs.getString("stato");
                // Utilizziamo l'id del tavolo al posto del numero
                int numeroTavolo = idTavolo;

                StatoOrdine statoOrdine = StatoOrdine.fromCodice(stato);

                model.addRow(new Object[] {
                        idOrdine,
                        numeroTavolo,
                        numPersone,
                        ora,
                        statoOrdine.getDescrizione()
                });

                // Se è l'ordine che era selezionato prima, lo riselezioniamo
                if (idOrdine == idOrdineSelezionato) {
                    newSelectedRow = rowIndex;
                }

                rowIndex++;
            }

            // Ripristino la selezione se possibile
            if (newSelectedRow != -1) {
                ordiniTable.setRowSelectionInterval(newSelectedRow, newSelectedRow);
            } else if (model.getRowCount() > 0) {
                ordiniTable.setRowSelectionInterval(0, 0);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento degli ordini: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carica i dettagli dell'ordine selezionato
     */
    private void caricaDettagliOrdine() {
        int selectedRow = ordiniTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        int idOrdine = Integer.parseInt(ordiniTable.getValueAt(selectedRow, 0).toString());

        try {
            Connection conn = DBConnection.getConnection();

            // Svuoto la tabella dettagli
            DefaultTableModel model = (DefaultTableModel) dettagliTable.getModel();
            model.setRowCount(0);

            // Carico le pietanze dell'ordine
            String queryPietanze = "SELECT p.nome, dop.quantita " +
                    "FROM dettaglio_ordine_pietanza dop " +
                    "JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                    "WHERE dop.id_ordine = ?";

            PreparedStatement stmtPietanze = conn.prepareStatement(queryPietanze);
            stmtPietanze.setInt(1, idOrdine);
            ResultSet rsPietanze = stmtPietanze.executeQuery();

            while (rsPietanze.next()) {
                String nome = rsPietanze.getString("nome");
                int quantita = rsPietanze.getInt("quantita");

                model.addRow(new Object[] {
                        "Pietanza",
                        nome,
                        quantita,
                        "" // Campo note vuoto
                });
            }

            rsPietanze.close();
            stmtPietanze.close();

            // Carico i menu fissi dell'ordine
            String queryMenu = "SELECT m.nome, dom.quantita " +
                    "FROM dettaglio_ordine_menu dom " +
                    "JOIN menu_fisso m ON dom.id_menu = m.id_menu " +
                    "WHERE dom.id_ordine = ?";

            PreparedStatement stmtMenu = conn.prepareStatement(queryMenu);
            stmtMenu.setInt(1, idOrdine);
            ResultSet rsMenu = stmtMenu.executeQuery();

            while (rsMenu.next()) {
                String nome = rsMenu.getString("nome");
                int quantita = rsMenu.getInt("quantita");

                model.addRow(new Object[] {
                        "Menu Fisso",
                        nome,
                        quantita,
                        ""
                });
            }

            rsMenu.close();
            stmtMenu.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei dettagli dell'ordine: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiorna lo stato dei pulsanti in base allo stato dell'ordine selezionato
     */
    private void aggiornaStatoButton() {
        int selectedRow = ordiniTable.getSelectedRow();

        if (selectedRow == -1) {
            // Nessun ordine selezionato, disabilita tutti i pulsanti
            iniziaPreparazioneButton.setEnabled(false);
            segnaCompletatoButton.setEnabled(false);
            segnaConsegnatoButton.setEnabled(false);
            visualizzaIngredientiButton.setEnabled(false);
            return;
        }

        String stato = ordiniTable.getValueAt(selectedRow, 4).toString();
        visualizzaIngredientiButton.setEnabled(true);

        if (stato.equals(StatoOrdine.IN_ATTESA.getDescrizione())) {
            // Ordine in attesa
            iniziaPreparazioneButton.setEnabled(true);
            segnaCompletatoButton.setEnabled(false);
            segnaConsegnatoButton.setEnabled(false);
        } else if (stato.equals(StatoOrdine.IN_PREPARAZIONE.getDescrizione())) {
            // Ordine in preparazione
            iniziaPreparazioneButton.setEnabled(false);
            segnaCompletatoButton.setEnabled(true);
            segnaConsegnatoButton.setEnabled(false);
        } else if (stato.equals(StatoOrdine.PRONTO.getDescrizione())) {
            // Ordine pronto
            iniziaPreparazioneButton.setEnabled(false);
            segnaCompletatoButton.setEnabled(false);
            segnaConsegnatoButton.setEnabled(true);
        } else {
            // Ordine consegnato o pagato
            iniziaPreparazioneButton.setEnabled(false);
            segnaCompletatoButton.setEnabled(false);
            segnaConsegnatoButton.setEnabled(false);
        }
    }

    /**
     * Cambia lo stato dell'ordine selezionato
     */
    private void cambiaStatoOrdine(StatoOrdine nuovoStato) {
        int selectedRow = ordiniTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        int idOrdine = Integer.parseInt(ordiniTable.getValueAt(selectedRow, 0).toString());

        try {
            Connection conn = DBConnection.getConnection();
            String query = "UPDATE ordine SET stato = ? WHERE id_ordine = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nuovoStato.getCodice());
            stmt.setInt(2, idOrdine);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Aggiorno la tabella
                ordiniTable.setValueAt(nuovoStato.getDescrizione(), selectedRow, 4);
                aggiornaStatoButton();

                JOptionPane.showMessageDialog(this,
                        "Stato dell'ordine aggiornato con successo",
                        "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

                // Se è stato segnato come consegnato, lo rimuovo dalla lista
                if (nuovoStato == StatoOrdine.CONSEGNATO) {
                    caricaOrdini();
                }
            }

            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante l'aggiornamento dello stato dell'ordine: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Visualizza gli ingredienti necessari per preparare l'ordine selezionato
     */
    private void visualizzaIngredienti() {
        int selectedRow = ordiniTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        int idOrdine = Integer.parseInt(ordiniTable.getValueAt(selectedRow, 0).toString());

        try {
            Connection conn = DBConnection.getConnection();

            // Query per ottenere gli ingredienti necessari per le pietanze dell'ordine
            String query = "SELECT i.nome, SUM(r.quantita * dop.quantita) AS quantita_totale, i.unita_misura " +
                    "FROM dettaglio_ordine_pietanza dop " +
                    "JOIN ricetta r ON dop.id_pietanza = r.id_pietanza " +
                    "JOIN ingrediente i ON r.id_ingrediente = i.id_ingrediente " +
                    "WHERE dop.id_ordine = ? " +
                    "GROUP BY i.nome, i.unita_misura " +
                    "ORDER BY i.nome";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idOrdine);
            ResultSet rs = stmt.executeQuery();

            StringBuilder result = new StringBuilder();
            result.append("Ingredienti necessari per l'ordine:\n\n");

            while (rs.next()) {
                String nomeIngrediente = rs.getString("nome");
                double quantita = rs.getDouble("quantita_totale");
                String unitaMisura = rs.getString("unita_misura");

                result.append(String.format("- %s: %.2f %s\n",
                        nomeIngrediente, quantita, unitaMisura));
            }

            rs.close();
            stmt.close();

            // Mostro il risultato
            JTextArea textArea = new JTextArea(result.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Ingredienti per l'Ordine",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il recupero degli ingredienti: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodi di utilità per lo styling moderno

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(regularFont);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(backgroundColor.darker(), 2, true),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effetti hover più evidenti
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(backgroundColor.darker().darker(), 2, true),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(backgroundColor.darker(), 2, true),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(backgroundColor.darker());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(backgroundColor.brighter());
                } else {
                    button.setBackground(backgroundColor);
                }
            }
        });
        
        return button;
    }

    private void styleTable(JTable table) {
        table.setFont(regularFont);
        table.setRowHeight(35);
        table.setBackground(Color.WHITE);
        table.setForeground(textColor);
        table.setSelectionBackground(accentColor);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(smallFont);
        table.getTableHeader().setBackground(infoColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    private JPanel createModernPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    // Metodo per caricare icone SVG
    private ImageIcon loadSVGIcon(String filename, int width, int height) {
        return loadSVGIcon(filename, width, height, Color.WHITE);
    }
    
    // Metodo sovraccaricato per specificare il colore dell'icona
    private ImageIcon loadSVGIcon(String filename, int width, int height, Color color) {
        try {
            // Percorsi possibili per le icone SVG (nell'ordine di priorità)
            String[] possiblePaths = {
                "bin/resources/icons/" + filename,                    // Nel container/dopo compilazione
                "resources/icons/" + filename,                       // Percorso relativo nel container
                "GestioneRistorante/bin/resources/icons/" + filename, // Dalla root progetto
                "GestioneRistorante/src/resources/icons/" + filename, // Sorgente originale
                "src/resources/icons/" + filename                    // Durante sviluppo
            };
            
            java.io.File svgFile = null;
            String usedPath = null;
            
            for (String path : possiblePaths) {
                java.io.File testFile = new java.io.File(path);
                if (testFile.exists()) {
                    svgFile = testFile;
                    usedPath = path;
                    System.out.println("Icona SVG trovata: " + usedPath);
                    break;
                }
            }
            
            // Se non trovato con percorsi diretti, prova con il class loader
            if (svgFile == null || !svgFile.exists()) {
                try {
                    java.net.URL resourceUrl = getClass().getClassLoader().getResource("icons/" + filename);
                    if (resourceUrl == null) {
                        resourceUrl = getClass().getClassLoader().getResource("resources/icons/" + filename);
                    }
                    if (resourceUrl != null) {
                        svgFile = new java.io.File(resourceUrl.toURI());
                        System.out.println("Icona SVG trovata via class loader: " + resourceUrl);
                    }
                } catch (Exception e) {
                    // Ignora e usa fallback
                }
            }
            
            if (svgFile == null || !svgFile.exists()) {
                System.out.println("File SVG non trovato in nessuno dei percorsi: " + filename);
                return createFallbackIcon(filename, width, height, color);
            }
            
            SVGUniverse svgUniverse = new SVGUniverse();
            java.net.URI svgUri = svgFile.toURI();
            SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(svgUri.toURL()));
            
            if (diagram == null) {
                System.out.println("Impossibile caricare il diagramma SVG: " + filename);
                return createFallbackIcon(filename, width, height, color);
            }
            
            // Imposta dimensioni
            diagram.setIgnoringClipHeuristic(true);
            
            // Renderizza SVG come BufferedImage con sfondo trasparente
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Pulisci lo sfondo
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, width, height);
            g2.setComposite(AlphaComposite.SrcOver);
            
            // Imposta il colore dell'SVG
            g2.setColor(color);
            
            // Scala e centra l'SVG
            java.awt.geom.Rectangle2D bounds = diagram.getViewRect();
            double scaleX = (double) width / bounds.getWidth();
            double scaleY = (double) height / bounds.getHeight();
            double scale = Math.min(scaleX, scaleY);
            
            int scaledWidth = (int) (bounds.getWidth() * scale);
            int scaledHeight = (int) (bounds.getHeight() * scale);
            int x = (width - scaledWidth) / 2;
            int y = (height - scaledHeight) / 2;
            
            g2.translate(x, y);
            g2.scale(scale, scale);
            
            diagram.render(g2);
            g2.dispose();
            
            System.out.println("Icona SVG caricata con successo: " + filename);
            return new ImageIcon(image);
            
        } catch (Exception e) {
            System.out.println("Errore nel caricamento SVG " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return createFallbackIcon(filename, width, height, color);
        }
    }
    
    // Metodo per creare icone di fallback
    private ImageIcon createFallbackIcon(String filename, int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Imposta colore e font
        g2.setColor(color);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, Math.min(width, height) - 4));
        
        String icon = getUnicodeIcon(filename);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(icon);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();
        
        g2.drawString(icon, x, y);
        g2.dispose();
        
        return new ImageIcon(image);
    }

    // Metodo per ottenere icone Unicode come fallback
    private String getUnicodeIcon(String filename) {
        if (filename.contains("restaurant_menu")) {
            return "🍽️";
        } else if (filename.contains("person")) {
            return "👤";
        } else if (filename.contains("payment")) {
            return "💳";
        } else if (filename.contains("admin_panel_settings")) {
            return "⚙️";
        } else {
            return "��";
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CuocoForm().setVisible(true);
            }
        });
    }
}
