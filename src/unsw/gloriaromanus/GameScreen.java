package unsw.gloriaromanus;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        //Load new scene into the stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(controller);
//        controller.setGame(game);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        //Display the stage
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public GloriaRomanusController getController() {
        return controller;
    }
}
