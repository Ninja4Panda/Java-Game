package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.gloriaromanus.controllers.ConfigMenuController;

import java.io.IOException;

public class ConfigScreen {
    private Stage stage;
    private ConfigMenuController controller;

    public ConfigScreen(Stage stage) {
        this.stage = stage;
        controller = new ConfigMenuController();
    }

    public void start() throws IOException {
        //Load new scene into the stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("configMenu.fxml"));
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

    public ConfigMenuController getController() {
        return controller;
    }
}
