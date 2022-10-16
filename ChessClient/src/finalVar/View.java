/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalVar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Ariel
 */
public class View {
//    The Default Constant Values
//    ======================================================

//    The Attributes of the View  
//    =======================================================   
    private JFrame view;
    private JButton[][] squareButtons;
    private JLabel lblTransmission;
    private JPanel pnlSquares;
    private JDialog promotionDialog;
    private JMenuBar menuBar;
    private final int boardSize;
    private String playerColor;

    public View() {
        this.boardSize = Const.DEFAULT_BOARD_SIZE;
        createGameGui();
    }

    /**
     * Creates the Window and all the visual of the GUI
     */
    private void createGameGui() {
        this.createWindow();
        this.createBoardGame();
        this.createMessageLabel();
        this.createMenuBar();
//        this.createCommandButtons();

        //Adding the requirments to the window
//        view.add(pnlCommands, BorderLayout.NORTH);
        view.add(pnlSquares, BorderLayout.CENTER);
        view.add(lblTransmission, BorderLayout.SOUTH);
        view.add(menuBar, BorderLayout.NORTH);

        //Addressing the Size and Location relative to the screen
        view.pack();
        view.setVisible(true);
        view.setLocationRelativeTo(null);

    }

    /**
     * creates the window
     */
    private void createWindow() {
        view = new JFrame(Const.GUI_TITLE);
        view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        view.setAlwaysOnTop(true);
        view.setResizable(false);
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAndExitGame();
            }
        });
    }

    private void createMenuBar() {
        // יצירת 
        menuBar = new JMenuBar();

        // יצירת תפרטים ראשיים
        JMenu menu = new JMenu("Statistics");

        // הוספת כל התפריטים הראשיים לבר התפריטים
        menuBar.add(menu); // About menu

        // About יצירת תת תפריטים עבור התפריט הראשי
        JMenuItem menuItem1_1 = new JMenuItem("Records");
        menuItem1_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientChess.writeRequestsPermissions(Const.STATISTICS);
            }
        });
        menu.add(menuItem1_1);
    }

    /**
     * creates the chess board
     */
    private void createBoardGame() {
//        ============================================
//          Creation of the Board
//        ============================================
        squareButtons = new JButton[boardSize][boardSize];
        pnlSquares = new JPanel(new GridLayout(boardSize, boardSize));
        for (int indexRow = 0; indexRow < boardSize; indexRow++) {
            for (int indexCol = 0; indexCol < boardSize; indexCol++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(Const.BUTTON_SIZE, Const.BUTTON_SIZE));
                btn.setFocusable(false);
                btn.setBorder(BorderFactory.createLineBorder(Const.BUTTON_BORDER_COLOR, 1));
                changeColorSquare(indexRow, indexCol, btn);

                //Addtional method for each of the Buttons of the game created
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton btn = (JButton) e.getSource();
                        int indexRow = (int) btn.getClientProperty("rowIndex");
                        int indexCol = (int) btn.getClientProperty("colIndex");
                        setHighlightSquare(new Square(indexRow, indexCol));
                        ClientChess.squareSelected(indexRow, indexCol);
                    }
                });

                //Gives to the buttons the coordinates of the index they are in the matrix
                btn.putClientProperty("rowIndex", indexRow);
                btn.putClientProperty("colIndex", indexCol);

                //Adding the Buttons inside the Panel and the Button System together
                pnlSquares.add(btn);
                squareButtons[indexRow][indexCol] = btn;
            }
        }
        fillPiecesToBoard();
    }

    /**
     * Creates a message label below the game that updates the player that plays
     * the game
     */
    private void createMessageLabel() {
        //Addtion to show the users updates about the game
        lblTransmission = new JLabel();
        lblTransmission.setOpaque(true);
        lblTransmission.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        lblTransmission.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        lblTransmission.setForeground(Color.BLACK);
        lblTransmission.setText("    ");
    }

    /**
     * Initializes the view, or restarts the UI of the game.
     */
    public void initGameGUI() {
        lockAllBoardButtons();
        fillPiecesToBoard();
        setOriginalBGColor();
    }

    /**
     * Function that fills the piece images on the chess board.
     */
    public void fillPiecesToBoard() {
        lockAllBoardButtons();
        for (int indexRow = 0; indexRow < boardSize; indexRow++) {
            for (int indexCol = 0; indexCol < boardSize; indexCol++) {
                fillPiecetoMatrix(indexRow, indexCol);
            }
        }
    }

    /**
     * Simple function to be used for controller to close the Window.
     */
    public void close() {
        view.dispose();
    }

    /**
     * Sets an icon or an image of a piece on certain location on the chess
     * board.
     *
     * @param Icon the request or give icon to submit.
     * @param row the row location on the chess board.
     * @param col the colum location on the chess board.
     */
    public void setButtonIconAt(ImageIcon Icon, int row, int col) {
        squareButtons[row][col].setIcon(Icon);
        squareButtons[row][col].setDisabledIcon(Icon);
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
                setButtonIconAt(Utils.imageTranslation(Const.PLAYER_BLACK * Const.PAWN), indexRow, indexCol);
                break;
            case Const.SECOND_ROW:
                setButtonIconAt(Utils.imageTranslation(Const.PAWN), indexRow, indexCol);
                break;
            case Const.FIRST_ROW:
                fillComplexPieces(Const.PLAYER_WHITE, indexRow, indexCol);
                break;
            default:
                setButtonIconAt(null, indexRow, indexCol);
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
                setButtonIconAt(Utils.imageTranslation(Const.ROOK * theType), indexRow, indexCol);
                break;
            case 1:
                setButtonIconAt(Utils.imageTranslation(Const.KNIGHT * theType), indexRow, indexCol);
                break;
            case 2:
                setButtonIconAt(Utils.imageTranslation(Const.BISHOP * theType), indexRow, indexCol);
                break;
            case 3:
                setButtonIconAt(Utils.imageTranslation(Const.QUEEN * theType), indexRow, indexCol);
                break;
            case 4:
                setButtonIconAt(Utils.imageTranslation(Const.KING * theType), indexRow, indexCol);
                break;
            case 5:
                setButtonIconAt(Utils.imageTranslation(Const.BISHOP * theType), indexRow, indexCol);
                break;
            case 6:
                setButtonIconAt(Utils.imageTranslation(Const.KNIGHT * theType), indexRow, indexCol);
                break;
            case 7:
                setButtonIconAt(Utils.imageTranslation(Const.ROOK * theType), indexRow, indexCol);
                break;

        }
    }

    /**
     * Visual function that shows the background color change, whenever a piece
     * been selected during the game.
     *
     * @param s the square on the chess board going to be highlighted.
     */
    public void setHighlightSquare(Square s) {
        int row = s.getRow();
        int col = s.getCol();
        if (squareButtons[row][col].getBackground() == Const.LIGHT_SQUARE) {
            squareButtons[row][col].setBackground(Const.SELECTED_LIGHT_SQUARE);
        }
        if (squareButtons[row][col].getBackground() == Const.DARK_SQUARE) {
            squareButtons[row][col].setBackground(Const.SELECTED_DARK_SQUARE);
        }

    }

    /**
     * Shows a dialog at the start of the program, the dialog inputs the address
     * needed to connect to the server through the port and giving the server IP
     * address to connect to the client.
     *
     * @param IP The IP address of the current user needed to connect to the
     * server.
     * @param port The server port needed to connect to.
     * @return Both IP address and Server port to store in the server.
     */
    public String getInputPort(String IP, int port) {
        return JOptionPane.showInputDialog(view, "Enter SERVER Address [IP : PORT]", IP + " : " + port);
    }

    /**
     * Shows an error exception when a connection issues happened.
     *
     * @param msg which type of issue being displayed as.
     */
    public void errorMessage(String msg) {
        Utils.showErrMsgToUser(view, "Game Client Error", msg);
    }

    /**
     * Shows a message dialog that presents as a table of a player game history
     * to view.
     *
     * @param stats the statistics of a player game history.
     */
    public void showStatisticsDialog(String stats) {
        String title = "Game Statistics!";
        stats = "game\t white\t score\t black\t date\n\n" + stats;
        JOptionPane.showMessageDialog(view, stats, title, JOptionPane.CLOSED_OPTION);
    }

    /**
     * Function that shows the promotion Dialog to the user and gives the user
     * to option to promote the pawn to a piece the options are Knight, Bishop,
     * Rook, Queen.
     */
    public void showPromotionDialog() {
        String title = "Pawn Promoted!";
        promotionDialog = new JDialog(view, title);
        JPanel pnl = new JPanel(new GridLayout(1, 4));
        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton();
            createChoiceButton(btn, i);
            pnl.add(btn);
        }
        promotionDialog.add(pnl);
        promotionDialog.setModal(true);
        promotionDialog.setAlwaysOnTop(true);
        promotionDialog.setResizable(false);
        promotionDialog.pack();
        promotionDialog.setLocationRelativeTo(null);
        promotionDialog.setVisible(true);
    }

    /**
     * Construction of the Pawn Promotion dialog.
     *
     * @param btn the actual button that we sent
     * @param i which button is it
     */
    private void createChoiceButton(JButton btn, int i) {
        ImageIcon playerIcon = null;
        switch (i) {
            case 0: //if the player chose 0 which the first button that represents a Queen piece 
                playerIcon = playerColor.equals(Const.WHITE) ? Utils.WQueen : Utils.BQueen;
                break;
            case 1: //if the player chose 0 which the first button that represents a Rook piece
                playerIcon = playerColor.equals(Const.WHITE) ? Utils.WRook : Utils.BRook;
                break;
            case 2: //if the player chose 0 which the first button that represents a Bishop piece
                playerIcon = playerColor.equals(Const.WHITE) ? Utils.WBishop : Utils.BBishop;
                break;
            case 3: //if the player chose 0 which the first button that represents a Knight piece
                playerIcon = playerColor.equals(Const.WHITE) ? Utils.WKnight : Utils.BKnight;
                break;
        }

        btn.setPreferredSize(new Dimension(Const.BUTTON_SIZE, Const.BUTTON_SIZE));
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Const.BUTTON_BORDER_COLOR, 1));
        btn.setIcon(playerIcon);
        btn.setDisabledIcon(playerIcon);
        btn.setBackground(Color.GRAY);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ClientChess.promotePiece(translatePieceValue(i));
                promotionDialog.dispose();
            }
        });
    }

    /**
     * Translates the button that has been picked to which piece it is
     *
     * @param value the value of which button has been pressed
     * @return the symbol of the piece
     */
    private String translatePieceValue(int value) {
        switch (value) {
            case 0:
                return Const.TAG_QUEEN;
            case 1:
                return Const.TAG_ROOK;
            case 2:
                return Const.TAG_BISHOP;
            case 3:
                return Const.TAG_KNIGHT;
        }
        return Const.TAG_QUEEN;
    }

    /**
     * Sets the GUI title welcoming the new player as a registered user.
     *
     * @param userName the name of the user been registered from the match of
     * the database or simply being a guest.
     * @param playerSign the sign of the player plays a set of pieces.
     */
    public void welcome(String userName, String playerSign) {
        playerColor = playerSign;
        setTitle("Chess Client - " + userName + " (" + playerSign + " Player)");
    }

    /**
     * changes the status bar and makes the player idle until he gets a human
     * partner to start a game with.
     */
    public void waitForPartner() {
        lockAllBoardButtons();
        setStatus("WAIT for partner!");
    }

    /**
     * Starts a new game when the previous game ended.
     */
    public void startNewGame() {
        initGameGUI();
    }

    /**
     * unlocks the player the option to play his turn in his chess game.
     *
     * @param list list of pieces a player can select.
     */
    public void playTurn(ArrayList<Square> list) {
        unlockBoardButtons(list);
        setStatus("It's your turn, Play!");
    }

    /**
     * A player that is warned being under attack on the king.
     *
     * @param msg warning message that the king is attacked.
     */
    public void inCheck(String msg) {
        setStatus(msg, Color.WHITE, Color.RED);
    }

    /**
     * Makes the player idle until player's opponent makes a move to pass the
     * turn.
     */
    public void waitTurn() {
        setOriginalBGColor();
        lockAllBoardButtons();
        setStatus("WAIT it's your partner turn!");
    }

    /**
     * Additional message for the player if he wants to exit the game
     */
    private void closeAndExitGame() {
        String title = "Close & Exit Game";
        String msg = "Do you really want to Exit?";
        int result = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            ClientChess.closeAndExitGame();
        }
    }

    /**
     * Function that colors the chess board with light and dark Squares
     *
     * @param indexRow index of the row on the board
     * @param indexCol index of colum on the board
     * @param btn the current button being created we color
     */
    private void changeColorSquare(int indexRow, int indexCol, JButton btn) {
        if (indexRow % 2 == 0) {
            if (indexCol % 2 == 0) {
                btn.setBackground(Const.LIGHT_SQUARE);
            } else {
                btn.setBackground(Const.DARK_SQUARE);
            }
        } else if (indexCol % 2 == 0) {
            btn.setBackground(Const.DARK_SQUARE);
        } else {
            btn.setBackground(Const.LIGHT_SQUARE);
        }
    }

    /**
     * Function locks all the buttons of the board
     */
    public void lockAllBoardButtons() {
//        if (squareButtons[7][7] == null) {
//            System.out.println("It's null");
//        }
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                squareButtons[row][col].setEnabled(false);
            }
        }
    }

    /**
     * Function unlocks all the buttons of the board
     */
    public void unlockAllBoardButtons() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                squareButtons[row][col].setEnabled(true);
            }
        }
    }

    /**
     * Gives access to the frame of the game.
     *
     * @return the window of the game.
     */
    public JFrame getWin() {
        return view;
    }

    /**
     * Gives access to the title of the player's client
     *
     * @return title of the player's client.
     */
    public String getTitle() {
        return view.getTitle();
    }

    /**
     * Gives access to what the message is in the status Bar.
     *
     * @return the String message in the status bar.
     */
    public String getStatus() {
        return lblTransmission.getText();
    }

    /**
     * Freezes the game as it ended and starts a countdown to start a new game.
     *
     * @param msg the result of the match between players.
     */
    public void gameOver(String msg) {
        lockAllBoardButtons();
        setOriginalBGColor();
        countDownMsg("You are " + msg + " \nGAME OVER - New Game will start in", Const.GAME_OVER_COUNTDOWN_TIME);
    }

    /**
     * changes the title of the client's GUI.
     *
     * @param string new title to replace the old one.
     */
    public void setTitle(String string) {
        view.setTitle(string);
    }

    /**
     * Closes the client
     */
    public void connectionToServerLost() {
        close();
    }

    /**
     * Enables the login dialog as it won't allow the player to start the game
     * without logging in.
     */
    public void login() {
        lockAllBoardButtons();
        setOriginalBGColor();
        setStatus("Player Login...");
    }

    /**
     * Changes and sets a message inside the status bar.
     * @param msg the new message that states the game condition currently.
     */
    public void setStatus(String msg) {
        setStatus(msg, null, null);
    }

    /**
     * Changes and sets a message inside the status bar.
     * @param msg the new message that states the game condition currently.
     * @param fg the foreground color of the status bar
     * @param bg the background color of the status bar
     */
    public void setStatus(String msg, Color fg, Color bg) {
        lblTransmission.setForeground(fg);
        lblTransmission.setBackground(bg);
        lblTransmission.setText(msg);
    }

    /**
     * Function that highlights the moves the piece can move
     *
     * @param list list of moves
     */
    public void setHighlightOn(ArrayList<Square> list) {
        for (int i = 0; i < list.size(); i++) {
            int row = list.get(i).getRow();
            int col = list.get(i).getCol();
            if (squareButtons[row][col].getBackground() == Const.LIGHT_SQUARE) {
                squareButtons[row][col].setBackground(Const.HIGHLIGHT_LIGHT_SQUARE);
            }
            if (squareButtons[row][col].getBackground() == Const.DARK_SQUARE) {
                squareButtons[row][col].setBackground(Const.HIGHLIGHT_DARK_SQUARE);
            }
        }
    }

    /**
     * Turns off the highlighted moves
     */
    public void setOriginalBGColor() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (squareButtons[row][col].getBackground() == Const.HIGHLIGHT_LIGHT_SQUARE) {
                    squareButtons[row][col].setBackground(Const.LIGHT_SQUARE);
                }
                if (squareButtons[row][col].getBackground() == Const.HIGHLIGHT_DARK_SQUARE) {
                    squareButtons[row][col].setBackground(Const.DARK_SQUARE);
                }

                if (squareButtons[row][col].getBackground() == Const.SELECTED_LIGHT_SQUARE) {
                    squareButtons[row][col].setBackground(Const.LIGHT_SQUARE);
                }
                if (squareButtons[row][col].getBackground() == Const.SELECTED_DARK_SQUARE) {
                    squareButtons[row][col].setBackground(Const.DARK_SQUARE);
                }
            }
        }
    }

    // countdown timer - is it a Thread? is it implemented in View or in Client?
    public void countDownMsg(String msg, int timeInSeconds) {
        for (int t = timeInSeconds; t >= 0; t--) {
            setStatus(msg + " " + t + " sec...", Color.MAGENTA, Color.YELLOW);
            Utils.sleep(1000);
        }
    }

    public void makeMove(Square pos, Square destinationPos, int piece) {
        ImageIcon icon = Utils.imageTranslation(piece);
        setButtonIconAt(icon, destinationPos.getRow(), destinationPos.getCol());
        setButtonIconAt(null, pos.getRow(), pos.getCol());
    }

    /**
     * Unlocks the chess squares for the user to click on
     * @param list list on squares to unlock the squares to click on
     */
    public void unlockBoardButtons(ArrayList<Square> list) {
        for (Square squares : list) {
            squareButtons[squares.getRow()][squares.getCol()].setEnabled(true);
        }
    }

    /**
     * locks the chess squares for the user to click on
     * @param list list on squares to lock the squares to click on
     */
    public void lockBoardButtons(ArrayList<Square> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (Square squares : list) {
                    squareButtons[squares.getRow()][squares.getCol()].setEnabled(false);
                }
            }
        }
    }

    /**
     * A dialog that asks the player to play with a machine or a human
     * @return AI if the user pressed yes and Human if player pressed no.
     */
    public String dialogAIRequest() {
        int res = Utils.showConfirmDialog(view, "AI?", "Do you want to play with an AI?");
        return res == JOptionPane.YES_OPTION ? Const.AI_CHOICE : Const.HUMAN_CHOICE;
    }
}
