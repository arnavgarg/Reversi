package game.main;

import game.sprites.Square;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

enum State {
    START, PLAY, END
}

public class Game extends Application {

    public static final int WIDTH = 680;
    public static final int HEIGHT = 770;

    private Canvas canvas;
    private Board board;

    private Location mouseOver;

    private static boolean isBlackTurn;
    private State state = State.START;

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        canvas = new Canvas(Game.WIDTH, Game.HEIGHT);
        root.getChildren().add(canvas);

        canvas.setOnKeyPressed(
            event -> {
                if (event.getCode() == KeyCode.ENTER && (state == State.START || state == State.END)) {
                    play();
                } else if (event.getCode() == KeyCode.Q) {
                    System.exit(0);
                }
            }
        );
        canvas.setOnMouseMoved(
            event -> {
                if (state == State.PLAY) {
                    mouseOver = board.getLoc((int) event.getX(), (int) event.getY());
                }
            }
        );
        canvas.setOnMouseClicked(
            event -> {
                if (state == State.PLAY) {
                    Location loc = board.getLoc((int) event.getX(), (int) event.getY());
                    if (loc != null) {
                        board.select(loc);
                    }
                }
            }
        );
        canvas.requestFocus();

        stage.setTitle("Reversi");
        stage.setResizable(false);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                render();
                if (state == State.PLAY) {
                    checkEnd();
                }
            }
        }.start();
    }

    private void play() {
        state = State.PLAY;
        board = new Board();
        isBlackTurn = true;
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // draw bg
        gc.setFill(Color.INDIANRED);
        gc.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        if (state == State.START) {
            gc.setFill(Color.BLUE);
            gc.setStroke(Color.GREEN);
            gc.setFont(new Font(150));
            gc.setLineWidth(1.5);
            gc.fillText("Reversi", 87, 260);
            gc.strokeText("Reversi", 87, 260);

            gc.setFont(new Font(50));
            gc.fillText("Press Enter to Play", 120, 540);
        } else {
            // draw score
            gc.setFont(new Font(100));
            gc.setFill(Color.BLACK);
            gc.fillText(Integer.toString(board.blackScore()), 425, 90);
            gc.setFill(Color.WHITE);
            gc.fillText(Integer.toString(board.whiteScore()), 190, 90);

            // draw board
            board.render(gc);

            // draw yellow cursor
            if (mouseOver != null) {
                gc.setStroke(Color.YELLOW);
                gc.setLineWidth(2.5);
                gc.strokeRoundRect((Game.WIDTH-(Square.SIZE*8))/2 + Square.SIZE*mouseOver.col(), 107+Square.SIZE*mouseOver.row(), Square.SIZE, Square.SIZE, 2, 2);
            }

            if (state == State.END) {
                gc.setFill(Color.BROWN);
                gc.fillRect(90, 130, 500, 200);

                gc.setFont(new Font(80));
                if (board.blackScore() > board.whiteScore()) {
                    gc.setFill(Color.BLACK);
                    gc.fillText("Black Wins!", 122, 220);
                } else if (board.blackScore() < board.whiteScore()) {
                    gc.setFill(Color.WHITE);
                    gc.fillText("White Wins!", 122, 220);
                } else {
                    gc.setFill(Color.DARKSLATEGRAY);
                    gc.fillText("Tie!", 270, 220);
                }

                gc.setFill(Color.BLACK);
                gc.setFont(new Font(36));
                gc.fillText("Press Enter to Play Again", 135, 295);
            }
        }
    }

    public static void nextTurn() {
        isBlackTurn = !isBlackTurn;
    }

    public static Square.Type currColor() {
        if (!isBlackTurn) {
            return Square.Type.White;
        } else {
            return Square.Type.Black;
        }
    }

    public static Square.Type nextColor() {
        if (isBlackTurn) {
            return Square.Type.White;
        } else {
            return Square.Type.Black;
        }
    }

    public static boolean blackTurn() {
        return isBlackTurn;
    }

    private void checkEnd() {
        if (board.blackScore() + board.whiteScore() == 64 || (board.getValidMoves() == 0 && board.blackScore() + board.whiteScore() > 4)) {
            state = State.END;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}
