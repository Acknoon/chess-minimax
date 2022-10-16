package finalVar;

import java.util.ArrayList;

/**
 * The point of the Class is the represent the knight that is located on the
 * board. A Knight is a chess piece that moves only "L" shaped moves, as the
 * Knight moves in very unique way it cannot be blocked by other pieces to move.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class Knight extends Piece {

//    Default attributes
//    ======================================================
    public static final int VALUE = 3;//This is a default calue given to knight as a piece
    //This value gives the computer a reason to value which piece stands better in ranking

//    Unique veriables of the class
//    ======================================================
    protected static enum KnightDirection {
        SOUTH, NORTH, EAST, WEST
    };//unique directions for the knight.

//    =====================Constructors=======================
    /**
     * The constructor of the knight class, which basically means to create a
     * body for the knight piece. The knight constructor just gives the Piece
     * class unique values and functions that represents the knight.
     *
     * @param row Which row location that the piece is in the board currently.
     * @param col Which colum location that the piece is the board currently.
     * @param type Variable to define on which player the piece belongs to. The
     * White pieces or Black pieces.
     */
    public Knight(int row, int col, int type) {
        super(row, col, type);
    }

//    =========================Functions=======================
    /**
     * The main idea of it to calculate and convert all squares a piece can move
     * to. In this sense as we are inside the knight piece class we get out of
     * the function the list of moves. List of Moves that indicate what a knight
     * on the board can move to. However, this function only calculates the
     * squares the knight potentially can move to. It also take in a count the
     * position of the pieces located on the board as a knight cannot move past
     * pieces.
     *
     * @param gLogic A variable that contains a model of the game to help
     * calculate the knights squares it can move.
     * @return a list of moves that shows where a knight can move non-relative
     * to the king.
     */
    @Override
    public ArrayList<Move> getDestinationList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();

//      The South direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, 2, 1, Avaliability.MOVE);
        super.occupyToList(this, mat, listMoves, 2, -1, Avaliability.MOVE);

//      The North direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, -2, 1, Avaliability.MOVE);
        super.occupyToList(this, mat, listMoves, -2, -1, Avaliability.MOVE);

//      The East direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, 1, 2, Avaliability.MOVE);
        super.occupyToList(this, mat, listMoves, -1, 2, Avaliability.MOVE);

//      The West direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, 1, -2, Avaliability.MOVE);
        super.occupyToList(this, mat, listMoves, -1, -2, Avaliability.MOVE);

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
     * calculate the knights squares it can move.
     * @return a list of moves that shows where a knight can move relative to
     * the king position while also contains allied pieces squares.
     */
    @Override
    public ArrayList<Move> getControllingList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();

//      The South direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, 2, 1, Avaliability.CONTROL);
        super.occupyToList(this, mat, listMoves, 2, -1, Avaliability.CONTROL);

//      The North direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, -2, 1, Avaliability.CONTROL);
        super.occupyToList(this, mat, listMoves, -2, -1, Avaliability.CONTROL);

//      The East direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, 1, 2, Avaliability.CONTROL);
        super.occupyToList(this, mat, listMoves, -1, 2, Avaliability.CONTROL);

//      The West direction of knight
//      ================================================================
        super.occupyToList(this, mat, listMoves, 1, -2, Avaliability.CONTROL);
        super.occupyToList(this, mat, listMoves, -1, -2, Avaliability.CONTROL);

        return listMoves;
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
        return (s.row >= this.indexRow - 2 && s.row <= this.indexRow + 2)
                && (s.col >= this.indexCol - 2 && s.col <= this.indexCol + 2);
    }

    /**
     * Give access to the value of the piece openly. mostly used for game
     * explanation or computer evaluation.
     *
     * @return The value of the piece
     */
    @Override
    public int getValuePiece() {
        return Const.KNIGHT;
    }

    /**
     * Prints the tag of the Piece the location of it in the board and the value
     * of it. (The Piece Sign)(The Piece Tag) The tag explains what type of
     * piece the knight is.
     */
    @Override
    public void toStringPiece() {
        super.toStringPiece();
        System.out.print(Const.TAG_KNIGHT);
    }
}
