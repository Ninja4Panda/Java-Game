package unsw.gloriaromanus.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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
    private Stage popupStage;
    private SaveScreen saveScreen;
    private Game game;

    public EscMenuController(Parent root, Stage popupStage, Game game) {
        this.root = root;
        this.popupStage = popupStage;
        this.game = game;
        this.saveScreen = new SaveScreen(popupStage);
    }

    @FXML
    void handleBackBtn() {
        root.setEffect(null);
        popupStage.hide();
    }

    @FXML
    void handleSaveBtn() {
        try {
            saveScreen.start(game);
        } catch (IOException e) {
            //Unable to load save
        }
    }

    @FXML
    void handleQuitBtn() {
        Platform.exit();
    }
}
