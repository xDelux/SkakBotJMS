package oldCode;

import oldCode.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GUI {
    //oldCode.GUI general values
    private final JFrame gameFrame;
    private static final Dimension FRAME_DIMENSION = new Dimension(800,800);
    private final Font FONT_DEFAULT = new Font("Serif", Font.PLAIN, 50);
    private final Color colorSelected = new Color(124, 225, 124);
    private final Color colorMovePossible = new Color(217, 225, 129);

    //boardtiles
    private TilePanel[][] tiles = new TilePanel[8][8];

    //for moves
    private boolean pickedMovePiece;
    private TilePanel fromTile;
    private JLabel statusLabel;

    private Game game;

    //constructor
    public GUI(Game game){
        this.game = game;
        //setup frame
        this.gameFrame = new JFrame("Jacobs super pro chess gui");
        this.gameFrame.setSize(FRAME_DIMENSION);
        this.gameFrame.setLayout(new GridLayout(9,9));

        //create tiles for board
        boolean nextTileWhite = true;
        Color white = Color.WHITE;
        Color black = Color.LIGHT_GRAY;
        Listener listener = new Listener();

        for (int i = 7; i>=0 ; i--) {
            for (int j = 0; j < 8; j++) {
                if(nextTileWhite){
                    nextTileWhite = false;
                    tiles[i][j] = new TilePanel(i, j, white, listener);
                    this.gameFrame.add(tiles[i][j]);
                }
                else{
                    nextTileWhite = true;
                    tiles[i][j] = new TilePanel(i, j, black, listener);
                    this.gameFrame.add(tiles[i][j]);
                }
            }

            JLabel tempLabel = new JLabel(" " + (i+1));
            tempLabel.setFont(FONT_DEFAULT);
            this.gameFrame.add(tempLabel);
            nextTileWhite = !nextTileWhite;
        }
        //add bottom line letters
        char tempChar = 'A';
        for (int i = 0; i < 8; i++) {
            JLabel tempLabel = new JLabel(" " + tempChar++);
            tempLabel.setFont(FONT_DEFAULT);
            this.gameFrame.add(tempLabel);
        }
        //add statuspanel
        statusLabel = new JLabel("Next: white");
        this.gameFrame.add(statusLabel);

        //set board at start
        setBoard(game.getBoard());

        //show frame
        this.gameFrame.setVisible(true);
    }
    
    public void setBoard(char[][] board){
        //converting letter representation from backend to unicode representation for oldCode.GUI
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (board[i][j]){
                    case ' ':
                        tiles[i][j].setIcon(' ');
                        break;
                    case 'R':
                        tiles[i][j].setIcon('\u2656');
                        break;
                    case 'N':
                        tiles[i][j].setIcon('\u2658');
                        break;
                    case 'B':
                        tiles[i][j].setIcon('\u2657');
                        break;
                    case 'Q':
                        tiles[i][j].setIcon('\u2655');
                        break;
                    case 'K':
                        tiles[i][j].setIcon('\u2654');
                        break;
                    case 'P':
                        tiles[i][j].setIcon('\u2659');
                        break;
                    case 'p':
                        tiles[i][j].setIcon('\u265F');
                        break;
                    case 'r':
                        tiles[i][j].setIcon('\u265C');
                        break;
                    case 'n':
                        tiles[i][j].setIcon('\u265E');
                        break;
                    case 'b':
                        tiles[i][j].setIcon('\u265D');
                        break;
                    case 'q':
                        tiles[i][j].setIcon('\u265B');
                        break;
                    case 'k':
                        tiles[i][j].setIcon('\u265A');
                        break;
                }
            }
        }
    }

    private class TilePanel extends JPanel{
        int row, column;
        JLabel label = new JLabel("you shouldnt see this");
        Color defaultColor;

        TilePanel(int i, int j, Color color, Listener listener){
            //set label
            this.label.setFont(FONT_DEFAULT);
            this.label.setForeground(Color.BLACK);
            this.add(label);
            //set tile values
            this.row = i;
            this.column = j;
            this.defaultColor = color;
            this.setBackground(color);
            //this.setSize(100,100);
            this.addMouseListener(listener);
            this.add(label);
            setVisible(true);
        }
        public void setIcon(char icon){
            this.label.setText(icon + "");
        }

        public void setPicked(boolean isPicked){
            if(isPicked){
                this.setBackground(colorSelected);
                colorPossibleTiles(fromTile.row, fromTile.column, true);
            }
            else{
                this.setBackground(this.defaultColor);
                colorPossibleTiles(fromTile.row, fromTile.column, false);
            }
        }

    }

    //onClickListener added to all tiles
    private class Listener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            TilePanel tileClicked = (TilePanel) e.getSource();
            if(pickedMovePiece){
                if(fromTile == tileClicked){
                    //undo selection
                    fromTile.setPicked(false);
                    pickedMovePiece = false;
                }
                else {
                    //make move
                    fromTile.setPicked(false);
                    setBoard(game.moveByIndex(fromTile.column, fromTile.row, tileClicked.column, tileClicked.row));

                    pickedMovePiece = false;

                    //update status
                    if(game.isWhiteNext()){
                        statusLabel.setText("Next: white");
                    }
                    else{
                        statusLabel.setText("Next: black");
                    }

                }
            }
            else{
                // if clicked on empty tile then return cause you cant move it.
                if(tileClicked.label.getText().equals(" ")){
                    return;
                }

                //select move piece
                fromTile = tileClicked;
                tileClicked.setPicked(true);
                pickedMovePiece = true;

                ;
            }


            System.out.println(tileClicked.column + ", " + tileClicked.row);
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private void colorPossibleTiles(int row, int column, boolean possible) {
        //System.out.println("colorPossibleTiles: " + row + " " + column + " " + possible);
        ArrayList<byte[]> moves = game.getMoves(row, column);
        if(possible){
            for (byte[] move: moves) {
                tiles[move[2]][move[3]].setBackground(colorMovePossible);
            }
        }
        else{
            for (byte[] move: moves) {
                tiles[move[2]][move[3]].setBackground(tiles[move[2]][move[3]].defaultColor);
            }
        }

    }
}

