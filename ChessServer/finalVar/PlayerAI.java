package finalVar;

/**
 * Class dedicated to present the player as a machine, the machine known as AI
 * is run by minimax algorithm. This gives the player option to also play
 * against a player. However, the player is ran by a machine.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class PlayerAI extends Player {

    /**
     * Constructor for the AI player, just needs to know what side the machine
     * plays as to activate it's algorithms.
     *
     * @param sign of which pieces the player plays as.
     */
    public PlayerAI(int sign) {
        super(sign);
    }

    /**
     * Machine calculation of finding the best move based on calculation. As the
     * algorithm runs the AI calculates the current position on the chess board
     * and produces the best move it can by calculating the best move the AI
     * activates the minimax to calculate it.
     *
     * @param gameModel Model of the chess game, the logical structure to run
     * the game legally.
     * @return The move minimax calculated.
     */
    @Override
    public Move getMove(Model gameModel) {
        if (this.partner != null && partner instanceof PlayerNetwork) {
            ((PlayerNetwork) this.partner).writeMessage(Const.WAIT_TURN);
        }
        return gameModel.doComputerMove();
    }

    /**
     * No Point in the function as it's been made to update the visual board of
     * the human player, as AI player doesn't use a GUI to play the game there
     * is no need for this function.
     */
    @Override
    public void doBoardUpdate(Move move) {
    }

    /**
     * There is no use for this function as AI doesn't require network
     * communication between server and client as the computer AI is already
     * part of the server.
     */
    @Override
    public void executeMessage(String msg) {
    }

    /**
     * Gives access to the name Identity of the player. Which in this case it's
     * a computer calculating machine AI.
     *
     * @return Identity of an AI player.
     */
    @Override
    public String getNameID() {
        return Const.AI;
    }

    /**
     * Gives access the type identity of the player either it's a Guest, AI or a
     * Player
     *
     * @return A type that is AI.
     */
    @Override
    public String getType() {
        return Const.AI;
    }

}
