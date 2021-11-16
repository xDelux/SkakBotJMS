package pieces;

public class Piece {
    int value;
    int[] directionOffset;
    boolean isWhite;

    public Piece(int value, int[] directionOffset, boolean isWhite) {
        this.value = value;
        this.directionOffset = directionOffset;
        this.isWhite = isWhite;

    }

}
