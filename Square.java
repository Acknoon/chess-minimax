/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

/**
 *The Main Class that holds 6 type of Pieces in chess game
 * @author Ariel
 */
public class Square {

//        The default attributes If I were to use The matrix with Char
//    ================================================
    public static final int FIRST_ROW = 7;
    public static final int SECOND_ROW = 6;
    public static final int LAST_ROW = 0;
    public static final int SEVENTH_ROW = 1;

//    Global veriables 
//    ================================================
    protected int indexRow;
    protected int indexCol;

    /**
     * Constructor that has been used by all 6 types of pieces in chess board
     * @param indexRow The coordinates of the row
     * @param indexCol The coordinates of the colum
     */
    public Square(int indexRow, int indexCol) {
        this.indexRow = indexRow;
        this.indexCol = indexCol;
    }
    
    /**
     * Function that checks if the indexes of row and col of pieces are equal
     * @param otherSquare the selected Square
     * @return returns true if the it is selected
     */
    public boolean equals(Square otherSquare){
        return this.indexRow == otherSquare.getRow() && this.indexCol == otherSquare.getCol();
    }
    
    
    /**
     * Function that returns the index row of Square
     * @return the index row
     */
    public int getRow() {
        return indexRow;
    }

    /**
     * Function that returns the index colum of Square
     * @return the index of colum
     */
    public int getCol() {
        return indexCol;
    }

    /**
     * Sets the index Row of the Square
     * @param indexRow the new value of the index
     */
    public void setRow(int indexRow) {
        this.indexRow = indexRow;
    }

    /**
     * Sets the index Colum of the Square
     * @param indexCol the new value of index
     */
    public void setCol(int indexCol) {
        this.indexCol = indexCol;
    }
    
    
   
    
    @Override
    public String toString() {
        return "["+ indexRow + " " + indexCol+"]";
    }

    public void setSquare(Square s) {
        this.indexRow = s.getRow();
        this.indexCol = s.getCol();
    }
    
    
}
