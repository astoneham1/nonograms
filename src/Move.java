// this class stores information about a move that the user has made

public class Move {
    int[] location;
    int oldColor;
    int newColor;

    public Move(int row, int col, int oldColor, int newColor) {
        this.location = new int[] {row, col};
        this.oldColor = oldColor;
        this.newColor = newColor;
    }
}
