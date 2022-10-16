package finalVar;

import java.util.ArrayList;

/**
 * A class Player the represents the human player that plays a match. A human
 * player contains information that is linked to the server. This type of player
 * is a default player used to create a match with however requires a real
 * person to login and connect to the server.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class PlayerNetwork extends Player {

    private final ClientData ID;//Variable that contains the valuable information of a link between the player and the server.
    private ArrayList<Move> listMoves;//helping variable to ensure a list of moves a player selected a piece off.

    /**
     * Network player constructor which is an object that contains the
     * connection link between client and server and a sign indicates a piece
     * set for the player to play as. This constructor also transforms a regular
     * client into a player that is ready to play a chess game
     *
     * @param ID The client data information necessary to maintain network
     * connection to the server.
     * @param sign for a piece set the player plays as.
     */
    public PlayerNetwork(ClientData ID, int sign) {
        super(sign);
        this.ID = ID;
        this.partner = null;
        listMoves = new ArrayList<>();
    }

    /**
     * A long Process that has 2 phases in, the first phase is to select the
     * piece that is avaliable for the player to legally play. meaning that a
     * player can't play illegal move in chess as the game will prevent it. As
     * the game gives the player to option to select pieces that are able to
     * move. After the first Phase the second phase comes where a player can
     * weather choose to select another piece and repeat the phase 1 or select
     * the square a current selected piece can move to.
     *
     * @param gameModel The model of the chess game, maintains the logic and
     * basic rules of the game.
     * @return a move that the player made.
     */
    @Override
    public Move getMove(Model gameModel) {
        ArrayList<Square> list = gameModel.getListSquaresOfPieces(this.getSign());
        ID.writeMessage(new Message(Const.PLAY_TURN, list));
        if (this.partner != null && partner instanceof PlayerNetwork) {
            ((PlayerNetwork) this.partner).writeMessage(Const.WAIT_TURN);
        }
        return createMove(gameModel, Const.CHECK_FIRST);
    }

    /**
     * The first phase of the move the player needs to make as it slowly
     * transitions to the second phase. A test being made for the player to
     * select a square that is legal for the piece to move or reselect another
     * piece to repeat this infinitely.
     *
     * @param gameModel The model of the chess game, maintains the logic and
     * basic rules of the game.
     * @param isSelected player selected piece from his piece set.
     * @return recursion to run over and over again till executed a full turn.
     * Which the player moved.
     */
    private Move createMove(Model gameModel, boolean isSelected) {
        Message msg = ID.readMessage();
        if (msg == null) {
            return null;
        }

        if (isSelectedPiece(msg, gameModel)) {
            ArrayList<Square> list = gameModel.getListSquares(listMoves);
            listMoves = getListSquaresOfPiece(gameModel, msg.getPos());
            ID.writeMessage(new Message(Const.SELECT_SQUARE, gameModel.getListSquares(listMoves), list));
            if (partner instanceof PlayerNetwork) {
                ((PlayerNetwork) this.partner).writeMessage(new Message(Const.SELECT_PIECE, msg.getPos()));
            }
        } else {
            return enableMovement(listMoves, msg.getPos(), gameModel, isSelected);
        }
        return createMove(gameModel, true);
    }

    /**
     * The phase 2 of the the player selecting to move the piece on the board,
     * this function operates after a player selected a piece and decides on
     * selecting another square to move the piece on the board.
     *
     * @param list list of moves that are matching of the move that has been
     * selected by the player.
     * @param pos The location of a square been chosen.
     * @param gameModel The model of the chess game, maintains the logic and
     * basic rules of the game.
     * @param isSelected if the piece from the piece set was first clicked and
     * selected.
     * @return
     */
    private Move enableMovement(ArrayList<Move> list, Square pos, Model gameModel, boolean isSelected) {
        if (isSelected) {
            for (Move squares : list) {
                if (pos.equals(squares.getDestinationSquare())) {
                    Move theMove = gameModel.newMove(squares.getPiece(), squares.getDestinationSquare());
                    if (gameModel.isPromoted(theMove)) {
                        ID.writeMessage(Const.PROMOTION);
                        Message pieceID = ID.readMessage();
                        gameModel.promotePiece(theMove, pieceID.getMsg());
                    }
                    listMoves.clear();
                    return theMove;
                }
            }
        }
        isSelected = false;
        return createMove(gameModel, false);
    }

    /**
     * Simple function that extracts every legal move of a piece relative to the
     * king as well. The function is very heavy and makes a lot of calculation
     * to determine a list of squares a piece can move to taking a count to all
     * parameters avaliable during the game.
     *
     * @param gameModel The model of the chess game, maintains the logic and
     * basic rules of the game.
     * @param square A position that was chosen by player that contains a piece
     * selected.
     * @return list of the legal moves a piece located on the square position
     * can make.
     */
    private ArrayList<Move> getListSquaresOfPiece(Model gameModel, Square square) {
        return gameModel.getLegalMoves(gameModel.getCoordinatedPiece(square));
    }

    /**
     * Function that returns boolean value if the piece that has been clicked
     * belongs to the owner in the game, as to make it clear players selected
     * their desired piece. When they select the piece the function checks if
     * they selected any kind of piece, further after it checks the piece
     * belongs to the players
     *
     * @param msg is the message that received from the client
     * @return returns true if the piece belongs to owner and false if it's
     * empty or enemy piece
     */
    private boolean isSelectedPiece(Message msg, Model gameModel) { //Had to add an if when the square is null
        int row = msg.getPos().getRow();
        int col = msg.getPos().getCol();
        Piece temporary = gameModel.getCoordinatedPiece(row, col);
        return temporary != null ? temporary.getSign() == this.getSign() : false;
    }

    /**
     * Gives direct access to the socket address which forms an endpoint of
     * network from the player. With the purpose of keeping the client data
     * information private.
     *
     * @return String of the IP address.
     */
    public String getAddressSocket() {
        return ID.getSocketAddress();
    }

    /**
     * Gives instructions to update the visual boards for the players in the
     * client section, update the view.
     *
     * @param move contains a piece, location, where it moves to update the
     * board for the player.
     */
    @Override
    public void doBoardUpdate(Move move) {
        ID.writeMessage(new Message(Const.MAKE_MOVE,
                move.getPiece().getValuePiece() * move.getPiece().getSign(),
                move.getPiece().getSquare(), move.getDestinationSquare()));
    }

    /**
     * Transfers commands to the client user to handle end of the game.This
     * Function is to centralize the process messages sent to the client to
     * handle the game over aspect.
     *
     * @param msg command message that is formatted as a string.
     */
    @Override
    public void executeMessage(String msg) {
        if (msg.equals(Const.GAME_OVER)) {
            ID.writeMessage(Const.GAME_OVER);
            return;
        }

        if (msg.equals(Const.DEFEATE)) {
            ID.writeMessage(Const.DEFEATE);
            return;
        }

        if (msg.equals(Const.VICTORY)) {
            ID.writeMessage(Const.VICTORY);
        }

        if (msg.equals(Const.START_NEW_GAME)) {
            ID.writeMessage(Const.START_NEW_GAME);
            listMoves.clear();
        }

    }

    /**
     * Function changes the current partner and replaces with a new partner. The
     * function gives access to change privilege of the partner management.
     *
     * @param partner a new partner to pair with this player
     */
    public void setPartner(PlayerNetwork partner) {
        this.partner = partner;
    }

    /**
     * Given access to the partner of this player.
     *
     * @return partner of this user.
     */
    @Override
    public Player getPartner() {
        return this.partner;
    }

    /**
     * Give access to the Identity of the player weather the user is registered
     * from the login database or the user is a guest.
     *
     * @return The identity of the player.
     */
    @Override
    public String getNameID() {
        return ID.getID();
    }

    
    public void writeMessage(String msg) {
        ID.writeMessage(msg);
    }

    /**
     * A link to the server that outputs information that is formatted as a
     * Message. Message is a complex Object that contains specific information
     * that is related to the game.
     *
     * @see Const.java
     * @param msg A unique structure of information related to the game that is
     * being transfered to the server.
     */
    public void writeMessage(Message msg) {
        ID.writeMessage(msg);
    }

    /**
     * A link to the server that outputs information that is a simple plain
     * text, the text is a command that is stored in the mutual class that both
     * server and client contain. The command that is being transfered is only
     * related to the game.
     *
     * @param text simple plain text or a command sent to the server related to
     * the game.
     */
    public void writeCommand(String text) {
        ID.writeCommand(text);
    }

    /**
     * A link to the server that outputs information that is formatted as a
     * Message. Message is a complex Object that contains specific information
     * to administrate or control the clients from the server. The information
     * that is sent are commands or templates to show the user functionality of
     * the UI.
     *
     * @see Const.java
     * @param cmd A unique structure of information non-related to the game that
     * is being transfered to the server.
     */
    public void writeCommand(Message cmd) {
        ID.writeCommand(cmd);
    }

    /**
     * An input data from the server that is related to the game. This blocks
     * the further code compilation as it awaits instructions from the server.
     * As the information being sent and transfered this extracts data and makes
     * sure the data is correct and wasn't corrupted. The information sent are
     * commands that notifies the client about the state of the game.
     *
     * @return The extracted data from server transfer. Checks weather the
     * information isn't corrupted.
     */
    public Message readMessage() {
        return ID.readMessage();
    }

    /**
     * An input data from the server. This blocks the further code compilation
     * as it awaits instructions from the server. As the information being sent
     * and transfered it gets extracted into fixed data of which isn't
     * corrupted. The information sent are commands of whom notifies the client
     * with critical messages.
     *
     * @return The extracted data from server transfer. Checks weather the
     * information isn't corrupted.
     */
    public Message readCommand() {
        return ID.readCommand();
    }

    /**
     * Closes the connection between client and the server.
     */
    public void exit() {
        ID.close();
    }

    /**
     * Gives access to current IP address used to surf the network.
     *
     * @return A string that contains the IP address of the user.
     */
    public String getLinkAddress() {
        return ID.getSocketAddress();
    }

    /**
     * Gives access the type identity of the player either it's a Guest, AI or a
     * Player
     *
     * @return the identity type of player which varies between AI, Player,
     * Guest.
     */
    @Override
    public String getType() {
        return this.ID.getType();
    }
}
