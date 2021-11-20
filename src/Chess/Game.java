package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;

import java.util.ArrayList;

public class Game {
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;
    Board boardClass;
    MoveGen moveGen;
    boolean whitesTurn;

    /* Constructor of game */
    public Game() {
        boardClass = new Board(true);
        moveGen = new MoveGen(boardClass.getBoardIndex(), boardClass.getBoard(), true);
        moves = moveGen.generateMoves();
    }

    /* Just to avoid writing the same over and over again */
    public void switchTurns() {
        whitesTurn = !whitesTurn;
    }

    /* MOVE EXECUTION */
    public void executeMove (Move move) {
        printMove(move);
        if(boardClass.movePiece(move.getTargetSquare(), move.getStartSquare())) {
            switchTurns();
            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
//            moveGen.updateAndGenerate(boardClass.getBoard());
        }
    }

    public void executeMoveByIndex (int startSquare, int targetSquare) {
        if(boardClass.movePiece(startSquare, targetSquare)) {
            switchTurns();
            moves = moveGen.updateAndGenerateMoves(boardClass.getBoard(), whitesTurn);
//            moveGen.updateAndGenerate(boardClass.getBoard());
        }
    }

    /* Get moves for a specific square on the chessboard */
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

    public int rewriteThis (int indexToConvert) {
        return boardClass.convertBoardIndexToIndex(indexToConvert);
    }

    public MoveGen returnMoveGen() {
        return moveGen;
    }

}
