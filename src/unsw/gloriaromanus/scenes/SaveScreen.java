package unsw.gloriaromanus.scenes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unsw.gloriaromanus.Controllers.SaveMenuController;
import unsw.gloriaromanus.Game.Game;

import java.io.IOException;

public class SaveScreen {
    private Stage stage;

    public SaveScreen(Stage stage) {
        this.stage = stage;
    }

    public void start(Game game) throws IOException {
        Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);

        //Load new scene into the stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("saveMenu.fxml"));
        loader.setController(new SaveMenuController(popupStage, game));
        Parent root = loader.load();

        //Set the popup size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();
        popupStage.setScene(new Scene(root, width*0.8, height*0.95));

        //Display the stage
        popupStage.show();
    }
}
