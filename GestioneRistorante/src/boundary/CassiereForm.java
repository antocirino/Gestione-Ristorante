package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import control.Controller;
import entity.EntityTavolo;

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
                stampaContoTavolo();
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
     * Carica tutti i tavoli dal database con il loro stato attuale
     */
    private void caricaTavoli() {
        tavoliComboBox.removeAllItems();

        try {
            Controller controller = Controller.getInstance();
            List<EntityTavolo> tavoli = controller.getAllTavoli();

            for (EntityTavolo tavolo : tavoli) {
                int idTavolo = tavolo.getIdTavolo();
                int maxPosti = tavolo.getMaxPosti();
                boolean occupato = tavolo.isOccupato();
                String displayText = "";

                if (!occupato) {
                    displayText = "Tavolo " + idTavolo + " (" + maxPosti + " posti) - LIBERO";
                } else {
                    displayText = "Tavolo " + idTavolo + " (" + maxPosti + " posti) - OCCUPATO";
                }

                tavoliComboBox.addItem(displayText);
            }
        } catch (Exception e) {
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
        // Estraiamo l'ID del tavolo dal formato "Tavolo X (Y posti) - STATO"
        String[] parts = selectedItem.split(" ");
        int idTavolo = Integer.parseInt(parts[1]);

        // Cancello il contenuto precedente
        pulisciCampi();
        coppertiField.setEditable(false);

        Controller controller = Controller.getInstance();
        Map<String, Object> risultatoConto = controller.getContoTavolo(idTavolo);

        if (risultatoConto.containsKey("errore")) {
            dettagliContoTextArea.setText((String) risultatoConto.get("errore"));
            return;
        }

        if ((Boolean) risultatoConto.get("success")) {
            // Imposta i dettagli del conto
            dettagliContoTextArea.setText((String) risultatoConto.get("dettagli"));

            // Imposta il totale
            double totale = (Double) risultatoConto.get("totale");
            totaleLabel.setText(String.format("Totale: € %.2f", totale));

            // Imposta il numero di coperti
            int numeroPersone = (Integer) risultatoConto.get("numeroPersone");
            coppertiField.setText(String.valueOf(numeroPersone));

            // Abilita i pulsanti
            stampaContoButton.setEnabled(true);
            pagaButton.setEnabled(true);
        } else {
            dettagliContoTextArea.setText("Si è verificato un errore nel calcolo del conto.");
            totaleLabel.setText("Totale: € 0.00");
            stampaContoButton.setEnabled(false);
            pagaButton.setEnabled(false);
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
        // Estraiamo l'ID del tavolo dal formato "Tavolo X (Y posti) - STATO"
        String[] parts = selectedItem.split(" ");
        int idTavolo = Integer.parseInt(parts[1]);

        // Verifica se il tavolo è libero
        if (selectedItem.contains("LIBERO")) {
            JOptionPane.showMessageDialog(this,
                    "Il tavolo selezionato è libero. Non ci sono ordini da pagare.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Conferma del pagamento
        int conferma = JOptionPane.showConfirmDialog(this,
                "Confermi il pagamento del conto?",
                "Conferma pagamento", JOptionPane.YES_NO_OPTION);

        if (conferma != JOptionPane.YES_OPTION) {
            return;
        }

        Controller controller = Controller.getInstance();
        boolean success = controller.registraPagamentoOrdine(idTavolo);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Pagamento registrato con successo!\nIl tavolo è stato liberato.",
                    "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

            // Aggiorno la lista dei tavoli
            caricaTavoli();

            // Pulisco i campi
            pulisciCampi();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la registrazione del pagamento.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Stampa il conto del tavolo selezionato
     */
    private void stampaContoTavolo() {
        if (tavoliComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un tavolo",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = (String) tavoliComboBox.getSelectedItem();
        // Estraiamo l'ID del tavolo dal formato "Tavolo X (Y posti) - STATO"
        String[] parts = selectedItem.split(" ");
        int idTavolo = Integer.parseInt(parts[1]);

        // Verifica se il tavolo è libero
        if (selectedItem.contains("LIBERO")) {
            JOptionPane.showMessageDialog(this,
                    "Il tavolo selezionato è libero. Non ci sono ordini da stampare.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Controller controller = Controller.getInstance();
        boolean success = controller.stampaConto(idTavolo);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Conto stampato con successo!",
                    "Informazione", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Impossibile stampare il conto. Nessun ordine attivo trovato.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pulisce i campi dell'interfaccia
     */
    private void pulisciCampi() {
        dettagliContoTextArea.setText("");
        totaleLabel.setText("TOTALE: € 0,00");
        coppertiField.setText("");
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
