package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import DTO.DTOIngrediente;
import control.Controller;
import utility.SvgIconManager;

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

    // Colori e font moderni - design system consistente
    private Color primaryColor = new Color(41, 128, 185); // Blu principale
    private Color accentColor = new Color(52, 152, 219); // Blu accent
    private Color textColor = new Color(44, 62, 80); // Grigio scuro
    private Color lightColor = new Color(236, 240, 241); // Grigio chiaro
    private Color successColor = new Color(39, 174, 96); // Verde
    private Color warningColor = new Color(230, 126, 34); // Arancione
    private Color dangerColor = new Color(192, 57, 43); // Rosso
    private Color infoColor = new Color(52, 73, 94); // Grigio scuro per header
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
    private Font regularFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 16);

    /**
     * Costruttore della schermata DirettoreForm
     * Inizializza i componenti e li dispone in un layout moderno
     */
    public DirettoreForm() {
        setTitle("Gestione Ristorante - Pannello Direttore");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(lightColor);

        // Pannello superiore con titolo moderno
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("PANNELLO DIRETTORE - REPORT E MONITORAGGIO");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);

        // Aggiunge l'icona SVG al titolo
        ImageIcon svgIcon = SvgIconManager.loadSVGIcon("admin_panel_settings.svg", 32, 32);
        JLabel iconLabel = new JLabel(svgIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(primaryColor);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Pannello centrale con contenuto
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        centerPanel.setBackground(lightColor);

        // Pannello di controllo superiore
        JPanel controlPanel = createModernPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        JLabel tipoReportLabel = new JLabel("Tipo di Report:");
        tipoReportLabel.setFont(boldFont);
        tipoReportLabel.setForeground(textColor);

        reportTypeComboBox = new JComboBox<>(new String[] {
                "Ingredienti da Ordinare"
        });
        styleComboBox(reportTypeComboBox);
        reportTypeComboBox.setPreferredSize(new Dimension(250, 40));

        /*
         * Commentato temporaneamente il filtro del periodo
         * JLabel periodoLabel = new JLabel("Periodo:");
         * periodoLabel.setFont(boldFont);
         * periodoLabel.setForeground(textColor);
         * periodoLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
         * 
         * periodoComboBox = new JComboBox<>(new String[] {
         * "Oggi",
         * "Ultima Settimana",
         * "Ultimo Mese",
         * "Ultimo Anno"
         * });
         * styleComboBox(periodoComboBox);
         * periodoComboBox.setPreferredSize(new Dimension(200, 40));
         */

        generaReportButton = createStyledButton("Genera Report", primaryColor);

        controlPanel.add(tipoReportLabel);
        controlPanel.add(reportTypeComboBox);
        /*
         * Commentato temporaneamente il filtro del periodo
         * controlPanel.add(periodoLabel);
         * controlPanel.add(periodoComboBox);
         */
        controlPanel.add(generaReportButton);

        // Pannello principale per contenuto centrale
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 20));
        mainContentPanel.setBackground(lightColor);

        // Pannello tabella con stile moderno
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel reportTitle = new JLabel("Report Generato");
        reportTitle.setFont(headerFont);
        reportTitle.setForeground(textColor);
        reportTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Creo il modello di tabella vuoto
        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        styleTable(reportTable);
        reportTable.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        styleScrollPane(tableScrollPane);

        tablePanel.add(reportTitle, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Pannello grafico moderno
        graphPanel = new JPanel();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        graphPanel.setPreferredSize(new Dimension(800, 180));

        JLabel graphTitle = new JLabel("Visualizzazione Grafica");
        graphTitle.setFont(headerFont);
        graphTitle.setForeground(textColor);
        graphTitle.setHorizontalAlignment(SwingConstants.CENTER);
        graphPanel.add(graphTitle);

        mainContentPanel.add(tablePanel, BorderLayout.CENTER);
        mainContentPanel.add(graphPanel, BorderLayout.SOUTH);

        // Pannello bottoni con stile moderno
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Pannello pulsanti azioni
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionButtonPanel.setBackground(new Color(245, 247, 250));

        stampaReportButton = createStyledButton("Stampa Report", warningColor);

        actionButtonPanel.add(stampaReportButton);

        // Pannello pulsante indietro
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonPanel.setBackground(new Color(245, 247, 250));

        indietroButton = createStyledButton("← Indietro", dangerColor);
        backButtonPanel.add(indietroButton);

        buttonPanel.add(backButtonPanel, BorderLayout.WEST);
        buttonPanel.add(actionButtonPanel, BorderLayout.CENTER);

        // Assemblaggio pannelli
        centerPanel.add(controlPanel, BorderLayout.NORTH);
        centerPanel.add(mainContentPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);

        // Event listeners
        generaReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<DTOIngrediente> ingredienti = Controller.generaReport();
                stampaReport(ingredienti);
            }
        });

        stampaReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DirettoreForm.this,
                        "Report inviato alla stampante con successo!",
                        "Stampa Completata", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Chiude la finestra generando un evento di chiusura
                DirettoreForm.this.dispatchEvent(new java.awt.event.WindowEvent(
                        DirettoreForm.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    /**
     * Crea un pulsante con stile moderno
     * Imposta font, colori, bordi e effetti hover
     * 
     * @param text            Il testo del pulsante
     * @param backgroundColor Il colore di sfondo del pulsante
     * @return Il pulsante stilizzato
     */
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(boldFont);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(backgroundColor.darker(), 2, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effetti hover
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
     * Applica lo stile moderno alla tabella
     * Imposta font, colori, altezza riga e header
     * 
     * @param table
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
        table.getTableHeader().setBackground(infoColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    /**
     * Applica lo stile moderno allo JScrollPane
     * Imposta bordi, colore di sfondo e padding
     * 
     * @param scrollPane Lo JScrollPane da stilizzare
     */
    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    /**
     * Applica lo stile moderno alla JComboBox
     * Imposta font, colori, bordi e renderer personalizzato
     * 
     * @param comboBox La JComboBox da stilizzare
     */
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(regularFont);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(textColor);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(149, 165, 166), 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Migliora l'aspetto del dropdown
        comboBox.setOpaque(true);

        // Personalizza il renderer
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

        // Aggiungi effetti hover
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
     * Crea un pannello con uno stile moderno
     * Imposta colore di sfondo, bordi e padding
     * 
     * @return Il pannello stilizzato
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
     * Genera il report in base alle selezioni dell'utente
     */
    private void stampaReport(ArrayList<DTOIngrediente> ingredienti) {
        // Imposta il modello per il report degli ingredienti da ordinare
        tableModel = new DefaultTableModel(
                new String[] { "Ingrediente", "Quantità Disponibile", "Unità di Misura",
                        "Soglia di Riordino", "Stato" },
                0);

        int ingredientiDaOrdinare = 0;
        int totaleIngredienti = 0;

        for (DTOIngrediente ingr : ingredienti) {
            String stato = ingr.getQuantitaDisponibile() <= ingr.getSogliaRiordino()
                    ? "DA ORDINARE"
                    : "OK";

            if ("DA ORDINARE".equals(stato)) {
                ingredientiDaOrdinare++;
            }
            totaleIngredienti++;

            tableModel.addRow(new Object[] {
                    ingr.getNome(),
                    ingr.getQuantitaDisponibile(),
                    ingr.getUnitaMisura(),
                    ingr.getSogliaRiordino(),
                    stato
            });
        }

        // Aggiorna la tabella con il nuovo modello
        reportTable.setModel(tableModel);

        // Aggiorna il pannello grafico con statistiche
        updateGraphPanel(ingredientiDaOrdinare, totaleIngredienti);

        // Mostra un messaggio di successo
        String message = String.format("Report generato con successo!");
        JOptionPane.showMessageDialog(this,
                message,
                "Report Completato", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Aggiorna il pannello grafico con le statistiche
     */
    private void updateGraphPanel(int ingredientiDaOrdinare, int totaleIngredienti) {
        graphPanel.removeAll();
        graphPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Statistiche Ingredienti");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Pannello con le statistiche
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Statistica totale
        JPanel totalPanel = createStatPanel("Totale", String.valueOf(totaleIngredienti), primaryColor);

        // Statistica da ordinare
        JPanel orderPanel = createStatPanel("Da Ordinare", String.valueOf(ingredientiDaOrdinare), dangerColor);

        // Statistica disponibili
        int disponibili = totaleIngredienti - ingredientiDaOrdinare;
        JPanel availablePanel = createStatPanel("Disponibili", String.valueOf(disponibili), successColor);

        statsPanel.add(totalPanel);
        statsPanel.add(orderPanel);
        statsPanel.add(availablePanel);

        graphPanel.add(titleLabel, BorderLayout.NORTH);
        graphPanel.add(statsPanel, BorderLayout.CENTER);

        graphPanel.revalidate();
        graphPanel.repaint();
    }

    /**
     * Crea un pannello per una statistica
     * 
     * @param label Il testo dell'etichetta
     * @param value Il valore della statistica
     * @param color Il colore del valore
     * @return Il pannello con la statistica
     */
    private JPanel createStatPanel(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(regularFont);
        labelLabel.setForeground(textColor);
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(labelLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
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
