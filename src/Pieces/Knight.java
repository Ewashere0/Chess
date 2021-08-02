package Pieces;

import MainGame.GameBoard;
import MainGame.Point2D;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(int x, int y, int colour, Image image){
        super(new Point2D(x, y), colour, colour == 0?'H':'h'/*Yee Haw*/, image, 3);
    }

    public ArrayList<Point2D> getMoves(Piece[][] pieces) {
        ArrayList<Point2D> moves = new ArrayList<>();

        //this could be done in a loop but that
        //would probably be more complicated than this
        if(getLoc().getX() + 1 < GameBoard.WIDTH && getLoc().getY() + 2 < GameBoard.HEIGHT &&
                pieces[getLoc().getX() + 1][getLoc().getY() + 2].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() + 1, getLoc().getY() + 2));

        if(getLoc().getX() + 1 < GameBoard.WIDTH && getLoc().getY() - 2 >= 0 &&
                pieces[getLoc().getX() + 1][getLoc().getY() - 2].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() + 1, getLoc().getY() - 2));

        if(getLoc().getX() - 1 >= 0 && getLoc().getY() + 2 < GameBoard.HEIGHT &&
                pieces[getLoc().getX() - 1][getLoc().getY() + 2].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() - 1, getLoc().getY() + 2));

        if(getLoc().getX() - 1 >= 0 && getLoc().getY() - 2 >= 0 &&
                pieces[getLoc().getX() - 1][getLoc().getY() - 2].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() - 1, getLoc().getY() - 2));

        if(getLoc().getX() + 2 < GameBoard.WIDTH && getLoc().getY() + 1 < GameBoard.HEIGHT &&
                pieces[getLoc().getX() + 2][getLoc().getY() + 1].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() + 2, getLoc().getY() + 1));

        if(getLoc().getX() + 2 < GameBoard.WIDTH && getLoc().getY() - 1 >= 0 &&
                pieces[getLoc().getX() + 2][getLoc().getY() - 1].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() + 2, getLoc().getY() - 1));

        if(getLoc().getX() - 2 >= 0 && getLoc().getY() + 1 < GameBoard.HEIGHT &&
                pieces[getLoc().getX() - 2][getLoc().getY() + 1].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() - 2, getLoc().getY() + 1));

        if(getLoc().getX() - 2 >= 0 && getLoc().getY() - 1 >= 0 &&
                pieces[getLoc().getX() - 2][getLoc().getY() - 1].getColour() != getColour())
            moves.add(new Point2D(getLoc().getX() - 2, getLoc().getY() - 1));

        return moves;
    }
}
