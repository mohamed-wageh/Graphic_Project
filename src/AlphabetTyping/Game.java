package AlphabetTyping;


import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    public static void main(String[] args) {
        new Game();
    }
    public Game() {
        GLCanvas glCanvas;
        Animator animator;

        Listener listener = new GLEventListener();
        glCanvas = new GLCanvas();
        glCanvas.addGLEventListener(listener);
        glCanvas.addKeyListener(listener);
        getContentPane().add(glCanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glCanvas);
        animator.start();

        setTitle("Alphabet Typing Challenge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocus();
    }
}
