import java.util.ArrayList;
import java.util.Collection;

public class Game {
    private static char[][] board;
    private boolean whiteNext;
    private boolean[][] whitePieces;
    private boolean[][] blackPieces;
    private ArrayList<byte[]> moves = new ArrayList<>();

    public Game(){
        board = newSetupBoard();
        whiteNext = true;
        generateMoves();
    }

    public char[][] getBoard(){
        return board;
    }

    public boolean isWhiteNext(){
        return whiteNext;
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
        /*return new char[][]{
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
        };*/

        whitePieces = new boolean[][]{
                {true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false}
        };
        blackPieces = new boolean[][]{
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false},
                {true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true},
        };

        return new char[][]{
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}
        };
    }

    public char[][] moveByIndex(int colFrom, int rowFrom, int colTo, int rowTo){

        //TODO: check if legal
        //generate all moves
        //is given move part of list?
        if(whiteNext){
            if(whitePieces[rowFrom][colFrom]){
                whitePieces[rowFrom][colFrom] = false;
                whitePieces[rowTo][colTo] = true;
                //in case kill
                blackPieces[rowTo][colTo] = false;
            }
            else{
                System.out.println("it is white turn, cannot move black");
                return board;
            }
        }
        else{
            if(blackPieces[rowFrom][colFrom]){
                blackPieces[rowFrom][colFrom] = false;
                blackPieces[rowTo][colTo] = true;
                //in case kill
                whitePieces[rowTo][colTo] = false;
            }
            else{
                System.out.println("it is black turn, cannot move white");
                return board;
            }
        }

        //move chars
        board[rowTo][colTo] = board[rowFrom][colFrom];
        board[rowFrom][colFrom] = ' ';

        whiteNext = !whiteNext;

        generateMoves();

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

    private char[] whiteChars = new char[]{'R', 'N', 'B', 'Q', 'K', 'P'};
    private char[] blackChars = new char[]{'r', 'n', 'b', 'q', 'k', 'p'};

    public void generateMoves(){
        moves.clear();
        char fromTile;
        if(whiteNext) {
            //generate moves for white pieces
            System.out.println("generating white moves");
            for (byte i = 0; i < 8; i++) {
                for (byte j = 0; j < 8; j++) {
                    System.out.println("generating move:" + i + " " + j);
                    fromTile = board[i][j];
                    System.out.println(fromTile + "");
                    switch (fromTile) {
                        case 'R':
                            //add downwards
                            moves.addAll(generateRookMoves(i, j));
                            break;
                        case 'N':
                            moves.addAll(generateKnightMoves(i, j));
                            break;
                        case 'B':

                            break;

                    }
                }
            }
        }
        else{
            //generate moves for black pieces
            System.out.println("generating black moves");
            for (byte i = 0; i < 8; i++) {
                for (byte j = 0; j < 8; j++) {
                    fromTile = board[i][j];
                    System.out.println(fromTile + "");
                    switch (fromTile) {
                        case 'r':
                            moves.addAll(generateRookMoves(i, j));
                            break;
                        case 'n':
                            moves.addAll(generateKnightMoves(i, j));
                            break;
                        case 'b':
                            break;
                    }
                }
            }
        }

        //print moves for testing
        for (byte[] bytes : moves) {
            System.out.println("from: " + bytes[0] + " " + bytes[1] + " to: " + bytes[2] + " " + bytes[3]);
        }
    }

    private ArrayList<byte[]> generateKnightMoves(byte i, byte j) {
        ArrayList<byte[]> knightmoves = new ArrayList<>();
        boolean[][] enemyPositions;
        if(whiteNext){
            enemyPositions = blackPieces;
        }
        else{
            enemyPositions = whitePieces;
        }

        byte ito, jto;
        //down 2
        if(i > 1){
            System.out.println("down2");
            //left
            if(j > 0){
                System.out.println("down2left");
                ito = (byte) (i-2);
                jto = (byte) (j-1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
            //right
            if(j < 7){
                System.out.println("down2right");
                ito = (byte) (i-2);
                jto = (byte) (j+1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
        }
        //up 2
        if(i < 6){
            System.out.println("up2");
            //left
            if(j > 0){
                System.out.println("up2left");
                ito = (byte) (i+2);
                jto = (byte) (j-1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
            //right
            if(j < 7){
                System.out.println("up2right");
                ito = (byte) (i+2);
                jto = (byte) (j+1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
        }
        //left 2
        if(j > 1){
            //down
            if(i > 0){
                ito = (byte) (i-1);
                jto = (byte) (j-2);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
            //up
            if(i < 7){
                ito = (byte) (i+1);
                jto = (byte) (j-2);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
        }
        //right 2
        if(j < 6){
            //down
            if(i > 0){
                ito = (byte) (i-1);
                jto = (byte) (j+2);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
            //up
            if(i < 7){
                ito = (byte) (i+1);
                jto = (byte) (j+2);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    knightmoves.add(new byte[]{i, j, ito, jto});
                }
            }
        }

        ;

        return knightmoves;
    }

    //rook move generator working
    public ArrayList<byte[]> generateRookMoves(byte i, byte j){
        ArrayList<byte[]> moves = new ArrayList<>();
        boolean[][] enemyPositions;
        if(whiteNext){
            enemyPositions = blackPieces;
        }
        else{
            enemyPositions = whitePieces;
        }
        /*
            selects which ones are enemy positions.
            moves in all vertical and horizontal directions and checks if tile is either:
            empty - then add the move
            enemy - then add move and break since u cant pass them
            friend - then break. you cannot move there nor pass them.
        */
        //add downwards
        for (byte k = (byte) (i-1); k >= 0; k--) {
            if(board[k][j] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                moves.add(new byte[]{i, j, k, j});
            }
            else if(enemyPositions[k][j]){
                moves.add(new byte[]{i, j, k, j});
                break;
            }
            else{
                break;
            }
        }
        //add upwards
        for(byte k = (byte) (i+1); k < 8; k++){
            if(board[k][j] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                moves.add(new byte[]{i, j, k, j});
            }
            else if(enemyPositions[k][j]){
                moves.add(new byte[]{i, j, k, j});
                break;
            }
            else{
                break;
            }
        }
        //add left
        for (byte k = (byte) (j-1); k >= 0; k--) {
            if(board[i][k] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                moves.add(new byte[]{i, j, i, k});
            }
            else if(enemyPositions[i][k]){
                moves.add(new byte[]{i, j, i, k});
                break;
            }
            else{
                break;
            }
        }
        //add right
        for (byte k = (byte) (j+1); k < 8; k++){
            if(board[i][k] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                moves.add(new byte[]{i, j, i, k});
            }
            else if(enemyPositions[i][k]){
                moves.add(new byte[]{i, j, i, k});
                break;
            }
            else{
                break;
            }
        }
        return moves;
    }

    public ArrayList<byte[]> getMoves(int i, int j){
        ArrayList<byte[]> tileMoves = new ArrayList<>();
        for (byte[] move :
                moves) {
            if (move[0] == i && move[1] == j){
                tileMoves.add(move);
            }
        }
        return tileMoves;
    }

}
