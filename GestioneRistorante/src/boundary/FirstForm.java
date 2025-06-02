package boundary;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import control.Controller;

// Importa la libreria SVG Salamander
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

public class FirstForm extends JFrame {
    private JPanel mainPanel;
    private JLabel statusLabel;
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

        // Pannello per testare la connessione al database
        JPanel testPanel = new JPanel(new BorderLayout());
        testPanel.setBackground(lightColor);
        testPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton testDbButton = new JButton("Verifica connessione database");
        styleButton(testDbButton);
        testDbButton.setPreferredSize(new Dimension(250, 40));
        testDbButton.setFont(smallFont);
        testDbButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Controller controller = Controller.getInstance();
                boolean result = controller.testDatabaseConnection();
                if (result) {
                    statusLabel.setText("✓ Connessione al database riuscita");
                    statusLabel.setForeground(new Color(46, 204, 113));
                } else {
                    statusLabel.setText("✗ Impossibile connettersi al database");
                    statusLabel.setForeground(new Color(231, 76, 60));
                }
            }
        });
        testPanel.add(testDbButton, BorderLayout.CENTER);

        // Pannello inferiore con status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(245, 247, 250));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Pannello sinistro per status
        JPanel leftStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftStatusPanel.setBackground(new Color(245, 247, 250));
        statusLabel = new JLabel("Pronto");
        statusLabel.setFont(smallFont);
        statusLabel.setForeground(textColor);
        leftStatusPanel.add(statusLabel);

        // Pannello centrale per nomi creatori
        JPanel centerStatusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerStatusPanel.setBackground(new Color(245, 247, 250));
        JLabel namesLabel = new JLabel("Matteo Adaggio, Alessandro Cioffi, Luigi Cirillo, Antonio Cirino");
        namesLabel.setFont(smallFont);
        namesLabel.setForeground(new Color(149, 165, 166));
        centerStatusPanel.add(namesLabel);

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
        statusPanel.add(centerStatusPanel, BorderLayout.CENTER);
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
    
    // Metodo per creare button moderni con icone SVG
    private JButton createModernButton(String text, String iconName) {
        JButton button = new JButton();
        
        // Layout per posizionare icona e testo verticalmente
        button.setLayout(new BorderLayout(0, 10));
        
        // Carica icona SVG
        ImageIcon svgIcon = loadSVGIcon(iconName + ".svg", 40, 40);
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
                // Manteniamo le azioni originali
                String role = text;
                if (role.equals("Cameriere")) {
                    new CameriereForm().setVisible(true);
                } else if (role.equals("Cuoco")) {
                    new CuocoForm().setVisible(true);
                } else if (role.equals("Cassiere")) {
                    new CassiereForm().setVisible(true);
                } else if (role.equals("Direttore")) {
                    new DirettoreForm().setVisible(true);
                }
            }
        });
        
        return button;
    }

    // Metodo per caricare icone SVG
    private ImageIcon loadSVGIcon(String filename, int width, int height) {
        try {
            // Percorsi possibili per le icone SVG (nell'ordine di priorità)
            String[] possiblePaths = {
                "bin/resources/icons/" + filename,                    // Nel container/dopo compilazione
                "resources/icons/" + filename,                       // Percorso relativo nel container
                "GestioneRistorante/bin/resources/icons/" + filename, // Dalla root progetto
                "GestioneRistorante/src/resources/icons/" + filename, // Sorgente originale
                "src/resources/icons/" + filename                    // Durante sviluppo
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
                for (String path : possiblePaths) {
                    System.out.println("  Tentato: " + path + " -> " + new java.io.File(path).exists());
                }
                return createFallbackIcon(filename, width, height);
            }
            
            SVGUniverse svgUniverse = new SVGUniverse();
            java.net.URI svgUri = svgFile.toURI();
            SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(svgUri.toURL()));
            
            if (diagram == null) {
                System.out.println("Impossibile caricare il diagramma SVG: " + filename);
                return createFallbackIcon(filename, width, height);
            }
            
            // Imposta dimensioni
            diagram.setIgnoringClipHeuristic(true);
            
            // Renderizza SVG come BufferedImage
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Pulisci lo sfondo
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, width, height);
            g2.setComposite(AlphaComposite.SrcOver);
            
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
            return createFallbackIcon(filename, width, height);
        }
    }
    
    // Metodo per creare icone di fallback
    private ImageIcon createFallbackIcon(String filename, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Imposta colore e font
        g2.setColor(accentColor);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
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
        if (filename.contains("person")) {
            return "👤";
        } else if (filename.contains("restaurant_menu")) {
            return "🍽️";
        } else if (filename.contains("payment")) {
            return "💳";
        } else if (filename.contains("admin_panel_settings")) {
            return "⚙️";
        } else {
            return "📋";
        }
    }
    
    // Metodo per stilizzare i pulsanti
    private void styleButton(JButton button) {
        button.setFont(regularFont);
        button.setForeground(textColor);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(224, 224, 224), 1, true), // Bordi arrotondati
            new EmptyBorder(14, 15, 14, 15)
        ));
        
        // Aggiunta effetto hover e animazioni
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 247, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(accentColor, 1, true),
                    new EmptyBorder(14, 15, 14, 15)
                ));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(224, 224, 224), 1, true),
                    new EmptyBorder(14, 15, 14, 15)
                ));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(235, 237, 240));
                // Effetto di pressione: bordo più scuro e spostamento leggero
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(41, 128, 185), 2, true),
                    new EmptyBorder(15, 15, 13, 15)  // Spostamento verso il basso
                ));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(new Color(245, 247, 250));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(accentColor, 1, true),
                        new EmptyBorder(14, 15, 14, 15)
                    ));
                } else {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(224, 224, 224), 1, true),
                        new EmptyBorder(14, 15, 14, 15)
                    ));
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
            new EmptyBorder(12, 14, 12, 14)
        ));
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
}
