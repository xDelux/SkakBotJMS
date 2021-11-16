import pieces.Piece;

public class Tile {

    boolean isOccupied;
    boolean isEdge;
    Piece piece;

    public Tile(boolean isOccupied, boolean isEdge, Piece piece) {
        this.isOccupied = isOccupied;
        this.isEdge = isEdge;
        this.piece = piece;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isEdge() {
        return isEdge;
    }

    public void setEdge(boolean edge) {
        isEdge = edge;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }




}
