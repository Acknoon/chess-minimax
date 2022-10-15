/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * The most Valuable Piece of the game The King represents the player if he gets
 * mated you lose the game
 *
 * @author Ariel
 */
public class King extends Piece {
//    Default attributes
//    ===========================================================

    public static final int STEP = 1;

//    Unique veriables of the class
//    ===========================================================
//    private boolean inCheck;
//    Constructors
//=============================================================
    public King(int row, int col, int type) {
        super(row, col, type);
//        this.inCheck = false;
    }

//    Functions
//    =============================================================
    @Override
    public ArrayList<Move> getDestinationList(Model logicModel) {
        Piece[][] mat = logicModel.getMatrix();
        ArrayList<Move> listKingMoves = super.getDestinationList(logicModel);
        ArrayList<Piece> listEnemyPieces = logicModel.getListPieces(this.getOppositeType());
        ArrayList<Move> mainList = new ArrayList<>();
        for (int indexR = -1; indexR < 2; indexR++) {
            for (int indexC = -1; indexC < 2; indexC++) {
                super.occupyToList(this, mat, listKingMoves, indexR, indexC, Avaliability.MOVE);
            }
        }
//        System.out.println(">>>>>"+listKingMoves);

        for (int i = 0; i < listKingMoves.size(); i++) {
            for (int j = 0; j < listEnemyPieces.size(); j++) {
                Piece tmp = listEnemyPieces.get(j);
                if ((tmp.isControllingSquare(listKingMoves.get(i).getDestinationSquare()))) {
                    ArrayList<Move> controlList = tmp.getControllingList(logicModel);
                    for (int k = 0; k < controlList.size(); k++) {
                        if (listKingMoves.get(i).getDestinationSquare().equals(controlList.get(k).getDestinationSquare())) {
                            break;
                        } else {
                            mainList.add(listKingMoves.get(i));
                            break;
                        }
                    }
                } else {
                    mainList.add(listKingMoves.get(i));
                    break;
                }
            }
        }

//        for (int i = 0; i < listEnemyPieces.size(); i++) {
//            Piece tmp = listEnemyPieces.get(i);
//            for (int k = 0; k < listKingMoves.size(); k++) {
//                if (tmp.isControllingSquare(listKingMoves.get(k).getDestinationSquare())) {
//                    ArrayList<Move> controlList = tmp.getDestinationList(logicModel);
//                    for (int j = 0; j < controlList.size(); j++) {
//                        if (listKingMoves.get(k).getDestinationSquare().equals(controlList.get(j).getDestinationSquare())) {
//                            listKingMoves.remove(k);
//                        }
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < listEnemyPieces.size(); i++) {
//            ArrayList<Move> controlList = listEnemyPieces.get(i).getControllingList(logicModel);
//            for (int j = 0; j < controlList.size(); j++) {
//                for (int k = 0; k < listKingMoves.size(); k++) {
//                    if (controlList.get(j).getDestinationSquare().equals(listKingMoves.get(k).getDestinationSquare())) {
//                        listKingMoves.remove(k);
//                    }
//                }
//            }
//        }
        return mainList;
    }

    @Override
    public ArrayList<Move> getControllingList(Model logicModel) {
        ArrayList<Move> listMoves = new ArrayList<>();
        for (int indexR = -1; indexR < 2; indexR++) {
            for (int indexC = -1; indexC < 2; indexC++) {
                super.occupyToList(this, logicModel.getMatrix(), listMoves, indexR, indexC, Avaliability.CONTROL);
            }
        }
        return listMoves;
    }

    @Override
    protected boolean isControllingSquare(Square s) {
//        return true;
        return s.getRow() <= this.indexRow + 1&&
                s.getRow() >= this.indexRow - 1 &&
                s.getCol() <= this.indexCol + 1 &&
                s.getCol() >= this.indexCol - 1;
    }

//    /**
//     * Function returns if the king is checked by enemy piece
//     *
//     * @return statement that says the king is checked or under attack
//     */
//    public boolean isChecked() {
//        return inCheck;
//    }
//
//    /**
//     * Function sets if the King under attack or NOT under attack
//     *
//     * @param isCheck boolean parameter that says if a piece attacks the King
//     */
//    public void setCheck(boolean isCheck) {
//        this.inCheck = isCheck;
//    }
    @Override
    public int getValuePiece() {
        return super.getValuePiece() + KING;
    }

    @Override
    public ImageIcon getCurrentPieceIcon() {
        switch (this.theType) {
            case Piece.PLAYER_WHITE:
                return Utils.WKing;
            case Piece.PLAYER_BLACK:
                return Utils.BKing;
        }
        return null;
    }

    @Override
    public void toStringPiece() {
        super.toStringPiece();
        System.out.print(Model.KING);
    }

    @Override
    public String getStringPiece() {
        return super.getStringPiece() + Model.KING;
    }

}
