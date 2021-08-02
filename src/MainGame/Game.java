package MainGame;

import Pieces.Piece;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> wCaptured, bCaptured;
    private GameBoard board;
    private int player;
    private Piece previousSelectedPiece;

    public Game(){
        pieces = new ArrayList<Piece>();
        wCaptured = new ArrayList<>();
        bCaptured = new ArrayList<>();
        board = new GameBoard(this);
        player = 0;
        previousSelectedPiece = null;
    }

    public void add(Piece p){
        pieces.add(p);
    }
    public void remove(int x, int y){
        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).getLoc().getX() == x && pieces.get(i).getLoc().getY() == y){
                pieces.remove(i);
            }
        }
    }
    public void setCaptured(Piece p){
        pieces.remove(p);
        if(p.getColour() == Piece.BLACK)
            bCaptured.add(p);
        else
            wCaptured.add(p);
    }

    public void removeCaptured(int loc, Piece p){
        pieces.add(loc, p);
        if(p.getColour() == Piece.BLACK)
            bCaptured.remove(p);
        else
            wCaptured.remove(p);
    }

    public ArrayList<Piece> getPieces(){return pieces;}

    public ArrayList<Piece> getbCaptured() {
        return bCaptured;
    }

    public ArrayList<Piece> getwCaptured() {
        return wCaptured;
    }

    public GameBoard getGameBoard(){return board;}
    public int getPlayer(){return player;}
    public void swapPlayers(){
        if(player == 0)
            player = 1;
        else
            player = 0;
    }

    public Piece getPreviousSelectedPiece(){return previousSelectedPiece;}
    public void setPreviousSelectedPiece(Piece previousSelectedPiece){
        this.previousSelectedPiece = previousSelectedPiece;
    }

    public void display() {
        board.display(this);
    }
}
