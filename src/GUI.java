import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI {
    //GUI general values
    private final JFrame gameFrame;
    private static Dimension FRAME_DIMENSION;
    private static final Color LIGHT = new Color(252,194,142);
    private static final Color DARK = new Color(149,84,28);
//    private final Font FONT_DEFAULT = new Font("Serif", Font.PLAIN, 70);

    //boardtiles
    private TilePanel[] tiles = new TilePanel[64];

    //for moves
    private boolean pickedMovePiece;
    private TilePanel fromTile;
    private JLabel statusLabel;

    private Game game;

    //constructor
    public GUI(Game game){
        this.game = game;

        //setup frame
        FRAME_DIMENSION = new Dimension(800,800);
        this.gameFrame = new JFrame("Jacobs super pro chess gui");
        this.gameFrame.setSize(FRAME_DIMENSION);
        this.gameFrame.setLayout(new GridLayout(9,9));


        //create tiles for board
        boolean nextTileBright = true;

//        Color white = Color.WHITE;
//        Color black = Color.LIGHT_GRAY;
        Listener listener = new Listener();

        int counter = 63;
        for (int i = 7; i >= 0 ; i--) {
            System.out.println(i);
            this.gameFrame.add(new JLabel(" " + (i+1)));

            for (int j = counter; j > counter-8; j--) {
                System.out.println("SOUTING COUNTER: " + counter);
                System.out.println("TILE: " + j);
                tiles[j] = new TilePanel(j,nextTileBright,listener);
                this.gameFrame.add(tiles[j]);
                nextTileBright = !nextTileBright;
            }
            counter -= 8;
            nextTileBright = !nextTileBright;
        }

        /*for (int i = 70; i >= 0; i--) {
            if(i%9 == 8) {
                System.out.println(i);
                this.gameFrame.add(new JLabel(" " + (counter)));
                counter--;

            } else {
                tiles[i] = new TilePanel(i,nextTileBright,listener);
                this.gameFrame.add(tiles[i]);
                nextTileBright = !nextTileBright;
            }
        }*/


        /*for (int i = 7; i>=0 ; i--) {
            for (int j = 0; j < 8; j++) {
                if(nextTileBright){
                    nextTileBright = false;
                    tiles[i][j] = new TilePanel(i, j, white, listener);
                    this.gameFrame.add(tiles[i][j]);
                }
                else{
                    nextTileBright = true;
                    tiles[i][j] = new TilePanel(i, j, black, listener);
                    this.gameFrame.add(tiles[i][j]);
                }
            }

            JLabel tempLabel = new JLabel(" " + (i+1));
//            tempLabel.setFont(FONT_DEFAULT);
            this.gameFrame.add(tempLabel);
            nextTileBright = !nextTileBright;
        }*/
//        for (int i = 0; i < 8; i++) {
//            this.gameFrame.add(new JLabel(" " + (i+1)));
//        }

        //add bottom line letters
        char tempChar = 'A';
//        this.gameFrame.add(new JLabel(" "));
        for (int i = 0; i < 8; i++) {
            JLabel tempLabel = new JLabel(" " + tempChar++);
//            tempLabel.setFont(FONT_DEFAULT);
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
    
    /*public void setBoard(char[][] board){
        //converting letter representation from backend to unicode representation for GUI
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (board[i][j]){
                    case ' ':
                        tiles[i][j].setPieceIcon(' ');
                        break;
                    case 'R':
                        tiles[i][j].setPieceIcon('\u2656');
                        break;
                    case 'N':
                        tiles[i][j].setPieceIcon('\u2658');
                        break;
                    case 'B':
                        tiles[i][j].setPieceIcon('\u2657');
                        break;
                    case 'Q':
                        tiles[i][j].setPieceIcon('\u2655');
                        break;
                    case 'K':
                        tiles[i][j].setPieceIcon('\u2654');
                        break;
                    case 'P':
                        tiles[i][j].setPieceIcon('\u2659');
                        break;
                    case 'p':
                        tiles[i][j].setPieceIcon('\u265F');
                        break;
                    case 'r':
                        tiles[i][j].setPieceIcon('\u265C');
                        break;
                    case 'n':
                        tiles[i][j].setPieceIcon('\u265E');
                        break;
                    case 'b':
                        tiles[i][j].setPieceIcon('\u265D');
                        break;
                    case 'q':
                        tiles[i][j].setPieceIcon('\u265B');
                        break;
                    case 'k':
//                        tiles[i][j].setIcon('\u265A');
                        tiles[i][j].setPieceIcon("k");

                        break;
                }
            }
        }
    }*/
    public void setBoard(char[] board){
        //converting letter representation from backend to unicode representation for GUI
        for (int i = 0; i < 64; i++) {
            switch (board[i]) {
                case ' ' -> tiles[i].setPieceIcon(' ');
                case 'R' -> tiles[i].setPieceIcon('\u2656');
                case 'N' -> tiles[i].setPieceIcon('\u2658');
                case 'B' -> tiles[i].setPieceIcon('\u2657');
                case 'Q' -> tiles[i].setPieceIcon('\u2655');
                case 'K' -> tiles[i].setPieceIcon('\u2654');
                case 'P' -> tiles[i].setPieceIcon('\u2659');
                case 'p' -> tiles[i].setPieceIcon('\u265F');
                case 'r' -> tiles[i].setPieceIcon('\u265C');
                case 'n' -> tiles[i].setPieceIcon('\u265E');
                case 'b' -> tiles[i].setPieceIcon('\u265D');
                case 'q' -> tiles[i].setPieceIcon('\u265B');
                case 'k' -> tiles[i].setPieceIcon('k');
//                        tiles[i]].setIcon('\u265A');

            }
        }
    }


    private class TilePanel extends JPanel{
        Icon displayPiece;
        int position;
        JLabel label = new JLabel();
        Color defaultColor;

        TilePanel(int i, boolean isBright, Listener listener){
            //set label
//            this.label.setFont(FONT_DEFAULT);
            this.label.setForeground(Color.BLACK);
            this.add(label);
//            this.label.setSize(200,200);
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setVerticalAlignment(JLabel.CENTER);
            //set tile values
            this.position = i;
//            this.column = j;
            this.defaultColor = isBright ? LIGHT : DARK;
            this.setBackground(defaultColor);
            //this.setSize(100,100);
            this.addMouseListener(listener);
//            this.add(label);
            setVisible(true);
        }
//        public void setPieceIcon(char icon){
//            this.label.setText(icon + "");
//        }

        public void setPieceIcon(char icon){
            //label.getWidth & getHeight
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/ressources/" + icon + ".png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            label.setIcon(imageIcon);
//            System.out.println(new java.io.File("src/ressources/k.png").exists());
//
//            this.label.setIcon(new ImageIcon("src/ressources/k.png"));
        }

        public void setPicked(boolean isPicked){
            if(isPicked){
                this.setBackground(new Color(124, 225, 124));
            }
            else{
                this.setBackground(this.defaultColor);
            }
        }

        public int getPosition() {
            return position;
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
                else{
                    //make move
                    setBoard(game.moveByIndex(fromTile.position, tileClicked.position));
                    fromTile.setPicked(false);
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
                tileClicked.setPicked(true);
                pickedMovePiece = true;
                fromTile = tileClicked;
            }


            System.out.println(tileClicked + ", " + tileClicked);
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

