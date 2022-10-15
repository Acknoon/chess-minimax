/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ariel
 */
public class Evaluation {

    public static final int MAX_PIECES = 16;
    public static final Square[] CENTER = new Square[]{
        new Square(3, 3),
        new Square(3, 4),
        new Square(4, 3),
        new Square(4, 4)
    };
    private final Model theGame;
    private final ArrayList<Piece> thePieces;

    private enum gamePhase {
        OPENING, MIDGAME, ENDGAME
    };
    private final int mainScore;

    public Evaluation(Model model, int player) {
        this.theGame = model;
        this.thePieces = model.getListPieces(player);
        this.mainScore = calculateScore();
    }

    /**
     * The main function that calculates all the principles together
     *
     * @return the main score of the board
     */
    private int calculateScore() {
        int score = calcMaterial();

        switch (scanCurrentPoision()) {
            case OPENING:
                score += developmentScore() + centerControlScore();
                break;
            case MIDGAME:

                break;
            case ENDGAME:

        }
        return score;
    }

    /**
     * Function calculates the value of all pieces that player has
     *
     * @param player the player to calculate the score
     * @return value of the score
     */
    private int calcMaterial() {
        int sum = 0;
        for (int i = 1; i < thePieces.size(); i++) {
            sum += thePieces.get(i).getValuePiece();
            if (isCenter(thePieces.get(i).getSquare())) {
                sum += 2;
            }
        }
        return sum;
    }

    /**
     * Function that identify which phase of the game is the current position
     * it's in
     *
     * @return what phase it is in
     */
    private gamePhase scanCurrentPoision() {
        int counter = 0, i = 0;
        if (thePieces.size() >= MAX_PIECES - 3) {
            for (i = 0; i < thePieces.size(); i++) {
                if (thePieces.get(i).getMoveCounter() == 0) {
                    counter++;
                }
            }
            if (counter > 10) {
                return gamePhase.OPENING;
            }
        }
        for (i = 1, counter = 0; !(thePieces.get(i) instanceof Pawn) && i < thePieces.size(); i++) {
            counter++;
        }
        if (counter < 4) {
            if (thePieces.get(1) instanceof Queen) {
                if (thePieces.get(2) instanceof Rook || thePieces.get(2) instanceof Queen) 
                    return gamePhase.MIDGAME;
            } else {
                return gamePhase.ENDGAME;
            }
        }
        return gamePhase.MIDGAME;
    }

    /**
     * Function that calculates a score in openings how developed a player's
     * pieces are
     *
     * @return score
     */
    private int developmentScore() {
        int sum = 0;
        for (int i = 0; i < thePieces.size(); i++) {
            if (thePieces.get(i).getMoveCounter() == 1) {
                sum++;
            }
            if (thePieces.get(i).getMoveCounter() > 1) {
                sum = sum - thePieces.get(i).getMoveCounter();
            }
        }
        return sum;
    }

    /**
     * Function that calculates the amount of center control a player has
     *
     * @return score
     */
    private int centerControlScore() {
        boolean control = false;
        float sum = 0;
        for (int i = 1; i < thePieces.size(); i++) {
            for (Square center : CENTER) {
                if (thePieces.get(i).isControllingSquare(center)) {
                    control = true;
                }
            }
            if (control) {
                ArrayList<Move> list = thePieces.get(i).getControllingList(theGame);
                for (int j = 0; j < list.size(); j++) {
                    if (isCenter(list.get(j).getDestinationSquare())) {
                        sum += 1;
                    } else {
                        sum += 0.5f;
                    }
                }
            }
        }
        return (int) sum;
    }

    /**
     * Boolean function that checks if a certain square is inside the center
     * Quadrom, center Quadrom is a 4x4 section of the board that is in the
     * center, usually that Quadrom the pieces are the most active and flexible
     * on them
     *
     * @param s the given square
     * @return true if inside and false if outside
     */
    private boolean isInsideCenterQuadrom(Square s) {
        return s.getCol() >= 2
                && s.getCol() <= 5
                && s.getRow() >= 2
                && s.getRow() <= 5;
    }
    
    private int promotionScore(){
        int sum = 0;
        for (int i = 1; i < thePieces.size(); i++) {
            if (thePieces.get(i) instanceof Pawn) {
                if (isPassPawn((Pawn) thePieces.get(i))) {
                    switch(thePieces.get(i).theType){
                        case Piece.PLAYER_WHITE:
                            thePieces.get(i).getRow();
                            break;
                        case Piece.PLAYER_BLACK:
                            
                    }
                }
            }
        }
        return sum;
    }
    

    /**
     * Function that checks if a pawn that it given is a pass
     * meaning the pawn that has no enemy pawns in front are considered to be pass pawns
     * since they won't be captured by them.
     * @param p the given pawn
     * @return true if it's a pass pawn and false if not
     */
    private boolean isPassPawn(Pawn p) {
        int type = p.getType();
        ArrayList<Piece> enemy = theGame.getListPieces(theGame.getOppositeTurn(type));

        switch (type) {
            case Piece.PLAYER_WHITE:
                for (int i = 0; i < enemy.size(); i++) {
                    if (enemy.get(i) instanceof Pawn) {
                        if (p.getCol() != 7 && p.getCol() != 0) {
                            if (!(enemy.get(i).getSquare().getCol() >= p.getCol() - 1 &&
                                    enemy.get(i).getSquare().getCol() <= p.getCol() + 1 &&
                                    enemy.get(i).getSquare().getRow() < p.getRow())) 
                            {
                                return true;
                            }
                        }
                        if (p.getCol() == 7) {
                            if (!(enemy.get(i).getSquare().getCol() >= p.getCol() - 1 &&
                                    enemy.get(i).getSquare().getRow() < p.getRow())) 
                            {
                                return true;
                            }
                        }
                        if (p.getCol() == 0) {
                            if (!(enemy.get(i).getSquare().getCol() <= p.getCol() + 1 &&
                                    enemy.get(i).getSquare().getRow() < p.getRow())) 
                            {
                                return true;
                            }
                        }
                    }
                }
                break;
            case Piece.PLAYER_BLACK:
                for (int i = 0; i < enemy.size(); i++) {
                    if (enemy.get(i) instanceof Pawn) {
                        if (p.getCol() != 7 && p.getCol() != 0) {
                            if (!(enemy.get(i).getSquare().getCol() >= p.getCol() - 1 &&
                                    enemy.get(i).getSquare().getCol() <= p.getCol() + 1 &&
                                    enemy.get(i).getSquare().getRow() > p.getRow())) 
                            {
                                return true;
                            }
                        }
                        if (p.getCol() == 7) {
                            if (!(enemy.get(i).getSquare().getCol() >= p.getCol() - 1 &&
                                    enemy.get(i).getSquare().getRow() > p.getRow())) 
                            {
                                return true;
                            }
                        }
                        if (p.getCol() == 0) {
                            if (!(enemy.get(i).getSquare().getCol() <= p.getCol() + 1 &&
                                    enemy.get(i).getSquare().getRow() > p.getRow())) 
                            {
                                return true;
                            }
                        }
                    }
                }
        }
        return false;
    }

    /**
     * Function that checks if a certain square is inside center
     *
     * @param s given square
     * @return true if it's inside and false if not
     */
    private boolean isCenter(Square s) {
        return s.getCol() >= 3
                && s.getCol() <= 4
                && s.getRow() >= 3
                && s.getRow() <= 4;
    }

    /**
     * Function that returns the score of the position, the only function that
     * any user can use outside of the constructor
     *
     * @return The score of the player chosen
     */
    public int getMainScore() {
        return mainScore;
    }
}
