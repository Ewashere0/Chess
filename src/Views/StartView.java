package Views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class StartView extends Pane {
    private static Button newGame, loadGame;
    private Label message;

    public StartView() {
        newGame = new Button("New Game");
        newGame.setPrefSize(100, 50);
        newGame.relocate(75, 125);

        loadGame = new Button("Load Game");
        loadGame.setPrefSize(100, 50);
        loadGame.relocate(225, 125);

        message = new Label("Welcome to Chess");
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
        message.relocate(72, 50);

        setPrefSize(400, 200);
        setStyle("-fx-background-color: #CCFFCC");

        getChildren().addAll(newGame, loadGame, message);
    }

    public static Button getLoadGameButton() {
        return loadGame;
    }

    public static Button getNewGameButton() {
        return newGame;
    }
}
