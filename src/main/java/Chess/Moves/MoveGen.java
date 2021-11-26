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
    int[] boardIndex, file;
    char[] board, rank, pinnedPieces;
    boolean whitesTurn = true, isInCheck = false, doubleCheck = false, bKingMoved = false, wKingMoved = false;
    boolean brRookMoved = false, blRookMoved = false, wrRookMoved = false, wlRookMoved = false;
    int startSquare;
    int targetSquare;
    char targetPiece;
    Move lastMove;

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
    ArrayList<Integer> attacks;
    ArrayList<Integer> defences;

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
        generateDefenseAndAttacks();
        if(isInCheck) {
            if(whitesTurn)
                wKingMoved = true;
            else
                bKingMoved = true;
        }
        if(doubleCheck) {
            for (int j = 0; j < 64; j++) {
                startSquare = boardIndex[j];
                piece = board[startSquare];

                if(piece == '0') {
                    break;
                }
                
                if(isKingPiece(piece) && isFriendlyFire(piece)) {
                    moves.addAll(generateKingOrKnightMoves(startSquare, piece));
                    doubleCheck = false;
                    isInCheck = false;
                    
                    return moves;
                } else {
                    continue;
                }
            } 
        }

        for (int i = 0; i < 64; i++) {
            /* Start from the first piece */
            startSquare = boardIndex[i];
            piece = board[startSquare];

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
        addCastlingMove();
        return moves;
    }


    private void generateDefenseAndAttacks() {
        attacks = new ArrayList<>();
        defences = new ArrayList<>();
        int startIndex, endIndex;
        char piece;
        for (int i = 0; i < 64; i++) {
            startSquare = boardIndex[i];
            piece = board[startSquare];

            if(piece == '0') {
                continue;
            }

            if(isEnemyFire(piece)) {
                 if(!isPawnPiece(piece)) {
                    if(isSlidingPiece(piece)) {
                        startIndex = (piece == 'b' || piece == 'B') ? 4 : 0;
                        endIndex = (piece == 'r' || piece == 'R') ? 4 : 8;
                        for (int j = startIndex; j < endIndex; j++) {
                            /* Looping through all the possible direction squares */
                            for (targetSquare = startSquare + directionOffsets[j];; targetSquare+= directionOffsets[j]) {
                                /* Setting target square and what piece stands on it */
                                //targetSquare = startSquare + directionOffsets[i];
                                targetPiece = board[targetSquare];
                
                                /* if target piece is OUT OF BOUNDS */
                                if (targetPiece == '0') {
                                    break;
                                }
                                
                                if(isKingPiece(piece)) {
                                    if(isInCheck) {
                                        doubleCheck = true;
                                    }
                                    isInCheck = true;
                                } 

                                /* if target piece is friendly break but add it first since its protected*/
                                if(Character.isLowerCase(piece) && Character.isLowerCase(targetPiece)) {
                                    if(!attacks.contains(targetSquare)) {
                                        attacks.add(targetSquare);
                                    } 
                                    break;
                                }
                                     
                                /* Adds newly found attack to list only if it's not in the list already */
                                if(!attacks.contains(targetSquare)) {
                                    attacks.add(targetSquare);
                                }                    
                            }
                        }

                    }
                    if (isKingPiece(piece) || isKnightPiece(piece)) {
                        int[] offset = (isKingPiece(piece)) ? directionOffsets : knightOffsets;
                        for (int dos : offset) {
                        /* Looping through all the possible direction squares */
                        /* Setting target square and what piece stands on it */
                        targetSquare = startSquare + dos;
                        targetPiece = board[targetSquare];

                        /* if target piece is OUT OF BOUNDS */
                        if (targetPiece == '0')
                            continue;
                        
                        if(isKingPiece(piece)) {
                            if(isInCheck) {
                                doubleCheck = true;
                            }
                            isInCheck = true;
                        }
                            
                        /* if target piece is friendly continue */
                        if (Character.isLowerCase(piece) && Character.isLowerCase(targetPiece)) {
                            if(!attacks.contains(targetSquare)) {
                                attacks.add(targetSquare);
                            }
                            continue;
                        }
                            

                        /* Adds newly found attack to list only if it's not in the list already */
                        if(!attacks.contains(targetSquare)) {
                            attacks.add(targetSquare);
                        }
                        
        }
                    }
                 } else {
                    int[] pawnOffsets = (!whitesTurn) ? whitePawnOffsets : blackPawnOffsets;
                    for (int k = 1; k < pawnOffsets.length; k++) {
                        targetSquare = startSquare + pawnOffsets[k];

                        // Skip the foward move we only need the attackslines
                        if(isKingPiece(piece)) {
                            if(isInCheck) {
                                doubleCheck = true;
                            }
                            isInCheck = true;
                        }
                        // Check if the pawnmove already is contained in attacks;
                        if(!attacks.contains(targetSquare)) {
                            attacks.add(targetSquare);
                        }
                    }
                }
            }
        }
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
    private boolean isOpponentFriendlyFire(char piece, char otherPiece) {
        return (Character.isUpperCase(piece) && Character.isUpperCase(otherPiece) || Character.isLowerCase(piece) && Character.isLowerCase(piece));
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
                moves.add(genericMove(startSquare, targetSquare, piece, targetPiece));

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
        boolean isKing = isKingPiece(piece);
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
            
            /* If the piece is the king and the targetSquare is being attacked dont add move  */
            if(isKing && attacks.contains(targetSquare)) {
                continue;
            }    
            /* Adds newly found move to list */
            tempMoves.add(genericMove(startSquare, targetSquare, piece, targetPiece));

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

                /* Adds newly found move to list, dont add if we are only checking for attacks */
                tempMoves.add(genericMove(startSquare, targetSquare, piece, targetPiece));

                /* CHECKING IF PAWN HASNT MOVED */
                // TODO: below doesnt care if black or white
                if (getFile(startSquare) == 2 || getFile(startSquare) == 7) {
                    /* CHECKS THE SQUARE TWO UP FROM PAWN */
                    targetSquare += pawnOffsets[i];
                    targetPiece = board[targetSquare];
                    /* IF SQUARE IS EMPTY : MOVE UP TWO AS MOVE*/
                    if (!isEnemyFire(targetPiece)) {
                        tempMoves.add(genericMove(startSquare, targetSquare, piece, targetPiece));
                    }
                    /*if (targetPiece == ' ') {
                        tempMoves.add(genericMove(startSquare, targetSquare, piece, targetPiece));
                    }*/
                }

            } else {
                /* Diagonal pawn captures */
                if(isEnemyFire(targetPiece))
                    tempMoves.add(genericMove(startSquare, targetSquare, piece, targetPiece));
            }

        }
        /* Returning found moves */
        return tempMoves;
    }

    private void checkLastMove() {
        // Don't bother to check if the kings has moved
        if(!wKingMoved && whitesTurn || !bKingMoved && !whitesTurn) {
            if(isKingPiece(lastMove.piece)) {
                if(whitesTurn) {
                    bKingMoved = true;
                } else {
                    wKingMoved = true;
                }
            }
            if(lastMove.piece == 'r' && lastMove.getStartSquare() == boardIndex[0])
                brRookMoved = true;
            if(lastMove.piece == 'r' && lastMove.getStartSquare() == boardIndex[7])
                blRookMoved = true;
            if(lastMove.piece == 'R' && lastMove.getStartSquare() == boardIndex[56])
                wlRookMoved = true;
            if(lastMove.piece == 'R' && lastMove.getStartSquare() == boardIndex[63])
                wrRookMoved = true;    
        }
    }

    private void addCastlingMove() {
        checkLastMove();
        if(whitesTurn) {
            if(!wKingMoved) {
                if(!wrRookMoved) {
                    if(board[boardIndex[61]] == ' ' && board[boardIndex[62]] == ' ') {
                        if(!attacks.contains(boardIndex[61]) && !attacks.contains(boardIndex[62])) {
                            // Add white right castleMove
                            moves.add(genericMove(60, 62, 'K', ' '));
                        }
                    }
                }
                if(!wlRookMoved) {
                    if(board[boardIndex[59]] == ' ' && board[boardIndex[58]] == ' ' && board[boardIndex[57]] == ' ') {
                        if(!attacks.contains(boardIndex[59]) && !attacks.contains(boardIndex[58]) && !attacks.contains(boardIndex[57])) {
                            // Add white left castleMove
                            moves.add(genericMove(60, 58, 'K', ' '));
                        }
                    }
                }
            }
        } else{
            if(!bKingMoved) {
                if(!wrRookMoved) {
                    if(board[boardIndex[1]] == ' ' && board[boardIndex[2]] == ' ' && board[boardIndex[3]] == ' ') {
                        if(!attacks.contains(boardIndex[1]) && !attacks.contains(boardIndex[2]) && !attacks.contains(boardIndex[3])) {
                            // Add black right castleMove
                            moves.add(genericMove(4, 6, 'k',' '));
                        }
                    }
                }
                if(!wlRookMoved) {
                    if(board[boardIndex[5]] == ' ' && board[boardIndex[6]] == ' ' ) {
                        if(!attacks.contains(boardIndex[5]) && !attacks.contains(boardIndex[6])) {
                            // Add black left castleMove
                            moves.add(genericMove(4, 2, 'k', ' '));
                        }
                    }
                }
            }
        }
        //if the king has not been in check, and has not moved
        //if then theres a rook that has not moved
        //if theres not a piece in between
        //then add castling 
    }

    
    public Move genericMove(int startSquare, int targetSquare, char piece, char killPiece){
        return new Move(
                new String[] {posToString(startSquare), posToString(targetSquare)},
                new int[] {startSquare, targetSquare},
                piece, killPiece);
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

    public ArrayList<Integer> getAttacks() {
        return attacks;
    }

    public void setLastMove(Move move) {
        move = lastMove;
    }
    

}