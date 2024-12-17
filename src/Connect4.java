import com.sun.opengl.util.GLUT;

import javax.media.opengl.GL;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

class Connect4 extends Component {
    private static final int WIDTH, HEIGHT, widthUnit, heightUnit, boardLength, boardHeight;
    private static Color[][] board;
    private static Color[] players;
    private static int turn;
    private static int hoverX;
    private static boolean gameDone;
    private static Point p1, p2; // Points for the winning line
    private static float animationProgress; // Progress of the animation
    private static Timer timer; // Timer for the turn
    private static TimerTask timerTask; // TimerTask for the turn
    private static int remainingTime; // Remaining time in seconds
    private static int player1Wins, player2Wins; // Track wins for best of 3 and best of 5
    private static int roundsToWin; // Number of rounds needed to win
    private static int player1Lives, player2Lives; // Lives for Player 1 and Player 2

    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    enum Mode { PLAYER_VS_PLAYER, PLAYER_VS_COMPUTER }
    enum Difficulty { EASY, MEDIUM, HARD }
    private static Mode currentMode;
    private static Difficulty currentDifficulty;


    static {
        int initialWidth = 800;
        int initialHeight = 800;
        boardLength = 7;
        boardHeight = 6;
        widthUnit = initialWidth / (boardLength + 2);
        WIDTH = widthUnit * (boardLength + 2);
        heightUnit = initialHeight / (boardHeight + 2);
        HEIGHT = heightUnit * (boardHeight + 2);

        board = new Color[boardLength][boardHeight];
        for (Color[] colors : board) {
            Arrays.fill(colors, Color.WHITE);
        }
        players = new Color[]{Color.YELLOW, Color.RED};
        turn = 0;
        animationProgress = 0f;
        timer = new Timer();
        remainingTime = 30; // Set initial remaining time to 30 seconds
        currentMode = Mode.PLAYER_VS_PLAYER; // Default mode
        currentDifficulty = Difficulty.EASY; // Default difficulty
        player1Wins = 0;
        player2Wins = 0;
        roundsToWin = 1; // Default to one round
        startTimer();
    }

    public void setMode(Mode mode) {
        currentMode = mode;
    }

    public void setDifficulty(Difficulty difficulty) {
        currentDifficulty = difficulty;
    }

    public void setRoundsToWin(int rounds) {
        roundsToWin = rounds;
    }

    public static void resetGame() {
        for (Color[] colors : board) {
            Arrays.fill(colors, Color.WHITE);
        }
        turn = 0;
        gameDone = false;
        p1 = null;
        p2 = null;
        animationProgress = 0f;
        remainingTime = 30;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        startTimer();
    }

    private void checkConnect(int x, int y) {
        if (gameDone) return;

        PointPair pair = search(board, x, y);

        if (pair != null) {
            p1 = new Point((pair.p1.x + 1) * widthUnit + widthUnit / 2, (pair.p1.y + 1) * heightUnit + heightUnit / 2);
            p2 = new Point((pair.p2.x + 1) * widthUnit + widthUnit / 2, (pair.p2.y + 1) * heightUnit + heightUnit / 2);
            gameDone = true;
            timer.cancel();

            // Update the win count
            if (turn == 0) {
                player1Wins++;
            } else {
                player2Wins++;
            }

            // Check if someone has won the series
            if (player1Wins >= roundsToWin || player2Wins >= roundsToWin) {
                String winnerMessage = "Player " + (player1Wins >= roundsToWin ? "2" : "1") + " wins the series!";
                System.out.println(winnerMessage);

                // Show pop-up with custom buttons
                int option = JOptionPane.showOptionDialog(
                        null, // Parent component (null means it will be centered on the screen)
                        winnerMessage, // Message to display
                        "Game Over", // Title of the dialog
                        JOptionPane.DEFAULT_OPTION, // Option type (DEFAULT_OPTION means no predefined options)
                        JOptionPane.INFORMATION_MESSAGE, // Message type
                        null, // Icon (null means use default)
                        new Object[] { "Restart Game", "Main Menu" }, // Custom buttons
                        "Restart Game" // Default button (the one selected by default)
                );

                // Handle button clicks
                if (option == 0) { // Restart Game
                    resetGame();
                } else if (option == 1) { // Main Menu
                    goToMainMenu(null);
                }

                // Reset the win counts for the series after showing the winner
                player1Wins = 0;
                player2Wins = 0;
            } else {
                // Reset the game for the next round
                resetGame();
            }
        }
    }

    private static void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!gameDone) {
                    remainingTime--;
                    if (remainingTime <= 0) {
                        gameDone = true;
                        String timeoutMessage = "Player " + (turn == 0 ? 2 : 1) + " wins by timeout!";

                        System.out.println(timeoutMessage);

                        // Show pop-up with custom buttons
                        showGameOverDialog("Time's up! Better luck next time.");

                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }
    public static void showGameOverDialog(String timeoutMessage) {
        // Custom font for the message
        JLabel messageLabel = new JLabel(timeoutMessage, JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setForeground(Color.RED); // Highlight the message in red

        // Custom button font and styling
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
        UIManager.put("OptionPane.buttonBackground", Color.GRAY);
        UIManager.put("OptionPane.buttonForeground", Color.WHITE);

        // Show the dialog
        int option = JOptionPane.showOptionDialog(
                null,
                messageLabel,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Restart Game", "Main Menu"}, // Custom button labels
                "Restart Game"
        );

        // Handle user response
        if (option == 0) {
            System.out.println("Restart Game selected");
        } else if (option == 1) {
            System.out.println("Main Menu selected");
        }
    }

    private void goToMainMenu(Connect4 connect4Game) {
        // Close the current game window
        if (currentFrame != null) {
            currentFrame.dispose(); // Close the current window
        }

        // Create a new instance of the MainMenu and show it
        new MainMenu(connect4Game, null); // Launch MainMenu (null for the game, as it's a new session)
    }


    private void drawScore(GL gl) {
        String scoreMessage;
        if (currentMode == Mode.PLAYER_VS_PLAYER) {
            scoreMessage = "Player 1: " + player2Wins + " | Player 2: " + player1Wins;
        } else if (currentMode == Mode.PLAYER_VS_COMPUTER) {
            scoreMessage = "Player: " + player2Wins + " | Computer: " + player1Wins;
        } else {
            scoreMessage = "Unknown mode";
        }

        GLUT glut = new GLUT();
        gl.glColor3f(1f, 1f, 1f); // White color for text
        gl.glRasterPos2f(10,  55); // Position: top-left corner of the board
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, scoreMessage);
    }
    public void draw(GL gl) {
        // Draw the frame for the board
        if (gameDone) {
            Color winningColor = players[(turn + 1) % players.length]; // Get the winning player's color
            gl.glColor3f(winningColor.getRed() / 255f, winningColor.getGreen() / 255f, winningColor.getBlue() / 255f); // Set color to winning player's color
        } else {
            gl.glColor3f(1f, 1f, 1f); // Set color to white
        }
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2f(widthUnit, heightUnit);
        gl.glVertex2f(WIDTH - widthUnit, heightUnit);
        gl.glVertex2f(WIDTH - widthUnit, HEIGHT - heightUnit);
        gl.glVertex2f(widthUnit, HEIGHT - heightUnit);
        gl.glEnd();

        for (int i = widthUnit; i <= WIDTH - widthUnit; i += widthUnit) {
            for (int j = heightUnit; j < HEIGHT - heightUnit; j += heightUnit) {
                int boardX = i / widthUnit - 1;
                int boardY = j / heightUnit - 1;

                if (boardX >= 0 && boardX < boardLength && boardY >= 0 && boardY < boardHeight) {
                    Color color = board[boardX][boardY];
                    if (color == Color.YELLOW) {
                        gl.glColor3f(1f, 1f, 0f); // Yellow
                    } else if (color == Color.RED) {
                        gl.glColor3f(1f, 0f, 0f); // Red
                    } else {
                        gl.glColor3f(1f, 1f, 1f); // White
                    }
                    drawCircle(gl, i + widthUnit / 2, j + heightUnit / 2, widthUnit / 2 - 5); // Draw circle for each cell
                }
            }
        }

        if (!gameDone) { // Draw hover piece if game is not done and it's the player's turn
            if (currentMode == Mode.PLAYER_VS_PLAYER || turn == 0) { // Only show hover for player 1 in PLAYER_VS_COMPUTER mode
                gl.glColor3f(turn == 0 ? 1f : 1f, turn == 0 ? 1f : 0f, 0f); // Yellow or Red based on turn
                drawCircle(gl, hoverX + widthUnit / 2, heightUnit / 2, widthUnit / 2 - 5); // Draw hover piece at top of column
            }
        } else if (p1 != null && p2 != null) {
            drawWinningLine(gl); // Draw the winning line if game is done
        }


        drawTurnMessage(gl);
        drawTimer(gl); // Draw the timer
        drawScore(gl);
        drawPauseButton(gl); // Draw the pause button on the top right
    }



    // Check iconSize and position to make sure they are not off-screen

    private void drawCircle(GL gl, int x, int y, int radius) {
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex2f(x, y);
        for (int i = 0; i <= 360; i++) {
            double angle = Math.toRadians(i);
            gl.glVertex2f((float) (x + Math.cos(angle) * radius), (float) (y + Math.sin(angle) * radius));
        }
        gl.glEnd();
    }

    private void drawWinningLine(GL gl) {
        gl.glColor3f(0f, 1f, 0f);
        gl.glLineWidth(5f);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(p1.x, p1.y);
        gl.glVertex2f(p1.x + (p2.x - p1.x) * animationProgress, p1.y + (p2.y - p1.y) * animationProgress);
        gl.glEnd();

        if (animationProgress < 1f) {
            animationProgress += 0.01f;
        }
    }

    private void drawTimer(GL gl) {
        String timeText = "Time: " + remainingTime + "s";
        GLUT glut = new GLUT();
        gl.glColor3f(1f, 1f, 1f);
        gl.glRasterPos2f(WIDTH / 2 - 50, HEIGHT - 40);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, timeText);
    }


    public void hover(int x) {
        x -= x % widthUnit;
        if (x < widthUnit) x = widthUnit;
        if (x >= WIDTH - widthUnit) x = WIDTH - 2 * widthUnit;
        hoverX = x;
        System.out.println("Hover: hoverX = " + hoverX);
    }



    public void drop() {
        int column = hoverX / widthUnit - 1;
        if (currentMode == Mode.PLAYER_VS_COMPUTER && turn == 1) {
            return;
        }
        if (board[column][0] != Color.WHITE) return;

        new Thread(() -> {
            Color color = players[turn];
            int row = 0;
            for (int i = 0; i < board[column].length && board[column][i] == Color.WHITE; i++) {
                row = i;
                board[column][i] = color;
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {
                }
                board[column][i] = Color.WHITE;
                if (gameDone) return;
            }
            if (gameDone) return;
            board[column][row] = color;
            System.out.println("Drop: column = " + column + ", row = " + row + ", color = " + color); // Debugging statement
            checkConnect(column, row);
        }).start();

        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }

        if (gameDone) return;
        turn = (turn + 1) % players.length;
        resetTimer(); // Reset the timer after a move

        if (currentMode == Mode.PLAYER_VS_COMPUTER && turn == 1) {
            // Wait 3 seconds after the player's move before AI plays
            new Thread(() -> {
                try {
                    Thread.sleep(1500); // 3-second delay before AI's turn
                    computerMove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }



    private void drawTurnMessage(GL gl) {
        String message;
        if (currentMode == Mode.PLAYER_VS_PLAYER) {
            message = "Player " + (turn + 1) + "'s turn";
        } else if (currentMode == Mode.PLAYER_VS_COMPUTER) {
            if (turn == 0) {
                message = "Your turn"; // If it's the player's turn
            } else {
                message = "Computer's turn"; // If it's the computer's turn
            }
        } else {
            message = "Unknown mode";
        }
        GLUT glut = new GLUT();
        gl.glColor3f(1f, 1f, 1f);
        gl.glRasterPos2f(10, 30);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, message);
    }
    private void drawLives(GL gl) {
        // Initialize message based on the mode
        String livesMessage;
        if (currentMode == Mode.PLAYER_VS_PLAYER || currentMode == Mode.PLAYER_VS_COMPUTER) {
            livesMessage = "Lives: ";
        } else {
            livesMessage = "Unknown Mode";
        }

        // Only show lives if roundsToWin is 2 (Best of 3) or 3 (Best of 5)
        if (roundsToWin == 1) {
            return; // Don't show lives if it's a single round match
        }

        // Determine the maximum number of lives based on the best-of mode
        int maxLives = (roundsToWin == 2) ? 3 : 5; // For best of 3, max lives are 3, for best of 5, max lives are 5

        // Build the visual representation of lives
        StringBuilder player1LivesDisplay = new StringBuilder();
        StringBuilder player2LivesDisplay = new StringBuilder();

        // Loop to add "1" for remaining lives and "0" for lost lives
        for (int i = 0; i < maxLives; i++) {
            if (i < player1Lives) {
                player1LivesDisplay.append("1 "); // Use "1" for each remaining life
            } else {
                player1LivesDisplay.append("0 "); // Use "0" for lost lives
            }

            if (i < player2Lives) {
                player2LivesDisplay.append("1 "); // Same for Player 2
            } else {
                player2LivesDisplay.append("0 ");
            }
        }

        // Draw the lives for both players
        GLUT glut = new GLUT();
        gl.glColor3f(1f, 1f, 1f); // Set text color to white

        // Draw Player 1 lives
        gl.glRasterPos2f(10, HEIGHT - 40);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Player 1: " + player1LivesDisplay.toString());

        // Draw Player 2 lives
        gl.glRasterPos2f(10, HEIGHT - 0);
        if (currentMode == Mode.PLAYER_VS_COMPUTER) {
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Computer: " + player2LivesDisplay.toString());
        } else {
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Player 2: " + player2LivesDisplay.toString());
        }
    }






    private PointPair search(Color[][] arr, int i, int j) {
        Color color = arr[i][j];
        int left, right, up, down;

        left = right = i;
        while (left >= 0 && arr[left][j] == color) left--;
        left++;
        while (right < arr.length && arr[right][j] == color) right++;
        right--;
        if (right - left >= 3) {
            return new PointPair(left, j, right, j);
        }

        down = j;
        while (down < arr[i].length && arr[i][down] == color) down++;
        down--;
        if (down - j >= 3) {
            return new PointPair(i, j, i, down);
        }

        left = right = i;
        up = down = j;
        while (left >= 0 && up >= 0 && arr[left][up] == color) { left--; up--; }
        left++; up++;
        while (right < arr.length && down < arr[right].length && arr[right][down] == color) { right++; down++; }
        right--; down--;
        if (right - left >= 3 && down - up >= 3) {
            return new PointPair(left, up, right, down);
        }

        left = right = i;
        up = down = j;
        while (left >= 0 && down < arr[left].length && arr[left][down] == color) {left--; down++;}
        left++; down--;
        while (right < arr.length && up >= 0 && arr[right][up] == color) {right++; up--;}
        right--; up++;
        if (right - left >= 3 && down - up >= 3) {
            return new PointPair(left, down, right, up);
        }

        return null;
    }



    private static void resetTimer() {
        timerTask.cancel();
        remainingTime = 30;
        startTimer();
    }

    private void computerMove() {
        switch (currentDifficulty) {
            case EASY:
                easyMove();
                break;
            case MEDIUM:
                minimaxMove(3); // Depth 3 for medium difficulty
                break;
            case HARD:
                minimaxMove(5); // Depth 5 for hard difficulty
                break;
        }
    }

    private void easyMove() {
        int col;
        do {
            col = (int) (Math.random() * boardLength);
        } while (board[col][0] != Color.WHITE);
        dropPiece(col);
    }

    private void minimaxMove(int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;
        for (int col = 0; col < boardLength; col++) {
            if (board[col][0] == Color.WHITE) {
                int row = getNextOpenRow(board, col);
                board[col][row] = players[turn];
                int score = minimax(board, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board[col][row] = Color.WHITE;
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }
        dropPiece(bestCol);
    }

    private int minimax(Color[][] board, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || gameDone) {
            return evaluateBoard(board);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < boardLength; col++) {
                if (board[col][0] == Color.WHITE) {
                    int row = getNextOpenRow(board, col);
                    board[col][row] = players[turn];
                    int eval = minimax(board, depth - 1, false, alpha, beta);
                    board[col][row] = Color.WHITE;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < boardLength; col++) {
                if (board[col][0] == Color.WHITE) {
                    int row = getNextOpenRow(board, col);
                    board[col][row] = players[(turn + 1) % players.length];
                    int eval = minimax(board, depth - 1, true, alpha, beta);
                    board[col][row] = Color.WHITE;
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }


    private int getNextOpenRow(Color[][] board, int col) {
        for (int row = 0; row < boardHeight; row++) {
            if (board[col][row] == Color.WHITE) {
                return row;
            }
        }
        return -1;
    }

    private boolean isTerminalNode(Color[][] board) {
        for (int col = 0; col < boardLength; col++) {
            for (int row = 0; row < boardHeight; row++) {
                if (board[col][row] == Color.WHITE) {
                    return false;
                }
                if (checkWin(col, row)) {
                    return true;
                }
            }
        }
        return true;
    }
    private int countConsecutive(Color[][] board, int col, int row, Color player) {
        int count = 0;

        for (int i = 0; i < 4 && col + i < boardLength; i++) {
            if (board[col + i][row] == player) count++;
        }

        return count;
    }
    private int evaluateBoard(Color[][] board) {
        int score = 0;

        for (int col = 0; col < boardLength; col++) {
            for (int row = 0; row < boardHeight; row++) {
                if (board[col][row] == players[turn]) {
                    score += countConsecutive(board, col, row, players[turn]) * 10;
                } else if (board[col][row] == players[(turn + 1) % players.length]) {
                    score -= countConsecutive(board, col, row, players[(turn + 1) % players.length]) * 10;
                }
            }
        }
        return score;
    }

    private boolean checkWin(int col, int row) {
        PointPair pair = search(board, col, row);
        return pair != null;
    }

    private void dropPiece(int column) {
        if (board[column][0] != Color.WHITE) return;

        new Thread(() -> {
            Color color = players[turn];
            int row = 0;
            for (int i = 0; i < board[column].length && board[column][i] == Color.WHITE; i++) {
                row = i;
                board[column][i] = color;
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {
                }
                board[column][i] = Color.WHITE;
                if (gameDone) return;
            }
            if (gameDone) return;
            board[column][row] = color;
            System.out.println("Drop: column = " + column + ", row = " + row + ", color = " + color); // Debugging statement
            checkConnect(column, row);
        }).start();

        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }
        if (gameDone) return;
        turn = (turn + 1) % players.length;
        resetTimer(); // Reset the timer after a move

        if (currentMode == Mode.PLAYER_VS_COMPUTER && turn == 1) {
            computerMove();
        }
    }

    static class PointPair {
        public Point p1, p2;

        PointPair(int x1, int y1, int x2, int y2) {
            p1 = new Point(x1, y1);
            p2 = new Point(x2, y2);
        }
    }
}