package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import CFG.DBConnection;

/**
 * Schermata per il cassiere che permette di calcolare il conto di un tavolo
 */
public class CassiereForm extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> tavoliComboBox;
    private JTextArea dettagliContoTextArea;
    private JLabel totaleLabel;
    private JTextField coppertiField;
    private JButton calcolaButton;
    private JButton stampaContoButton;
    private JButton pagaButton;
    private JButton indietroButton;

    public CassiereForm() {
        setTitle("Gestione Ristorante - Cassiere");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Pannello superiore con titolo
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("GESTIONE CASSA");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Pannello centrale con i controlli
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pannello per la selezione del tavolo
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel tavoloLabel = new JLabel("Seleziona Tavolo:");
        tavoliComboBox = new JComboBox<>();
        JLabel coppertiLabel = new JLabel("Coperti:");
        coppertiField = new JTextField(5);

        selectionPanel.add(tavoloLabel);
        selectionPanel.add(tavoliComboBox);
        selectionPanel.add(Box.createHorizontalStrut(20)); // Spazio
        selectionPanel.add(coppertiLabel);
        selectionPanel.add(coppertiField);

        // Pannello per i dettagli del conto
        JPanel dettagliPanel = new JPanel(new BorderLayout());
        dettagliPanel.setBorder(BorderFactory.createTitledBorder("Dettagli Conto"));
        dettagliContoTextArea = new JTextArea(15, 40);
        dettagliContoTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(dettagliContoTextArea);
        dettagliPanel.add(scrollPane, BorderLayout.CENTER);

        // Pannello per il totale
        JPanel totalePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totaleLabel = new JLabel("TOTALE: € 0,00");
        totaleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalePanel.add(totaleLabel);

        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        calcolaButton = new JButton("Calcola Conto");
        stampaContoButton = new JButton("Stampa Conto");
        pagaButton = new JButton("Registra Pagamento");
        indietroButton = new JButton("Indietro");

        buttonPanel.add(calcolaButton);
        buttonPanel.add(stampaContoButton);
        buttonPanel.add(pagaButton);
        buttonPanel.add(indietroButton);

        // Assemblo i pannelli
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(selectionPanel, BorderLayout.NORTH);
        contentPanel.add(dettagliPanel, BorderLayout.CENTER);
        contentPanel.add(totalePanel, BorderLayout.SOUTH);

        centerPanel.add(contentPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Aggiungo i pannelli principali
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setLocationRelativeTo(null);

        // Carico i tavoli disponibili
        caricaTavoli();

        // Event listeners
        calcolaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcolaConto();
            }
        });

        stampaContoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(CassiereForm.this,
                        "Stampa conto inviata alla stampante",
                        "Stampa", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        pagaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registraPagamento();
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Carica i tavoli occupati dal database
     */
    private void caricaTavoli() {
        tavoliComboBox.removeAllItems();

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            String query = "SELECT t.numero_tavolo, o.id_ordine FROM tavolo t " +
                    "JOIN ordine o ON t.id_tavolo = o.id_tavolo " +
                    "WHERE t.stato = 'occupato' AND o.stato != 'pagato'";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int numeroTavolo = rs.getInt("numero_tavolo");
                int idOrdine = rs.getInt("id_ordine");
                tavoliComboBox.addItem(numeroTavolo + " (Ordine: " + idOrdine + ")");
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
     * Calcola il conto per il tavolo selezionato
     */
    private void calcolaConto() {
        if (tavoliComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un tavolo",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = (String) tavoliComboBox.getSelectedItem();
        int idOrdine = Integer.parseInt(selectedItem.split("\\(Ordine: ")[1].replace(")", ""));

        int numeroCoperti;
        try {
            numeroCoperti = Integer.parseInt(coppertiField.getText().trim());
            if (numeroCoperti < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Inserisci un numero valido di coperti",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            double costoCoperto = 0;
            double totalePietanze = 0;
            double totaleMenus = 0;

            // Recupero il costo del coperto dalle configurazioni
            String queryCoperto = "SELECT valore FROM configurazione WHERE chiave = 'costo_coperto'";
            PreparedStatement stmtCoperto = conn.prepareStatement(queryCoperto);
            ResultSet rsCoperto = stmtCoperto.executeQuery();
            if (rsCoperto.next()) {
                costoCoperto = Double.parseDouble(rsCoperto.getString("valore"));
            }
            rsCoperto.close();
            stmtCoperto.close();

            // Recupero le pietanze ordinate
            String queryPietanze = "SELECT p.nome, p.prezzo, dop.quantita " +
                    "FROM dettaglio_ordine_pietanza dop " +
                    "JOIN pietanza p ON dop.id_pietanza = p.id_pietanza " +
                    "WHERE dop.id_ordine = ?";
            PreparedStatement stmtPietanze = conn.prepareStatement(queryPietanze);
            stmtPietanze.setInt(1, idOrdine);
            ResultSet rsPietanze = stmtPietanze.executeQuery();

            StringBuilder dettagli = new StringBuilder();
            dettagli.append("DETTAGLIO CONTO\n");
            dettagli.append("===============================\n\n");
            dettagli.append("PIETANZE:\n");

            // Aggiungo le pietanze al dettaglio
            while (rsPietanze.next()) {
                String nome = rsPietanze.getString("nome");
                double prezzo = rsPietanze.getDouble("prezzo");
                int quantita = rsPietanze.getInt("quantita");
                double subtotale = prezzo * quantita;

                dettagli.append(String.format("%-30s %2d x €%6.2f = €%7.2f\n",
                        nome, quantita, prezzo, subtotale));

                totalePietanze += subtotale;
            }
            rsPietanze.close();
            stmtPietanze.close();

            // Recupero i menu fissi ordinati
            String queryMenus = "SELECT m.nome, m.prezzo, dom.quantita " +
                    "FROM dettaglio_ordine_menu dom " +
                    "JOIN menu_fisso m ON dom.id_menu = m.id_menu " +
                    "WHERE dom.id_ordine = ?";
            PreparedStatement stmtMenus = conn.prepareStatement(queryMenus);
            stmtMenus.setInt(1, idOrdine);
            ResultSet rsMenus = stmtMenus.executeQuery();

            dettagli.append("\nMENU FISSI:\n");

            // Aggiungo i menu fissi al dettaglio
            while (rsMenus.next()) {
                String nome = rsMenus.getString("nome");
                double prezzo = rsMenus.getDouble("prezzo");
                int quantita = rsMenus.getInt("quantita");
                double subtotale = prezzo * quantita;

                dettagli.append(String.format("%-30s %2d x €%6.2f = €%7.2f\n",
                        nome, quantita, prezzo, subtotale));

                totaleMenus += subtotale;
            }
            rsMenus.close();
            stmtMenus.close();

            // Calcolo il totale complessivo
            double importoCoperti = costoCoperto * numeroCoperti;
            double totaleComplessivo = totalePietanze + totaleMenus + importoCoperti;

            dettagli.append("\n===============================\n");
            dettagli.append(String.format("%-30s %2d x €%6.2f = €%7.2f\n",
                    "COPERTI", numeroCoperti, costoCoperto, importoCoperti));
            dettagli.append("===============================\n");
            dettagli.append(String.format("%-42s €%7.2f\n", "TOTALE PIETANZE:", totalePietanze));
            dettagli.append(String.format("%-42s €%7.2f\n", "TOTALE MENU:", totaleMenus));
            dettagli.append(String.format("%-42s €%7.2f\n", "TOTALE COPERTI:", importoCoperti));
            dettagli.append("===============================\n");
            dettagli.append(String.format("%-42s €%7.2f\n", "TOTALE:", totaleComplessivo));

            // Aggiorno l'interfaccia
            dettagliContoTextArea.setText(dettagli.toString());
            totaleLabel.setText(String.format("TOTALE: € %.2f", totaleComplessivo));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il calcolo del conto: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registra il pagamento dell'ordine
     */
    private void registraPagamento() {
        if (tavoliComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un tavolo",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = (String) tavoliComboBox.getSelectedItem();
        int idOrdine = Integer.parseInt(selectedItem.split("\\(Ordine: ")[1].replace(")", ""));
        int numeroTavolo = Integer.parseInt(selectedItem.split(" ")[0]);

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            // Aggiorno lo stato dell'ordine a pagato
            String queryOrdine = "UPDATE ordine SET stato = 'pagato' WHERE id_ordine = ?";
            PreparedStatement stmtOrdine = conn.prepareStatement(queryOrdine);
            stmtOrdine.setInt(1, idOrdine);
            stmtOrdine.executeUpdate();
            stmtOrdine.close();

            // Aggiorno lo stato del tavolo a libero
            String queryTavolo = "UPDATE tavolo SET stato = 'libero' WHERE numero_tavolo = ?";
            PreparedStatement stmtTavolo = conn.prepareStatement(queryTavolo);
            stmtTavolo.setInt(1, numeroTavolo);
            stmtTavolo.executeUpdate();
            stmtTavolo.close();

            JOptionPane.showMessageDialog(this,
                    "Pagamento registrato con successo.",
                    "Pagamento completato", JOptionPane.INFORMATION_MESSAGE);

            // Aggiorno la lista dei tavoli
            caricaTavoli();

            // Resetto i campi
            dettagliContoTextArea.setText("");
            totaleLabel.setText("TOTALE: € 0,00");
            coppertiField.setText("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il pagamento: " + e.getMessage(),
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
                new CassiereForm().setVisible(true);
            }
        });
    }
}
