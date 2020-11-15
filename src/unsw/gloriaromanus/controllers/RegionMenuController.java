package unsw.gloriaromanus.controllers;

import java.io.IOException;
import java.util.*;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.phase.MovePhase;
import unsw.gloriaromanus.phase.PreparationPhase;
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
    private VBox logBox;

    @FXML
    private Pane leftPane;

    @FXML
    private Pane rightPane;

    @FXML
    private Label leftTaxLabel;

    @FXML
    private Label leftWealthLabel;

    @FXML
    private Label rightWealthLabel;

    @FXML
    private Label rightTaxLabel;

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

    
    private HashMap<UnitPaneController, Unit> leftUnits;
    private HashMap<UnitPaneController, Unit> rightUnits;

    private List<Unit> selectedUnits;
    
   

    @FXML
    private void handleTrain() {
        // Handles when no target is selected
        if(this.getParent().getCurrentlySelectedLeftProvince()==null) {
            showSummary("Please select a origin region");
            return;
        }
        // Handles when no unit is selected
        if(selectedUnits.size()==0) {
            showSummary("Please select unit to train");
            return;
        }

        // adds all selected units into a training list
        List<String> train = new ArrayList<>();
        for(Unit u : selectedUnits) {
            train.add(u.getClassName());
        }

        // Sends the training list to backend
        String msg = this.getParent().regionConTrainRequest(train, leftProvinceLabel.getText());
        // show a msg to user about results of the training
        showSummary(msg);
        try {
            this.getParent().resetSelections();
        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Error occurred when resetting graphics");
            a.show();
        }
    }

    @FXML
    private void handleMove() {
        //Handles when no target is selected
        if(this.getParent().getCurrentlySelectedLeftProvince()==null) {
            showSummary("Please select a origin region");
            return;
        }
        //Handles when no target is selected
        if(this.getParent().getCurrentlySelectedRightProvince()==null) {
            showSummary("Please select a target region");
            return;
        }

        //Handles when no unit is selected
        if(selectedUnits.size()==0) {
            showSummary("Please select unit to move");
            return;
        }

        //Handles moving the units
        List<String> moveUnits = new ArrayList<>();
        for(Unit u : selectedUnits) {
            moveUnits.add(u.getClassName());
        }

        String origin = leftProvinceLabel.getText();
        String target = rightProvinceLabel.getText();
        try {
            String msg = this.getParent().regionMoveRequest(origin, target, moveUnits);
            showSummary(msg);
            addLog("======Moving from "+origin+" to "+target+"======\n"+msg);
            this.getParent().resetSelections();
        } catch(IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Unexpected move error");
            a.show();
        }
    }

    @FXML
    private void handleAttack() {
        //Handles when no target is selected
        if(this.getParent().getCurrentlySelectedLeftProvince()==null) {
            showSummary("Please select a origin region");
            return;
        }
        if(selectedUnits.size()==0) {
            showSummary("Please select unit to attack");
            return;
        }
        List<String> attackUnits = new ArrayList<>();
        for(Unit u : selectedUnits) {
            attackUnits.add(u.getClassName());
        }
        String origin = leftProvinceLabel.getText();
        String target = rightProvinceLabel.getText();
        try {
            String msg = this.getParent().regionAttackRequest(origin, target, attackUnits);
            showSummary(msg);
            addLog("======Attacking from "+origin+" to "+target+"======\n"+msg);
            this.getParent().resetSelections();
        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Unexpected attack error");
            a.show();
        }
    }

    /**
     * Add to log box
     */
    public void addLog(String msg) {
        logBox.getChildren().add(new Text(msg));
    }

    /**
     * Clear the log
     */
    public void clearLog() {
        logBox.getChildren().clear();
    }

    /**
     * Shows the summary for a phase
     * @param msg msg to display
     */
    private void showSummary(String msg) {
        //Popup summary
        Stage stage = (Stage)interactionButton.getScene().getWindow();
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        //Container
        VBox row = new VBox();
        row.setSpacing(15);
        row.setAlignment(Pos.CENTER);
        //msg
        Label summary = new Label();
        summary.setText(msg);
        summary.setTextFill(Paint.valueOf("red"));
        summary.setFont(Font.font("",30));
        row.getChildren().add(summary);
        //Set the popup size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();
        popupStage.setScene(new Scene(row, width*0.5, height*0.5));
        popupStage.show();
        //Auto close after 1sec
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(e->popupStage.hide());
        delay.play();
    }

    /**
     * Changes the left side panel when left clicked on a region
     * @param region region object
     * @pre region is owned by current player
     */
    public void handleLeftClick(Region region) {
        // Ensures no information from previous clicks are passed on
        leftScrollVbox.getChildren().clear();
        selectedUnits.clear();
        leftUnits.clear();

        
        leftProvinceLabel.setText(region.getName());
        List<Unit> units = region.getUnits();

        for(Unit u : units) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
            try {
                Pane root = (Pane) loader.load();
                UnitPaneController UPC = (UnitPaneController) loader.getController();

                if(this.getParent().getCurPhase() instanceof  PreparationPhase ) {
                    if(region.getUnitsTraining().containsKey(u.getClassName()) ) {
                        UPC.configure(u, region.getUnitsTraining().get(u.getClassName()), false);
                    } else {
                        UPC.configure(u, 0, false);

                    }
                } else if (u.getCurAmount() != 0){
                    UPC.configure(u, 0, false);
                } else {
                    continue;
                }
                UPC.setParent(this);
                leftScrollVbox.getChildren().add(root);
                leftUnits.put(UPC, u);

                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(this.getParent().getCurPhase() instanceof  PreparationPhase) {
            handleRightClick("After Training", units, region.getUnitsTraining(), false);
        }
        wealth.setText(Integer.toString(region.getWealth()));
        taxDropDown.setText(Integer.toString(region.getTax()));
        wealthMirror.setText(Integer.toString(region.getWealth()));
        taxMirror.setText(Integer.toString(region.getTax()));
    }

    /**
     * Changes the right side panel when right clicked on a region
     * @param name name of the region
     * @param units Units list from backend
     * @param isEnemy true/false to indicate enemy or not
     */
    public void handleRightClick(String name, List<Unit> units, Hashtable<String, Integer> trainingUnits, boolean isEnemy) {
        // Ensures no information from previous clicks are retained
        rightScrollVbox.getChildren().clear();
        rightUnits.clear();
        
        rightProvinceLabel.setText(name);
        for(Unit u : units) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/unitPane.fxml"));
            try {
                Pane root = (Pane) loader.load();
                UnitPaneController UPC = (UnitPaneController) loader.getController();
                if(isEnemy) {
                    UPC.configureEnemy(u);
                } else {
                    if(trainingUnits != null && trainingUnits.containsKey(u.getClassName())) {
                        UPC.configure(u, trainingUnits.get(u.getClassName()), true);
                    } else {
                        UPC.configure(u, 0, true);
                    }
                }
                UPC.setParent(this);
                rightScrollVbox.getChildren().add(root);
                rightUnits.put(UPC, u);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Setting the button
        if(this.getParent().getCurPhase() instanceof PreparationPhase) {
            setTrainButton();
        } else if(isEnemy){
            setAttackButton();
        } else {
            setMoveButton();
        }

    }

    /**
     * Adds the unit into the selected list  and shows information about what
     * may happen to the unit
     * @param unit
     */
    public void selectUnit(Unit unit) {
        selectedUnits.add(unit);
        // loops through the controllers
        for(UnitPaneController UPC : rightUnits.keySet() ) {
            // if right unit is found, show the increased amount
            if(Objects.equals(rightUnits.get(UPC).getClassName(), unit.getClassName())) {

                if(this.getParent().getCurPhase() instanceof PreparationPhase) {
                    UPC.showAmountAdded(unit.getTrainAmount());
                } else {
                    UPC.showAmountAdded(unit.getCurAmount());
                }
            }
        }
        
    }

    /**
     * Remove the unit from the selected list and remove any information 
     * presented about what happens to a selected unit 
     * @param unit
     */
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

    public void setMoveButton() {
        setTaxWealthVisible(false);
        interactionButton.setText("Move");
        interactionButton.setOnAction(event -> handleMove() );
    }

    private void setAttackButton() {
        interactionButton.setText("Attack");
        interactionButton.setOnAction(event -> handleAttack() );
    }

    public void setTrainButton() {
        setTaxWealthVisible(true);
        interactionButton.setText("Train");
        interactionButton.setOnAction(event -> handleTrain() );
    }

    /**
     * Clears the Whole RegionMenuController, also updates the button
     * accordingly
     */
    public void reset() {
        leftScrollVbox.getChildren().clear();
        rightScrollVbox.getChildren().clear();
        leftProvinceLabel.setText("Select Region");
        rightProvinceLabel.setText("Select Region");
        taxMirror.setText("???");
        taxDropDown.setText("???");
        wealth.setText("???");
        wealthMirror.setText("???");
        selectedUnits.clear();
        leftUnits.clear();
        rightUnits.clear();
        if(this.getParent().getCurPhase() instanceof MovePhase) {
            setMoveButton();
        } else {
            setTrainButton();
        }
    }

    /**
     * Set the visibility of tax and wealth
     * @param b true/false
     */
    private void setTaxWealthVisible(boolean b) {
        for (Node child :leftPane.getChildren()) {
            child.setVisible(b);
        }
        for (Node child :rightPane.getChildren()) {
            child.setVisible(b);
        }
        leftPane.setVisible(b);
        rightPane.setVisible(b);
    }

}
