import javax.media.opengl.GL;
import java.awt.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

class Connect4 {
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

    public void resetGame() {
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

        if (!gameDone) { // Draw hover piece if game is not done
            gl.glColor3f(turn == 0 ? 1f : 1f, turn == 0 ? 1f : 0f, 0f); // Yellow or Red based on turn
            drawCircle(gl, hoverX + widthUnit / 2, heightUnit / 2, widthUnit / 2 - 5); // Draw hover piece at top of column
        } else if (p1 != null && p2 != null) {
            drawWinningLine(gl); // Draw the winning line if game is done
        }

        drawTimer(gl); // Draw the timer
    }

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
        gl.glColor3f(0f, 1f, 0f); // Set color to green for the winning line
        gl.glLineWidth(5f);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(p1.x, p1.y);
        gl.glVertex2f(p1.x + (p2.x - p1.x) * animationProgress, p1.y + (p2.y - p1.y) * animationProgress);
        gl.glEnd();

        if (animationProgress < 1f) {
            animationProgress += 0.01f; // Increment the animation progress
        }
    }

    private void drawTimer(GL gl) {
        String timeText = "Time: " + remainingTime + "s";
        // Set the color to white for the timer text
        gl.glColor3f(1f, 1f, 1f);
        // Draw the timer text at the top of the screen
        // You can use a text rendering library like GLUT or another method to draw text in OpenGL
        // For example, using GLUT:
        // GLUT.glutBitmapString(GLUT.BITMAP_HELVETICA_18, timeText);
    }

    public void hover(int x) {
        x -= x % widthUnit;
        if (x < widthUnit) x = widthUnit;
        if (x >= WIDTH - widthUnit) x = WIDTH - 2 * widthUnit;
        hoverX = x;
        System.out.println("Hover: hoverX = " + hoverX); // Debugging statement
    }

    public void drop() {
        int column = hoverX / widthUnit - 1;
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

    private void checkConnect(int x, int y) {
        if (gameDone) return;

        PointPair pair = search(board, x, y);

        if (pair != null) {
            p1 = new Point((pair.p1.x + 1) * widthUnit + widthUnit / 2, (pair.p1.y + 1) * heightUnit + heightUnit / 2);
            p2 = new Point((pair.p2.x + 1) * widthUnit + widthUnit / 2, (pair.p2.y + 1) * heightUnit + heightUnit / 2);
            gameDone = true;
            timer.cancel(); // Stop the timer when the game is done

            // Update win counts for best of 3 and best of 5
            if (turn == 0) {
                player1Wins++;
            } else {
                player2Wins++;
            }

            // Check if a player has won the series
            if (player1Wins >= roundsToWin || player2Wins >= roundsToWin) {
                System.out.println("Player " + (player1Wins >= roundsToWin ? "1" : "2") + " wins the series!");
                // Reset win counts for a new series
                player1Wins = 0;
                player2Wins = 0;
            } else {
                // Reset the board for the next round
                resetGame();
            }
        }
    }

    private PointPair search(Color[][] arr, int i, int j) {
        Color color = arr[i][j];
        int left, right, up, down;

        // check horizontally left to right
        left = right = i;
        while (left >= 0 && arr[left][j] == color) left--;
        left++;
        while (right < arr.length && arr[right][j] == color) right++;
        right--;
        if (right - left >= 3) {
            return new PointPair(left, j, right, j);
        }

        // check vertically top to bottom
        down = j;
        while (down < arr[i].length && arr[i][down] == color) down++;
        down--;
        if (down - j >= 3) {
            return new PointPair(i, j, i, down);
        }

        // check diagonal top left to bottom right
        left = right = i;
        up = down = j;
        while (left >= 0 && up >= 0 && arr[left][up] == color) { left--; up--; }
        left++; up++;
        while (right < arr.length && down < arr[right].length && arr[right][down] == color) { right++; down++; }
        right--; down--;
        if (right - left >= 3 && down - up >= 3) {
            return new PointPair(left, up, right, down);
        }

        // check diagonal top right to bottom left
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

    private static void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!gameDone) {
                    remainingTime--;
                    if (remainingTime <= 0) {
                        gameDone = true;
                        System.out.println("Player " + (turn + 1) % players.length + " wins by timeout!");
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000); // Schedule the task to run every second
    }

    private static void resetTimer() {
        timerTask.cancel(); // Cancel the current task
        remainingTime = 30; // Reset the remaining time to 30 seconds
        startTimer(); // Start a new timer task
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
        // Simple random move
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
        if (depth == 0 || isTerminalNode(board)) {
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
                    if (beta <= alpha) {
                        break;
                    }
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
                    if (beta <= alpha) {
                        break;
                    }
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
        return -1; // Should never happen if called correctly
    }

    private boolean isTerminalNode(Color[][] board) {
        // Check for a win or if the board is full
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

    private int evaluateBoard(Color[][] board) {
        // Evaluate the board and return a score
        // Positive score for maximizing player, negative for minimizing player
        int score = 0;
        // Add your evaluation logic here
        return score;
    }

    private boolean checkWin(int col, int row) {
        // Check if the current move wins the game
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