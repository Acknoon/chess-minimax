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
public class Knight extends Piece{

//    Default attributes
//    ======================================================
    public static final int VALUE = 3;

//    Unique veriables of the class
//    ======================================================
    protected static enum KnightDirection {SOUTH, NORTH, EAST, WEST};
    
    
//    =====================Constructors=======================
    public Knight(int row, int col, int type){    
        super(row, col, type);
    }
    
    

//    =========================Functions=======================
    @Override
    public ArrayList<Move> getDestinationList(Model logicModel) {
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = super.getDestinationList(logicModel);
        
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
    
    @Override
    public ArrayList<Move> getControllingList(Model logicModel){
        Piece[][] mat = logicModel.getMatrix();
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
    
    @Override
    protected boolean isControllingSquare(Square s){
        return (s.indexRow >= this.indexRow - 2  && s.indexRow <= this.indexRow + 2) &&
                (s.indexCol >= this.indexCol - 2 && s.indexCol <= this.indexCol + 2);
    }

    
     @Override
    public int getValuePiece(){
        return super.getValuePiece() + KNIGHT;
    }
    
    @Override
    public ImageIcon getCurrentPieceIcon(){
        switch(this.theType){
            case Piece.PLAYER_WHITE:
                return Utils.WKnight;
            case Piece.PLAYER_BLACK:
                return Utils.BKnight;
        }
        return null;
    }
    
    @Override
    public void toStringPiece(){
        super.toStringPiece();
        System.out.print(Model.KNIGHT);
    }
    
    @Override
    public String getStringPiece() {
        return super.getStringPiece() + Model.KNIGHT;
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
}
