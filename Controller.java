/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;


/**
 * Game: Chess game with a smart computer. Phones: 0543550123 Email:
 * mazarariel@gmail.com
 *
 * @author Ariel
 */
public class Controller {
//    The Default Components
//    ==============================================================

    public static final int DEFAULT_BOARD_SIZE = 8;

//  Global veriables used for logic for the game  
//    ===============================================================
    static int boardSize;
    static boolean isMove;
    static boolean isHumanPlayerFirst;
    static View guiView;
    static Model logicModel;

    public static void main(String[] args) {
//            Loads all the graphics of the game
        Utils.loadAssets();

//            Takes the size of the board into a veriable
        boardSize = DEFAULT_BOARD_SIZE;
        isMove = false;
        isHumanPlayerFirst = true;

//            Creation of the visual game and logic
        guiView = new View(boardSize);
        logicModel = new Model(boardSize);

//            Initalizing the game
        initGame();
    }

    /**
     * Loads the Logic and visuals of the game
     */
    public static void initGame() {
        logicModel.initLogicModel();
        guiView.initGameGUI();
        isMove = false;
        if (!isHumanPlayerFirst) {
            computerMove();
        }
    }

    /**
     * Action that closes the window if needed.
     */
    public static void closeAndExitGame() {
        guiView.close();
        System.exit(0);
    }

    /**
     * Function that returns the current piece been selected with these
     * coordinates. While also which Piece piece belongs
     *
     * @param indexRow the coordinates of the index row in the board
     * @param indexCol the coordinates of the index in the board
     * @return returns the Piece
     */
    public static Piece getSquare(int indexRow, int indexCol) {
        return logicModel.getCoordinatedPiece(indexRow, indexCol);
    }

    /**
     * Function that returns boolean value if the piece that has been clicked
     * belongs to the owner in the game
     *
     * @param indexRow the index of the board where the button was clicked in
     * the row
     * @param indexCol the index of the board where the button was clicked in
     * the colum
     * @return returns true if the piece belongs to owner and false if it's
     * empty or enemy piece
     */
    public static boolean isSelectedPiece(int indexRow, int indexCol) { //Had to add an if when the square is null
        Piece temporary = logicModel.getCoordinatedPiece(indexRow, indexCol);
        return temporary != null ? temporary.getType() == logicModel.getCurrentTurn() : false;
    }

    /**
     * Second part of the Process of doing the move The Piece selects the place
     * where he wants to move the piece he chose.
     *
     * @param indexRow index row where the piece located in the board
     * @param indexCol index colum where the piece located in the board
     */
    public static void enableMovement(int indexRow, int indexCol) {
        if (isMove) {
            int row = logicModel.getSelectedPiece().getRow();
            int col = logicModel.getSelectedPiece().getCol();
            if (logicModel.isMovePossible(indexRow, indexCol)) { //checks if the move been selected is possible
                System.out.println("\n\n\n===================Got to second===================");
                if (logicModel.isPromoted(indexRow)) {
                    guiView.showPromotionDialog();
                }
                logicModel.doHumanMove(indexRow, indexCol);
                guiView.switchPlayerTextTurn();
                guiView.setButtonIconAt(logicModel.getSelectedPiece());
                guiView.setButtonIconAt(row, col);
                
                if (!logicModel.isDeletedMovesEmpty()) 
                    guiView.unlockUndoButton();
                else
                    guiView.lockUndoButton();
                
                checkGameOver();
            }
        }
        isMove = false;
    }

    /**
     * Function that has been called in view to do the move
     *
     * @param indexRow the row square of the board
     * @param indexCol the colum square of the board
     */
    public static void humanMove(int indexRow, int indexCol) {
        guiView.setHighlightOff();
        if (isSelectedPiece(indexRow, indexCol)) {
            logicModel.setSelectedPiece(indexRow, indexCol);
            guiView.highlightSelectedPiece(logicModel.getSelectedPiece().getSquare());
            guiView.setHighlightOn(logicModel.getAllPieceSquares(logicModel.getSelectedPiece()));
            isMove = true;
        } else {
            enableMovement(indexRow, indexCol);
        }
    }
    
    /**
     * Function handles if you press the computer move the computer will play
     */
    public static void computerMove(){
        Move compMove = logicModel.doComputerMove();
        if (logicModel.isPromoted(compMove.getPiece().getRow())) {
            logicModel.setSelectedPiece(compMove.getPiece());
            promotePiece(Model.QUEEN);
        }
        
        guiView.switchPlayerTextTurn();
        guiView.setButtonIconAt(compMove.getPiece());
        guiView.setButtonIconAt(compMove.getDestinationSquare().indexRow, compMove.getDestinationSquare().getCol());

        if (!logicModel.isDeletedMovesEmpty()) 
            guiView.unlockUndoButton();
        else
            guiView.lockUndoButton();
        checkGameOver();
    }
    
    /**
     * Button and handles the undoButton which undoes the previous button
     */
    public static void undoButton() {
        guiView.setHighlightOff();
        Move tmp = logicModel.undoHumanMove();
        guiView.setButtonIconAt(tmp.getPiece());
        if (tmp.getCapturedPiece() != null) 
            guiView.setButtonIconAt(tmp.getCapturedPiece());
        else
            guiView.setButtonIconAt(tmp.getDestinationSquare().indexRow, tmp.getDestinationSquare().getCol());
        if (!logicModel.isDeletedMovesEmpty()) 
            guiView.unlockUndoButton();
        else
            guiView.lockUndoButton();
        guiView.switchPlayerTextTurn();
    }

    /**
     * Function that checks if the game is over
     */
    public static void checkGameOver(){
        if (logicModel.isGameOver()) {
            if (logicModel.checkForWin()) {
                guiView.changeTextLabelToWinner(logicModel.getCurrentTurn());
                guiView.lockAllBoardButtons();
            } else {
                guiView.changeTextLabelToTie();
                guiView.lockAllBoardButtons();
            }
            guiView.lockUndoButton();
        }
    }
    
    /**
     * Function that returns the turn of the player
     *
     * @return the turn of the player
     */
    public static int getTurn() {
        return logicModel.getCurrentTurn();
    }
    
    

    /**
     * Function that promotes the pawn to a piece
     *
     * @param newPiece which piece has been picked
     */
    public static void promotePiece(char newPiece) {
        try {
            logicModel.createNewPiece(newPiece);

        } catch (SquareException ex) {
            System.out.println("The Piece is not valid " + ex.getMessage());
        }
    }

    public static void setStartPlayer(boolean whoStarts) {
        isHumanPlayerFirst = whoStarts;
    }
}
