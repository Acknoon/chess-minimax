package finalVar;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Ariel
 */
public class Model {

//      Logic Veriables
//    ============================================
    private Piece[][] matGame;
    private ArrayList<Piece> whitePieces, blackPieces, currentTurn = null;
    private Piece selectedPiece;
    private King wKing, bKing;
    private boolean flagCheck;
    private int moveCounter;

    /**
     * The Constructor of the Model in the game
     */
    public Model() {
        initGameLogic();
    }

    /**
     * Function that resets the Logic of the game
     */
    public final void initGameLogic() {
        matGame = new Piece[Const.DEFAULT_BOARD_SIZE][Const.DEFAULT_BOARD_SIZE];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        int indexRow, indexCol;
        for (indexRow = 0; indexRow < Const.DEFAULT_BOARD_SIZE; indexRow++) {
            for (indexCol = 0; indexCol < Const.DEFAULT_BOARD_SIZE; indexCol++) {
                fillPiecetoMatrix(indexRow, indexCol);
                fillPiecesToList(indexRow, indexCol);
            }
        }
        flagCheck = false;
        selectedPiece = null;
        moveCounter = 0;
//        sortPieceList(whitePieces);
//        sortPieceList(blackPieces);
        currentTurn = whitePieces;
    }

    public void updateBoard(Move move) {
        executeMoveTempoPlayer(move);
        flagCheck = isCheckHappen(currentTurn);
        switchTurn();
        ArrayList<Move> possibleMoves = getAllPossibleMoves(currentTurn);
        System.out.println(possibleMoves);
        System.out.println(possibleMoves.size());
    }

    /**
     * Function that moves the piece been selected to the coordinates been told
     *
     * @param move is the move to be played, contains the piece that can move to
     * a location
     */
    public void executeMoveTempoPlayer(Move move) {
        Square locationMove = move.getDestinationSquare();
        Square s = new Square(move.getPiece().getRow(), move.getPiece().getCol());
        matGame[move.getPiece().getRow()][move.getPiece().getCol()] = null;
        matGame[locationMove.getRow()][locationMove.getCol()] = move.getPiece();
        matGame[locationMove.getRow()][locationMove.getCol()].setRow(locationMove.getRow());
        matGame[locationMove.getRow()][locationMove.getCol()].setCol(locationMove.getCol());
        locationMove.setSquare(s);
        removePiece(move.getCapturedPiece());
        move.getPiece().addMoveCounter();
        if (!(move.getPiece() instanceof Pawn) || move.getCapturedPiece() == null) {
            moveCounter++;
        } else {
            moveCounter = 0;
        }
    }

    /**
     * Function that moves the piece been selected to the coordinates been told
     *
     * @param move is the move to be played, contains the piece that can move to
     * a location
     */
    public void excuteMoveTempoAI(Move move) {
        Square locationMove = move.getDestinationSquare();
        Square s = new Square(move.getPiece().getRow(), move.getPiece().getCol());
        matGame[move.getPiece().getRow()][move.getPiece().getCol()] = null;
        if (isPromoted(move)) {
            move.setPiece(createPiece(Const.TAG_QUEEN, move.getPiece()));
            move.promote(true);
        }
        matGame[locationMove.getRow()][locationMove.getCol()] = move.getPiece();
        matGame[locationMove.getRow()][locationMove.getCol()].setRow(locationMove.getRow());
        matGame[locationMove.getRow()][locationMove.getCol()].setCol(locationMove.getCol());
        locationMove.setSquare(s);
        removePiece(move.getCapturedPiece());
        move.getPiece().addMoveCounter();
        if (!(move.getPiece() instanceof Pawn) || move.getCapturedPiece() == null) {
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
        if (move.hasPromoted()) {
            move.setPiece(new Pawn(move.getPiece().getRow(), move.getPiece().getCol(), move.getPiece().getSign()));
            move.promote(false);
        }
        matGame[locationMove.getRow()][locationMove.getCol()] = move.getPiece();
        matGame[locationMove.getRow()][locationMove.getCol()].setRow(locationMove.getRow());
        matGame[locationMove.getRow()][locationMove.getCol()].setCol(locationMove.getCol());
        locationMove.setSquare(s);
        addPiece(move.getCapturedPiece());
        move.getPiece().subMoveCounter();
        if (!(move.getPiece() instanceof Pawn) || move.getCapturedPiece() == null) {
            moveCounter--;
        } else {
            moveCounter = 0;
        }
    }
    
    public Move doComputerMove() {
        long totalTime = 0, deadlineTime = Const.RUNTIME;
        Move tempMove = null, compMove = null;
        int maxDepth = 1;
        do {
            System.out.println("////////////////////////////////////////////Minimax Begun////////////////////////////////////");
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

        System.out.println("========================Finished Minix, Process Complete=======================");
//        System.out.println(compMove.getPossibleMoves());
        System.out.println("--------------------------------------------------------");
        System.out.println("Total time: " + totalTime);
        System.out.println("-------------------------------------------------------");
        printPieces();
        printMatrix();

        return compMove;
    }

    private Move miniMax(boolean isMax, double alpha, double beta, int depth, int maxDepth, long timeMs) {
        boolean isGameOver = isGameOver();
        if (isGameOver || maxDepth == 0 || timeMs <= 0) {
//            System.out.println("===================================================\n"
//                    + "============================================================\n"
//                    + "=============================================================");
//            printPieces();
//            printMatrix();
//            System.out.println("VVVVVVVVVVVVVVVVVVV terminal VVVVVVVVVVVVVVVVVVVV");
//            System.out.println("isGameOver=" +isGameOver);
//            System.out.println("timeMs="+timeMs);
//            System.out.println("depth="+depth);
//            System.out.println("maxDepth="+maxDepth);
            int score = getBoardScore(this.currentTurn);
//            System.out.println("score="+score);
//            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
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
                excuteMoveTempoAI(possibleMoves.get(i));
                checkOccurence(isMax);
                time2 = System.currentTimeMillis();
                timeMs = timeMs - (time2 - time1);
                tempMove = miniMax(false, alpha, beta, depth + 1, maxDepth - 1, timeMs);
                undoMove(possibleMoves.get(i));

//                 if(depth == 0)
//                {
//                    System.out.println("move #" +(i+1));
//                    System.out.println(possibleMoves.get(i));
//                    System.out.println("score: " + tempMove.getScore());
//                    
//                }
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
                excuteMoveTempoAI(possibleMoves.get(i));
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
//        bestMove.setPossibleMoves(possibleMoves);
        return bestMove;
    }

    /**
     * Function that returns number the computer thinks of the position
     *
     * @param turn which turn to said to move
     * @return a number of the valuated position
     */
    private int getBoardScore(ArrayList<Piece> turn) {
        if (checkForWin(getOppositeSign(turn))) {
            return Const.WIN_SCORE;
        }

        if (checkForWin(turn)) {
            return Const.WIN_SCORE * (-1);
        }

        if (checkForDraw()) {
            return 0; // A tie!
        }
        int score1 = getMaterialValue(turn);
        int score2 = getMaterialValue(getOppositeSign(turn));
        return score1 - score2;
    }

    /**
     * Function calculates the value of all pieces that player has
     *
     * @param player the player to calculate the score
     * @return value of the score
     */
    private int getMaterialValue(ArrayList<Piece> player) {
        int score = 0;
        if (player == whitePieces) {
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
    
    /**
     * Updates the pieces check status
     *
     * @param pieceList
     * @return returns if a check happens or not
     */
    public boolean isCheckHappen(ArrayList<Piece> pieceList) {
        ArrayList<Move> list;

        for (int i = 0; i < pieceList.size(); i++) {
            if (pieceList.get(i).isControllingSquare(getKing(getOppositeSign(pieceList)).getSquare())) {
                list = pieceList.get(i).getControllingList(this);
                System.out.println(list.size());
                System.out.println(list);
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getDestinationSquare().equals(getKing(getOppositeSign(pieceList)).getSquare())) {
                        return true;
                    }
                }
            }
        }
        return false;
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
            this.flagCheck = isCheckHappen(getOpponent());
        }
    }

    /**
     * Function checks if the game is over
     *
     * @return true if the game is over and false if the game still continues
     */
    public boolean checkForWin() {
        if (flagCheck) {
            if (getAllPossibleMoves(whitePieces).isEmpty()) {
                return true;
            }
            if (getAllPossibleMoves(blackPieces).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Function checks if the game is over
     *
     * @param turn turn currently given
     * @return true if the game is over and false if the game still continues
     */
    public boolean checkForWin(ArrayList<Piece> turn) {
        if (flagCheck) {
            if (getAllPossibleMoves(turn).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Function checks if the game is a tie
     *
     * @return true if the game is a tie and false if the game still continues
     */
    public boolean checkForDraw() {
        if (flagCheck == false) {
            if (getAllPossibleMoves(currentTurn).isEmpty() || getAllPossibleMoves(getOpponent()).isEmpty()) {
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
    public ArrayList<Move> getLegalMoves(Piece currentPiece) {
        ArrayList<Move> listViableMoves = new ArrayList<>();
        ArrayList<Move> list = currentPiece.getDestinationList(this);
        for (int i = 0; i < list.size(); i++) {
            executeMoveTempoPlayer(list.get(i));
            if (!isCheckHappen(getOppositeSign(currentPiece.getSign()))) {
                listViableMoves.add(list.get(i));
            }
            undoMove(list.get(i));
        }
        return listViableMoves;
    }

    /**
     * The purpose of the function is the convert the current list of moves on
     * the selected piece into a list of squares. The list contains a variety of
     * squares that the selected piece can go to. However, the Move is different
     * from just simple squares on the board. Move contains the information of
     * piece moving on the board, while a square only defines location on the
     * board.
     *
     * @param luckyPiece The Piece that was selected to be converted the list of
     * moves of that specific piece
     * @return a list of locations on the board that the selected piece can move
     * to
     */
    public ArrayList<Square> getConvertedListSquares(Piece luckyPiece) {
        ArrayList<Move> list = getLegalMoves(luckyPiece);
        ArrayList<Square> mainList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mainList.add(list.get(i).getDestinationSquare());
        }
        return mainList;
    }

    /**
     * Function that converts the list of moves to simple squares of destination
     * of a piece. Which means that the list which is given in the function is a
     * list of a piece that is unknown to function. The list contains a variety
     * of squares that the unknown piece can go to. However, the Moves is
     * different from just simple squares on the board. Move contains the
     * information of piece moving on the board and a square only defines the
     * location of the board.
     *
     * @param list list contains the destination squares of a piece
     * @return the list of squares that the piece could be moved
     */
    public ArrayList<Square> getListSquares(ArrayList<Move> list) {
        ArrayList<Square> mainList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mainList.add(list.get(i).getDestinationSquare());
        }
        return mainList;
    }

    /**
     * Creates a move with the piece that has been selected by a player
     *
     * @param piece The Piece that is to move
     * @param destinationSquare The destination square
     * @return the move that has been created with the selectedPiece
     */
    public Move newMove(Piece piece, Square destinationSquare) {
        if (matGame[destinationSquare.getRow()][destinationSquare.getCol()] != null) {
            return new Move(piece, destinationSquare, matGame[destinationSquare.getRow()][destinationSquare.getCol()]);
        }
        return new Move(piece, destinationSquare);
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
            listPieces = currentTurn;
        } else {
            listPieces = getOpponent();
        }

        for (int indexPiece = 0; indexPiece < listPieces.size(); indexPiece++) {
            ArrayList<Move> listMoves = getLegalMoves(listPieces.get(indexPiece));
            for (int index = 0; index < listMoves.size(); index++) {
                list.add(listMoves.get(index));
            }
        }
        return list;
    }

    /**
     * Function that makes a least of all Moves a piece can move to
     *
     * @param pieceList list of player pieces
     * @return list of moves
     */
    public ArrayList<Move> getAllPossibleMoves(ArrayList<Piece> pieceList) {
        ArrayList<Move> list = new ArrayList<>();

        for (int indexPiece = 0; indexPiece < pieceList.size(); indexPiece++) {
            ArrayList<Move> listMoves = getLegalMoves(pieceList.get(indexPiece));
            for (int index = 0; index < listMoves.size(); index++) {
                list.add(listMoves.get(index));
            }
        }
        return list;
    }

    /**
     * Algorithm that returns the squares which player's pieces located at.
     * @param player which player's pieces
     * @return a piece set but the location of them on the chess board.
     */
    public ArrayList<Square> getListSquaresOfPieces(int player) {
        ArrayList<Square> list = new ArrayList<>();
        ArrayList<Piece> listPieces;
        if (player == Const.PLAYER_WHITE) {
            listPieces = whitePieces;
        } else {
            listPieces = blackPieces;
        }
        for (int unit = 0; unit < listPieces.size(); unit++) {
            ArrayList<Move> listMoves = getLegalMoves(listPieces.get(unit));
            if (!listMoves.isEmpty()) {
                list.add(listPieces.get(unit).getSquare());
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
        return type == Const.PLAYER_WHITE ? whitePieces : blackPieces;
    }

    /**
     * Function that returns the king piece of the player
     *
     * @param player which player black or white
     * @return the King of the player chosen
     */
    public King getKing(ArrayList<Piece> player) {
        return player == whitePieces ? wKing : bKing;
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
        return piece != null ? piece.getSign() == type : false;
    }

    /**
     * Function confirms pawn has been promoted
     *
     * @param move that has to be played
     * @return true if the pawn is at the edge of the board
     */
    public boolean isPromoted(Move move) {
        return move.getPiece() instanceof Pawn ? ((Pawn) move.getPiece()).inPromotion(move.getDestinationSquare()) : false;
    }

    /**
     * Function that creates new Piece for the Player when his pawn is promoted
     *
     * @param piece which type of piece is it
     * @throws SquareException if the player decided to hack the system
     */
    public void createNewPiece(int piece) throws SquareException {
        int type = selectedPiece.getSign();
        int row = selectedPiece.getRow(), col = selectedPiece.getCol();
        removePiece(selectedPiece);
        switch (piece) {
            case Const.QUEEN:
                selectedPiece = new Queen(row, col, type);
                addPiece(selectedPiece);
                break;
            case Const.KNIGHT:
                selectedPiece = new Knight(row, col, type);
                addPiece(selectedPiece);
                break;
            case Const.BISHOP:
                selectedPiece = new Bishop(row, col, type);
                addPiece(selectedPiece);
                break;
            case Const.ROOK:
                selectedPiece = new Rook(row, col, type);
                addPiece(selectedPiece);
                break;
            default:
                throw new SquareException();
        }
    }

    /**
     * Function that creates new Piece for the Player when his pawn is promoted
     *
     * @param signPiece what type of piece needs to be created
     * @param piece which type of piece is it
     * @return returns a newly created piece
     */
    public Piece createPiece(String signPiece, Piece piece) {
        int type = piece.getSign();
        int row = piece.getRow(), col = piece.getCol()/*, index = piece.getIndexOfList()*/;
        switch (signPiece) {
            case Const.TAG_QUEEN:
                piece = new Queen(row, col, type);
//                piece.setIndexOfList(index);
                break;
            case Const.TAG_KNIGHT:
                piece = new Knight(row, col, type);
//                piece.setIndexOfList(index);
                break;
            case Const.TAG_BISHOP:
                piece = new Bishop(row, col, type);
//                piece.setIndexOfList(index);
                break;
            case Const.TAG_ROOK:
                piece = new Rook(row, col, type);
//                piece.setIndexOfList(index);
        }
        return piece;
    }

    /**
     * Promotes the pawn that marched to the edge of the board to a piece.
     * @param move the move that promotes the pawn.
     * @param valuePiece special id code for a piece to be promoted.
     */
    public void promotePiece(Move move, String valuePiece){
        removePiece(move.getPiece());
        move.setPiece(createPiece(valuePiece, move.getPiece()));
        addPiece(move.getPiece());
        move.promote(true);
    }
    
    /**
     * Function that removes the piece from the List of the piece that avaliable
     * for the player
     *
     * @param suspectPiece the piece that has to be removed;
     */
    private void removePiece(Piece suspectPiece) {
        if (suspectPiece != null) {
            int type = suspectPiece.getSign();
            if (type == Const.PLAYER_WHITE) {
                whitePieces.remove(suspectPiece);
            }
            if (type == Const.PLAYER_BLACK) {
                blackPieces.remove(suspectPiece);
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
            int type = suspectPiece.getSign();
            if (type == Const.PLAYER_WHITE) {
                whitePieces.add(suspectPiece);
            }
            if (type == Const.PLAYER_BLACK) {
                blackPieces.add(suspectPiece);
            }
        }
    }

    private void fillPiecesToList(int indexRow, int indexCol) {
        if (isLocatedPiece(matGame[indexRow][indexCol], Const.PLAYER_WHITE)) {
            whitePieces.add(matGame[indexRow][indexCol]);
        }
        if (isLocatedPiece(matGame[indexRow][indexCol], Const.PLAYER_BLACK)) {
            blackPieces.add(matGame[indexRow][indexCol]);
        }
    }

    /**
     * Fills the matrix of the game logic with pieces and empty cells
     *
     * @param indexRow The coordinates of the Row of the board
     * @param indexCol The coordinates of the Colum of the board
     */
    private void fillPiecetoMatrix(int indexRow, int indexCol) {

        switch (indexRow) {
            case Const.LAST_ROW:
                fillComplexPieces(Const.PLAYER_BLACK, indexRow, indexCol);
                break;
            case Const.SEVENTH_ROW:
                matGame[indexRow][indexCol] = new Pawn(indexRow, indexCol, Const.PLAYER_BLACK);
                break;
            case Const.SECOND_ROW:
                matGame[indexRow][indexCol] = new Pawn(indexRow, indexCol, Const.PLAYER_WHITE);
                break;
            case Const.FIRST_ROW:
                fillComplexPieces(Const.PLAYER_WHITE, indexRow, indexCol);
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
    private void fillComplexPieces(int theType, int indexRow, int indexCol) {
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
                if (theType == Const.PLAYER_WHITE) {
                    wKing = new King(indexRow, indexCol, theType);
                    matGame[indexRow][indexCol] = wKing;
                } else {
                    bKing = new King(indexRow, indexCol, theType);
                    matGame[indexRow][indexCol] = bKing;
                }
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
     * @param square the Square of the location
     * @return returns the player
     */
    public Piece getCoordinatedPiece(Square square) {
        return square == null ? null : this.matGame[square.getRow()][square.getCol()];
    }

    /**
     * Sends the player current turn
     *
     * @return returns the player;
     */
    public ArrayList<Piece> getCurrentTurn() {
        return this.currentTurn;
    }

    /**
     * function says if the player pieces checking the king
     *
     * @return true if one or more piece checking the opponent king
     */
    public boolean getCheck() {
        return flagCheck;
    }

    /**
     * Function that sends the opposite type of this Square
     *
     * @return the opposite turn or type of enemy
     */
    public ArrayList<Piece> getOpponent() {
        return currentTurn == whitePieces ? blackPieces : whitePieces;
    }

    /**
     * Function that returns the opposite pieces or opponent of the player sent
     *
     * @param turn which player has been sent
     * @return the opponent of that player
     */
    public ArrayList<Piece> getOppositeSign(ArrayList<Piece> turn) {
        return turn == whitePieces ? blackPieces : whitePieces;
    }
    
    /**
     * Function that returns the opposite pieces or opponent of the player sent
     *
     * @param turn which player has been sent
     * @return the opponent of that player
     */
    public ArrayList<Piece> getOppositeSign(int turn) {
        return turn == Const.PLAYER_WHITE ? blackPieces : whitePieces;
    }

    /**
     * Switches the turn of the players
     */
    public void switchTurn() {
        if (currentTurn == whitePieces) {
            currentTurn = blackPieces;
        } else {
            currentTurn = whitePieces;
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
        for (int indexRow = 0; indexRow < Const.DEFAULT_BOARD_SIZE; indexRow++) {
            for (int indexCol = 0; indexCol < Const.DEFAULT_BOARD_SIZE; indexCol++) {
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

    public int[][] getTranslatedBoard() {
        int[][] simpleBoard;
        simpleBoard = new int[Const.DEFAULT_BOARD_SIZE][Const.DEFAULT_BOARD_SIZE];
        for (int row = 0; row < Const.DEFAULT_BOARD_SIZE; row++) {
            for (int col = 0; col < Const.DEFAULT_BOARD_SIZE; col++) {
                switch (matGame[row][col].colorSide) {
                    case Const.PLAYER_WHITE:
                        simpleBoard[row][col] = matGame[row][col].getValuePiece() * Const.PLAYER_WHITE;
                        break;
                    case Const.PLAYER_BLACK:
                        simpleBoard[row][col] = matGame[row][col].getValuePiece() * Const.PLAYER_BLACK;
                        break;
                    default:
                        simpleBoard[row][col] = Const.DEFAULT;
                }
            }
        }
        return simpleBoard;
    }
    
}
