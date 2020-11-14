package unsw.gloriaromanus.Controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import unsw.gloriaromanus.Game.Game;
import unsw.gloriaromanus.scenes.SaveScreen;

import java.io.IOException;

public class EscMenuController {
    @FXML
    private Button saveGameBtn;
    @FXML
    private Button quitGameBtn;
    @FXML
    private Button backBtn;

    private Parent root;
    private Stage stage;
    private SaveScreen saveScreen;
    private Game game;

    public EscMenuController(Parent root, Stage popupStage, Game game) {
        this.root = root;
        this.stage = popupStage;
        this.game = game;
        this.saveScreen = new SaveScreen(popupStage);
    }

    @FXML
    void handleBackBtn() {
        root.setEffect(null);
        stage.hide();
    }

    @FXML
    void handleSaveBtn() {
        try {
            saveScreen.start(game);
        } catch (IOException error) {
            //Unable to load save
            error.printStackTrace();

            //Modal
            Stage errorStage = new Stage();
            errorStage.initStyle(StageStyle.UNDECORATED);
            errorStage.initModality(Modality.APPLICATION_MODAL);
            errorStage.initOwner(stage);

            //Error msg
            Label errorMsg = new Label();
            errorMsg.setText("Failed to load saves!");
            errorMsg.setStyle("-fx-font-size: 24; -fx-text-fill: red");
            errorStage.setScene(new Scene(errorMsg));
            errorStage.show();

            //Auto close after 3sec
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e->errorStage.hide());
            delay.play();
        }
    }

    @FXML
    void handleQuitBtn() {
        Platform.exit();
    }
}
