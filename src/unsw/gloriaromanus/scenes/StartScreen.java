package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.gloriaromanus.controllers.StartMenuController;

import java.io.IOException;

public class StartScreen {
    private String title;
    private Stage stage;
    private StartMenuController controller;

    public StartScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Gloria Romanus";
        stage.setTitle(title);

        controller = new StartMenuController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
    }

    public void start() throws IOException {
        //Load new scene into the stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));
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

    public StartMenuController getController() {
        return controller;
    }
}
