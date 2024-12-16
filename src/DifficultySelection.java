import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultySelection {
    private Connect4 connect4Game;
    private Game game;

    public DifficultySelection(Connect4 connect4Game, Game game) {
        this.connect4Game = connect4Game;
        this.game = game;
        createDifficultySelection();
    }

    private void createDifficultySelection() {
        JFrame difficultyFrame = new JFrame("Select Difficulty");
        difficultyFrame.setSize(800, 800);
        difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        difficultyFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
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

        JLabel titleLabel = new JLabel("Select Difficulty");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(250, 10, 40, 10));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        JButton easyButton = new JButton("Easy");
        styleButton(easyButton, new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(easyButton, gbc);

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameModeAndStart(Connect4.Difficulty.EASY, difficultyFrame);
            }
        });

        JButton mediumButton = new JButton("Medium");
        styleButton(mediumButton, new Color(220, 20, 60));
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(mediumButton, gbc);

        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameModeAndStart(Connect4.Difficulty.MEDIUM, difficultyFrame);
            }
        });

        JButton hardButton = new JButton("Hard");
        styleButton(hardButton, new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(hardButton, gbc);

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameModeAndStart(Connect4.Difficulty.HARD, difficultyFrame);
            }
        });

        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(255, 69, 0));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficultyFrame.dispose();
                new MainMenu(connect4Game, game);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 20, 10, 20);
        buttonPanel.add(backButton, gbc);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        difficultyFrame.add(mainPanel);
        difficultyFrame.setVisible(true);
    }

    private void setGameModeAndStart(Connect4.Difficulty difficulty, JFrame difficultyFrame) {
        connect4Game.setDifficulty(difficulty);
        connect4Game.setMode(Connect4.Mode.PLAYER_VS_COMPUTER);
        game.startGame();
        difficultyFrame.dispose();
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
