package Chess.Moves;

public class Move {
    int[] posIndex;
    String[] pos;
    char piece;
    int moveValue;
    char killPiece;

    public Move (String[] pos, int[] posIndex, char piece, char killPiece) {
        this.pos = pos;
        this.posIndex = posIndex;
        this.piece = piece;
        this.killPiece = killPiece;
    }

    public String moveToString () {
        return "MOVE: Piece  " + piece + "  from  " + pos[0] + " -> " + pos[1];
    }

    public char getPiece() {
        return piece;
    }

    public int getStartSquare() {
        return posIndex[0];
    }
    public int getTargetSquare() {
        return posIndex[1];
    }
    public int getMoveValue() {return moveValue;}

}
