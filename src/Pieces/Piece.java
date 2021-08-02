package Pieces;

import MainGame.Game;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import MainGame.Point2D;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Comparable, Serializable {
    private Point2D loc, prevLoc;
    private char letter;
    private transient Image image;
    private int colour;
    private int value;

    public static final int WHITE = 0, BLACK = 1;

    public Piece(Point2D loc, int colour, char letter, Image image, int value){
        this.loc = loc;
        prevLoc = loc;
        this.colour = colour;
        this.letter = letter;
        this.image = image;
        this.value = value;
    }

    public void moveTo(Point2D aSpace){
        prevLoc = loc;
        loc = aSpace;
    }

    public int compareTo(Object o){
        Piece p = (Piece)o;
        if (value < p.value)
            return 1;
        return -1;
    }

    public abstract ArrayList<Point2D> getMoves(Piece[][] pieces);
//    public char getLetter(){return letter;}
    public Image getImage() {return image;}
    public Point2D getLoc(){return loc;}
    public Point2D getPrevLoc(){return prevLoc;}
    public int getColour(){return colour;}
    public void setCaptured(Game g){ g.setCaptured(this);}
    public void removeCaptured(int loc, Game g){g.removeCaptured(loc, this);}

    //make image serializable
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if(!(this instanceof EmptySpace))
            image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        if(!(this instanceof EmptySpace))
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", s);
    }
}
