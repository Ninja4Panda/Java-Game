package unsw.gloriaromanus.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EscMenuController {
    @FXML
    private Button saveGameBtn;
    @FXML
    private Button quitGameBtn;
    @FXML
    private Button backBtn;

    private Parent root;
    private Stage popupStage;

    public EscMenuController(Parent root, Stage popupStage) {
        this.root = root;
        this.popupStage = popupStage;
    }

    @FXML
    void handleBackBtn() {
        root.setEffect(null);
        popupStage.hide();
    }

    @FXML
    void handleSaveBtn() {

    }

    @FXML
    void handleQuitBtn() {
        Platform.exit();
    }
}
