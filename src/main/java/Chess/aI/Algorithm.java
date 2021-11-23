package Chess.aI;

import Chess.Game;
import Chess.Moves.Move;

import java.util.*;

import static java.lang.Double.max;
import static java.lang.Double.min;

/*class boardState {
    final char[] board;
    final boolean turn;
    public boardState(char[] board, boolean turn) {
        this.board = board;
        this.turn = turn;
    }
    public char[] getBoard() {
        return board;
    }
    public boolean getTurn() {
        return turn;
    }
}*/
record boardState(char[] board, boolean turn) {
    public char[] getBoard() {
        return board;
    }

    public boolean getTurn() {
        return turn;
    }
}

public class Algorithm {
    Stack<ArrayList<Move>> moveStack = new Stack<>();
    Stack<char[]> boardStack = new Stack<>();
    HashMap<String, Double> evaluatedStates =  new HashMap<>();
    Stack<boardState> stateStack = new Stack<>();
    boardState tempState;

    double eval, maxEval, minEval;
    Game chessGame;

    int pawnValue = 100;
    int knightValue = 300;
    int bishopValue = 320;
    int rookValue = 500;
    int queenValue = 1100;
    int kingValue = 999999;//;
    int[] pawnBlackHeat = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 5, 10, 15, 15, 10, 5, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50
    };
    int[] pawnWhiteHeat = pawnBlackHeat;

    int[] knightBlackHeat = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50
    };
    int[] knightWhiteHeat = reverseArrays(knightBlackHeat);

    int[] bishopBlackHeat = {
            -20, -10, -10, -10, -10, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 10, 10, 5, 0, -10,
            -10, 5, 5, 10, 10, 5, 5, -10,
            -10, 0, 10, 10, 10, 10, 0, -10,
            -10, 10, 10, 10, 10, 10, 10, -10,
            -10, 5, 0, 0, 0, 0, 5, -10,
            -20, -10, -10, -10, -10, -10, -10, -20
    };
    int[] bishopWhiteHeat = reverseArrays(bishopBlackHeat);

    int[] rookBlackHeat = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, 10, 10, 10, 10, 5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            0, 0, 0, 5, 5, 0, 0, 0};
    int[] rookWhiteHeat = reverseArrays(rookBlackHeat);

    int[] queenBlackHeat = {
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 5, 5, 5, 5, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 0, 0, 0, 0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20};
    int[] queenWhiteHeat = reverseArrays(queenBlackHeat);

    int[] kingBlackHeat = {
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
            20, 20, 0, 0, 0, 0, 20, 20,
            20, 30, 10, 0, 0, 10, 30, 20};
    int[] kingWhiteHeat = reverseArrays(kingBlackHeat);

    public Algorithm(Game chessGame) {
        this.chessGame = chessGame;
    }


    static int DEPTH;
    public void setDepth(int Depth) {
        DEPTH = Depth;
    }
    public Move runAlphaBeta() {
        /* Start value alpha & beta */
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        /* Values found by evaluating positions*/
        double tempValue;
        double bestValue = Double.NEGATIVE_INFINITY;

        /* Moves */
        ArrayList<Move> moves = chessGame.getAllMoves();

        Move bestMove = moves.get(0);
        System.out.println(keyGen());

        /*clear evaluated list*/
        evaluatedStates.clear();

        /* Running alphabeta on current positions moves */
        for (Move m : moves) {
            makeMove(m);
            tempValue = alphaBeta(DEPTH, alpha, beta, false);
            System.out.println("Move: " + m.moveToString() + " evaluated to: " + tempValue);
            if(tempValue > bestValue) {
                bestValue = tempValue;
                bestMove = m;
            }
            unmakeMove();
        }
        System.out.println("best move: " + bestMove.moveToString() + " bestValue: " + bestValue);
        return bestMove;
    }

    public double alphaBeta(int depth, double alpha, double beta, boolean maximizing) {
        //check if already calculated
        String stateKey = keyGen();
        Double preValue = evaluatedStates.get(stateKey);
        //System.out.println(stateKey);
        if(preValue != null){
            //System.out.println("evaluate MATCHED!!");
            return preValue;
        }
        else{
            //System.out.println("eveluate mismatch!");
        }

        if (depth == 0) {
            eval = evaluatePosition();
            evaluatedStates.put(stateKey, eval);
            return eval;
        }

        /* Check if game is over or something*/

        ArrayList<Move> moves = chessGame.getAllMoves();
        /*if(moves.isEmpty()) {
            if(chessGame.playerInCheck()) {
                return Double.NEGATIVE_INFINITY;
            }
            return 0;
        }*/
        if (maximizing) {
            for (Move move : moves) {
                makeMove(move);
                eval = alphaBeta(depth - 1, alpha, beta, false);
                unmakeMove();

                if(alpha < eval) {
                    alpha = eval;
                }

                // Prune
                if (beta <= alpha) {
                    break;
                }
            }
            return alpha;

        } else {
            for (Move move : moves) {
                makeMove(move);
                eval = alphaBeta(depth - 1, alpha, beta, true);
                unmakeMove();
                if(eval < beta){
                    beta = eval;
                }
                // Prune
                if (beta <= alpha) {
                    break;
                }
            }
            return beta;
        }
    }

    public void makeMove(Move move) {
        /*add board before execute*/
        tempState = new boardState(chessGame.getWorkloadBoard().clone(), chessGame.isWhitesTurn());
        stateStack.add(tempState);
        chessGame.executeMove(move);
//        moves = chessGame.getAllMoves();
    }

    public void unmakeMove() {
        tempState = stateStack.pop();
        chessGame.setBoardState(tempState.getBoard(), tempState.getTurn());
//        moves = chessGame.getAllMoves();
    }

    private void returnOriginalPosition() {
//        System.out.println(boardStack.firstElement());
        tempState = stateStack.firstElement();
        System.out.println("first element: " + "turn: " + tempState.getTurn() + " & board: " + Arrays.toString(tempState.getBoard()));
        chessGame.setBoardState(tempState.getBoard(), tempState.getTurn());
        int size = stateStack.size();
        for (int i = size; i > 0; i--) {
            tempState = stateStack.pop();
            logBoard(tempState);
            System.out.println("Depth: " + i + " - turn: " + tempState.getTurn() + " & board: " + Arrays.toString(tempState.getBoard()));
        }
    }

    private double evaluatePosition() {
        char[] board = chessGame.get8By8Board();
        double whiteEval = 0, blackEval = 0, curr = 0;

        //We look through the board and add the pieces plus the heap maps to evaluate the position
        for (int i = 0; i < board.length; i++) {
            {
                if (board[i] != ' ' || board[i] != '0') {
                    //For white eval
                    if (Character.isUpperCase(board[i])) {
                        switch (board[i]) {
                            case 'P' -> whiteEval += pawnValue + pawnWhiteHeat[i];
                            case 'N' -> whiteEval += knightValue + knightWhiteHeat[i];
                            case 'B' -> whiteEval += bishopValue + bishopWhiteHeat[i];
                            case 'R' -> whiteEval += rookValue + rookWhiteHeat[i];
                            case 'Q' -> whiteEval += queenValue + queenWhiteHeat[i];
                            case 'K' -> whiteEval += kingValue + kingWhiteHeat[i];
                        }
                        //For black eval
                    } else {
                        switch (board[i]) {
                            case 'p' -> blackEval += pawnValue + pawnBlackHeat[i];
                            case 'n' -> blackEval += knightValue + knightBlackHeat[i];
                            case 'b' -> blackEval += bishopValue + bishopBlackHeat[i];
                            case 'r' -> blackEval += rookValue + rookBlackHeat[i];
                            case 'q' -> blackEval += queenValue + queenBlackHeat[i];
                            case 'k' -> blackEval += kingValue + kingBlackHeat[i];
                        }
                    }
                }
            }
        }
        double evaluation = whiteEval - blackEval;
        int pointPerspective = (chessGame.isAIwhite()) ? 1 : -1;
//        int pointPerspective = (chessGame.isWhitesTurn()) ? 1 : -1;
        return evaluation * pointPerspective;
    }

    private String keyGen(){
        char[] board = chessGame.get8By8Board();
        StringBuilder keyBuilder = new StringBuilder();
        int spaceCounter = 0;
        boolean countingSpaces = false;
        //build board string
        for (int i = 0; i < board.length; i++) {
            if(board[i] != ' '){
                if(countingSpaces){
                    keyBuilder.append(spaceCounter);
                    keyBuilder.append(board[i]);
                    countingSpaces = false;
                    spaceCounter = 0;
                }
                else{
                    keyBuilder.append(board[i]);
                }
            }
            else{
                countingSpaces = true;
                spaceCounter++;
            }
        }
        //add whose turn
        if(chessGame.isWhitesTurn()){
            keyBuilder.append('w');
        }
        else{
            keyBuilder.append('b');
        }
        return keyBuilder.toString();
    }

    private int[] reverseArrays(int[] array) {
        int[] y = new int[64];
        int p = 64;
        for (int i = 0; i < 64; i++) {
            y[p - 1] = array[i];
            p = p - 1;
        }
        return y;
    }

    private int[] reverseArrays(int[] array, int num) {
        int[] y = new int[num];
        int p = num;
        for (int i = 0; i < num; i++) {
            y[p - 1] = array[i];
            p = p - 1;
        }
        return y;
    }

    public void logBoard(boardState bs) {
        char[] temp = chessGame.get8By8Board(bs.getBoard());
        System.out.print("\n|");
        for (int i = 0; i < 64; i++) {
            if (i != 0 && i % 8 == 0)
                System.out.print("\n|");

            System.out.print(temp[i] + "|");
//            System.out.print(temp[i] + "|" );
        }
        System.out.println();
    }

    public int getNumberOfPositions(int depth) {
        if (depth == 0)
            return 1;

        ArrayList<Move> moves = chessGame.getAllMoves();
        int numberOfPositions = 0;

        for (Move m : moves) {
            makeMove(m);
            numberOfPositions += getNumberOfPositions(-1);
            unmakeMove();
        }

        return numberOfPositions;
    }
}
