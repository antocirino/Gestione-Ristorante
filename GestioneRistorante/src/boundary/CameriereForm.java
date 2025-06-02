package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import control.Controller;
import entity.EntityPietanza;
import entity.EntityTavolo;

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

    // Per tenere traccia dell'ordine corrente
    private DefaultTableModel ordineTableModel;
    private Map<Integer, Double> prezziPietanze = new HashMap<>();
    private Map<Integer, Double> prezziMenu = new HashMap<>();
    private Map<Integer, String> nomiPietanze = new HashMap<>();
    private Map<Integer, String> nomiMenu = new HashMap<>();

    public CameriereForm() {
        setTitle("Gestione Ristorante - Cameriere");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Pannello superiore con titolo
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("GESTIONE ORDINI");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Creazione del tabbed pane
        tabbedPane = new JTabbedPane();

        // Tab per le pietanze
        menuPanel = createMenuPanel();
        tabbedPane.addTab("Pietanze", menuPanel);

        // Tab per i menu fissi
        menuFissoPanel = createMenuFissoPanel();
        tabbedPane.addTab("Menu Fissi", menuFissoPanel);

        // Tab per l'ordine
        ordinePanel = createOrdinePanel();
        tabbedPane.addTab("Ordine", ordinePanel);

        // Pannello bottoni in fondo
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        indietroButton = new JButton("Indietro");
        indietroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(indietroButton);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Pannello di controllo superiore
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel categoriaLabel = new JLabel("Filtra per categoria:");
        categorieComboBox = new JComboBox<>();
        categorieComboBox.addItem("Tutte le categorie");

        controlPanel.add(categoriaLabel);
        controlPanel.add(categorieComboBox);

        // Tabella pietanze
        DefaultTableModel menuTableModel = new DefaultTableModel(
                new String[] { "ID", "Nome", "Categoria", "Prezzo (€)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi tutte le celle non modificabili
            }
        };

        pietanzeTable = new JTable(menuTableModel);
        pietanzeTable.getColumnModel().getColumn(0).setMaxWidth(50);
        pietanzeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(pietanzeTable);

        // Pannello inferiore per aggiungere all'ordine
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel quantitaLabel = new JLabel("Quantità:");
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 20, 1);
        quantitaSpinner = new JSpinner(spinnerModel);

        JLabel noteLabel = new JLabel("Note:");
        noteField = new JTextField(20);

        aggiungiPietanzaButton = new JButton("Aggiungi all'ordine");
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabella menu fissi
        DefaultTableModel menuFissiTableModel = new DefaultTableModel(
                new String[] { "ID", "Nome", "Dettagli Menu", "Prezzo (€)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi tutte le celle non modificabili
            }
        };

        menuFissiTable = new JTable(menuFissiTableModel);
        menuFissiTable.getColumnModel().getColumn(0).setMaxWidth(50);
        menuFissiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurazione per rendere visibile il testo multilinea con scrollbar
        menuFissiTable.setDefaultRenderer(Object.class, new javax.swing.table.TableCellRenderer() {
            // Manteniamo istanze dei renderer per non creare sempre nuovi oggetti
            private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {

                // Per la colonna dei dettagli del menu, usiamo un pannello con scrollbar
                if (column == 2) {
                    JTextArea textArea = new JTextArea();
                    textArea.setText((value != null) ? value.toString() : "");
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    textArea.setEditable(false);
                    textArea.setMargin(new Insets(5, 5, 5, 5)); // Aggiungiamo un po' di margine per la leggibilità

                    // Imposta i colori per la selezione
                    if (isSelected) {
                        textArea.setBackground(table.getSelectionBackground());
                        textArea.setForeground(table.getSelectionForeground());
                    } else {
                        textArea.setBackground(table.getBackground());
                        textArea.setForeground(table.getForeground());
                    }

                    // Creiamo uno scrollpane per il textArea
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    scrollPane.setOpaque(true);
                    scrollPane.setBackground(textArea.getBackground());

                    // Coloriamo anche lo scrollpane quando la riga è selezionata
                    scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = isSelected ? table.getSelectionBackground().darker()
                                    : new Color(0xB0B0B0);
                        }
                    });

                    return scrollPane;
                } else {
                    // Per le altre colonne, utilizziamo il renderer standard
                    return defaultRenderer.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(menuFissiTable);

        // Pannello inferiore per aggiungere all'ordine
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel quantitaMenuLabel = new JLabel("Quantità:");
        SpinnerModel spinnerModelMenu = new SpinnerNumberModel(1, 1, 20, 1);
        JSpinner quantitaMenuSpinner = new JSpinner(spinnerModelMenu);

        aggiungiMenuButton = new JButton("Aggiungi all'ordine");
        aggiungiMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aggiungiMenuAllOrdine(Integer.parseInt(quantitaMenuSpinner.getValue().toString()));
            }
        });

        addPanel.add(quantitaMenuLabel);
        addPanel.add(quantitaMenuSpinner);
        addPanel.add(aggiungiMenuButton);

        // Assemblaggio pannello
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea il pannello per la gestione dell'ordine
     */
    private JPanel createOrdinePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Pannello superiore con la selezione del tavolo
        JPanel tavoloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel tavoloLabel = new JLabel("Tavolo:");
        tavoliComboBox = new JComboBox<>();

        tavoloPanel.add(tavoloLabel);
        tavoloPanel.add(tavoliComboBox);

        // Tabella ordine
        ordineTableModel = new DefaultTableModel(
                new String[] { "ID", "Tipo", "Nome", "Quantità", "Prezzo unit. (€)", "Totale (€)", "Note" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi tutte le celle non modificabili
            }
        };

        ordineTable = new JTable(ordineTableModel);
        ordineTable.getColumnModel().getColumn(0).setMaxWidth(50);
        ordineTable.getColumnModel().getColumn(1).setMaxWidth(80);
        ordineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(ordineTable);

        // Pannello inferiore con i pulsanti
        JPanel ordineButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        eliminaButton = new JButton("Elimina selezionato");
        inviaOrdineButton = new JButton("Invia Ordine");

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

        // Assemblaggio pannello
        panel.add(tavoloPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(ordineButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Carica le categorie di pietanze nel combobox
     */
    private void caricaCategorie() {
        try {
            Controller controller = Controller.getInstance();
            Map<Integer, String> categorie = controller.getCategoriePietanze();

            for (String nomeCategoria : categorie.values()) {
                categorieComboBox.addItem(nomeCategoria);
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
            Controller controller = Controller.getInstance();
            List<EntityTavolo> tavoli = controller.getAllTavoli();

            tavoliComboBox.removeAllItems();
            for (EntityTavolo tavolo : tavoli) {
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
            Controller controller = Controller.getInstance();
            String categoriaSelezionata = (String) categorieComboBox.getSelectedItem();

            List<EntityPietanza> pietanze;
            Map<Integer, String> categorie = controller.getCategoriePietanze();
            Map<String, Integer> categorieInverse = new HashMap<>();

            // Creiamo una mappa inversa da nome categoria a ID categoria
            for (Map.Entry<Integer, String> entry : categorie.entrySet()) {
                categorieInverse.put(entry.getValue(), entry.getKey());
            }

            if (categoriaSelezionata == null || categoriaSelezionata.equals("Tutte le categorie")) {
                pietanze = controller.getAllPietanze();
            } else {
                Integer idCategoria = categorieInverse.get(categoriaSelezionata);
                if (idCategoria != null) {
                    pietanze = controller.getPietanzeByCategoria(idCategoria);
                } else {
                    pietanze = new ArrayList<>();
                }
            }

            // Svuoto la tabella
            DefaultTableModel model = (DefaultTableModel) pietanzeTable.getModel();
            model.setRowCount(0);

            // Popolo la tabella e la mappa dei prezzi
            for (EntityPietanza pietanza : pietanze) {
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
            Controller controller = Controller.getInstance();
            Map<Integer, Map<String, Object>> menuFissi = controller.getMenuFissi();

            // Svuoto la tabella
            DefaultTableModel model = (DefaultTableModel) menuFissiTable.getModel();
            model.setRowCount(0);

            // Popolo la tabella e la mappa dei prezzi
            for (Map.Entry<Integer, Map<String, Object>> entry : menuFissi.entrySet()) {
                int idMenu = entry.getKey();
                Map<String, Object> menuInfo = entry.getValue();

                String nome = (String) menuInfo.get("nome");
                String descrizione = (String) menuInfo.get("descrizione");
                double prezzo = (Double) menuInfo.get("prezzo");

                // Dettagli menu - per ora utilizziamo solo la descrizione
                String dettagliMenu = descrizione
                        + "\n\nPIETANZE INCLUSE:\nIl dettaglio delle pietanze è disponibile nel controller";

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

        // Vai alla tab dell'ordine
        tabbedPane.setSelectedComponent(ordinePanel);
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
                "Menu aggiunto all'ordine. In una versione completa, qui verrebbero mostrate le singole pietanze incluse nel menu.",
                "Menu aggiunto", JOptionPane.INFORMATION_MESSAGE);

        // Vai alla tab dell'ordine
        tabbedPane.setSelectedComponent(ordinePanel);
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
