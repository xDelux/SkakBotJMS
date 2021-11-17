import static java.lang.Double.max;
import static java.lang.Double.min;

public class Algorithm {
    double pieceValue, heapMapValue, eval, maxEval, minEval;


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

}
