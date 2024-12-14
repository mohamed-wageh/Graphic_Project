import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuScreen extends JFrame implements GLEventListener, MouseListener {
    private GLCanvas canvas;
    private int selectedButton = -1;
    private static final String[] BUTTONS = {"Play 1v1", "Play vs Computer", "Rules", "Quit"};
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 10;
    private static final int BUTTON_Y_START = 200;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuScreen().createMenuScreen());
    }

    public void createMenuScreen() {
        setTitle("Connect 4 Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        canvas.addMouseListener(this);

        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < BUTTONS.length; i++) {
            int yPos = BUTTON_Y_START + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            drawButton(gl, 200, yPos, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTONS[i], i == selectedButton);
        }
    }

    private void drawButton(GL gl, int x, int y, int width, int height, String text, boolean isSelected) {
        if (isSelected) {
            gl.glColor3f(0.8f, 0.8f, 0.8f);
        } else {
            gl.glColor3f(0.5f, 0.5f, 0.5f);
        }

        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + width, y);
        gl.glVertex2f(x + width, y + height);
        gl.glVertex2f(x, y + height);
        gl.glEnd();

        drawText(gl, x + width / 2, y + height / 2, text);
    }

    private void drawText(GL gl, int x, int y, String text) {
        gl.glColor3f(0f, 0f, 0f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, width, height, 0, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        for (int i = 0; i < BUTTONS.length; i++) {
            int yPos = BUTTON_Y_START + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            if (mouseX >= 200 && mouseX <= 200 + BUTTON_WIDTH && mouseY >= yPos && mouseY <= yPos + BUTTON_HEIGHT) {
                selectedButton = i;
                handleButtonClick(i);
                break;
            }
        }
    }

    private void handleButtonClick(int buttonIndex) {
        switch (buttonIndex) {
            case 0:
                new Connect4().startGame(); // Navigate to Connect4 screen
                this.dispose();  // Close menu screen
                break;
            case 1:
                System.out.println("Starting game vs Computer...");
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Connect 4 Rules:\n\n1. Players take turns to drop pieces.\n2. The goal is to connect four in a row.\n3. First player to connect four wins!");
                break;
            case 3:
                System.out.println("Exiting game...");
                System.exit(0);
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
