package MainGame;

import Pieces.*;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class ChessModel implements Serializable {
    private Game g;
    public ChessModel(){
        g = new Game();

        try {
            g.add(new Queen(3, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackQueen.png"))));
            g.add(new Queen(3, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteQueen.png"))));

            g.add(new Rook(0, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackRook.png"))));
            g.add(new Rook(7, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackRook.png"))));
            g.add(new Rook(0, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteRook.png"))));
            g.add(new Rook(7, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteRook.png"))));

            g.add(new Bishop(2, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackBishop.png"))));
            g.add(new Bishop(5, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackBishop.png"))));
            g.add(new Bishop(2, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteBishop.png"))));
            g.add(new Bishop(5, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteBishop.png"))));

            g.add(new Knight(1, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackKnight.png"))));
            g.add(new Knight(6, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackKnight.png"))));
            g.add(new Knight(1, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteKnight.png"))));
            g.add(new Knight(6, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteKnight.png"))));

            for (int i = 0; i < GameBoard.WIDTH; i++) {
                g.add(new Pawn(i, 1, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackPawn.png"))));
                g.add(new Pawn(i, 6, Piece.WHITE, new Image(new FileInputStream("src/Images/WhitePawn.png"))));
            }

            g.add(new King(4, 0, Piece.BLACK, new Image(new FileInputStream("src/Images/BlackKing.png"))));
            g.add(new King(4, 7, Piece.WHITE, new Image(new FileInputStream("src/Images/WhiteKing.png"))));

        }catch(FileNotFoundException ignored){}

        g.display();

    }

    public Game getGame(){
        return g;
    }
}
