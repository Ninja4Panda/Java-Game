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

    @FXML
    private void initialize() {
        
    }
}
