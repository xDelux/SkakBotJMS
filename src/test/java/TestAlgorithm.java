import Chess.Board;
import Chess.Game;
import Chess.aI.Algorithm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestAlgorithm {
    Board board;
    Game chessGame;
    Algorithm algo;

    @Before
    public void setUp () {
        Board board = new Board(false);
        Game chessGame = new Game(false);
        Algorithm algo = new Algorithm(chessGame);
    }

    @After
    public void tearDown () {

    }

    @Test
    public void expectedPositionsAtDepth(){

    }
}
