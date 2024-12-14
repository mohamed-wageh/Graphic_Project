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
    private static Point p1, p2;
    private static float animationProgress;
    private static Timer timer;
    private static TimerTask timerTask;

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
        startTimer();
    }

    public void draw(GL gl) {
        gl.glClearColor(0.4f, 0f, 0.6f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        if (gameDone) {
            Color winningColor = players[(turn + 1) % players.length];
            gl.glColor3f(winningColor.getRed() / 255f, winningColor.getGreen() / 255f, winningColor.getBlue() / 255f);
        } else {
            gl.glColor3f(1f, 1f, 1f);
        }
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2f(widthUnit, heightUnit);
        gl.glVertex2f(WIDTH - widthUnit, heightUnit);
        gl.glVertex2f(WIDTH - widthUnit, HEIGHT - heightUnit);
        gl.glVertex2f(widthUnit, HEIGHT - heightUnit);
        gl.glEnd();

        gl.glColor3f(0.95f, 0.95f, 0.95f);
        for (int i = widthUnit; i <= WIDTH - widthUnit; i += widthUnit) {
            for (int j = heightUnit; j < HEIGHT - heightUnit; j += heightUnit) {
                int boardX = i / widthUnit - 1;
                int boardY = j / heightUnit - 1;

                if (boardX >= 0 && boardX < boardLength && boardY >= 0 && boardY < boardHeight) {
                    Color color = board[boardX][boardY];
                    if (color == Color.YELLOW) {
                        gl.glColor3f(0.4f, 0f, 0.6f);
                    } else if (color == Color.RED) {
                        gl.glColor3f(0.4f, 0f, 0.6f);
                    } else {
                        gl.glColor3f(1f, 1f, 1f);
                    }
                    drawCircleWithShadow(gl, i + widthUnit / 2, j + heightUnit / 2, widthUnit / 2 - 5);
                }
            }
        }

        if (!gameDone) {
            gl.glColor3f(turn == 0 ? 0.4f : 0.4f, turn == 0 ? 0f : 0f, 0.6f);
            drawCircleWithShadow(gl, hoverX + widthUnit / 2, heightUnit / 2, widthUnit / 2 - 5);
        } else if (p1 != null && p2 != null) {
            drawWinningLine(gl);
        }
    }

    private void drawCircleWithShadow(GL gl, int x, int y, int radius) {
        gl.glColor3f(0f, 0f, 0f);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex2f(x + 3, y - 3);
        for (int i = 0; i <= 360; i++) {
            double angle = Math.toRadians(i);
            gl.glVertex2f((float) (x + Math.cos(angle) * (radius + 2)), (float) (y + Math.sin(angle) * (radius + 2)));
        }
        gl.glEnd();

        gl.glColor3f(0.4f, 0f, 0.6f);
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

    public void hover(int x) {
        x -= x % widthUnit;
        if (x < widthUnit) x = widthUnit;
        if (x >= WIDTH - widthUnit) x = WIDTH - 2 * widthUnit;
        hoverX = x;
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
            checkConnect(column, row);
        }).start();

        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }
        if (gameDone) return;
        turn = (turn + 1) % players.length;
        resetTimer();
    }

    private void checkConnect(int x, int y) {
        if (gameDone) return;

        PointPair pair = search(board, x, y);

        if (pair != null) {
            p1 = new Point((pair.p1.x + 1) * widthUnit + widthUnit / 2, (pair.p1.y + 1) * heightUnit + heightUnit / 2);
            p2 = new Point((pair.p2.x + 1) * widthUnit + widthUnit / 2, (pair.p2.y + 1) * heightUnit + heightUnit / 2);
            gameDone = true;
            timer.cancel();
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

    private static void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!gameDone) {
                    gameDone = true;
                }
            }
        };
        timer.schedule(timerTask, 30000);
    }

    private static void resetTimer() {
        timerTask.cancel();
        startTimer();
    }

    static class PointPair {
        public Point p1, p2;

        PointPair(int x1, int y1, int x2, int y2) {
            p1 = new Point(x1, y1);
            p2 = new Point(x2, y2);
        }
    }
}
