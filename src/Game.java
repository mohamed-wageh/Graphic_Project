import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
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

        Animator animator = new FPSAnimator(glCanvas, 60);
        animator.start();

        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocusInWindow();
    }
}