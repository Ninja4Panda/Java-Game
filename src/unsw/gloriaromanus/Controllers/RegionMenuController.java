package unsw.gloriaromanus.Controllers;

import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

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

    private boolean isLeftSelected;
    private boolean isRightSelected;
    

    @FXML
    private void handleInteraction(){

    }

    public boolean isLeftSelected() {
        return isLeftSelected;
    }

    public boolean isRightSelected() {
        return isRightSelected;
    }

    public void handleLeftClick(String name, List<Unit> units) {
        leftProvinceLabel.setText(name);
        leftScrollVbox.getChildren().clear();
        for(Unit u : units) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
            try {
                Pane root = (Pane) loader.load();
                UnitPaneController UPC = (UnitPaneController) loader.getController();
                UPC.configure(u);
                UPC.setParent(this);
                leftScrollVbox.getChildren().add(root);
    
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRightClick(String name, List<Unit> units) {

        if(Objects.equals("Preparation", this.getParent().getCurPhase())) {
            handleLeftClick(name, units);
        } else {
            rightProvinceLabel.setText(name);
            rightScrollVbox.getChildren().clear();
            for(Unit u : units) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
                try {
                    Pane root = (Pane) loader.load();
                    UnitPaneController UPC = (UnitPaneController) loader.getController();
                    UPC.configure(u);
                    UPC.setParent(this);
                    rightScrollVbox.getChildren().add(root);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

  


    @FXML
    public void initialize() {
  
    }

}
