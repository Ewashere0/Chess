package MainGame;

import Pieces.EmptySpace;
import Pieces.Piece;
import java.io.Serializable;

public class GameBoard implements Serializable {

    private Piece[][] board;
    public static final int WIDTH = 8, HEIGHT = 8;

    public GameBoard(Game g){
        board = new Piece[WIDTH][HEIGHT];

        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                board[i][j] = new EmptySpace(new Point2D(i, j));
            }
        }

        for(Piece p: g.getPieces()){
            board[p.getLoc().getX()][p.getLoc().getY()] = p;
        }
    }

    public Piece[][] getBoard(){return board;}
    public void setSpace(Piece aPiece){ board[aPiece.getLoc().getX()][aPiece.getLoc().getY()] = aPiece; }
    public void promote(Piece aPiece, Game g){
        board[aPiece.getLoc().getX()][aPiece.getLoc().getY()] = aPiece;
        g.remove(aPiece.getLoc().getX(), aPiece.getLoc().getY());
        g.add(aPiece);
    }

    public void display(Game g) {

        for(Piece p: g.getPieces()){
//            board[p.getPrevLoc().getX()][p.getPrevLoc().getY()] =
//                    new EmptySpace(new Point2D(p.getPrevLoc().getX(), p.getPrevLoc().getY()));
            board[p.getLoc().getX()][p.getLoc().getY()] = p;
        }

        //this can be used to show the game in the console, but
        //it will show extra boards while checkmates are being checked

//        for(int i = 0; i < HEIGHT; i++){
//            for(int j = 0; j < WIDTH; j++){
//                System.out.print(board[j][i].getLetter());
//            }
//            System.out.println();
//        }
    }
}
