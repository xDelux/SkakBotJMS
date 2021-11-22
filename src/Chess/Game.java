package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;

import java.util.ArrayList;

public class Game {
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;
    Board boardClass;
    MoveGen moveGen;
    boolean whitesTurn = true;
   Move lastMoveExecuted;

    /* Constructor of game */
    public Game(boolean wantAlhpaBeta) {
        boardClass = new Board(true);
        moveGen = new MoveGen(boardClass.getBoardIndex(), boardClass.getBoard(), true);
        moves = moveGen.generateMoves();
//        for (Move m : moves)
//            System.out.println(m.moveToString());
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
        if(boardClass.movePieceWithConersion(startSquare, targetSquare)) {
            switchTurns();
            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
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

    public boolean whosTurn() {
        return whitesTurn;
    }

    /*TODO rewrite / make smarter.*/
    public int rewriteThis (int indexToConvert) {
        return boardClass.convertBoardIndexToIndex(indexToConvert);
    }

    public MoveGen returnMoveGen() {
        return moveGen;
    }
}
