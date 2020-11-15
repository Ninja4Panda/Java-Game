package unsw.gloriaromanus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.phase.GamePhase;
import unsw.gloriaromanus.phase.PreparationPhase;

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

    /**
     * Appropriately changes the Phase label to represent the current phase the game is in
     * and changePhase button text to represent what will happen when the button is pressed
     * @param phase current phase of the game
     */
    public void update(GamePhase phase) {
        this.phase.setText(phase.toString());
        if(phase instanceof PreparationPhase) {
            changePhase.setText("Next Phase");
        } else {
            changePhase.setText("End Turn");
        }
    }
}
