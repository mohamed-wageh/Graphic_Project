import com.sun.opengl.util.FPSAnimator;
import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame {
    private static Connect4 connect4Game;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }

    public Game() {
        GLCanvas glCanvas = new GLCanvas();
        GLEventListener listener = new GLEventListener();
        glCanvas.addGLEventListener(listener);
        glCanvas.addMouseListener(listener);
        glCanvas.addMouseMotionListener(listener);

        getContentPane().add(glCanvas, BorderLayout.CENTER);

        FPSAnimator animator = new FPSAnimator(glCanvas, 60);
        animator.start();

        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocusInWindow();

        JPanel buttonPanel = new JPanel();
        JButton playerVsPlayerButton = new JButton("1v1");
        JButton playerVsComputerButton = new JButton("1vComputer");

        playerVsPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Game.setMode(Connect4.Mode.PLAYER_VS_PLAYER);
                connect4Game.resetGame();
                glCanvas.repaint();
            }
        });

        playerVsComputerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect4Game.setMode(Connect4.Mode.PLAYER_VS_COMPUTER);
                connect4Game.resetGame();
                glCanvas.repaint();
            }
        });

        buttonPanel.add(playerVsPlayerButton);
        buttonPanel.add(playerVsComputerButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        connect4Game = new Connect4(glCanvas);
    }
}
