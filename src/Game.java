import java.util.Scanner;

public class Game {
    private static char[][] board;
    private boolean whiteNext;

    public Game(){
        board = newSetupBoard();
        whiteNext = true;
    }

    public char[][] getBoard(){
        return board;
    }

    public char[][] newSetupBoard() {
        /*
        * Unicode for pieces
                        "\u2654 " + // white king
                        "\u2655 " + // white queen
                        "\u2656 " + // white rook
                        "\u2657 " + // white bishop
                        "\u2658 " + // white knight
                        "\u2659 " + // white pawn
                        "\n" +
                        "\u265A " + // black king
                        "\u265B " + // black queen
                        "\u265C " + // black rook
                        "\u265D " + // black bishop
                        "\u265E " + // black knight
                        "\u265F " + // black pawn
                        "\n" ;*/
        return new char[][]{
                //white side (row 0 and 1)
                {'\u2656', '\u2658', '\u2657', '\u2655', '\u2654', '\u2657', '\u2658', '\u2656'},
                {'\u2659', '\u2659', '\u2659', '\u2659', '\u2659', '\u2659', '\u2659', '\u2659'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'\u265F', '\u265F', '\u265F', '\u265F', '\u265F', '\u265F', '\u265F', '\u265F'},
                {'\u265C', '\u265E', '\u265D', '\u265B', '\u265A', '\u265D', '\u265E', '\u265C'}
                //black side (row 6 and 7)
        };

        /*return new char[][]{
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}
        };*/
    }

    public char[][] moveByIndex(int colFrom, int rowFrom, int colTo, int rowTo){

        //TODO: check if legal

        board[rowTo][colTo] = board[rowFrom][colFrom];
        board[rowFrom][colFrom] = ' ';
        return board;
    }

    public void move(String input){
        int xFrom = input.charAt(0) - 'a';
        int yFrom = input.charAt(1) - '1';
        int xTo = input.charAt(2) - 'a';
        int yTo = input.charAt(3) - '1';

        System.out.println("moving from:" + xFrom + " " + yFrom + " to: " + xTo + " " + yTo);

        board[yTo][xTo] = board[yFrom][xFrom];
        board[yFrom][xFrom] = ' ';
    }

    public static void print(char[][] board){
        System.out.println("__A_B_C_D_E_F_G_H");;
        for (int i = 7; i >= 0; i--) {
            System.out.print((i+1) + "|");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println('\u2654' + "\u265A ");
    }

}
