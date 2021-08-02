package MainGame;

import Pieces.EmptySpace;
import Pieces.Piece;
import Views.ChessView;
import Views.IllegalMoveView;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class IllegalMoveException extends Exception{
    private ChessModel model;
    private ChessView view;
    private Piece selectedPiece;

    public IllegalMoveException(ChessModel model, ChessView view, Piece selectedPiece){
        this.model = model;
        this.view = view;
        this.selectedPiece = selectedPiece;
    }

    public void handle(){
        Stage secondaryStage = new Stage();
        IllegalMoveView illegal = new IllegalMoveView();

        secondaryStage.setTitle("Illegal Move");
        secondaryStage.setResizable(false);

        secondaryStage.setScene(new Scene(illegal));
        secondaryStage.show();

        model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(selectedPiece.getLoc().getX(), selectedPiece.getLoc().getY())));
        selectedPiece.moveTo(new Point2D(selectedPiece.getPrevLoc().getX(), selectedPiece.getPrevLoc().getY()));

        model.getGame().display();
        view.update(model);

        IllegalMoveView.getOk().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                secondaryStage.close();
            }
        });

    }

}
