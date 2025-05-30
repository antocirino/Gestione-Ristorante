package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import database.DatabaseTest;

public class FirstForm extends JFrame {
    private JPanel mainPanel;
    private JLabel statusLabel;

    public FirstForm() {
        setTitle("Sistema di Gestione Ristorante");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Pannello superiore con titolo
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("GESTIONE RISTORANTE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Pannello centrale
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton testDbButton = new JButton("Test Connessione Database");
        testDbButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean result = DatabaseTest.testConnection();
                if (result) {
                    statusLabel.setText("Connessione al database riuscita!");
                    statusLabel.setForeground(new Color(39, 174, 96));
                } else {
                    statusLabel.setText("ERRORE: Impossibile connettersi al database!");
                    statusLabel.setForeground(Color.RED);
                }
            }
        });

        // Pannello inferiore con status
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Pronto");
        statusPanel.add(statusLabel);

        // Aggiungo i pannelli al main panel
        centerPanel.add(new JLabel("Benvenuto nel sistema di gestione ristorante!"));
        centerPanel.add(testDbButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        try {
            // Imposta il look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FirstForm().setVisible(true);
            }
        });
    }
}
