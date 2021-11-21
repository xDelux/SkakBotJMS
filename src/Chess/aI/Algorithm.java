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

    Stack<boardState> boardState = new Stack<>();
    boardState tempState;

    double eval, maxEval, minEval;
    Move bestMove, currMove;
    Game chessGame;
    ArrayList<Move> moves;
    ArrayList<Move> tempMoves;
    int pawnValue = 100; int knightValue = 300; int bishopValue = 320; int rookValue = 500; int queenValue = 1100; int kingValue = 20000;
    int[] pawnBlackHeat = {
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    };
    int[] pawnWhiteHeat = pawnBlackHeat;

    int[] knightBlackHeat = {
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    };
    int[] knightWhiteHeat = reverseArrays(knightBlackHeat);

    int[] bishopBlackHeat = {
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20
    };
    int[] bishopWhiteHeat = reverseArrays(bishopBlackHeat);

    int[] rookBlackHeat = {
         0,  0,  0,  0,  0,  0,  0,  0,
         5,  10, 10, 10, 10, 10, 10, 5,
        -5,  0,  0,  0,  0,  0,  0,  -5,
        -5,  0,  0,  0,  0,  0,  0,  -5,
        -5,  0,  0,  0,  0,  0,  0,  -5,
        -5,  0,  0,  0,  0,  0,  0,  -5,
        -5,  0,  0,  0,  0,  0,  0,  -5,
         0,  0,  0,  5,  5,  0,  0,   0};
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
    int [] queenWhiteHeat = reverseArrays(queenBlackHeat);

    int[] kingBlackHeat = {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20};
    int[] kingWhiteHeat = reverseArrays(kingBlackHeat);

    public Algorithm (Game chessGame) {
        this.chessGame = chessGame;

        int[] testarray = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 42, 53, 10};
        System.out.println(Arrays.toString(reverseArrays(testarray,12)));
    }


    public void runAlphaBeta(boolean run) {
        maxEval = Double.NEGATIVE_INFINITY;
        minEval = Double.POSITIVE_INFINITY;
//        boardStack.add(chessGame.getWorkloadBoard());
        moves = chessGame.getAllMoves();
        if(run) {
            System.out.println(alphaBeta(3, maxEval, minEval, true));
            returnOriginalPosition();
            System.out.println();
        }
    }

    public double alphaBeta(int depth, double alpha, double beta, boolean maximizing) {
        if(depth == 0) {
            return evaluatePosition();
        }
        if(maximizing) {
            maxEval = Double.NEGATIVE_INFINITY;
            for (Move move : moves) {
                makeMove(move);
                eval = alphaBeta(depth - 1, alpha, beta, false);
                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                // Prune
                if(beta <= alpha) {
                    unmakeMove();
//                    stackOfPositions.pop();
                    break;
                }
                    return maxEval;
                }

        } else {
            minEval = Double.POSITIVE_INFINITY;
            for (Move move: moves) {
                makeMove(move);
                eval = alphaBeta(depth - 1, alpha, beta, true);
                minEval = min(minEval, eval);
                beta = min(beta, eval);
                // Prune
                if (beta <= alpha) {
                    unmakeMove();
                    break;
                }
                return minEval;
            }
        }
        /* ??? */
        return 0;
    }

    private void makeMove(Move move) {
        /*add board before execute*/
//        boardStack.add(chessGame.getWorkloadBoard());

        tempState = new boardState(chessGame.getWorkloadBoard(), chessGame.whosTurn());
        boardState.add(tempState);
        chessGame.executeMove(move);
        moves = chessGame.getAllMoves();
    }

    private void unmakeMove() {
        tempState = boardState.pop();
        chessGame.setBoardState(tempState.getBoard(), tempState.getTurn());
        moves = chessGame.getAllMoves();
    }

    private void returnOriginalPosition() {
//        System.out.println(boardStack.firstElement());
        tempState = boardState.firstElement();
        System.out.println("first element: " + Arrays.toString(tempState.getBoard()) + " " + tempState.getTurn());
        chessGame.setBoardState(tempState.getBoard(), tempState.getTurn());
        int size = boardState.size();
        for (int i = 0; i < size; i++) {
            tempState = boardState.pop();
            System.out.println("turn: " + tempState.getTurn() + "board: " + Arrays.toString(tempState.getBoard()));
        }
    }

    private double evaluatePosition() {
        char[] board = chessGame.get8By8Board();
        double whiteEval = 0, blackEval = 0, curr = 0;
        //We look through the board and add the pieces plus the heap maps to evaluate the position
        for (int i = 0; i < board.length; i++) {
            {
                if(board[i] != ' ' || board[i] != '0') {
                    //For white eval
                    if(Character.isUpperCase(board[i])) {
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
        return whiteEval - blackEval;
    }

    private int[] reverseArrays(int[] array) {
        int[] y = new int[64];
        int p = 64;
        for(int i = 0; i < 64; i++)
        {
            y[p - 1] = array[i];
            p = p - 1;
        }
        return y;
    }

    private int[] reverseArrays(int[] array, int num) {
        int[] y = new int[num];
        int p = num;
        for(int i = 0; i < num; i++)
        {
            y[p - 1] = array[i];
            p = p - 1;
        }
        return y;
    }
}
