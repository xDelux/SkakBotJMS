package Chess.aI;

import Chess.Game;
import Chess.Moves.Move;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import static java.lang.Math.max;

/* Used to determine board state to undo move */
record boardState(char[] board, boolean turn, boolean isWhiteWinner, boolean isBlackWinner) {
    public char[] getBoard() {
        return board;
    }
    public boolean getTurn() {
        return turn;
    }
    public boolean isWhiteWinner() {
        return isWhiteWinner;
    }
    public boolean isBlackWinner() {
        return isBlackWinner;
    }
}

public class Algorithm {

    HashMap<String, Double> evaluatedStates =  new HashMap<>();


    /* Stack for keeping board position to undo moves */
    boardState tempState;
    Stack<boardState> stateStack = new Stack<>();

    double eval, maxEval, minEval;
    boolean turn = false;
    Game chessGame;

    /* Pawn Piece-Square Tables (Heatmaps) */
    ArrayList<Integer> pawnBlackHeat;
    ArrayList<Integer> pawnWhiteHeat;
    /* Knight Piece-Square Tables (Heatmaps) */
    ArrayList<Integer> knightBlackHeat;
    ArrayList<Integer> knightWhiteHeat;
    /* Bishop Piece-Square Tables (Heatmaps) */
    ArrayList<Integer> bishopBlackHeat;
    ArrayList<Integer> bishopWhiteHeat;
    /* Rook Piece-Square Tables (Heatmaps) */
    ArrayList<Integer> rookWhiteHeat;
    ArrayList<Integer> rookBlackHeat;
    /* Queen Piece-Square Tables (Heatmaps) */
    ArrayList<Integer> queenBlackHeat;
    ArrayList<Integer> queenWhiteHeat;
    /* King Piece-Square Tables (Heatmaps) */
    ArrayList<Integer> kingBlackHeat;
    ArrayList<Integer> kingEndGameBlackHeat;
    ArrayList<Integer> kingWhiteHeat;
    ArrayList<Integer> kingEndGameWhiteHeat;

    ArrayList<ArrayList<Integer>> whiteHeats = new ArrayList<>();
    ArrayList<ArrayList<Integer>> blackHeats = new ArrayList<>();
    
    /* pre-calulated heatmaps */
    Map<Character, ArrayList<Integer>> calculatedPosition = Collections.synchronizedMap(new HashMap<>(12));
    private void setCalculatedPosition () {
        char[] piecelist = chessGame.getPieceList();

        ArrayList<Integer> calculatedPosValues;

        int counter = 0;
        ArrayList<ArrayList<Integer>> colorHeats;

        for (char c : piecelist) {
            calculatedPosValues = new ArrayList<>(64);
            int valueOfc = pieceValues.get(c);
            int startIndex;
            int endIndex;

            if(counter < whiteHeats.size()) {
                colorHeats = whiteHeats;
            } else {
                colorHeats = blackHeats;
                counter = 0;
            }
            for (int i = 0; i < 64; i++) {
                calculatedPosValues.add(valueOfc + colorHeats.get(counter).get(i));
            }
            calculatedPosition.put(c, calculatedPosValues);
//            System.out.println(calculatedPosValues);
            counter++;
        }
//        System.out.println(calculatedPosition);
    }

    /* Sets up all piece-square tables (heatmaps) */
    private void setUpPieceSquareTables() {

        pawnWhiteHeat = new ArrayList<>(Arrays.asList(
                0, 0, 0, 0, 0, 0, 0, 0,
                7, 7, 13, 23, 26, 13, 7, 7,
                -2, -2, 4, 12, 15, 4, -2, -2,
                -3, -3, 2, 9, 11, 2, -3, -3,
                -4, -4, 0, 6, 8, 0, -4, -4,
                -4, -5, -10, 0, 0, -10, -5, 5,
                -4, -4, 0, 4, 6, 0, -4, -4,
                0, 0, 0, 0, 0, 0, 0, 0
        ));
//        System.out.println("arraylist : " + pawnWhiteHeat);
        pawnBlackHeat = pawnWhiteHeat;
        Collections.reverse(pawnBlackHeat);
//        System.out.println("reversed : " + pawnWhiteHeat);

        bishopWhiteHeat = new ArrayList<>(Arrays.asList(
                2, 3, 4, 4, 4, 4, 3, 2,
                4, 7, 7, 7, 7, 7, 7, 4,
                3, 5, 6, 6, 6, 6, 5, 3,
                3, 5, 7, 7, 7, 7, 5, 3,
                3, 5, 6, 8, 8, 6, 5, 4,
                4, 5, 5,-2,-2, 5, 5, 4,
                5, 5, 5, 3, 3, 5, 5, 5,
                0, 0, 0, 0, 0, 0, 0, 0
        ));
        bishopBlackHeat = bishopWhiteHeat;
        Collections.reverse(bishopBlackHeat);

         knightWhiteHeat = new ArrayList<>(Arrays.asList(
                 -2, 2, 7, 9, 9, 7, 2, -2,
                 1, 4, 12, 13, 13, 12, 4, 1,
                 5, 11, 18, 19, 19, 18, 11, 5,
                 3, 10, 14, 14, 14, 14, 10, 3,
                 0, 5, 8, 9, 9, 8, 5, 0,
                 -3, 1, 3, 4, 4, 3, 1, -3,
                 -5, -3, -1, 0, 0, -1, -3, -5,
                 -7, -5, -4, -2, -2, -4, -5, -7
        ));
        knightBlackHeat = knightWhiteHeat;
        Collections.reverse(knightBlackHeat);


        rookWhiteHeat = new ArrayList<>(Arrays.asList(
                9, 9, 11, 10, 11, 9, 9, 9,
                4, 6, 7, 9, 9, 7, 6, 4,
                9, 10, 10, 11, 11, 10, 10, 9,
                8, 8, 8, 9, 9, 8, 8, 8,
                6, 6, 5, 6, 6, 5, 6, 6,
                4, 5, 5, 5, 5, 5, 5, 4,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        ));
        rookBlackHeat = rookWhiteHeat;
        Collections.reverse(rookBlackHeat);


        queenWhiteHeat = new ArrayList<>(Arrays.asList(
                2, 3, 4, 3, 4, 3, 3, 2,
                2, 3, 4, 4, 4, 4, 3, 2,
                3, 4, 4, 4, 4, 4, 4, 3,
                3, 3, 4, 4, 4, 4, 3, 3,
                2, 3, 3, 4, 4, 3, 3, 2,
                2, 2, 2, 3, 3, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2,
                0, 0, 0, 0, 0, 0, 0, 0
        ));
        queenBlackHeat = queenWhiteHeat;
        Collections.reverse(queenBlackHeat);

        kingWhiteHeat = new ArrayList<>(Arrays.asList(
                -30,-40,-40,-50,-50,-40,-40,-30,
                -30,-40,-40,-50,-50,-40,-40,-30,
                -30,-40,-40,-50,-50,-40,-40,-30,
                -30,-40,-40,-50,-50,-40,-40,-30,
                -20,-30,-30,-40,-40,-30,-30,-20,
                -10,-20,-20,-20,-20,-20,-20,-10,
                -10,-10,-10,-10,-10,-10,-10,-10,
                 5, 10, 5,  0,  0, 5, 10, 5
        ));
        kingBlackHeat = kingWhiteHeat;
        Collections.reverse(kingBlackHeat);

        kingEndGameWhiteHeat = new ArrayList<>(Arrays.asList(
                -50,-40,-30,-20,-20,-30,-40,-50,
                -30,-20,-10,  0,  0,-10,-20,-30,
                -30,-10, 20, 30, 30, 20,-10,-30,
                -30,-10, 30, 40, 40, 30,-10,-30,
                -30,-10, 30, 40, 40, 30,-10,-30,
                -30,-10, 20, 30, 30, 20,-10,-30,
                -30,-30,  0,  0,  0,  0,-30,-30,
                -50,-30,-30,-30,-30,-30,-30,-50
        ));
        kingEndGameBlackHeat = kingEndGameWhiteHeat;
        Collections.reverse(kingEndGameBlackHeat);


        /*pawnBlackHeat = new int[] {
                -50, -40, -30, -30, -30, -30, -40, -50,
                -40, -20, 0, 0, 0, 0, -20, -40,
                -30, 0, 10, 15, 15, 10, 0, -30,
                -30, 5, 15, 20, 20, 15, 5, -30,
                -30, 0, 15, 20, 20, 15, 0, -30,
                -30, 5, 10, 15, 15, 10, 5, -30,
                -40, -20, 0, 5, 5, 0, -20, -40,
                -50, -40, -30, -30, -30, -30, -40, -50
        };
        System.out.println("int[] :     " + Arrays.toString(pawnBlackHeat));
        pawnWhiteHeat = pawnBlackHeat;

        knightBlackHeat = new int[] {
                -50, -40, -30, -30, -30, -30, -40, -50,
                -40, -20, 0, 0, 0, 0, -20, -40,
                -30, 0, 10, 15, 15, 10, 0, -30,
                -30, 5, 15, 20, 20, 15, 5, -30,
                -30, 0, 15, 20, 20, 15, 0, -30,
                -40, -20, 0, 5, 5, 0, -20, -40,
                -40, -20, 0, 5, 5, 0, -20, -40,
                -50, -40, -30, -30, -30, -30, -40, -50
        };
        knightWhiteHeat = reverseArrays(knightBlackHeat);

        bishopBlackHeat = new int[] {
                -20, -10, -10, -10, -10, -10, -10, -20,
                -10, 0, 0, 0, 0, 0, 0, -10,
                -10, 0, 5, 10, 10, 5, 0, -10,
                -10, 5, 5, 10, 10, 5, 5, -10,
                -10, 0, 10, 10, 10, 10, 0, -10,
                -10, 10, 10, 10, 10, 10, 10, -10,
                -10, 5, 0, 0, 0, 0, 5, -10,
                -20, -10, -10, -10, -10, -10, -10, -20
        };
        bishopWhiteHeat = reverseArrays(bishopBlackHeat);

        rookBlackHeat = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                5, 10, 10, 10, 10, 10, 10, 5,
                -5, 0, 0, 0, 0, 0, 0, -5,
                -5, 0, 0, 0, 0, 0, 0, -5,
                -5, 0, 0, 0, 0, 0, 0, -5,
                -5, 0, 0, 0, 0, 0, 0, -5,
                -5, 0, 0, 0, 0, 0, 0, -5,
                0, 0, 0, 5, 5, 0, 0, 0};
        rookWhiteHeat = reverseArrays(rookBlackHeat);

        queenBlackHeat = new int[] {
                -20, -10, -10, -5, -5, -10, -10, -20,
                -10, 0, 0, 0, 0, 0, 0, -10,
                -10, 0, 5, 5, 5, 5, 0, -10,
                -5, 0, 5, 5, 5, 5, 0, -5,
                0, 0, 5, 5, 5, 5, 0, -5,
                -10, 5, 5, 5, 5, 5, 0, -10,
                -10, 0, 5, 0, 0, 0, 0, -10,
                -20, -10, -10, -5, -5, -10, -10, -20};
        queenWhiteHeat = reverseArrays(queenBlackHeat);

        kingBlackHeat = new int[] {
                -30, -40, -40, -50, -50, -40, -40, -30,
                -30, -40, -40, -50, -50, -40, -40, -30,
                -30, -40, -40, -50, -50, -40, -40, -30,
                -30, -40, -40, -50, -50, -40, -40, -30,
                -20, -30, -30, -40, -40, -30, -30, -20,
                -10, -20, -20, -20, -20, -20, -20, -10,
                20, 20, 0, 0, 0, 0, 20, 20,
                20, 30, 10, 0, 0, 10, 30, 20};
        kingWhiteHeat = reverseArrays(kingBlackHeat);*/

        whiteHeats.add(kingWhiteHeat);
        whiteHeats.add(queenWhiteHeat);
        whiteHeats.add(rookWhiteHeat);
        whiteHeats.add(knightWhiteHeat);
        whiteHeats.add(bishopWhiteHeat);
        whiteHeats.add(pawnWhiteHeat);


        blackHeats.add(kingBlackHeat);
        blackHeats.add(queenBlackHeat);
        blackHeats.add(rookBlackHeat);
        blackHeats.add(knightBlackHeat);
        blackHeats.add(bishopBlackHeat);
        blackHeats.add(pawnBlackHeat);

//        }
    }

    /* Sets up all piece values into a hashmap for faster look up*/
    Map<Character, Integer> pieceValues = Collections.synchronizedMap(new HashMap<>());
    private void setUpPieceValues () {
        /* Piece values */
        int pawnValue = 100;
        int knightValue = 300;
        int bishopValue = 320;
        int rookValue = 500;
        int queenValue = 900;
        int kingValue = 90000;

        /* Mapping all pieces */
        pieceValues.put('K',kingValue);
        pieceValues.put('Q',queenValue);
        pieceValues.put('R',rookValue);
        pieceValues.put('N',knightValue);
        pieceValues.put('B',bishopValue);
        pieceValues.put('P',pawnValue);

        pieceValues.put('k',kingValue);
        pieceValues.put('q',queenValue);
        pieceValues.put('r',rookValue);
        pieceValues.put('n',knightValue);
        pieceValues.put('b',bishopValue);
        pieceValues.put('p',pawnValue);
    }

    /* Constructor */
    public Algorithm(Game chessGame) {
        this.chessGame = chessGame;

        setUpPieceValues();
        setUpPieceSquareTables();
        setCalculatedPosition();
    }


    /* Depth value and setter method */
    static int DEPTH;
    public void setDepth(int Depth) {
        DEPTH = Depth;
    }

    /* METHOD FOR RUNNING ALPHA BETA */
    public Move runAlphaBeta() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        /* Values found by evaluating positions*/
        double tempValue;
        double bestValue = Double.NEGATIVE_INFINITY;

        /* Moves */
        ArrayList<Move> moves = sortMoves(chessGame.getAllMoves());

        Move bestMove = moves.get(0);

        /* Start value alpha & beta */
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        /* Running alphabeta on current positions moves */
        for (Move m : moves) {
            makeMove(m);
            tempValue = alphaBeta(DEPTH, alpha, beta, false);
//            tempValue = negaMaxAlphaBeta(DEPTH, alpha, beta, (chessGame.isWhitesTurn()) ? 1 : -1);
            //tempValue = negaMaxAlphaBeta(DEPTH, alpha, beta);

            System.out.println("Move: " + m.moveToString() + " evaluated to: " + tempValue);
            if(tempValue > bestValue) {
                System.out.println("new bestmove");
                bestValue = tempValue;
                bestMove = m;
            }
            unmakeMove();
        }

//        System.out.println("TIMING ALPHA BETA : ");
        System.out.println("TIMING NEGAMAX : ");
        System.out.println("best move: " + bestMove.moveToString() + " bestValue: " + bestValue);
        stopWatch.stop();

        System.out.println("TIME : " + stopWatch.getTime(TimeUnit.MILLISECONDS));

        /*try {
            FileWriter myWriter = new FileWriter("C:\\Users\\2100m\\Documents\\Code Projects\\SkakbotAI\\src\\main\\java\\Chess\\aI\\performanceTest.txt", true);
            myWriter.write("TIME OF FIRST MOVE WITH HASHMAP: " + stopWatch.getTime(TimeUnit.MILLISECONDS) + " SECONDS\n");
            myWriter.flush();
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("PERFORMANCE : " + stopWatch.getTime(TimeUnit.MILLISECONDS));*/

        //System.out.println("matches: " + matchcount + " mismatches: " + mismatchcount); // dynamic programming
        return bestMove;
    }
    int matchcount = 0, mismatchcount = 0;

    public double alphaBeta(int depth, double alpha, double beta, boolean maximizing) {
        //check if already calculated
       /* String stateKey = keyGen(depth);
        Double preValue = evaluatedStates.get(stateKey);
        //System.out.println(stateKey);
        if(preValue != null){
            //System.out.println("evaluate MATCHED!!");
            return preValue;
        }
        else{
            //System.out.println("eveluate mismatch!");
        }*/

        //check if game is won/lost in given position
        if(chessGame.isBlackIsWinner()){
            if(chessGame.isAIwhite())
                return -Double.MAX_VALUE;
            else
                return Double.MAX_VALUE;
        }
        else if(chessGame.isWhiteIsWinner()){
            if(chessGame.isAIwhite())
                return Double.MAX_VALUE;
            else
                return -Double.MAX_VALUE;
        }

        if (depth == 0) {
            eval = evaluatePosition();
            //evaluatedStates.put(stateKey, eval);
            return eval;
        }

        /* Check if game is over or something*/
        ArrayList<Move> moves = sortMoves(chessGame.getAllMoves());
//        ArrayList<Move> moves = chessGame.getAllMoves();
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

    public double negaMaxAlphaBeta(int depth, double alpha, double beta) {
        if (depth == 0) {
            eval = evaluatePosition();
            //evaluatedStates.put(stateKey, eval); // dynamic programming
            return eval;
        }

        ArrayList<Move> moves = sortMoves(chessGame.getAllMoves());

        maxEval = Double.NEGATIVE_INFINITY;
        for (Move m : moves) {
            makeMove(m);
//             Our negated beta is opponents alpha (Negate so both sides are trying to maximize
            eval = -negaMaxAlphaBeta(depth-1, -beta, -alpha);
            unmakeMove();

            if(eval >= beta)
                return beta;



            alpha = max(alpha, eval);

        }
        return alpha;
    }

    /* Quiesent search is called when a leaf node is hit on the alpha beta algorithm.
     * THe main purpose of this is to look through captures / take bakes & so on.
     *
     * "Essentially, a quiescent search is an evaluation function that takes into account some dynamic possibilities."
     * https://web.archive.org/web/20071027170528/http://www.brucemo.com/compchess/programming/quiescent.htm */
    private double quiescentSearch(double alpha, double beta) {
        double evaluation = evaluatePosition();
        if (evaluation >= beta)
            return beta;
        alpha = max(alpha,evaluation);

        ArrayList<Move> captureMoves = sortMoves(chessGame.getCaptureMoves());

        for (Move m : captureMoves) {
            makeMove(m);
            evaluation = -quiescentSearch(-beta, -alpha);
            unmakeMove();
            if(evaluation >= beta)
                return beta;
            alpha = max(evaluation, alpha);
        }
        return alpha;
    }

    /* Saves the current state of the board for later undoing, then makes the move on the board */
    public void makeMove(Move move) {
        /*add board before execute*/
        tempState = new boardState(chessGame.getWorkloadBoard().clone(), chessGame.isWhitesTurn(), chessGame.isWhiteIsWinner(),chessGame.isBlackIsWinner());
        stateStack.add(tempState);
        chessGame.executeMove(move);
//        moves = chessGame.getAllMoves();
    }

    /* Unmakes a move by popping the stack and then sets the board */
    public void unmakeMove() {
        tempState = stateStack.pop();
        chessGame.setBoardState(tempState.getBoard(), tempState.getTurn(), tempState.isWhiteWinner(), tempState.isBlackWinner());
//        moves = chessGame.getAllMoves();
    }

    private double evaluatePosition() {
        char[] board = chessGame.get8By8Board();
        double whiteEval = 0, blackEval = 0, curr = 0;
        char piece;

        //We look through the board and add the pieces plus the heap maps to evaluate the position
        for (int i = 0; i < board.length; i++) {
                if (board[i] != ' ') {
                    piece = board[i];
                     /*White eval */
                    /*if(Character.isUpperCase(board[i]))
                        whiteEval += calculatedPosition.get(board[i]).get(i);
                    else
                        blackEval += calculatedPosition.get(board[i]).get(i);*/
                    if (Character.isUpperCase(board[i])) {
                        switch (board[i]) {
                            case 'P' -> whiteEval += pieceValues.get(board[i]) + pawnWhiteHeat.get(i);
                            case 'N' -> whiteEval += pieceValues.get(board[i]) + knightWhiteHeat.get(i);
                            case 'B' -> whiteEval += pieceValues.get(board[i]) + bishopWhiteHeat.get(i);
                            case 'R' -> whiteEval += pieceValues.get(board[i]) + rookWhiteHeat.get(i);
                            case 'Q' -> whiteEval += pieceValues.get(board[i]) + queenWhiteHeat.get(i);
                            case 'K' -> whiteEval += pieceValues.get(board[i]) + kingWhiteHeat.get(i);
                        }
                        //For black eval
                    } else {
                        switch (board[i]) {
                            case 'p' -> blackEval += pieceValues.get(board[i])+ pawnBlackHeat.get(i);
                            case 'n' -> blackEval += pieceValues.get(board[i])+ knightBlackHeat.get(i);
                            case 'b' -> blackEval += pieceValues.get(board[i]) + bishopBlackHeat.get(i);
                            case 'r' -> blackEval += pieceValues.get(board[i])+ rookBlackHeat.get(i);
                            case 'q' -> blackEval += pieceValues.get(board[i]) + queenBlackHeat.get(i);
                            case 'k' -> blackEval += pieceValues.get(board[i])+ kingBlackHeat.get(i);
                        }
                    }
                }
            }

        double evaluation = whiteEval - blackEval;
        int pointPerspective = (chessGame.isAIwhite()) ? 1 : -1;

        return evaluation * pointPerspective;
    }

    private ArrayList<Move> sortMoves(ArrayList<Move> movesToSort) {
        char movingPiece;
        char targetPiece;

        for (Move m : movesToSort) {
            movingPiece = m.getPiece();
            targetPiece = m.getKillPiece();

            if(targetPiece != ' ' && targetPiece != '0')
                m.setMoveScoreGuess(10 * pieceValues.get(targetPiece) - pieceValues.get(movingPiece));

            /* TODO (IF PROMOTION) */

            if (chessGame.getOpponentAttackedSquares('p').contains(m.getTargetSquare()))
                m.setMoveScoreGuess(-pieceValues.get(movingPiece));

        }

        movesToSort.sort(Collections.reverseOrder(Comparator.comparing(Move::getMoveScoreGuess)));

        return movesToSort;

    }


    private String keyGen(int currentDepth){
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
        keyBuilder.append(currentDepth);
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
            numberOfPositions += getNumberOfPositions(depth-1);
            unmakeMove();
        }

        return numberOfPositions;
    }
}
