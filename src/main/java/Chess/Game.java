package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    private static NewGUI GUI;
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;
    Board boardClass;
    MoveGen moveGen;
    boolean whitesTurn = true;
    Algorithm AI;
    private final boolean isAIwhite;
   Move lastMoveExecuted;

    /* Constructor of game */
    public Game(boolean isAIwhite) {
        boardClass = new Board(false);
        moveGen = new MoveGen(boardClass.getBoardIndex(), boardClass.getBoard(), true);
        moves = moveGen.generateMoves();
        AI = new Algorithm(this);
        this.isAIwhite = isAIwhite;
        if(isAIwhite){
            //if AI is white then run alphabeta and execute best move at start
            Move bestMove = AI.runAlphaBeta();
            this.executeMove(bestMove);
        }

        //then wait for input from gui for player move. Every playermove should then result in triggering AI move.
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
        if(boardClass.movePiece(move.getStartSquare(), move.getTargetSquare())) {
            switchTurns();
            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
        }
    }

    public void executeMoveByIndex (int startSquare, int targetSquare) {
        /* ONLY USED BY GUI
            TRIGGERS AI TO MAKE A MOVE AFTERWARDS! */

        if(boardClass.movePieceWithConversion(startSquare, targetSquare)) {
            switchTurns();
            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
            //AI will make next move
            Move nextAIMove = AI.runAlphaBeta();
            executeMove(nextAIMove);
            GUI.updateBoard();
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

    public void setBoardState(char[] board, boolean turn) {
        whitesTurn = turn;
        boardClass.setBoard(board);
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
}
