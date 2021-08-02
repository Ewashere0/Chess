package Pieces;

import MainGame.GameBoard;
import MainGame.IllegalMoveException;
import MainGame.Point2D;
import javafx.scene.image.Image;

import java.util.ArrayList;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class King extends Piece {
    private boolean hasMoved;

    public King(int x, int y, int colour, Image image){
        super(new Point2D(x, y), colour, colour == 0?'K':'k', image, 100);
        hasMoved = false;
    }

    public ArrayList<Point2D> getMoves(Piece[][] pieces) {
        ArrayList<Point2D> moves = new ArrayList<>();
        int x, y;

        x = getLoc().getX();
        y = getLoc().getY();

        for(int i = -1; i <= 1; i++){ //regular
            for(int j = -1; j <= 1; j++){
                if (0 <= x + i && x + i < GameBoard.HEIGHT && 0 <= y + j && y + j < GameBoard.WIDTH &&
                        pieces[x + i][y + j].getColour() != getColour()) {
                    moves.add(new Point2D(x + i, y + j));
                }
            }
        }

        //castling

        return moves;
    }

    public ArrayList<Point2D> getCastle(Piece[][] pieces){
        ArrayList<Point2D> moves = new ArrayList<>();

        boolean emptySpaces;
        if(!hasMoved) {
            if(challengedSquare(getLoc(), pieces)){
                return moves; //king is currently in check
            }

            for(int i = 0; i <= 1; i++) {
                emptySpaces = true;
                if (pieces[i * (GameBoard.WIDTH - 1)][getLoc().getY()] instanceof Rook) {
                    if (!((Rook) pieces[i * (GameBoard.WIDTH - 1)][getLoc().getY()]).hasMoved()) {
                        for (int j = 1; j <= 3; j++) {
                            int squaresToCheck;
                            if(i == 0)
                                squaresToCheck = max(2, j); //2, 2, 3
                            else
                                squaresToCheck = min(2, j); //1, 2, 2
                            if (!(pieces[j + (i * getLoc().getX())][getLoc().getY()] instanceof EmptySpace ||
                                    pieces[j + (i * getLoc().getX())][getLoc().getY()] instanceof Rook) || //on third iteration of castles to the right
                                    challengedSquare(new Point2D(squaresToCheck + (i * getLoc().getX()), getLoc().getY()), pieces)){
                                emptySpaces = false;
                                break;
                            }
                        }
                        if (emptySpaces)
                            moves.add(new Point2D(i==0?2:6, getLoc().getY()));
                    }
                }
            }
        }

        return moves;
    }

    public boolean challengedSquare(Point2D aSquare, Piece[][] pieces){
        for(Piece[] p2: pieces){
            for(Piece p: p2) {
                if (p.getColour() != this.getColour()) {
                    for (Point2D location : p.getMoves(pieces)) {
                        if (location.equals(aSquare)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setHasMoved(){hasMoved = true;}
}
