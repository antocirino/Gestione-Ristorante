package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import CFG.DBConnection;

/**
 * Schermata per il direttore che permette di generare report e monitorare il
 * ristorante
 */
public class DirettoreForm extends JFrame {
    private JPanel mainPanel;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JPanel graphPanel;
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<String> periodoComboBox;
    private JButton generaReportButton;
    private JButton stampaReportButton;
    private JButton indietroButton;

    public DirettoreForm() {
        setTitle("Gestione Ristorante - Pannello Direttore");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Pannello superiore con titolo
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("PANNELLO DIRETTORE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Pannello di controllo
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tipoReportLabel = new JLabel("Tipo di Report:");
        reportTypeComboBox = new JComboBox<>(new String[] {
                "Vendite per Pietanza",
                "Ingredienti da Ordinare",
                "Incasso Giornaliero",
                "Analisi Menu Fissi"
        });

        JLabel periodoLabel = new JLabel("Periodo:");
        periodoComboBox = new JComboBox<>(new String[] {
                "Oggi",
                "Ultima Settimana",
                "Ultimo Mese",
                "Ultimo Anno"
        });

        generaReportButton = new JButton("Genera Report");

        controlPanel.add(tipoReportLabel);
        controlPanel.add(reportTypeComboBox);
        controlPanel.add(periodoLabel);
        controlPanel.add(periodoComboBox);
        controlPanel.add(generaReportButton);

        // Pannello tabella
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Report"));

        // Creo il modello di tabella vuoto
        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        reportTable.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Pannello grafico
        graphPanel = new JPanel();
        graphPanel.setBorder(BorderFactory.createTitledBorder("Grafico"));
        graphPanel.setPreferredSize(new Dimension(800, 200));

        // Pannello bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        stampaReportButton = new JButton("Stampa Report");
        indietroButton = new JButton("Indietro");

        buttonPanel.add(stampaReportButton);
        buttonPanel.add(indietroButton);

        // Pannello centrale
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(graphPanel, BorderLayout.SOUTH);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Assemblaggio pannelli
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.NORTH, 1);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);

        // Event listeners
        generaReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generaReport();
            }
        });

        stampaReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DirettoreForm.this,
                        "Report inviato alla stampante",
                        "Stampa", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Genera il report in base alle selezioni dell'utente
     */
    private void generaReport() {
        String tipoReport = (String) reportTypeComboBox.getSelectedItem();
        String periodo = (String) periodoComboBox.getSelectedItem();

        // Data di inizio per il filtro
        Calendar cal = Calendar.getInstance();
        Date dataFine = cal.getTime();

        // Imposta la data di inizio in base al periodo selezionato
        switch (periodo) {
            case "Oggi":
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                break;
            case "Ultima Settimana":
                cal.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case "Ultimo Mese":
                cal.add(Calendar.MONTH, -1);
                break;
            case "Ultimo Anno":
                cal.add(Calendar.YEAR, -1);
                break;
        }

        Date dataInizio = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataInizioStr = sdf.format(dataInizio);
        String dataFineStr = sdf.format(dataFine);

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement stmt;
            ResultSet rs;

            switch (tipoReport) {
                case "Vendite per Pietanza":
                    // Clear the table model
                    tableModel = new DefaultTableModel(
                            new String[] { "Nome Pietanza", "Categoria", "Quantità Venduta", "Incasso Totale (€)" }, 0);

                    String queryVenditePietanze = "SELECT p.nome, cp.nome AS categoria, SUM(dop.quantita) AS quantita_totale, "
                            +
                            "SUM(p.prezzo * dop.quantita) AS incasso_totale " +
                            "FROM dettaglio_ordine_pietanza dop " +
                            "JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                            "JOIN categoria_pietanza cp ON p.id_categoria = cp.id_categoria " +
                            "JOIN ordine o ON dop.id_ordine = o.id_ordine " +
                            "WHERE o.data_ordine BETWEEN ? AND ? " +
                            "GROUP BY p.nome, cp.nome " +
                            "ORDER BY incasso_totale DESC";

                    stmt = conn.prepareStatement(queryVenditePietanze);
                    stmt.setString(1, dataInizioStr);
                    stmt.setString(2, dataFineStr);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        tableModel.addRow(new Object[] {
                                rs.getString("nome"),
                                rs.getString("categoria"),
                                rs.getInt("quantita_totale"),
                                String.format("%.2f", rs.getDouble("incasso_totale"))
                        });
                    }
                    break;

                case "Ingredienti da Ordinare":
                    // Clear the table model
                    tableModel = new DefaultTableModel(
                            new String[] { "Ingrediente", "Quantità Disponibile", "Unità di Misura",
                                    "Soglia di Riordino", "Stato" },
                            0);

                    String queryIngredienti = "SELECT nome, quantita_disponibile, unita_misura, soglia_riordino " +
                            "FROM ingrediente " +
                            "ORDER BY (quantita_disponibile <= soglia_riordino) DESC, nome";

                    stmt = conn.prepareStatement(queryIngredienti);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        String stato = rs.getDouble("quantita_disponibile") <= rs.getDouble("soglia_riordino")
                                ? "DA ORDINARE"
                                : "OK";

                        tableModel.addRow(new Object[] {
                                rs.getString("nome"),
                                rs.getDouble("quantita_disponibile"),
                                rs.getString("unita_misura"),
                                rs.getDouble("soglia_riordino"),
                                stato
                        });
                    }
                    break;

                case "Incasso Giornaliero":
                    // Clear the table model
                    tableModel = new DefaultTableModel(
                            new String[] { "Data", "Numero Ordini", "Numero Clienti", "Incasso Pietanze (€)",
                                    "Incasso Menu (€)", "Coperti (€)", "Totale (€)" },
                            0);

                    String queryIncasso = "SELECT DATE(o.data_ordine) AS data, COUNT(DISTINCT o.id_ordine) AS numero_ordini, "
                            +
                            "SUM(o.num_persone) AS numero_clienti, " +
                            "COALESCE(SUM(p.prezzo * dop.quantita), 0) AS incasso_pietanze, " +
                            "COALESCE(SUM(mf.prezzo * dom.quantita), 0) AS incasso_menu, " +
                            "SUM(o.num_persone) * (SELECT CAST(valore AS DECIMAL(10,2)) FROM configurazione WHERE chiave = 'costo_coperto') AS incasso_coperti "
                            +
                            "FROM ordine o " +
                            "LEFT JOIN dettaglio_ordine_pietanza dop ON o.id_ordine = dop.id_ordine " +
                            "LEFT JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                            "LEFT JOIN dettaglio_ordine_menu dom ON o.id_ordine = dom.id_ordine " +
                            "LEFT JOIN menu_fisso mf ON dom.id_menu = mf.id_menu " +
                            "WHERE o.data_ordine BETWEEN ? AND ? AND o.stato = 'pagato' " +
                            "GROUP BY DATE(o.data_ordine) " +
                            "ORDER BY data DESC";

                    stmt = conn.prepareStatement(queryIncasso);
                    stmt.setString(1, dataInizioStr);
                    stmt.setString(2, dataFineStr);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        double incassoPietanze = rs.getDouble("incasso_pietanze");
                        double incassoMenu = rs.getDouble("incasso_menu");
                        double incassoCoperti = rs.getDouble("incasso_coperti");
                        double totale = incassoPietanze + incassoMenu + incassoCoperti;

                        tableModel.addRow(new Object[] {
                                rs.getString("data"),
                                rs.getInt("numero_ordini"),
                                rs.getInt("numero_clienti"),
                                String.format("%.2f", incassoPietanze),
                                String.format("%.2f", incassoMenu),
                                String.format("%.2f", incassoCoperti),
                                String.format("%.2f", totale)
                        });
                    }
                    break;

                case "Analisi Menu Fissi":
                    // Clear the table model
                    tableModel = new DefaultTableModel(
                            new String[] { "Nome Menu", "Prezzo (€)", "Quantità Venduta", "Incasso Totale (€)" }, 0);

                    String queryMenuFissi = "SELECT mf.nome, mf.prezzo, SUM(dom.quantita) AS quantita_venduta, " +
                            "SUM(mf.prezzo * dom.quantita) AS incasso_totale " +
                            "FROM dettaglio_ordine_menu dom " +
                            "JOIN menu_fisso mf ON dom.id_menu = mf.id_menu " +
                            "JOIN ordine o ON dom.id_ordine = o.id_ordine " +
                            "WHERE o.data_ordine BETWEEN ? AND ? " +
                            "GROUP BY mf.nome, mf.prezzo " +
                            "ORDER BY quantita_venduta DESC";

                    stmt = conn.prepareStatement(queryMenuFissi);
                    stmt.setString(1, dataInizioStr);
                    stmt.setString(2, dataFineStr);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        tableModel.addRow(new Object[] {
                                rs.getString("nome"),
                                String.format("%.2f", rs.getDouble("prezzo")),
                                rs.getInt("quantita_venduta"),
                                String.format("%.2f", rs.getDouble("incasso_totale"))
                        });
                    }
                    break;
            }

            // Aggiorno la tabella con il nuovo modello
            reportTable.setModel(tableModel);

            // Mostro un messaggio di successo
            JOptionPane.showMessageDialog(this,
                    "Report generato con successo",
                    "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la generazione del report: " + e.getMessage(),
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
                new DirettoreForm().setVisible(true);
            }
        });
    }
}
