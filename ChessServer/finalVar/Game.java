package finalVar;

/**
 * This is the game section of the server. The server handles the game between 2
 * players, the game server is located inside a thread that is ran on every 2
 * players found and paired, it sends instructions for the client to move
 * pieces. the game contains the model of the game to keep the logical concept
 * of chess working. Everything related to the game and game communication will
 * be located here
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class Game {

    /**
     * ================Client Variables================
     */
    private final Player white;//The player known to play with the white pieces in the match.
    private final Player black;//The player known to play with the black pieces in the match.
    private final Model gameModel;//Model of the chess game, handles the computer, game status, etc...
    private Player currentPlayer;//Current turn of the player that is playing.

    /**
     * Game Constructor, sets up the game to run with input the players that are
     * matched. The first player will always play with white pieces while second
     * player always will play with black pieces. The game constructor requires
     * 2 Operating Players which are matched together, they need to be linked
     * together as partners. This concept can be visualized as a board set up
     * before starting the match between players.
     *
     * @param p1 First of the Operating Players that entered the server, this
     * player will play with white pieces.
     * @param p2 Second of the Operating Players that entered the server, this
     * player will play with black pieces pieces.
     */
    public Game(Player p1, Player p2) {
        white = p1;
        black = p2;
        gameModel = new Model();
        currentPlayer = white;
    }

    /**
     * This is the Run operation in the game, what it means is the game begins
     * between 2 players, the match begun! While the game runs it functions as a
     * communication between 2 players as the server transfers the data and
     * instructions every selection being made.
     */
    public void startGame() {
        Move move;
        do {
            do {
                move = currentPlayer.getMove(gameModel);
                if (move == null) {
                    break;
                }
                updateGraphicalBoard(move);
                gameModel.updateBoard(move);
//                isUnderCheck(gameModel.getCheck());
                swapTurn();
            } while (!isGameOver());

            if (processThePlayerWhiteMessage()) {
                break;
            }
            if (processThePlayerBlackMessage()) {
                break;
            }
            doNewGame();
        } while (true);
    }

    /**
     * This resets the logic model of the game to start a new game between
     * players when the count down finished. If any of the players exists the
     * game then this process won't continue.
     */
    private void doNewGame() {
        gameModel.initGameLogic();
        currentPlayer = white;
    }

    /**
     * A process which analyzes the message from the player white after the game
     * finished. As there are 2 players in the game this function only processes
     * the messages from player that plays with the white pieces.
     *
     * @return true if there is connection issue and the message is null.
     */
    private boolean processThePlayerWhiteMessage() {
        Message msg;

        if (white instanceof PlayerNetwork) {
            msg = ((PlayerNetwork) white).readMessage();
            if (msg == null) {
                return true;
            }
            if (msg.getMsg().equals(Const.COUNTDOWN_FINISHED)) {
                white.executeMessage(Const.START_NEW_GAME);
            }
        }
        return false;
    }

    /**
     * A process which analyzes the message from the player black after the game
     * finished. As there are 2 players in the game this function only processes
     * the messages from player that plays with the black pieces.
     *
     * @return true if there is connection issue and the message is null.
     */
    private boolean processThePlayerBlackMessage() {
        Message msg;

        if (black instanceof PlayerNetwork) {
            msg = ((PlayerNetwork) black).readMessage();
            if (msg == null) {
                return true;
            }
            if (msg.getMsg().equals(Const.COUNTDOWN_FINISHED)) {
                black.executeMessage(Const.START_NEW_GAME);
            }
        }
        return false;
    }

//    private void isUnderCheck(boolean check) {
//        if (check) {
//            if (currentPlayer instanceof PlayerNetwork) {
//                ((PlayerNetwork) currentPlayer).writeMessage(Const.CHECK);
//            }
//        }
//    }
    /**
     * Configuration weather the game is over or continues after every full move
     * was made on the board. After finishing the game, the server will
     * automatically start a count down for a new game between 2 players. When
     * the game is over and one of the players won, the match will be submitted
     * into the database that holds the game history of both players.
     *
     * @return true if the game ended, false if the game continues.
     */
    private boolean isGameOver() {
        if (gameModel.isGameOver()) {
            currentPlayer.executeMessage(Const.DEFEATE);
            getOpponent().executeMessage(Const.VICTORY);
            DB.insertGameToDB(white, black, getOpponent());
            return true;
        }
        return false;
    }

    /**
     * Gets the opponent of the player that currently has to move, or player
     * that has his turn to move.
     *
     * @return the player that waits for the partner to play a move.
     */
    private Player getOpponent() {
        return currentPlayer == white ? black : white;
    }

    /**
     * This function switches turns between players, this is used only when a
     * player finished his turn completely
     */
    private void swapTurn() {
        if (currentPlayer == white) {
            currentPlayer = black;
        } else {
            currentPlayer = white;
        }
    }

    /**
     * Gives instructions to update the visual boards for the players in the
     * client section, update the view.
     *
     * @param move contains a piece, location, where it moves to update the
     * board for both players.
     */
    private void updateGraphicalBoard(Move move) {
        white.doBoardUpdate(move);
        black.doBoardUpdate(move);
    }
}
