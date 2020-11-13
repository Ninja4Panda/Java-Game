package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        loader = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
        Stage popupStage = new Stage(StageStyle.UNDECORATED);
        loader.setController(new EscMenuController(root, popupStage, game));
        Parent pauseMenu = loader.load();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(gameStage);
        popupStage.setScene(new Scene(pauseMenu));

        //Handle Esc key press
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE) {
                root.setEffect(new BoxBlur());
                popupStage.show();
            }
        });
    }

    public GloriaRomanusController getController() {
        return controller;
    }
}
