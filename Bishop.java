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
public class Bishop extends Piece {

//    Default attributes
//    ===========================================================
    public static final int VALUE = 4;

//    Unique veriables of the class
//    ==========================================================
    
    
    
    
//    =======================Constructors==========================
    public Bishop(int row, int col, int type) {
        super(row, col, type);
    }
    
//    Functions
//    ===================================================================
    @Override
    public ArrayList<Move> getDestinationList(Model logicModel) {
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = super.getDestinationList(logicModel);
        
//        ============================Main Diagnal===============================
        super.occupyOrderToList(this, mat, listMoves, 1, 1, Avaliability.MOVE);
        super.occupyOrderToList(this, mat, listMoves, -1, -1, Avaliability.MOVE);
//        ===========================Secondary Diagnal============================
        super.occupyOrderToList(this, mat, listMoves, 1, -1, Avaliability.MOVE);
        super.occupyOrderToList(this, mat, listMoves, -1, 1, Avaliability.MOVE);
        
        return listMoves;
    }
    
    

    @Override
    public ArrayList<Move> getControllingList(Model logicModel){
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listControl = new ArrayList<>();
        
//        ============================Main Diagnal===============================
        super.occupyOrderToList(this, mat, listControl, 1, 1, Avaliability.CONTROL);
        super.occupyOrderToList(this, mat, listControl, -1, -1, Avaliability.CONTROL);
//        ===========================Secondary Diagnal============================
        super.occupyOrderToList(this, mat, listControl, 1, -1, Avaliability.CONTROL);
        super.occupyOrderToList(this, mat, listControl, -1, 1, Avaliability.CONTROL);
        return listControl;
    }
    
    @Override
    protected boolean isControllingSquare(Square s){
        return Math.abs(s.indexRow - this.indexRow) == Math.abs(s.indexCol - this.indexCol);
    }
    
    
//    /**
//     * Function returns the moves you can move to, to be able to block the check,
//     * Or be able to capture the piece that is checking
//     * @param logicModel reference to the model
//     * @return list of squares where any piece can occupy to prevent check
//     */
//    @Override
//    public ArrayList<Square> getEliminationList(Model logicModel){
//        Piece[][] mat = logicModel.getMatrix();
//        ArrayList<Square> list = new ArrayList<>();
//        if (this.isCheckHappen) {
//            list = super.getEliminationList(logicModel);
////            ============================Main Diagnal===============================
//            super.occupyEliminationToList(this, mat, list, 1, 1);
//            super.occupyEliminationToList(this, mat, list, -1, -1);
////            ===========================Secondary Diagnal============================
//            super.occupyEliminationToList(this, mat, list, 1, -1);
//            super.occupyEliminationToList(this, mat, list, -1, 1);
//        }
//        
//        return list;
//    }
    
    @Override
    public int getValuePiece(){
        return super.getValuePiece() + BISHOP;
    }
    
    
    @Override
    public ImageIcon getCurrentPieceIcon() {
        switch (this.theType) {
            case Piece.PLAYER_WHITE:
                return Utils.WBishop;
            case Piece.PLAYER_BLACK:
                return Utils.BBishop;
        }
        return null;
    }

    @Override
    public void toStringPiece() {
        super.toStringPiece();
        System.out.print(Model.BISHOP);
    }
    
    @Override
    public String getStringPiece() {
        return super.getStringPiece() + Model.BISHOP;
    }
}
