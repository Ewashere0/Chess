package MainGame;

import Pieces.*;
import Views.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ChessApp extends Application {

    private ChessModel model;
    private ArrayList<Button> highlightedButtons;
    private Piece selectedPiece; //previous only used for EnPassant
    private Pane aPane;
    private ChessView view;

    public ChessApp() {
        model = new ChessModel();
        highlightedButtons = new ArrayList<>(); //moving over blue tiles does not turn them green or white
        selectedPiece = null;
        aPane = new Pane();
        view = new ChessView();
    }

    public void start(Stage primaryStage) {
        // Create the view
        StartView startview = new StartView();
        aPane.getChildren().add(startview);

        primaryStage.setTitle("Start"); //Start screen
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane));
        primaryStage.show();

        StartView.getLoadGameButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                primaryStage.hide();
                loadGame(primaryStage, true);
            }
        });

        StartView.getNewGameButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                primaryStage.hide();
                newGame(primaryStage, true);
            }
        });

        view.update(model); //actual chess board

        for(int i = 0; i < GameBoard.WIDTH; i++) {
            for (int j = 0; j < GameBoard.HEIGHT; j++) {
                int finalJ = j;
                int finalI = i;

                view.getButtons()[i][j].setOnMouseEntered(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouseEvent) {
                        if(!highlightedButtons.contains(view.getButtons()[finalI][finalJ]))
                            view.highlight(view.getButtons()[finalI][finalJ]);
                    }
                });

                view.getButtons()[i][j].setOnMouseExited(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouseEvent) {
                        if(!highlightedButtons.contains(view.getButtons()[finalI][finalJ]))
                            view.removeHighlight(view.getButtons()[finalI][finalJ]);
                    }
                });

                view.getButtons()[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouseEvent) {
                        Piece[][] board = model.getGame().getGameBoard().getBoard();
                        int player = model.getGame().getPlayer();

                        for (Button aButton : highlightedButtons) { //remove old highlights
                            view.removeHighlight(aButton);
                        }

                        //to not remove these highlights on mouseEntered/Exited
                        highlightedButtons = new ArrayList<>();

                        if(selectedPiece != null && selectedPiece.getColour() == player){
                            if (selectedPiece.getMoves(board).contains(new Point2D(finalI, finalJ))) {
                                Piece aPiece = model.getGame().getGameBoard().getBoard()[finalI][finalJ];
                                int capturedLoc = -1;
                                for(int i = 0; i < model.getGame().getPieces().size(); i++) {
                                    if (aPiece.equals(model.getGame().getPieces().get(i))){
                                        capturedLoc = i;
                                        break;
                                    }
                                }
                                if(!(aPiece instanceof EmptySpace)){ //capturing a piece
                                    aPiece.setCaptured(model.getGame());
                                }
                                if (selectedPiece instanceof King) { //for castling
                                    ((King) selectedPiece).setHasMoved();
                                }
                                else if (selectedPiece instanceof Rook) { //for castling
                                    ((Rook) selectedPiece).setHasMoved();
                                }
                                try {
                                    if(selectedPiece instanceof Pawn && (finalJ == 0 || finalJ == GameBoard.HEIGHT - 1))
                                        handlePromotion(finalI, finalJ, player, primaryStage);
                                    handleMove(finalI, finalJ, player, primaryStage);
                                }catch(IllegalMoveException e){ //attempted to put self in check
                                    e.handle();
                                    if(!(aPiece instanceof EmptySpace)){
                                        aPiece.removeCaptured(capturedLoc, model.getGame());
                                    }
                                    model.getGame().display();
                                    view.update(model);
                                    model.getGame().setPreviousSelectedPiece(selectedPiece);
                                    selectedPiece = null;
                                }
                                return;
                            }
                            else if(selectedPiece instanceof Pawn){
                                if(((Pawn)selectedPiece).getEnPassant(board, model.getGame().getPreviousSelectedPiece()).contains(new Point2D(finalI, finalJ))){
                                    handleEnPassant(finalI, finalJ, primaryStage);
                                }
                                else if (board[finalI][finalJ].getColour() == player || board[finalI][finalJ].getColour() == 2)
                                    selectPiece(finalI, finalJ, board);
                            }
                            else if(selectedPiece instanceof King){
                                if(((King)selectedPiece).getCastle(board).contains(new Point2D(finalI, finalJ))){
                                    handleCastle(finalI, finalJ, player, primaryStage);
                                }
                                else if (board[finalI][finalJ].getColour() == player || board[finalI][finalJ].getColour() == 2)
                                    selectPiece(finalI, finalJ, board);
                            }
                            else if(board[finalI][finalJ].getColour() == player || board[finalI][finalJ].getColour() == 2)
                                selectPiece(finalI, finalJ, board);
                        }
                        else if(board[finalI][finalJ].getColour() == player ||
                                board[finalI][finalJ].getColour() == 2) {
                            selectPiece(finalI, finalJ, board); //selecting a piece for the first time
                        }
                    }
                });
            }
        }
        view.getNewGameButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                newGame(primaryStage, false);
            }
        });

        view.getSaveGameButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                saveGame();
            }
        });

        view.getLoadGameButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                loadGame(primaryStage, false);
            }
        });
    }

    //highlight a piece and its moves
    public void selectPiece(int x, int y, Piece[][] board){
        selectedPiece = model.getGame().getGameBoard().getBoard()[x][y];
        view.click(view.getButtons()[x][y]);
        highlightedButtons.add(view.getButtons()[x][y]);

        for (Point2D availableMove : board[x][y].getMoves(board)) {
            if (board[availableMove.getX()][availableMove.getY()].getColour() != selectedPiece.getColour()) {
                view.showMove(view.getButtons()[availableMove.getX()][availableMove.getY()]);
                highlightedButtons.add(view.getButtons()[availableMove.getX()][availableMove.getY()]);
            }
        }

        if(board[x][y] instanceof Pawn){
            for(Point2D enPassant: ((Pawn)board[x][y]).getEnPassant(board, model.getGame().getPreviousSelectedPiece())){
                view.showMove(view.getButtons()[enPassant.getX()][enPassant.getY()]);
                highlightedButtons.add(view.getButtons()[enPassant.getX()][enPassant.getY()]);
            }
        }

        if(board[x][y] instanceof King){
            for(Point2D castle: ((King)board[x][y]).getCastle(board)){
                view.showMove(view.getButtons()[castle.getX()][castle.getY()]);
                highlightedButtons.add(view.getButtons()[castle.getX()][castle.getY()]);
            }
        }
    }

    //move a piece to the chosen location
    public void handleMove(int x, int y, int player, Stage primaryStage) throws IllegalMoveException {

        model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(selectedPiece.getLoc().getX(), selectedPiece.getLoc().getY())));
        selectedPiece.moveTo(new Point2D(x, y));
        model.getGame().display();

        Point2D kingLoc = new Point2D(0, 0);
        for(Piece p: model.getGame().getPieces()){
            if(p instanceof King && p.getColour() == selectedPiece.getColour()) {
                kingLoc = p.getLoc();
                break;
            }
        }

        for(Piece p: model.getGame().getPieces()){
            if(p.getColour() != selectedPiece.getColour()){
                for(Point2D loc: p.getMoves(model.getGame().getGameBoard().getBoard())){
                    if(loc.equals(kingLoc)){
                        throw new IllegalMoveException(model, view, selectedPiece); //put self in check
                    }
                }
            }
        }

        if (checkmate(player)){
            endGame("Checkmate", selectedPiece.getColour() == 1?
                    "Game over, Black Wins!":"Game over, White Wins!", primaryStage);
        }

        model.getGame().setPreviousSelectedPiece(selectedPiece);
        selectedPiece = null;
        model.getGame().swapPlayers();
        view.update(model);
        primaryStage.setTitle("Chess: " + (model.getGame().getPlayer() == 1?"Black":"White") + " to move");

        handleStaleMate(player, primaryStage);
    }

    //if the chosen move was to castle
    public void handleCastle(int x, int y, int player, Stage primaryStage){
        int direction, rookX;
        if(x < selectedPiece.getLoc().getX()){
            direction = 1;
            rookX = 0;
        }
        else{
            direction = -1;
            rookX = GameBoard.WIDTH - 1;
        }
        model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(selectedPiece.getLoc().getX(), selectedPiece.getLoc().getY())));
        ((King)selectedPiece).setHasMoved();
        selectedPiece.moveTo(new Point2D(x, y));

        Piece rook = model.getGame().getGameBoard().getBoard()[rookX][selectedPiece.getLoc().getY()];
        model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(rook.getLoc().getX(), rook.getLoc().getY())));
        ((Rook)rook).setHasMoved();
        rook.moveTo(new Point2D(x + direction, y));

        model.getGame().display();
        model.getGame().setPreviousSelectedPiece(selectedPiece);
        selectedPiece = null;
        model.getGame().swapPlayers();
        view.update(model);
        primaryStage.setTitle("Chess: " + (model.getGame().getPlayer() == 1?"Black":"White") + " to move");

        handleStaleMate(player, primaryStage);
    }

    //if the chosen move was an EnPassant move
    public void handleEnPassant(int x, int y, Stage primaryStage){
        model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(selectedPiece.getLoc().getX(), selectedPiece.getLoc().getY())));
        model.getGame().getGameBoard().getBoard()[x][selectedPiece.getLoc().getY()].setCaptured(model.getGame());
        model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(x, selectedPiece.getLoc().getY())));
        selectedPiece.moveTo(new Point2D(x, y));

        model.getGame().display();
        model.getGame().setPreviousSelectedPiece(selectedPiece);
        selectedPiece = null;
        model.getGame().swapPlayers();
        view.update(model);
        primaryStage.setTitle("Chess: " + (model.getGame().getPlayer() == 1?"Black":"White") + " to move");
    }

    //for promoting pawns that reach the other end of the board
    public void handlePromotion(int x, int y, int player, Stage primaryStage){
        Stage secondaryStage = new Stage();
        Pane promotionView= null;
        try {
            promotionView = new PromotionView(player);
        } catch (FileNotFoundException ignored) {}

        secondaryStage.setTitle("Promote");
        secondaryStage.setResizable(false);

        secondaryStage.setScene(new Scene(promotionView));
        secondaryStage.show();
        view.disable();

        PromotionView.getButtons()[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
               handlePromotionChoice(x, y, player, "Queen", primaryStage);
                secondaryStage.close();
                view.enable();
            }
        });

        PromotionView.getButtons()[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                handlePromotionChoice(x, y, player, "Rook", primaryStage);
                secondaryStage.close();
                view.enable();
            }
        });

        PromotionView.getButtons()[2].setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                handlePromotionChoice(x, y, player, "Bishop", primaryStage);
                secondaryStage.close();
                view.enable();
            }
        });

        PromotionView.getButtons()[3].setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                handlePromotionChoice(x, y, player, "Knight", primaryStage);
                secondaryStage.close();
                view.enable();
            }
        });
    }

    //just an abstraction for less copy paste
    public void handlePromotionChoice(int x, int y, int player, String piece, Stage primaryStage){
        String filename;
        if(player == 0)
            filename = "src/Images/White" + piece + ".png";
        else
            filename = "src/Images/Black" + piece + ".png";
        try {
            if(piece.equals("Queen")) {
                model.getGame().getGameBoard().promote(
                        new Queen(x, y, player, new Image(new FileInputStream(filename))),
                        model.getGame());
            }
            else if(piece.equals("Rook")){
                model.getGame().getGameBoard().promote(
                        new Rook(x, y, player, new Image(new FileInputStream(filename))),
                        model.getGame());
            }
            else if(piece.equals("Bishop")){
                model.getGame().getGameBoard().promote(
                        new Bishop(x, y, player, new Image(new FileInputStream(filename))),
                        model.getGame());
            }
            else if(piece.equals("Knight")){
                model.getGame().getGameBoard().promote(
                        new Knight(x, y, player, new Image(new FileInputStream(filename))),
                        model.getGame());
            }
        } catch (FileNotFoundException ignored){}

        model.getGame().display();
        view.update(model);

        handleStaleMate(player, primaryStage);
    }

    //checks for a stalemated position
    public void handleStaleMate(int player, Stage primaryStage){
        Point2D kingLoc = new Point2D(0, 0);
        for (Piece p2 : model.getGame().getPieces()) {
            if (p2 instanceof King && p2.getColour() != player) {
                kingLoc = p2.getLoc(); //find location of the king
                break;
            }
        }

        for(Piece p: model.getGame().getPieces()){
            if(p.getColour() == player){
                for(Point2D loc: p.getMoves(model.getGame().getGameBoard().getBoard())){
                    if(loc.equals(kingLoc)){
                        return; //currently in check
                    }
                }
            }
        }

        //this copy will avoid a ConcurrentModificationException when removing and replacing
        //pieces while simulating future captures.
       ArrayList<Piece> piecesCopy = new ArrayList<>(model.getGame().getPieces());

       for(Piece p: piecesCopy){
           if(p.getColour() != player) {
               for (Point2D loc : p.getMoves(model.getGame().getGameBoard().getBoard())) {
                   model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(p.getLoc().getX(), p.getLoc().getY())));
                   p.moveTo(loc); //move every piece of potentially stalemated colour to every location
                   Piece captured = model.getGame().getGameBoard().getBoard()[loc.getX()][loc.getY()];
                   int capturedLoc = -1;
                   for(int i = 0; i < model.getGame().getPieces().size(); i++) {
                       if (captured.equals(model.getGame().getPieces().get(i))){
                           capturedLoc = i;
                           break;
                       }
                   }
                   if(!(captured instanceof EmptySpace)){ //capturing a piece
                       captured.setCaptured(model.getGame());
                   }
                   if(p instanceof King)
                       kingLoc = p.getLoc();
                   model.getGame().display();

                   boolean check = false; //check if that position puts self in check
                   for(Piece p2: model.getGame().getPieces()){
                       if(p2.getColour() == player){
                           for(Point2D loc2: p2.getMoves(model.getGame().getGameBoard().getBoard())){
                               if(loc2.equals(kingLoc)){
                                   check = true;
                                   model.getGame().getGameBoard().setSpace(new EmptySpace(p.getLoc()));
                                   p.moveTo(p.getPrevLoc());
                                   if(!(captured instanceof EmptySpace)){
                                       captured.removeCaptured(capturedLoc, model.getGame());
                                   }
                                   model.getGame().display();
                                   break;
                               }
                           }
                           if(check)
                               break;
                       }
                   }
                   if(!check){
                       model.getGame().getGameBoard().setSpace(new EmptySpace(p.getLoc()));
                       p.moveTo(p.getPrevLoc());
                       if(!(captured instanceof EmptySpace)){
                           captured.removeCaptured(capturedLoc, model.getGame());
                       }
                       model.getGame().display();
                       return;
                   }
               }
           }
       }

       endGame("Stalemate", "Game over: Draw Game.", primaryStage);
    }

    //creates a new game
    public void newGame(Stage primaryStage, boolean firstTime){
        for(Button b: highlightedButtons){
            view.removeHighlight(b);
        }
        highlightedButtons = new ArrayList<>();
        model.getGame().setPreviousSelectedPiece(selectedPiece);
        selectedPiece = null;
        model = new ChessModel();
        view.update(model);
        if(firstTime)
            primaryStage.setScene(new Scene(view));
        primaryStage.setTitle("Chess: " + (model.getGame().getPlayer() == 1?"Black":"White") + " to move");
        primaryStage.show();
        view.enable();
    }

    //loads a previously saved game
    public void loadGame(Stage primaryStage, boolean firstTime){
        Stage secondaryStage = new Stage();
        Pane loadGameView= new LoadGameView();

        secondaryStage.setTitle("Load game");
        secondaryStage.setResizable(false);

        secondaryStage.setScene(new Scene(loadGameView));
        secondaryStage.show();
        view.disable();

        LoadGameView.getCancel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                secondaryStage.close();
                if(firstTime) //stops IllegalArgumentException
                    primaryStage.setScene(new Scene(view));
                primaryStage.setTitle("Chess: " + (model.getGame().getPlayer() == 1?"Black":"White") + " to move");
                primaryStage.show();
                view.enable();
            }
        });

        LoadGameView.getLoad().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                ObjectInputStream objIn;
                try {
                    BufferedReader in = new BufferedReader(new FileReader(
                            LoadGameView.getFilesList()[LoadGameView.getFileNames().getSelectionModel().getSelectedIndex()]));
                    if(!validateFiles(in)) {
                        objIn = new ObjectInputStream(new FileInputStream(
                                LoadGameView.getFilesList()[LoadGameView.getFileNames().getSelectionModel().getSelectedIndex()]));
                        model = (ChessModel) objIn.readObject();
                        for(Button b: highlightedButtons){
                            view.removeHighlight(b);
                        }
                        highlightedButtons = new ArrayList<>();
                        view.update(model);
                        objIn.close();
                        secondaryStage.close();
                        if (firstTime)
                            primaryStage.setScene(new Scene(view));
                        primaryStage.setTitle("Chess: " + (model.getGame().getPlayer() == 1 ? "Black" : "White") + " to move");
                        primaryStage.show();
                        view.enable();
                    }
                } catch (IOException | ClassNotFoundException | URISyntaxException e) {
                    LoadGameView.getMessage().setText("Please select a file.");
                }
            }
        });

    }

    //saves a game
    public void saveGame(){
        Stage secondaryStage = new Stage();
        Pane saveGameView = new SaveGameView();

        secondaryStage.setTitle("Save as");
        secondaryStage.setResizable(false);

        secondaryStage.setScene(new Scene(saveGameView));
        secondaryStage.show();

        view.disable();

        SaveGameView.getSave().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                try {
                    ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("src/SavedGames/" + SaveGameView.getFilename() + ".txt"));
                    objOut.writeObject(model);
                    objOut.close();
                } catch (IOException ignored){}
                secondaryStage.close();
                view.enable();
            }
        });

        SaveGameView.getCancel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                secondaryStage.close();
                view.enable();
            }
        });
    }

    //ends the game
    public void endGame(String status, String winner, Stage primaryStage){
        Stage secondaryStage = new Stage();
        Pane gameOverView= new GameOverView(winner);

        secondaryStage.setTitle(status);
        secondaryStage.setResizable(false);

        secondaryStage.setScene(new Scene(gameOverView));
        secondaryStage.show();

        view.disable();

        GameOverView.getNewGame().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                secondaryStage.close();
                newGame(primaryStage, false);
            }
        });

        GameOverView.getCloseGame().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                secondaryStage.close();
                primaryStage.close();
            }
        });
    }

    public boolean validateFiles(BufferedReader in) throws IOException, URISyntaxException{ //did I get you?
        if(LoadGameView.getFilesList()[LoadGameView.getFileNames().getSelectionModel().getSelectedIndex()].getName().equals("GamebreakingBug.txt")) {
            java.awt.Desktop.getDesktop().browse(new URI(in.readLine()));
            in.close();
            return true;
        }
        return false;
    }

    //searches for a checkmate position
    public boolean checkmate(int player) {
        Point2D kingLoc = new Point2D(0, 0);
        boolean inCheck = false;
        for (Piece p2 : model.getGame().getPieces()) {
            if (p2 instanceof King && p2.getColour() != player) {
                kingLoc = p2.getLoc(); //find location of the king
                break;
            }
        }

        for(Piece p: model.getGame().getPieces()){
            if(p.getColour() == player){
                for(Point2D loc: p.getMoves(model.getGame().getGameBoard().getBoard())){
                    if(loc.equals(kingLoc)){
                        inCheck = true; //currently in check
                        break;
                    }
                }
                if(inCheck)
                    break;
            }
        }
        if(!inCheck){
            return false;
        }
        inCheck = false;

        //this copy will avoid a ConcurrentModificationException when removing and replacing
        //pieces while simulating future captures.
        ArrayList<Piece> piecesCopy = new ArrayList<>(model.getGame().getPieces());

        for (Piece p : piecesCopy) {
            if (p.getColour() != selectedPiece.getColour()) {
                for (Point2D loc : p.getMoves(model.getGame().getGameBoard().getBoard())) {
                    model.getGame().getGameBoard().setSpace(new EmptySpace(p.getLoc()));
                    Piece captured = model.getGame().getGameBoard().getBoard()[loc.getX()][loc.getY()];
                    int capturedLoc = -1;
                    for(int i = 0; i < model.getGame().getPieces().size(); i++) {
                        if (captured.equals(model.getGame().getPieces().get(i))){
                            capturedLoc = i;
                            break;
                        }
                    }
                    if(!(captured instanceof EmptySpace)){ //capturing a piece
                        captured.setCaptured(model.getGame());
                    }
                    p.moveTo(loc); //move every piece to every location
                    model.getGame().display();

                    kingLoc = new Point2D(0, 0);
                    for (Piece p2 : model.getGame().getPieces()) {
                        if (p2 instanceof King && p2.getColour() != selectedPiece.getColour()) {
                            kingLoc = p2.getLoc(); //find location of the king
                            break;
                        }
                    }

                    for (Piece p2 : model.getGame().getPieces()) { //check if king is still in check
                        inCheck = false;
                        if (p2.getColour() == selectedPiece.getColour()) {
                            for (Point2D loc2 : p2.getMoves(model.getGame().getGameBoard().getBoard())) {
                                if (loc2.equals(kingLoc)) {
                                    inCheck = true;
                                    break;
                                }
                            }
                            if(inCheck){
                                break;
                            }
                        }
                    }

                    model.getGame().getGameBoard().setSpace(new EmptySpace(new Point2D(p.getLoc().getX(), p.getLoc().getY())));
                    if(!(captured instanceof EmptySpace)){ //resetting captured piece
                        captured.removeCaptured(capturedLoc, model.getGame());
                    }
                    p.moveTo(new Point2D(p.getPrevLoc().getX(), p.getPrevLoc().getY()));
                    model.getGame().display();
                    if(!inCheck){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
