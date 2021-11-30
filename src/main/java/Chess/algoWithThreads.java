package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class algoWithThreads {

    private static volatile double bestValue = Double.NEGATIVE_INFINITY;
    private static volatile double tempValue = Double.NEGATIVE_INFINITY;
    private static volatile Move bestMove;
    Algorithm AI;
    Game game;
    Board board;

    public algoWithThreads (Game game) {
        this.game = game;
    }


    public Move runAlgoWithThreads(ArrayList<Move> moves, Double alpha, Double beta) {
        ExecutorService executer = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<List<Move>> splitListForThreads = Lists.partition(moves,5);
        try {
            for (int i = 0; i < 5; i++) {
                int moveList = i;
                executer.execute(new InstaceOfChess(game.boardClass, game, game.AI, game.moveGen, splitListForThreads.get(moveList)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return bestMove;
    }


    private static class InstaceOfChess implements Runnable {
        Game game;

        public InstaceOfChess (Board board, Game game, Algorithm AI, MoveGen mg, List<Move> moveList) {
            this.board = board;
            this.game = game;
            this.AI = AI;
            this.moveList = moveList;
        }

        @Override
        public void run() {
            for (Move m : moveList) {
                game.AI.makeMove();
                AI.makeMove(m);
                tempValue = AI.alphaBeta(,  false);
                System.out.println("Move: " + m.moveToString() + " evaluated to: " + tempValue);
                if(tempValue > bestValue) {
                    System.out.println("new bestmove");
                    bestValue = tempValue;
                    bestMove = m;
                }
                unmakeMove();
            }
        }
    }
}
