package Views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class GameOverView extends Pane {
    private static Button newGame, closeGame;
    private static Label message;

    public GameOverView(String result){
        newGame = new Button("New Game");
        newGame.setPrefSize(100, 50);
        newGame.relocate(75, 125);

        closeGame = new Button("Close Game");
        closeGame.setPrefSize(100, 50);
        closeGame.relocate(225, 125);

        message = new Label(result);
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        message.relocate(65, 50);

        setPrefSize(400, 200);
        setStyle("-fx-background-color: #CCFFCC");

        getChildren().addAll(newGame, closeGame, message);
    }

    public static Button getNewGame(){return newGame;}
    public static Button getCloseGame(){return closeGame;}
}
