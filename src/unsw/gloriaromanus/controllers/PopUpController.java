package unsw.gloriaromanus.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class PopUpController {
    @FXML
    private Button backBtn;
    private Stage stage;

    public PopUpController(Stage popupStage) {
        this.stage = popupStage;
    }

    @FXML
    void handleBackBtn(ActionEvent e) {
        stage.hide();
    }
}
