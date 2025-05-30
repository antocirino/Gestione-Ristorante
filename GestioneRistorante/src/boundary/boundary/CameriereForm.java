package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import CFG.DBConnection;

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
                new String[] { "ID", "Nome", "Descrizione", "Prezzo (€)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi tutte le celle non modificabili
            }
        };

        menuFissiTable = new JTable(menuFissiTableModel);
        menuFissiTable.getColumnModel().getColumn(0).setMaxWidth(50);
        menuFissiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
            Connection conn = DBConnection.getInstance().getConnection();
            String query = "SELECT id_categoria, nome FROM categoria_pietanza ORDER BY nome";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categorieComboBox.addItem(rs.getString("nome"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
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
            Connection conn = DBConnection.getInstance().getConnection();
            String query = "SELECT id_tavolo, numero_tavolo, max_posti FROM tavolo WHERE stato = 'libero' ORDER BY numero_tavolo";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tavoliComboBox.removeAllItems();
            while (rs.next()) {
                int idTavolo = rs.getInt("id_tavolo");
                int numeroTavolo = rs.getInt("numero_tavolo");
                int maxPosti = rs.getInt("max_posti");
                tavoliComboBox.addItem(idTavolo + " - Tavolo " + numeroTavolo + " (max " + maxPosti + " posti)");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
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
            Connection conn = DBConnection.getInstance().getConnection();
            String categoriaSelezionata = (String) categorieComboBox.getSelectedItem();
            String query;
            PreparedStatement stmt;

            if (categoriaSelezionata == null || categoriaSelezionata.equals("Tutte le categorie")) {
                query = "SELECT p.id_pietanza, p.nome, cp.nome as categoria, p.prezzo " +
                        "FROM pietanza p JOIN categoria_pietanza cp ON p.id_categoria = cp.id_categoria " +
                        "ORDER BY cp.nome, p.nome";
                stmt = conn.prepareStatement(query);
            } else {
                query = "SELECT p.id_pietanza, p.nome, cp.nome as categoria, p.prezzo " +
                        "FROM pietanza p JOIN categoria_pietanza cp ON p.id_categoria = cp.id_categoria " +
                        "WHERE cp.nome = ? ORDER BY p.nome";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, categoriaSelezionata);
            }

            ResultSet rs = stmt.executeQuery();

            // Svuoto la tabella
            DefaultTableModel model = (DefaultTableModel) pietanzeTable.getModel();
            model.setRowCount(0);

            // Popolo la tabella e la mappa dei prezzi
            while (rs.next()) {
                int idPietanza = rs.getInt("id_pietanza");
                String nome = rs.getString("nome");
                String categoria = rs.getString("categoria");
                double prezzo = rs.getDouble("prezzo");

                model.addRow(new Object[] { idPietanza, nome, categoria, String.format("%.2f", prezzo) });

                // Salvo nome e prezzo per uso futuro
                prezziPietanze.put(idPietanza, prezzo);
                nomiPietanze.put(idPietanza, nome);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
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
            Connection conn = DBConnection.getInstance().getConnection();
            String query = "SELECT id_menu, nome, descrizione, prezzo FROM menu_fisso ORDER BY nome";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Svuoto la tabella
            DefaultTableModel model = (DefaultTableModel) menuFissiTable.getModel();
            model.setRowCount(0);

            // Popolo la tabella e la mappa dei prezzi
            while (rs.next()) {
                int idMenu = rs.getInt("id_menu");
                String nome = rs.getString("nome");
                String descrizione = rs.getString("descrizione");
                double prezzo = rs.getDouble("prezzo");

                model.addRow(new Object[] { idMenu, nome, descrizione, String.format("%.2f", prezzo) });

                // Salvo nome e prezzo per uso futuro
                prezziMenu.put(idMenu, prezzo);
                nomiMenu.put(idMenu, nome);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
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
     * Aggiunge un menu fisso all'ordine corrente
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

        ordineTableModel.addRow(new Object[] {
                idMenu,
                "Menu",
                nomeMenu,
                quantita,
                String.format("%.2f", prezzo),
                String.format("%.2f", totale),
                ""
        });

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

        // Ottieni il numero di persone
        String numPersoneStr = JOptionPane.showInputDialog(this, "Numero di persone al tavolo:", "1");
        if (numPersoneStr == null)
            return; // Annullato

        int numPersone;
        try {
            numPersone = Integer.parseInt(numPersoneStr);
            if (numPersone < 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Il numero di persone deve essere un valore positivo",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Inizio transazione

            try {
                // 1. Inserisco l'ordine
                String queryOrdine = "INSERT INTO ordine (id_tavolo, num_persone, data_ordine, stato) " +
                        "VALUES (?, ?, NOW(), 'in_attesa')";
                PreparedStatement stmtOrdine = conn.prepareStatement(queryOrdine,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                stmtOrdine.setInt(1, idTavolo);
                stmtOrdine.setInt(2, numPersone);
                stmtOrdine.executeUpdate();

                ResultSet rs = stmtOrdine.getGeneratedKeys();
                int idOrdine = -1;
                if (rs.next()) {
                    idOrdine = rs.getInt(1);
                } else {
                    throw new SQLException("Errore durante la creazione dell'ordine, ID non generato");
                }

                // 2. Aggiorno lo stato del tavolo a occupato
                String queryTavolo = "UPDATE tavolo SET stato = 'occupato' WHERE id_tavolo = ?";
                PreparedStatement stmtTavolo = conn.prepareStatement(queryTavolo);
                stmtTavolo.setInt(1, idTavolo);
                stmtTavolo.executeUpdate();

                // 3. Inserisco i dettagli dell'ordine (pietanze)
                String queryPietanza = "INSERT INTO dettaglio_ordine_pietanza (id_ordine, id_pietanza, quantita, note) "
                        +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPietanza = conn.prepareStatement(queryPietanza);

                // 4. Inserisco i dettagli dell'ordine (menu fissi)
                String queryMenu = "INSERT INTO dettaglio_ordine_menu (id_ordine, id_menu, quantita) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement stmtMenu = conn.prepareStatement(queryMenu);

                // Ciclo su tutte le righe dell'ordine
                for (int i = 0; i < ordineTableModel.getRowCount(); i++) {
                    int id = Integer.parseInt(ordineTableModel.getValueAt(i, 0).toString());
                    String tipo = ordineTableModel.getValueAt(i, 1).toString();
                    int quantita = Integer.parseInt(ordineTableModel.getValueAt(i, 3).toString());
                    String note = (String) ordineTableModel.getValueAt(i, 6);

                    if ("Pietanza".equals(tipo)) {
                        stmtPietanza.setInt(1, idOrdine);
                        stmtPietanza.setInt(2, id);
                        stmtPietanza.setInt(3, quantita);
                        stmtPietanza.setString(4, note);
                        stmtPietanza.executeUpdate();
                    } else if ("Menu".equals(tipo)) {
                        stmtMenu.setInt(1, idOrdine);
                        stmtMenu.setInt(2, id);
                        stmtMenu.setInt(3, quantita);
                        stmtMenu.executeUpdate();
                    }
                }

                // Commit della transazione
                conn.commit();

                // Mostro conferma e resetto l'ordine
                JOptionPane.showMessageDialog(this,
                        "Ordine inviato con successo",
                        "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

                // Resetto l'ordine
                ordineTableModel.setRowCount(0);

                // Aggiorno la lista dei tavoli
                caricaTavoli();

            } catch (SQLException e) {
                // Rollback in caso di errore
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true); // Ripristina auto commit
            }
        } catch (SQLException e) {
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
