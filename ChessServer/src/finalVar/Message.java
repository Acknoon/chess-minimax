package finalVar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Essential class that used to communicate between the server and client, every
 * data stored inside the message classed. The Message class can contain
 * commands that have special instructions for the client or the server. The
 * Message should be synchronized between the client and server as any slight
 * change will appear different class. Messages sent between sockets are either
 * information related to the game and information that isn't related to the
 * game.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class Message implements Serializable {

    private String msg;//String that contains essential commands that get scanned and processed.
    private String stats;//String that contains the statistics of the game history of a player.
    private int sign;//sign of the player, could be -1 which is black pieces or 1 white pieces.
    private int pieceID;//unique special value that is used to convert the number into an image of view.
    private ArrayList<Square> list;//list of squares with many different purposes,
    //dependent on the command string along with it as instructions.
    private ArrayList<Square> reserveList = null;//Secondary list used mostly to unlock or lock pieces,
    //as the main list occupied.
    private Square destinationPos;//Highlighted location which shows square to move the piece to.
    private Square pos;//The position of the piece been selected.

    /**
     * Constructor message that used to set the title of the client. By setting
     * up the title to inform the user what pieces him playing as.
     *
     * @param msg Command or special message to start up process in the client
     * or server.
     * @param sign A sign of pieces weather black pieces or white pieces.
     */
    public Message(String msg, int sign) {
        this.msg = msg;
        this.sign = sign;
    }

    /**
     * Most frequently used constructor. It sends an instruction command as a
     * string message, while also a list which used to manage the game. The list
     * contains number of squares that are highlighted as locations on the chess
     * board. The String message gives a client instructions on the next steps.
     *
     * @param msg String command essential to the process
     * @param list list of squares that are highlight of the chess board.
     */
    public Message(String msg, ArrayList<Square> list) {
        this.msg = msg;
        this.list = new ArrayList<>(list);
    }

    /**
     * A constructor It sends an instruction command as a string message, the
     * message contains a purpose to what the square that got sent.
     *
     * @param msg String command essential to the process
     * @param pos A simple location that is represented as a square on a chess
     * board.
     */
    public Message(String msg, Square pos) {
        this.msg = msg;
        this.pos = new Square(pos);
    }

    /**
     * A constructor with a String messaged attached as to send out a command
     * message to activate a process for the client
     *
     * @param msg String command essential to the process
     */
    public Message(String msg) {
        this.msg = msg;
    }

    /**
     * Important Constructor created to have the purpose of guiding the client
     * with essential information to make the client execute a chess move a
     * turn. The server extracts the information from the end of a logical turn
     * into a message to pass into clients GUI.
     *
     * @param msg String command essential to the process
     * @param pieceID The special number that defines the piece type.
     * @param pos The position of the Piece that is currently located preview to
     * the execution of the move
     * @param destinationPos a location on the chess board which the selected
     * piece moves to during it's turn.
     */
    public Message(String msg, int pieceID, Square pos, Square destinationPos) {
        this.msg = msg;
        this.pieceID = pieceID;
        this.pos = new Square(pos);
        this.destinationPos = new Square(destinationPos);
    }

    /**
     * A Constructor that creates a message to transfer data to the client
     * during the 2nd phase of move selection, the purpose is to lock certain
     * squares to make it user friendly for the player.
     *
     * @param msg String command essential to the process
     * @param list highlighted squares of a piece can move too.
     * @param reserveList to lock the previous selected piece potential move
     * when the player took back the selection on another piece.
     */
    public Message(String msg, ArrayList<Square> list, ArrayList<Square> reserveList) {
        this.msg = msg;
        this.list = new ArrayList<>(list);
        this.reserveList = new ArrayList<>(reserveList);
    }

    /**
     * A constructor that holds coordinates of square or location on a chess
     * board. Mostly used by the client to communicate the squares been selected
     * to the server to handle the logic.
     *
     * @param pos Location on a chess board also known a chess Square.
     */
    public Message(Square pos) {
        this.pos = new Square(pos);
    }

    /**
     * Constructor that is transfer data of requested statistics of the game
     * history from a player. this constructor isn't related to the game much as
     * it's used to show statistics during the game, because of that this
     * message usually gets transfered through the CMD socket.
     *
     * @param constStats String command essential to the process the statistics.
     * @param stats A string contains a game history of a player.
     */
    public Message(String constStats, String stats) {
        this.msg = constStats;
        this.stats = stats;
    }

    /**
     * Gives direct access to the special id number of a piece.
     *
     * @return id number of a piece.
     */
    public int getPieceID() {
        return pieceID;
    }

    /**
     * Gives the program access to enter the message which is the commands that
     * formatted as a string.
     *
     * @return An essential command message that is formatted in string.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Gives the program access to enter the position which a location on a
     * board.
     *
     * @return a Location on a chess board.
     */
    public Square getPos() {
        return pos;
    }

    /**
     * Gives direct access the sign of a type of player, black pieces or white
     * pieces.
     *
     * @return a sign that can be -1 which represents black pieces and 1
     * represents white pieces.
     */
    public int getSign() {
        return sign;
    }

    /**
     * Gives access to ArrayList variable that contains a list of squares which
     * is a number of locations on a chess board.
     *
     * @return a list of squares that that represent multiple squares on a chess
     * board.
     */
    public ArrayList<Square> getList() {
        return list;
    }

    /**
     * Gives access to reserve ArrayList variable that contains a list of
     * squares which is a number of locations on a chess board.
     *
     * @return a list of squares that that represent multiple squares on a chess
     * board.
     */
    public ArrayList<Square> getReserveList() {
        return reserveList;
    }

    /**
     * Gives access to a position square that usually simulates the destination
     * of a certain piece been selected.
     *
     * @return a position or location on a chess board.
     */
    public Square getDestinationPos() {
        return destinationPos;
    }

    /**
     * Gives access to the table of a game history from a certain player in
     * String formate.
     *
     * @return game history of a player.
     */
    public String getStats() {
        return stats;
    }
}
