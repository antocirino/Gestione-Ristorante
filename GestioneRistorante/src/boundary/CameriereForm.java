package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DTO.DTOCategoriaPietanza;
import DTO.DTOMenuFisso;
import DTO.DTOPietanza;
import DTO.DTOTavolo;
import entity.EntityOrdine;
import utility.SvgIconManager;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import control.Controller;

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
    private JComboBox<String> tavoliLiberiComboBox;
    private JSpinner copertiSpinner;

    // Per tenere traccia dell'ordine corrente
    private int currentOrderId = -1;
    private int currentTableId = -1;
    private int currentPersons = 0;

    /**
     * Costruttore della schermata CameriereForm
     * 
     */
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
        ImageIcon svgIcon = SvgIconManager.loadSVGIcon("person.svg", 32, 32);
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
        ImageIcon pietanzaIcon = SvgIconManager.loadSVGIcon("pietanza.svg", 20, 20, textColor);
        tabbedPane.addTab("Pietanze", pietanzaIcon, menuPanel);

        // Tab per i menu fissi
        menuFissoPanel = createMenuFissoPanel();
        ImageIcon menuIcon = SvgIconManager.loadSVGIcon("menu.svg", 20, 20, textColor);
        tabbedPane.addTab("Menu Fissi", menuIcon, menuFissoPanel);

        // Tab per l'ordine
        ordinePanel = createOrdinePanel();
        ImageIcon ordineIcon = SvgIconManager.loadSVGIcon("order.svg", 20, 20, textColor);
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
        // Disabilita inizialmente il pulsante indietro
        indietroButton.setEnabled(false);

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
        caricaTavoliLiberi();
        caricaPietanze();
        caricaMenuFissi();

        // IMPORTANTE: Mostra il dialog per la creazione dell'ordine all'apertura
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mostraDialogInizioOrdine();
            }
        });
    }

    /**
     * Crea il pannello con la lista delle pietanze
     * 
     * @return Un JPanel con la tabella delle pietanze e i controlli per aggiungere
     *         all'ordine
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
     * 
     * @return Un JPanel con la tabella dei menu fissi e i pulsanti per aggiungere
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
     * 
     * @return Un JPanel con le informazioni dell'ordine e la tabella delle pietanze
     */
    private JPanel createOrdinePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(lightColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello superiore con le informazioni del tavolo e numero di persone
        JPanel tavoloPanel = createModernPanel();
        tavoloPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        // svg tavolo
        ImageIcon tavoloIcon = SvgIconManager.loadSVGIcon("table.svg", 20, 20);
        JLabel tavoloIconLabel = new JLabel(tavoloIcon);
        tavoloIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        tavoloPanel.add(tavoloIconLabel);

        // Etichetta con informazioni del tavolo
        JLabel tavoloInfoLabel = new JLabel("Tavolo n.: -");
        tavoloInfoLabel.setFont(headerFont);
        tavoloInfoLabel.setForeground(textColor);
        tavoloPanel.add(tavoloInfoLabel);

        // Separatore
        JLabel separatorLabel = new JLabel(" | ");
        separatorLabel.setFont(headerFont);
        separatorLabel.setForeground(textColor);
        tavoloPanel.add(separatorLabel);

        // Etichetta per il numero di persone
        JLabel personeInfoLabel = new JLabel("N. di persone: -");
        personeInfoLabel.setFont(headerFont);
        personeInfoLabel.setForeground(textColor);
        tavoloPanel.add(personeInfoLabel);

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

        inviaOrdineButton = createStyledButton("Invia Ordine", primaryColor);

        // Aggiungi icona SVG al pulsante
        ImageIcon inviaIcon = SvgIconManager.loadSVGIcon("send.svg", 20, 20);
        inviaOrdineButton.setIcon(inviaIcon);

        inviaOrdineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inviaOrdine();
            }
        });

        ordineButtonPanel.add(inviaOrdineButton);

        panel.add(tavoloPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(ordineButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Metodi di utilità per lo styling moderno

    /**
     * Crea un pulsante
     * 
     * @param text            Il testo del pulsante
     * @param backgroundColor Il colore di sfondo del pulsante
     * @return Un JButton con lo stile applicato
     */
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

    /**
     * Aggiungi stile al JTable
     * 
     * @param table il JTable da stilizzare
     */
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

    /**
     * Aggiunge uno stile allo JScrollPane
     * 
     * @param scrollPane lo JScrollPane da stilizzare
     */
    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    /**
     * Stile per il JComboBox
     * 
     * @param comboBox il JComboBox da stilizzare
     */
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

    /**
     * Stile per il JTextField
     * 
     * @param textField il JTextField da stilizzare
     */
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

    /**
     * Aggiunge uno stile allo Spinner
     * 
     * @param spinner lo Spinner da stilizzare
     */
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
            // Disabilita la modifica diretta del campo di testo dello spinner
            textField.setEditable(false);
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

    /**
     * Crea un pannello
     */
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
    private void caricaTavoliLiberi() {
        try {

            List<DTOTavolo> tavoli = Controller.getTavoliByStato("libero");

            tavoliComboBox.removeAllItems();
            for (DTOTavolo tavolo : tavoli) {
                int idTavolo = tavolo.getIdTavolo();
                int maxPosti = tavolo.getMaxPosti();
                tavoliComboBox.addItem(
                        idTavolo + " - Tavolo " + " (max " + maxPosti + " posti)");
            }
        } catch (Exception e) {
            // Ignora o gestisci l'errore in modo silenzioso, o loggalo
            System.err.println("Errore durante il caricamento dei tavoli: " + e.getMessage());
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

            // Ordina le pietanze per categoria e poi per nome all'interno della stessa
            // categoria
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

        // Chiamata al Controller per aggiungere la pietanza all'ordine nel DB
        if (currentOrderId > 0) {
            boolean ok = Controller.aggiungiPietanzaAllOrdine(currentOrderId, idPietanza, quantita);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Errore nell'aggiunta della pietanza all'ordine.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Aggiorna la tabella visuale dell'ordine (resta invariata)
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

        // Chiamata al Controller per aggiungere il menu fisso all'ordine nel DB
        if (currentOrderId > 0) {
            boolean ok = Controller.aggiungiMenuFisso(currentOrderId, idMenu, quantita);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Errore nell'aggiunta del menu fisso all'ordine.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Aggiorna la tabella visuale dell'ordine (resta invariata)
        ordineTableModel.addRow(new Object[] {
                idMenu,
                "Menu",
                "=== " + nomeMenu + " ===",
                quantita,
                String.format("%.2f", prezzo),
                String.format("%.2f", totale),
                "Menu completo"
        });

        JOptionPane.showMessageDialog(this,
                "Menu aggiunto all'ordine.",
                "Menu aggiunto", JOptionPane.INFORMATION_MESSAGE);
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

        // Usa il tavolo corrente e il numero di persone impostati durante la creazione
        // dell'ordine
        if (currentTableId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Non è stato selezionato un tavolo valido",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idTavolo = currentTableId;

        try {
            // Conferma l'ordine
            Controller.ConfermaOrdine(currentOrderId, idTavolo);

            // Mostro conferma
            JOptionPane.showMessageDialog(this,
                    "Ordine inviato con successo",
                    "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

            // Riabilita il pulsante indietro
            indietroButton.setEnabled(true);

            // Chiudi il form e torna alla home
            this.dispose();
            new FirstForm().setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante l'invio dell'ordine: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Mostra il dialog per la creazione di un nuovo ordine
     */
    private void mostraDialogInizioOrdine() {
        // Crea un JDialog personalizzato
        JDialog dialog = new JDialog(this, "Inizio Nuovo Ordine", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Pannello interno al dialog
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridBagLayout());
        dialogPanel.setBackground(lightColor);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel dialogTitleLabel = new JLabel("Inizio Nuovo Ordine");
        dialogTitleLabel.setFont(headerFont);
        dialogTitleLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialogPanel.add(dialogTitleLabel, gbc);

        // Selezione tavolo
        JLabel tavoloLabel = new JLabel("Seleziona tavolo:");
        tavoloLabel.setFont(regularFont);
        tavoloLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialogPanel.add(tavoloLabel, gbc);

        tavoliLiberiComboBox = new JComboBox<>();
        caricaTuttiTavoli();
        styleComboBox(tavoliLiberiComboBox);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialogPanel.add(tavoliLiberiComboBox, gbc);

        // Numero coperti
        JLabel copertiLabel = new JLabel("Numero coperti:");
        copertiLabel.setFont(regularFont);
        copertiLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        dialogPanel.add(copertiLabel, gbc);

        SpinnerModel copertiSpinnerModel = new SpinnerNumberModel(2, 1, 20, 1);
        copertiSpinner = new JSpinner(copertiSpinnerModel);
        styleSpinner(copertiSpinner);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialogPanel.add(copertiSpinner, gbc);

        // Pulsanti
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBackground(lightColor);

        // Pulsante conferma
        JButton confermaButton = createStyledButton("Inizia Ordine", successColor);
        confermaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniziaNuovoOrdine();
                dialog.dispose();
            }
        });

        // Pulsante annulla
        JButton annullaButton = createStyledButton("Annulla", dangerColor);
        annullaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CameriereForm.this.dispose(); // Chiudi il form cameriere
                dialog.dispose();
                new FirstForm().setVisible(true); // Ritorna al primo form
            }
        });

        buttonsPanel.add(confermaButton);
        buttonsPanel.add(annullaButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialogPanel.add(buttonsPanel, gbc);

        // Aggiungi pannello al dialog e mostra
        dialog.add(dialogPanel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    /**
     * Carica i tavoli liberi per la selezione iniziale
     */
    private void caricaTuttiTavoli() {
        try {
            List<DTOTavolo> tavoli = Controller.getAllTavoli();

            tavoliLiberiComboBox.removeAllItems();
            for (DTOTavolo tavolo : tavoli) {
                if (!tavolo.isOccupato()) {
                    int idTavolo = tavolo.getIdTavolo();
                    int maxPosti = tavolo.getMaxPosti();
                    tavoliLiberiComboBox.addItem(
                            idTavolo + " - Tavolo (max " + maxPosti + " posti)");
                }
            }

            // Verifica se ci sono tavoli liberi
            if (tavoliLiberiComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Non ci sono tavoli liberi al momento.",
                        "Avviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            // Ignora o gestisci l'errore in modo silenzioso, o loggalo
            System.err.println("Errore durante il caricamento dei tavoli liberi: " + e.getMessage());
        }
    }

    /**
     * Inizia un nuovo ordine utilizzando Controller.CreaOrdine
     */
    private void iniziaNuovoOrdine() {
        if (tavoliLiberiComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un tavolo libero",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ottiene l'ID del tavolo dalla stringa selezionata
        String tavoloString = tavoliLiberiComboBox.getSelectedItem().toString();
        int idTavolo = Integer.parseInt(tavoloString.split(" - ")[0]);

        // Ottiene il numero di coperti
        int numeroCoperti = (Integer) copertiSpinner.getValue();

        // Recupera il max posti del tavolo selezionato
        int maxPosti = 0;
        try {
            // Supponendo che Controller.getAllTavoli() restituisca la lista dei tavoli
            List<DTOTavolo> tavoli = Controller.getAllTavoli();
            for (DTOTavolo tavolo : tavoli) {
                if (tavolo.getIdTavolo() == idTavolo) {
                    maxPosti = tavolo.getMaxPosti();
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il controllo dei posti del tavolo: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica che il numero di coperti non superi il massimo consentito
        if (numeroCoperti > maxPosti) {
            JOptionPane.showMessageDialog(this,
                    "Il numero di coperti (" + numeroCoperti
                            + ") supera il massimo consentito per il tavolo selezionato (" + maxPosti + ").",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            // Riapri il dialog per la selezione
            SwingUtilities.invokeLater(() -> mostraDialogInizioOrdine());
            return;
        }

        try {
            // Chiamata al controller per creare un nuovo ordine
            // Imposta il tavolo corrente e numero di persone
            currentTableId = idTavolo;
            currentPersons = numeroCoperti;
            // Il metodo CreaOrdine richiede (num_persone, id_tavolo, stato)
            EntityOrdine ordine = Controller.CreaOrdine(numeroCoperti, idTavolo, "in_attesa");
            // Salva l'ID dell'ordine
            currentOrderId = ordine.getIdOrdine();

            // Aggiorna le etichette nell'interfaccia
            aggiornaInfoOrdine();

            // Disabilita il pulsante indietro
            indietroButton.setEnabled(false);

            // Imposta l'etichetta dell'ordine
            JOptionPane.showMessageDialog(this,
                    "Ordine iniziato per il tavolo " + idTavolo + " con " + numeroCoperti + " coperti",
                    "Ordine Creato", JOptionPane.INFORMATION_MESSAGE);

            // Aggiorna la lista dei tavoli disponibili
            caricaTavoliLiberi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la creazione dell'ordine: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiorna le etichette delle informazioni sul tavolo e persone nel pannello
     * ordine
     */
    private void aggiornaInfoOrdine() {
        // Cerca i componenti nel pannello dell'ordine
        if (ordinePanel != null) {
            Component[] components = ordinePanel.getComponents();
            if (components.length > 0 && components[0] instanceof JPanel) {
                JPanel tavoloPanel = (JPanel) components[0];
                for (Component comp : tavoloPanel.getComponents()) {
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        String text = label.getText();
                        if (text != null && text.startsWith("Tavolo n.:")) {
                            label.setText("Tavolo n.: " + currentTableId);
                        } else if (text != null && text.startsWith("N. di persone:")) {
                            label.setText("N. di persone: " + currentPersons);
                        }
                    }
                }
            }
        }
    }
}
