import java.io.IOException;
import newBoard.*;

public class Main {

    public static char[][] board;
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        GUI gui = new GUI(game);
        /*Board board = new Board();
        MoveGen moves = new MoveGen();*/



//        System.out.println("Make sure to keep backend using simple letters as unicode letters are encoded as multiple bytes");
//        System.out.println("binary of \u2656: " + Integer.toBinaryString('\u2656'));
//        System.out.println("binary of R: " + Integer.toBinaryString('R'));
        /*board = newSetupBoard();
        boolean whiteNext = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        GUI gui = new GUI();
        while(true){
            print(board);
            gui.setBoard(board);
            System.out.println("input move in format like: e2e4 (moving e2 to e4)");
            input = scanner.nextLine();
            move(input);
        }*/
    }

    public static char[][] newSetupBoard() {
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
    public static void move(String input){
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
