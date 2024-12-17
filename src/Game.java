import com.sun.opengl.util.FPSAnimator;
import javax.media.opengl.GLCanvas;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
public class Game extends JFrame {
    private static Connect4 connect4Game;
    private FPSAnimator animator;
    private boolean paused = false;
    private JDialog pauseMenu;
    private MainMenu mainMenu; // Add a reference to the main menu

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }


    private void playSound(String soundFile) {
        try {
            // Open the sound file as a Clip
            File soundPath = new File(soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Start the clip and loop it indefinitely
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public Game() {
        showMainMenu(); // Show the main menu when starting
        playSound("C:/Users/ahmed/Downloads/a8.wav");
        connect4Game = new Connect4();
    }

    private void showMainMenu() {
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }

        // Hide or dispose of the main menu when the Connect4 game starts
        if (mainMenu != null) {
            mainMenu.dispose(); // Close the main menu if it's open
        }

        mainMenu = new MainMenu(connect4Game, this); // Create a new main menu
    }

    public void startGame() {
        GLCanvas glCanvas = new GLCanvas();
        GLEventListener listener = new GLEventListener(connect4Game);
        glCanvas.addGLEventListener(listener);
        glCanvas.addMouseListener(listener);
        glCanvas.addMouseMotionListener(listener);

        getContentPane().add(glCanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(glCanvas, 60);
        animator.start();
        connect4Game.resetGame();

        connect4Game.setVisible(true); // Show Connect4 frame

        if (mainMenu != null) {
            mainMenu.dispose();
        }

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
        animator.stop();
        showPauseMenu();
    }

    private void resumeGame() {
        paused = false;
        animator.start();
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }
        this.setVisible(true);
    }

    private void showPauseMenu() {
        if (pauseMenu == null) {
            // Create the pause menu dialog
            pauseMenu = new JDialog(this, "Game Paused", true);
            pauseMenu.setSize(400, 300);
            pauseMenu.setLocationRelativeTo(this);
            pauseMenu.setLayout(new GridBagLayout());
            pauseMenu.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Set dialog background color
            pauseMenu.getContentPane().setBackground(new Color(30, 30, 30));

            // Create a label for the title
            JLabel titleLabel = new JLabel(" Game Paused");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);

            // Style helper for buttons
            JButton resumeButton = createStyledButton(" Resume", Color.GREEN);
            JButton mainMenuButton = createStyledButton(" Main Menu", Color.ORANGE);
            JButton quitButton = createStyledButton(" Quit Game", Color.RED);

            // Add button actions
            resumeButton.addActionListener(e -> resumeGame());
            mainMenuButton.addActionListener(e -> showMainMenu());
            quitButton.addActionListener(e -> quitGame());

            // Add components using GridBagLayout
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.CENTER;

            // Add title
            pauseMenu.add(titleLabel, gbc);

            // Add buttons
            gbc.gridy = 1;
            pauseMenu.add(resumeButton, gbc);

            gbc.gridy = 2;
            pauseMenu.add(mainMenuButton, gbc);

            gbc.gridy = 3;
            pauseMenu.add(quitButton, gbc);
        }

        pauseMenu.setVisible(true);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void quitGame() {
        System.exit(0); // Exit the application
    }
}
