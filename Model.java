/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 *
 * @author Ariel
 */
public class Model {

//    The default attributes If I were to use The matrix with Char
//    ============================================
    public static final char PAWN = 'P';
    public static final char KNIGHT = 'N';
    public static final char BISHOP = 'B';
    public static final char ROOK = 'R';
    public static final char QUEEN = 'Q';
    public static final char KING = 'K';
    public static final char NO_PIECE = ' ';
    public static final long RUNTIME = 5 * 1000L;
    public static final int DEFAULT = 0;
    public static final int WIN_SCORE = 1000;

//      Logic Veriables
//    ============================================
    private Piece[][] matGame;
    private ArrayList<Piece> whitePieces, blackPieces;
    private Stack<Move> deletedMoves;
    private Piece selectedPiece;
    private boolean flagCheck;
    private int currentTurn;
    private int moveCounter;
//    private long time;
    private final int boardSize;

    /**
     * The Constructor of the Model in the game
     *
     * @param boardSize The Size of the board
     */
    public Model(int boardSize) {
        this.boardSize = boardSize;
    }

    /**
     * Function that resets the Logic of the game
     */
    public void initLogicModel() {
        matGame = new Piece[boardSize][boardSize];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        deletedMoves = new Stack<>();
        int indexRow, indexCol;
        for (indexRow = 0; indexRow < boardSize; indexRow++) {
            for (indexCol = 0; indexCol < boardSize; indexCol++) {
                fillMatrixPlayers(indexRow, indexCol);
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = matGame[DEFAULT][DEFAULT];
        currentTurn = Piece.PLAYER_WHITE;
        moveCounter = 0;
        sortPieceList(whitePieces);
        sortPieceList(blackPieces);
    }

    /**
     * Analyzing position from a different start
     */
    public void initLogicModel1() {
        matGame = new Piece[boardSize][boardSize];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        deletedMoves = new Stack<>();
        int indexRow, indexCol;
        matGame[3][3] = new King(3, 3, Piece.PLAYER_WHITE);
        matGame[4][5] = new Rook(4, 5, Piece.PLAYER_WHITE);
        matGame[6][6] = new King(6, 6, Piece.PLAYER_BLACK);

        for (indexRow = 0; indexRow < boardSize; indexRow++) {
            for (indexCol = 0; indexCol < boardSize; indexCol++) {
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = matGame[DEFAULT][DEFAULT];
        currentTurn = Piece.PLAYER_WHITE;
        moveCounter = 45;
        sortPieceList(whitePieces);
        sortPieceList(blackPieces);
    }

    public void initLogicModel2() {
        matGame = new Piece[boardSize][boardSize];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        deletedMoves = new Stack<>();
        int indexRow, indexCol;
        matGame[7][7] = new King(7, 7, Piece.PLAYER_WHITE);
        matGame[7][5] = new Rook(7, 5, Piece.PLAYER_WHITE);
        matGame[6][6] = new Pawn(6, 6, Piece.PLAYER_WHITE);
        matGame[5][7] = new Pawn(5, 7, Piece.PLAYER_WHITE);

        matGame[4][0] = new Rook(4, 0, Piece.PLAYER_BLACK);
        matGame[1][1] = new Pawn(1, 1, Piece.PLAYER_BLACK);
        matGame[1][7] = new Pawn(1, 7, Piece.PLAYER_BLACK);
        matGame[0][7] = new King(0, 7, Piece.PLAYER_BLACK);

        for (indexRow = 0; indexRow < boardSize; indexRow++) {
            for (indexCol = 0; indexCol < boardSize; indexCol++) {
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = matGame[DEFAULT][DEFAULT];
        currentTurn = Piece.PLAYER_WHITE;
        moveCounter = 10;
        sortPieceList(whitePieces);
        sortPieceList(blackPieces);
    }

    public void initLogicModel3() {
        matGame = new Piece[boardSize][boardSize];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        deletedMoves = new Stack<>();
        int indexRow, indexCol;
        matGame[7][7] = new King(7, 7, Piece.PLAYER_WHITE);
        matGame[6][6] = new Pawn(6, 6, Piece.PLAYER_WHITE);
        matGame[5][7] = new Pawn(5, 7, Piece.PLAYER_WHITE);

        matGame[1][1] = new Pawn(1, 1, Piece.PLAYER_BLACK);
        matGame[1][7] = new Pawn(1, 7, Piece.PLAYER_BLACK);
        matGame[0][7] = new King(0, 7, Piece.PLAYER_BLACK);

        for (indexRow = 0; indexRow < boardSize; indexRow++) {
            for (indexCol = 0; indexCol < boardSize; indexCol++) {
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = matGame[DEFAULT][DEFAULT];
        currentTurn = Piece.PLAYER_WHITE;
        moveCounter = 0;
        sortPieceList(whitePieces);
        sortPieceList(blackPieces);
    }

    public void initLogicModel4() {
        matGame = new Piece[boardSize][boardSize];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        deletedMoves = new Stack<>();
        int indexRow, indexCol;
        matGame[2][5] = new King(2, 5, Piece.PLAYER_WHITE);
        matGame[7][0] = new Rook(7, 0, Piece.PLAYER_WHITE);

        matGame[0][5] = new King(0, 5, Piece.PLAYER_BLACK);

        for (indexRow = 0; indexRow < boardSize; indexRow++) {
            for (indexCol = 0; indexCol < boardSize; indexCol++) {
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = matGame[DEFAULT][DEFAULT];
        currentTurn = Piece.PLAYER_WHITE;
        moveCounter = 0;
        sortPieceList(whitePieces);
        sortPieceList(blackPieces);
    }

    public void initLogicModel5() {
        matGame = new Piece[boardSize][boardSize];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        deletedMoves = new Stack<>();
        int indexRow, indexCol;
        matGame[2][4] = new King(2, 4, Piece.PLAYER_WHITE);
        matGame[3][7] = new Queen(3, 7, Piece.PLAYER_WHITE);

        matGame[0][5] = new King(0, 5, Piece.PLAYER_BLACK);

        for (indexRow = 0; indexRow < boardSize; indexRow++) {
            for (indexCol = 0; indexCol < boardSize; indexCol++) {
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = matGame[DEFAULT][DEFAULT];
        currentTurn = Piece.PLAYER_WHITE;
        moveCounter = 0;
        sortPieceList(whitePieces);
        sortPieceList(blackPieces);
    }

    /**
     * Function that does the Computer minimax move
     *
     * @return the move the minimax did
     */
    public Move doComputerMove() {
        long totalTime = 0, deadlineTime = RUNTIME;
        Move tempMove = null, compMove = null;
        int maxDepth = 1;
        do {
            System.out.println("///////////////////////////////////////////////////////Minimax Begun//////////////////////////////////////////////////");
            System.out.println("Depth: " + maxDepth);
            System.out.println("Time: " + totalTime);
            long timeStamp1 = System.currentTimeMillis();
            tempMove = miniMax(true, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, maxDepth, deadlineTime - totalTime);
            
            long timeStamp2 = System.currentTimeMillis();
            totalTime = totalTime + (timeStamp2 - timeStamp1);
            if (totalTime <= deadlineTime) 
            {
                System.out.println("Comp move - Depth: " + maxDepth);
                compMove = tempMove;
            }
            maxDepth++;
            System.out.println("----------------------------------------minimax finish-----------------------------------");
            System.out.println("minimax Time: " + (timeStamp2 - timeStamp1));
            System.out.println("depth: " + compMove.getDepth());
            System.out.println("move :" + compMove);
            //System.out.println(compMove.getPossibleMoves());
           
        } while (totalTime < deadlineTime /*|| !isGameOverMove(compMove)*/);

        System.out.println("=============================================donedone=======================================");
        System.out.println(compMove.getPossibleMoves());
        System.out.println("--------------------------------------------------------");
        System.out.println("Total time: " + totalTime);
        System.out.println("-------------------------------------------------------");
        excuteMove(compMove);
        deletedMoves.push(compMove);

        this.flagCheck = isCheckHappen(currentTurn);
        switchTurn();
        printPieces();
        printMatrix();

        return compMove;
    }

//    ==================================================
//    ==================================================
//    ===============The MiniMax Code=====================
//    ==================================================
//    ==================================================
    private Move miniMax(boolean isMax, double alpha, double beta, int depth, int maxDepth, long timeMs) {
        boolean isGameOver = isGameOver();
        if (isGameOver || maxDepth == 0 || timeMs <= 0) {
//            System.out.println("===================================================\n"
//                    + "============================================================\n"
//                    + "=============================================================");
//            printPieces();
//            printMatrix();
System.out.println("VVVVVVVVVVVVVVVVVVV terminal VVVVVVVVVVVVVVVVVVVV");
            System.out.println("isGameOver=" +isGameOver);
            System.out.println("timeMs="+timeMs);
            System.out.println("depth="+depth);
            System.out.println("maxDepth="+maxDepth);
            int score = getBoardScore(this.currentTurn);
            System.out.println("score="+score);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            return new Move(score, depth);
        }
        long time1, time2;
        time1 = System.currentTimeMillis();
        Move bestMove, tempMove;
        ArrayList<Move> possibleMoves = getAllPossibleMoves(isMax);
//        System.out.println(possibleMoves);
        

        if (isMax) {
            bestMove = new Move(Integer.MIN_VALUE);
            for (int i = 0; i < possibleMoves.size(); i++) {
                excuteMove(possibleMoves.get(i));
                checkOccurence(isMax);
                time2 = System.currentTimeMillis();
                timeMs = timeMs - (time2 - time1);
                tempMove = miniMax(false, alpha, beta, depth + 1, maxDepth - 1, timeMs);
                undoMove(possibleMoves.get(i));

                 if(depth == 0)
                {
                    System.out.println("move #" +(i+1));
                    System.out.println(possibleMoves.get(i));
                    System.out.println("score: " + tempMove.getScore());
                    
                }
                if (tempMove.getScore() > bestMove.getScore()) {
                    bestMove.setScore(tempMove.getScore());
                    bestMove.setDestinationSquare(possibleMoves.get(i).getDestinationSquare());
                    bestMove.setPiece(possibleMoves.get(i).getPiece());
                    bestMove.setCapturedPiece(possibleMoves.get(i).getCapturedPiece());
                    bestMove.setDepth(tempMove.getDepth());
                }
                
                possibleMoves.get(i).setScore(tempMove.getScore());
                
                
                alpha = Math.max(bestMove.getScore(), alpha);
                if (beta <= alpha) // <==> if(bestState.getScore() >= b)
                    break;
            }
        } else {
            bestMove = new Move(Integer.MAX_VALUE);
            for (int i = 0; i < possibleMoves.size(); i++) {
                excuteMove(possibleMoves.get(i));
                checkOccurence(isMax);
                time2 = System.currentTimeMillis();
                timeMs = timeMs - (time2 - time1);
                tempMove = miniMax(true, alpha, beta, depth + 1, maxDepth - 1, timeMs);
                undoMove(possibleMoves.get(i));

                if (tempMove.getScore() < bestMove.getScore()) {
                    bestMove.setScore(tempMove.getScore());
                    bestMove.setDestinationSquare(possibleMoves.get(i).getDestinationSquare());
                    bestMove.setPiece(possibleMoves.get(i).getPiece());
                    bestMove.setCapturedPiece(possibleMoves.get(i).getCapturedPiece());
                    bestMove.setDepth(tempMove.getDepth());
                }
               
                possibleMoves.get(i).setScore(tempMove.getScore());
                beta = Math.min(bestMove.getScore(), beta);
                if (beta <= alpha) // <==> if(bestState.getScore() <= a)
                    break;
            }
        }
        bestMove.setPossibleMoves(possibleMoves);
        return bestMove;
    }

    /**
     * Function that returns number the computer thinks of the position
     *
     * @param turn which turn to said to move
     * @return a number of the valuated position
     */
    private int getBoardScore(int turn) {
        if (checkForWin(getOppositeTurn(turn))) {
            return WIN_SCORE;
        }

        if (checkForWin(turn)) {
            return WIN_SCORE * (-1);
        }

        if (checkForDraw()) {
            return 0; // A tie!
        }
        int score1 = getMaterialValue(turn);
        int score2 = getMaterialValue(getOppositeTurn(turn));
        return score1 - score2;
    }

    /**
     * Function calculates the value of all pieces that player has
     *
     * @param player the player to calculate the score
     * @return value of the score
     */
    private int getMaterialValue(int player) {
        int score = 0;
        if (player == Piece.PLAYER_WHITE) {
            for (int i = 1; i < whitePieces.size(); i++) {
                score += whitePieces.get(i).getValuePiece();
            }
        } else {
            for (int i = 1; i < blackPieces.size(); i++) {
                score += blackPieces.get(i).getValuePiece();
            }
        }
        return score;
    }

    
//    private boolean isGameOverMove(Move move){
//        return move.getScore() == WIN_SCORE;
//    }
    
    /**
     * Function that does the human move and updates the model
     *
     * @param selectedR gets the coordinates of square row been selected
     * @param selectedC gets the coordinates of square colum been selected
     */
    public void doHumanMove(int selectedR, int selectedC) {
        Move move = newMove(selectedR, selectedC);

        excuteMove(move);
//        selectedPiece.setRow(selectedR);
//        selectedPiece.setCol(selectedC);
        deletedMoves.push(move);

        this.flagCheck = isCheckHappen(currentTurn);
        switchTurn();

        printPieces();
        printMatrix();
        ArrayList<Move> possibleMoves = getAllPossibleMoves(currentTurn);
        System.out.println(possibleMoves);
        System.out.println(possibleMoves.size());
    }

    /**
     * Function that returns the last move made and undoes the move in the main
     * matrix
     *
     * @return the undo move
     */
    public Move undoHumanMove() {
        Move value = null;
        if (!deletedMoves.isEmpty()) {
            value = deletedMoves.pop();
            undoMove(value);
            this.flagCheck = isCheckHappen(currentTurn);
            switchTurn();
        }
        return value;
    }

    /**
     * Function returns if the any move has been played
     *
     * @return if the list of deleted moves empty or not
     */
    public boolean isDeletedMovesEmpty() {
        return deletedMoves.isEmpty();
    }

    /**
     * Function that moves the piece been selected to the coordinates been told
     *
     * @param move is the move to be played, contains the piece that can move to
     * a location
     */
    public void excuteMove(Move move) {
        Square locationMove = move.getDestinationSquare();
        Square s = new Square(move.getPiece().getRow(), move.getPiece().getCol());
        matGame[move.getPiece().getRow()][move.getPiece().getCol()] = null;
        matGame[locationMove.getRow()][locationMove.getCol()] = move.getPiece();
        matGame[locationMove.getRow()][locationMove.getCol()].setRow(locationMove.getRow());
        matGame[locationMove.getRow()][locationMove.getCol()].setCol(locationMove.getCol());
        locationMove.setSquare(s);
        removePiece(move.getCapturedPiece());
        move.getPiece().addMoveCounter();
        if (!(selectedPiece instanceof Pawn) && move.getCapturedPiece() == null) {
            moveCounter++;
        } else {
            moveCounter = 0;
        }

    }

    /**
     * Function that reverts the previous move that has happened
     *
     * @param move the last move
     */
    public void undoMove(Move move) {
        Square locationMove = move.getDestinationSquare();
        Square s = new Square(move.getPiece().getRow(), move.getPiece().getCol());
        matGame[move.getPiece().getRow()][move.getPiece().getCol()] = move.getCapturedPiece();
        matGame[locationMove.getRow()][locationMove.getCol()] = move.getPiece();
        matGame[locationMove.getRow()][locationMove.getCol()].setRow(locationMove.getRow());
        matGame[locationMove.getRow()][locationMove.getCol()].setCol(locationMove.getCol());
        locationMove.setSquare(s);
        addPiece(move.getCapturedPiece());
        move.getPiece().subMoveCounter();
        if (!(selectedPiece instanceof Pawn) && move.getCapturedPiece() == null) {
            moveCounter--;
        } else {
            moveCounter = 0;
        }

    }

//    /**
//     * Sets the flagCheck flag up when there is a checking piece
//     *
//     * @param player which player piece are checking
//     * @return true if there is a flagCheck happens to the king and false if it
//     * doesn't happen
//     */
//    public boolean isCheckHappening(int player) {
//        if (player == Piece.PLAYER_WHITE) {
//            for (int i = 0; i < whitePieces.size(); i++) {
//                if (whitePieces.get(i).isCheckingPiece()) {
//                    return true;
//                }
//            }
//        } else {
//            for (int i = 0; i < blackPieces.size(); i++) {
//                if (blackPieces.get(i).isCheckingPiece()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    /**
     * Updates the pieces check status
     *
     * @param turn which player piece are checking
     * @return returns if a check happens or not
     */
    public boolean isCheckHappen(int turn) {
        boolean isGivingCheck = false, checkHappen = false;
        ArrayList<Move> list;

        if (turn == Piece.PLAYER_WHITE) {
            for (int i = 0; i < whitePieces.size(); i++) {
                if (whitePieces.get(i).isControllingSquare(getKing(getOppositeTurn(turn)).getSquare())) {
                    list = whitePieces.get(i).getControllingList(this);
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getDestinationSquare().equals(getKing(getOppositeTurn(turn)).getSquare())) {
                            isGivingCheck = true;
                            checkHappen = true;
                            whitePieces.get(i).setCheckStatus(true);
                            break;
//                        System.out.println("I changed this Piece check status");
//                        whitePieces.get(i).toStringPiece();
//                        System.out.println(whitePieces.get(i).isCheckingPiece());
                        }
                    }
                }
                if (!isGivingCheck) {
                    whitePieces.get(i).setCheckStatus(false);
                }
                isGivingCheck = false;
            }
        } else {
            for (int i = 0; i < blackPieces.size(); i++) {
                if (blackPieces.get(i).isControllingSquare(getKing(getOppositeTurn(turn)).getSquare())) {
                    list = blackPieces.get(i).getControllingList(this);
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getDestinationSquare().equals(getKing(getOppositeTurn(turn)).getSquare())) {
                            isGivingCheck = true;
                            checkHappen = true;
                            blackPieces.get(i).setCheckStatus(true);
                            break;
//                        System.out.println("I changed this Piece check status");
//                        blackPieces.get(i).toStringPiece();
//                        System.out.println(blackPieces.get(i).isCheckingPiece());
                        }
                    }
                }
                if (!isGivingCheck) {
                    blackPieces.get(i).setCheckStatus(false);
                }
                isGivingCheck = false;
            }
        }
        return checkHappen;
    }

    /**
     * Function that updates the piece check status that happens on the board.
     *
     * @param isMax Which player is it
     */
    public void checkOccurence(boolean isMax) {
        if (isMax) {
            this.flagCheck = isCheckHappen(currentTurn);
        } else {
            this.flagCheck = isCheckHappen(getOppositeTurn());
        }
    }

    /**
     * Function checks if the game is over
     *
     * @return true if the game is over and false if the game still continues
     */
    public boolean checkForWin() {
        if (flagCheck) {
            if (getAllPossibleMoves(true).isEmpty() || getAllPossibleMoves(false).isEmpty()) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * Function checks if the game is a tie
//     * @return true if the game is a tie and false if the game still continues
//     */
//    public boolean checkForTie(){
//        if (flagCheck == false) {
//            if (getAllPossibleMoves(true).isEmpty()) {
//                return true;
//            }
//            if (whitePieces.size() == 1 && blackPieces.size() == 1) {
//                return true;
//            }
//            if (moveCounter == 50) {
//                return true;
//            }
//        }
//        return false;
//    }
    /**
     * Function checks if the game is over
     *
     * @param turn turn currently given
     * @return true if the game is over and false if the game still continues
     */
    public boolean checkForWin(int turn) {
        if (flagCheck) {
            if (getAllPossibleMoves(turn).isEmpty()) {
                return true;
            }
        }
//        if (getKing(getOppositeTurn(turn)) == null) {
//            return true;
//        }
        return false;
    }

    /**
     * Function checks if the game is a tie
     *
     * @return true if the game is a tie and false if the game still continues
     */
    public boolean checkForDraw() {
        if (flagCheck == false) {
            if (getAllPossibleMoves(currentTurn).isEmpty() || getAllPossibleMoves(getOppositeTurn()).isEmpty()) {
                return true;
            }
            if (whitePieces.size() == 1 && blackPieces.size() == 1) {
                return true;
            }
            if (moveCounter == 50) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return checkForWin() || checkForDraw();
    }
    

    /**
     * Function returns array of legal moves the piece can do
     *
     * @param currentPiece the current type Piece
     * @return list of legal Moves
     */
    public ArrayList<Move> getAvaliableMovesOfPiece(Piece currentPiece) {
        ArrayList<Move> listViableMoves = new ArrayList<>();
        ArrayList<Move> list = currentPiece.getDestinationList(this);
        for (int i = 0; i < list.size(); i++) {
            excuteMove(list.get(i));
//            System.out.println("======================before Undo=================");
//            printMatrix();
            if (!isCheckHappen(getOppositeTurn(currentPiece.getType()))) {
                listViableMoves.add(list.get(i));
            }
            undoMove(list.get(i));
//            System.out.println("===================after Undo==================");
//            printMatrix();
        }
        return listViableMoves;
    }

    public ArrayList<Square> getAllPieceSquares(Piece currentPiece) {
        ArrayList<Move> list = getAvaliableMovesOfPiece(currentPiece);
        ArrayList<Square> mainList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mainList.add(list.get(i).getDestinationSquare());
        }
        return mainList;
    }

    /**
     * Creates a move with the piece that has been selected by a player
     *
     * @param selectedR the destination where the piece can move
     * @param selectedC
     * @return
     */
    public Move newMove(int selectedR, int selectedC) {
        if (matGame[selectedR][selectedC] != null) {
            return new Move(selectedPiece, new Square(selectedR, selectedC), matGame[selectedR][selectedC]);
        }
        return new Move(selectedPiece, new Square(selectedR, selectedC));
    }

    /**
     * Boolean function that checks if the selected square is where the piece
     * can move to
     *
     * @param selectedRow the selected row of the Square
     * @param selectedCol the selected col of the Square
     * @return returns true if the square is reachable by the piece
     */
    public boolean isMovePossible(int selectedRow, int selectedCol) {
        ArrayList<Move> listMoves = selectedPiece.getDestinationList(this);
        for (int index = 0; index < listMoves.size(); index++) {
            if (listMoves.get(index).getDestinationSquare().equals(new Square(selectedRow, selectedCol))) {
                excuteMove(listMoves.get(index));
                if (!isCheckHappen(getOppositeTurn())) {
                    undoMove(listMoves.get(index));
                    return true;
                }
                undoMove(listMoves.get(index));
            }
        }
        return false;
    }

    /**
     * Function that makes a least of all Moves a piece can move to
     *
     * @param isMax which player is it
     * @return list of moves
     */
    public ArrayList<Move> getAllPossibleMoves(boolean isMax) {
        ArrayList<Move> list = new ArrayList<>();
        ArrayList<Piece> listPieces;
        if (isMax) {
            listPieces = getListPieces(currentTurn);
        } else {
            listPieces = getListPieces(getOppositeTurn());
        }

        for (int indexPiece = 0; indexPiece < listPieces.size(); indexPiece++) {
            ArrayList<Move> listMoves = getAvaliableMovesOfPiece(listPieces.get(indexPiece));
            for (int index = 0; index < listMoves.size(); index++) {
                list.add(listMoves.get(index));
            }
        }
        return list;
    }

    /**
     * Function that makes a least of all Moves a piece can move to
     *
     * @param player which player is it
     * @return list of moves
     */
    public ArrayList<Move> getAllPossibleMoves(int player) {
        ArrayList<Move> list = new ArrayList<>();
        ArrayList<Piece> listPieces;
        if (player == Piece.PLAYER_WHITE) {
            listPieces = whitePieces;
        } else {
            listPieces = blackPieces;
        }

        for (int indexPiece = 0; indexPiece < listPieces.size(); indexPiece++) {
            ArrayList<Move> listMoves = getAvaliableMovesOfPiece(listPieces.get(indexPiece));
            for (int index = 0; index < listMoves.size(); index++) {
                list.add(listMoves.get(index));
            }
        }
        return list;
    }

    /**
     * Function that returns the enemy pieces of the player
     *
     * @param type the type is the player you are either white or black
     * @return list of all the enemy pieces
     */
    public ArrayList<Piece> getListPieces(int type) {
        return type == Piece.PLAYER_WHITE ? whitePieces : blackPieces;
    }

    /**
     * Function checks if a piece controls a certain square
     *
     * @param piece the current piece
     * @param square the square if it controls
     * @return true if it does control the square and false if it doesn't
     */
//    public boolean isControllingSquare(Piece piece, Square square) {
//        for (int i = 0; i < getAvaliableMovesOfPiece(piece).size(); i++) {
//            if (getAvaliableMovesOfPiece(piece).get(i).equals(square)) {
//                return true;
//            }
//        }
//        return false;
//    }
    /**
     * Function that returns the king piece of the player
     *
     * @param whichPlayer which player black or white
     * @return the King of the player chosen
     */
    public King getKing(int whichPlayer) {
//        try{
        if (whichPlayer == Piece.PLAYER_WHITE) {
            return (King) whitePieces.get(0);
        } else {
            return (King) blackPieces.get(0);
        }
//        } catch(ClassCastException e){
//            return null;
//        }
    }

    /**
     * Boolean function that shows if the square is owned and returns the value
     *
     * @param piece The piece on location we flagCheck
     * @param type the type piece might be located
     * @return true if the square is avaliable for the piece to move and returns
     * false if it's not
     */
    protected boolean isLocatedPiece(Piece piece, int type) {
        return piece != null ? piece.getType() == type : false;
    }

    /**
     * Function confirms pawn has been promoted
     *
     * @param selectedRow the row of which pawn located
     * @return true if the pawn is at the edge of the board
     */
    public boolean isPromoted(int selectedRow) {
        return selectedPiece instanceof Pawn ? ((Pawn) selectedPiece).inPromotion(selectedRow) : false;
    }

    /**
     * Function that creates new Piece for the Player when his pawn is promoted
     *
     * @param piece which type of piece is it
     * @throws SquareException if the player decided to hack the system
     */
    public void createNewPiece(char piece) throws SquareException {
        int type = selectedPiece.getType();
        int row = selectedPiece.getRow(), col = selectedPiece.getCol();
        removePiece(selectedPiece);
        switch (piece) {
            case QUEEN:
                selectedPiece = new Queen(row, col, type);
                addPiece(selectedPiece);
                break;
            case KNIGHT:
                selectedPiece = new Knight(row, col, type);
                addPiece(selectedPiece);
                break;
            case BISHOP:
                selectedPiece = new Bishop(row, col, type);
                addPiece(selectedPiece);
                break;
            case ROOK:
                selectedPiece = new Rook(row, col, type);
                addPiece(selectedPiece);
                break;
            default:
                throw new SquareException();
        }
    }

    /**
     * Function that removes the piece from the List of the piece that avaliable
     * for the player
     *
     * @param suspectPiece the piece that has to be removed;
     */
    private void removePiece(Piece suspectPiece) {
        if (suspectPiece != null) {
            int type = suspectPiece.getType(), index;
            if (type == Piece.PLAYER_WHITE) {
                index = whitePieces.indexOf(suspectPiece);
                suspectPiece.setIndexOfList(index);
                whitePieces.remove(index);
            }
            if (type == Piece.PLAYER_BLACK) {
                index = blackPieces.indexOf(suspectPiece);
                suspectPiece.setIndexOfList(index);
                blackPieces.remove(index);
            }
        }
    }

    /**
     * The function adds the piece to the list of it's type.
     *
     * @param suspectPiece the piece we're adding
     */
    private void addPiece(Piece suspectPiece) {
        if (suspectPiece != null) {
            int type = suspectPiece.getType();
            if (type == Piece.PLAYER_WHITE) {
                whitePieces.add(suspectPiece.getIndexOfList(), suspectPiece);
            }
            if (type == Piece.PLAYER_BLACK) {
                blackPieces.add(suspectPiece.getIndexOfList(), suspectPiece);
            }
        }
    }

    private void fillPiecesToList(int indexRow, int indexCol) {
        if (isLocatedPiece(matGame[indexRow][indexCol], Piece.PLAYER_WHITE)) {
            whitePieces.add(matGame[indexRow][indexCol]);
        }
        if (isLocatedPiece(matGame[indexRow][indexCol], Piece.PLAYER_BLACK)) {
            blackPieces.add(matGame[indexRow][indexCol]);
        }
    }

    /**
     * Fills the matrix of the game logic with pieces and empty cells
     *
     * @param indexRow The coordinates of the Row of the board
     * @param indexCol The coordinates of the Colum of the board
     */
    private void fillMatrixPlayers(int indexRow, int indexCol) {

        switch (indexRow) {
            case Square.LAST_ROW:
                fillPiecesMetrix(Piece.PLAYER_BLACK, indexRow, indexCol);
                break;
            case Square.SEVENTH_ROW:
                matGame[indexRow][indexCol] = new Pawn(indexRow, indexCol, Piece.PLAYER_BLACK);
                break;
            case Square.SECOND_ROW:
                matGame[indexRow][indexCol] = new Pawn(indexRow, indexCol, Piece.PLAYER_WHITE);
                break;
            case Square.FIRST_ROW:
                fillPiecesMetrix(Piece.PLAYER_WHITE, indexRow, indexCol);
                break;
            default:
                matGame[indexRow][indexCol] = null;
        }
    }

    /**
     * Function mate to fill the First and Last Rows as initial start of the
     * game
     *
     * @param theType The Current Piece holds the Pieces
     * @param indexRow The coordinates of the Row in the board
     * @param indexCol The coordinates of the Colum in the board
     */
    private void fillPiecesMetrix(int theType, int indexRow, int indexCol) {
        switch (indexCol) {
            case 0:
                matGame[indexRow][indexCol] = new Rook(indexRow, indexCol, theType);
                break;
            case 1:
                matGame[indexRow][indexCol] = new Knight(indexRow, indexCol, theType);
                break;
            case 2:
                matGame[indexRow][indexCol] = new Bishop(indexRow, indexCol, theType);
                break;
            case 3:
                matGame[indexRow][indexCol] = new Queen(indexRow, indexCol, theType);
                break;
            case 4:
                matGame[indexRow][indexCol] = new King(indexRow, indexCol, theType);
                break;
            case 5:
                matGame[indexRow][indexCol] = new Bishop(indexRow, indexCol, theType);
                break;
            case 6:
                matGame[indexRow][indexCol] = new Knight(indexRow, indexCol, theType);
                break;
            case 7:
                matGame[indexRow][indexCol] = new Rook(indexRow, indexCol, theType);
                break;

        }
    }

    /**
     * function that sends the matrix game
     *
     * @return returns the matrix
     */
    protected Piece[][] getMatrix() {
        return matGame;
    }

    /**
     * Sends the player of that current piece and player in the matrix
     *
     * @param indexRow gets the index of row in the board
     * @param indexCol gets the index of colum in the board
     * @return returns the player
     */
    public Piece getCoordinatedPiece(int indexRow, int indexCol) {
        return this.matGame[indexRow][indexCol];
    }

    /**
     * Sends the player of that current piece and player in the matrix
     *
     * @param s the Square of the location
     * @return returns the player
     */
    public Piece getCoordinatedPiece(Square s) {
        return this.matGame[s.getRow()][s.getCol()];
    }

    /**
     * Sends the player current turn
     *
     * @return returns the player;
     */
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * function says if the player pieces checking the king
     *
     * @return true if one or more piece checking the opponent king
     */
    public boolean isCheck() {
        return flagCheck;
    }

    /**
     * Function that sends the opposite type of this Square
     *
     * @return the opposite turn or type of enemy
     */
    public int getOppositeTurn() {
        return currentTurn == Piece.PLAYER_WHITE ? Piece.PLAYER_BLACK : Piece.PLAYER_WHITE;
    }

    /**
     * Function that sends the opposite type of this Square
     *
     * @param turn the turn given
     * @return the opposite turn or type of enemy
     */
    public int getOppositeTurn(int turn) {
        return turn == Piece.PLAYER_WHITE ? Piece.PLAYER_BLACK : Piece.PLAYER_WHITE;
    }

    /**
     * Switches the turn of the players
     */
    public void switchTurn() {
        if (currentTurn == Piece.PLAYER_WHITE) {
            currentTurn = Piece.PLAYER_BLACK;
        } else {
            currentTurn = Piece.PLAYER_WHITE;
        }
    }

    /**
     * Sets the Selected Square to be selected
     *
     * @param indexRow Index row of which the piece is selected in the game
     * matrix
     * @param indexCol Index colum of which the piece is selected in the game
     * matrix
     */
    public void setSelectedPiece(int indexRow, int indexCol) {
        selectedPiece = matGame[indexRow][indexCol];
    }

    /**
     * Functions sets the selected piece with the piece given
     *
     * @param p the new piece
     */
    public void setSelectedPiece(Piece p) {
        selectedPiece = p;
    }

    /**
     * Function that returns the Square that is the selected
     *
     * @return the Square and which is piece belongs
     */
    public Piece getSelectedPiece() {
        return this.selectedPiece;
    }
    

    /**
     * Prints the matrix of the board
     */
    public void printMatrix() {
        System.out.println("");
        for (int indexRow = 0; indexRow < boardSize; indexRow++) {
            for (int indexCol = 0; indexCol < boardSize; indexCol++) {
                if (matGame[indexRow][indexCol] == null) {
                    System.out.print("  ");
                } else {
                    matGame[indexRow][indexCol].toStringPiece();
                    System.out.print("");
                }
            }
            System.out.print("\n");
        }
    }

    /**
     * Prints the both sides pieces
     */
    public void printPieces() {
        int k = 1;
        for (int i = 0; i < whitePieces.size(); i++) {
            whitePieces.get(i).toStringPiece();
            whitePieces.get(i).toStringCoordinates();
            System.out.print(" ");
            if (i == 4 * k) {
                System.out.println("");
                k++;
            }
        }
        System.out.println("");
        System.out.println("");
        k = 1;
        for (int i = 0; i < blackPieces.size(); i++) {
            blackPieces.get(i).toStringPiece();
            blackPieces.get(i).toStringCoordinates();
            System.out.print(" ");
            if (i == 4 * k) {
                System.out.println("");
                k++;
            }
        }
    }

    /**
     * Function that sorts the list
     *
     * @param list
     */
    public void sortPieceList(ArrayList<Piece> list) {
        Collections.sort(list, (Piece p1, Piece p2) -> p2.getValuePiece() - p1.getValuePiece());
    }

    /**
     * This function analyzes the position of the pieces on the board
     *
     * @param player which player to calculate
     * @return the value it values
     */
//    private float getPositionPiecesValue(int player){
//        float score = 0;
//        if (player == Piece.PLAYER_WHITE) {
//            for (int i = 0; i < whitePieces.size(); i++) {
//                
//            }
//        } else{
//            for (int i = 0; i < blackPieces.size(); i++) {
//                
//            }
//        }
//        return score;
//    }
}
