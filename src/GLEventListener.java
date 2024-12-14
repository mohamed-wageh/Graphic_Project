import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GLEventListener extends Listener {
    private GLU glu = new GLU();
    private Connect4 connect4;

    public GLEventListener() {
        connect4 = new Connect4();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(0.1f, 0.1f, 0.3f, 1f); // Set background color to a dark blue
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        connect4.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL gl = glAutoDrawable.getGL();
        if (height <= 0) height = 1;
        float aspect = (float) width / height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0, width, height, 0.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean modeChanged, boolean deviceChanged) {
        // This method is deprecated and not used in modern JOGL applications.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed events if needed
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle key pressed events if needed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handle key released events if needed
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Handle mouse clicked events if needed
    }

    @Override
    public void mousePressed(MouseEvent e) {
        connect4.drop();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Handle mouse released events if needed
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Handle mouse entered events if needed
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Handle mouse exited events if needed
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Handle mouse dragged events if needed
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        connect4.hover(e.getX());
    }
}