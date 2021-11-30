import Chess.Board;
import Chess.Game;
import Chess.Moves.Move;
import Chess.aI.Algorithm;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.apache.commons.lang3.time.StopWatch;
import com.google.common.base.Stopwatch;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;


public class TestAlgorithm {
    Board board = new Board(false);
    Game chessGame = new Game(false);
    Algorithm algo = new Algorithm(chessGame);

    @Before
    public void setUp() {
        System.out.println(chessGame.get8By8Board());
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testingNumberOfPositions() {
        StopWatch stopWatch = new StopWatch();
        for (int i = 1; i <= 4; i++) {
            stopWatch.start();
            int numb = algo.getNumberOfPositions(i);
            stopWatch.stop();
            System.out.println("NUMBER OF POSITIONS AT DEPTH " + i + ": " + numb + " | EXECUTION TIME: " + stopWatch.getTime(TimeUnit.SECONDS) + " SECONDS");
            stopWatch.reset();
        }
    }
}
