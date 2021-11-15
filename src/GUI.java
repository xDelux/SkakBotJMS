import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.jar.JarEntry;

public class GUI {
    //GUI general values
    private final JFrame window;
    private final JPanel numberPanel;
    private final JPanel letterPanel;
    private final JPanel gamePanel;
    private static Dimension FRAME_DIMENSION;
    private static final Color LIGHT = new Color(252,194,142);
    private static final Color DARK = new Color(149,84,28);
//    private final Font FONT_DEFAULT = new Font("Serif", Font.PLAIN, 70);

    //boardtiles
    final ArrayList<TilePanel> tilesA = new ArrayList<>(64);
    private final TilePanel[] tiles = new TilePanel[64];

    //for moves
    private boolean pickedMovePiece;
    private TilePanel fromTile;
    private JLabel statusLabel;

    private Game game;
    private char board[];

    //constructor
    public GUI(Game game){
        this.game = game;
        this.board = game.getBoard();

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
        for (int i = 63; i >= 0; i--) {
            System.out.println("TILE: " + i);
            temp = new TilePanel(i,nextTileBright, listener);
            tilesA.add(temp);
            setBoard(temp);
            gamePanel.add(temp.getTile());
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
        switch (board[tile.getPosition()]) {
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

    public void setGUI(TilePanel source, TilePanel target) {
        Container parent = source.getParent();
        TilePanel temp = source;

        System.out.println(parent.getComponent(source.position));
    }


    private class TilePanel extends JPanel{
        Icon displayPiece;
        int position;
        JPanel tile = new JPanel(new BorderLayout());
        JLabel label = new JLabel();

        boolean transparent = false;
        Color defaultColor;

        TilePanel(int i, boolean isBright, Listener listener){
            setLayout(new BorderLayout());
            label.setHorizontalAlignment(JLabel.CENTER);

            //set tile values
            position = i;
            defaultColor = isBright ? LIGHT : DARK;
            setBackground(defaultColor);
            addMouseListener(listener);

        }

        public TilePanel getTile () {
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

        public void setPicked(boolean isPicked){
            if(isPicked){
                this.setBackground(new Color(124, 225, 124));
            }
            else{
                this.setBackground(defaultColor);
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

            System.out.println("\nTILE CLICKED: " + tileClicked);
            Container parent = tileClicked.getParent();

            System.out.println("\nPARENT CONTAINER LOOKING FOR CHILD: " + parent.getComponentAt(tileClicked.getLocation()));
            System.out.println("\nPARENT CONTAINER: " + parent);
//            System.out.println(Arrays.toString(parent.getComponents()));


            if(pickedMovePiece){
                if(fromTile == tileClicked){
                    //undo selection
                    fromTile.setPicked(false);
                    pickedMovePiece = false;
                } else{
                    //make move
                    game.moveByIndex(fromTile.position, tileClicked.position);

                    tileClicked.setTileIcon(fromTile.getTileIcon());
                    fromTile.setTileIcon("transparent");

                    System.out.println(game.getBoard());

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
            } else{
                // if clicked on empty tile then return cause you cant move it.
                if(tileClicked.getTransparency()){
                    return;
                }
                //select move piece
                tileClicked.setPicked(true);
                pickedMovePiece = true;
                fromTile = tileClicked;
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
}

