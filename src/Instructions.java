import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Instructions {
    private JFrame instructionsFrame;
    private Game game;

    public Instructions(Game game) {
        this.game = game;
        createInstructionsFrame();
    }

    private void createInstructionsFrame() {
        instructionsFrame = new JFrame("Game Instructions");
        instructionsFrame.setSize(800, 800);
        instructionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructionsFrame.setLocationRelativeTo(null);

        // Main Instructions panel with Gradient Background
        JPanel instructionsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 60); // Gradient start color
                Color color2 = new Color(10, 20, 40); // Gradient end color
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        instructionsPanel.setLayout(new BorderLayout());

        // Instructions Text Area
        JTextArea instructionsText = new JTextArea("Instructions for Connect4 game...\n" +
                "\n" +
                "1. Each player takes turns to drop a piece into one of the columns.\n" +
                "2. The goal is to align 4 pieces horizontally, vertically, or diagonally.\n" +
                "3. Player 1 is Red, Player 2 is Yellow.\n" +
                "4. Use the mouse to select a column to drop a piece.\n" +
                "5. Press ESC to pause the game.");
        instructionsText.setEditable(false);
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsText.setBackground(new Color(245, 245, 245));
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setCaretPosition(0); // Ensure that the text starts from the top

        // Center the instructions text using JScrollPane and BorderLayout
        JPanel instructionsTextPanel = new JPanel(new BorderLayout());
        instructionsTextPanel.add(new JScrollPane(instructionsText), BorderLayout.CENTER);

        // Adding content panel to the main panel
        instructionsPanel.add(instructionsTextPanel, BorderLayout.CENTER);

        // Back Button styled similarly to the main menu buttons
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(255, 69, 0)); // Using the same red color as "Back" buttons in previous classes
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsFrame.dispose();
                // Optionally, you can call a method to return to the MainMenu or any other window
                // new MainMenu(new Connect4(), game); // Uncomment if necessary
            }
        });

        // Content panel for instructions
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(instructionsPanel, BorderLayout.CENTER);

        // Adding the back button in the center at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Centering the button
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        instructionsFrame.add(contentPanel);
        instructionsFrame.setVisible(true);
    }

    // This method styles buttons similarly to the ones in the other screens (MainMenu, DifficultySelection)
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Set rounded corners using EmptyBorder
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20))); // Add padding for rounded look
    }
}
