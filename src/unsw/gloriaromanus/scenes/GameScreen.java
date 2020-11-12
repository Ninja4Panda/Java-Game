package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unsw.gloriaromanus.Controllers.EscMenuController;
import unsw.gloriaromanus.GloriaRomanusController;
import unsw.gloriaromanus.Game.Game;

import java.io.IOException;

public class GameScreen {
    private Stage stage;
    private GloriaRomanusController controller;

    public GameScreen(Stage stage) {
        controller = new GloriaRomanusController();
        this.stage = stage;
    }

    public void start(Game game) throws IOException {
        controller.setGame(game);
        //Load new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(controller);
        Parent root = loader.load();

        //Get the window size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();
        Scene scene = new Scene(root, width, height);

        //Display the stage
        stage.hide();
        Stage gameStage = new Stage();
        gameStage.initStyle(StageStyle.UNDECORATED);
        gameStage.setScene(scene);
        gameStage.show();

        //Popup stage for the pause menu
        loader = new FXMLLoader(getClass().getResource("escMenu.fxml"));
        loader.setController(new EscMenuController());
        Parent pauseMenu = loader.load();
        Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initOwner(gameStage);
        popupStage.setScene(new Scene(pauseMenu));

        //Handle Esc key press
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE) {
                root.setEffect(new GaussianBlur());
                popupStage.show();
            }
        });
    }

    /**
     * Show the menu when Esc key is pressed
     * @param gameStage the game stage
     * @param root root of the game
     */
    private void handleEscapeBtn(Stage gameStage, Parent root) {
        VBox pauseMenu = new VBox();
        pauseMenu.getChildren().add(new Label("Paused Menu"));
        pauseMenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setPadding(new Insets(20));

        Button resume = new Button("Resume");
        pauseMenu.getChildren().add(resume);

        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(gameStage);
        popupStage.setScene(new Scene(pauseMenu));
        popupStage.show();


        resume.setOnAction(event -> {
            root.setEffect(null);
//            animation.play();
            popupStage.hide();
        });

    }

    public GloriaRomanusController getController() {
        return controller;
    }
}
