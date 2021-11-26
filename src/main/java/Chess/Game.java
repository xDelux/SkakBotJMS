package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class Game {
    private static NewGUI GUI;
    ArrayList<Move> opponentMoves;
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;
    Board boardClass;
    MoveGen moveGen;
    boolean whitesTurn = true;
    Algorithm AI;
    private final boolean isAIwhite;
   Move lastMoveExecuted;



    /* Constructor of game */
    public Game(boolean isAIwhite, boolean wantAI) {
        boardClass = new Board(false);
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

    public ArrayList<Integer> getOpponentAttackedSquares(char piece){
        if (piece == 'p' || piece == 'P') {
            return moveGen.getOpponentAttackedSquares();
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

    public char[] getPieceList() {
        return new char[] {'K', 'Q', 'R', 'N', 'B', 'P', 'k', 'q', 'r', 'n', 'b', 'p'};
    }
}
