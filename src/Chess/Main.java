package Chess;

import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;

public class Main {
    public static void main(String[] args) {
//        Board board = new Board(true);
//        MoveGen moveGen = new MoveGen();
        Game chessGame = new Game(true);
//        NewGUI gui = new NewGUI(chessGame);

        Algorithm AI = new Algorithm(chessGame);
        AI.runAlphaBeta();
//        AI.alphaBeta(chessGame.getAllMoves(),3, minEval, maxEval, true);
    }
}
