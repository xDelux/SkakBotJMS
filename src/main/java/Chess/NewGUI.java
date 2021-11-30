package Chess;

import Chess.Moves.Move;
import Chess.Moves.MoveGen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NewGUI {
    //oldCode.GUI general values
    private final JFrame window;
    private final JPanel numberPanel;
    private final JPanel letterPanel;
    private final JPanel gamePanel;
    private static Dimension FRAME_DIMENSION;
    private static final Color LIGHT = new Color(252,194,142);
    private static final Color DARK = new Color(149,84,28);
    private static final Color colorMovePossible = new Color(150, 225, 129);

//    private final Font FONT_DEFAULT = new Font("Serif", Font.PLAIN, 70);

    //boardtiles
    final ArrayList<TilePanel> tiles = new ArrayList<>(64);

    //for moves
    private boolean pickedMovePiece;
    ArrayList<Move> tileMoves;
    private TilePanel startTile;
    private TilePanel targetTile;
    private JLabel statusLabel;

    private Board boardClass;
    private char board[];
    private int boardIndex[];
    private int boardToInt[];
    private MoveGen moveGen;
    private final Game chessGame;

    //constructor
    public NewGUI(Game chessGame) {
        this.chessGame = chessGame;
//        this.moveGen = mg;
        this.board = chessGame.get8By8Board();

        //setup all frames and panels
        FRAME_DIMENSION = new Dimension(900,900);
        window = new JFrame("Jacobs super pro chess gui");
//        window.setSize(FRAME_DIMENSION);
        window.setLayout(new BorderLayout());
//        window.setLayout(new GridLayout(9,9));

        //panels
        letterPanel = new JPanel();
        letterPanel.setLayout(new GridLayout(1,8));

        numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(9  ,1));

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(9,8));

        //buttonpanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));

        //make buttons and add to panel
        JLabel chooseColor = new JLabel("Choose color you'd like:");
        buttonPanel.add(chooseColor);
        JButton blackButton = new JButton("Black");
        blackButton.addActionListener(new BlackButtonListener());
        buttonPanel.add(blackButton);
        JButton whiteButton = new JButton("White");
        whiteButton.addActionListener(new WhiteButtonListener());
        buttonPanel.add(whiteButton);
        JButton newGameButton = new JButton("Start Game");
        newGameButton.addActionListener(new NewGameButtonListener());
        buttonPanel.add(newGameButton);


        JPanel smallBox = new JPanel(new GridLayout(1,1));
        smallBox.add(new JLabel("T"));

        //create tiles for board
        boolean nextTileBright = true;


        //Setting up files
        JLabel tempLabel;
        for (int i = 8; i > 0; i--) {
            tempLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
//            tempLabel.setText("" + i);
            tempLabel.setFont(new Font("Verdana",Font.BOLD, 15));
            tempLabel.setPreferredSize(new Dimension(50,50));
//            tempLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            numberPanel.add(tempLabel);
//            numberPanel.setMinimumSize(new Dimension(50,50));
        }

        //Setting up main chess board
        TilePanel temp;
        char piece;
        int counter = 0;

        Listener listener = new Listener();

        for (int i = 0; i < 64; i++) {
//            System.out.println("TILE: " + i);
            piece = board[i];
            temp = new TilePanel(i, piece, nextTileBright);
//            temp.addMouseListener(listener);
            tiles.add(temp);
            setBoardTileIcon(temp);
            gamePanel.add(temp.getTilePanel());
            if(counter==7)
                counter = 0;
            else {
                counter++;
                nextTileBright = !nextTileBright;
            }
        };

        //Setting up ranks
        for (char a = 'a'; a < 'i'; a++ ) {
            tempLabel = new JLabel(String.valueOf(a),SwingConstants.CENTER);
//            tempLabel.setText(String.valueOf(a));
            tempLabel.setFont(new Font("Verdana",Font.BOLD, 20));
//            tempLabel.setPreferredSize(new Dimension(5,40));
//            tempLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            gamePanel.add(tempLabel);
        }

        /* Adding all the panels to the frame */
        window.add(numberPanel, BorderLayout.LINE_END);
        window.add(gamePanel, BorderLayout.CENTER);
        window.add(letterPanel, BorderLayout.PAGE_END);
        window.add(buttonPanel, BorderLayout.PAGE_END);

        //show frame
        window.pack();
        this.window.setVisible(true);
    }

    public void updateBoard(){
        char[] board_backend = chessGame.get8By8Board();
        for (int i = 0; i < tiles.size(); i++) {
            char piece = board_backend[i];
            tiles.get(i).setTilePiece(piece);
            setBoardTileIcon(tiles.get(i));
        }
//        tileMoves = chessGame.getSpecificMoves(61);
//        System.out.println(tileMoves.toString());

    }
    public void setBoardTileIcon(TilePanel tile){
        //converting letter representation from backend to unicode representation for oldCode.GUI
        switch (tile.getTilePiece()) {
            case ' ' -> tile.setTileIcon("transparent");
            case 'R' -> tile.setTileIcon("Rw");
            case 'N' -> tile.setTileIcon("KTw");
            case 'B' -> tile.setTileIcon("Bw");
            case 'Q' -> tile.setTileIcon("Qw");
            case 'K' -> tile.setTileIcon("Kw");
            case 'P' -> tile.setTileIcon("Pw");
            case 'p' -> tile.setTileIcon("pb");
            case 'r' -> tile.setTileIcon("rb");
            case 'n' -> tile.setTileIcon("ktb");
            case 'b' -> tile.setTileIcon("bb");
            case 'q' -> tile.setTileIcon("qb");
            case 'k' -> tile.setTileIcon("kb");
//                        tiles[i]].setIcon('\u265A');
        }
    }

    private class BlackButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            chessGame.playerChooseColor(false);
        }
    }
    private class WhiteButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            chessGame.playerChooseColor(true);
        }
    }
    private class NewGameButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            chessGame.startNewGame();
        }
    }

    private class TilePanel extends JPanel{
        int tilePos;
        char tilePiece;

        JLabel label = new JLabel();

        boolean transparent = false;
        Color defaultColor;

        TilePanel(int i, char piece, boolean isBright){
            setLayout(new BorderLayout());
            label.setHorizontalAlignment(JLabel.CENTER);

            //set tile values
            tilePos = i;
            tilePiece = piece;
            defaultColor = isBright ? LIGHT : DARK;
            setBackground(defaultColor);
            Listener mouseListener = new Listener();
            addMouseListener(mouseListener);
//            addMouseListener(listener);

        }

        public TilePanel getTilePanel() {
            return this;
        }
        public JLabel getLabel () {
            return label;
        }
        public boolean getTransparency() {
            return transparent;
        }

        public void setTileIcon(String icon){
            //label.getWidth & getHeight
            //ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/main/java/Chess/res/" + icon + ".png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/" + icon + ".png"));
            Image img = imageIcon.getImage().getScaledInstance(60,60,Image.SCALE_SMOOTH);


            transparent = icon.equals("transparent");
            //label.setIcon(imageIcon);
            label.setIcon(new ImageIcon(img));
            add(label);
//            System.out.println(new java.io.File("src/ressources/kb.png").exists());
        }

        public void setTileIcon(Icon icon){
            transparent = false;
            getLabel().setIcon(icon);
        }

        public Icon getTileIcon () {return getLabel().getIcon();}
        public int getTilePos() {
            return tilePos;
        }
        public char getTilePiece() { return  tilePiece; }
        public void setTilePiece(char tilePiece) {
            this.tilePiece = tilePiece;
        }

        public void setPicked(boolean isPicked, ArrayList<Move> moves){
            if(isPicked){
                this.setBackground(new Color(124, 225, 124));
                colorPossibleMoveTiles(targetTile, true, moves);
            }
            else{
                this.setBackground(defaultColor);
                colorPossibleMoveTiles(targetTile, false, moves);
            }
        }


    }

    //onClickListener added to all tiles
    private class Listener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            targetTile = (TilePanel) e.getSource();

//            System.out.println("\nTILE CLICKED: " + targetTile);
//            Container parent = targetTile.getParent();
//
//            System.out.println("\nPARENT CONTAINER LOOKING FOR CHILD: " + parent.getComponentAt(targetTile.getLocation()));
//            System.out.println("\nPARENT CONTAINER: " + parent);
//            System.out.println(Arrays.toString(parent.getComponents()));

            if(pickedMovePiece){
                if(startTile == targetTile){
                    //undo selection
                    startTile.setPicked(false, tileMoves);
                    pickedMovePiece = false;
                } else {
                    // Check if the move is valid and can be made
                    boolean targetTrue = false;
                    for (Move m: tileMoves) {
                        if(targetTile.getTilePos() == chessGame.rewriteThis(m.getTargetSquare())) {
                            targetTrue = true;
                            break;
                        }
                    }
                    if(targetTrue) {
                        //make move
//                        targetTile.setTileIcon(startTile.getTileIcon());
//                        startTile.setTileIcon("transparent");
                        pickedMovePiece = false;
                        startTile.setPicked(false, tileMoves);
                        //System.out.println("GUI attempting move: " + startTile.getTilePos() + " " + targetTile.getTilePos());
                        chessGame.executeMoveFromGui(startTile.getTilePos(), targetTile.getTilePos());

//                        moveGen.moveByIndex(startTile.tilePos, targetTile.tilePos);
                    }

                }
            } else{
                // if clicked on empty tile then return cause you cant move it.
                if(targetTile.getTransparency()) {
                    return;
                }

                //select move piece
                startTile = targetTile;

                /* Prepare moves list for coloring by using method in move gen
                 * And using boardIndex for finding the correct index of the specific tile in the
                 * 12x12 array containing the pieces. Because a "Move" contains index of the piece
                 * position before it performs a move.  */
                tileMoves = chessGame.getSpecificMoves(startTile.getTilePos());

                targetTile.setPicked(true, tileMoves);
                pickedMovePiece = true;

            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private void colorPossibleMoveTiles(TilePanel startTile, boolean isPicked, ArrayList<Move> moves) {
        int tileToColor;
        if(isPicked) {
            for (Move m : moves){
                tileToColor = chessGame.rewriteThis(m.getTargetSquare());
                //System.out.println(tileToColor);
                try{
                    tiles.get(tileToColor).setBackground(colorMovePossible);
                } catch (IndexOutOfBoundsException ignored) {}

//                tilesA[indexOf].setBackground(colorMovePossible);
            }
        } else {
            for (Move m : moves) {
                tileToColor = chessGame.rewriteThis(m.getTargetSquare());
                try {
                    TilePanel temp = tiles.get(tileToColor);
                    temp.setBackground(temp.defaultColor);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }
    }
}

