import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class AIPlayer {
    private static final int MAX_DEPTH = 3; // Limit the depth for performance reasons
    private int boardSize;
    private int player; // AI player number, e.g., 1 for white
    private int opponent; // Opponent player number, e.g., 0 for black

    // Constructor with board size and player number
    public AIPlayer(int boardSize, int player) {
        this.boardSize = boardSize;
        this.player = player;
        this.opponent = player == 0 ? 1 : 0;
    }

    // Default constructor if needed
    public AIPlayer() {
    }

    // Method to initiate the minimax algorithm and return the best move
    public Point makeMove(int[][] boardState) {
        Move bestMove = minimax(boardState, 0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return bestMove.point;
    }

    // Minimax algorithm with alpha-beta pruning
    private Move minimax(int[][] boardState, int depth, boolean maximizingPlayer, int alpha, int beta) {
        if (depth == MAX_DEPTH || gameIsOver(boardState)) {
            return new Move(evaluate(boardState), null);
        }

        List<Point> availableMoves = getAvailableMoves(boardState);
        Move bestMove = new Move(maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE, null);

        for (Point move : availableMoves) {
            boardState[move.y][move.x] = maximizingPlayer ? player : opponent;
            Move currentMove = minimax(boardState, depth + 1, !maximizingPlayer, alpha, beta);
            boardState[move.y][move.x] = -1; // Undo move

            if (maximizingPlayer && currentMove.score > bestMove.score) {
                bestMove = new Move(currentMove.score, move);
                alpha = Math.max(alpha, bestMove.score);
            } else if (!maximizingPlayer && currentMove.score < bestMove.score) {
                bestMove = new Move(currentMove.score, move);
                beta = Math.min(beta, bestMove.score);
            }

            if (beta <= alpha) {
                break;
            }
        }
        return bestMove;
    }

    // Method to get a list of available moves considering only empty spots near existing stones
    private List<Point> getAvailableMoves(int[][] boardState) {
        List<Point> availableMoves = new ArrayList<>();
        int searchRange = 2; // Define a proximity range to existing stones

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardState[row][col] == -1 && isAdjacentToStone(row, col, boardState, searchRange)) {
                    availableMoves.add(new Point(col, row));
                }
            }
        }
        return availableMoves;
    }

    // Method to check if a spot is near an existing stone
    private boolean isAdjacentToStone(int row, int col, int[][] boardState, int range) {
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < boardSize && newCol >= 0 && newCol < boardSize) {
                    if (boardState[newRow][newCol] != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Method to determine if the game is over
    private boolean gameIsOver(int[][] boardState) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardState[row][col] != -1 && hasWinningSequence(boardState, row, col, boardState[row][col])) {
                    return true;
                }
            }
        }

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardState[row][col] == -1) {
                    return false; // Game is not over if moves are available
                }
            }
        }
        return true; // Game is over if the board is full
    }

    // Method to check for a winning sequence on the board
    private boolean hasWinningSequence(int[][] boardState, int row, int col, int player) {
        return (checkDirection(boardState, row, col, 1, 0, player) >= 5 ||
                checkDirection(boardState, row, col, 0, 1, player) >= 5 ||
                checkDirection(boardState, row, col, 1, 1, player) >= 5 ||
                checkDirection(boardState, row, col, -1, 1, player) >= 5);
    }

    // Helper method to check for a sequence in a particular direction
    private int checkDirection(int[][] boardState, int row, int col, int dRow, int dCol, int player) {
        int count = 0;
        int r = row;
        int c = col;

        // Check in one direction
        while (r >= 0 && r < boardSize && c >= 0 && c < boardSize && boardState[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }

        // Check in the opposite direction
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < boardSize && c >= 0 && c < boardSize && boardState[r][c] == player) {
            count++;
            r -= dRow;
            c -= dCol;
        }

        // Subtract one to not double-count the original cell
        return count - 1;
    }

    // Evaluate the board to get a score for the AI player
    private int evaluate(int[][] boardState) {
        // Implementation of board evaluation logic
        // ...

        return 0; // Replace with actual evaluation logic
    }

    // Inner class to represent a move and its evaluation score
    private class Move {
        int score;
        Point point;

        Move(int score, Point point) {
            this.score = score;
            this.point = point;
        }
    }

    // Method to get the AI player's number
    public int getPlayerNumber() {
        return player;
    }
}
