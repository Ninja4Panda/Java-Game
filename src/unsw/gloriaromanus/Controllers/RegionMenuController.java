package unsw.gloriaromanus.Controllers;

import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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
    private Label taxMirror;


    @FXML
    public void setFirstTaxBracket(){
        taxMirror.setText("10%");
        taxDropDown.setText("10%");
        this.getParent().regionConTaxReform(10, leftProvinceLabel.getText());
    }

    @FXML
    public void setSecondTaxBracket(){
        taxMirror.setText("15%");
        taxDropDown.setText("15%");
        this.getParent().regionConTaxReform(15, leftProvinceLabel.getText());
    }

    @FXML
    public void setThirdTaxBracket(){
        taxMirror.setText("20%");
        taxDropDown.setText("20%");
        this.getParent().regionConTaxReform(20, leftProvinceLabel.getText());
        
    }

    @FXML
    public void setFourthTaxBracket(){
        taxMirror.setText("25%");
        taxDropDown.setText("25%");
        this.getParent().regionConTaxReform(25, leftProvinceLabel.getText());
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
            for(UnitPaneController UPC : rightUnits.keySet() ) {
                if(Objects.equals(rightUnits.get(UPC).getClassName(), u.getClassName())) {
                    UPC.revertShowAmountAdded(u);
                }
            }
            for(UnitPaneController UPC : leftUnits.keySet() ) {
                if(Objects.equals(leftUnits.get(UPC).getClassName(), u.getClassName())) {
                    UPC.revertShowAmountAdded(u);
                }
            }
        }

        selectedUnits.clear();
        String msg = this.getParent().regionConTrainRequest( train, leftProvinceLabel.getText());
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,leftProvinceLabel.getText()+msg);
        a.show();
        rightProvinceLabel.setText("New " + leftProvinceLabel.getText());
    }

    @FXML
    private void handleMove() {
        List<String> moveUnits = new ArrayList<>();
        for(Unit u : selectedUnits) {
            moveUnits.add(u.getClassName());
        }

        String msg = this.getParent().regionMoveRequest(leftProvinceLabel.getText(), rightProvinceLabel.getText(), moveUnits);
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,leftProvinceLabel.getText()+msg);
        a.setResizable(true);
        a.show();
    }

    @FXML
    private void handleAttack() {
        List<String> attackUnits = new ArrayList<>();
        for(Unit u : selectedUnits) {
            attackUnits.add(u.getClassName());
        }
        String msg = this.getParent().regionAttackRequest(leftProvinceLabel.getText(), rightProvinceLabel.getText(), attackUnits);
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,leftProvinceLabel.getText()+msg);
        a.show();
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
            handleRightClick("After Training", units, false);
        }
        wealth.setText(Integer.toString(region.getWealth()));
        taxDropDown.setText(Integer.toString(region.getTax()));
        wealthMirror.setText(Integer.toString(region.getWealth()));
        taxMirror.setText(Integer.toString(region.getTax()));
    }

    public void handleRightClick(String name, List<Unit> units, boolean isEnemy) {
        rightProvinceLabel.setText(name);
        rightScrollVbox.getChildren().clear();
        
        if(units == null) {
            if(leftUnits == null) {
                // error msg "please select attacking region first"
                return;
            } else {
            units = new ArrayList<>();
            units.addAll(leftUnits.values());
            }
        }
       
        for(Unit u : units) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
            try {
                Pane root = (Pane) loader.load();
                UnitPaneController UPC = (UnitPaneController) loader.getController();
                if(isEnemy) { 
                    UPC.configureEnemy(u);
                    setAttackButton();
                }else {
                    UPC.configure(u, true);
                        if(Objects.equals(this.getParent().getCurPhase(), "Preparation") ) {
                            setTrainButton();
                        } else {
                            setMoveButton();
                        }
                }
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
                if(Objects.equals(this.getParent().getCurPhase(), "Preparation")) {
                    UPC.showAmountAdded(unit.getTrainAmount());
                } else {
                    UPC.showAmountAdded(unit.getCurAmount());
                }
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
        for(UnitPaneController UPC : leftUnits.keySet() ) {
            if(Objects.equals(leftUnits.get(UPC).getClassName(), unit.getClassName())) {
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

    private void setMoveButton() {
        interactionButton.setText("Move");
        interactionButton.setOnAction(event -> handleMove() );
    }

    private void setAttackButton() {
        interactionButton.setText("Attack");
        interactionButton.setOnAction(event -> handleAttack() );
    }

    private void setTrainButton() {
        interactionButton.setText("Train");
        interactionButton.setOnAction(event -> handleInteraction() );

    }

    public void reset() {
        leftScrollVbox.getChildren().clear();
        rightScrollVbox.getChildren().clear();
        leftProvinceLabel.setText("Select Region");
        rightProvinceLabel.setText("Select Region");
        selectedUnits.clear();
        if(Objects.equals(this.getParent().getCurPhase(), "Move Phase")) {
            setAttackButton();
        } else {
            setTrainButton();
        }
    }

}
