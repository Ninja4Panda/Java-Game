package unsw.gloriaromanus.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.scenes.InfoScreen;
import unsw.gloriaromanus.scenes.PopUpInfoScreen;
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
    private PopUpInfoScreen infoScreen;
    private Game game;

    public EscMenuController(Parent root, Stage popupStage, Game game) {
        this.root = root;
        this.stage = popupStage;
        this.game = game;
        this.saveScreen = new SaveScreen(popupStage);
        this.infoScreen = new PopUpInfoScreen(popupStage);
    }

    @FXML
    void handleInfoBtn() {
        try {
            infoScreen.start();
        } catch (IOException error) {
            error.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
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
            error.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
    }

    @FXML
    void handleQuitBtn() {
        Platform.exit();
    }
}
