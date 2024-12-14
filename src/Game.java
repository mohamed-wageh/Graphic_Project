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
        JFrame mainMenuFrame = new JFrame("Select Mode");
        mainMenuFrame.setSize(400, 200);
        mainMenuFrame.setLayout(new GridLayout(2, 1));
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setLocationRelativeTo(null);

        JButton oneVsOneButton = new JButton("1v1");
        JButton oneVsComputerButton = new JButton("1vComputer");

        oneVsOneButton.addActionListener(e -> {
            connect4Game = new Connect4(); // Reset the game state
            showOneVsOneOptions();
            mainMenuFrame.dispose();
        });

        oneVsComputerButton.addActionListener(e -> {
            connect4Game = new Connect4(); // Reset the game state
            showDifficultySelectionWindow();
            mainMenuFrame.dispose();
        });

        mainMenuFrame.add(oneVsOneButton);
        mainMenuFrame.add(oneVsComputerButton);
        mainMenuFrame.setVisible(true);
    }

    private void showOneVsOneOptions() {
        JFrame oneVsOneFrame = new JFrame("Select 1v1 Mode");
        oneVsOneFrame.setSize(400, 200);
        oneVsOneFrame.setLayout(new GridLayout(3, 1));
        oneVsOneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oneVsOneFrame.setLocationRelativeTo(null);

        JButton oneRoundButton = new JButton("One Round");
        JButton bestOf3Button = new JButton("Best of 3");
        JButton bestOf5Button = new JButton("Best of 5");

        oneRoundButton.addActionListener(e -> {
            connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
            connect4Game.setRoundsToWin(1);
            startGame();
            oneVsOneFrame.dispose();
        });

        bestOf3Button.addActionListener(e -> {
            connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
            connect4Game.setRoundsToWin(2); // Best of 3 requires 2 wins
            startGame();
            oneVsOneFrame.dispose();
        });

        bestOf5Button.addActionListener(e -> {
            connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
            connect4Game.setRoundsToWin(3); // Best of 5 requires 3 wins
            startGame();
            oneVsOneFrame.dispose();
        });

        oneVsOneFrame.add(oneRoundButton);
        oneVsOneFrame.add(bestOf3Button);
        oneVsOneFrame.add(bestOf5Button);
        oneVsOneFrame.setVisible(true);
    }

    private void showDifficultySelectionWindow() {
        JFrame difficultyFrame = new JFrame("Select Difficulty");
        difficultyFrame.setSize(400, 200);
        difficultyFrame.setLayout(new GridLayout(3, 1));
        difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        difficultyFrame.setLocationRelativeTo(null);

        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> setGameModeAndStart(Connect4.Difficulty.EASY, difficultyFrame));
        mediumButton.addActionListener(e -> setGameModeAndStart(Connect4.Difficulty.MEDIUM, difficultyFrame));
        hardButton.addActionListener(e -> setGameModeAndStart(Connect4.Difficulty.HARD, difficultyFrame));

        difficultyFrame.add(easyButton);
        difficultyFrame.add(mediumButton);
        difficultyFrame.add(hardButton);
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