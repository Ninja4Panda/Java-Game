package unsw.gloriaromanus.StartUpMenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import unsw.gloriaromanus.ConfigMenu.ConfigScreen;
import javafx.event.ActionEvent;

import java.io.IOException;

public class StartMenuController {
    @FXML
    private Button newGameBtn;
    @FXML
    private Button loadGameBtn;
    private ConfigScreen configScreen;

    public void setConfigScreen(ConfigScreen configScreen) {
        this.configScreen = configScreen;
    }

    @FXML
    void handleNewBtn(ActionEvent e) throws IOException {
        configScreen.start();
    }

    @FXML
    void handleLoadBtn(ActionEvent e) {

    }
}
