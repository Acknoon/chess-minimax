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
public class Rook extends Piece{
    
//    Default attributes
//    ========================================================
    public static final int VALUE = 5;

//    Unique veriables of the class
//    =======================================================
    

    
//    ========================Constructors======================
    public Rook(int row, int col, int type){
        super(row, col, type);
    }

//    ========================Functions===========================
    @Override
    public ArrayList<Move> getDestinationList(Model logicModel) {
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = super.getDestinationList(logicModel);
        
//            =====================Vertical Line====================
        super.occupyOrderToList(this, mat, listMoves, 1, 0, Avaliability.MOVE);
        super.occupyOrderToList(this, mat, listMoves, -1, 0, Avaliability.MOVE);

//            ===================Horizontal Line===================
        super.occupyOrderToList(this, mat, listMoves, 0, 1, Avaliability.MOVE);
        super.occupyOrderToList(this, mat, listMoves, 0, -1, Avaliability.MOVE);
        
        return listMoves;
    }

    @Override
    public ArrayList<Move> getControllingList(Model logicModel){
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = new ArrayList<>();
        
//            =====================Vertical Line====================
        super.occupyOrderToList(this, mat, listMoves, 1, 0, Avaliability.CONTROL);
        super.occupyOrderToList(this, mat, listMoves, -1, 0, Avaliability.CONTROL);

//            ===================Horizontal Line===================
        super.occupyOrderToList(this, mat, listMoves, 0, 1, Avaliability.CONTROL);
        super.occupyOrderToList(this, mat, listMoves, 0, -1, Avaliability.CONTROL);

        return listMoves;
    }
    
    @Override
    protected boolean isControllingSquare(Square s){
        return (this.indexRow == s.indexRow) || (this.indexCol == s.indexCol);
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
//        ArrayList<Square> listElimination = new ArrayList<>();
//        if (this.isCheckHappen) {
//            listElimination = super.getEliminationList(logicModel);
////            =====================Vertical Line====================
//            super.occupyEliminationToList(this, mat, listElimination, 1, 0);
//            super.occupyEliminationToList(this, mat, listElimination, -1, 0);
////            ===================Horizontal Line===================
//            super.occupyEliminationToList(this, mat, listElimination, 0, 1);
//            super.occupyEliminationToList(this, mat, listElimination, 0, -1);
//        }
//        return listElimination;
//    }
    
    @Override
    public int getValuePiece(){
        return super.getValuePiece() + ROOK;
    }
    
    @Override
    public ImageIcon getCurrentPieceIcon(){
        switch(this.theType){
            case Piece.PLAYER_WHITE:
                return Utils.WRook;
            case Piece.PLAYER_BLACK:
                return Utils.BRook;
        }
        return null;
    }
    
    @Override
    public void toStringPiece()
    {
        super.toStringPiece();
        System.out.print(Model.ROOK);
    }
    
    @Override
    public String getStringPiece() {
        return super.getStringPiece() + Model.ROOK;
    }
    
//    @Override
//    public ArrayList<Square> getDestinationList(Square[][] mat){
//        int boardSize = Controller.boardSize;
//        ArrayList<Square> listMoves = new ArrayList<>();
//        int row = this.indexRow, col = this.indexCol, index;
//        
////        ===============Vertical Line====================
//        for (index = 1; row+index < boardSize; index++) {
//            if(((Piece)mat[row+index][col]).getType() == this.theType)
//                break;
//            if(((Piece)mat[row+index][col]).getType() == this.getOppositeType()){
//                listMoves.add(new Square(row+index, col));
//                break;
//            }
//            listMoves.add(new Square(row+index, col));
//        }
//        for (index = 1; row-index >= 0; index++) {
//            if(((Piece)mat[row-index][col]).getType() == this.theType)
//                break;
//            if(((Piece)mat[row-index][col]).getType() == this.getOppositeType()){
//                listMoves.add(new Square(row-index, col));
//                break;
//            }
//            listMoves.add(new Square(row-index, col));
//        }
//        
//        
////        ===================Horizontal Line===================
//        for (index = 1; col+index < boardSize; index++) {
//            if(((Piece)mat[row][col+index]).getType() == this.theType)
//                break;
//            if(((Piece)mat[row][col+index]).getType() == this.getOppositeType()){
//                listMoves.add(new Square(row, col+index));
//                break;
//            }
//            listMoves.add(new Square(row, col+index));
//        }
//        for (index = 1; col-index >= 0; index++) {
//            if(((Piece)mat[row][col-index]).getType() == this.theType)
//                break;
//            if(((Piece)mat[row][col-index]).getType() == this.getOppositeType()){
//                listMoves.add(new Square(row, col-index));
//                break;
//            }
//            listMoves.add(new Square(row, col-index));
//        }
//        
//        return listMoves;
//    }
}
