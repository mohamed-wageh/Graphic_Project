import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JFrame {
    private static Connect4 connect4Game;
    private FPSAnimator animator;
    private boolean paused = false; // Tracks whether the game is paused
    private JDialog pauseMenu; // The pause menu dialog

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }

    public Game() {
        connect4Game = new Connect4();
        showMainMenu();
    }

    private void showMainMenu() {
        JFrame mainMenuFrame = new JFrame("Connect4 - Main Menu");
        mainMenuFrame.setSize(800, 800);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 60); // dark blue
                Color color2 = new Color(10, 20, 40);//light blue
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

        // buttonPanel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        // oneVsOneButton
        JButton oneVsOneButton = new JButton(" Player vs Player");
        styleButton(oneVsOneButton, new Color(70, 130, 180)); //blue
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(oneVsOneButton, gbc);

        oneVsOneButton.addActionListener(e -> {
            connect4Game = new Connect4(); // Reset the game state
            showOneVsOneOptions();
            mainMenuFrame.dispose();
        });

        // oneVsComputerButton
        JButton oneVsComputerButton = new JButton(" Player vs AI");
        styleButton(oneVsComputerButton, new Color(220, 20, 60)) ;//red
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(oneVsComputerButton, gbc);

        oneVsComputerButton.addActionListener(e -> {
            connect4Game = new Connect4(); // Reset the game state
            showDifficultySelectionWindow();
            mainMenuFrame.dispose();
        });

        // instructionsButton
        JButton instructionsButton = new JButton("Instructions");
        styleButton(instructionsButton, new Color(34, 139, 34));
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(instructionsButton, gbc);

        instructionsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainMenuFrame,
                    "Instructions:\n1. Players take turns dropping discs.\n2. The goal is to connect 4 discs in a row.\n3. You can connect horizontally, vertically, or diagonally.\n4. First player to connect 4 wins!",
                    "Game Instructions",
                    JOptionPane.INFORMATION_MESSAGE);
        });


        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);


        mainMenuFrame.add(mainPanel);
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


    private void showOneVsOneOptions() {
        JFrame oneVsOneFrame = new JFrame("Select 1v1 Mode");
        oneVsOneFrame.setSize(800, 800); // Increase frame size
        oneVsOneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oneVsOneFrame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 60); // Dark blue color
                Color color2 = new Color(10, 20, 40); //light blue color
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Select 1 to 1 Mode");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(250, 10, 40, 10));

        // Button panel with GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); // Padding between buttons

        // One Round button
        JButton oneRoundButton = new JButton("One Round");
        styleButton(oneRoundButton, new Color(70, 130, 180)); // blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(oneRoundButton, gbc);

        oneRoundButton.addActionListener(e -> {
            connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
            connect4Game.setRoundsToWin(1);
            startGame();
            oneVsOneFrame.dispose();
        });

        // Best of 3 button
        JButton bestOf3Button = new JButton("Best of 3");
        styleButton(bestOf3Button, new Color(220, 20, 60)); // red color
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(bestOf3Button, gbc);

        bestOf3Button.addActionListener(e -> {
            connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
            connect4Game.setRoundsToWin(2); // Best of 3 requires 2 wins
            startGame();
            oneVsOneFrame.dispose();
        });

        // Best of 5 button
        JButton bestOf5Button = new JButton("Best of 5");
        styleButton(bestOf5Button, new Color(34, 139, 34)); //  green color
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(bestOf5Button, gbc);

        bestOf5Button.addActionListener(e -> {
            connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
            connect4Game.setRoundsToWin(3); // Best of 5 requires 3 wins
            startGame();
            oneVsOneFrame.dispose();
        });

        // Back button
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(255, 69, 0)); // orange color
        backButton.addActionListener(e -> {
            oneVsOneFrame.dispose();
            showMainMenu();
        });


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 20, 10, 20);
        buttonPanel.add(backButton, gbc);


        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Set up the frame
        oneVsOneFrame.add(mainPanel);
        oneVsOneFrame.setVisible(true);
    }





    private void showDifficultySelectionWindow() {
        JFrame difficultyFrame = new JFrame("Select Difficulty");
        difficultyFrame.setSize(800, 800);
        difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        difficultyFrame.setLocationRelativeTo(null);

        // Set up main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(20, 30, 60); // Dark blue color
                Color color2 = new Color(10, 20, 40); // Darker blue color
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Select Difficulty");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(250, 10, 40, 10)); // Padding around text

        // Button panel with GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make background transparent to match the gradient
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); // Padding between buttons

        // Easy button
        JButton easyButton = new JButton("Easy");
        styleButton(easyButton, new Color(70, 130, 180)); // Stylish blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(easyButton, gbc);
        easyButton.addActionListener(e -> setGameModeAndStart(Connect4.Difficulty.EASY, difficultyFrame));

        // Medium button
        JButton mediumButton = new JButton("Medium");
        styleButton(mediumButton, new Color(220, 20, 60)); // Stylish red color
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(mediumButton, gbc);
        mediumButton.addActionListener(e -> setGameModeAndStart(Connect4.Difficulty.MEDIUM, difficultyFrame));

        // Hard button
        JButton hardButton = new JButton("Hard");
        styleButton(hardButton, new Color(34, 139, 34)); // Stylish green color
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(hardButton, gbc);
        hardButton.addActionListener(e -> setGameModeAndStart(Connect4.Difficulty.HARD, difficultyFrame));

        // Back button
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(255, 69, 0)); // Stylish orange color
        backButton.addActionListener(e -> {
            difficultyFrame.dispose(); // Close the current frame
            showMainMenu(); // Go back to the main menu (or previous page)
        });

        // Place Back button at the end (bottom) of the panel
        gbc.gridx = 0;
        gbc.gridy = 3; // Place it under the other buttons
        gbc.insets = new Insets(30, 20, 10, 20); // Extra space for bottom margin
        buttonPanel.add(backButton, gbc);

        // Add components to the main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Set up the frame
        difficultyFrame.add(mainPanel);
        difficultyFrame.setVisible(true);
    }












    private void setGameModeAndStart(Connect4.Difficulty difficulty, JFrame difficultyFrame) {
        connect4Game.setDifficulty(difficulty);
        connect4Game.setMode(Connect4.Mode.PLAYER_VS_COMPUTER);
        startGame();
        difficultyFrame.dispose();
    }

    private void startGame() {
        GLCanvas glCanvas = new GLCanvas();
        GLEventListener listener = new GLEventListener(connect4Game);
        glCanvas.addGLEventListener(listener);
        glCanvas.addMouseListener(listener);
        glCanvas.addMouseMotionListener(listener);

        getContentPane().add(glCanvas, BorderLayout.CENTER);

        animator = new FPSAnimator(glCanvas, 60);
        animator.start();

        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });

        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocusInWindow();
    }

    private void togglePause() {
        if (paused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        paused = true;
        animator.stop(); // Pause the game animation
        showPauseMenu();
    }

    private void resumeGame() {
        paused = false;
        animator.start(); // Resume the game animation
        if (pauseMenu != null) {
            pauseMenu.dispose(); // Close the pause menu
        }
    }

    private void showPauseMenu() {
        pauseMenu = new JDialog(this, "Game Paused", true);
        pauseMenu.setSize(300, 200);
        pauseMenu.setLayout(new GridLayout(3, 1));
        pauseMenu.setLocationRelativeTo(this);
        pauseMenu.setUndecorated(true); // Remove the title bar and close button

        JButton resumeButton = new JButton("Resume Game");
        JButton restartButton = new JButton("Restart Game");
        JButton mainMenuButton = new JButton("Back to Main Menu");

        resumeButton.addActionListener(e -> resumeGame());
        restartButton.addActionListener(e -> {
            pauseMenu.dispose();
            restartGame();
        });
        mainMenuButton.addActionListener(e -> {
            pauseMenu.dispose();
            backToMainMenu();
        });

        pauseMenu.add(resumeButton);
        pauseMenu.add(restartButton);
        pauseMenu.add(mainMenuButton);
        pauseMenu.setVisible(true);
    }

    private void restartGame() {
        dispose();
        connect4Game = new Connect4(); // Reset the Connect4 game object
        showMainMenu(); // Go back to the main menu
    }

    private void backToMainMenu() {
        dispose();
        connect4Game = new Connect4(); // Reset the Connect4 game object
        showMainMenu(); // Go back to the main menu
    }
}