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

    // White and black chars
    private final char[] whiteChars = new char[]{'R', 'N', 'B', 'Q', 'K', 'P'};
    private final char[] blackChars = new char[]{'r', 'n', 'b', 'q', 'k', 'p'};

    //initialize move list
    ArrayList<char[]> moves = new ArrayList<>();

    //constructor
    public MoveGen() {
        generateMoves();
    }

    private void generateMoves() {
        for (int i = 0; i < 64; i++) {
            generateSlidingMoves(boardIndex[i],'b');
        }

    }


    private void generateRookMoves () {

    }
    private void generateSlidingMoves (int startSquare, char piece) {
        int startIndex;
        startIndex = (piece == 'b' || piece == 'B' ) ? 4 : 0;
        startIndex = (piece == 'r' || piece == 'R') ? 0 : 4;

        for (int i = startIndex; i < 8; i++) {

            int target = board[startSquare + directionOffsets[i]];

            for(int targetSquare = i;;){

                targetSquare = board[startSquare + directionOffsets[i]];

                if(targetSquare == '0')
                    break;

                if(targetSquare  != ' '){
                    if(Character.isUpperCase(targetSquare)) {

                    }
                }
//                int targetSquare = startSquare + directionOffsets[i];

            }

        }
    }
    private void generateKnightMoves () {

    }
    private void generate () {

    }

}