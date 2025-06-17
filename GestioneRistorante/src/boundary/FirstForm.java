package boundary;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import utility.SvgIconManager;

// Importa la libreria SVG Salamander

public class FirstForm extends JFrame {
    private JPanel mainPanel;
    private Color primaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(41, 128, 185);
    private Color textColor = new Color(44, 62, 80);
    private Color lightColor = new Color(236, 240, 241);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private Font regularFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 14);

    public FirstForm() {
        setTitle("Ristorante Manager");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Imposta il colore di sfondo principale
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(lightColor);

        // Pannello superiore con titolo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("GESTIONE RISTORANTE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Pannello centrale
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(lightColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Pannello di benvenuto in alto
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(lightColor);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JLabel welcomeLabel = new JLabel("Benvenuto nel sistema di gestione del ristorante");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        welcomeLabel.setForeground(textColor);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Pannello per i ruoli utente
        JPanel ruoliPanel = new JPanel();
        ruoliPanel.setLayout(new GridLayout(2, 2, 25, 25));
        ruoliPanel.setBackground(lightColor);
        ruoliPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        // Metodo per creare pulsanti moderni
        cameriereButton = createModernButton("Cameriere", "person");
        cuocoButton = createModernButton("Cuoco", "restaurant_menu");
        cassiereButton = createModernButton("Cassiere", "payment");
        direttoreButton = createModernButton("Direttore", "admin_panel_settings");

        ruoliPanel.add(cameriereButton);
        ruoliPanel.add(cuocoButton);
        ruoliPanel.add(cassiereButton);
        ruoliPanel.add(direttoreButton);

        // Pannello uscire dall'applicazione
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        testPanel.setBackground(lightColor);
        testPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 40));

        JButton exitButton = new JButton("Esci dall'applicazione");
        exitButton.setPreferredSize(new Dimension(250, 40));
        exitButton.setFont(smallFont);
        exitButton.setBackground(new Color(231, 76, 60)); // Colore rosso per indicare l'uscita
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(192, 57, 43), 1, true), // Bordo rosso più scuro
                new EmptyBorder(14, 15, 14, 15)));

        // Effetti hover personalizzati per il pulsante rosso
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(192, 57, 43)); // Rosso più scuro
                exitButton.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(169, 50, 38), 1, true),
                        new EmptyBorder(14, 15, 14, 15)));
                exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(231, 76, 60)); // Rosso originale
                exitButton.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(192, 57, 43), 1, true),
                        new EmptyBorder(14, 15, 14, 15)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                exitButton.setBackground(new Color(169, 50, 38)); // Rosso molto scuro
                exitButton.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(146, 43, 33), 2, true),
                        new EmptyBorder(15, 15, 13, 15)));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (exitButton.contains(e.getPoint())) {
                    exitButton.setBackground(new Color(192, 57, 43)); // Ritorna al rosso scuro
                    exitButton.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(169, 50, 38), 1, true),
                            new EmptyBorder(14, 15, 14, 15)));
                } else {
                    exitButton.setBackground(new Color(231, 76, 60)); // Ritorna al rosso originale
                    exitButton.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(192, 57, 43), 1, true),
                            new EmptyBorder(14, 15, 14, 15)));
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Crea una finestra di dialogo personalizzata
                JDialog confirmDialog = new JDialog(FirstForm.this, "Conferma uscita", true);
                confirmDialog.setSize(400, 150);
                confirmDialog.setLocationRelativeTo(FirstForm.this);
                confirmDialog.setResizable(false);

                JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
                dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                dialogPanel.setBackground(Color.WHITE);

                // Icona e messaggio
                JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                messagePanel.setBackground(Color.WHITE);

                JLabel iconLabel = new JLabel("⚠️");
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

                JLabel messageLabel = new JLabel("Sei sicuro di voler uscire dall'applicazione?");
                messageLabel.setFont(regularFont);
                messageLabel.setForeground(textColor);

                messagePanel.add(iconLabel);
                messagePanel.add(messageLabel);

                // Pulsanti
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                buttonPanel.setBackground(Color.WHITE);

                JButton noButton = new JButton("No");
                noButton.setPreferredSize(new Dimension(80, 35));
                noButton.setFont(smallFont);
                styleButton(noButton);
                noButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        confirmDialog.dispose();
                    }
                });

                JButton yesButton = new JButton("Sì");
                yesButton.setPreferredSize(new Dimension(80, 35));
                yesButton.setFont(smallFont);
                yesButton.setBackground(new Color(231, 76, 60));
                yesButton.setForeground(Color.WHITE);
                yesButton.setFocusPainted(false);
                yesButton.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(192, 57, 43), 1, true),
                        new EmptyBorder(8, 12, 8, 12)));
                yesButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        confirmDialog.dispose();
                        System.exit(0);
                    }
                });

                buttonPanel.add(noButton);
                buttonPanel.add(yesButton);

                dialogPanel.add(messagePanel, BorderLayout.CENTER);
                dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

                confirmDialog.add(dialogPanel);
                confirmDialog.setVisible(true);
            }
        });

        testPanel.add(exitButton);

        // Pannello inferiore con status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(245, 247, 250));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Pannello sinistro per nomi creatori
        JPanel leftStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftStatusPanel.setBackground(new Color(245, 247, 250));
        JLabel namesLabel = new JLabel("Matteo Adaggio, Alessandro Cioffi, Luigi Cirillo, Antonio Cirino");
        namesLabel.setFont(smallFont);
        namesLabel.setForeground(new Color(149, 165, 166));
        leftStatusPanel.add(namesLabel);

        // Pannello destro per versione e copyright
        JPanel rightStatusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightStatusPanel.setBackground(new Color(245, 247, 250));

        JLabel copyrightLabel = new JLabel("© " + Calendar.getInstance().get(Calendar.YEAR));
        copyrightLabel.setFont(smallFont);
        copyrightLabel.setForeground(new Color(149, 165, 166));

        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(smallFont);
        versionLabel.setForeground(new Color(149, 165, 166));

        rightStatusPanel.add(copyrightLabel);
        rightStatusPanel.add(versionLabel);

        // Assemblaggio pannello di stato
        statusPanel.add(leftStatusPanel, BorderLayout.WEST);
        statusPanel.add(rightStatusPanel, BorderLayout.EAST);

        // Assemblaggio pannelli
        centerPanel.add(welcomePanel, BorderLayout.NORTH);
        centerPanel.add(ruoliPanel, BorderLayout.CENTER);
        centerPanel.add(testPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    /**
     * Crea un pulsante moderno con icona e testo
     * Utilizza un layout per posizionare l'icona sopra il testo
     * 
     * @param text
     * @param iconName
     * @return
     */
    private JButton createModernButton(String text, String iconName) {
        JButton button = new JButton();

        // Layout per posizionare icona e testo verticalmente
        button.setLayout(new BorderLayout(0, 10));

        // Carica icona SVG
        ImageIcon svgIcon = SvgIconManager.loadSVGIcon(iconName + ".svg", 40, 40);
        JLabel iconLabel = new JLabel(svgIcon, SwingConstants.CENTER);

        // Etichetta per il testo
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(regularFont);
        textLabel.setForeground(textColor);

        // Aggiungi componenti al pulsante
        button.add(iconLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);

        button.setPreferredSize(new Dimension(180, 120));
        styleButton(button);

        // Aggiungi azione appropriata
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String role = text;
                openRoleWindow(role);
            }
        });

        return button;
    }

    /**
     * Applica lo stile moderno al pulsante
     * Imposta font, colori, bordi e aggiunge effetti hover e animazioni
     * 
     * @param button Il pulsante da stilizzare
     */
    private void styleButton(JButton button) {
        button.setFont(regularFont);
        button.setForeground(textColor);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true), // Bordi arrotondati
                new EmptyBorder(14, 15, 14, 15)));

        // Aggiunta effetto hover e animazioni
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 247, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(accentColor, 1, true),
                        new EmptyBorder(14, 15, 14, 15)));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(224, 224, 224), 1, true),
                        new EmptyBorder(14, 15, 14, 15)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(235, 237, 240));
                // Effetto di pressione: bordo più scuro e spostamento leggero
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(41, 128, 185), 2, true),
                        new EmptyBorder(15, 15, 13, 15) // Spostamento verso il basso
                ));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(new Color(245, 247, 250));
                    button.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(accentColor, 1, true),
                            new EmptyBorder(14, 15, 14, 15)));
                } else {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(224, 224, 224), 1, true),
                            new EmptyBorder(14, 15, 14, 15)));
                }
            }
        });

        // Aggiungere effetto elevato con ombreggiatura
        button.setBorder(new CompoundBorder(
                new SoftBevelBorder(SoftBevelBorder.RAISED,
                        new Color(250, 250, 250),
                        new Color(230, 230, 230),
                        new Color(210, 210, 210),
                        new Color(230, 230, 230)),
                new EmptyBorder(12, 14, 12, 14)));
    }

    public static void main(String[] args) {
        try {
            // Imposta il look and feel
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
                new FirstForm().setVisible(true);
            }
        });
    }

    // Dichiarazione dei pulsanti
    private JButton cameriereButton;
    private JButton cuocoButton;
    private JButton cassiereButton;
    private JButton direttoreButton;

    /**
     * Apre la finestra del ruolo selezionato
     * Nasconde la finestra principale e mostra quella del ruolo
     * Gestisce la chiusura della finestra del ruolo per tornare alla finestra
     * principale
     * 
     * @param role Il ruolo selezionato (Cameriere, Cuoco, Cassiere, Direttore)
     */
    private void openRoleWindow(String role) {
        JFrame roleWindow = null;

        // Crea la finestra appropriata
        if (role.equals("Cameriere")) {
            roleWindow = new CameriereForm();
        } else if (role.equals("Cuoco")) {
            roleWindow = new CuocoForm();
        } else if (role.equals("Cassiere")) {
            roleWindow = new CassiereForm();
        } else if (role.equals("Direttore")) {
            roleWindow = new DirettoreForm();
        }

        if (roleWindow != null) {
            final JFrame finalRoleWindow = roleWindow;

            // Nascondi FirstForm
            this.setVisible(false);

            // Configura la finestra del ruolo
            finalRoleWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            // Aggiungi listener per gestire la chiusura
            finalRoleWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    // Chiudi la finestra del ruolo
                    finalRoleWindow.dispose();
                    // Rimostra FirstForm
                    FirstForm.this.setVisible(true);
                    FirstForm.this.toFront();
                    FirstForm.this.requestFocus();
                }
            });

            // Mostra la finestra del ruolo
            finalRoleWindow.setVisible(true);
            finalRoleWindow.toFront();
            finalRoleWindow.requestFocus();
        }
    }
}
