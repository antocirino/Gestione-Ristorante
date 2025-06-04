package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer; // Import specifico per evitare ambiguità
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import CFG.DBConnection;

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
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Pannello superiore con titolo
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("GESTIONE CUCINA");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Pannello centrale diviso in due parti
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Pannello superiore con la lista degli ordini
        JPanel ordiniPanel = new JPanel(new BorderLayout());
        ordiniPanel.setBorder(BorderFactory.createTitledBorder("Ordini da preparare"));

        DefaultTableModel ordiniTableModel = new DefaultTableModel(
                new String[] { "ID Ordine", "Tavolo", "Persone", "Ora", "Stato" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi tutte le celle non modificabili
            }
        };

        ordiniTable = new JTable(ordiniTableModel);
        ordiniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane ordiniScrollPane = new JScrollPane(ordiniTable);
        ordiniPanel.add(ordiniScrollPane, BorderLayout.CENTER);

        // Pannello inferiore con i dettagli dell'ordine
        JPanel dettagliPanel = new JPanel(new BorderLayout());
        dettagliPanel.setBorder(BorderFactory.createTitledBorder("Dettaglio Ordine"));

        DefaultTableModel dettagliTableModel = new DefaultTableModel(
                new String[] { "Tipo", "Nome", "Quantità", "Note" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi tutte le celle non modificabili
            }
        };

        dettagliTable = new JTable(dettagliTableModel);
        JScrollPane dettagliScrollPane = new JScrollPane(dettagliTable);
        dettagliPanel.add(dettagliScrollPane, BorderLayout.CENTER);

        // Pannello pulsanti per la gestione degli ordini
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        iniziaPreparazioneButton = new JButton("Inizia Preparazione");
        segnaCompletatoButton = new JButton("Segna come Pronto");
        segnaConsegnatoButton = new JButton("Segna come Consegnato");
        visualizzaIngredientiButton = new JButton("Visualizza Ingredienti");

        buttonsPanel.add(iniziaPreparazioneButton);
        buttonsPanel.add(segnaCompletatoButton);
        buttonsPanel.add(segnaConsegnatoButton);
        buttonsPanel.add(visualizzaIngredientiButton);

        // Pannello pulsanti in fondo
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        indietroButton = new JButton("Indietro");
        bottomButtonPanel.add(indietroButton);

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
                dispose();
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
