import java.util.ArrayList;
import javax.swing.JFrame;

public class WindowManager {
    private static final ArrayList<JFrame> openWindows = new ArrayList<>();

    // Register a new window
    public static void registerWindow(JFrame window) {
        openWindows.add(window);
    }

    // Close all open windows
    public static void closeAllWindows() {
        for (JFrame window : openWindows) {
            if (window != null) {
                window.dispose(); // Close the window
            }
        }
        openWindows.clear(); // Clear the list
    }
}
