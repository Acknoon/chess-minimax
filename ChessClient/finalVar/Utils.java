package finalVar;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Document : Utils Date : 18/10/2019 Author : Ariel Mazar
 * (mazarariel@gmail.com)
 */
public class Utils {

    public static final boolean SHOW_DEBUG_MESSAGES = false;    // for log debug
    public static final boolean SHOW_DEBUG_WITH_DIALOG = false;   // for log debug
    public static ImageIcon WPawn, WKnight, WBishop, WRook, WQueen, WKing, BPawn, BBishop, BKnight, BRook, BQueen, BKing;

    public static void loadAssets() {
        int sizeButton = (int) (Const.BUTTON_SIZE * .90);

        //The White Pieces Assets
        WPawn = new ImageIcon(ClientChess.class.getResource(Const.WHITE_PAWN));
        WPawn = new ImageIcon(WPawn.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WBishop = new ImageIcon(ClientChess.class.getResource(Const.WHITE_BISHOP));
        WBishop = new ImageIcon(WBishop.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WKnight = new ImageIcon(ClientChess.class.getResource(Const.WHITE_KNIGHT));
        WKnight = new ImageIcon(WKnight.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WRook = new ImageIcon(ClientChess.class.getResource(Const.WHITE_ROOK));
        WRook = new ImageIcon(WRook.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WQueen = new ImageIcon(ClientChess.class.getResource(Const.WHITE_QUEEN));
        WQueen = new ImageIcon(WQueen.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WKing = new ImageIcon(ClientChess.class.getResource(Const.WHITE_KING));
        WKing = new ImageIcon(WKing.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));

        //The Black pieces assets
        BPawn = new ImageIcon(ClientChess.class.getResource(Const.BLACK_PAWN));
        BPawn = new ImageIcon(BPawn.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BBishop = new ImageIcon(ClientChess.class.getResource(Const.BLACK_BISHOP));
        BBishop = new ImageIcon(BBishop.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BKnight = new ImageIcon(ClientChess.class.getResource(Const.BLACK_KNIGHT));
        BKnight = new ImageIcon(BKnight.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BRook = new ImageIcon(ClientChess.class.getResource(Const.BLACK_ROOK));
        BRook = new ImageIcon(BRook.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BQueen = new ImageIcon(ClientChess.class.getResource(Const.BLACK_QUEEN));
        BQueen = new ImageIcon(BQueen.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BKing = new ImageIcon(ClientChess.class.getResource(Const.BLACK_KING));
        BKing = new ImageIcon(BKing.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
    }

    /**
     * A static function that translate special number of a piece into an image
     * @param code piece id number
     * @return image of that piece id.
     */
    public static ImageIcon imageTranslation(int code) {
        if (code > Const.PLAYER_BLACK) {
            switch (code) {
                case Const.PAWN:
                    return WPawn;
                case Const.KNIGHT:
                    return WKnight;
                case Const.BISHOP:
                    return WBishop;
                case Const.ROOK:
                    return WRook;
                case Const.QUEEN:
                    return WQueen;
                case Const.KING:
                    return WKing;
                default:
                    return null;
            }
        } else {
            code = code * Const.PLAYER_BLACK;
            switch (code) {
                case Const.PAWN:
                    return BPawn;
                case Const.KNIGHT:
                    return BKnight;
                case Const.BISHOP:
                    return BBishop;
                case Const.ROOK:
                    return BRook;
                case Const.QUEEN:
                    return BQueen;
                case Const.KING:
                    return BKing;
                default:
                    return null;
            }
        }
    }

    public static void debug(String msg) {
        if (SHOW_DEBUG_MESSAGES) {
            System.out.println(">>  " + msg);
        }
    }

    public static void debug(Exception ex) {
        if (SHOW_DEBUG_MESSAGES) {
            String errLine;
            String className = Utils.class.getPackage().getName();

            String errInfo = "Exception: " + ex + "\n";
            errInfo += "------------------------------------------------------------------\n";
            for (int i = ex.getStackTrace().length - 1; i >= 0; i--) {
                errLine = ex.getStackTrace()[i].toString();
                if (errLine.startsWith(className) && i > 0 && !ex.getStackTrace()[i - 1].toString().startsWith(className)) {
                    errInfo += ">>>>  " + errLine + "\n";
                } else {
                    errInfo += ">>  " + errLine + "\n";
                }
            }
            System.out.println("\n" + errInfo);
            if (SHOW_DEBUG_WITH_DIALOG) {
                showDebugDialog(errInfo);
            }
        }
    }
    
    public static String getOtherPlayer(String sign)
    {
        if(sign.equals(Const.WHITE))
        {
            return Const.BLACK;
        }
        return Const.WHITE;
    }
    
    public static int getOtherPlayer(int sign)
    {
        if(sign == Const.PLAYER_WHITE)
        {
            return Const.PLAYER_BLACK;
        }
        return Const.PLAYER_WHITE;
    }

    public static void showErrMsgToUser(JFrame win, String title, String msg) {
        JOptionPane.showMessageDialog(win, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showDebugDialog(String msg) {
        showDebugDialog(msg, null);
    }

    public static void showDebugDialog(String msg, JFrame win) {
        JOptionPane optionPane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Debug Message");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);  // WILL BLOCK the program
        dialog.dispose();
    }
    
    public static int showConfirmDialog(JFrame win, String title, String msg)
    {
        showWin(win);
        return JOptionPane.showConfirmDialog(win, msg, title, JOptionPane.YES_NO_OPTION);
    }

    public static void showWin(JFrame win) {
        // show the window, bring it to front, de-ICONIFIED it
        win.setVisible(true);
        win.toFront();
        win.setState(JFrame.NORMAL);
    }

    public static void sleep(int sleepTimeMS) {
        try {
            Thread.sleep(sleepTimeMS);
        } catch (InterruptedException ex) {
        }
    }
}
