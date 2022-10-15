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
public class Queen extends Piece{
    
//    Default attributes
//    =====================================================
    public static final int VALUE = 9;

//    Unique veriables of the class
//    =====================================================

//    ===================Constructors========================
    public Queen(int row, int col, int type) {
        super(row, col, type);
    }
    

//    ====================Functions===========================
    @Override
    public ArrayList<Move> getDestinationList(Model logicModel){
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listMoves = super.getDestinationList(logicModel);
        for (int indexR = -1; indexR < 2; indexR++) {
            for (int indexC = -1; indexC < 2; indexC++) {
                super.occupyOrderToList(this, mat, listMoves, indexR, indexC, Avaliability.MOVE);
            }
        }
        return listMoves;
    }
    
    
    @Override
    public ArrayList<Move> getControllingList(Model logicModel){
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listControl = new ArrayList<>();
        for (int indexR = -1; indexR < 2; indexR++)
            for (int indexC = -1; indexC < 2; indexC++)
                super.occupyOrderToList(this, mat, listControl, indexR, indexC, Avaliability.CONTROL);
        
        for (int i = 0; i < listControl.size(); i++) 
            if (listControl.get(i).getDestinationSquare().equals(new Square(this.indexRow, this.indexCol)))
                listControl.remove(i);
        return listControl;
    }
    
    @Override
    protected boolean isControllingSquare(Square s){
        return (Math.abs(s.indexRow - this.indexRow) == Math.abs(s.indexCol - this.indexCol)) ||
                ((this.indexRow == s.indexRow) || (this.indexCol == s.indexCol));
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
//            for (int indexR = -1; indexR < 2; indexR++)
//                for (int indexC = -1; indexC < 2; indexC++)
//                    super.occupyEliminationToList(this, mat, list, indexR, indexC);
//        
//            for (int i = 0; i < list.size(); i++) 
//                if (list.get(i).equals(new Square(this.indexRow, this.indexCol)))
//                    list.remove(i);
//        }
//        return list;
//    }
//    
    
//    @Override
//    public ArrayList<Square> getDestinationList(Square[][] mat){
//        int index;
//        ArrayList<Square> listMoves = new ArrayList<>();
//        ArrayList<Square> bishop = (new Bishop(this.indexRow, this.indexCol, this.theType)).getDestinationList(mat);
//        ArrayList<Square> rook = (new Rook(this.indexRow, this.indexCol, this.theType)).getDestinationList(mat);
//        for (index = 0; index < bishop.size() ; index++)
//            listMoves.add(bishop.get(index));
//        for(index = 0; index < rook.size(); index++)
//            listMoves.add(rook.get(index));
//        return listMoves;
//    }
    
    
    @Override
    public int getValuePiece(){
        return super.getValuePiece() + QUEEN;
    }
    
    @Override
    public ImageIcon getCurrentPieceIcon(){
        switch(this.theType){
            case Piece.PLAYER_WHITE:
                return Utils.WQueen;
            case Piece.PLAYER_BLACK:
                return Utils.BQueen;
        }
        return null;
    }
    
    @Override
    public void toStringPiece()
    {
        super.toStringPiece();
        System.out.print(Model.QUEEN);
    }
    
    @Override
    public String getStringPiece() {
        return super.getStringPiece() + Model.QUEEN;
    }
}
