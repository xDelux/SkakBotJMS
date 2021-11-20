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
    Board BoardClass;
    int[] boardIndex;
    char[] board;
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
        this.BoardClass = boardClass;
        this.board = boardClass.getBoardChar();
        this.boardIndex = boardClass.getBoardIndex();
        generateMoves();
    }

    public Board returnWorkloadBoardClass() {return BoardClass;}
    public char[] workloadBoard() {
        return board;
    }
    public int[] returnBoardIndex() {
        return BoardClass.boardIndex;
    }
    public int[] returnBoardInt() {
        return BoardClass.getBoardInt();
    }



    public ArrayList<Move> getSpecificMoves(int startSquare) {
        tempMoves = new ArrayList<>();
        for (Move m : moves) {
            if(m.getStartSquare() == startSquare)
                tempMoves.add(m);
        }
        return tempMoves;
    }
    public ArrayList<Move> getAllMoves() {
        return moves;
    }

    /* MOVE EXECUTION */
    public void executeMove (Move move) {
        System.out.println(
                "EXECUTING ORDER 66 MOVE: " +
                        move.getPiece() +
                        " FROM SQUARE: " +
                        move.getStartSquare() +
                        " TO: " +
                        move.getTargetSquare());
        board[move.getTargetSquare()] = board[move.getPiece()];
        board[move.getStartSquare()] = ' ';
        whitesTurn = !whitesTurn;
        generateMoves();
    }

    public void moveByIndex (int startSquare, int targetSquare) {
        if(!(startSquare == '0' || targetSquare == '0')) {
            board[boardIndex[targetSquare]] = board[boardIndex[startSquare]];
            board[boardIndex[startSquare]] = ' ';

            whitesTurn = !whitesTurn;
            generateMoves();
        }

    }
    /* END : MOVE EXEC */


    /* GENERATION OF EVERY MOVE
    * Every square is checked for moves within the current board position  */
    public void generateMoves() {
        moves.clear();
        char piece;
        for (int i = 0; i < 64; i++) {
            /* Start from the first piece */
            startSquare = boardIndex[i];
            piece = board[startSquare];

            /* TESTING */
            //System.out.println("SQUARE: [" +BoardClass.getRank(startSquare) + BoardClass.getFile(startSquare) + "] - " +startSquare + " PIECE: " + piece);
//            System.out.println("Testing getFile() of all squares : ");
//            System.out.println(getFile(startSquare));
            /* TESTING ENDS */

            if (piece == '0')
                break;

            if(boardIndex[i] > 50 && piece == 'P')
                System.out.println();

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

//        for (Move m : moves) {
//            System.out.println(m.moveToString());
//        }

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
        if(piece == 'q' || piece == 'Q')
            System.out.println();

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
                break;

            /* if target piece is friendly break */
            if (isFriendlyFire(targetPiece))
                break;

            if(i == 0) {
                /* If opponents piece is on the square can't move any further */
                if (isEnemyFire(targetPiece))
                    continue;

                /* Adds newly found move to list */
                tempMoves.add(genericMove(startSquare, targetSquare, piece));

                /* CHECKING IF PAWN HASNT MOVED */
                if (BoardClass.getFile(startSquare) == 2 || BoardClass.getFile(startSquare) == 7) {
                    /* CHECKS THE SQUARE TWO UP FROM PAWN */
                    targetSquare += pawnOffsets[i];
                    targetPiece = board[targetSquare];
                    /* IF SQUARE IS EMPTY : MOVE UP TWO AS MOVE*/
                    if (!isEnemyFire(targetPiece)) {
                        tempMoves.add(genericMove(startSquare, targetSquare, piece));
                    }
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
                new String[] {BoardClass.posToString(startSquare), BoardClass.posToString(targetSquare)},
                new int[] {startSquare, targetSquare},
                piece);
    }
}