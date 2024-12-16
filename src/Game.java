import com.sun.opengl.util.FPSAnimator;
import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JFrame {
    private static Connect4 connect4Game;
    private FPSAnimator animator;
    private boolean paused = false;
    private JDialog pauseMenu;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }

    public Game() {
        connect4Game = new Connect4();
        showMainMenu();
    }

    private void showMainMenu() {
        if (pauseMenu != null) {
            pauseMenu.dispose();
        }
        new MainMenu(connect4Game, this);
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
            pauseMenu = new JDialog(this, "Paused", true);
            pauseMenu.setSize(300, 250);
            pauseMenu.setLocationRelativeTo(this);

            // Resume Button
            JButton resumeButton = new JButton("Resume");
            resumeButton.addActionListener(e -> resumeGame());
            pauseMenu.add(resumeButton, BorderLayout.NORTH);

            // Main Menu Button
            JButton mainMenuButton = new JButton("Main Menu");
            mainMenuButton.addActionListener(e -> showMainMenu());
            pauseMenu.add(mainMenuButton, BorderLayout.CENTER);

            // Quit Game Button
            JButton quitButton = new JButton("Quit Game");
            quitButton.addActionListener(e -> quitGame());
            pauseMenu.add(quitButton, BorderLayout.SOUTH);
        }
        pauseMenu.setVisible(true);
    }

    private void quitGame() {
        System.exit(0); // Exit the application
    }
}
