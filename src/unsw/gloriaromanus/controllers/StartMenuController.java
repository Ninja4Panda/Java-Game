package unsw.gloriaromanus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import unsw.gloriaromanus.scenes.ConfigScreen;
import javafx.event.ActionEvent;
import unsw.gloriaromanus.scenes.LoadSaveScreen;

import java.io.IOException;

public class StartMenuController {
    @FXML
    private Button newGameBtn;
    @FXML
    private Button loadGameBtn;
    private ConfigScreen configScreen;
    private LoadSaveScreen loadSaveScreen;

    public void setConfigScreen(ConfigScreen configScreen) {
        this.configScreen = configScreen;
    }

    public void setLoadSaveScreen(LoadSaveScreen loadSaveScreen) {
        this.loadSaveScreen = loadSaveScreen;
    }

    @FXML
    void handleNewBtn(ActionEvent e) throws IOException {
        configScreen.start();
    }

    @FXML
    void handleLoadBtn(ActionEvent e) throws IOException {
        loadSaveScreen.start();
    }
}
