package Chess;

import Chess.Moves.MoveGen;

public class Game {
    public Game() {
        Board board = new Board(true);
        MoveGen moveGen = new MoveGen(board);
    }
}
