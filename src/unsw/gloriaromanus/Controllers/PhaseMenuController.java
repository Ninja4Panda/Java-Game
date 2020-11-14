package unsw.gloriaromanus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import unsw.gloriaromanus.MenuController;

import java.io.IOException;

public class PhaseMenuController extends MenuController{
    @FXML
    private Label phase;

    @FXML
    private Button changePhase;

    @FXML
    public void handleChangePhase() {
        try {
            this.getParent().endPhase();
        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Error occurred when resetting graphics");
            a.show();
        }
    }

    public void update(String phase) {
        switch (phase) {
            case "Preparation":
                this.phase.setText("Preparation");
                changePhase.setText("Next Phase");
                break;
            case "Move":
                this.phase.setText("Move Phase");
                changePhase.setText("End Turn");
            default:
                break;
        }
    }

    @FXML
    private void initialize() {
        
    }
}
