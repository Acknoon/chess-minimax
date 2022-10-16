package finalVar;

/**
 * The Player, it's an abstract class that varies between 2 different types of
 * players: The AI machine player or a regular human player. Every type of
 * player must be able to execute function as a requirement. The class simply
 * represent a player that plays the game and controls a piece set from one
 * side.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public abstract class Player {

    /*=====================Private Variables===============*/
    protected Player partner;//Another player that is paired with
    protected final int sign;//A sign of a piece set the player plays with

    /**
     * Constructor that creates a player with a sign of the which color or side
     * the player plays the game. Due to the game rules, the color white always
     * starts first the game and black moves second. It is possible to make that
     * the black will start the first move. However, this isn't the traditional
     * chess rules
     *
     * @param sign The color of the pieces the player plays
     */
    public Player(int sign) {
        this.sign = sign;
    }

    /**
     * The function creates that was made by the player that is playing after
     * doing that move it sends back to the game which will update the board
     * model.
     *
     * @param gameModel the Model of the board that checks the logic of the
     * game.
     * @return The move that was made by the current player.
     */
    public abstract Move getMove(Model gameModel);

    /**
     * The doing and the purpose of the function is to update the board.
     * Updating the board by sending a message the client if one of the players
     * is human player. If the player is Human then the function serves by
     * sending a message through the connection of TCP/IP, To the Client to
     * update the Client's board change or movement. However, if the player
     * isn't human the function isn't beneficial.
     *
     * @param move The move that has been calculated by the player that made the
     * decision.
     */
    public abstract void doBoardUpdate(Move move);

    /**
     * Determines the game end stage. This means the games comes to the end, the
     * function is called to announce to the client that server chose the game
     * to end. However, if the player isn't the client or human player, then it
     * closes all the threads that run from the computer side
     *
     * @param msg
     */
    public abstract void executeMessage(String msg);

    /**
     * The function returns the sign of the player which he plays The Sign is a
     * color of the player in chess plays there are 2 signs or colors in chess
     * Black pieces and White pieces and the sign determines which pieces the
     * player plays
     *
     * @return the color of the pieces that the player plays
     */
    public int getSign() {
        return sign;
    }

    /**
     * Give direct access to the program to enter the partner of a player.
     *
     * @return second player that is paired with this player.
     */
    public Player getPartner() {
        return partner;
    }

    /**
     * Sets up a new partner to this player, this will cut the connection
     * between the old partner and replace it with a new one.
     *
     * @param partner new partner to pair with.
     */
    public void setPartner(Player partner) {
        this.partner = partner;
    }

    /**
     * Gives access to the name Identity of the player.
     *
     * @return An identity of a player.
     */
    public abstract String getNameID();

    /**
     * Gives access the type identity of the player either it's a Guest, AI or a
     * Player
     *
     * @return the identity type of player which varies between AI, Player,
     * Guest.
     */
    public abstract String getType();
}
