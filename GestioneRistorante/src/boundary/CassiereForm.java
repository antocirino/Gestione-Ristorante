package boundary;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import control.Controller;

// Importa la libreria SVG Salamander
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

import DTO.DTOOrdine;
import DTO.DTOTavolo;

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

    // Colori e font moderni - design system consistente
    private Color primaryColor = new Color(41, 128, 185); // Blu principale
    private Color accentColor = new Color(52, 152, 219); // Blu accent
    private Color textColor = new Color(44, 62, 80); // Grigio scuro
    private Color lightColor = new Color(236, 240, 241); // Grigio chiaro
    private Color successColor = new Color(39, 174, 96); // Verde
    private Color warningColor = new Color(230, 126, 34); // Arancione
    private Color dangerColor = new Color(192, 57, 43); // Rosso
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
    private Font regularFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 16);

    public CassiereForm() {
        setTitle("Gestione Ristorante - Cassiere");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(lightColor);

        // Pannello superiore con titolo moderno
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("GESTIONE CASSA");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);

        // Aggiunge l'icona SVG al titolo
        ImageIcon svgIcon = loadSVGIcon("payment.svg", 32, 32);
        JLabel iconLabel = new JLabel(svgIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(primaryColor);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Pannello centrale con i controlli
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        centerPanel.setBackground(lightColor);

        // Pannello per la selezione del tavolo
        JPanel selectionPanel = createModernPanel();
        selectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        // Icona tavolo SVG
        ImageIcon tavoloIcon = loadSVGIcon("table.svg", 20, 20, textColor);
        JLabel tavoloIconLabel = new JLabel(tavoloIcon);
        tavoloIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel tavoloLabel = new JLabel("Seleziona Tavolo:");
        tavoloLabel.setFont(boldFont);
        tavoloLabel.setForeground(textColor);

        tavoliComboBox = new JComboBox<>();
        styleComboBox(tavoliComboBox);
        tavoliComboBox.setPreferredSize(new Dimension(300, 40));

        JLabel coppertiLabel = new JLabel("Coperti:");
        coppertiLabel.setFont(boldFont);
        coppertiLabel.setForeground(textColor);
        coppertiLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        coppertiField = new JTextField(8);
        styleTextField(coppertiField);

        selectionPanel.add(tavoloIconLabel);
        selectionPanel.add(tavoloLabel);
        selectionPanel.add(tavoliComboBox);
        selectionPanel.add(coppertiLabel);
        selectionPanel.add(coppertiField);

        // Pannello centrale con dettagli e totale
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 20));
        mainContentPanel.setBackground(lightColor);

        // Pannello per i dettagli del conto
        JPanel dettagliPanel = new JPanel(new BorderLayout());
        dettagliPanel.setBackground(Color.WHITE);
        dettagliPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel dettagliTitle = new JLabel("Dettagli Conto");
        dettagliTitle.setFont(headerFont);
        dettagliTitle.setForeground(textColor);
        dettagliTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        dettagliContoTextArea = new JTextArea(18, 60);
        dettagliContoTextArea.setEditable(false);
        dettagliContoTextArea.setFont(regularFont);
        dettagliContoTextArea.setBackground(new Color(248, 249, 250));
        dettagliContoTextArea.setForeground(textColor);
        dettagliContoTextArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(dettagliContoTextArea);
        styleScrollPane(scrollPane);

        dettagliPanel.add(dettagliTitle, BorderLayout.NORTH);
        dettagliPanel.add(scrollPane, BorderLayout.CENTER);

        // Pannello per il totale
        JPanel totalePanel = new JPanel(new BorderLayout());
        totalePanel.setBackground(Color.WHITE);
        totalePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(successColor, 2),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        totaleLabel = new JLabel("TOTALE: € 0,00");
        totaleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totaleLabel.setForeground(successColor);
        totaleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        totalePanel.add(totaleLabel, BorderLayout.CENTER);

        mainContentPanel.add(dettagliPanel, BorderLayout.CENTER);
        mainContentPanel.add(totalePanel, BorderLayout.SOUTH);

        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        calcolaButton = createStyledButton("Calcola Conto", primaryColor);
        stampaContoButton = createStyledButton("Stampa Conto", warningColor);
        pagaButton = createStyledButton("Registra Pagamento", successColor);
        indietroButton = createStyledButton("← Indietro", dangerColor);

        // Inizialmente disabilita alcuni pulsanti
        stampaContoButton.setEnabled(false);
        pagaButton.setEnabled(false);

        buttonPanel.add(calcolaButton);
        buttonPanel.add(stampaContoButton);
        buttonPanel.add(pagaButton);
        buttonPanel.add(indietroButton);

        // Assemblaggio pannelli
        centerPanel.add(selectionPanel, BorderLayout.NORTH);
        centerPanel.add(mainContentPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

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
                // Chiude la finestra generando un evento di chiusura
                CassiereForm.this.dispatchEvent(new java.awt.event.WindowEvent(
                        CassiereForm.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    // Metodi di utilità per lo styling moderno

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
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));

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
                        BorderFactory.createEmptyBorder(15, 25, 15, 25)));
                button.setForeground(Color.WHITE);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                // Stato disabilitato con opacità ridotta
                button.setBackground(disabledBackground);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(disabledBorder, 2, true),
                        BorderFactory.createEmptyBorder(15, 25, 15, 25)));
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
                            BorderFactory.createEmptyBorder(15, 25, 15, 25)));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normalBackground);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(normalBackground.darker(), 2, true),
                            BorderFactory.createEmptyBorder(15, 25, 15, 25)));
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

    // Metodo helper per creare colori con opacità ridotta per lo stato disabilitato
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

    private void styleTextField(JTextField textField) {
        textField.setFont(regularFont);
        textField.setBackground(Color.WHITE);
        textField.setForeground(textColor);
        textField.setPreferredSize(new Dimension(100, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(149, 165, 166), 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        textField.setHorizontalAlignment(JTextField.CENTER);

        // Aggiungi effetti focus
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

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    private JPanel createModernPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        return panel;
    }

    /**
     * Carica tutti i tavoli dal database con stato = 'occupato'
     */
    private void caricaTavoli() {
        tavoliComboBox.removeAllItems();
        String stato = "occupato";

        try {

            ArrayList<DTOTavolo> tavoli = Controller.getTavoliByStato(stato);

            for (DTOTavolo tavolo : tavoli) {
                int idTavolo = tavolo.getIdTavolo();
                int maxPosti = tavolo.getMaxPosti();
                boolean occupato = tavolo.isOccupato();
                String displayText = "";

                displayText = idTavolo + " - Tavolo " + " (" + maxPosti + " posti) - OCCUPATO";
                tavoliComboBox.addItem(displayText);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei tavoli: " + e.getMessage(),
                    "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Calcola il conto del tavolo selezionato
     */
    private void calcolaConto() {
        if (tavoliComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un tavolo",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedItem = (String) tavoliComboBox.getSelectedItem();
        // Estraiamo l'ID del tavolo dal formato "ID - Tavolo X (Y posti) - STATO"
        String[] parts = selectedItem.split(" - ");
        int idTavolo = Integer.parseInt(parts[0]);

        // Cancello il contenuto precedente
        pulisciCampi();
        coppertiField.setEditable(false);

        DTOOrdine ordine = Controller.getOrdineByTavolo(idTavolo);
        System.out.println("Ordine recuperato: " + ordine);

        if (ordine == null || ordine.getIdOrdine() == 0) {
            dettagliContoTextArea.setText("Nessun ordine attivo per questo tavolo.");
            dettagliContoTextArea.setForeground(dangerColor);
            totaleLabel.setText("TOTALE: € 0,00");
            stampaContoButton.setEnabled(false);
            pagaButton.setEnabled(false);
            return;
        }

        // Mostra i dettagli dell'ordine (personalizza questa stringa come preferisci)
        String dettagli = String.format(
                "Ordine #%d\nData: %s\nNumero Persone: %d\nTotale: € %.2f",
                ordine.getIdOrdine(),
                ordine.getDataOrdine() != null ? ordine.getDataOrdine().toString() : "-",
                ordine.getNumPersone(),
                ordine.getCostoTotale());
        dettagliContoTextArea.setText(dettagli);
        dettagliContoTextArea.setForeground(textColor);

        // Imposta il totale
        totaleLabel.setText(String.format("TOTALE: € %.2f", ordine.getCostoTotale()));

        // Imposta il numero di coperti
        coppertiField.setText(String.valueOf(ordine.getNumPersone()));

        // Abilita i pulsanti
        stampaContoButton.setEnabled(true);
        pagaButton.setEnabled(true);
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
        // Estraiamo l'ID del tavolo dal formato "ID - Tavolo X (Y posti) - STATO"
        String[] parts = selectedItem.split(" - ");
        int idTavolo = Integer.parseInt(parts[0]);

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
                    "💰 Pagamento registrato con successo!\n🪑 Il tavolo è stato liberato.",
                    "Operazione completata", JOptionPane.INFORMATION_MESSAGE);

            // Aggiorno la lista dei tavoli
            caricaTavoli();

            // Pulisco i campi
            pulisciCampi();

            // Disabilito i pulsanti
            stampaContoButton.setEnabled(false);
            pagaButton.setEnabled(false);
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
        // Estraiamo l'ID del tavolo dal formato "ID - Tavolo X (Y posti) - STATO"
        String[] parts = selectedItem.split(" - ");
        int idTavolo = Integer.parseInt(parts[0]);

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
                    "🖨️ Conto stampato con successo!",
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
        dettagliContoTextArea.setForeground(textColor);
        totaleLabel.setText("TOTALE: € 0,00");
        coppertiField.setText("");
        coppertiField.setEditable(true);
    }

    // Metodo per caricare icone SVG (copiato dalle altre forme)
    private ImageIcon loadSVGIcon(String filename, int width, int height) {
        return loadSVGIcon(filename, width, height, Color.WHITE);
    }

    // Metodo sovraccaricato per specificare il colore dell'icona
    private ImageIcon loadSVGIcon(String filename, int width, int height, Color color) {
        try {
            // Percorsi possibili per le icone SVG (nell'ordine di priorità)
            String[] possiblePaths = {
                    "bin/resources/icons/" + filename, // Nel container/dopo compilazione
                    "resources/icons/" + filename, // Percorso relativo nel container
                    "GestioneRistorante/bin/resources/icons/" + filename, // Dalla root progetto
                    "GestioneRistorante/src/resources/icons/" + filename, // Sorgente originale
                    "src/resources/icons/" + filename // Durante sviluppo
            };

            java.io.File svgFile = null;
            String usedPath = null;

            for (String path : possiblePaths) {
                java.io.File testFile = new java.io.File(path);
                if (testFile.exists()) {
                    svgFile = testFile;
                    usedPath = path;
                    System.out.println("Icona SVG trovata: " + usedPath);
                    break;
                }
            }

            // Se non trovato con percorsi diretti, prova con il class loader
            if (svgFile == null || !svgFile.exists()) {
                try {
                    java.net.URL resourceUrl = getClass().getClassLoader().getResource("icons/" + filename);
                    if (resourceUrl == null) {
                        resourceUrl = getClass().getClassLoader().getResource("resources/icons/" + filename);
                    }
                    if (resourceUrl != null) {
                        svgFile = new java.io.File(resourceUrl.toURI());
                        System.out.println("Icona SVG trovata via class loader: " + resourceUrl);
                    }
                } catch (Exception e) {
                    // Ignora e usa fallback
                }
            }

            if (svgFile == null || !svgFile.exists()) {
                System.out.println("File SVG non trovato in nessuno dei percorsi: " + filename);
                return createFallbackIcon(filename, width, height, color);
            }

            SVGUniverse svgUniverse = new SVGUniverse();
            java.net.URI svgUri = svgFile.toURI();
            SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(svgUri.toURL()));

            if (diagram == null) {
                System.out.println("Impossibile caricare il diagramma SVG: " + filename);
                return createFallbackIcon(filename, width, height, color);
            }

            // Imposta dimensioni
            diagram.setIgnoringClipHeuristic(true);

            // Renderizza SVG come BufferedImage con sfondo trasparente
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Pulisci lo sfondo
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, width, height);
            g2.setComposite(AlphaComposite.SrcOver);

            // Imposta il colore dell'SVG
            g2.setColor(color);

            // Scala e centra l'SVG
            java.awt.geom.Rectangle2D bounds = diagram.getViewRect();
            double scaleX = (double) width / bounds.getWidth();
            double scaleY = (double) height / bounds.getHeight();
            double scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int) (bounds.getWidth() * scale);
            int scaledHeight = (int) (bounds.getHeight() * scale);
            int x = (width - scaledWidth) / 2;
            int y = (height - scaledHeight) / 2;

            g2.translate(x, y);
            g2.scale(scale, scale);

            diagram.render(g2);
            g2.dispose();

            System.out.println("Icona SVG caricata con successo: " + filename);
            return new ImageIcon(image);

        } catch (Exception e) {
            System.out.println("Errore nel caricamento SVG " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return createFallbackIcon(filename, width, height, color);
        }
    }

    // Metodo per creare icone di fallback
    private ImageIcon createFallbackIcon(String filename, int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Imposta colore e font
        g2.setColor(color);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, Math.min(width, height) - 4));

        String icon = getUnicodeIcon(filename);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(icon);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();

        g2.drawString(icon, x, y);
        g2.dispose();

        return new ImageIcon(image);
    }

    // Metodo per ottenere icone Unicode come fallback
    private String getUnicodeIcon(String filename) {
        if (filename.contains("payment")) {
            return "💳";
        } else if (filename.contains("table")) {
            return "🪑";
        } else if (filename.contains("receipt") || filename.contains("bill")) {
            return "🧾";
        } else if (filename.contains("print")) {
            return "🖨️";
        } else if (filename.contains("check")) {
            return "✅";
        } else if (filename.contains("back")) {
            return "←";
        } else {
            return "📄";
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Migliora l'aspetto dei componenti Swing
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
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
