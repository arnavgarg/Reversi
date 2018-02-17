package game.sprites;

import game.sprites.Square;
import javafx.scene.image.Image;

public class BlackSqaure extends Square {

    public BlackSqaure() {
        super(Type.Black);
        image = new Image("black.png");
    }

}
