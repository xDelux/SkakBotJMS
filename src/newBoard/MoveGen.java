package newBoard;

import java.util.ArrayList;

public class MoveGen {
    /*
    DIRECTION OFFSETS FOR THE 12x12 BOARD
    -------------------------------------
    ORTHOGONALLY:   -1 = left,       -12 = down,       1 = right,    12 = up
    DIAGONALLY:    -13 = left down, -11 = right down, 11 = left up, 13 = right up */
    int[] directionOffsets = new int[]{-1, -12, 1, 12, -13, -11, 11, 13};
    int[] knightOffsets = new int[]{-25, -14, 10, 23, 25, 14, -10, -23};

    /* 24 = PUSH TWO SQUARES FORWARD | 12 = ONE SQUARE | 13 & 11 CAPTURE DIAGONALLY*/
    int[] whitePawnOffsets = new int[]{12, 13, 11};
    int[] blackPawnOffsets = new int[]{-12, -13, -11};
    //initialize board and turn variable.
    Board boardInit = new Board();
    int[] boardIndex = boardInit.getBoardIndex();
    ;
    char[] board = boardInit.getBoardChar();
    boolean whitesTurn = true;
    int startSquare;
    int targetSquare;
    char targetPiece;

    // White and black chars
    private final char[] whiteChars = new char[]{'R', 'N', 'B', 'Q', 'K', 'P'};
    private final char[] blackChars = new char[]{'r', 'n', 'b', 'q', 'k', 'p'};
    int[] color = new int[]{
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
    ArrayList<Move> moves = new ArrayList<>();
    ArrayList<Move> tempMoves;

    //constructor
    public MoveGen() {
        generateMoves();
    }

    /* Convert 12x12 board to 8x8 board with the pieces */
    public char[] get8by8() {
        char[] tempBoard = new char[64];
        for (int i = 0; i < 64; i++) {
            tempBoard[i] = board[boardIndex[i]];
        }
        return tempBoard;
    }

    public int getFile(int startSquare) {
        int file;
        if (startSquare <= boardIndex[7])
            return file = 1;
        else if (startSquare <= boardIndex[15])
            return file = 2;
        else if (startSquare <= boardIndex[23])
            return file = 3;
        else if (startSquare <= boardIndex[31])
            return file = 4;
        else if (startSquare <= boardIndex[39])
            return file = 5;
        else if (startSquare <= boardIndex[47])
            return file = 6;
        else if (startSquare <= boardIndex[55])
            return file = 7;
        else if (startSquare <= boardIndex[63])
            return file = 8;
        else
            return 0;

    }


    private boolean isSameColor(char piece) {
        if (whitesTurn && Character.isUpperCase(piece))
            return true;
        if (!whitesTurn && Character.isLowerCase(piece))
            return true;
        return false;
    }

    private void generateMoves() {
        moves.clear();
        char piece;
        for (int i = 0; i < 64; i++) {
            /* Start from the first piece */
            startSquare = boardIndex[i];
            piece = board[startSquare];

            /* TESTING */
//            System.out.println("SQUARE: " +startSquare + " PIECE: " + piece);
//            System.out.println("Testing getFile() of all squares : ");
//            System.out.println(getFile(startSquare));
            /* TESTING ENDS */

            if (piece == '0')
                break;

            if (isSameColor(piece)) {
                if(!isPawnPiece(piece)) {
                    if (isSlidingPiece(piece))
                        moves.addAll(generateSlidingMoves(startSquare, piece));
                    if (isKingPiece(piece) || isKnightPiece(piece))
                        moves.addAll(generateKingOrKnightMoves(startSquare, piece));
                } else
                    moves.addAll(generatePawnMoves(startSquare, piece));
            }
        }

        for (Move m : moves) {
            System.out.println(m.moveToString());
        }

    }

    private char getTargetPiece(int targetSquare) {
        return targetPiece = board[targetSquare];
    }

    private int getTargetSquare(int startSquare, int i) {
        return targetSquare = startSquare + directionOffsets[i];
    }

    private void generateRookMoves() {

    }

    private boolean isSlidingPiece(char piece) {
        return piece == 'b' || piece == 'B' || piece == 'q' || piece == 'Q' || piece == 'r' || piece == 'R';
    }

    private ArrayList<Move> generateSlidingMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        int startIndex, endIndex;
        /* Sets the indexes related to the offsets and input */
        startIndex = (piece == 'r' || piece == 'R') ? 4 : 0;
        endIndex = (piece == 'b' || piece == 'B') ? 4 : 8;

        for (int i = startIndex; i < endIndex; i++) {

//            int target = board[startSquare + directionOffsets[i]];
            for (; ; ) {
                targetSquare = startSquare + directionOffsets[i];
                targetPiece = board[targetSquare];

//                genericMoves(targetPiece, targetSquare);
                /* if target piece is OUT OF BOUNDS */
                if (targetPiece == '0')
                    break;

                /* if target piece is friendly break */
                if (isSameColor(targetPiece))
                    break;

                /* Adds newly found move to list */
                moves.add(new Move(startSquare, targetSquare, piece));

                /* If opponents piece is on the square cant move any further */
                if (!isSameColor(targetPiece))
                    break;

            }
        }
        return tempMoves;
    }


    private boolean isKnightPiece(char piece) {
        return (piece == 'n' || piece == 'N');
    }

    private boolean isKingPiece(char piece) {
        return (piece == 'k' || piece == 'K');
    }

    private ArrayList<Move> generateKingOrKnightMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        int[] offset = (isKingPiece(piece)) ? directionOffsets : knightOffsets;

        for (int dos : offset) {

            for(;;) {
                targetSquare = startSquare + dos;
                targetPiece = board[targetSquare];

                if (targetPiece == '0')
                    break;

                if (isSameColor(targetPiece))
                    break;

                tempMoves.add(new Move(startSquare, targetSquare, piece));

                if (!isSameColor(targetPiece))
                    break;
            }
        }
        return tempMoves;
    }

    private boolean isPawnPiece(char piece) {
        return (piece == 'p' || piece == 'P');
    }

    private ArrayList<Move> generatePawnMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        /* PAWN MOVES BASED ON WHOS TURN IT IS. */
        int[] pawnOffsets = (whitesTurn) ? whitePawnOffsets : blackPawnOffsets;

        for (int i = 0; i < pawnOffsets.length; i++) {
            targetSquare = startSquare + pawnOffsets[i];
            targetPiece = board[targetSquare];

            if (targetPiece == '0')
                break;

            if (isSameColor(targetPiece))
                break;


            tempMoves.add(new Move(startSquare, targetSquare, piece));

            /* CHECKING IF PAWN HASNT MOVED */
            if (i == 0 && (getFile(startSquare) == 2 || getFile(startSquare) == 7)) {
                /* CHECKS THE SQUARE TWO UP FROM PAWN */
                targetSquare += pawnOffsets[i];
                targetPiece = board[targetSquare];
                /* IF SQUARE IS EMPTY : MOVE UP TWO AS MOVE*/
                if (targetPiece == ' ') {
                    tempMoves.add(new Move(startSquare, targetSquare, piece));
                }
            }

            if (!isSameColor(targetPiece))
                break;

        }

        return tempMoves;
    }
}