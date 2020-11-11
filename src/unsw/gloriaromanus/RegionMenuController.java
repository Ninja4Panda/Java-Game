package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class RegionMenuController extends MenuController {
    @FXML
    private Label leftProvinceLabel;

    @FXML
    private Label rightProvinceLabel;

    @FXML
    private VBox leftScrollVbox;

    @FXML
    private VBox rightScrollVbox;

    @FXML
    private Button interactionButton;

    @FXML
    private void handleInteraction(){

    }

    @FXML
    public void initialize() {
  
        // set up the titledPane for all units
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/unitTitledPane.fxml"));
        try {
            TitledPane root = (TitledPane) loader.load();
            if(root.getChildrenUnmodifiable().isEmpty()) {
                System.out.println("no dropdown");

            }
            leftScrollVbox.getChildren().add(root);

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
