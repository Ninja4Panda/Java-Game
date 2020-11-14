package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.gloriaromanus.controllers.InfoScreenController;

import java.io.IOException;

public class InfoScreen {
    private Stage stage;
    private InfoScreenController controller;

    public InfoScreen(Stage stage) {
        this.stage = stage;
        controller = new InfoScreenController();
    }

    public void start() throws IOException {
        //Load new scene into the stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("info.fxml"));
        loader.setController(controller);
        Parent root = loader.load();

        //Maintain the same screen size
        double height = stage.getScene().getHeight();
        double width = stage.getScene().getWidth();
        Scene scene = new Scene(root, width, height);

        //Display the stage
        stage.setScene(scene);
        stage.show();
    }

    public InfoScreenController getController() {
        return controller;
    }
}
