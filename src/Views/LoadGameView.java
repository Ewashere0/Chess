package Views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.File;


public class LoadGameView extends Pane {
    private static Button load, cancel;
    private static Label message;
    private static ListView filenames;
    private static File[] filesList;

    public LoadGameView() {
        load = new Button("Load");
        load.setPrefSize(100, 50);
        load.relocate(75, 225);

        cancel = new Button("Cancel");
        cancel.setPrefSize(100, 50);
        cancel.relocate(225, 225);

        message = new Label("Select a file to load: ");
        message.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        message.relocate(85, 30);

        filenames = new ListView();
        filenames.setPrefSize(200, 125);
        filenames.relocate(100, 75);

        filesList = new File("src/SavedGames").listFiles();
        for(File f:filesList){
            filenames.getItems().add(f.getName());
        }

        setPrefSize(400, 300);
        setStyle("-fx-background-color: #CCFFCC");

        getChildren().addAll(load, cancel, message, filenames);
    }

    public static Button getLoad(){return load;}
    public static Button getCancel(){return cancel;}
    public static File[] getFilesList(){return filesList;}
    public static ListView getFileNames(){return filenames;}
    public static Label getMessage(){return message;}
}
