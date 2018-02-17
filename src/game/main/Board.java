package game.main;

import game.sprites.BlackSqaure;
import game.sprites.EmptySqaure;
import game.sprites.Square;
import game.sprites.SuggestedBlackSqaure;
import game.sprites.SuggestedWhiteSqaure;
import game.sprites.WhiteSqaure;

import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;

public class Board {

    private Square[][] board;

    private final int boundX = (Game.WIDTH-(Square.SIZE*8))/2;
    private final int boundY = 107;

    public static final int SIZE = 8;

    private int validMoves;

    public Board() {
        board = new Square[Board.SIZE][Board.SIZE];
        for (int i = 0; i < Board.SIZE; i++) {
            Arrays.fill(board[i], new EmptySqaure());
        }
        board[3][3] = new BlackSqaure();
        board[3][4] = new WhiteSqaure();
        board[4][4] = new BlackSqaure();
        board[4][3] = new WhiteSqaure();
    }

    public Location getLoc(int x, int y) {
        if (x > boundX && x < Game.WIDTH-boundX && y > boundY && y < boundY+(Square.SIZE*Board.SIZE)) {
            return new Location((y - boundY) / Square.SIZE, (x - boundX) / Square.SIZE);
        }
        return null;
    }

    public void select(Location loc) {
        if (isValid(loc)) {
            if (Game.blackTurn()) {
                board[loc.row()][loc.col()] = new BlackSqaure();
            } else {
                board[loc.row()][loc.col()] = new WhiteSqaure();
            }
            flip(loc);
            Game.nextTurn();
        }
    }

    public void render(GraphicsContext gc) {
        int moves = 0;
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (!isValid(new Location(i, j))) {
                    gc.drawImage(board[i][j].getImage(), boundX + (j * Square.SIZE), boundY + (i * Square.SIZE));
                } else if (Game.blackTurn()) {
                    gc.drawImage(new SuggestedBlackSqaure().getImage(), boundX + (j * Square.SIZE), boundY + (i * Square.SIZE));
                    moves++;
                } else {
                    gc.drawImage(new SuggestedWhiteSqaure().getImage(), boundX + (j * Square.SIZE), boundY + (i * Square.SIZE));
                    moves++;
                }
            }
        }
        validMoves = moves;
    }

    private boolean isValid(Location loc) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && checkDir(loc, i, j, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDir(Location loc, int drow, int dcol, boolean mustBeEmpty) {
        if (mustBeEmpty && board[loc.row()][loc.col()].getType() != Square.Type.Empty) {
            return false;
        }

        int row = loc.row() + drow, col = loc.col() + dcol;
        for (int i = 0; row < Board.SIZE && row >= 0 && col < Board.SIZE && col >= 0; i++, row += drow, col += dcol) {
            if (board[row][col].getType() == Game.currColor()) {
                if (i > 0) {
                    return true;
                }
                return false;
            } else if (board[row][col].getType() == Square.Type.Empty) {
                return false;
            }
        }
        return false;
    }

    private void flip(Location loc) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && checkDir(loc, i, j, false)) {
                    int row = loc.row()+i, col = loc.col()+j;
                    while (board[row][col].getType() == Game.nextColor()) {
                        if (Game.blackTurn()) {
                            board[row][col] = new BlackSqaure();
                        } else {
                            board[row][col] = new WhiteSqaure();
                        }
                        row += i;
                        col += j;
                    }
                }
            }
        }
    }

    public int whiteScore() {
        int count = 0;
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (board[i][j].getType() == Square.Type.White) {
                    count++;
                }
            }
        }
        return count;
    }

    public int blackScore() {
        int count = 0;
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (board[i][j].getType() == Square.Type.Black) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getValidMoves() {
        return validMoves;
    }
}
