package game.sprites;

import javafx.scene.image.Image;

public class Square {

    public enum Type {
        Empty, White, Black, SuggestedWhite, SuggestedBlack
    }

    public static final int SIZE = 80;

    private Type type;
    Image image = null;

    public Square(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Square)){
            throw new ClassCastException();
        }
        return this.type == ((Square)o).type;
    }

}
