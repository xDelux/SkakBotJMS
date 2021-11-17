import java.lang.reflect.Array;
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
                            moves.addAll(generateBishopMoves(i,j));
                            break;
                        case 'Q':
                            //queen moves like a rook and a bishop connected
                            moves.addAll(generateRookMoves(i, j));
                            moves.addAll(generateBishopMoves(i, j));
                            break;
                        case 'K':
                            moves.addAll(generateKingMoves(i,j));
                            break;
                        case 'P':
                        moves.addAll(generatePawnMoves(i,j));
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
                            moves.addAll(generateBishopMoves(i,j));
                            break;
                        case 'q':
                            //queen moves like a rook and a bishop connected
                            moves.addAll(generateRookMoves(i, j));
                            moves.addAll(generateBishopMoves(i, j));
                            break;
                        case 'k':
                            moves.addAll(generateKingMoves(i,j));
                            break;
                        case 'p':
                            moves.addAll(generatePawnMoves(i,j));
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

    private ArrayList<byte[]> generatePawnMoves(byte i, byte j) {
        ArrayList<byte[]> pawnMoves = new ArrayList<>();
        boolean[][] enemyPositions;
        byte ito, jto;
        if(whiteNext){
            enemyPositions = blackPieces;
           //up
            if(i<7){
                ito = (byte) (i+1);
                //upleft attack
                if(j > 0){
                    jto = (byte) (j-1);
                    if(enemyPositions[ito][jto]){
                        pawnMoves.add(new byte[]{i, j, ito, jto});
                    }
                }
                //upright attack
                if(j < 7){
                    jto = (byte) (j+1);
                    if(enemyPositions[ito][jto]){
                        pawnMoves.add(new byte[]{i, j, ito, jto});
                    }
                }
                //up 1 nonattack
                if(board[ito][j] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    pawnMoves.add(new byte[]{i, j, ito, j});
                    //up 2 if not moved yet and noone in front(non attack)
                    if(i == 1){
                        ito = 3;
                        if(board[ito][j] == ' '){
                            pawnMoves.add(new byte[]{i, j, ito, j});
                        }
                    }
                }
            }
        }
        else{
            enemyPositions = whitePieces;
            //down
            if(i>0){
                ito = (byte) (i-1);
                //downleft attack
                if(j > 0){
                    jto = (byte) (j-1);
                    if(enemyPositions[ito][jto]){
                        pawnMoves.add(new byte[]{i, j, ito, jto});
                    }
                }
                //downright attack
                if(j < 7){
                    jto = (byte) (j+1);
                    if(enemyPositions[ito][jto]){
                        pawnMoves.add(new byte[]{i, j, ito, jto});
                    }
                }
                //down 1 non-attack
                if(board[ito][j] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    pawnMoves.add(new byte[]{i, j, ito, j});
                    //down 2 if not moved yet and noone in front(non attack)
                    if(i == 6){
                        ito = 4;
                        if(board[ito][j] == ' '){
                            pawnMoves.add(new byte[]{i, j, ito, j});
                        }
                    }
                }
            }
        }
        return pawnMoves;
    }

    //king move generator. working.
    private ArrayList<byte[]> generateKingMoves(byte i, byte j) {
        ArrayList<byte[]> kingMoves = new ArrayList<>();
        boolean[][] enemyPositions;
        if(whiteNext){
            enemyPositions = blackPieces;
        }
        else{
            enemyPositions = whitePieces;
        }
        byte ito, jto;
        //down
        if(i>0){
            ito = (byte) (i-1);
            if(board[ito][j] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                kingMoves.add(new byte[]{i, j, ito, j});
            }
            else if(enemyPositions[ito][j]){
                kingMoves.add(new byte[]{i, j, ito, j});
            }
            //downleft
            if(j>0){
                jto = (byte) (j-1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
            }
            //downright
            if(j<7){
                jto = (byte) (j+1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
            }

        }
        //up
        if(i<7){
            ito = (byte) (i+1);
            if(board[ito][j] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                kingMoves.add(new byte[]{i, j, ito, j});
            }
            else if(enemyPositions[ito][j]){
                kingMoves.add(new byte[]{i, j, ito, j});
            }
            //upleft
            if(j>0){
                jto = (byte) (j-1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
            }
            //upright
            if(j<7){
                jto = (byte) (j+1);
                if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
                else if(enemyPositions[ito][jto]){
                    kingMoves.add(new byte[]{i, j, ito, jto});
                }
            }

        }
        //left
        if(j>0){
            jto = (byte) (j-1);
            if(board[i][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                kingMoves.add(new byte[]{i, j, i, jto});
            }
            else if(enemyPositions[i][jto]){
                kingMoves.add(new byte[]{i, j, i, jto});
            }
        }
        //right
        if(j<7){
            jto = (byte) (j+1);
            if(board[i][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                kingMoves.add(new byte[]{i, j, i, jto});
            }
            else if(enemyPositions[i][jto]){
                kingMoves.add(new byte[]{i, j, i, jto});
            }
        }

        return kingMoves;
    }

    //bishop move generator. working.
    private ArrayList<byte[]> generateBishopMoves(byte i, byte j) {
        ArrayList<byte[]> bishopMoves = new ArrayList<>();
        boolean[][] enemyPositions;
        if(whiteNext){
            enemyPositions = blackPieces;
        }
        else{
            enemyPositions = whitePieces;
        }
        byte ito = i;
        byte jto = j;
        //upleft
        while(ito < 7 && jto > 0){
            ito++;
            jto--;
            if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                bishopMoves.add(new byte[]{i, j, ito, jto});
            }
            else if(enemyPositions[ito][jto]){
                bishopMoves.add(new byte[]{i, j, ito, jto});
                break;
            }
            else break;
        }
        ito = i;
        jto = j;
        //upright
        while(ito < 7 && jto < 7){
            ito++;
            jto++;
            if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                bishopMoves.add(new byte[]{i, j, ito, jto});
            }
            else if(enemyPositions[ito][jto]){
                bishopMoves.add(new byte[]{i, j, ito, jto});
                break;
            }
            else break;
        }
        ito = i;
        jto = j;
        //downleft
        while(ito > 0 && jto > 0){
            ito--;
            jto--;
            if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                bishopMoves.add(new byte[]{i, j, ito, jto});
            }
            else if(enemyPositions[ito][jto]){
                bishopMoves.add(new byte[]{i, j, ito, jto});
                break;
            }
            else break;
        }
        ito = i;
        jto = j;
        //downright
        while(ito > 0 && jto < 7){
            ito--;
            jto++;
            if(board[ito][jto] == ' '){ //could be !whitepiece[k][j] && !blackpieces[k][j] ??? which is speed?
                bishopMoves.add(new byte[]{i, j, ito, jto});
            }
            else if(enemyPositions[ito][jto]){
                bishopMoves.add(new byte[]{i, j, ito, jto});
                break;
            }
            else break;
        }


        return bishopMoves;
    }

    //knight move generator. working.
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

    //rook move generator. working.
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

    //return the moves from movelist that start at i,j
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
