package Chess;

import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(true);
        MoveGen moveGen = new MoveGen(board);
        Algorithm AI = new Algorithm(moveGen);
        NewGUI gui = new NewGUI(moveGen);
    }
}
