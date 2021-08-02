package Pieces;

import MainGame.Point2D;
import Pieces.Piece;

import java.util.ArrayList;

public class EmptySpace extends Piece {

    public EmptySpace(Point2D loc){
        super(loc, 2, '-', null, 0);
    }

    @Override
    public void moveTo(Point2D aSpace) {}

    @Override
    public ArrayList<Point2D> getMoves(Piece[][] pieces) {
        return new ArrayList<>();
    }
}
