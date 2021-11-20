package newBoard;

import java.util.ArrayList;

public class Move {
    int[] posIndex;
    String[] pos;
    char piece;

    public Move (String[] pos, int[] posIndex, char piece) {
        this.pos = pos;
        this.posIndex = posIndex;
        this.piece = piece;
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

}
