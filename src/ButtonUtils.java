import javax.swing.*;
import java.awt.*;

public class ButtonUtils {
    public static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
    }
}
