package finalVar;

import java.io.Serializable;

/**
 * 
 * @author Ariel
 */
public class Square implements Serializable{


//    Global veriables 
//    ================================================
    protected int row;
    protected int col;

    /**
     * Constructor that has been used by all 6 types of pieces in chess board
     * @param indexRow The coordinates of the row
     * @param indexCol The coordinates of the colum
     */
    public Square(int indexRow, int indexCol) {
        this.row = indexRow;
        this.col = indexCol;
    }

    /**
     * Constructor that has been used by all 6 types of pieces in chess board
     * @param otherSquare The other square
     */
    public Square(Square otherSquare) {
        this(otherSquare.getRow(), otherSquare.getCol());
    }
    
    /**
     * Function that checks if the indexes of row and col of pieces are equal
     * @param otherSquare the selected Square
     * @return returns true if the it is selected
     */
    public boolean equals(Square otherSquare){
        return this.row == otherSquare.getRow() && this.col == otherSquare.getCol();
    }
    
    
    /**
     * Function that returns the index row of Square
     * @return the index row
     */
    public int getRow() {
        return row;
    }

    /**
     * Function that returns the index colum of Square
     * @return the index of colum
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the index Row of the Square
     * @param indexRow the new value of the index
     */
    public void setRow(int indexRow) {
        this.row = indexRow;
    }

    /**
     * Sets the index Colum of the Square
     * @param indexCol the new value of index
     */
    public void setCol(int indexCol) {
        this.col = indexCol;
    }
    
    @Override
    public String toString() {
        return "["+ row + " " + col+"]";
    }

    public void setSquare(Square s) {
        this.row = s.getRow();
        this.col = s.getCol();
    }
    
    
}
