package Pieces;

import MainGame.GameBoard;
import MainGame.Point2D;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(int x, int y, int colour, Image image){
        super(new Point2D(x, y), colour, colour == 0?'Q':'q', image, 9);
    }

    public ArrayList<Point2D> getMoves(Piece[][] pieces) {
        ArrayList<Point2D> moves = new ArrayList<>();
        int x, y;

        x = getLoc().getX();
        y = getLoc().getY();
        while(x + 1 < GameBoard.HEIGHT && y + 1 < GameBoard.WIDTH && pieces[x + 1][y + 1] instanceof EmptySpace){
            x++;
            y++;
            moves.add(new Point2D(x, y));
        }
        if(x + 1 < GameBoard.HEIGHT && y + 1 < GameBoard.WIDTH && pieces[x + 1][y + 1].getColour() != getColour()){
            moves.add(new Point2D(x + 1,y + 1));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(x - 1 >= 0 && y - 1 >= 0 && pieces[x - 1][y - 1] instanceof EmptySpace){
            x--;
            y--;
            moves.add(new Point2D(x, y));
        }
        if(x - 1 >= 0 && y - 1 >= 0 && pieces[x - 1][y - 1].getColour() != getColour()){
            moves.add(new Point2D(x - 1,y - 1));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(y + 1 < GameBoard.WIDTH && x - 1 >= 0 && pieces[x - 1][y + 1] instanceof EmptySpace){
            y++;
            x--;
            moves.add(new Point2D(x, y));
        }
        if(y + 1 < GameBoard.WIDTH && x - 1 >= 0 && pieces[x - 1][y + 1].getColour() != getColour()){
            moves.add(new Point2D(x - 1,y + 1));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(y - 1 >= 0 && x + 1 < GameBoard.WIDTH && pieces[x + 1][y - 1] instanceof EmptySpace){
            y--;
            x++;
            moves.add(new Point2D(x, y));
        }
        if(y - 1 >= 0 && x + 1 < GameBoard.WIDTH && pieces[x + 1][y - 1].getColour() != getColour()){
            moves.add(new Point2D(x + 1,y - 1));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(x + 1 < GameBoard.HEIGHT && pieces[x + 1][y] instanceof EmptySpace){
            x++;
            moves.add(new Point2D(x, y));
        }
        if(x + 1 < GameBoard.HEIGHT && pieces[x + 1][y].getColour() != getColour()){
            moves.add(new Point2D(x + 1,y));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(x - 1 >= 0 && pieces[x - 1][y] instanceof EmptySpace){
            x--;
            moves.add(new Point2D(x, y));
        }
        if(x - 1 >= 0 && pieces[x - 1][y].getColour() != getColour()){
            moves.add(new Point2D(x - 1,y));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(y + 1 < GameBoard.WIDTH && pieces[x][y + 1] instanceof EmptySpace){
            y++;
            moves.add(new Point2D(x, y));
        }
        if(y + 1 < GameBoard.WIDTH && pieces[x][y + 1].getColour() != getColour()){
            moves.add(new Point2D(x,y + 1));
        }

        x = getLoc().getX();
        y = getLoc().getY();
        while(y - 1 >= 0 && pieces[x][y - 1] instanceof EmptySpace){
            y--;
            moves.add(new Point2D(x, y));
        }
        if(y - 1 >= 0 && pieces[x][y - 1].getColour() != getColour()){
            moves.add(new Point2D(x,y - 1));
        }

        return moves;
    }
}
