import java.util.Arrays;


import static java.lang.Double.max;
import static java.lang.Double.min;

public class Algorithm {
    double eval, maxEval, minEval;
    Game game = Game.getInstance();
    int pawnValue = 100; int knightValue = 300; int bishopValue = 310; int rookValue = 500; int queenValue = 900; int kingValue = 20000;
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
    int[] pawnWhiteHeat = reverseArray(pawnBlackHeat);

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
    int[] knightWhiteHeat = reverseArray(knightBlackHeat);

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
    int[] bishopWhiteHeat = reverseArray(bishopBlackHeat);

    int[] rookBlackHeat = {
     0,  0,  0,  0,  0,  0,  0,  0,
     5,  10, 10, 10, 10, 10, 10, 5,
    -5,  0,  0,  0,  0,  0,  0,  -5,
    -5,  0,  0,  0,  0,  0,  0,  -5,
    -5,  0,  0,  0,  0,  0,  0,  -5,
    -5,  0,  0,  0,  0,  0,  0,  -5,
    -5,  0,  0,  0,  0,  0,  0,  -5,
     0,  0,  0,  5,  5,  0,  0,   0};
    int[] rookWhiteHeat = reverseArray(rookBlackHeat);
    int[] queenBlackHeat = {
    -20, -10, -10, -5, -5, -10, -10, -20,
    -10, 0, 0, 0, 0, 0, 0, -10,
    -10, 0, 5, 5, 5, 5, 0, -10,
    -5, 0, 5, 5, 5, 5, 0, -5,
    0, 0, 5, 5, 5, 5, 0, -5,
    -10, 5, 5, 5, 5, 5, 0, -10,
    -10, 0, 5, 0, 0, 0, 0, -10,
    -20, -10, -10, -5, -5, -10, -10, -20};
    int [] queenWhiteHeat = reverseArray(queenBlackHeat);

    int[] kingBlackHeat = {-30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20};
    int[] kingWhiteHeat = reverseArray(kingBlackHeat);


    public double alphaBeta(byte[] move, int depth, double alpha, double beta, boolean maximizing) {
        if(depth == 0) {
            return eval;
        }
        if(maximizing) {
            maxEval = Double.NEGATIVE_INFINITY;
            //for each child of current position
            eval = alphaBeta(child,depth - 1, alpha, beta, false);
            maxEval = max(maxEval, eval);
            alpha = max(alpha, eval);
            if(beta <= alpha) {
                break;
            }
            return maxEval;
        } else {
            minEval = Double.POSITIVE_INFINITY;
            //for each child of current position
            eval = alphaBeta(child,depth - 1, alpha, beta, true);
            minEval = min(minEval, eval);
            beta = min(beta, eval);
            if(beta <= alpha) {
                break;
            }
            return minEval;
        }
    }

    private double evaluatePosition() {
        char[][] board = game.getBoard();
        double whiteEval = 0, blackEval = 0, curr = 0;
        //We look through the board and add the pieces plus the heap maps to evaluate the position
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] != ' ') {
                    //For white eval
                    if(Character.isUpperCase(board[i][j])) {
                        switch (board[i][j]) {
                            case 'P' :
                                whiteEval += pawnValue + pawnWhiteHeat[i];
                                break;
                            case 'N' :
                                whiteEval += knightValue + knightWhiteHeat[i];
                                break;
                            case 'B' :
                                whiteEval += bishopValue + bishopWhiteHeat[i];
                                break;
                            case 'R' :
                                whiteEval += rookValue + rookWhiteHeat[i];
                                break;
                            case 'Q' :
                                whiteEval += queenValue + queenWhiteHeat[i];
                                break;
                            case 'K' :
                                whiteEval += kingValue + kingWhiteHeat[i];
                                break;
                        }
                    //For black eval
                    } else {
                        switch (board[i][j]) {
                            case 'p' :
                                blackEval += pawnValue + pawnBlackHeat[i];
                                break;
                            case 'n' :
                                blackEval += knightValue + knightBlackHeat[i];
                                break;
                            case 'b' :
                                blackEval += bishopValue + bishopBlackHeat[i];
                                break;
                            case 'R' :
                                blackEval += rookValue + rookBlackHeat[i];
                                break;
                            case 'Q' :
                                blackEval += queenValue + queenBlackHeat[i];
                                break;
                            case 'K' :
                                blackEval += kingValue + kingBlackHeat[i];
                                break;
                        }
                    }
                }
            }
        }
        return whiteEval - blackEval;
    }

    private int[] reverseArray(int[] array) {
        for(int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }

}
