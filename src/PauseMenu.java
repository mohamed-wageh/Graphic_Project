import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseMenu {
    private JDialog pauseMenu;
    private Game game;

    public PauseMenu(Game game) {
        this.game = game;
        createPauseMenu();
    }

    public void showPauseMenu() {
        pauseMenu.setVisible(true);
    }
    private void createPauseMenu() {
        pauseMenu = new JDialog(game, "Game Paused", true);
        pauseMenu.setSize(300, 200);
        pauseMenu.setLayout(new GridLayout(3, 1));
        pauseMenu.setLocationRelativeTo(game);
        pauseMenu.setUndecorated(true);

        JButton resumeButton = new JButton("Resume Game");
        JButton restartButton = new JButton("Restart Game");
        JButton mainMenuButton = new JButton("Back to Main Menu");

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                game.resumeGame();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame();
                pauseMenu.dispose();
            }
        });

        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                game.showMainMenu();
                pauseMenu.dispose();
            }
        });

        pauseMenu.add(resumeButton);
        pauseMenu.add(restartButton);
        pauseMenu.add(mainMenuButton);
    }
}
