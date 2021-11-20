package Chess;

import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;

public class Main {
    public static void main(String[] args) {
//        Board board = new Board(true);
//        MoveGen moveGen = new MoveGen();
        Game chessGame = new Game();
        double maxEval = Double.NEGATIVE_INFINITY;
        double minEval = Double.POSITIVE_INFINITY;
//        Algorithm AI = new Algorithm(moveGen, chessGame);
//        AI.alphaBeta(chessGame.getAllMoves(),3, minEval, maxEval, true);
        NewGUI gui = new NewGUI(chessGame);
    }
}
