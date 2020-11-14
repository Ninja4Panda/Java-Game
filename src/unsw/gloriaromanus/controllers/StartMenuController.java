package unsw.gloriaromanus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import unsw.gloriaromanus.scenes.ConfigScreen;
import unsw.gloriaromanus.scenes.InfoScreen;
import unsw.gloriaromanus.scenes.LoadSaveScreen;

import java.io.IOException;

public class StartMenuController {
    @FXML
    private Button newGameBtn;
    @FXML
    private Button loadGameBtn;
    @FXML
    private Button infoBtn;
    private ConfigScreen configScreen;
    private LoadSaveScreen loadSaveScreen;
    private InfoScreen infoScreen;

    public void setConfigScreen(ConfigScreen configScreen) {
        this.configScreen = configScreen;
    }

    public void setLoadSaveScreen(LoadSaveScreen loadSaveScreen) {
        this.loadSaveScreen = loadSaveScreen;
    }

    public void setInfoScreen(InfoScreen infoScreen) {
        this.infoScreen = infoScreen;
    }

    @FXML
    void handleNewBtn() {
        System.out.println("ddd");
        try {
            configScreen.start();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
    }

    @FXML
    void handleLoadBtn() {
        try {
            loadSaveScreen.start();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
    }

    @FXML
    void handleInfoBtn() {
        try {
            infoScreen.start();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
    }
}
