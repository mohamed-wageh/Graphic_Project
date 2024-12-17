import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OneVsOneOptions {
    private Connect4 connect4Game;
    private Game game;

    public OneVsOneOptions(Connect4 connect4Game, Game game) {
        this.connect4Game = connect4Game;
        this.game = game;
        createOptionsMenu();
    }

    private void createOptionsMenu() {
        JFrame optionsFrame = new JFrame("Select Game Mode");
        optionsFrame.setSize(800, 800);
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsFrame.setLocationRelativeTo(null);

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

        JLabel titleLabel = new JLabel("Select Player vs Player Mode");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(250, 10, 40, 10));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        JButton oneRoundButton = new JButton("One Round");
        styleButton(oneRoundButton, new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(oneRoundButton, gbc);

        oneRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameModeAndStart(Connect4.Mode.PLAYER_VS_PLAYER, 1, optionsFrame);
            }
        });

        JButton bestOf3Button = new JButton("Best of 3");
        styleButton(bestOf3Button, new Color(220, 20, 60));
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(bestOf3Button, gbc);

        bestOf3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameModeAndStart(Connect4.Mode.PLAYER_VS_PLAYER, 3, optionsFrame);
            }
        });

        JButton bestOf5Button = new JButton("Best of 5");
        styleButton(bestOf5Button, new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(bestOf5Button, gbc);

        bestOf5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameModeAndStart(Connect4.Mode.PLAYER_VS_PLAYER, 5, optionsFrame);
            }
        });

        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(255, 69, 0));
        gbc.gridx = 0;
        gbc.gridy = 3;
        buttonPanel.add(backButton, gbc);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionsFrame.dispose();
                new MainMenu(connect4Game, game);
            }
        });

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        optionsFrame.add(mainPanel);
        optionsFrame.setVisible(true);
    }

    // Modified to accept roundsToWin as a parameter
    private void setGameModeAndStart(Connect4.Mode mode, int roundsToWin, JFrame optionsFrame) {
        connect4Game.setMode(mode);
        connect4Game.setRoundsToWin(roundsToWin);

        connect4Game.resetGame();
        game = new Game();
        game.startGame();

        // Close the options frame and start the game.
        optionsFrame.dispose();
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
