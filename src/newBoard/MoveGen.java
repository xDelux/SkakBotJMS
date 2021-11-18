package newBoard;

import java.util.ArrayList;

public class MoveGen {
    /*
    DIRECTION OFFSETS FOR THE 12x12 BOARD
    -------------------------------------
    ORTHOGONALLY:   -1 = left,       -12 = down,       1 = right,    12 = up
    DIAGNONALLY:    -13 = left down, -11 = right down, 11 = left up, 13 = right up
    */
    int[] directionOffsets = new int[] {-1, -12, 1, 12, -13, -11, 11, 13};

    //initialize board and turn variable.
    Board boardInit = new Board();
    int[] boardIndex = boardInit.getBoardIndex(); ;
    char[] board = boardInit.getBoardChar();
    boolean whitesTurn = true;
    int targetSquare;
    char targetPiece;

    // White and black chars
    private final char[] whiteChars = new char[]{'R', 'N', 'B', 'Q', 'K', 'P'};
    private final char[] blackChars = new char[]{'r', 'n', 'b', 'q', 'k', 'p'};
    int[] color = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1
    };

    //initialize move list
    ArrayList<char[]> moves = new ArrayList<>();
    ArrayList<char[]> tempMoves;

    //constructor
    public MoveGen() {
        generateMoves();
    }

    private boolean isSameColor(char piece) {
        if (whitesTurn && Character.isUpperCase(piece))
            return true;
        else if (!whitesTurn && Character.isLowerCase(piece))
            return true;
        else
            return false;
    }

    private boolean isSlidingPiece (char piece) {
        return piece == 'b' || piece == 'B' || piece == 'q' || piece == 'Q' || piece == 'r' || piece == 'R';
    }

    private void generateMoves() {
        char piece;
        for (int i = 0; i < 64; i++) {
            /* Start from the first piece */
            piece = board[boardIndex[i]];
            if(piece != '0')
                break;

            if(isSameColor(piece)) {
                if(isSlidingPiece(piece)) {
                    generateSlidingMoves(boardIndex[i], piece);
                }

            }
        }

    }


    private void generateRookMoves () {

    }
    private void generateSlidingMoves (int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        int startIndex, endIndex;
        /* Sets the indexes related to the offsets and input */
        startIndex = (piece == 'r' || piece == 'R') ? 0 : 4;
        endIndex = (piece == 'b' || piece == 'B' ) ? 8 : 4;

        for (int i = startIndex; i < endIndex; i++) {

//            int target = board[startSquare + directionOffsets[i]];
            for (;;){
                targetSquare = startSquare + directionOffsets[i];
                targetPiece = board[targetSquare];

                if(targetPiece == '0')
                    break;

                if(isSameColor(targetPiece))
                    break;

                moves.add(new char[] {startSquare, targetSquare});

//                int targetSquare = startSquare + directionOffsets[i];

                System.out.println("infinite loop working");


            }

        }
    }
    private void generateKnightMoves () {

    }
    private void generate () {

    }

}