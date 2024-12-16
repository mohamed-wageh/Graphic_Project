import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    private JFrame mainMenuFrame;
    private JPanel mainPanel;
    private Connect4 connect4Game;
    private Game game;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainMenu(Connect4 connect4Game, Game game) {
        this.connect4Game = connect4Game;
        this.game = game;
        createMenu();
    }



    private void createMenu() {
        mainMenuFrame = new JFrame("Connect4 - Main Menu");
        mainMenuFrame.setSize(800, 800);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Main menu panel
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 60);
                Color color2 = new Color(10, 20, 40);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to Connect4");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(250, 10, 40, 10));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        // Player vs Player Button
        JButton oneVsOneButton = new JButton("Player vs Player");
        styleButton(oneVsOneButton, new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(oneVsOneButton, gbc);

        oneVsOneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Game = new Connect4();
                new OneVsOneOptions(connect4Game, game);
                mainMenuFrame.dispose();
            }
        });

        // Player vs AI Button
        JButton oneVsComputerButton = new JButton("Player vs AI");
        styleButton(oneVsComputerButton, new Color(220, 20, 60));
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(oneVsComputerButton, gbc);

        oneVsComputerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Game = new Connect4();
                new DifficultySelection(connect4Game, game);
                mainMenuFrame.dispose();
            }
        });

        // Instructions Button
        // Instructions Button
        JButton instructionsButton = new JButton("Instructions");
        styleButton(instructionsButton, new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(instructionsButton, gbc);

        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Instructions(connect4Game, game); // Pass connect4Game and game to Instructions
                mainMenuFrame.dispose();
            }
        });


        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add panels to CardLayout
        cardPanel.add(mainPanel, "MainMenu");
        mainMenuFrame.add(cardPanel);
        mainMenuFrame.setVisible(true);
    }



    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }
}
