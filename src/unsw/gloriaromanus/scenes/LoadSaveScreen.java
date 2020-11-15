package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.gloriaromanus.controllers.LoadSaveMenuController;

import java.io.IOException;

public class LoadSaveScreen {
    private Stage stage;
    private LoadSaveMenuController controller;

    public LoadSaveScreen(Stage stage) {
        this.stage = stage;
        controller = new LoadSaveMenuController();
    }

    public LoadSaveMenuController getController() {
        return controller;
    }

    public void start() throws IOException {
        //Load new scene into the stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("saveMenu.fxml"));
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
}
