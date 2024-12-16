import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Instructions {
    private final Connect4 connect4Game;
    private Game game;
    private JFrame instructionsFrame;

    public Instructions(Connect4 connect4Game, Game game) {
        this.game = game;
        this.connect4Game = connect4Game;
        createInstructionsFrame();
    }

    private void createInstructionsFrame() {
        instructionsFrame = new JFrame("Game Instructions");
        instructionsFrame.setSize(800, 800);
        instructionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        instructionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Define font styles
        Font regularFont = new Font("Tahoma", Font.PLAIN, 16);
        Font boldFont = new Font("Arial", Font.BOLD, 18);

        // Instructions content panel
        JPanel instructionsContentPanel = new JPanel();
        instructionsContentPanel.setLayout(new BoxLayout(instructionsContentPanel, BoxLayout.Y_AXIS));
        instructionsContentPanel.setOpaque(false); // Transparent background

        addStyledInstruction(instructionsContentPanel, "• Welcome to Connect 4!", boldFont, Color.WHITE, true);
        addStyledInstruction(instructionsContentPanel, "   - Each player takes turns dropping colorful discs into one of 7 columns.", regularFont, Color.WHITE, false);
        addStyledInstruction(instructionsContentPanel, "   - The discs stack up at the lowest available slot in that column.", regularFont, Color.WHITE, false);
        addStyledInstruction(instructionsContentPanel, "   - Use the mouse to select a column to drop a disc", regularFont, Color.WHITE, false);
        addStyledInstruction(instructionsContentPanel, "   - Press ESC to pause the game.", regularFont, Color.WHITE, false);

        addStyledInstruction(instructionsContentPanel, "   - Your mission: connect FOUR discs in a row horizontally, vertically, or diagonally to win!", regularFont, Color.WHITE, false);
        addStyledInstruction(instructionsContentPanel, "   - Hurry! You have only 30 seconds per turn to make your move.", boldFont, Color.WHITE, true);
        addStyledInstruction(instructionsContentPanel, "• Modes:", boldFont, Color.WHITE, true);
        addStyledInstruction(instructionsContentPanel, "   - Player vs Player: Play one round, Best of 3, or Best of 5!", regularFont, Color.WHITE, false);
        addStyledInstruction(instructionsContentPanel, "   - Player vs AI: Challenge the computer on Easy, Medium, or Hard.", regularFont, Color.WHITE, false);
        addStyledInstruction(instructionsContentPanel, "- Get creative! Winning requires careful planning and a touch of trickery.", regularFont, Color.WHITE, false);

        // Add the instructions panel to a scrollable view
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsContentPanel);
        instructionsScrollPane.setBorder(null);
        instructionsScrollPane.setOpaque(false);
        instructionsScrollPane.getViewport().setOpaque(false);

        instructionsPanel.add(instructionsScrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(255, 69, 0));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsFrame.dispose();
                new MainMenu(connect4Game, game);
            }
        });
        // Bottom panel for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        // Combine panels into the main content
        instructionsPanel.add(buttonPanel, BorderLayout.SOUTH);
        instructionsFrame.add(instructionsPanel);

        // Display the frame
        instructionsFrame.setVisible(true);
    }

    // Method to add styled instructions to the panel
    public static void addStyledInstruction(JPanel panel, String instructionText, Font font, Color color, boolean isBold) {
        JLabel label = new JLabel(instructionText);

        // Apply bolding to text if required
        if (isBold) {
            label.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
        } else {
            label.setFont(font);
        }
        label.setForeground(color);

        // Align left and add padding
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label);
    }

    // Method to style buttons uniformly
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Padding for rounded corners look
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
    }
}
