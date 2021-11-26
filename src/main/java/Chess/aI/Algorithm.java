package Chess.aI;

import Chess.Game;
import Chess.Moves.Move;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import static java.lang.Math.max;

/* Used to determine board state to undo move */
record boardState(char[] board, boolean turn) {
    public char[] getBoard() {
        return board;
    }

    public boolean getTurn() {
        return turn;
    }
}

public class Algorithm {

    HashMap<String, Double> evaluatedStates =  new HashMap<>();


    /* Stack for keeping board position to undo moves */
    boardState tempState;
    Stack<boardState> stateStack = new Stack<>();


    double eval, maxEval, minEval;
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
            if(counter < 6) {
                colorHeats = whiteHeats;
            } else {
                colorHeats = blackHeats;
                counter = 0;
            }

            int valueOfc = pieceValues.get(c);

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
                0,  0,  0,  0,  0,  0,  0,  0,
                50, 50, 50, 50, 50, 50, 50, 50,
                10, 10, 20, 30, 30, 20, 10, 10,
                5,  5, 10, 25, 25, 10,  5,  5,
                0,  0,  0, 20, 20,  0,  0,  0,
                5, -5,-10,  0,  0,-10, -5,  5,
                5, 10, 10,-20,-20, 10, 10,  5,
                0,  0,  0,  0,  0,  0,  0,  0
        ));
//        System.out.println("arraylist : " + pawnWhiteHeat);
        pawnBlackHeat = pawnWhiteHeat;
        Collections.reverse(pawnBlackHeat);
//        System.out.println("reversed : " + pawnWhiteHeat);

        bishopWhiteHeat = new ArrayList<>(Arrays.asList(
                -20,-10,-10,-10,-10,-10,-10,-20,
                -10,  0,  0,  0,  0,  0,  0,-10,
                -10,  0,  5, 10, 10,  5,  0,-10,
                -10,  5,  5, 10, 10,  5,  5,-10,
                -10,  0, 10, 10, 10, 10,  0,-10,
                -10, 10, 10, 10, 10, 10, 10,-10,
                -10,  5,  0,  0,  0,  0,  5,-10,
                -20,-10,-10,-10,-10,-10,-10,-20
        ));
        bishopBlackHeat = bishopWhiteHeat;
        Collections.reverse(bishopBlackHeat);

         knightWhiteHeat = new ArrayList<>(Arrays.asList(
                 -50,-40,-30,-30,-30,-30,-40,-50,
                 -40,-20,  0,  0,  0,  0,-20,-40,
                 -30,  0, 10, 15, 15, 10,  0,-30,
                 -30,  5, 15, 20, 20, 15,  5,-30,
                 -30,  0, 15, 20, 20, 15,  0,-30,
                 -30,  5, 10, 15, 15, 10,  5,-30,
                 -40,-20,  0,  5,  5,  0,-20,-40,
                 -50,-40,-30,-30,-30,-30,-40,-50
        ));
        knightBlackHeat = knightWhiteHeat;
        Collections.reverse(knightBlackHeat);


        rookWhiteHeat = new ArrayList<>(Arrays.asList(
                0,  0,  0,  0,  0,  0,  0,  0,
                5, 10, 10, 10, 10, 10, 10,  5,
                -5,  0,  0,  0,  0,  0,  0, -5,
                -5,  0,  0,  0,  0,  0,  0, -5,
                -5,  0,  0,  0,  0,  0,  0, -5,
                -5,  0,  0,  0,  0,  0,  0, -5,
                -5,  0,  0,  0,  0,  0,  0, -5,
                0,  0,  0,  5,  5,  0,  0,  0
        ));
        rookBlackHeat = rookWhiteHeat;
        Collections.reverse(rookBlackHeat);


        queenWhiteHeat = new ArrayList<>(Arrays.asList(
                -20,-10,-10, -5, -5,-10,-10,-20,
                -10,  0,  0,  0,  0,  0,  0,-10,
                -10,  0,  5,  5,  5,  5,  0,-10,
                -5,  0,  5,  5,  5,  5,  0, -5,
                0,  0,  5,  5,  5,  5,  0, -5,
                -10,  5,  5,  5,  5,  5,  0,-10,
                -10,  0,  5,  0,  0,  0,  0,-10,
                -20,-10,-10, -5, -5,-10,-10,-20
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
                 20, 30, 10,  0,  0, 10, 30, 20
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
        /* Start value alpha & beta */
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        /* Values found by evaluating positions*/
        double tempValue;
        double bestValue = Double.NEGATIVE_INFINITY;

        /* Moves */
        ArrayList<Move> moves = sortMoves(chessGame.getAllMoves());


        Move bestMove = moves.get(0);
        System.out.println(keyGen());


        /*clear evaluated list (dynamic programming)*/
        /*evaluatedStates.clear();
        matchcount = 0;
        mismatchcount = 0;*/

        /* Running alphabeta on current positions moves */
        for (Move m : moves) {

            makeMove(m);
//            tempValue = alphaBeta(DEPTH, alpha, beta, chessGame.isAIwhite());
//            tempValue = negaMaxAlphaBeta(DEPTH, alpha, beta, (chessGame.isWhitesTurn()) ? 1 : -1);
            tempValue = negaMaxAlphaBeta(DEPTH, alpha, beta, (chessGame.isAIwhite()) ? 1 : -1);

//            System.out.println("Move: " + m.moveToString() + " evaluated to: " + tempValue);
            if(tempValue > bestValue) {
                bestValue = tempValue;
                bestMove = m;
            }
            unmakeMove();
        }
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
        /*//check if already calculated // dynamic programming
        String stateKey = keyGen(depth);
        Double preValue = evaluatedStates.get(stateKey);
        //System.out.println(stateKey);
        if(preValue != null){
            matchcount++;
            //System.out.println("evaluate MATCHED!!");
            return preValue;
        }
        else{
            mismatchcount++;
            //System.out.println("eveluate mismatch!");
        }*/

    /* TODO CONVERT TO NAGAMAX */
    /* MAYBE https://en.wikipedia.org/wiki/Principal_variation_search */
    /* ALPHA BETA ALGORITHM */
//    public double alphaBeta(int depth, double alpha, double beta, boolean maximizing) {
//        //check if already calculated
//        String stateKey = keyGen();
//        Double preValue = evaluatedStates.get(stateKey);
//        //System.out.println(stateKey);
//        if(preValue != null){
//            //System.out.println("evaluate MATCHED!!");
//            return preValue;
//        }
//        else{
//            //System.out.println("eveluate mismatch!");
//        }
//
//        if (depth == 0) {
//            eval = evaluatePosition();
//            evaluatedStates.put(stateKey, eval);
//            return eval;
//        }
//
//        /* Check if game is over or something*/
//        ArrayList<Move> moves = sortMoves(chessGame.getAllMoves());
////        ArrayList<Move> moves = chessGame.getAllMoves();
//        /*if(moves.isEmpty()) {
//            if(chessGame.playerInCheck()) {
//                return Double.NEGATIVE_INFINITY;
//            }
//            return 0;
//        }*/
//        if (maximizing) {
//            for (Move move : moves) {
//                makeMove(move);
//                eval = alphaBeta(depth - 1, alpha, beta, false);
//                unmakeMove();
//
//                if(alpha < eval) {
//                    alpha = eval;
//                }
//
//                // Prune
//                if (beta <= alpha) {
//                    break;
//                }
//            }
//            return alpha;
//
//        } else {
//            for (Move move : moves) {
//                makeMove(move);
//                eval = alphaBeta(depth - 1, alpha, beta, true);
//                unmakeMove();
//                if(eval < beta){
//                    beta = eval;
//                }
//                // Prune
//                if (beta <= alpha) {
//                    break;
//                }
//            }
//            return beta;
//        }
//    }

    public double negaMaxAlphaBeta(int depth, double alpha, double beta, int turnMultiplier) {
        if (depth == 0) {
            eval = evaluatePosition();
            //evaluatedStates.put(stateKey, eval); // dynamic programming
            return eval;
        }

//        StopWatch sw = new StopWatch();
//        sw.start();
        ArrayList<Move> moves = sortMoves(chessGame.getAllMoves());
//        sw.stop();
//        System.out.println("MOVES SORTED WITH TIME: " + sw.getTime(TimeUnit.MILLISECONDS));
//        ArrayList<Move> moves = chessGame.getAllMoves();

        double bestValue = Double.NEGATIVE_INFINITY;
        for (Move m : moves) {
            makeMove(m);
//             Our negated beta is opponents alpha (Negate so both sides are trying to maximize
            double value = -negaMaxAlphaBeta(depth-1, -beta, -alpha, -turnMultiplier);
            unmakeMove();
            if(value > bestValue) {
                bestValue = value;
            }
            alpha = max(alpha, bestValue);

            if(alpha >= beta)
                break;

        }
        return bestValue;
    }

    /* Saves the current state of the board for later undoing, then makes the move on the board */
    public void makeMove(Move move) {
        /*add board before execute*/
        tempState = new boardState(chessGame.getWorkloadBoard().clone(), chessGame.isWhitesTurn());
        stateStack.add(tempState);
        chessGame.executeMove(move);
//        moves = chessGame.getAllMoves();
    }

    /* Unmakes a move by popping the stack and then sets the board */
    public void unmakeMove() {
        tempState = stateStack.pop();
        chessGame.setBoardState(tempState.getBoard(), tempState.getTurn());
//        moves = chessGame.getAllMoves();
    }


    private double evaluatePosition() {
        char[] board = chessGame.get8By8Board();
        double whiteEval = 0, blackEval = 0, curr = 0;
        char piece;

        //We look through the board and add the pieces plus the heap maps to evaluate the position
        for (int i = 0; i < board.length; i++) {
                if (board[i] != ' ' && board[i] != '0') {
                    piece = board[i];
                     /*White eval */
                    if(Character.isUpperCase(board[i]))
                        whiteEval += calculatedPosition.get(board[i]).get(i);
                    else
                        blackEval += calculatedPosition.get(board[i]).get(i);
                    /*if (Character.isUpperCase(board[i])) {
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
                    }*/
                }
            }
            return (whiteEval-blackEval) * perspective;

//        double evaluation = whiteEval - blackEval;
//        int pointPerspective = (chessGame.isAIwhite()) ? 1 : -1;
//        int pointPerspective = (chessGame.isWhitesTurn()) ? 1 : -1;
//        System.out.println("EVALUATION " + evaluation + " TURN: " + chessGame.isAIwhite()
//        + " EVAL * PERSPECT " + (evaluation * pointPerspective));

//        return evaluation * pointPerspective;
    }



    /* Quiesent search is called when a leaf node is hit on the alpha beta algorithm.
    * THe main purpose of this is to look through captures / take bakes & so on.
    *
    * "Essentially, a quiescent search is an evaluation function that takes into account some dynamic possibilities."
    * https://web.archive.org/web/20071027170528/http://www.brucemo.com/compchess/programming/quiescent.htm */
    private double quiescentSearch(double alpha, double beta, int tm) {
        double evaluation = evaluatePosition(tm);
        if (evaluation >= beta)
            return beta;
        alpha = max(alpha,evaluation);

        ArrayList<Move> captureMoves = sortMoves(chessGame.getCaptureMoves());

        for (Move m : captureMoves) {
            makeMove(m);
            evaluation = -quiescentSearch(-beta, -alpha, -tm);
            unmakeMove();
            if(evaluation >= beta)
                return beta;
            alpha = max(evaluation, alpha);
        }
        return alpha;

    }

    private ArrayList<Move> sortMoves(ArrayList<Move> movesToSort) {
        char movingPiece;
        char targetPiece;

        for (Move m : movesToSort) {
            movingPiece = m.getPiece();
            targetPiece = m.getKillPiece();

//            if(targetPiece == 'Q' || targetPiece == 'q')
//                System.out.println("queen getting attacked");

            if(targetPiece != ' ' && targetPiece != '0')
                m.setMoveScoreGuess(10 * pieceValues.get(targetPiece) - pieceValues.get(movingPiece));


            /* TODO (IF PROMOTION) */


            if (chessGame.getOpponentAttackedSquares('p').contains(m.getTargetSquare()))
                m.setMoveScoreGuess(-pieceValues.get(movingPiece));

//            System.out.print("MS: " + m.getMoveScoreGuess() + " ");
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
