package unsw.gloriaromanus.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

public class RegionMenuController extends MenuController {


    @FXML
    private MenuButton taxDropDown; 
    
    @FXML
    private Label wealth;

    @FXML 
    private Label wealthMirror;

    @FXML
    private Label tax;

    @FXML 
    private Label taxMirror;

    private int changeToTax;

    @FXML
    public void setFirstTaxBracket(){
        taxDropDown.setText("10%");
        changeToTax = 10;
    }

    @FXML
    public void setSecondTaxBracket(){
        taxDropDown.setText("15%");
        changeToTax = 15;
    }

    @FXML
    public void setThirdTaxBracket(){
        taxDropDown.setText("20%");
        changeToTax = 20;
        
    }

    @FXML
    public void setFourthTaxBracket(){
        taxDropDown.setText("25%");
        changeToTax = 25;
    }

    @FXML
    private VBox headerVboxRight;

    @FXML
    private VBox headerVboxLeft;

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
    
    private HashMap<UnitPaneController, Unit> leftUnits;
    private HashMap<UnitPaneController, Unit> rightUnits;

    private List<Unit> selectedUnits;
    
   

    @FXML
    private void handleInteraction(){
        List<String> train = new ArrayList<>();
        for(Unit u : selectedUnits) {
            train.add(u.getClassName());
        }
        this.getParent().regionConRequest(changeToTax, train, leftProvinceLabel.getText());
        rightProvinceLabel.setText("New " + leftProvinceLabel.getText());
        interactionButton.setText("Cancel");
    }

    public boolean isLeftSelected() {
        return isLeftSelected;
    }

    public boolean isRightSelected() {
        return isRightSelected;
    }

    public void handleLeftClick(Region region) {
        
        leftProvinceLabel.setText(region.getName());
        leftScrollVbox.getChildren().clear();
        List<Unit> units = region.getUnits();
        for(Unit u : units) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
            try {
                Pane root = (Pane) loader.load();
                UnitPaneController UPC = (UnitPaneController) loader.getController();
                UPC.configure(u, false);
                UPC.setParent(this);
                leftScrollVbox.getChildren().add(root);
                leftUnits.put(UPC, u);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Objects.equals(this.getParent().getCurPhase(), "Preparation") ) {
            handleRightClick("After Train", units);
        }
        wealth.setText(Integer.toString(region.getWealth()));
        // tax.setText(Integer.toString(region.getTax()));
        wealthMirror.setText(Integer.toString(region.getWealth()));
        // taxMirror.setText(Integer.toString(region.getTax()));
    }

    public void handleRightClick(String name, List<Unit> units) {
        rightProvinceLabel.setText(name);
        rightScrollVbox.getChildren().clear();
        for(Unit u : units) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
            try {
                Pane root = (Pane) loader.load();
                UnitPaneController UPC = (UnitPaneController) loader.getController();
                UPC.configure(u, true);
                UPC.setParent(this);
                rightScrollVbox.getChildren().add(root);
                rightUnits.put(UPC, u);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
        
    }

    public void selectUnit(Unit unit) {
        selectedUnits.add(unit);
        for(UnitPaneController UPC : rightUnits.keySet() ) {
            if(Objects.equals(rightUnits.get(UPC).getClassName(), unit.getClassName())) {
                UPC.showAmountAdded(unit);
            }
        }
    }

    public void deselectUnit(Unit unit) {
        selectedUnits.remove(unit);
        for(UnitPaneController UPC : rightUnits.keySet() ) {
            if(Objects.equals(rightUnits.get(UPC).getClassName(), unit.getClassName())) {
                UPC.revertShowAmountAdded(unit);
            }
        }
    }

    @FXML
    public void initialize() {
        selectedUnits = new ArrayList<>();
        leftUnits = new HashMap<>();
        rightUnits = new HashMap<>();
        
    }

}
