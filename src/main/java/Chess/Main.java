package Chess;

import Chess.Moves.MoveGen;
import Chess.aI.Algorithm;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //figure out if ai is white or black:
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to play as black(b) or white (w)? input b or w followed by enter");
        String input = scanner.nextLine();
        boolean isAIwhite;
        isAIwhite = input.charAt(0) != 'w'; //AI will be opposite of input :)


//        Board board = new Board(true);
//        MoveGen moveGen = new MoveGen();
        Game chessGame = new Game(isAIwhite, true, true);
        NewGUI gui = new NewGUI(chessGame);
        chessGame.setGUI(gui);
        //Algorithm AI = new Algorithm(chessGame);
        //AI.runAlphaBeta();
//        AI.alphaBeta(chessGame.getAllMoves(),3, minEval, maxEval, true);
    }
}
