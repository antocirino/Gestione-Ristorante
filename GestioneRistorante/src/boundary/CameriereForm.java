package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DTO.DTOCategoriaPietanza;
import DTO.DTOMenuFisso;
import DTO.DTOPietanza;
import DTO.DTOTavolo;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import control.Controller;

// Importa la libreria SVG Salamander
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

/**
 * Schermata per il cameriere che permette di visualizzare il menu
 * e gestire gli ordini dei tavoli
 */
public class CameriereForm extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel menuPanel;
    private JPanel menuFissoPanel;
    private JPanel ordinePanel;
    private JTable pietanzeTable;
    private JTable menuFissiTable;
    private JTable ordineTable;
    private JComboBox<String> categorieComboBox;
    private JComboBox<String> tavoliComboBox;
    private JButton aggiungiPietanzaButton;
    private JButton aggiungiMenuButton;
    private JButton inviaOrdineButton;
    private JButton eliminaButton;
    private JButton indietroButton;
    private JTextField noteField;
    private JSpinner quantitaSpinner;

    // Colori e font moderni - versioni più contrastate
    private Color primaryColor = new Color(41, 128, 185); // Blu più scuro
    private Color accentColor = new Color(52, 152, 219); // Blu accent
    private Color textColor = new Color(44, 62, 80); // Grigio scuro
    private Color lightColor = new Color(236, 240, 241); // Grigio chiaro
    private Color successColor = new Color(39, 174, 96); // Verde più vivido
    private Color warningColor = new Color(230, 126, 34); // Arancione più vivido
    private Color dangerColor = new Color(192, 57, 43); // Rosso più vivido
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
    private Font regularFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 14);

    // Per tenere traccia dell'ordine corrente
    private DefaultTableModel ordineTableModel;
    private Map<Integer, Double> prezziPietanze = new HashMap<>();
    private Map<Integer, Double> prezziMenu = new HashMap<>();
    private Map<Integer, String> nomiPietanze = new HashMap<>();
    private Map<Integer, String> nomiMenu = new HashMap<>();

    public CameriereForm() {
        setTitle("Gestione Ristorante - Cameriere");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(lightColor);

        // Pannello superiore con titolo moderno
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("GESTIONE ORDINI - CAMERIERE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);

        // Aggiunge l'icona SVG al titolo
        ImageIcon svgIcon = loadSVGIcon("person.svg", 32, 32);
        JLabel iconLabel = new JLabel(svgIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(primaryColor);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Creazione del tabbed pane con stile moderno
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(regularFont);
        tabbedPane.setBackground(lightColor);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Tab per le pietanze
        menuPanel = createMenuPanel();
        ImageIcon pietanzaIcon = loadSVGIcon("pietanza.svg", 20, 20, textColor);
        tabbedPane.addTab("Pietanze", pietanzaIcon, menuPanel);

        // Tab per i menu fissi
        menuFissoPanel = createMenuFissoPanel();
        ImageIcon menuIcon = loadSVGIcon("menu.svg", 20, 20, textColor);
        tabbedPane.addTab("Menu Fissi", menuIcon, menuFissoPanel);

        // Tab per l'ordine
        ordinePanel = createOrdinePanel();
        ImageIcon ordineIcon = loadSVGIcon("order.svg", 20, 20, textColor);
        tabbedPane.addTab("Ordine Corrente", ordineIcon, ordinePanel);

        // Pannello bottoni in fondo con stile moderno
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        indietroButton = createStyledButton("← Indietro", dangerColor);
        indietroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Chiude la finestra generando un evento di chiusura
                CameriereForm.this.dispatchEvent(new java.awt.event.WindowEvent(
                        CameriereForm.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftButtonPanel.setBackground(new Color(245, 247, 250));
        leftButtonPanel.add(indietroButton);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);

        // Assemblaggio pannelli
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);

        // Inizializzo i dati
        caricaCategorie();
        caricaTavoli();
        caricaPietanze();
        caricaMenuFissi();
    }

    /**
     * Crea il pannello con la lista delle pietanze
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(lightColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello di controllo superiore
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        controlPanel.setBackground(lightColor);

        JLabel categoriaLabel = new JLabel("Filtra per categoria:");
        categoriaLabel.setFont(regularFont);
        categoriaLabel.setForeground(textColor);

        categorieComboBox = new JComboBox<>();
        categorieComboBox.addItem("Tutte le categorie");
        styleComboBox(categorieComboBox);

        controlPanel.add(categoriaLabel);
        controlPanel.add(categorieComboBox);

        // Tabella pietanze con stile moderno
        DefaultTableModel menuTableModel = new DefaultTableModel(
                new String[] { "ID", "Nome", "Categoria", "Prezzo (€)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pietanzeTable = new JTable(menuTableModel);
        styleTable(pietanzeTable);
        pietanzeTable.getColumnModel().getColumn(0).setMaxWidth(60);
        pietanzeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(pietanzeTable);
        styleScrollPane(scrollPane);

        // Pannello inferiore per aggiungere all'ordine
        JPanel addPanel = createModernPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JLabel quantitaLabel = new JLabel("Quantità:");
        quantitaLabel.setFont(regularFont);
        quantitaLabel.setForeground(textColor);

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 20, 1);
        quantitaSpinner = new JSpinner(spinnerModel);
        styleSpinner(quantitaSpinner);

        JLabel noteLabel = new JLabel("Note:");
        noteLabel.setFont(regularFont);
        noteLabel.setForeground(textColor);

        noteField = new JTextField(20);
        styleTextField(noteField);

        aggiungiPietanzaButton = createStyledButton("+ Aggiungi all'ordine", successColor);
        aggiungiPietanzaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aggiungiPietanzaAllOrdine();
            }
        });

        addPanel.add(quantitaLabel);
        addPanel.add(quantitaSpinner);
        addPanel.add(noteLabel);
        addPanel.add(noteField);
        addPanel.add(aggiungiPietanzaButton);

        // Assemblaggio pannello
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addPanel, BorderLayout.SOUTH);

        // Aggiungo listener alla combo box categorie
        categorieComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                caricaPietanze();
            }
        });

        return panel;
    }

    /**
     * Crea il pannello con la lista dei menu fissi
     */
    private JPanel createMenuFissoPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(lightColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabella menu fissi
        DefaultTableModel menuFissiTableModel = new DefaultTableModel(
                new String[] { "ID", "Nome", "Dettagli Menu", "Prezzo (€)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        menuFissiTable = new JTable(menuFissiTableModel);
        styleTable(menuFissiTable);
        menuFissiTable.getColumnModel().getColumn(0).setMaxWidth(60);
        menuFissiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuFissiTable.setRowHeight(150);
        menuFissiTable.getColumnModel().getColumn(2).setPreferredWidth(400);

        // Configurazione per rendere visibile il testo multilinea
        menuFissiTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {

                if (column == 2) {
                    JTextArea textArea = new JTextArea();
                    textArea.setText((value != null) ? value.toString() : "");
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    textArea.setEditable(false);
                    textArea.setFont(smallFont);
                    textArea.setMargin(new Insets(8, 8, 8, 8));

                    if (isSelected) {
                        textArea.setBackground(accentColor);
                        textArea.setForeground(Color.WHITE);
                    } else {
                        textArea.setBackground(Color.WHITE);
                        textArea.setForeground(textColor);
                    }

                    return textArea;
                } else {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setFont(regularFont);
                    if (isSelected) {
                        c.setBackground(accentColor);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(textColor);
                    }
                    return c;
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(menuFissiTable);
        styleScrollPane(scrollPane);

        // Pannello inferiore per aggiungere all'ordine
        JPanel addPanel = createModernPanel();
        addPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JLabel quantitaMenuLabel = new JLabel("Quantità:");
        quantitaMenuLabel.setFont(regularFont);
        quantitaMenuLabel.setForeground(textColor);

        SpinnerModel spinnerModelMenu = new SpinnerNumberModel(1, 1, 20, 1);
        JSpinner quantitaMenuSpinner = new JSpinner(spinnerModelMenu);
        styleSpinner(quantitaMenuSpinner);

        aggiungiMenuButton = createStyledButton("+ Aggiungi menu all'ordine", warningColor);
        aggiungiMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aggiungiMenuAllOrdine(Integer.parseInt(quantitaMenuSpinner.getValue().toString()));
            }
        });

        addPanel.add(quantitaMenuLabel);
        addPanel.add(quantitaMenuSpinner);
        addPanel.add(aggiungiMenuButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea il pannello per la gestione dell'ordine
     */
    private JPanel createOrdinePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(lightColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello superiore con la selezione del tavolo
        JPanel tavoloPanel = createModernPanel();
        tavoloPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        // svg tavolo
        ImageIcon tavoloIcon = loadSVGIcon("table.svg", 20, 20);
        JLabel tavoloIconLabel = new JLabel(tavoloIcon);
        tavoloIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel tavoloLabel = new JLabel("Seleziona tavolo:");
        tavoloLabel.setFont(headerFont);
        tavoloLabel.setForeground(textColor);

        tavoliComboBox = new JComboBox<>();
        styleComboBox(tavoliComboBox);

        tavoloPanel.add(tavoloLabel);
        tavoloPanel.add(tavoliComboBox);

        // Tabella ordine
        ordineTableModel = new DefaultTableModel(
                new String[] { "ID", "Tipo", "Nome", "Quantità", "Prezzo unit. (€)", "Totale (€)", "Note" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordineTable = new JTable(ordineTableModel);
        styleTable(ordineTable);
        ordineTable.getColumnModel().getColumn(0).setMaxWidth(60);
        ordineTable.getColumnModel().getColumn(1).setMaxWidth(80);
        ordineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(ordineTable);
        styleScrollPane(scrollPane);

        // Pannello inferiore con i pulsanti
        JPanel ordineButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        ordineButtonPanel.setBackground(lightColor);

        eliminaButton = createStyledButton("Elimina selezionato", dangerColor);
        inviaOrdineButton = createStyledButton("Invia Ordine", primaryColor);

        // Aggiungi icone SVG ai pulsanti
        ImageIcon eliminaIcon = loadSVGIcon("delete.svg", 20, 20);
        ImageIcon inviaIcon = loadSVGIcon("send.svg", 20, 20);
        eliminaButton.setIcon(eliminaIcon);
        inviaOrdineButton.setIcon(inviaIcon);

        eliminaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminaElementoOrdine();
            }
        });

        inviaOrdineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inviaOrdine();
            }
        });

        ordineButtonPanel.add(eliminaButton);
        ordineButtonPanel.add(inviaOrdineButton);

        panel.add(tavoloPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(ordineButtonPanel, BorderLayout.SOUTH);

        return panel;
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
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effetti hover più evidenti
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(backgroundColor.darker().darker(), 2, true),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(backgroundColor.darker(), 2, true),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)));
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
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(regularFont);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(textColor);
        comboBox.setPreferredSize(new Dimension(250, 40));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(149, 165, 166), 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Migliora l'aspetto del dropdown
        comboBox.setOpaque(true);

        // Personalizza il renderer per le opzioni nel dropdown
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setFont(regularFont);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                if (isSelected) {
                    setBackground(accentColor);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(textColor);
                }

                return this;
            }
        });

        // Aggiungi effetti hover per il ComboBox
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                comboBox.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(accentColor, 2, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboBox.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(149, 165, 166), 2, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
        });
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(regularFont);
        textField.setBackground(Color.WHITE);
        textField.setForeground(textColor);
        textField.setPreferredSize(new Dimension(200, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(149, 165, 166), 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Aggiungi effetti focus per il TextField
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(accentColor, 2, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(149, 165, 166), 2, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
        });
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(regularFont);
        spinner.setPreferredSize(new Dimension(80, 40));
        spinner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(149, 165, 166), 2, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setFont(regularFont);
            textField.setBackground(Color.WHITE);
            textField.setForeground(textColor);
            textField.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            textField.setHorizontalAlignment(JTextField.CENTER);
        }

        // Aggiungi effetti focus per lo Spinner
        spinner.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                spinner.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(accentColor, 2, true),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                spinner.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(149, 165, 166), 2, true),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            }
        });
    }

    private JPanel createModernPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        return panel;
    }

    /**
     * Carica le categorie di pietanze nel combobox
     */
    private void caricaCategorie() {
        try {
            ArrayList<DTOCategoriaPietanza> categorie = Controller.getCategoriePietanze();

            for (DTOCategoriaPietanza categoria : categorie) {
                categorieComboBox.addItem(categoria.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento delle categorie: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carica i tavoli liberi nel combobox
     */
    private void caricaTavoli() {
        try {

            List<DTOTavolo> tavoli = Controller.getAllTavoli();

            tavoliComboBox.removeAllItems();
            for (DTOTavolo tavolo : tavoli) {
                if (!tavolo.isOccupato()) {
                    int idTavolo = tavolo.getIdTavolo();
                    int maxPosti = tavolo.getMaxPosti();
                    tavoliComboBox.addItem(
                            idTavolo + " - Tavolo " + " (max " + maxPosti + " posti)");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei tavoli: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carica le pietanze nella tabella
     */
    private void caricaPietanze() {
        try {
            String categoriaSelezionata = (String) categorieComboBox.getSelectedItem();

            List<DTOPietanza> pietanze;
            ArrayList<DTOCategoriaPietanza> categorie = Controller.getCategoriePietanze();
            Map<String, Integer> categorieInverse = new HashMap<>();

            // Creiamo una mappa inversa da nome categoria a ID categoria
            for (DTOCategoriaPietanza categoria : categorie) {
                categorieInverse.put(categoria.getNome(), categoria.getIdCategoria());
            }

            if (categoriaSelezionata == null || categoriaSelezionata.equals("Tutte le categorie")) {
                pietanze = Controller.getAllPietanze();
            } else {
                Integer idCategoria = categorieInverse.get(categoriaSelezionata);
                if (idCategoria != null) {
                    pietanze = Controller.getPietanzeByCategoria(idCategoria);
                } else {
                    pietanze = new ArrayList<>();
                }
            }
            
            // Ordina le pietanze per categoria e poi per nome all'interno della stessa categoria
            java.util.Collections.sort(pietanze, new java.util.Comparator<DTOPietanza>() {
                @Override
                public int compare(DTOPietanza p1, DTOPietanza p2) {
                    int catComp = p1.getNomeCategoria().compareTo(p2.getNomeCategoria());
                    if (catComp != 0) {
                        return catComp; // Prima ordina per categoria
                    }
                    return p1.getNome().compareTo(p2.getNome()); // Poi per nome
                }
            });

            // Svuoto la tabella
            DefaultTableModel model = (DefaultTableModel) pietanzeTable.getModel();
            model.setRowCount(0);

            // Popolo la tabella e la mappa dei prezzi
            for (DTOPietanza pietanza : pietanze) {
                int idPietanza = pietanza.getIdPietanza();
                String nome = pietanza.getNome();
                String categoria = pietanza.getNomeCategoria();
                double prezzo = pietanza.getPrezzo();

                model.addRow(new Object[] { idPietanza, nome, categoria, String.format("%.2f", prezzo) });

                // Salvo nome e prezzo per uso futuro
                prezziPietanze.put(idPietanza, prezzo);
                nomiPietanze.put(idPietanza, nome);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento delle pietanze: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carica i menu fissi nella tabella
     */
    private void caricaMenuFissi() {
        try {

            ArrayList<DTOMenuFisso> menuFissi = Controller.getTuttiMenuFissi();
            
            // Ordina i menu fissi per ID
            java.util.Collections.sort(menuFissi, new java.util.Comparator<DTOMenuFisso>() {
                @Override
                public int compare(DTOMenuFisso m1, DTOMenuFisso m2) {
                    return Integer.compare(m1.getIdMenu(), m2.getIdMenu());
                }
            });

            // Svuoto la tabella
            DefaultTableModel model = (DefaultTableModel) menuFissiTable.getModel();
            model.setRowCount(0);

            for (DTOMenuFisso menu : menuFissi) {
                int idMenu = menu.getIdMenu();
                String nome = menu.getNome();
                String descrizione = menu.getDescrizione();
                double prezzo = menu.getPrezzo();

                // Dettagli menu - per ora utilizziamo solo la descrizione
                String dettagliMenu = descrizione;

                List<DTOPietanza> pietanze = menu.getPietanze();
                if (pietanze != null && !pietanze.isEmpty()) {
                    dettagliMenu += "\n\nPietanze incluse:\n";
                    for (DTOPietanza pietanza : pietanze) {
                        dettagliMenu += pietanza.getNomeCategoria().toUpperCase() + ": " + pietanza.getNome() + "\n";
                    }
                } else {
                    dettagliMenu += "\n\nNessuna pietanza inclusa.";
                }

                model.addRow(new Object[] { idMenu, nome, dettagliMenu, String.format("%.2f", prezzo) });

                // Salvo nome e prezzo per uso futuro
                prezziMenu.put(idMenu, prezzo);
                nomiMenu.put(idMenu, nome);
            }

            // Ajusto le dimensioni delle celle per una migliore visualizzazione
            menuFissiTable.setRowHeight(180); // Aumento ulteriormente l'altezza delle righe per mostrare più contenuto
                                              // scrollabile
            menuFissiTable.getColumnModel().getColumn(2).setPreferredWidth(350); // Allargo la colonna dei dettagli

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei menu fissi: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiunge una pietanza all'ordine corrente
     */
    private void aggiungiPietanzaAllOrdine() {
        int selectedRow = pietanzeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona una pietanza",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPietanza = Integer.parseInt(pietanzeTable.getValueAt(selectedRow, 0).toString());
        String nomePietanza = pietanzeTable.getValueAt(selectedRow, 1).toString();
        int quantita = Integer.parseInt(quantitaSpinner.getValue().toString());
        double prezzo = prezziPietanze.get(idPietanza);
        double totale = prezzo * quantita;
        String note = noteField.getText();

        ordineTableModel.addRow(new Object[] {
                idPietanza,
                "Pietanza",
                nomePietanza,
                quantita,
                String.format("%.2f", prezzo),
                String.format("%.2f", totale),
                note
        });

        // Reset campi
        quantitaSpinner.setValue(1);
        noteField.setText("");

        JOptionPane.showMessageDialog(this,
                "Pietanza aggiunta all'ordine.",
                "Pietanza aggiunta", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Aggiunge un menu fisso all'ordine corrente, inserendo singolarmente tutte le
     * pietanze che lo compongono
     */
    private void aggiungiMenuAllOrdine(int quantita) {
        int selectedRow = menuFissiTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un menu fisso",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idMenu = Integer.parseInt(menuFissiTable.getValueAt(selectedRow, 0).toString());
        String nomeMenu = menuFissiTable.getValueAt(selectedRow, 1).toString();
        double prezzo = prezziMenu.get(idMenu);
        double totale = prezzo * quantita;

        // Aggiungiamo il menu come intestazione
        ordineTableModel.addRow(new Object[] {
                idMenu,
                "Menu",
                "=== " + nomeMenu + " ===",
                quantita,
                String.format("%.2f", prezzo),
                String.format("%.2f", totale),
                "Menu completo"
        });

        // Per una vera implementazione, dovremo modificare il Controller per ottenere
        // le pietanze incluse nel menu
        // Per ora solo un messaggio informativo
        JOptionPane.showMessageDialog(this,
                "Menu aggiunto all'ordine.",
                "Menu aggiunto", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Elimina un elemento dall'ordine
     */
    private void eliminaElementoOrdine() {
        int selectedRow = ordineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un elemento da eliminare",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ordineTableModel.removeRow(selectedRow);
    }

    /**
     * Invia l'ordine al database
     */
    private void inviaOrdine() {
        if (ordineTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "L'ordine è vuoto",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tavoliComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un tavolo",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tavolo = tavoliComboBox.getSelectedItem().toString();
        int idTavolo = Integer.parseInt(tavolo.split(" - ")[0]);

        // Prepara i dati per l'ordine
        List<Map<String, Object>> elementiOrdine = new ArrayList<>();
        String note = ""; // Le note potrebbero essere aggiunte come campo nell'interfaccia

        // Ciclo su tutte le righe dell'ordine
        for (int i = 0; i < ordineTableModel.getRowCount(); i++) {
            int id = Integer.parseInt(ordineTableModel.getValueAt(i, 0).toString());
            String tipo = ordineTableModel.getValueAt(i, 1).toString();
            int quantita = Integer.parseInt(ordineTableModel.getValueAt(i, 3).toString());
            String noteElemento = String.valueOf(ordineTableModel.getValueAt(i, 6));

            // Controlliamo se è un'intestazione di menu o una pietanza inclusa in un menu
            boolean isMenuHeader = "Menu".equals(tipo) && noteElemento.equals("Menu completo");
            boolean isPietanzaFromMenu = "Pietanza".equals(tipo) && noteElemento.contains("Inclusa nel menu");

            // Aggiungiamo solo i menu completi e le pietanze singole (non quelle incluse
            // nei menu)
            if (isMenuHeader) {
                Map<String, Object> elementoMenu = new HashMap<>();
                elementoMenu.put("id", id);
                elementoMenu.put("isMenu", true);
                elementoMenu.put("quantita", quantita);
                elementoMenu.put("note", noteElemento);
                elementiOrdine.add(elementoMenu);
            } else if ("Pietanza".equals(tipo) && !isPietanzaFromMenu) {
                Map<String, Object> elementoPietanza = new HashMap<>();
                elementoPietanza.put("id", id);
                elementoPietanza.put("isMenu", false);
                elementoPietanza.put("quantita", quantita);
                elementoPietanza.put("note", noteElemento);
                elementiOrdine.add(elementoPietanza);
            }
        }

        try {
            Controller controller = Controller.getInstance();
            boolean success = controller.insertOrdine(idTavolo, elementiOrdine, note);

            if (success) {
                // Mostro conferma e resetto l'ordine
                JOptionPane.showMessageDialog(this,
                        "Ordine inviato con successo",
                        "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

                // Resetto l'ordine
                ordineTableModel.setRowCount(0);

                // Aggiorno la lista dei tavoli
                caricaTavoli();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Si è verificato un errore durante l'invio dell'ordine",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante l'invio dell'ordine: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per caricare icone SVG (copiato da FirstForm)
    private ImageIcon loadSVGIcon(String filename, int width, int height) {
        return loadSVGIcon(filename, width, height, Color.WHITE);
    }

    // Metodo sovraccaricato per specificare il colore dell'icona
    private ImageIcon loadSVGIcon(String filename, int width, int height, Color color) {
        try {
            // Percorsi possibili per le icone SVG (nell'ordine di priorità)
            String[] possiblePaths = {
                    "bin/resources/icons/" + filename, // Nel container/dopo compilazione
                    "resources/icons/" + filename, // Percorso relativo nel container
                    "GestioneRistorante/bin/resources/icons/" + filename, // Dalla root progetto
                    "GestioneRistorante/src/resources/icons/" + filename, // Sorgente originale
                    "src/resources/icons/" + filename // Durante sviluppo
            };

            java.io.File svgFile = null;

            for (String path : possiblePaths) {
                java.io.File testFile = new java.io.File(path);
                if (testFile.exists()) {
                    svgFile = testFile;
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
                    }
                } catch (Exception e) {
                    // Ignora e usa fallback
                }
            }

            if (svgFile == null || !svgFile.exists()) {
                return createFallbackIcon(filename, width, height, color);
            }

            SVGUniverse svgUniverse = new SVGUniverse();
            java.net.URI svgUri = svgFile.toURI();
            SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(svgUri.toURL()));

            if (diagram == null) {
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
        if (filename.contains("pietanza")) {
            return "🍝";
        } else if (filename.contains("menu")) {
            return "📋";
        } else if (filename.contains("order")) {
            return "🛒";
        } else if (filename.contains("person")) {
            return "👤";
        } else if (filename.contains("restaurant_menu")) {
            return "🍽️";
        } else if (filename.contains("payment")) {
            return "💳";
        } else if (filename.contains("admin_panel_settings")) {
            return "⚙️";
        } else {
            return "📄";
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
                new CameriereForm().setVisible(true);
            }
        });
    }
}
