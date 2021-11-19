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
    Board BoardClass = new Board();
    int[] boardIndex = BoardClass.getBoardIndex();
    char[] board = BoardClass.getBoardChar();
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
    public MoveGen(Board boardClass) {
        generateMoves();
    }

    private boolean isSameColor(char piece) {
        if (whitesTurn && Character.isUpperCase(piece))
            return true;
        if (!whitesTurn && Character.isLowerCase(piece))
            return true;
        return false;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    private void generateMoves() {
        moves.clear();
        char piece;
        for (int i = 0; i < 64; i++) {
            /* Start from the first piece */
            startSquare = boardIndex[i];
            piece = board[startSquare];

            /* TESTING */
            System.out.println("SQUARE: [" +BoardClass.getRank(startSquare) + BoardClass.getFile(startSquare) + "] - " +startSquare + " PIECE: " + piece);
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


    private boolean isSlidingPiece(char piece) {
        return  piece == 'b' || piece == 'B' ||
                piece == 'q' || piece == 'Q' ||
                piece == 'r' || piece == 'R';
    }

    private boolean isKnightPiece(char piece) {
        return (piece == 'n' || piece == 'N');
    }

    private boolean isKingPiece(char piece) {
        return (piece == 'k' || piece == 'K');
    }

    private boolean isPawnPiece(char piece) {
        return (piece == 'p' || piece == 'P');
    }

    /* GENERATING BISHOP, ROOK & QUEEN MOVES MOVES */
    private ArrayList<Move> generateSlidingMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        int startIndex, endIndex;
        /* Sets the indexes related to the offsets and input */
        startIndex = (piece == 'r' || piece == 'R') ? 4 : 0;
        endIndex = (piece == 'b' || piece == 'B') ? 4 : 8;

        for (int i = startIndex; i < endIndex; i++) {
            /* Looping through all the possible direction squares */
            for (; ; ) {
                /* Setting target square and what piece stands on it */
                targetSquare = startSquare + directionOffsets[i];
                targetPiece = board[targetSquare];

                /* if target piece is OUT OF BOUNDS */
                if (targetPiece == '0')
                    break;

                /* if target piece is friendly break */
                if (isSameColor(targetPiece))
                    break;

                /* Adds newly found move to list */
                moves.add(new Move(startSquare, targetSquare, piece));

                /* If opponents piece is on the square can't move any further */
                if (!isSameColor(targetPiece))
                    break;
            }
        }

        /* Returning found moves */
        return tempMoves;
    }

    /* GENERATING KING OR KNIGHT MOVES */
    private ArrayList<Move> generateKingOrKnightMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        int[] offset = (isKingPiece(piece)) ? directionOffsets : knightOffsets;

        for (int dos : offset) {
            /* Looping through all the possible direction squares */
            for(;;) {
                /* Setting target square and what piece stands on it */
                targetSquare = startSquare + dos;
                targetPiece = board[targetSquare];

                /* if target piece is OUT OF BOUNDS */
                if (targetPiece == '0')
                    break;

                /* if target piece is friendly break */
                if (isSameColor(targetPiece))
                    break;

                /* Adds newly found move to list */
                tempMoves.add(new Move(startSquare, targetSquare, piece));

                /* If opponents piece is on the square can't move any further */
                if (!isSameColor(targetPiece))
                    break;
            }
        }
        /* Returning found moves */
        return tempMoves;
    }

    /* GENERATING PAWN MOVES */
    private ArrayList<Move> generatePawnMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();

        /* PAWN MOVES BASED ON WHOS TURN IT IS. */
        int[] pawnOffsets = (whitesTurn) ? whitePawnOffsets : blackPawnOffsets;

        for (int i = 0; i < pawnOffsets.length; i++) {
            /* Setting target square and what piece stands on it */
            targetSquare = startSquare + pawnOffsets[i];
            targetPiece = board[targetSquare];

            /* if target piece is OUT OF BOUNDS */
            if (targetPiece == '0')
                break;

            /* if target piece is friendly break */
            if (isSameColor(targetPiece))
                break;

            /* Adds newly found move to list */
            tempMoves.add(new Move(startSquare, targetSquare, piece));

            /* CHECKING IF PAWN HASNT MOVED */
            if (i == 0 && (BoardClass.getFile(startSquare) == 2 || BoardClass.getFile(startSquare) == 7)) {
                /* CHECKS THE SQUARE TWO UP FROM PAWN */
                targetSquare += pawnOffsets[i];
                targetPiece = board[targetSquare];
                /* IF SQUARE IS EMPTY : MOVE UP TWO AS MOVE*/
                if (targetPiece == ' ') {
                    tempMoves.add(new Move(startSquare, targetSquare, piece));
                }
            }

            /* If opponents piece is on the square can't move any further */
            if (!isSameColor(targetPiece))
                break;

        }
        /* Returning found moves */
        return tempMoves;
    }
}