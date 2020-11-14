package unsw.gloriaromanus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import unsw.gloriaromanus.scenes.StartScreen;

import java.io.IOException;

public class InfoScreenController {
    @FXML
    private Button backbtn;
    @FXML
    private ScrollPane scrollPane;
    private StartScreen startScreen;

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @FXML
    void handleBackBtn() {
        try {
            startScreen.start();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
    }
}
