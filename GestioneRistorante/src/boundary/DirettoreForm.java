package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "Ingredienti da Ordinare"
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
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt;
            ResultSet rs;

            // Impostiamo il modello per il report degli ingredienti da ordinare
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
