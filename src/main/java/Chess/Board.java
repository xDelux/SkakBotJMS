package Chess;

import java.util.Arrays;

public class Board {
    /* BOARD ARRAYS */
    public int[] boardIndex = new int[64];
    public int[] boardInt = new int[144];
    public char[] board = new char[144];
    public char[] boardStack;

    /* RANK & FILE ARRAY*/
    public char[] rank = new char[64];
    public int[] file = new int[64];

    /* NOT USED: color array*/
    public boolean[] whitePieces;
    public boolean[] blackPieces;

    public Board (boolean wantTest) {
        if ((wantTest)) {
            setupBoardWithCustomStartingPosition();
        } else {
            setupBoardChar();
        }

        setupBoardInt();
        setupBoardIndex();
//        setupRanksAndFiles();
    }

    private void setupBoardIndex() {
        /*int counter = 26;
        for (int i = 0; i < 64; i++) {
//            System.out.println(counter);
            boardIndex[i] = counter;
            if(! (i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || i == 47 || i == 55 || i == 63))
                counter++;
            else
                counter += 5;
        }*/
        boardIndex = new int[] {
                26, 27, 28, 29, 30, 31, 32, 33,
                38, 39, 40, 41, 42, 43, 44, 45,
                50, 51, 52, 53, 54, 55, 56, 57,
                62, 63, 64, 65, 66, 67, 68, 69,
                74, 75, 76, 77, 78, 79, 80, 81,
                86, 87, 88, 89, 90, 91, 92, 93,
                98, 99, 100, 101, 102, 103, 104, 105,
                110, 111, 112, 113, 114, 115, 116, 117
        };
        System.out.println(Arrays.toString(boardIndex));
    }

    private void setupBoardInt () {
        /* Use together with the index board to easily look uå index if needed.
        * Else start index for this array is
        * STARTINDEX = 26 || ENDINDEX = 117
        * */
        boardInt = new int[]{
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
//        System.out.println("start index: board[26] | "+ boardInt[26] + "\nNow adding offset for vertical: +12 | board[26+12] | " + boardInt[26+12]);
    }

    private void setupBoardWithCustomStartingPosition () {
        board = new char[]{
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 'r', 'n', 'b', 'q', 'k', 'b', ' ', 'r', '0', '0',
                '0', '0', 'p', 'p', 'p', ' ', ' ', 'p', 'p', 'p', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', 'B', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', 'p', 'p', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', 'P', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', 'P', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'P', 'P', 'P', ' ', ' ', 'P', 'P', 'P', '0', '0',
                '0', '0', 'R', 'N', ' ', 'Q', 'K', 'B', 'N', 'R', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };
        /*board = new char[]{
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'p', ' ', ' ', ' ', 'k', 'p', ' ', ' ', '0', '0',
                '0', '0', 'p', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'Q', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'P', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'P', ' ', ' ', ' ', ' ', ' ', 'q', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', 'K', ' ', ' ', ' ', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };*/
        /*board = new char[]{
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', 'p', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', 'P', ' ', 'P', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };*/
    }
    private void setupBoardChar () {
        /* TESTING BOARD | SWAPPED ARRAY ATM */
        board = new char[]{
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r', '0', '0',
                '0', '0', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', '0', '0',
                '0', '0', 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };
    }


    public int[] getBoardIndex() {
        return boardIndex;
    }

    public int[] getBoardInt() {
        return boardInt;
    }



    /* Convert 12x12 board to 8x8 board with the pieces */
    public char[] get8by8AsChars() {
        char[] tempBoard = new char[64];
        for (int i = 0; i < 64; i++) {
            tempBoard[i] = board[boardIndex[i]];
        }
        return tempBoard;
    }
    public char[] get8by8AsChars(char[] custom) {
        char[] tempBoard = new char[64];
        for (int i = 0; i < 64; i++) {
            tempBoard[i] = custom[boardIndex[i]];
        }
        return tempBoard;
    }


    public int convertBoardIndexToIndex (int startSquare) {
        if(startSquare != -1)
            return boardInt[startSquare];
        return 0;
    }
    public int convertIndexToBoardIndex(int startSquare) {
        return boardIndex[startSquare];
    }

    /* Moves a piece one the board with conversion.
    * This method is used when coming from squares that are 8x8 (0 to 63)
    * like in the GUI with tile positions when clicking on tiles.
    * BoardIndex converts a 8x8 (0 to 64) to the board 12x12 (0 to 143) */
    public boolean movePieceWithConversion(int startSquare, int targetSquare) {
        System.out.println("board movepiecewith conversion: " + startSquare + " " + targetSquare);
        if(board[boardIndex[targetSquare]] != '0') {
            board[boardIndex[targetSquare]] = board[boardIndex[startSquare]];
            board[boardIndex[startSquare]] = ' ';
            System.out.println("movepiecewithconversion: true");
            return true;
        }
        System.out.println("movepiecewithconversion: false");
        return false;
    }
    /* Moves a piece on the board.
    * Used when operating on moves */
    public void movePiece(int startSquare, int targetSquare) {
        if(!(startSquare == '0' || targetSquare == '0')) {
            board[targetSquare] = board[startSquare];
            board[startSquare] = ' ';
        }
    }


    public char[] getBoard() {
        return board;
    }
    public void setBoard(char[] board) {
        this.board = board;
    }
}
