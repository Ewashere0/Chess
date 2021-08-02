package Pieces;

import MainGame.GameBoard;
import MainGame.Point2D;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean hasMoved;
    private boolean hasMovedTwice;

    public Pawn(int x, int y, int colour, Image image){
        super(new Point2D(x, y), colour, colour == 0?'P':'p', image, 1);
        hasMoved = false;
    }

    public void moveTo(Point2D aSpace){
        if(hasMoved){
            hasMovedTwice = true;
        }
        super.moveTo(aSpace);
        if((getColour() == Piece.BLACK && getLoc().getY() == 1) ||
            getColour() == Piece.WHITE && getLoc().getY() == 6)
            hasMoved = false;
        else
            hasMoved = true;
    }

    public ArrayList<Point2D> getMoves(Piece[][] pieces) {
        ArrayList<Point2D> moves = new ArrayList<>();
        int direction;
        if (getColour() == Piece.BLACK)
            direction = 1;
        else
            direction = -1;

        //space in front
        if(getLoc().getY() + direction >= 0 && getLoc().getY() + direction < GameBoard.HEIGHT) {
            if (pieces[getLoc().getX()][getLoc().getY() + direction] instanceof EmptySpace) {
                moves.add(new Point2D(getLoc().getX(), getLoc().getY() + direction));
            }
        }

        if(getLoc().getY() + direction >= 0 && getLoc().getY() + direction < GameBoard.HEIGHT) {
            if (pieces[getLoc().getX()][getLoc().getY() + direction] instanceof EmptySpace && !hasMoved) {
                if (pieces[getLoc().getX()][getLoc().getY() + 2 * direction] instanceof EmptySpace) {
                    moves.add(new Point2D(getLoc().getX(), getLoc().getY() + 2 * direction));
                }
            }
        }

        //diagonal to the right
        if(getLoc().getY() + direction >= 0 && getLoc().getY() + direction < GameBoard.HEIGHT && getLoc().getX() + 1 < GameBoard.WIDTH) {
            Piece temp = pieces[getLoc().getX() + 1][getLoc().getY() + direction];
            if (!(temp instanceof EmptySpace) && temp.getColour() != getColour())
                moves.add(new Point2D(getLoc().getX() + 1, getLoc().getY() + direction));
        }

        if(getLoc().getY() + direction >= 0 && getLoc().getY() + direction < GameBoard.HEIGHT && getLoc().getX() - 1 >= 0) {
            Piece temp = pieces[getLoc().getX() - 1][getLoc().getY() + direction];
            if (!(temp instanceof EmptySpace) && temp.getColour() != getColour())
                moves.add(new Point2D(getLoc().getX() - 1, getLoc().getY() + direction));
        }

        return moves;
    }

    public ArrayList<Point2D> getEnPassant(Piece[][] pieces, Piece previousMove){
        ArrayList<Point2D> moves = new ArrayList<>();
        int direction;
        if (getColour() == Piece.BLACK)
            direction = 1;
        else
            direction = -1;

        if(getLoc().getY() + direction >= 0 && getLoc().getY() + direction < GameBoard.HEIGHT && getLoc().getX() + 1 < GameBoard.WIDTH) {
            Piece temp = pieces[getLoc().getX() + 1][getLoc().getY()]; //en passant right
            if (temp instanceof Pawn && temp.getColour() != getColour() && temp.equals(previousMove) &&((Pawn) temp).hasMovedTwice == false) {
                moves.add(new Point2D(getLoc().getX() + 1, getLoc().getY() + direction));
            }
        }

        if(getLoc().getY() + direction >= 0 && getLoc().getY() + direction < GameBoard.HEIGHT && getLoc().getX() - 1 >= 0) {
            Piece temp = pieces[getLoc().getX() - 1][getLoc().getY()]; //en passant right
            if (temp instanceof Pawn && temp.getColour() != getColour() && temp.equals(previousMove) && ((Pawn) temp).hasMovedTwice == false) {
                moves.add(new Point2D(getLoc().getX() - 1, getLoc().getY() + direction));
            }
        }

        return moves;
    }
}
