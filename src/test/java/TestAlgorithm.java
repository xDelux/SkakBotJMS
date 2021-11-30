import Chess.Board;
import Chess.Game;
import Chess.Moves.Move;
import Chess.aI.Algorithm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class TestAlgorithm {
    Board board = new Board(false);
    Game chessGame = new Game(false, false, false);
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
        for (int i = 1; i <= 5; i++) {
            stopWatch.start();
            int numb = algo.getShannonNumbers(i);
            stopWatch.stop();
            System.out.println("NUMBER OF POSITIONS AT DEPTH " + i + ": " + numb + " | EXECUTION TIME: " + stopWatch.getTime(TimeUnit.SECONDS) + " SECONDS");
            stopWatch.reset();
        }
    }

    @Test
    public void testLeafs() {
        /* Values found by evaluating positions*/
        double tempValue;
        double bestValue = Double.NEGATIVE_INFINITY;

        /* Moves */
        ArrayList<Move> moves = algo.sortMoves(chessGame.getAllMoves());

        Move bestMove = moves.get(0);

        /* Start value alpha & beta */
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        /* Running alphabeta on current positions moves */
        int counter = 0;
        StopWatch stopWatch = new StopWatch();
        for (int i = 1; i <= 4; i++) {

            for (int j = 0; j < 2; j++) {
                if(j == 0) {
                    stopWatch.start();
                    algo.resetCounter();
                    for (Move m : moves) {
                        algo.makeMove(m);
                        tempValue = algo.alphaBeta(i, alpha, beta, false);
                        if (tempValue > bestValue) {
                            bestValue = tempValue;
                            bestMove = m;
                        }
                        algo.unmakeMove();
                    }
                    stopWatch.stop();
                    System.out.println("NUMBER OF LEAFS AT DEPTH WITH ALPHA BETA: " + i + " : " + algo.getCounter() + " | EXECUTION TIME: " + stopWatch.getTime(TimeUnit.SECONDS) + " SECONDS");
                    stopWatch.reset();
                } else {
                    stopWatch.start();
                    algo.resetCounter();
                    for (Move m : moves) {
                        algo.makeMove(m);
                        tempValue = algo.minMax(i, false);

                        if (tempValue > bestValue) {
                            bestValue = tempValue;
                            bestMove = m;
                        }
                        algo.unmakeMove();
                    }
                    stopWatch.stop();
                    System.out.println("NUMBER OF LEAFS AT DEPTH WITH MINMAX: " + i + " : " + algo.getCounter() + " | EXECUTION TIME: " + stopWatch.getTime(TimeUnit.SECONDS) + " SECONDS");
                    stopWatch.reset();
                }
            }
        }
    }
}
