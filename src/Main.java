import java.util.Scanner;

public class Main {

    public static char[][] board;
    public static void main(String[] args) {
        board = newSetupBoard();
        boolean whiteNext = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        while(true){
            print(board);
            System.out.println("input move in format like: e2e4 (moving e2 to e4)");
            input = scanner.nextLine();
            move(input);
        }
    }

    public static char[][] newSetupBoard() {
        return new char[][]{
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}
        };
    }
    public static void move(String input){
        int xFrom = input.charAt(0) - 'a';
        int yFrom = input.charAt(1) - '1';
        int xTo = input.charAt(2) - 'a';
        int yTo = input.charAt(3) - '1';

        System.out.println("moving from:" + xFrom + " " + yFrom + " to: " + xTo + " " + yTo);

        board[yTo][xTo] = board[yFrom][xFrom];
        board[yFrom][xFrom] = '-';


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
    }

}
