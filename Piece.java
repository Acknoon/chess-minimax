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
public abstract class Piece implements Comparable {
//    The Default Attributes for the Piece
//    =========================================================

    public static final int PAWN = 10;
    public static final int KNIGHT = 30;
    public static final int BISHOP = 40;
    public static final int ROOK = 50;
    public static final int QUEEN = 90;
    public static final int KING = 9999;

    public static final int PLAYER_WHITE = 1;
    public static final int PLAYER_BLACK = -1;

    public static enum Avaliability {
        MOVE, CONTROL
    };

//    The global veriables that are been used for the logic in the Object
//    =========================================================
    protected int indexRow;
    protected int indexCol;
    protected int theType;
    protected boolean isChecking;
    protected int indexOfList;
    protected int moveCounter;

    /**
     * A constructor that identifies the square that belongs to the owner
     *
     * @param row row location in the board
     * @param col colum location in the board
     * @param type what type of square it belongs to
     */
    public Piece(int row, int col, int type) {
        this.indexRow = row;
        this.indexCol = col;
        this.theType = type;
        this.isChecking = false;
        this.moveCounter = 0;
    }

    /**
     * Function that returns list of moves
     *
     * @param logicModel the reference to the model
     * @return a list of moves avaliable for the piece
     */
    public ArrayList<Move> getDestinationList(Model logicModel) {
        return new ArrayList<>();
    }

    /**
     * Function that occupies the Square if it's empty or if enemy piece is
     * located
     *
     * @param mat the current game matrix
     * @param listMoves the list of moves the current piece can move
     * @param row is the next row for all different type of moves belongs to
     * unique piece
     * @param col is the next colum for all different type of moves belongs to
     * unique piece
     * @param mode is an option by which what piece is piece can do move or
     * Control squares
     * @throws SquareException that stops when the piece that occupies the
     * square
     */
    protected void confirmOccupy(Piece[][] mat, ArrayList<Move> listMoves,
            int row, int col, Avaliability mode) throws SquareException {

        switch (mode) {
            case CONTROL:
                if (mat[row][col] == null || (mat[row][col] instanceof King && mat[row][col].getType() == getOppositeType())) {
                    listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                } else {
                    if(mat[row][col].getType() == this.theType)
                        listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                    throw new SquareException();
                }
            case MOVE:
                if (mat[row][col] == null) {
                    listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                } else {
                    if (mat[row][col].getType() == getOppositeType()) {
                        listMoves.add(new Move(this, new Square(row, col), mat[row][col]));
                    }
                    throw new SquareException();
                }
        }
    }

    /**
     * Function that fills a series of moves of the current piece this series is
     * could be infinite however it eventually stop up until it reaches another
     * piece or reaches the edge of the board
     *
     * @param mainPiece The current piece that has been selected
     * @param mat the board matrix in the game
     * @param listMoves list of moves that saves the moves that piece has to
     * move
     * @param rowStep current constant value that makes a series of moves by row
     * @param colStep current constant value that makes a series of moves by
     * colum
     * @param mode boolean value that asks the piece controls a list of moves or
     * moves it can move
     */
    protected void occupyOrderToList(Piece mainPiece, Piece[][] mat,
            ArrayList<Move> listMoves, int rowStep, int colStep, Avaliability mode) {
        int row = mainPiece.getRow(), col = mainPiece.getCol();
        try {
            for (;;) {
                row += rowStep;
                col += colStep;
                confirmOccupy(mat, listMoves, row, col, mode);
            }
        } catch (SquareException | ArrayIndexOutOfBoundsException e) {
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
     * @param mode Boolean factor that decides what type of squares the piece
     * controls or moves
     */
    protected void occupyToList(Piece mainPiece, Piece[][] mat,
            ArrayList<Move> listMoves, int rowStep, int colStep, Avaliability mode) {
        int row = mainPiece.getRow() + rowStep, col = mainPiece.getCol() + colStep;
        try {
            confirmOccupy(mat, listMoves, row, col, mode);
        } catch (SquareException | ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Boolean function that shows if the square is owned and returns the value
     *
     * @param mat the game matrix
     * @param row the row index of the matrix
     * @param col the col index of the matrix
     * @return true if the square is avaliable for the piece to move and returns
     * false if it's not
     */
    protected boolean isEnemyPiece(Piece[][] mat, int row, int col) {
        try {
            return (mat[row][col]).getType() == getOppositeType();
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Function that returns a list of squares a piece controls
     *
     * @param logicModel reference to the model
     * @return list of square the piece controls
     */
    public ArrayList<Move> getControllingList(Model logicModel) {
        return null;
    }

    /**
     * The Function that checks if the each piece on the board are able to
     * control the square, Despite being blocked by other pieces
     *
     * @param s The current square that the piece can reach
     * @return true if Any type of piece are able to reach that square and false
     * if not
     */
    protected boolean isControllingSquare(Square s) {
        return false;
    }

    /**
     * Function that sets the status of the piece if it's checking the enemy
     * king
     *
     * @param isCheck the status to change the piece status
     */
    public void setCheckStatus(boolean isCheck) {
        this.isChecking = isCheck;
    }

    /**
     * Function returns if the piece checks the king or not
     *
     * @return factor that decides if the piece checking the enemy king
     */
    public boolean isCheckingPiece() {
        return this.isChecking;
    }

    /**
     * sets the icon that located on the square
     *
     * @return the icon of that square
     */
    public ImageIcon getCurrentPieceIcon() {
        return null;
    }

    /**
     * Function that returns the Piece of the control that square
     *
     * @return the current player
     */
    public int getType() {
        return theType;
    }

    /**
     * Function that sets new Piece if it's white or black
     *
     * @param thePlayer the Piece that has been set
     */
    public void setType(int thePlayer) {
        this.theType = thePlayer;
    }

    /**
     * Function gets the row of the piece
     *
     * @return the colum piece located
     */
    public int getRow() {
        return indexRow;
    }

    /**
     * Function that sets the row of the piece
     *
     * @param indexRow new row
     */
    public void setRow(int indexRow) {
        this.indexRow = indexRow;
    }

    /**
     * Function gets the colum of the piece
     *
     * @return the colum piece located
     */
    public int getCol() {
        return indexCol;
    }

    /**
     * Function sets the colum of the piece
     *
     * @param indexCol new colum to set
     */
    public void setCol(int indexCol) {
        this.indexCol = indexCol;
    }

    /**
     * function returns the location of the piece on the board
     *
     * @return the square the piece located
     */
    public Square getSquare() {
        return new Square(this.indexRow, this.indexCol);
    }

    /**
     * Function that sends the opposite type of this Square
     *
     * @return the opposite or enemy of this type
     */
    public int getOppositeType() {
        return this.theType == PLAYER_WHITE ? PLAYER_BLACK : PLAYER_WHITE;
    }

    /**
     * Function that returns the value of the piece
     *
     * @return the value of piece
     */
    public int getValuePiece() {
        return 0;
    }

    /**
     * Prints the Sign of what currently located on the square
     */
    public void toStringPiece() {
        System.out.print(theType == -1 ? "-" : "+");
    }

    /**
     * Prints the coordinates of the piece
     */
    public void toStringCoordinates() {
        System.out.print("[" + indexRow + ", " + indexCol + "]");
    }

    /**
     * Sends Symbol of the Piece
     *
     * @return char of the piece
     */
    public String getStringPiece() {
        return theType == -1 ? "-" : "+";
    }

    /**
     * Sends the coordinates of location of the piece to be printed
     *
     * @return sends the row and col of the piece
     */
    public String getStringCoordinates() {
        return "[" + indexRow + ", " + indexCol + "]";
    }

    /**
     * Had no time okay?
     *
     * @param t object
     * @return something
     */
    @Override
    public int compareTo(Object t) {
        return 1;
    }
    
    

//    /**
//     * Function returns the moves you can move to, to be able to block the
//     * check, Or be able to capture the piece that is checking
//     *
//     * @param logicModel reference to the model
//     * @return list of squares where any piece can occupy to prevent check
//     */
//    public ArrayList<Square> getEliminationList(Model logicModel) {
//        ArrayList<Square> listMoves = new ArrayList<>();
//        listMoves.add(this.getSquare());
//        return listMoves;
//    }
//    /**
//     * Function role to reduce the pieces avaliable reducing squares of the
//     * piece to only protect the king of the game
//     *
//     * @param logicModel the model of the game
//     * @param listOccupy the list of avaliable squares the piece can go
//     * currently
//     */
//    protected void canEliminate(Model logicModel, ArrayList<Square> listOccupy) {
//        ArrayList<Piece> enemyPieces = logicModel.getListPieces(getOppositeType());
//        Piece checkingPiece = null;
//        boolean isDisturbed = false;
//        int amountCheckingPieces = 0;
//
//        if (logicModel.isCheck())
//        {
//            for (int enemyIndex = 0; enemyIndex < enemyPieces.size(); enemyIndex++) {
//                if (enemyPieces.get(enemyIndex).isCheckingPiece()) {
//                    if (amountCheckingPieces > 1) {
//                        break;
//                    }
//                    checkingPiece = enemyPieces.get(enemyIndex);
//                    amountCheckingPieces++;
//                }
//            }
//        }
//        if (amountCheckingPieces == 1) {
//            System.out.println("===========================================");
//            System.out.println("===========================================");
//            System.out.println(".                     Got to Elimination list");
//            System.out.println("===========================================");
//            System.out.println("===========================================");
//            System.out.println(checkingPiece.getEliminationList(logicModel));
//            for (int myList = 0; myList < listOccupy.size(); myList++) {
//                for (int i = 0; i < checkingPiece.getEliminationList(logicModel).size(); i++) {
//                    if (checkingPiece.getEliminationList(logicModel).get(i).equals(listOccupy.get(myList))) {
//                        isDisturbed = true;
//                    }
//                }
//                if (!isDisturbed) {
//                    listOccupy.remove(myList);
//                    isDisturbed = false;
//                }
//            }
//        }
//        if (amountCheckingPieces > 1) {
//            listOccupy.clear();
//        }
//    }
//    /**
//     * Function returns a list of moves avaliable for the piece to interrupt
//     * what it means is that the function sends the array list of square of
//     * which any piece that is avaliable to move there can block check to the
//     * king
//     *
//     * @param mainPiece is the piece attacking the king
//     * @param mat the board of the game
//     * @param listMoves the list of squares a piece can block the check
//     * @param rowStep the order of the squares
//     * @param colStep the order of the squares
//     */
//    protected void occupyEliminationToList(Piece mainPiece, Piece[][] mat,
//        ArrayList<Square> listMoves, int rowStep, int colStep)
//    {
//        int row = mainPiece.getRow(), col = mainPiece.getCol();
//        try {
//            for (;;) {
//                row += rowStep;
//                col += colStep;
//                isLocatedPiece(mat, row, col);
//            }
//        } catch (SquareException e) {
//            if (mat[row][col] instanceof King && mat[row][col].getType() == getOppositeType()) {
//                try {
//                    row = mainPiece.getRow();
//                    col = mainPiece.getCol();
//                    for (;;) {
//                        row += rowStep;
//                        col += colStep;
//                        confirmOccupy(mat, listMoves, row, col, Piece.CAN_MOVE);
//                    }
//                } catch (SquareException | ArrayIndexOutOfBoundsException ea) {
//                }
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//        }
//    }
//    
//     /**
//     * Function checks if there is possibly a check
//     *
//     * @param mainPiece
//     * @param mat
//     * @param rowStep the order of the squares
//     * @param colStep the order of the squares
//     * @return 
//     */
//    protected boolean isAttackingKing(Piece mainPiece,Piece[][] mat, int rowStep, int colStep)
//    {
//        int row = mainPiece.getRow(), col = mainPiece.getCol();
//        try {
//            for (;;) {
//                row += rowStep;
//                col += colStep;
//                isLocatedPiece(mat, row, col);
//            }
//        } catch (SquareException e) {
//            if (mat[row][col] instanceof King && mat[row][col].getType() == getOppositeType()){
//                mainPiece.isChecking = true;
//                return true;
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//        }
//        return false;
//    }
//    /**
//     * Function that searches the coordinates of the any piece on the board
//     *
//     * @param mainPiece the piece we search it's path to another piece
//     * @param mat the board of the game
//     * @param row the distance between the mainPiece to another piece with row
//     * index
//     * @param col the distance between the mainPiece to another piece with colum
//     * index
//     * @throws SquareException Whenever it encounters another piece on it's path
//     */
//    private void isLocatedPiece(Piece[][] mat, int row, int col)
//            throws SquareException {
//        if (mat[row][col] != null) {
//            throw new SquareException();
//        }
//    }

    /**
     * Function that gets the index of where it is in the list
     * @return 
     */
    public int getIndexOfList() {
        return indexOfList;
    }
    
    /**
     * Function that sets a new index of the list it's is in
     * @param indexOfList a new index
     */
    public void setIndexOfList(int indexOfList) {
        this.indexOfList = indexOfList;
    }
    
    /**
     * Function that adds one move to move counter
     */
    public void addMoveCounter(){
        this.moveCounter++;
    }
    /**
     * Function that subtracts a move that was taken back
     */
    public void subMoveCounter(){
        this.moveCounter--;
    }
    
    /**
     * Function that returns the amount of moves that have been made of this piece
     * @return 
     */
    public int getMoveCounter(){
        return this.moveCounter;
    }
}
