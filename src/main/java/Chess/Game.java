package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.time.StopWatch;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    private static NewGUI GUI;
    ArrayList<Move> opponentMoves;
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;
    Board boardClass;
    MoveGen moveGen;
    boolean whitesTurn = true, updateGUI = false;
    Algorithm AI;
    private final boolean isAIwhite;
    boolean WhiteRightBlackLeftCastling;
    Move lastMoveExecuted;

    private boolean whiteIsWinner;
    private boolean blackIsWinner;


    /* Constructor of game */
    public Game(boolean isAIwhite, boolean wantAI, boolean test) {
        whiteIsWinner = false;
        blackIsWinner = false;
        boardClass = new Board(test);
        moveGen = new MoveGen(boardClass.getBoardIndex(), boardClass.getBoard(), true);
        moves = moveGen.generateMoves();

        AI = new Algorithm(this);
        this.isAIwhite = isAIwhite;

        /* TOGGLES AI */
        if (wantAI) {
            /* SET DEPTH OF AI */
            AI.setDepth(4);
            //if AI is white then run alphabeta and execute best move at start
            if (isAIwhite) {

                Move bestMove = AI.runAlphaBeta();
                this.executeMove(bestMove);
            }
        }
        //then wait for input from gui for player move. Every playermove should then result in triggering AI move.
    }

    /*public ArrayList<Integer> getOpponentAttackedSquares(char piece){
        if (piece == 'p' || piece == 'P') {
            return moveGen.getOpponentAttackedSquares();
        }

        return new ArrayList<>();
    }*/
    public ArrayList<Integer> getOpponentAttackedSquares(char piece) {
        if (piece == 'p' || piece == 'P') {
            return moveGen.getAttacks();
        }

        return new ArrayList<>();
    }

    public boolean isAIwhite() {
        return isAIwhite;
    }

    /* Just to avoid writing the same over and over again */
    public void switchTurns() {
        whitesTurn = !whitesTurn;
    }


    /* MOVE EXECUTION :
     * Executes a move on the chessboard by switching indexes,
     * switches whose turn it is & then generates new moves for that position */
    public void executeMove(Move move) {
        int startSquare = move.getStartSquare();
        int targetSquare = move.getTargetSquare();
        char piece = move.getPiece();
        char killPiece = move.getKillPiece();

        movePieceOnChessboard(startSquare, targetSquare, isCastleMove(piece, startSquare, targetSquare));
        checkEnPassant(move);
        lastMoveExecuted = move;
        moveGen.setLastMove(lastMoveExecuted);

        updateEssentials();
    }

    /* ONLY USED BY GUI
     * TRIGGERS AI TO MAKE A MOVE AFTERWARDS! */
    public void executeMoveFromGui(int startSquare, int targetSquare) {
        startSquare = boardClass.getBoardIndex()[startSquare];
        targetSquare = boardClass.getBoardIndex()[targetSquare];
        char piece = boardClass.board[startSquare];
        char killPiece = boardClass.board[targetSquare];

        Move playerMove;
        /* IF CASTLING MOVE TRUE ELSE FALSE PARAMETER */
        if(isCastleMove(piece, startSquare, targetSquare)) {
            playerMove = moveGen.genericCastleMove(startSquare,targetSquare, piece, true);
        } else {
            playerMove = moveGen.genericMove(startSquare,targetSquare,piece, killPiece);
        }
        executeMove(playerMove);
//        movePieceOnChessboard(startSquare, targetSquare, isCastleMove(piece, startSquare, targetSquare));
//        updateEssentials();

        aiMakeMove();

        GUI.updateBoard();
    }

    public void aiMakeMove() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        /* Get move from AI */
        Move nextAIMove = AI.runAlphaBeta();
        stopwatch.stop();
        System.out.println("time: " + stopwatch.getTime(TimeUnit.MILLISECONDS));

        /* Execute the AI generated move */
        executeMove(nextAIMove);

    }

    /* Makes the move on the board class*/
    public void movePieceOnChessboard(int startSquare, int targetSquare, boolean castling) {
        char killPiece = boardClass.board[targetSquare];
        if (killPiece== 'k')
            whiteIsWinner = true;
        else if (killPiece == 'K')
            blackIsWinner = true;

        if(castling) {
            makeCastlingMove(startSquare, targetSquare, WhiteRightBlackLeftCastling);
        } else {
            boardClass.movePiece(startSquare,targetSquare);
        }
        /* if killed piece is king declare winner */

    }

    public void checkEnPassant(Move move) {
        if((move.getStartSquare() + 24 == move.getTargetSquare() || move.getStartSquare()-24 == move.getTargetSquare()) && (move.getPiece() == 'p' || move.getPiece() == 'P')) {
            moveGen.setEnPassant(move.getTargetSquare());
        }
        if( move.getPiece() == 'P' && move.getStartSquare()+11 ==move.getTargetSquare() && move.getPiece() == ' ') {
            boardClass.getBoard()[boardClass.getBoardIndex()[move.getTargetSquare()-12]] = ' ';
        }
        if(move.getPiece() == 'P' && (move.getStartSquare()+13 ==move.getTargetSquare() && move.getPiece() == ' ')) {
            boardClass.getBoard()[boardClass.getBoardIndex()[move.getTargetSquare()-12]] = ' ';
        }
        if( move.getPiece() == 'p' && move.getStartSquare()-11 ==move.getTargetSquare() && move.getPiece() == ' ') {
            boardClass.getBoard()[boardClass.getBoardIndex()[move.getTargetSquare()+12]] = ' ';
        }
        if(move.getPiece() == 'p' && (move.getStartSquare()-13 ==move.getTargetSquare() && move.getPiece() == ' ')) {
            boardClass.getBoard()[boardClass.getBoardIndex()[move.getTargetSquare()+12]] = ' ';
        }
    }

    public void updateEssentials() {
        checkForPromotions();
        switchTurns();
        moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
    }


    /* Checks if a move is a castle move && which side*/
    public boolean isCastleMove(char piece, int startSquare, int targetSquare) {
        if(piece == 'k' || piece == 'K'){
            if(startSquare + 2 == targetSquare){
                WhiteRightBlackLeftCastling = true;
                return true;
            }
            if(startSquare - 2 == targetSquare) {
                WhiteRightBlackLeftCastling = false;
                return true;
            }
        }
        return false;
    }

    /* Performs a castling move */
    private void makeCastlingMove(int startSquare, int targetSquare, boolean WhiteRightBlackLeft) {
        if (WhiteRightBlackLeft) {
            boardClass.movePiece(startSquare, targetSquare);
            boardClass.movePiece(targetSquare + 1, startSquare + 1);
        } else {
            boardClass.movePiece(startSquare, targetSquare);
            boardClass.movePiece(targetSquare - 2, startSquare - 1);
        }
    }

    public boolean isWhiteIsWinner() {
        return whiteIsWinner;
    }

    public boolean isBlackIsWinner() {
        return blackIsWinner;
    }

    public void checkForPromotions() {
        for (int i = 0; i < 8; i++) {
            if (boardClass.board[boardClass.getBoardIndex()[i]] == 'P') {
                boardClass.board[boardClass.getBoardIndex()[i]] = 'Q';
            }
        }
        for (int i = 56; i < 64; i++) {
            if (boardClass.board[boardClass.getBoardIndex()[i]] == 'p') {
                boardClass.board[boardClass.getBoardIndex()[i]] = 'q';
            }
        }
    }

    /* Get moves for a specific square on the chessboard (primarily used in GUI) */
    public ArrayList<Move> getSpecificMoves(int startSquare) {
        startSquare = boardClass.convertIndexToBoardIndex(startSquare);
        tempMoves = new ArrayList<>();
        for (Move m : moves) {
            if (m.getStartSquare() == startSquare)
                tempMoves.add(m);
        }
        return tempMoves;
    }

    /* return all moves found at the current chessboard state*/
    public ArrayList<Move> getAllMoves() {
        return moves;
    }

    ArrayList<Move> captureMoves = new ArrayList<>();

    public ArrayList<Move> getCaptureMoves() {
        for (Move m : moves) {
            if (m.getKillPiece() != ' ' && m.getKillPiece() != '0')
                captureMoves.add(m);
        }
        System.out.println(captureMoves.toString());
        return captureMoves;
    }


    public void printMove(Move move) {
        System.out.println(
                "EXECUTING ORDER 66 MOVE: " +
                        move.getPiece() +
                        " FROM SQUARE: " +
                        move.getStartSquare() +
                        " TO: " +
                        move.getTargetSquare());
    }

    public char[] get8By8Board() {
        return boardClass.get8by8AsChars();
    }

    public char[] get8By8Board(char[] custom) {
        return boardClass.get8by8AsChars(custom);
    }

    public char[] getWorkloadBoard() {
        return boardClass.getBoard();
    }

    public void setBoardState(char[] board, boolean turn, boolean isWhiteWinner, boolean isBlackWinner) {
        whitesTurn = turn;
        boardClass.setBoard(board);
        whiteIsWinner = isWhiteWinner;
        blackIsWinner = isBlackWinner;
    }

    public boolean isWhitesTurn() {
        return whitesTurn;
    }

    /*TODO rewrite / make smarter.*/
    public int rewriteThis(int indexToConvert) {
        return boardClass.convertBoardIndexToIndex(indexToConvert);
    }

    public MoveGen returnMoveGen() {
        return moveGen;
    }

    public void setGUI(NewGUI gui) {
        this.GUI = gui;
    }

    public char[] getPieceList() {
        return new char[]{'K', 'Q', 'R', 'N', 'B', 'P', 'k', 'q', 'r', 'n', 'b', 'p'};
    }
}
