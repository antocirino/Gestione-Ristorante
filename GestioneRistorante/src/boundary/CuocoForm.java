package boundary;

// Import specifico per evitare ambiguità
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import DTO.DTOIngredienteCuoco;
import DTO.DTOIngredientiRicettaPietanza;
import DTO.DTOOrdine;
import DTO.DTOPietanzaCuoco;
import control.Controller;
import utility.SvgIconManager;

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

    // Colori e font moderni - coerenti con il design system
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

    // Enums per gli stati degli ordini
    private enum StatoOrdine {
        IN_ATTESA("in_attesa", "In attesa"),
        IN_PREPARAZIONE("in_preparazione", "In preparazione"),
        CONFERMATO("confermato", "Confermato"),
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

    /**
     * Costruttore della schermata CuocoForm
     */
    public CuocoForm() {
        setTitle("Gestione Ristorante - Cuoco");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(lightColor);

        // Pannello superiore con titolo moderno
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("GESTIONE CUCINA - CUOCO");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);

        // Aggiunge l'icona SVG al titolo
        ImageIcon svgIcon = SvgIconManager.loadSVGIcon("restaurant_menu.svg", 32, 32);
        JLabel iconLabel = new JLabel(svgIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(primaryColor);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Pannello centrale diviso in due parti con stile moderno
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        splitPane.setBackground(lightColor);

        // Pannello superiore con la lista degli ordini
        JPanel ordiniPanel = createModernPanel();
        ordiniPanel.setLayout(new BorderLayout(0, 15));

        JLabel ordiniTitleLabel = new JLabel("Ordini da Preparare");
        ordiniTitleLabel.setFont(headerFont);
        ordiniTitleLabel.setForeground(textColor);
        ordiniTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        DefaultTableModel ordiniTableModel = new DefaultTableModel(
                new String[] { "ID Ordine", "Tavolo", "Persone", "Ora", "Stato" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordiniTable = new JTable(ordiniTableModel);
        styleTable(ordiniTable);
        ordiniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordiniTable.getColumnModel().getColumn(0).setMaxWidth(80);
        ordiniTable.getColumnModel().getColumn(1).setMaxWidth(80);
        ordiniTable.getColumnModel().getColumn(2).setMaxWidth(80);
        ordiniTable.getColumnModel().getColumn(3).setMaxWidth(100);

        JScrollPane ordiniScrollPane = new JScrollPane(ordiniTable);
        styleScrollPane(ordiniScrollPane);

        ordiniPanel.add(ordiniTitleLabel, BorderLayout.NORTH);
        ordiniPanel.add(ordiniScrollPane, BorderLayout.CENTER);

        // Pannello inferiore con i dettagli dell'ordine
        JPanel dettagliPanel = createModernPanel();
        dettagliPanel.setLayout(new BorderLayout(0, 15));

        JLabel dettagliTitleLabel = new JLabel("Dettaglio Ordine Selezionato");
        dettagliTitleLabel.setFont(headerFont);
        dettagliTitleLabel.setForeground(textColor);
        dettagliTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        DefaultTableModel dettagliTableModel = new DefaultTableModel(
                new String[] { "Tipo", "Nome", "Quantità", "Note" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dettagliTable = new JTable(dettagliTableModel);
        styleTable(dettagliTable);
        dettagliTable.getColumnModel().getColumn(0).setMaxWidth(100);
        dettagliTable.getColumnModel().getColumn(2).setMaxWidth(80);

        JScrollPane dettagliScrollPane = new JScrollPane(dettagliTable);
        styleScrollPane(dettagliScrollPane);

        dettagliPanel.add(dettagliTitleLabel, BorderLayout.NORTH);
        dettagliPanel.add(dettagliScrollPane, BorderLayout.CENTER);

        // Pannello pulsanti per la gestione degli ordini
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Crea i pulsanti
        indietroButton = createStyledButton("← Indietro", dangerColor);
        iniziaPreparazioneButton = createStyledButton("Inizia Preparazione", warningColor);
        segnaCompletatoButton = createStyledButton("Segna come Pronto", successColor);
        segnaConsegnatoButton = createStyledButton("Segna come Consegnato", primaryColor);
        visualizzaIngredientiButton = createStyledButton("Visualizza Dettagli", infoColor);

        // Aggiungi tutti i pulsanti nello stesso ordine
        bottomPanel.add(indietroButton);
        bottomPanel.add(iniziaPreparazioneButton);
        bottomPanel.add(segnaCompletatoButton);
        bottomPanel.add(segnaConsegnatoButton);
        bottomPanel.add(visualizzaIngredientiButton);

        // Assemblaggio pannelli
        splitPane.setTopComponent(ordiniPanel);
        splitPane.setBottomComponent(dettagliPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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
                // Chiude la finestra generando un evento di chiusura
                CuocoForm.this.dispatchEvent(new java.awt.event.WindowEvent(
                        CuocoForm.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });

        // Carica i dati iniziali
        ArrayList<DTOOrdine> ordini_in_attesa = Controller.getOrdiniByStato("confermato");
        ArrayList<DTOOrdine> ordini_in_preparazione = Controller.getOrdiniByStato("in_preparazione");
        ArrayList<DTOOrdine> ordini_pronti = Controller.getOrdiniByStato("pronto");

        // Unisci tutte le liste
        ordini_in_attesa.addAll(ordini_in_preparazione);
        ordini_in_attesa.addAll(ordini_pronti);

        // Stampa tutti gli ordini (in attesa + in preparazione + pronti)
        stampaOrdini(ordini_in_attesa);
        aggiornaStatoButton();

        // Imposta un timer per aggiornare gli ordini ogni 30 secondi
        Timer timer = new Timer(30000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<DTOOrdine> ordini_in_attesa = Controller.getOrdiniByStato("confermato");
                ArrayList<DTOOrdine> ordini_in_preparazione = Controller.getOrdiniByStato("in_preparazione");
                ArrayList<DTOOrdine> ordini_pronti = Controller.getOrdiniByStato("pronto");

                // Unisci tutte le liste
                ordini_in_attesa.addAll(ordini_in_preparazione);
                ordini_in_attesa.addAll(ordini_pronti);

                // Stampa tutti gli ordini (in attesa + in preparazione + pronti)
                stampaOrdini(ordini_in_attesa);
                aggiornaStatoButton();
            }
        });
        timer.start();
    }

    /**
     * Stampa gli ordini da preparare sulla tabella
     */
    private void stampaOrdini(ArrayList<DTOOrdine> ordini) {
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

        for (DTOOrdine ordine : ordini) {
            int idOrdine = ordine.getIdOrdine();
            int numeroTavolo = ordine.getIdTavolo();
            int numPersone = ordine.getNumPersone();
            java.util.Date dataOrdine = ordine.getDataOrdine();
            String ora = dataOrdine != null ? sdf.format(dataOrdine) : "";
            String stato = ordine.getStato();

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
    }

    /**
     * Carica i dettagli dell'ordine selezionato
     */
    private void caricaDettagliOrdine() {
        int selectedRow = ordiniTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        int idOrdine = Integer.parseInt(ordiniTable.getValueAt(selectedRow, 0).toString());

        // Svuoto la tabella dettagli
        DefaultTableModel model = (DefaultTableModel) dettagliTable.getModel();
        model.setRowCount(0);

        // Carico le pietanze dell'ordine dal Controller
        ArrayList<DTOPietanzaCuoco> pietanze = Controller.getPietanzeDaOrdine(idOrdine);
        for (DTOPietanzaCuoco p : pietanze) {
            model.addRow(new Object[] {
                    "Pietanza",
                    p.getNome(),
                    p.getQuantita(),
                    "" // Campo note vuoto
            });
        }
        // NON CARICHIAMO I MENU FISSI, PERCHE LE PIETANZE DEL MENU FISSO VENGONO
        // CARICATE GIA DA Controller.getPietanzeDaOrdine(idOrdine);

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

        if (stato.equals(StatoOrdine.CONFERMATO.getDescrizione())) {
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
        Controller.aggiornaStatoOrdine(idOrdine, nuovoStato.getCodice());

        JOptionPane.showMessageDialog(this,
                "Stato dell'ordine aggiornato con successo",
                "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

        // Ricarica gli ordini dopo l'aggiornamento
        ArrayList<DTOOrdine> ordini_in_attesa = Controller.getOrdiniByStato("confermato");
        ArrayList<DTOOrdine> ordini_in_preparazione = Controller.getOrdiniByStato("in_preparazione");
        ArrayList<DTOOrdine> ordini_pronti = Controller.getOrdiniByStato("pronto");

        // Unisci tutte le liste
        ordini_in_attesa.addAll(ordini_in_preparazione);
        ordini_in_attesa.addAll(ordini_pronti);

        // Stampa tutti gli ordini (in attesa + in preparazione + pronti)
        stampaOrdini(ordini_in_attesa);
        aggiornaStatoButton();
    }

    /**
     * Visualizza gli ingredienti necessari per preparare l'ordine selezionato
     */
    private void visualizzaIngredienti() {
        // Verifica se è stata selezionata una pietanza dalla tabella dettagli
        int selectedRow = dettagliTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona una pietanza dalla tabella dei dettagli",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verifica se l'elemento selezionato è una pietanza
        String tipo = dettagliTable.getValueAt(selectedRow, 0).toString();
        String nomeItem = dettagliTable.getValueAt(selectedRow, 1).toString();

        if (tipo.equalsIgnoreCase("Pietanza")) {
            // Usa il Controller per ottenere i dettagli di preparazione
            DTOIngredientiRicettaPietanza dettagli = Controller.getDettagliPreparazionePietanza(nomeItem);

            if (dettagli == null) {
                JOptionPane.showMessageDialog(this, "Pietanza non trovata nel database", "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crea una visualizzazione formattata dei dettagli
            StringBuilder result = new StringBuilder();
            result.append("DETTAGLI DI PREPARAZIONE: " + nomeItem.toUpperCase() + "\n\n");
            result.append("--------------------------\n\n");
            result.append("INGREDIENTI NECESSARI:\n");

            for (DTOIngredienteCuoco ingrediente : dettagli.getIngredienti()) {
                result.append(String.format("- %s: %.2f %s\n",
                        ingrediente.getNome(),
                        ingrediente.getQta_necessaria(),
                        ingrediente.getUnitaMisura()));
            }

            result.append("\n--------------------------\n");
            result.append("\nISTRUZIONI DI PREPARAZIONE:\n");
            result.append(dettagli.getIstruzioni());

            // Mostra il risultato
            JTextArea textArea = new JTextArea(result.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Dettagli di Preparazione - " + dettagli.getDescrizione(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Se non è una pietanza, mostra un messaggio di errore
            JOptionPane.showMessageDialog(this,
                    "Seleziona una pietanza per visualizzare i dettagli di preparazione",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
        }

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

        // Colori per stato normale e disabilitato
        final Color normalBackground = backgroundColor;
        final Color disabledBackground = createDisabledColor(backgroundColor);
        final Color normalBorder = backgroundColor.darker();
        final Color disabledBorder = createDisabledColor(backgroundColor.darker());

        // Listener per gestire il cambio di stato enabled/disabled
        button.addPropertyChangeListener("enabled", e -> {
            boolean enabled = (Boolean) e.getNewValue();
            if (enabled) {
                // Stato normale
                button.setBackground(normalBackground);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(normalBorder, 2, true),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                button.setForeground(Color.WHITE);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                // Stato disabilitato con opacità ridotta
                button.setBackground(disabledBackground);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(disabledBorder, 2, true),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                button.setForeground(new Color(255, 255, 255, 120)); // Testo bianco semi-trasparente
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Effetti hover (solo se il pulsante è abilitato)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normalBackground.brighter());
                    button.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(normalBackground.darker().darker(), 2, true),
                            BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normalBackground);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(normalBackground.darker(), 2, true),
                            BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normalBackground.darker());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.isEnabled()) {
                    if (button.contains(e.getPoint())) {
                        button.setBackground(normalBackground.brighter());
                    } else {
                        button.setBackground(normalBackground);
                    }
                }
            }
        });

        return button;
    }

    /**
     * Crea un colore disabilitato basato su un colore originale
     * Riduce la saturazione e aumenta la luminosità per un effetto "spento"
     * 
     * @param originalColor Il colore originale da disabilitare
     * @return Il colore disabilitato
     */
    private Color createDisabledColor(Color originalColor) {
        // Riduce la saturazione e aumenta la luminosità per un effetto "spento"
        float[] hsb = Color.RGBtoHSB(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), null);

        // Riduce saturazione del 60% e aumenta luminosità del 20%
        float newSaturation = hsb[1] * 0.4f;
        float newBrightness = Math.min(1.0f, hsb[2] + 0.2f);

        Color desaturatedColor = Color.getHSBColor(hsb[0], newSaturation, newBrightness);

        // Applica anche un po' di trasparenza (alpha channel)
        return new Color(desaturatedColor.getRed(), desaturatedColor.getGreen(),
                desaturatedColor.getBlue(), 150); // Alpha = 150 (circa 60% opacità)
    }

    /**
     * Applica lo stile moderno alla tabella
     * Imposta font, colori, altezza riga e header
     * 
     * @param table La tabella da stilizzare
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
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
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
                new CuocoForm().setVisible(true);
            }
        });
    }
}
