package finalVar;

import java.util.ArrayList;

/**
 * The point of the Class is the represent the queen that is located on the
 * board. A Queen is a chess piece that can move diagonally, Horizontally,
 * Vertically on the board, can be any length of squares. However, till it
 * reaches the end of the board or till it reaches another piece. The Queen is
 * the most powerful piece in the chess game since that it has the most value of
 * it.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class Queen extends Piece {

//    Default attributes
//    =====================================================
    public static final int VALUE = 9;//This is a default calue given to Queen as a piece
    //This value gives the computer a reason to value which piece stands better in ranking

//    ===================Constructors========================
    /**
     * The constructor of the Queen class, which basically means to create a
     * body for the Queen piece. The Queen constructor just gives the Piece
     * class unique values and functions that represents the Queen.
     *
     * @param row Which row location that the piece is in the board currently.
     * @param col Which colum location that the piece is the board currently.
     * @param type Variable to define on which player the piece belongs to. The
     * White pieces or Black pieces.
     */
    public Queen(int row, int col, int type) {
        super(row, col, type);
    }

//    ====================Functions===========================
    /**
     * The main idea of it to calculate and convert all squares a piece can move
     * to. In this sense as we are inside the Queen piece class we get out of
     * the function the list of moves. List of Moves that indicate what a Queen
     * on the board can move to. However, this function only calculates the
     * squares the Queen potentially can move to. It also take in a count the
     * position of the pieces located on the board as a Queen cannot move past
     * pieces.
     *
     * @param gLogic A variable that contains a model of the game to help
     * calculate the Queens squares it can move.
     * @return a list of moves that shows where a Queen can move non-relative to
     * the king.
     */
    @Override
    public ArrayList<Move> getDestinationList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();
        for (int indexR = -1; indexR < 2; indexR++) {
            for (int indexC = -1; indexC < 2; indexC++) {
                super.occupyOrderToList(this, mat, listMoves, indexR, indexC, Avaliability.MOVE);
            }
        }
        return listMoves;
    }

    /**
     * Calculates and defines the squares of the chess board the piece controls.
     * This means the calculation of the function defines the squares the piece
     * can move. In addition of the squares of the friendly pieces it protects.
     * This calculation is also relative to the king position on the board as it
     * ignores the king completely from the board. The purpose of the avoiding
     * the king position is because the king can't block a square since it's the
     * main objective of the game.
     *
     * @param gLogic A variable that contains a model of the game to help
     * calculate the Queens squares it can move.
     * @return a list of moves that shows where a Queen can move relative to the
     * king position while also contains allied pieces squares.
     */
    @Override
    public ArrayList<Move> getControllingList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listControl = new ArrayList<>();
        for (int indexR = -1; indexR < 2; indexR++) {
            for (int indexC = -1; indexC < 2; indexC++) {
                super.occupyOrderToList(this, mat, listControl, indexR, indexC, Avaliability.CONTROL);
            }
        }

        for (int i = 0; i < listControl.size(); i++) {
            if (listControl.get(i).getDestinationSquare().equals(new Square(this.indexRow, this.indexCol))) {
                listControl.remove(i);
            }
        }
        return listControl;
    }

    /**
     * A simple calculations that shows true or false result weather the piece
     * are able to control the square. However, this function's flaw is
     * completely dismissing the other Pieces on the chess board. The advantage
     * of this function that it's very efficient with O(1).
     *
     * @param s represents the square requested to check if the current piece
     * can control it.
     * @return either true of false if it can potentially control the square.
     */
    @Override
    public boolean isControllingSquare(Square s) {
        return (Math.abs(s.row - this.indexRow) == Math.abs(s.col - this.indexCol))
                || ((this.indexRow == s.row) || (this.indexCol == s.col));
    }

    /**
     * Give access to the value of the piece openly. mostly used for game
     * explanation or computer evaluation.
     *
     * @return The value of the piece
     */
    @Override
    public int getValuePiece() {
        return Const.QUEEN;
    }

    /**
     * Prints the tag of the Piece the location of it in the board and the value
     * of it. (The Piece Sign)(The Piece Tag) The tag explains what type of
     * piece the Queen is.
     */
    @Override
    public void toStringPiece() {
        super.toStringPiece();
        System.out.print(Const.TAG_QUEEN);
    }
}
