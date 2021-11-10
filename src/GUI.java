import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI {
    private final JFrame gameFrame;
    private static Dimension FRAME_DIMENSION = new Dimension(800,800);
    private TilePanel[][] tiles = new TilePanel[8][8];

    private boolean pickedMovePiece;
    private TilePanel fromTile;

    private Game game;

    public GUI(Game game){
        this.game = game;

        //setup frame
        this.gameFrame = new JFrame("Jacobs super pro chess gui");
        this.gameFrame.setSize(FRAME_DIMENSION);
        this.gameFrame.setLayout(new GridLayout(8,8));

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
            nextTileWhite = !nextTileWhite;
        }

        //set board at start
        setBoard(game.getBoard());

        //show frame
        this.gameFrame.setVisible(true);
    }
    
    public void setBoard(char[][] board){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].setIcon(board[i][j]);
            }
        }
    }

    private class TilePanel extends JPanel{
        int row, column;
        JLabel label = new JLabel("null");
        Color defaultColor;

        TilePanel(int i, int j, Color color, Listener listener){
            //set label
            this.label.setFont(new Font("Serif", Font.PLAIN, 50));
            this.label.setForeground(Color.BLACK);
            this.add(label);
            //set tile values
            this.row = i;
            this.column = j;
            this.defaultColor = color;
            this.setBackground(color);
            this.setSize(100,100);
            this.addMouseListener(listener);
            this.add(label);
            setVisible(true);
        }
        public void setIcon(char icon){
            this.label.setText(icon + "");
        }

        public void setPicked(boolean isPicked){
            if(isPicked){
                this.setBackground(new Color(124, 225, 124));
            }
            else{
                this.setBackground(this.defaultColor);
            }
        }

    }

    private class Listener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            TilePanel tileClicked = (TilePanel) e.getSource();
            if(pickedMovePiece){
                if(fromTile == tileClicked){
                    fromTile.setPicked(false);
                    pickedMovePiece = false;
                }
                else{
                    setBoard(game.moveByIndex(fromTile.column, fromTile.row, tileClicked.column, tileClicked.row));
                    fromTile.setPicked(false);
                    pickedMovePiece = false;
                }
            }
            else{
                tileClicked.setPicked(true);
                pickedMovePiece = true;
                fromTile = tileClicked;
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
}

