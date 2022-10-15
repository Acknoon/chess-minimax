/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

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
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

/**
 *
 * @author Ariel
 */
public class View {
//    The Default Constant Values
//    ======================================================

    public static final String GUI_TITLE = "My Chess Game(ver4)";
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

    public static final int BUTTON_SIZE = 85;

    public static final Color BUTTON_BORDER_COLOR = Color.GRAY;
    public static final Color DARK_SQUARE = new Color(153, 102, 0);
    public static final Color LIGHT_SQUARE = new Color(255, 255, 190);
    public static final Color HIGHLIGHT_LIGHT_SQUARE = new Color(48, 249, 2);
    public static final Color HIGHLIGHT_DARK_SQUARE = new Color(40, 160, 12);
    public static final Color SELECTED_LIGHT_SQUARE = new Color(255, 191, 2);
    public static final Color SELECTED_DARK_SQUARE = new Color(255, 128, 12);

//    The Attributes of the View  
//    =======================================================   
    private JFrame view;
    private JMenuBar menuBar;
    private JButton undoButton;
    private JButton btnNewGame;
    private JButton btnComputer;
    private JButton[][] squareButtons;
    private JLabel lblMsg;
    private JPanel pnlCommands;
    private JPanel pnlSquares;
    private JDialog promotionDialog;
    private final int boardSize;

    public View(int boardSize) {
        this.boardSize = boardSize;
        createGameGui();
    }

    /**
     * Creates the Window and all the visual of the GUI
     */
    private void createGameGui() {

        this.createWindow();
        this.createBoardGame();
        this.createMenuBar();
        this.createCommandButtons();
        this.createMessageLabel();

        //Adding the requirments to the window
        view.setJMenuBar(menuBar);
        view.add(pnlCommands, BorderLayout.NORTH);
        view.add(pnlSquares, BorderLayout.CENTER);
        view.add(lblMsg, BorderLayout.SOUTH);

        //Addressing the Size and Locatiob relative to the screen
        view.pack();
        view.setVisible(true);
        view.setLocationRelativeTo(null);
    }

    /**
     * creates the window
     */
    private void createWindow() {
        view = new JFrame(GUI_TITLE);
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

    private void createMenuBar()
    {
        // יצירת 
        menuBar = new JMenuBar();

        // יצירת תפרטים ראשיים
        JMenu menu1 = new JMenu("About");
        JMenu menu2 = new JMenu("Game");

        // הוספת כל התפריטים הראשיים לבר התפריטים
        menuBar.add(menu1); // About menu
        menuBar.add(menu2); // Game menu

        // About יצירת תת תפריטים עבור התפריט הראשי
        JMenuItem menuItem1_1 = new JMenuItem("Game Rules");
        menuItem1_1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = "About the Game";

                String msg = "Chess Game Rules not supported yet\n";

                showMessage(title, msg);
            }
        });
        JMenuItem menuItem1_2 = new JMenuItem("Credits");
        menuItem1_2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = "Credits";

                String msg = "<html><u>Programming:</u></html>\n";
                msg += "1. Ariel Mazar (mazarariel@gmail.com)\n";
                msg += "2. Kiryat Noaar Students.\n\n";

                msg += "<html><u>GUI & Graphics:</u></html>\n";
                msg += "1. GUI Designer - Ariel Mazar.\n";
                msg += "<html><span color='blue'>All rights reserved (c) 2018</span></html>\n";

                showMessage(title, msg);
            }
        });
        menu1.add(menuItem1_1);
        menu1.add(menuItem1_2);

        // Game יצירת תת תפריטים עבור התפריט הראשי
        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButtonMenuItem humanStarter = new JRadioButtonMenuItem("Human To Start");
        JRadioButtonMenuItem compStarter = new JRadioButtonMenuItem("Computer To Start");
        radioGroup.add(humanStarter);
        radioGroup.add(compStarter);
        humanStarter.setSelected(true);

        humanStarter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Controller.setStartPlayer(true);
            }
        });
        compStarter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Controller.setStartPlayer(false);
            }
        });
        
        menu2.add(humanStarter);
        menu2.add(compStarter);
        

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
                btn.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                btn.setFocusable(false);
                btn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, 1));
                changeColorSquare(indexRow, indexCol, btn);

                //Addtional method for each of the Buttons of the game created
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton btn = (JButton) e.getSource();
                        int indexRow = (int) btn.getClientProperty("rowIndex");
                        int indexCol = (int) btn.getClientProperty("colIndex");
                        Controller.humanMove(indexRow, indexCol);
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
    }

    
    private void createCommandButtons()
    {
        btnNewGame = new JButton("New Game");
        btnNewGame.setFocusable(false);
        btnNewGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Controller.initGame();
            }
        });

        // יצירת  כפתור לצורך ביצוע מהלך שחקן ממוחשב
        btnComputer = new JButton("Computer Move");
        btnComputer.setFocusable(false);
        btnComputer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Controller.computerMove();
            }
        });

        
        undoButton = new JButton("Undo");
        undoButton.setFocusable(false);
        undoButton.setEnabled(false);
        undoButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Controller.undoButton();
            }
        });
        
        
        
        // יצירת פאנל עבור הכפתוורים של משחק חדש ומהלך שחקן ממוחשב
        pnlCommands = new JPanel(new GridLayout(1, 2, 10, 10));
        pnlCommands.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlCommands.add(undoButton);
        pnlCommands.add(btnNewGame);
        pnlCommands.add(btnComputer);

    }
    
    /**
     * Creates a message label below the game that updates the player that plays
     * the game
     */
    private void createMessageLabel() {
        //Addtion to show the users updates about the game
        lblMsg = new JLabel();
        lblMsg.setOpaque(true);
        lblMsg.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        lblMsg.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        lblMsg.setForeground(Color.BLACK);
        lblMsg.setText("Player White's Turn");
    }

    public void initGameGUI() {
        unlockAllBoardButtons();
        for (int indexRow = 0; indexRow < boardSize; indexRow++) {
            for (int indexCol = 0; indexCol < boardSize; indexCol++) {
                if (Controller.getSquare(indexRow, indexCol) == null)
                    setButtonIconAt(indexRow, indexCol);
                else
                    setButtonIconAt(Controller.getSquare(indexRow, indexCol));
            }
        }
        lblMsg.setText("Player White's Turn");
        setHighlightOff();
    }

    /**
     * Simple function to be used for controller to close the Window
     */
    public void close() {
        view.dispose();
    }

    public void setButtonIconAt(Piece player) {
        if (player != null) {
            int row = player.getRow();
            int col = player.getCol();
            ImageIcon playerIcon = player.getCurrentPieceIcon();
            squareButtons[row][col].setIcon(playerIcon);
            squareButtons[row][col].setDisabledIcon(playerIcon);
        }
    }

    public void setButtonIconAt(int indexRow, int indexCol) {
        squareButtons[indexRow][indexCol].setIcon(null);
        squareButtons[indexRow][indexCol].setDisabledIcon(null);
    }

    /**
     * Function that shows the promotion Dialog
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

    public char getPromotedPiece(char piece){
        return piece;
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
            if (squareButtons[row][col].getBackground() == LIGHT_SQUARE) {
                squareButtons[row][col].setBackground(HIGHLIGHT_LIGHT_SQUARE);
            }
            if (squareButtons[row][col].getBackground() == DARK_SQUARE) {
                squareButtons[row][col].setBackground(HIGHLIGHT_DARK_SQUARE);
            }
        }
    }

    public void highlightSelectedPiece(Square s){
            int row = s.getRow();
            int col = s.getCol();
            if (squareButtons[row][col].getBackground() == LIGHT_SQUARE) {
                squareButtons[row][col].setBackground(SELECTED_LIGHT_SQUARE);
            }
            if (squareButtons[row][col].getBackground() == DARK_SQUARE) {
                squareButtons[row][col].setBackground(SELECTED_DARK_SQUARE);
            }
    
    }
    
    /**
     * Turns off the highlighted moves
     */
    public void setHighlightOff() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (squareButtons[row][col].getBackground() == HIGHLIGHT_LIGHT_SQUARE) {
                    squareButtons[row][col].setBackground(LIGHT_SQUARE);
                }
                if (squareButtons[row][col].getBackground() == HIGHLIGHT_DARK_SQUARE) {
                    squareButtons[row][col].setBackground(DARK_SQUARE);
                }
                
                if (squareButtons[row][col].getBackground() == SELECTED_LIGHT_SQUARE) {
                    squareButtons[row][col].setBackground(LIGHT_SQUARE);
                }
                if (squareButtons[row][col].getBackground() == SELECTED_DARK_SQUARE) {
                    squareButtons[row][col].setBackground(DARK_SQUARE);
                }
            }
        }

    }

    /**
     * Just changes the text label under the board
     */
    public void switchPlayerTextTurn() {
        if (Piece.PLAYER_WHITE == Controller.getTurn()) {
            lblMsg.setText("Player White's Turn");
        } else {
            lblMsg.setText("Player Black's Turn");
        }
    }
    
    /**
     * Just changes the text label under the board
     * @param player the winning player
     */
    public void changeTextLabelToWinner(int player) {
        if (Piece.PLAYER_WHITE == player) {
            lblMsg.setText("Player Black WINS!!!!!!");
        } else {
            lblMsg.setText("Player White WINS!!!!!!");
        }
    }
    
     /**
     * Just changes the text label under the board
     */
    public void changeTextLabelToTie() {
        lblMsg.setText("DRAW!");
    }
    
    private void showMessage(String title, String msg)
    {
        UIManager.put("OptionPane.okButtonText", "OK");
        JOptionPane.showMessageDialog(view, msg, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Function that builds the button inside the Dialog
     * @param btn the actual button that we sent
     * @param i which button is it
     */
    private void createChoiceButton(JButton btn, int i) {
        ImageIcon playerIcon = null;
        switch (i) {
            case 0:
                playerIcon = Utils.WQueen;
                break;
            case 1:
                playerIcon = Utils.WRook;
                break;
            case 2:
                playerIcon = Utils.WBishop;
                break;
            case 3:
                playerIcon = Utils.WKnight;
                break;
        }

        btn.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, 1));
        btn.setIcon(playerIcon);
        btn.setDisabledIcon(playerIcon);
        btn.setBackground(Color.GRAY);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Controller.promotePiece(translatePieceValue(i));
                promotionDialog.dispose();
            }
        });
    }

    /**
     * Translates the button that has been picked to which piece it is
     * @param value the value of which button has been pressed
     * @return the symbol of the piece
     */
    private char translatePieceValue(int value){
        switch(value){
            case 0:
                return Model.QUEEN;
            case 1:
                return Model.ROOK;
            case 2:
                return Model.BISHOP;
            case 3:
                return Model.KNIGHT;
        }
        return Model.QUEEN;
    }
    
    /**
     * Additional message for the player if he wants to exit the game
     */
    private void closeAndExitGame() {
        String title = "Close & Exit Game";
        String msg = "Do you really want to Exit?";
        int res = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION);

        if (res == JOptionPane.YES_OPTION) {
            Controller.closeAndExitGame();
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
                btn.setBackground(LIGHT_SQUARE);
            } else {
                btn.setBackground(DARK_SQUARE);
            }
        } else if (indexCol % 2 == 0) {
            btn.setBackground(DARK_SQUARE);
        } else {
            btn.setBackground(LIGHT_SQUARE);
        }
    }
    
    /**
     * Function locks all the buttons of the board
     */
    public void lockAllBoardButtons()
    {
        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                squareButtons[row][col].setEnabled(false);
            }
        }
    }
    
    
     /**
     * Function unlocks all the buttons of the board
     */
    public void unlockAllBoardButtons()
    {
        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++)
            {
                squareButtons[row][col].setEnabled(true);
            }
        }
    }
    
    /**
     * Function unlocks the undo button
     */
    public void unlockUndoButton(){
        undoButton.setEnabled(true);
    }
    
    /**
     * Function locks the undo button
     */
    public void lockUndoButton(){
        undoButton.setEnabled(false);
    }
}
