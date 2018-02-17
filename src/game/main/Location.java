package game.main;

public class Location {

    private int row;
    private int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location)) {
            throw new ClassCastException();
        }
        return ((Location) o).col == col && ((Location) o).row == row;
    }

}
