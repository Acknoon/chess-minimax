/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Ariel
 */
public class Utils {
    
    public static ImageIcon WPawn,WKnight,WBishop,WRook,WQueen,WKing,BPawn,BBishop,BKnight,BRook,BQueen,BKing;
    
    public static void loadAssets(){
        int sizeButton = (int)(View.BUTTON_SIZE * .90);
        
        //The White Pieces Assets
        WPawn = new ImageIcon(Controller.class.getResource(View.WHITE_PAWN));
        WPawn = new ImageIcon(WPawn.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WBishop = new ImageIcon(Controller.class.getResource(View.WHITE_BISHOP));
        WBishop = new ImageIcon(WBishop.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WKnight = new ImageIcon(Controller.class.getResource(View.WHITE_KNIGHT));
        WKnight = new ImageIcon(WKnight.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WRook = new ImageIcon(Controller.class.getResource(View.WHITE_ROOK));
        WRook = new ImageIcon(WRook.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WQueen = new ImageIcon(Controller.class.getResource(View.WHITE_QUEEN));
        WQueen = new ImageIcon(WQueen.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        WKing = new ImageIcon(Controller.class.getResource(View.WHITE_KING));
        WKing = new ImageIcon(WKing.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        
        //The Black pieces assets
        BPawn = new ImageIcon(Controller.class.getResource(View.BLACK_PAWN));
        BPawn = new ImageIcon(BPawn.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BBishop = new ImageIcon(Controller.class.getResource(View.BLACK_BISHOP));
        BBishop = new ImageIcon(BBishop.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BKnight = new ImageIcon(Controller.class.getResource(View.BLACK_KNIGHT));
        BKnight = new ImageIcon(BKnight.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BRook = new ImageIcon(Controller.class.getResource(View.BLACK_ROOK));
        BRook = new ImageIcon(BRook.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BQueen = new ImageIcon(Controller.class.getResource(View.BLACK_QUEEN));
        BQueen = new ImageIcon(BQueen.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        BKing = new ImageIcon(Controller.class.getResource(View.BLACK_KING));
        BKing = new ImageIcon(BKing.getImage().getScaledInstance(sizeButton, sizeButton, Image.SCALE_SMOOTH));
        
        
    }
    
}
