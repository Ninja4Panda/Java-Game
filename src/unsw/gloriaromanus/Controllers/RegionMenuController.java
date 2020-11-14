package unsw.gloriaromanus.Controllers;

import java.io.IOException;
import java.util.*;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import unsw.gloriaromanus.Phase.MovePhase;
import unsw.gloriaromanus.Phase.PreparationPhase;
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
    private void handleTrain() {
        //Handles when no target is selected
        if(this.getParent().getCurrentlySelectedLeftProvince()==null) {
            showSummary("Please select a origin region");
            return;
        }
        //Handles when no unit is selected
        if(selectedUnits.size()==0) {
            showSummary("Please select unit to train");
            return;
        }
        List<String> train = new ArrayList<>();
        for(Unit u : selectedUnits) {
            train.add(u.getClassName());
        }
        String msg = this.getParent().regionConTrainRequest(train, leftProvinceLabel.getText());
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
        if(this.getParent().getCurPhase() instanceof  PreparationPhase) {
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
            units = new ArrayList<>();
            units.addAll(leftUnits.values());
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
                        if(this.getParent().getCurPhase() instanceof  PreparationPhase) {
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
                if(this.getParent().getCurPhase() instanceof PreparationPhase) {
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
        interactionButton.setOnAction(event -> handleTrain() );

    }

    public void reset() {
        leftScrollVbox.getChildren().clear();
        rightScrollVbox.getChildren().clear();
        leftProvinceLabel.setText("Select Region");
        rightProvinceLabel.setText("Select Region");
        selectedUnits.clear();
        if(this.getParent().getCurPhase() instanceof MovePhase) {
            setMoveButton();
        } else {
            setTrainButton();
        }
    }

}
