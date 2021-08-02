package Views;

import Pieces.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PromotionView extends Pane {
    private static Button[] buttons;
    private Label message;

    public PromotionView(int colour) throws FileNotFoundException {
        buttons = new Button[4];
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = new Button();
        }

        if(colour == 1){
            buttons[0].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/BlackQueen.png"))));
            buttons[1].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/BlackRook.png"))));
            buttons[2].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/BlackBishop.png"))));
            buttons[3].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/BlackKnight.png"))));
        }
        else{
            buttons[0].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/WhiteQueen.png"))));
            buttons[1].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/WhiteRook.png"))));
            buttons[2].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/WhiteBishop.png"))));
            buttons[3].setGraphic(new ImageView(new Image(new FileInputStream("src/Images/WhiteKnight.png"))));
        }

        for(int i = 0; i < buttons.length; i++){
            buttons[i].setPrefSize(60, 60);
            buttons[i].relocate(i * 60 + 50 + i * 20, 75);
        }

        message = new Label("Select a Piece to Promote to");
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        message.relocate(50, 35);

        setPrefSize(420, 200);
        setStyle("-fx-background-color: #CCFFCC");

        for(int i = 0; i < buttons.length; i++)
            getChildren().add(buttons[i]);
        getChildren().add(message);
    }

    public static Button[] getButtons(){return buttons;}
}
