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

    public void update(GamePhase phase) {
        this.phase.setText(phase.toString());
        if(phase instanceof PreparationPhase) {
            changePhase.setText("Next Phase");
        } else {
            changePhase.setText("End Turn");
        }
    }

    public void setLost() {
        this.phase.setText("You Lost");
        changePhase.setText("Great Game!");
    }
}
