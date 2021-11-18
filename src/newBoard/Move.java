package newBoard;

public class Move {
    int startSquare;
    int targetSquare;
    char piece;

    public Move (int sq, int tq, char piece) {
        this.startSquare = sq;
        this.targetSquare = tq;
        this.piece = piece;
    }

    public String moveToString () {
        return "" + startSquare + " " + targetSquare + " " +piece;
    }

    public char getPiece() {
        return piece;
    }

    public int getStartSquare() {
        return startSquare;
    }

    public int getTargetSquare() {
        return targetSquare;
    }
}
