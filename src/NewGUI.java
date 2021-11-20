import newBoard.Board;
import newBoard.Move;
import newBoard.MoveGen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NewGUI {
    //GUI general values
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
    final ArrayList<TilePanel> tilesA = new ArrayList<>(64);
    private final TilePanel[] tiles = new TilePanel[64];

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

    //constructor
    public NewGUI(MoveGen mg) {
        this.moveGen = mg;
        this.board = mg.workloadBoard();
        this.boardIndex = mg.returnBoardIndex();
        this.boardToInt = mg.returnBoardInt();


        //setup all frames and panels
        FRAME_DIMENSION = new Dimension(900,900);
        window = new JFrame("Jacobs super pro chess gui");
//        window.setSize(FRAME_DIMENSION);
        window.setLayout(new BorderLayout());
//        window.setLayout(new GridLayout(9,9));

        //panels
        letterPanel = new JPanel();
        letterPanel.setLayout(new GridLayout(1,8));
//        letterPanel.setSize(new Dimension(,200));
//        letterPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//        letterPanel.setLayout(new BoxLayout(letterPanel,BoxLayout.X_AXIS));
//        letterPanel.setSize(300,500);

        numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(9  ,1));
//        numberPanel.setSize(new Dimension(200,200));
//        numberPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//        numberPanel.setLayout(new BoxLayout(numberPanel,BoxLayout.Y_AXIS));
//        numberPanel.setSize(800,300);

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(9,8));
//        gamePanel.setSize(new Dimension(500,500));

//        gamePanel.setPreferredSize(new Dimension(600,600));
//        gamePanel.setBounds(0,0,window.getWidth(),window.getHeight());

        JPanel smallBox = new JPanel(new GridLayout(1,1));
        smallBox.add(new JLabel("T"));

        //create tiles for board
        boolean nextTileBright = true;

//        Color white = Color.WHITE;
//        Color black = Color.LIGHT_GRAY;
        Listener listener = new Listener();


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
        int counter = 0;
       /* for (int i = 63; i >= 0; i--) {
            temp = new TilePanel(i, nextTileBright, listener);
            tilesA.add(temp);
            setBoard(temp);
            gamePanel.add(temp.getTilePanel());
            if(counter==7)
                counter = 0;
            else {
                counter++;
                nextTileBright = !nextTileBright;
            }
        };*/

        for (int i = 0; i < 64; i++) {
//            System.out.println("TILE: " + i);
            temp = new TilePanel(i, nextTileBright, listener);
            tilesA.add(temp);
            setBoard(temp);
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
        /*tempLabel = new JLabel(" ", SwingConstants.CENTER);
        tempLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        letterPanel.add(tempLabel);*/

        /* Adding all the panels to the frame */
        window.add(numberPanel, BorderLayout.LINE_END);
        window.add(gamePanel, BorderLayout.CENTER);
        window.add(letterPanel, BorderLayout.PAGE_END);

        //show frame
        window.pack();
        this.window.setVisible(true);
    }

    public void setBoard(TilePanel tile){
        //converting letter representation from backend to unicode representation for GUI
        switch (board[boardIndex[tile.getTilePos()]]) {
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

    private class TilePanel extends JPanel{
        Icon displayPiece;
        int tilePos;
        JPanel tile = new JPanel(new BorderLayout());
        JLabel label = new JLabel();

        boolean transparent = false;
        Color defaultColor;

        TilePanel(int i, boolean isBright, Listener listener){
            setLayout(new BorderLayout());
            label.setHorizontalAlignment(JLabel.CENTER);

            //set tile values
            tilePos = i;
            defaultColor = isBright ? LIGHT : DARK;
            setBackground(defaultColor);
            addMouseListener(listener);

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
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/res/" + icon + ".png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            if(icon.equals("transparent"))
                transparent = true;
            label.setIcon(imageIcon);
            add(label);
//            System.out.println(new java.io.File("src/ressources/kb.png").exists());
        }

        public void setTileIcon(Icon icon){
            transparent = false;
            getLabel().setIcon(icon);
        }

        public Icon getTileIcon () {
            return getLabel().getIcon();
        }

        public int getTilePos() {
            return tilePos;
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
                } else{
                    // Check if the move is valid and can be made
                    boolean targetTrue = false;
                    for (Move m: tileMoves) {
                        if(boardIndex[targetTile.getTilePos()] == m.getTargetSquare()) {
                            targetTrue = true;
                            break;
                        }
                    }
                    if(targetTrue) {
                        //make move
                        targetTile.setTileIcon(startTile.getTileIcon());
                        startTile.setTileIcon("transparent");
                        pickedMovePiece = false;
                        startTile.setPicked(false, tileMoves);
                        moveGen.moveByIndex(startTile.tilePos, targetTile.tilePos);
                    }

                }
            } else{
                // if clicked on empty tile then return cause you cant move it.
                if(targetTile.getTransparency()){
                    return;
                }

                //select move piece
                startTile = targetTile;

                /* Prepare moves list for coloring by using method in move gen
                 * And using boardIndex for finding the correct index of the specific tile in the
                 * 12x12 array containing the pieces. Because a "Move" contains index of the piece
                 * position before it performs a move.  */
                tileMoves = moveGen.getSpecificMoves(boardIndex[startTile.getTilePos()]);

                targetTile.setPicked(true, tileMoves);
                pickedMovePiece = true;

            }


//            System.out.println(tileClicked + ", " + tileClicked);
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

    private void colorPossibleMoveTiles(TilePanel startTile, boolean isPicked, ArrayList<Move> moves) {
        int tileToColor;
        if(isPicked) {
            for (Move m : moves){
                tileToColor = boardToInt[m.getTargetSquare()];
                System.out.println(tileToColor);
                tilesA.get(tileToColor).setBackground(colorMovePossible);
//                tilesA[indexOf].setBackground(colorMovePossible);
            }
        } else {
            for (Move m : moves) {
                tileToColor = boardToInt[m.getTargetSquare()];
                TilePanel temp = tilesA.get(tileToColor);
                temp.setBackground(temp.defaultColor);
            }
        }
    }
}

