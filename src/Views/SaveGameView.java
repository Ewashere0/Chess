package Views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class SaveGameView extends Pane {
    private static Button save, cancel;
    private static TextField filename;
    private Label message, dotTXT;

    public SaveGameView() {
        save = new Button("Save");
        save.setPrefSize(100, 50);
        save.relocate(75, 125);

        cancel = new Button("Cancel");
        cancel.setPrefSize(100, 50);
        cancel.relocate(225, 125);

        filename = new TextField();
        filename.setPrefSize(255, 25);
        filename.relocate(50, 75);

        dotTXT = new Label(".txt");
        dotTXT.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        dotTXT.relocate(315, 78);

        message = new Label("Enter a file name to save to: ");
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        message.relocate(40, 30);

        setPrefSize(400, 200);
        setStyle("-fx-background-color: #CCFFCC");

        getChildren().addAll(save, cancel, filename, dotTXT, message);
    }

    public static Button getSave(){return save;}
    public static Button getCancel(){return cancel;}
    public static String getFilename(){return filename.getText();}
}
