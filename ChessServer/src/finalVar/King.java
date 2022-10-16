package finalVar;

import java.util.ArrayList;

/**
 * The point of the Class is the represent the king that is located on the
 * board. A King is a chess piece that can only move one square in all
 * directions on the board. The king is the most valuable piece in the game and
 * determines if the player loses or wins, by the rules the can't cannot be
 * captured.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class King extends Piece {
//    Default attributes
//    ===========================================================

    public static final int STEP = 1;

//=============================================================
    /**
     * The constructor of the king class, which basically means to create a body
     * for the king piece. The king constructor just gives the Piece class
     * unique values and functions that represents the king.
     *
     * @param row Which row location that the piece is in the board currently.
     * @param col Which colum location that the piece is the board currently.
     * @param type Variable to define on which player the piece belongs to. The
     * White pieces or Black pieces.
     */
    public King(int row, int col, int type) {
        super(row, col, type);
//        this.inCheck = false;
    }

//    Functions
//    =============================================================
    /**
     * The main idea of it to calculate and convert all squares a piece can move
     * to. In this sense as we are inside the king piece class we get out of the
     * function the list of moves. List of Moves that indicate what e king on
     * the board can move to. However, this function only calculates the squares
     * the king potentially can move to. It also take in a count the position of
     * the pieces located on the board as e king cannot move past pieces.
     *
     * @param gLogic A variable that contains a model of the game to help
     * calculate the king squares it can move.
     * @return a list of moves that shows where e king can move.
     */
    @Override
    public ArrayList<Move> getDestinationList(Model gLogic) {
        Piece[][] mat = gLogic.getMatrix();
        ArrayList<Move> listKingMoves = new ArrayList<>();
        ArrayList<Piece> listEnemyPieces = gLogic.getListPieces(this.getOppositeSign());
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
                    ArrayList<Move> controlList = tmp.getControllingList(gLogic);
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
        return mainList;
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
     * calculate the Kings squares it can move.
     * @return a list of moves that shows where a King can control however
     * potentially not move to if the opponent piece control the game squares.
     */
    @Override
    public ArrayList<Move> getControllingList(Model gLogic) {
        ArrayList<Move> listMoves = new ArrayList<>();
        for (int indexR = -1; indexR < 2; indexR++) {
            for (int indexC = -1; indexC < 2; indexC++) {
                super.occupyToList(this, gLogic.getMatrix(), listMoves, indexR, indexC, Avaliability.CONTROL);
            }
        }
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
        return s.getRow() <= this.indexRow + 1
                && s.getRow() >= this.indexRow - 1
                && s.getCol() <= this.indexCol + 1
                && s.getCol() >= this.indexCol - 1;
    }

    /**
     * Give access to the value of the piece openly. mostly used for game
     * explanation or computer evaluation.
     *
     * @return The value of the piece
     */
    @Override
    public int getValuePiece() {
        return Const.KING;
    }

    /**
     * Prints the tag of the Piece the location of it in the board and the value
     * of it. (The Piece Sign)(The Piece Tag) The tag explains what type of
     * piece it is.
     */
    @Override
    public void toStringPiece() {
        super.toStringPiece();
        System.out.print(Const.TAG_KING);
    }
}
