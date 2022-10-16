package finalVar;

import java.util.ArrayList;

/**
 *  A piece that represents it's generic form compared to other 6 pieces.
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public abstract class Piece{
//    The Default Attributes for the Piece
//    =========================================================

    /**
     * A mode on which a piece controls squares.
     */
    public static enum Avaliability {
        MOVE, CONTROL
    };

//    The global veriables that are been used for the logic in the Object
//    =========================================================
    protected int indexRow;// The row location on the chess board
    protected int indexCol;// The colum location on the chess board
    protected int colorSide;//What type of piece is it.
    protected int moveCounter;//counts the move the piece made

    /**
     * A constructor that identifies the square that belongs to the owner
     *
     * @param row row location in the board
     * @param col colum location in the board
     * @param sign what type of square it belongs to
     */
    public Piece(int row, int col, int sign) {
        this.indexRow = row;
        this.indexCol = col;
        this.colorSide = sign;
//        this.isChecking = false;
        this.moveCounter = 0;
    }

    /**
     * Function that returns list of moves
     *
     * @param logicModel the reference to the model
     * @return a list of moves avaliable for the piece
     */
    public abstract ArrayList<Move> getDestinationList(Model logicModel);

    /**
     * Function that returns a list of squares a piece controls
     *
     * @param logicModel reference to the model
     * @return list of square the piece controls
     */
    public abstract ArrayList<Move> getControllingList(Model logicModel);

//    protected abstract ArrayList<Move> getLegalMoves(Model logicModel);
    /**
     * The Function that checks if the each piece on the board are able to
     * control the square, Despite being blocked by other pieces
     *
     * @param s The current square that the piece can reach
     * @return true if Any type of piece are able to reach that square and false
     * if not
     */
    public abstract boolean isControllingSquare(Square s);

    /**
     * Function that occupies the Square if it's empty or if enemy piece is
     * located
     *
     * @param mat the current game matrix
     * @param listMoves the list of moves the current piece can move
     * @param row is the next row for all different type of moves belongs to
     * unique piece
     * @param col is the next colum for all different type of moves belongs to
     * unique piece
     * @param mode is an option by which what piece is piece can do move or
     * Control squares
     * @throws SquareException that stops when the piece that occupies the
     * square
     */
    protected void confirmOccupy(Piece[][] mat, ArrayList<Move> listMoves,
            int row, int col, Avaliability mode) throws SquareException {

        switch (mode) {
            case CONTROL:
                if (mat[row][col] == null || (mat[row][col] instanceof King && mat[row][col].getSign() == getOppositeSign())) {
                    listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                } else {
                    if (mat[row][col].getSign() == this.colorSide) {
                        listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                    }
                    throw new SquareException();
                }
                break;
            case MOVE:
                if (mat[row][col] == null) {
                    listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                } else {
                    if (mat[row][col].getSign() == getOppositeSign()) {
                        listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                    }
                    throw new SquareException();
                }
        }
    }

    /**
     * Function that fills a series of moves of the current piece this series is
     * could be infinite however it eventually stop up until it reaches another
     * piece or reaches the edge of the board
     *
     * @param mainPiece The current piece that has been selected
     * @param mat the board matrix in the game
     * @param listMoves list of moves that saves the moves that piece has to
     * move
     * @param rowStep current constant value that makes a series of moves by row
     * @param colStep current constant value that makes a series of moves by
     * colum
     * @param mode boolean value that asks the piece controls a list of moves or
     * moves it can move
     */
    protected void occupyOrderToList(Piece mainPiece, Piece[][] mat,
            ArrayList<Move> listMoves, int rowStep, int colStep, Avaliability mode) {
        int row = mainPiece.getRow(), col = mainPiece.getCol();
        try {
            for (;;) {
                row += rowStep;
                col += colStep;
                confirmOccupy(mat, listMoves, row, col, mode);
            }
        } catch (SquareException | ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Function tests the next square near the piece being selected on each
     * direction
     *
     * @param mainPiece The current piece that has been selected
     * @param mat the board matrix in the game
     * @param listMoves list of moves that saves the moves that piece has to
     * move
     * @param rowStep current constant value that makes a series of moves by row
     * @param colStep current constant value that makes a series of moves by
     * colum
     * @param mode Boolean factor that decides what type of squares the piece
     * controls or moves
     */
    protected void occupyToList(Piece mainPiece, Piece[][] mat,
            ArrayList<Move> listMoves, int rowStep, int colStep, Avaliability mode) {
        int row = mainPiece.getRow() + rowStep, col = mainPiece.getCol() + colStep;
        try {
            confirmOccupy(mat, listMoves, row, col, mode);
        } catch (SquareException | ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Function that returns the Piece of the control that square
     *
     * @return the current player
     */
    public int getSign() {
        return colorSide;
    }

    /**
     * Function that sets new Piece if it's white or black
     *
     * @param thePlayer the Piece that has been set
     */
    public void setType(int thePlayer) {
        this.colorSide = thePlayer;
    }

    /**
     * Function gets the row of the piece
     *
     * @return the colum piece located
     */
    public int getRow() {
        return indexRow;
    }

    /**
     * Function that sets the row of the piece
     *
     * @param indexRow new row
     */
    public void setRow(int indexRow) {
        this.indexRow = indexRow;
    }

    /**
     * Function gets the colum of the piece
     *
     * @return the colum piece located
     */
    public int getCol() {
        return indexCol;
    }

    /**
     * Function sets the colum of the piece
     *
     * @param indexCol new colum to set
     */
    public void setCol(int indexCol) {
        this.indexCol = indexCol;
    }

    /**
     * function returns the location of the piece on the board
     *
     * @return the square the piece located
     */
    public Square getSquare() {
        return new Square(this.indexRow, this.indexCol);
    }

    /**
     * Function that sends the opposite type of this Square
     *
     * @return the opposite or enemy of this type
     */
    public int getOppositeSign() {
        return this.colorSide == Const.PLAYER_WHITE ? Const.PLAYER_BLACK : Const.PLAYER_WHITE;
    }

    /**
     * Function that returns the value of the piece
     *
     * @return the value of piece
     */
    public abstract int getValuePiece();

    /**
     * Prints the Sign of what currently located on the square
     */
    public void toStringPiece() {
        System.out.print(colorSide == -1 ? "-" : "+");
    }

    /**
     * Prints the coordinates of the piece
     */
    public void toStringCoordinates() {
        System.out.print("[" + indexRow + ", " + indexCol + "]");
    }

    /**
     * Sends the coordinates of location of the piece to be printed
     *
     * @return sends the row and col of the piece
     */
    public String getStringCoordinates() {
        return "[" + indexRow + ", " + indexCol + "]";
    }

    /**
     * Function that adds one move to move counter
     */
    public void addMoveCounter() {
        this.moveCounter++;
    }

    /**
     * Function that subtracts a move that was taken back
     */
    public void subMoveCounter() {
        this.moveCounter--;
    }

    /**
     * Function that returns the amount of moves that have been made of this
     * piece
     *
     * @return
     */
    public int getMoveCounter() {
        return this.moveCounter;
    }
}
