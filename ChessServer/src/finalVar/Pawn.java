package finalVar;

import java.util.ArrayList;

/**
 * The point of the Class is the represent the pawn that is located on the
 * board. A Pawn is a chess piece that can only march forward and never return
 * back, the pawn is the most complex piece in the game. as it has unique
 * features such as able to start to move 2 squares from their original position
 * and are able to promote to another piece. Pawns are the pieces that have the
 * least amount of value.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class Pawn extends Piece {

//    ========================================================
    public static final int VALUE = 1;
    public static final boolean PAWN_CAPTURE = false;
    public static final boolean PAWN_MOVE = true;

//    Veriables of unique class
//    =======================================================
    private final boolean isPromoted;

//   ======================Constructors========================
    public Pawn(int row, int col, int type) {
        super(row, col, type);
        this.isPromoted = false;
    }

//    ========================Functions========================
    /**
     * The main idea of it to calculate and convert all squares a piece can move
     * to. In this sense as we are inside the Pawn piece class we get out of the
     * function the list of moves. List of Moves that indicate what a Pawn on
     * the board can move to. However, this function only calculates the squares
     * the Pawn potentially can move to. It also take in a count the position of
     * the pieces located on the board as a Pawn cannot move past pieces.
     *
     * @param gLogic A variable that contains a model of the game to help
     * calculate the Pawns squares it can move.
     * @return a list of moves that shows where a Pawn can move non-relative to
     * the king.
     */
    @Override
    public ArrayList<Move> getDestinationList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();
        int row = this.indexRow;
        switch (this.colorSide) {
//            ============================White Pawns Movement============================
            case Const.PLAYER_WHITE:
                if (occupyToListPawn(this, mat, listMoves, -1, 0, PAWN_MOVE, Avaliability.MOVE)
                        && row == Const.SECOND_ROW) {
                    occupyToListPawn(this, mat, listMoves, -2, 0, PAWN_MOVE, Avaliability.MOVE);
                }

//                =======================Capture avaliable for white pawn=========================
                occupyToListPawn(this, mat, listMoves, -1, -1, PAWN_CAPTURE, Avaliability.MOVE);
                occupyToListPawn(this, mat, listMoves, -1, 1, PAWN_CAPTURE, Avaliability.MOVE);
                break;

//                ===========================Black Pawns Movement============================
            case Const.PLAYER_BLACK:
                if (occupyToListPawn(this, mat, listMoves, 1, 0, PAWN_MOVE, Avaliability.MOVE)
                        && row == Const.SEVENTH_ROW) {
                    occupyToListPawn(this, mat, listMoves, 2, 0, PAWN_MOVE, Avaliability.MOVE);
                }

//                =====================Capture avaliable for black pawns===========================
                occupyToListPawn(this, mat, listMoves, 1, -1, PAWN_CAPTURE, Avaliability.MOVE);
                occupyToListPawn(this, mat, listMoves, 1, 1, PAWN_CAPTURE, Avaliability.MOVE);
                break;
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
     * calculate the Pawns squares it can move.
     * @return a list of moves that shows where a Pawn can move relative to the
     * king position while also contains allied pieces squares.
     */
    @Override
    public ArrayList<Move> getControllingList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();
        switch (this.colorSide) {
            case Const.PLAYER_WHITE:
//                =======================Capture avaliable for white pawn=========================
                occupyToListPawn(this, mat, listMoves, -1, -1, PAWN_CAPTURE, Avaliability.CONTROL);
                occupyToListPawn(this, mat, listMoves, -1, 1, PAWN_CAPTURE, Avaliability.CONTROL);
                break;

            case Const.PLAYER_BLACK:
//                =====================Capture avaliable for black pawns===========================
                occupyToListPawn(this, mat, listMoves, 1, -1, PAWN_CAPTURE, Avaliability.CONTROL);
                occupyToListPawn(this, mat, listMoves, 1, 1, PAWN_CAPTURE, Avaliability.CONTROL);
                break;
        }
        return listMoves;
    }

    /**
     * Function that occupies the Square if it's empty and not occupied by any
     * other piece then inserts the non-occupied square to a list of squares
     * that are avaliable for the pawn to move
     *
     * @param mat the current game matrix
     * @param listMoves the list of moves the current piece can move
     * @param row is the next row for all different type of moves belongs to
     * unique piece
     * @param col is the next colum for all different type of moves belongs to
     * unique piece
     * @throws SquareException that stops when the piece that occupies the
     * square
     */
    private void confirmOccupyEmptySquare(Piece[][] mat, ArrayList<Move> listMoves, int row, int col)
            throws SquareException {
        if (mat[row][col] == null) {
            listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
        } else {
            throw new SquareException();
        }
    }

    /**
     * Function that occupies the Square if it's occupied only by the opponent
     * pieces then adds to the list of moves that avaliable for the pawn to
     * capture diagonally one square of the opponent piece.
     *
     * @param mat the current game matrix
     * @param listMoves the list of moves the current piece can move
     * @param row is the next row for all different type of moves belongs to
     * unique piece
     * @param col is the next colum for all different type of moves belongs to
     * unique piece
     * @throws SquareException that stops when the piece that occupies the
     * square
     */
    private void confirmOccupyEnemySquare(Piece[][] mat, ArrayList<Move> listMoves,
            int row, int col, Avaliability mode) throws SquareException {
        switch (mode) {
            case CONTROL:
                listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                break;
            case MOVE:
                if (mat[row][col] != null) {
                    if (mat[row][col].getSign() == super.getOppositeSign()) {
                        listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                    }
                } else {
                    throw new SquareException();
                }
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
     * @param which defines if the square should be avaliable only if it's empty
     * or only if enemy piece is located there
     * @return returns if the piece stands in front of the pawn
     */
    private boolean occupyToListPawn(Piece mainPiece, Piece[][] mat,
            ArrayList<Move> listMoves, int rowStep, int colStep, boolean which, Avaliability mode) {
        try {
            if (which) {
                confirmOccupyEmptySquare(mat, listMoves, mainPiece.getRow() + rowStep, mainPiece.getCol() + colStep);
                return true;
            } else {
                confirmOccupyEnemySquare(mat, listMoves, mainPiece.getRow() + rowStep, mainPiece.getCol() + colStep, mode);
            }
        } catch (SquareException | ArrayIndexOutOfBoundsException e) {
        }
        return false;
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
        switch (this.colorSide) {
            case Const.PLAYER_WHITE:
                return this.indexRow - 1 == s.row
                        && (this.indexCol - 1 == s.col || this.indexCol + 1 == s.col);
            case Const.PLAYER_BLACK:
                return this.indexRow + 1 == s.row
                        && (this.indexCol - 1 == s.col || this.indexCol + 1 == s.col);
        }
        return false;
    }

    /**
     * Function that sets if the pawn is promoted
     *
     * @param destinationMove the selected piece of the move
     * @return returns if the pawn is promoted
     */
    public boolean inPromotion(Square destinationMove) {
        if (this.getSign() == Const.PLAYER_WHITE && destinationMove.getRow() == Const.LAST_ROW) {
            return true;
        }
        return this.getSign() == Const.PLAYER_BLACK && destinationMove.getRow() == Const.FIRST_ROW;
    }

    /**
     * Give access to the value of the piece openly. mostly used for game
     * explanation or computer evaluation.
     *
     * @return The value of the piece
     */
    @Override
    public int getValuePiece() {
        return Const.PAWN;
    }

    /**
     * Prints the tag of the Piece the location of it in the board and the value
     * of it. (The Piece Sign)(The Piece Tag) The tag explains what type of
     * piece the Pawn is.
     */
    @Override
    public void toStringPiece() {
        super.toStringPiece();
        System.out.print(Const.TAG_PAWN);
    }
}
