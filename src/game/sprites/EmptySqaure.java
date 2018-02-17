package game.sprites;

import javafx.scene.image.Image;

public class EmptySqaure extends Square {

    public EmptySqaure() {
        super(Type.Empty);
        image = new Image("empty.png");
    }

}
