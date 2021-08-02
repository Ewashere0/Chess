package Views;

import MainGame.ChessModel;
import MainGame.GameBoard;
import Pieces.Piece;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Collections;

public class ChessView extends Pane {
    private GridPane board;
    private ImageView[] capturedImages;
    private Button[][] buttons;
    private Button saveGame, loadGame, newGame;
    private static final int DIMENSION = 600;

    public ChessView(){
        board = new GridPane();
        buttons = new Button[GameBoard.WIDTH][GameBoard.HEIGHT];
        for(int i = 0; i < GameBoard.WIDTH; i++){
            for(int j = 0; j < GameBoard.HEIGHT; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].relocate(i * DIMENSION / GameBoard.WIDTH, j * DIMENSION / GameBoard.HEIGHT);
                buttons[i][j].setPrefSize(DIMENSION / GameBoard.WIDTH, DIMENSION / GameBoard.HEIGHT);
                removeHighlight(buttons[i][j]);
                board.add(buttons[i][j], i, j);
            }
        }

        capturedImages = new ImageView[32];

        Text wCaptured = new Text("White Captured");
        Text bCaptured = new Text("Black Captured");
        saveGame = new Button("Save Game");
        loadGame = new Button("Load Game");
        newGame = new Button("New Game");

        wCaptured.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        bCaptured.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        wCaptured.relocate(DIMENSION + 140, 50);
        bCaptured.relocate(DIMENSION + 140, 250);
        saveGame.relocate(DIMENSION + 50, 500);
        loadGame.relocate(DIMENSION + 175, 500);
        newGame.relocate(DIMENSION + 300, 500);

        saveGame.setPrefSize(100, 50);
        loadGame.setPrefSize(100, 50);
        newGame.setPrefSize(100, 50);

        getChildren().add(board);
        getChildren().add(wCaptured);
        getChildren().add(bCaptured);
        getChildren().add(saveGame);
        getChildren().add(loadGame);
        getChildren().add(newGame);

        setPrefSize(DIMENSION + 450, DIMENSION);
        setStyle("-fx-background-color: #54C571;");
    }

    public Button[][] getButtons() {
        return buttons;
    }
    public Button getNewGameButton(){return newGame;}
    public Button getSaveGameButton(){return saveGame;}
    public Button getLoadGameButton(){return loadGame;}

    public void setColour(Button aButton, String green, String white){
        int x = (int)aButton.getLayoutX();
        int y = (int)aButton.getLayoutY();
        if(x % 2 == 0){
            if((x * GameBoard.HEIGHT + y) % 2 != 0)
                aButton.setStyle("-fx-background-color:" + green);
            else
                aButton.setStyle("-fx-background-color:" + white);
        }
        else{
            if((x * GameBoard.HEIGHT + y) % 2 == 0)
                aButton.setStyle("-fx-background-color:" + green);
            else
                aButton.setStyle("-fx-background-color:" + white);
        }
    }

    public void highlight(Button aButton){
        setColour(aButton, "#CCFFCC;", "#FCFCFC;" );
    }
    public void removeHighlight(Button aButton) {
        setColour(aButton, "#99FF99;", "#EFEFEF;" );
    }
    public void click(Button aButton){
        setColour(aButton, "#CCCCFF;","#CCCCFF;" );
    }
    public void showMove(Button aButton){ setColour(aButton, "#CCE5FF;", "#CCE5FF;");}

    public void update(ChessModel model){
        ImageView image;
        Piece aPiece;
        for(int i = 0; i < GameBoard.WIDTH; i++) {
            for(int j = 0; j < GameBoard.HEIGHT; j++){
                aPiece = model.getGame().getGameBoard().getBoard()[i][j];
                image = new ImageView(aPiece.getImage());
                image.setFitHeight(DIMENSION / GameBoard.HEIGHT - 20);
                image.setPreserveRatio(true);
                buttons[aPiece.getLoc().getX()][aPiece.getLoc().getY()].setGraphic(image);
            }
        }

        Collections.sort(model.getGame().getwCaptured());
        Collections.sort(model.getGame().getbCaptured());
        if(model.getGame().getwCaptured().size() == 0 && model.getGame().getbCaptured().size() == 0){
            for(int i = 0; i < capturedImages.length; i++){
                getChildren().remove(capturedImages[i]);
            }
            capturedImages = new ImageView[32];
        }

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < GameBoard.WIDTH; j++) {
                try{
                    aPiece = model.getGame().getwCaptured().get(i * GameBoard.WIDTH + j);
                    image = new ImageView(aPiece.getImage());
                    image.setFitHeight(50);
                    image.setPreserveRatio(true);
                    image.relocate(625 + j * 50, 100 + i * 50);
                    getChildren().remove(capturedImages[i * GameBoard.WIDTH + j]);
                    capturedImages[i * GameBoard.WIDTH + j] = image;
                    getChildren().add(image);
                }catch(IndexOutOfBoundsException e){
                    break;
                }
            }
        }
        for(int i = 2; i < 4; i++){
            for(int j = 0; j < GameBoard.WIDTH; j++) {
                try{
                    aPiece = model.getGame().getbCaptured().get((i - 2) * GameBoard.WIDTH + j);
                    image = new ImageView(aPiece.getImage());
                    image.setFitHeight(50);
                    image.setPreserveRatio(true);
                    image.relocate(625 + j * 50, 200 + i * 50);
                    getChildren().remove(capturedImages[i * GameBoard.WIDTH + j]);
                    capturedImages[i * GameBoard.WIDTH + j] = image;
                    getChildren().add(image);
                }catch(IndexOutOfBoundsException e){
                    break;
                }
            }
        }
    }

    public void disable(){
        for(int i = 0; i < GameBoard.HEIGHT; i++){
            for(int j = 0; j < GameBoard.WIDTH; j++){
                buttons[i][j].setDisable(true);
                buttons[i][j].setOpacity(1.0);
            }
        }
        newGame.setDisable(true);
        loadGame.setDisable(true);
        saveGame.setDisable(true);
    }

    public void enable(){
        for(int i = 0; i < GameBoard.HEIGHT; i++){
            for(int j = 0; j < GameBoard.WIDTH; j++){
                buttons[i][j].setDisable(false);
            }
        }
        newGame.setDisable(false);
        loadGame.setDisable(false);
        saveGame.setDisable(false);
    }
}
