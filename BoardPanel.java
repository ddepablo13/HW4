import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    private GameStatusListener statusListener;
    private AIPlayer aiPlayer;
    private GameMode currentGameMode;

    public static final int BOARD_SIZE = 15; // Assuming a 15x15 board
    private static final int CELL_SIZE = 30; // The size of each cell in the grid
    private static final int PIECE_SIZE = 20; // Size of each game piece
    private final Color[] pieceColors = {Color.BLACK, Color.WHITE}; // Colors for two players
    private final int[][] boardState = new int[BOARD_SIZE][BOARD_SIZE]; // Stores the state of the board
    private int currentPlayer = 0; // 0 for one player, 1 for the other
    private int hoverRow = -1;
    private int hoverCol = -1;
    public enum GameMode {
        HUMAN_HUMAN,
        HUMAN_AI;
    }

    public BoardPanel() {
        setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE));
        initializeBoard();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;
                placePiece(row, col);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverRow = -1;
                hoverCol = -1;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                hoverRow = e.getY() / CELL_SIZE;
                hoverCol = e.getX() / CELL_SIZE;
                repaint();
            }

        });
    }

    public void setAIPlayer(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    public void setGameMode(GameMode gameMode) {
        this.currentGameMode = gameMode;
        // Additional setup based on game mode
    }

    public GameMode getGameMode() {
        return this.currentGameMode;
    }

    private void aiMove() {
        Point move = aiPlayer.makeMove(boardState);
        if (move != null) {
            placePiece(move.y, move.x);
        }
    }

    public void setStatusListener(GameStatusListener listener) {
        this.statusListener = listener;
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardState[i][j] = -1; // -1 will denote an empty cell
            }
        }
    }

    private void placePiece(int row, int col) {
        if (isCellEmpty(row, col)) {
            boardState[row][col] = currentPlayer;
            repaint(); // Request the board to repaint itself with the new piece

            if (isWin(row, col)) {
                JOptionPane.showMessageDialog(this,
                        "Player " + (currentPlayer == 0 ? "Black" : "White") + " wins!",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                if (statusListener != null) {
                    statusListener.onStatusUpdate("Player " + (currentPlayer == 0 ? "Black" : "White") + " wins!");
                }
                resetBoard(); // Reset the board after a win
            } else {
                currentPlayer = (currentPlayer + 1) % 2; // Switch turns
                if (statusListener != null) {
                    statusListener.onStatusUpdate("Player " + (currentPlayer == 0 ? "Black" : "White") + "'s turn.");
                }

                // If the game mode is Human vs AI and it's AI's turn, make the AI move
                if (currentGameMode == GameMode.HUMAN_AI && currentPlayer == aiPlayer.getPlayerNumber()) {
                    System.out.println("AI is making a move..."); // Debug statement
                    SwingUtilities.invokeLater(this::aiMove);
                }
            }
        }
    }

    private boolean isCellEmpty(int row, int col) {
        return boardState[row][col] == -1;
    }

    private boolean isWin(int placedRow, int placedCol) {
        int player = boardState[placedRow][placedCol];
        return checkFiveInARow(placedRow, placedCol, 1, 0, player) ||
                checkFiveInARow(placedRow, placedCol, 0, 1, player) ||
                checkFiveInARow(placedRow, placedCol, 1, 1, player) ||
                checkFiveInARow(placedRow, placedCol, -1, 1, player);
    }

    private boolean checkFiveInARow(int row, int col, int dRow, int dCol, int player) {
        int count = 1; // Start with the last placed piece
        int r, c;
        r = row + dRow;
        c = col + dCol;
        while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && boardState[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && boardState[r][c] == player) {
            count++;
            r -= dRow;
            c -= dCol;
        }
        return count == 5;
    }

    public void resetBoard() {
        initializeBoard();
        currentPlayer = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                g2d.setColor(Color.GRAY);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                if (i == hoverRow && j == hoverCol && boardState[i][j] == -1) {
                    g2d.setColor(new Color(120, 120, 120, 80)); // semi-transparent highlight
                    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
                if (boardState[i][j] != -1) {
                    g2d.setColor(pieceColors[boardState[i][j]]);
                    g2d.fillOval(x + (CELL_SIZE - PIECE_SIZE) / 2, y + (CELL_SIZE - PIECE_SIZE) / 2, PIECE_SIZE, PIECE_SIZE);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(boardState[i][j] == 0 ? Color.WHITE : Color.BLACK);
                    g2d.drawOval(x + (CELL_SIZE - PIECE_SIZE) / 2, y + (CELL_SIZE - PIECE_SIZE) / 2, PIECE_SIZE, PIECE_SIZE);
                }
            }
        }
        g2d.dispose();
    }
}
