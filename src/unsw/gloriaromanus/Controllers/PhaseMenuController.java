package unsw.gloriaromanus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import unsw.gloriaromanus.MenuController;

public class PhaseMenuController extends MenuController{
    @FXML
    private Label phase;

    @FXML
    private Button changePhase;

    @FXML
    public void handleChangePhase() {
        this.getParent().endPhase();
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
