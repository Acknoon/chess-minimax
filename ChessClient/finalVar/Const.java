package finalVar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Class that contains all important constant variable. As those variables exist
 * they make the program easier to read and understand for a Programmer. This is
 * a static class and must be synchronized between client and server in order to
 * maintain fully operating state. Here lies the explanation of all the static
 * variables that are scattered around the project.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class Const {

    /*==============================Code Attributes==============================*/
    public static final String LOGIN = "#LOGIN";//A message to tell the Client to activate the UI of the Login dialog.
    public static final String WELCOME = "#WELCOME";//Simple plain message which is sent along with player name and sign.
                                                                                      //The purpose of the message is to change the title to the name of the user.
    public static final String PLAY_TURN = "#PLAY_TURN";//Message which allows the player make his move on the board.
                                                                                          //However, the player can only select the pieces that are avaliable.
                                                                                          //It's a first part process of making a full move.
    public static final String WAIT_TURN = "#WAIT_TURN";//Message which restricts the player from making a move.
    public static final String WAIT_FOR_PARTNER = "#WAIT_FOR_PARTNER";//Message to inform in status bar of the UI of the client.
                                                                                                                          //To wait for another human partner to connect while,
                                                                                                                          //makeing the player idle.
    public static final String GAME_OVER = "#GAME_OVER";//Message that is sent when the game finished with the result of a draw!
    public static final String START_NEW_GAME = "#NEW_GAME";//Message that allows the game to be reset visually.
    public static final String LOCK_BUTTON = "#LOCK_BUTTON";//Gui control message to prevent the player from playing
                                                                                                     //as it locks the board
    public static final String CLIENT_EXIT = "#CLIENT_EXIT";//Message informs that the client exists the game or the server connection.
    public static final String PARTNER_EXIT = "#PARTNER_EXIT";//A message that informs that the partner of the player lost connection.
    public static final String COUNTDOWN_FINISHED = "#COUNTDOWN_FINISHED";//Message that confirms the count down timer finished.
    public static final String SELECT_PIECE = "#SELECT_PIECE";//Selects visually the piece that was seleced by the partner of the player.
    public static final String SELECT_SQUARE = "#SELECT_SQUARE";//Out of the selected piece the player chose,
                                                                                                             //now stands the choose which squre to move the selected piece.
    public static final String MAKE_MOVE = "#MAKE_MOVE";//Finalizes the move and makes the visual update on the board.
    public static final String VICTORY = "#VICTORY";//Shows a victory message in the status bar and finishes the game with a victory result.
    public static final String DEFEATE = "#DEFEATE";//Shows a defeate message in status bar and finishes the game with lose result.
    public static final String AI_REQUEST = "#AI_REQUEST";//Shows the user a dialog of choice between human player and a computer.
    public static final String AI_CHOICE = "#AI_CHOICE";//message that confirms the choice to play against the computer.
    public static final String HUMAN_CHOICE = "#HUMAN_CHOICE";//message that confirms the choice to play against a human player.
    public static final String CHECK = "#CHECK";//Sends the message that the king was being checked.
    public static final String STATISTICS = "#STATISTICS";//Shows the statistics of the player that is currently playing.
    public static final String PROMOTION = "#PROMOTED";//Shows the dialog that the pawn,
                                                                                           //is being promoted and given choice to promote to a piece.
    
    public static final String TAG_PAWN = "Pawn";//Stroes the name of Pawn when tagged.
    public static final String TAG_KNIGHT = "Knight";//Stroes the name of Knight when tagged.
    public static final String TAG_BISHOP = "Bishop";//Stroes the name of Bishop when tagged.
    public static final String TAG_ROOK = "Rook";//Stroes the name of Rook when tagged.
    public static final String TAG_QUEEN = "Queen";//Stroes the name of Queen when tagged.
    public static final String TAG_KING = "King";//Stroes the name of King when tagged.
    
    public static String BLACK = "BLACK";//Stores the identity of pieces which could be black pieces.
    public static String WHITE = "WHITE";//Stores the identity of pieces which could be white pieces.
    public static String AI = "AI";//Type of Identity which in this case AI.
    public static String GUEST = "GUEST";//Type of Identity which in this case a Guest.
    public static final String PLAYER = "PLAYER";//Type of Identity which in this case a Player.

    public static final int GAME_OVER_COUNTDOWN_TIME = 10;  // seconds
    public static final int GET_READY_COUNTDOWN_TIME = 10;  // seconds

    /*==============================Attributes for Server============================*/
    public static final int DEFAULT_SERVER_PORT = 3333;
    public static final Dimension WIN_SIZE = new Dimension(1000, 500);
    public static final Color LOG_BG_COLOR = Color.BLACK;
    public static final Color LOG_FONT_COLOR = Color.GREEN;
    public static final Font LOG_FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);

    /*==============================Attributes for Client============================*/
    //imported images from the assets file to represent the pieces
    public static final String GUI_TITLE = "Chess Game";//Title of the gui when there is no client yet.
    public static final String WHITE_PAWN = "/assets/1Pawn.png";
    public static final String WHITE_KNIGHT = "/assets/1Knight.png";
    public static final String WHITE_BISHOP = "/assets/1Bishop.png";
    public static final String WHITE_ROOK = "/assets/1Rook.png";
    public static final String WHITE_QUEEN = "/assets/1Queen.png";
    public static final String WHITE_KING = "/assets/1King.png";
    public static final String BLACK_PAWN = "/assets/2Pawn.png";
    public static final String BLACK_KNIGHT = "/assets/2Knight.png";
    public static final String BLACK_BISHOP = "/assets/2Bishop.png";
    public static final String BLACK_ROOK = "/assets/2Rook.png";
    public static final String BLACK_QUEEN = "/assets/2Queen.png";
    public static final String BLACK_KING = "/assets/2King.png";

    //the Size of the Gui board and buttons
    public static final int DEFAULT_BOARD_SIZE = 8;
    public static final int BUTTON_SIZE = 85;

    //This is a coloration of the game board, the squares on the board
    public static final Color BUTTON_BORDER_COLOR = Color.GRAY;
    public static final Color DARK_SQUARE = new Color(153, 102, 0);
    public static final Color LIGHT_SQUARE = new Color(255, 255, 190);
    public static final Color HIGHLIGHT_LIGHT_SQUARE = new Color(48, 249, 2);
    public static final Color HIGHLIGHT_DARK_SQUARE = new Color(40, 160, 12);
    public static final Color SELECTED_LIGHT_SQUARE = new Color(255, 191, 2);
    public static final Color SELECTED_DARK_SQUARE = new Color(255, 128, 12);
    public static final Color BUTTON_LOCK_BG_COLOR = Color.LIGHT_GRAY;

    public static final int PLAYER_WHITE = 1;//Value that indicates the type of white pieces.
    public static final int PLAYER_BLACK = -1;//Value that indicates the type of black pieces.

    //Stores the value of each piece during the game,
    //this is to be used to translate the number of the value piece into an image board during the game.
    public static final int PAWN = 1;
    public static final int KNIGHT = 3;
    public static final int BISHOP = 4;
    public static final int ROOK = 5;
    public static final int QUEEN = 9;
    public static final int KING = 256;

    //Convenient use to understand which rows are relavent with the pawn pieces as they are unique.
    public static final int FIRST_ROW = 7;//First row is theortically the first row of white pieces.
    public static final int SECOND_ROW = 6;//Second row of the starting position of the white pawns.
    public static final int LAST_ROW = 0;//Last row is theortically the first row of black pieces.
    public static final int SEVENTH_ROW = 1;//Seventh row of the starting position of the black pawns.

    public static final int DEFAULT = 0;//Default score when calculating the position when starting minimax.
    public static final int WIN_SCORE = 1000;//A winning score when calculating a position.
    public static final long RUNTIME = 5 * 1000L;//The run time of the minimax.

    public static final boolean CHECK_FIRST = false;//Convenient status to ensure there won't be malfunction of the Move process.
}
