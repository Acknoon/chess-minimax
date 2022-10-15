/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ver4;

import java.util.ArrayList;

/**
 *
 * @author Ariel
 */
class Move {

    private Piece piece;
    private Piece capturedPiece;
    private Square destinationSquare;
    private ArrayList<Move> allPossibleMoves;
    private int score;
    private int depth;

    public Move(Piece piece, Square destinationSquare) {
        this.piece = piece;
        this.destinationSquare = destinationSquare;
        this.capturedPiece = null;
    }
    
    public Move(Piece piece, Square destinationSquare, Piece capturedPiece) {
        this.piece = piece;
        this.destinationSquare = destinationSquare;
        this.capturedPiece = capturedPiece;
    }

    public Move(Piece piece, Square destinationSquare, int score) {
        this.piece = piece;
        this.destinationSquare = destinationSquare;
        this.capturedPiece = null;
        this.score = score;
    }

    public Move(Piece piece, Square destinationSquare, Piece capturedPiece, int score) {
        this.piece = piece;
        this.destinationSquare = destinationSquare;
        this.capturedPiece = capturedPiece;
        this.score = score;
    }

     public Move(int score) {
        this.piece = null;
        this.destinationSquare = null;
        this.capturedPiece = null;
        this.score = score;
    }

    public Move(int score, int depth) {
        this.score = score;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    
    
    public Piece getPiece() {
        return piece;
    }

    public Square getDestinationSquare() {
        return destinationSquare;
    }
    
    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setDestinationSquare(Square destinationSquare) {
        this.destinationSquare = destinationSquare;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public ArrayList<Move> getPossibleMoves() {
        return allPossibleMoves;
    }

    public void setPossibleMoves(ArrayList<Move> allPossibleMoves) {
        this.allPossibleMoves = allPossibleMoves;
    }
    
    @Override
    public String toString(){
        return this.piece.getStringPiece()+"("+ this.piece.getStringCoordinates() + ")"
                + "--->  " + this.destinationSquare.toString() + "      The Score ==>"+ this.score  +"\n";
    }
}
