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
    Move lastMoveExecuted;

    private boolean whiteIsWinner = false;
    private boolean blackIsWinner = false;



    /* Constructor of game */
    public Game(boolean isAIwhite, boolean wantAI, boolean test) {
        boardClass = new Board(test);
        moveGen = new MoveGen(boardClass.getBoardIndex(), boardClass.getBoard(), true);
        moves = moveGen.generateMoves();

        AI = new Algorithm(this);
        this.isAIwhite = isAIwhite;

        /* TOGGLES AI */
        if(wantAI) {
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
    public ArrayList<Integer> getOpponentAttackedSquares(char piece){
        if (piece == 'p' || piece == 'P') {
            return moveGen.getAttacks();
        }

        return new ArrayList<>();
    }

    public boolean isAIwhite(){
        return isAIwhite;
    }
    /* Just to avoid writing the same over and over again */
    public void switchTurns() {
        whitesTurn = !whitesTurn;
    }


    /* MOVE EXECUTION :
    * Executes a move on the chessboard by switching indexes,
    * switches whose turn it is & then generates new moves for that position */
    public void executeMove (Move move) {
        lastMoveExecuted = move;
//        opponentMoves.addAll(moves);
        if(boardClass.movePiece(move.getStartSquare(), move.getTargetSquare())) {
            if(move.getKillPiece() == 'k')
                whiteIsWinner = true;
            else if(move.getKillPiece() == 'K')
                blackIsWinner = true;
            checkForPromotions();
            switchTurns();
            moveGen.setLastMove(lastMoveExecuted);

            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
        }
    }

    public void executeMoveByIndex (int startSquare, int targetSquare) {
        /* ONLY USED BY GUI
            TRIGGERS AI TO MAKE A MOVE AFTERWARDS! */

        if(boardClass.movePieceWithConversion(startSquare, targetSquare)) {
            if(get8By8Board()[targetSquare] == 'k' )
                whiteIsWinner = true;
            else if(get8By8Board()[targetSquare] == 'K')
                blackIsWinner = true;
            checkForPromotions();
            switchTurns();
            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
            //AI will make next move
            StopWatch dinbish = new StopWatch();
            dinbish.start();
            Move nextAIMove = AI.runAlphaBeta();
            dinbish.stop();
            System.out.println("time: " + dinbish.getTime(TimeUnit.MILLISECONDS));
            executeMove(nextAIMove);
            GUI.updateBoard();
        }
    }

    private void executeCastling(Move move) {
        // For black right side castling
        if(move.getStartSquare() == boardClass.getBoardIndex()[4] && move.getTargetSquare() == boardClass.getBoardIndex()[2]) {
            
        }
        // For black left side castling
        if(move.getStartSquare() == boardClass.getBoardIndex()[4] && move.getTargetSquare() == boardClass.getBoardIndex()[6]) {

        }
        // For white right side castling
        if(move.getStartSquare() == boardClass.getBoardIndex()[60] && move.getTargetSquare() == boardClass.getBoardIndex()[62]) {

        }
        // For white left side castling
        if(move.getStartSquare() == boardClass.getBoardIndex()[60] && move.getTargetSquare() == boardClass.getBoardIndex()[58]) {

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
            if(boardClass.board[boardClass.getBoardIndex()[i]] == 'P') {
                boardClass.board[boardClass.getBoardIndex()[i]] = 'Q';
            }
        }
        for (int i = 56; i < 64; i++) {
            if(boardClass.board[boardClass.getBoardIndex()[i]] == 'p') {
                boardClass.board[boardClass.getBoardIndex()[i]] = 'q';
            }
        }
    }

    /* Get moves for a specific square on the chessboard (primarily used in GUI) */
    public ArrayList<Move> getSpecificMoves(int startSquare) {
        startSquare = boardClass.convertIndexToBoardIndex(startSquare);
        tempMoves = new ArrayList<>();
        for (Move m : moves) {
            if(m.getStartSquare() == startSquare)
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
    public int rewriteThis (int indexToConvert) {
        return boardClass.convertBoardIndexToIndex(indexToConvert);
    }

    public MoveGen returnMoveGen() {
        return moveGen;
    }

    public void setGUI(NewGUI gui) {
        this.GUI = gui;
    }

    public char[] getPieceList() {
        return new char[] {'K', 'Q', 'R', 'N', 'B', 'P', 'k', 'q', 'r', 'n', 'b', 'p'};
    }
}
