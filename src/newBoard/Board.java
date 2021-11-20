package newBoard;

public class Board {
    /* BOARD ARRAYS */
    public int[] boardIndex = new int[64];
    public int[] boardInt = new int[144];
    public char[] boardChar = new char[144];

    /* RANK & FILE ARRAY*/
    public char[] rank = new char[64];
    public int[] file = new int[64];

    /* NOT USED: color array*/
    public boolean[] whitePieces;
    public boolean[] blackPieces;

    public Board (boolean wantTest) {
        if (wantTest) {
            setupBoardCharTESTARRAY();
        } else {
            setupBoardChar();
        }
        setupBoardInt();
        setupBoardIndex();
        setupRanksAndFiles();
    }

    private void setupBoardIndex() {
        int counter = 26;
        for (int i = 0; i < 64; i++) {
//            System.out.println(counter);
            boardIndex[i] = counter;
            if(! (i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || i == 47 || i == 55 || i == 63))
                counter++;
            else
                counter += 5;
        }
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
        System.out.println("start index: board[26] | "+ boardInt[26] + "\nNow adding offset for vertical: +12 | board[26+12] | " + boardInt[26+12]);
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
    private void setupBoardCharTESTARRAY () {
        boardChar = new char[]{
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 'R', ' ', 'B', 'Q', 'K', 'B', 'N', 'R', '0', '0',
                '0', '0', 'P', 'P', 'P', 'P', ' ', 'P', 'P', 'P', '0', '0',
                '0', '0', ' ', ' ', 'N', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', 'P', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', 'p', 'p', ' ', ' ', ' ', '0', '0',
                '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0',
                '0', '0', 'p', 'p', 'p', ' ', ' ', 'p', 'p', 'p', '0', '0',
                '0', '0', 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };
    }

    private void setupRanksAndFiles() {
        rank = new char[] {
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'D', 'H', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'
        };

        file = new int[] {
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '0', '0', 1, 1, 1, 1, 1, 1, 1, 1, '0', '0',
                '0', '0', 2, 2, 2, 2, 2, 2, 2, 2, '0', '0',
                '0', '0', 3, 3, 3, 3, 3, 3, 3, 3, '0', '0',
                '0', '0', 4, 4, 4, 4, 4, 4, 4, 4, '0', '0',
                '0', '0', 5, 5, 5, 5, 5, 5, 5, 5, '0', '0',
                '0', '0', 6, 6, 6, 6, 6, 6, 6, 6, '0', '0',
                '0', '0', 7, 7, 7, 7, 7, 7, 7, 7, '0', '0',
                '0', '0', 8, 8, 8, 8, 8, 8, 8, 8, '0', '0',
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

    public char[] getBoardChar() {
        return boardChar;
    }

    /* Convert 12x12 board to 8x8 board with the pieces */
    public char[] get8by8() {
        char[] tempBoard = new char[64];
        for (int i = 0; i < 64; i++) {
            tempBoard[i] = boardChar[boardIndex[i]];
        }
        return tempBoard;
    }

    public int getFile(int startSquare) {
        return file[startSquare];
    }

    public char getRank (int startSquare) {
        return rank[startSquare];
    }

    public String posToString(int startSquare) {
        return "" + getRank(startSquare) + "" + getFile(startSquare);
    }
    public int getIndexOfSpecific (int startSquare) {
        if(startSquare != -1)
            return boardInt[startSquare];
        return 0;
    }



}
