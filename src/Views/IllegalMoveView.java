package Views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class IllegalMoveView extends Pane {
    private static Button ok;
    private Label message, message2;

    public IllegalMoveView(){
        ok = new Button("Okay");
        ok.setPrefSize(100, 50);
        ok.relocate(150, 125);

        message = new Label("You have attempted ");
        message2 = new Label("to put yourself in check");
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        message2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        message.relocate(80, 50);
        message2.relocate(65, 70);


        setPrefSize(400, 200);
        setStyle("-fx-background-color: #CCFFCC");

        getChildren().addAll(ok, message, message2);
    }

    public static Button getOk(){return ok;}
}
