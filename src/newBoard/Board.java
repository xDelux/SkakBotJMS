package newBoard;

import java.util.Arrays;

public class Board {
    public int[] boardIndex = new int[64];
    public int[] board = new int[144];
    public char[] boardChar = new char[144];
    public boolean[] whitePieces;
    public boolean[] blackPieces;

    public Board () {
        setupBoardInt();
        setupBoardChar();
        setupBoardIndex();
    }

    private void setupBoardIndex() {
        int counter = 26;
        for (int i = 0; i < 64; i++) {
//            System.out.println(counter);
            boardIndex[i] = counter;
            if(! (i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || i == 47 || i == 55 || i == 63))
                counter ++;
            else
                counter += 5;
        }

//        for (int i = 0; i < 144; i++) {
//            if(board[i] != -1)
//                System.out.println("INDEX I BOARD SAT MED INT: " + i);
//        }
//        System.out.println(Arrays.toString(boardIndex));
    }
    private void setupBoardInt () {
        /* Use together with the index board to easily look uå index if needed.
        * Else start index for this array is
        * STARTINDEX = 26 || ENDINDEX = 117
        * */
        board = new int[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1,  0,  1,  2,  3,  4,  5,  6,  7, -1, -1,
                -1, -1,  8,  9, 10, 11, 12, 13, 14, 15, -1, -1,
                -1, -1, 16, 17, 18, 19, 20, 21, 22, 23, -1, -1,
                -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1,
                -1, -1, 32, 33, 34, 35, 36, 37, 38, 39, -1, -1,
            -1, -1, 40, 41, 42, 43, 44, 45, 46, 47, -1, -1,
                -1, -1, 48, 49, 50, 51, 52, 53, 54, 55, -1, -1,
                -1, -1, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
        };
        System.out.println("start index: board[26] | "+ board[26] + "\nNow adding offset for vertical: +12 | board[26+12] | " + board[26+12]);
    }
    private void setupBoardChar () {
        boardChar = new char[]{
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R', '0', '0',
                '0', '0', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', '0', '0',
                '0', '0', 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };
    }

    public int[] getBoardIndex() {
        return board;
    }

    public int[] getBoardInt() {
        return board;
    }

    public char[] getBoardChar() {
        return boardChar;
    }

}
