/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Ariel
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
    @Override
    public ArrayList<Move> getDestinationList(Model logicModel)
    {
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = super.getDestinationList(logicModel);
        int row = this.indexRow;
        switch (this.theType) {
//            ============================White Pawns Movement============================
            case Piece.PLAYER_WHITE:
                if (occupyToListPawn(this, mat, listMoves, -1, 0, PAWN_MOVE, Avaliability.MOVE) 
                        && row == Square.SECOND_ROW)
                {
                    occupyToListPawn(this, mat, listMoves, -2, 0, PAWN_MOVE, Avaliability.MOVE);
                }

//                =======================Capture avaliable for white pawn=========================
                occupyToListPawn(this, mat, listMoves, -1, -1, PAWN_CAPTURE, Avaliability.MOVE);
                occupyToListPawn(this, mat, listMoves, -1, 1, PAWN_CAPTURE, Avaliability.MOVE);
                break;

//                ===========================Black Pawns Movement============================
            case Piece.PLAYER_BLACK:
                if (occupyToListPawn(this, mat, listMoves, 1, 0, PAWN_MOVE, Avaliability.MOVE) 
                        && row == Square.SEVENTH_ROW)
                {
                    occupyToListPawn(this, mat, listMoves, 2, 0, PAWN_MOVE, Avaliability.MOVE);
                }

//                =====================Capture avaliable for black pawns===========================
                occupyToListPawn(this, mat, listMoves, 1, -1, PAWN_CAPTURE, Avaliability.MOVE);
                occupyToListPawn(this, mat, listMoves, 1, 1, PAWN_CAPTURE, Avaliability.MOVE);
                break;
        }
        return listMoves;
    }
    
    @Override
    public ArrayList<Move> getControllingList(Model logicModel){
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();
        switch (this.theType) {
            case Piece.PLAYER_WHITE:
//                =======================Capture avaliable for white pawn=========================
                occupyToListPawn(this, mat, listMoves, -1, -1, PAWN_CAPTURE, Avaliability.CONTROL);
                occupyToListPawn(this, mat, listMoves, -1, 1, PAWN_CAPTURE, Avaliability.CONTROL);
                break;

            case Piece.PLAYER_BLACK:
//                =====================Capture avaliable for black pawns===========================
                occupyToListPawn(this, mat, listMoves, 1, -1, PAWN_CAPTURE, Avaliability.CONTROL);
                occupyToListPawn(this, mat, listMoves, 1, 1, PAWN_CAPTURE, Avaliability.CONTROL);
                break;
        }
        return listMoves;
    }

     /**
     * Function that occupies the Square if it's empty and not occupied by any
     * other piece
     *
     * @param mat the current game matrix
     * @param listMoves the list of moves the current piece can move
     * @param row is the next row for all different type of moves belongs to
     * unique piece
     * @param col is the next colum for all different type of moves belongs to
     * unique piece
     * @throws SquareException that stops when the piece that occupies
     * the square
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
     * pieces
     *
     * @param mat the current game matrix
     * @param listMoves the list of moves the current piece can move
     * @param row is the next row for all different type of moves belongs to
     * unique piece
     * @param col is the next colum for all different type of moves belongs to
     * unique piece
     * @throws SquareException that stops when the piece that occupies
     * the square
     */
    private void confirmOccupyEnemySquare(Piece[][] mat, ArrayList<Move> listMoves,
            int row, int col, Avaliability mode) throws SquareException
    {
        switch(mode){
            case CONTROL:
                listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                break;
            case MOVE:
                if(mat[row][col] != null){
                    if (mat[row][col].getType() == super.getOppositeType())
                        listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                } else
                    throw new SquareException();
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
            } else
                confirmOccupyEnemySquare(mat, listMoves, mainPiece.getRow() + rowStep, mainPiece.getCol() + colStep, mode);
        } catch (SquareException | ArrayIndexOutOfBoundsException e) {}
        return false;
    }
    
//    /**
//     * Function returns the moves you can move to, to be able to block the check,
//     * Or be able to capture the piece that is checking
//     * @param logicModel reference to the model
//     * @return list of squares where any piece can occupy to prevent check
//     */
//    @Override
//    public ArrayList<Square> getEliminationList(Model logicModel){
//        ArrayList<Square> list = null;
//        if (this.isCheckHappen)
//            list = super.getEliminationList(logicModel);
//        return list;
//    }

    @Override
    protected boolean isControllingSquare(Square s){
        switch(this.theType){
            case PLAYER_WHITE:
                return this.indexRow - 1 == s.indexRow &&
                        (this.indexCol - 1 == s.indexCol || this.indexCol + 1 == s.indexCol);
            case PLAYER_BLACK:
                return this.indexRow + 1 == s.indexRow &&
                        (this.indexCol - 1 == s.indexCol || this.indexCol + 1 == s.indexCol);
        }
        return false;
    }
    
    
    /**
     * Function that sets if the pawn is promoted
     *
     * @param selectedRow selected Row of the move
     * @return returns if the pawn is promoted
     */
    public boolean inPromotion(int selectedRow) {
        if (this.getType() == Piece.PLAYER_WHITE && selectedRow == Square.LAST_ROW)
            return true;
        return this.getType() == Piece.PLAYER_BLACK && selectedRow == Square.FIRST_ROW;
    }

    @Override
    public int getValuePiece(){
        return super.getValuePiece() + PAWN;
    }
    
    
    @Override
    public ImageIcon getCurrentPieceIcon() {
        switch (this.theType) {
            case Piece.PLAYER_WHITE:
                return Utils.WPawn;
            case Piece.PLAYER_BLACK:
                return Utils.BPawn;
        }
        return null;
    }

    @Override
    public void toStringPiece() 
    {
        super.toStringPiece();
        System.out.print(Model.PAWN);
    }
    
    @Override
    public String getStringPiece() {
        return super.getStringPiece() + Model.PAWN;
    }
}
