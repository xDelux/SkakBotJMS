package Chess.Moves;

import Chess.Board;
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
    int[] whitePawnOffsets = new int[]{-12, -13, -11};
    int[] blackPawnOffsets = new int[]{12, 13, 11};

    //initialize board and turn variable.
    int[] boardIndex;
    char[] board;
    char[] rank;
    int[] file;
    boolean whitesTurn = true;
    boolean isInCheck;
    int kingAttackCount;
    char[] pinnedPieces;
    int startSquare;
    int targetSquare;
    char targetPiece;

    private void setupRanksAndFiles() {
        rank = new char[] {
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };

        file = new int[] {
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 8, 8, 8, 8, 8, 8, 8, 8, '0', '0',
                '0', '0', 7, 7, 7, 7, 7, 7, 7, 7, '0', '0',
                '0', '0', 6, 6, 6, 6, 6, 6, 6, 6, '0', '0',
                '0', '0', 5, 5, 5, 5, 5, 5, 5, 5, '0', '0',
                '0', '0', 4, 4, 4, 4, 4, 4, 4, 4, '0', '0',
                '0', '0', 3, 3, 3, 3, 3, 3, 3, 3, '0', '0',
                '0', '0', 2, 2, 2, 2, 2, 2, 2, 2, '0', '0',
                '0', '0', 1, 1, 1, 1, 1, 1, 1, 1, '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };
    }

    //initialize move list
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;

    //constructor
    public MoveGen(int[] boardIndex, char[] board, boolean whitesTurn) {
        this.boardIndex = boardIndex;
        this.board = board;
        this.whitesTurn = whitesTurn;
        setupRanksAndFiles();

    }

    public ArrayList<Move> updateAndGenerateMoves(char[] board, boolean isWhitesTurn) {
        this.whitesTurn = isWhitesTurn;
        this.board = board;
        return generateMoves();
    }


    /* GENERATION OF EVERY MOVE
    * Every square is checked for moves within the current board position  */
    public ArrayList<Move> generateMoves() {
        moves = new ArrayList<>();
        char piece;
        for (int i = 0; i < 64; i++) {
            /* Start from the first piece */
            startSquare = boardIndex[i];
            piece = board[startSquare];

/*
             TESTING
*/
//            System.out.println("SQUARE: [" +getRank(startSquare) + getFile(startSquare) + "] - " +startSquare + " PIECE: " + piece);
//            System.out.println("Testing getFile() of all squares : ");
//            System.out.println(getFile(startSquare));
/*
             TESTING ENDS
*/

            if (piece == '0')
                break;

            /*if(boardIndex[i] > 50 && piece == 'P')
                System.out.println();*/

            if (isFriendlyFire(piece)) {
                if(!isPawnPiece(piece)) {
                    if (isSlidingPiece(piece))
                        moves.addAll(generateSlidingMoves(startSquare, piece));
                    if (isKingPiece(piece) || isKnightPiece(piece))
                        moves.addAll(generateKingOrKnightMoves(startSquare, piece));
                } else
                    moves.addAll(generatePawnMoves(startSquare, piece));
            }
        }
        return moves;
    }





    /* Checking if a piece is the same color as the player */
    private boolean isFriendlyFire(char piece) {

        /* IF FRIENDLY FOR WHITE */
        if (whitesTurn && Character.isUpperCase(piece))
            return true;

        /* IF FRIENDLY FOR BLACK */
        if (!whitesTurn && Character.isLowerCase(piece))
            return true;

        return false;
    }
    private boolean isEnemyFire(char piece) {
        if(whitesTurn && Character.isLowerCase(piece))
            return true;

        if(!whitesTurn && Character.isUpperCase(piece))
            return true;

        return false;
    }

    /* Generic methods to check what type of piece a char is */
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
        startIndex = (piece == 'b' || piece == 'B') ? 4 : 0;
        endIndex= (piece == 'r' || piece == 'R') ? 4 : 8;


//        piece = 'q';

        for (int i = startIndex; i < endIndex; i++) {
            /* Looping through all the possible direction squares */
            for (targetSquare = startSquare + directionOffsets[i];; targetSquare+= directionOffsets[i]) {
                /* Setting target square and what piece stands on it */
//                targetSquare = startSquare + directionOffsets[i];
                targetPiece = board[targetSquare];

                /* if target piece is OUT OF BOUNDS */
                if (targetPiece == '0')
                    break;

                /* if target piece is friendly break */
                if (isFriendlyFire(targetPiece))
                    break;

                /* Adds newly found move to list */
                moves.add(genericMove(startSquare, targetSquare, piece));

                /* If opponents piece is on the square can't move any further */
                if (isEnemyFire(targetPiece))
                    break;
            }
        }

        /* Returning found moves */
        return tempMoves;
    }


    /* GENERATING KING OR KNIGHT MOVES
    * they are different as they can only jump once to a specific square
    * and not slide around. so doesn't have to check for enemy piece to stop */
    private ArrayList<Move> generateKingOrKnightMoves(int startSquare, char piece) {
        tempMoves = new ArrayList<>();
        int[] offset = (isKingPiece(piece)) ? directionOffsets : knightOffsets;

        for (int dos : offset) {
            /* Looping through all the possible direction squares */
            /* Setting target square and what piece stands on it */
            targetSquare = startSquare + dos;
            targetPiece = board[targetSquare];

            /* if target piece is OUT OF BOUNDS */
            if (targetPiece == '0')
                continue;

            /* if target piece is friendly break */
            if (isFriendlyFire(targetPiece))
                continue;

            /* Adds newly found move to list */
            tempMoves.add(genericMove(startSquare, targetSquare, piece));

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
                continue;

            /* if target piece is friendly break */
            if (isFriendlyFire(targetPiece))
                continue;

            if(i == 0) {//if going forward (pawnOffset[0] is +-12)
                /* If opponents piece is on the square can't move any further */
                if (isEnemyFire(targetPiece))
                    continue;

                /* Adds newly found move to list */
                tempMoves.add(genericMove(startSquare, targetSquare, piece));

                /* CHECKING IF PAWN HASNT MOVED */
                // TODO: below doesnt care if black or white
                if (getFile(startSquare) == 2 || getFile(startSquare) == 7) {
                    /* CHECKS THE SQUARE TWO UP FROM PAWN */
                    targetSquare += pawnOffsets[i];
                    targetPiece = board[targetSquare];
                    /* IF SQUARE IS EMPTY : MOVE UP TWO AS MOVE*/
                    if (!isEnemyFire(targetPiece)) {
                        tempMoves.add(genericMove(startSquare, targetSquare, piece));
                    }
                    /*if (targetPiece == ' ') {
                        tempMoves.add(genericMove(startSquare, targetSquare, piece, targetPiece));
                    }*/
                }

            } else {
                /* Diagonal pawn captures */
                if(isEnemyFire(targetPiece))
                    tempMoves.add(genericMove(startSquare, targetSquare, piece));
            }

        }
        /* Returning found moves */
        return tempMoves;
    }

    public Move genericMove(int startSquare, int targetSquare, char piece){
        return new Move(
                new String[] {posToString(startSquare), posToString(targetSquare)},
                new int[] {startSquare, targetSquare},
                piece);
    }

    public int getFile(int startSquare) {
        return file[startSquare];
    }
    public char getRank (int startSquare) {
        return rank[startSquare];
    }
    public String posToString(int startSquare) {
        return "" + getRank(startSquare) + "" + getFile(startSquare);
    }
}