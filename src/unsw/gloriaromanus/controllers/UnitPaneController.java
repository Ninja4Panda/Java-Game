package unsw.gloriaromanus.controllers;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import unsw.gloriaromanus.units.Unit;

public class UnitPaneController {
    @FXML
    private Label unitName;

    @FXML
    private ImageView unitImage;

    @FXML
    private Label unitHealth;

    @FXML
    private Label unitAttack;

    @FXML
    private Label unitArmour;

    @FXML
    private Label unitMoveSpeed;

    @FXML
    private Label unitAmount;

    @FXML
    private Pane unitPane;

    private RegionMenuController parent;
    private Unit unit;

    @FXML
    public void unitSelected() {
        parent.selectUnit(unit);
        unitPane.setStyle("-fx-background-color: #0416f9; -fx-border-color:black;-fx-border-width: 5;");
        unitPane.setOnMouseClicked(event -> unselectUnit() );
    }

    @FXML
    public void unselectUnit() {
        parent.deselectUnit(unit);
        unitPane.setStyle("-fx-background-color: #f4f4f4;-fx-border-color:black;-fx-border-width: 5;");
        unitPane.setOnMouseClicked(event -> unitSelected() );
    }

    public RegionMenuController getParent() {
        return parent;
    }

    public void setParent(RegionMenuController parent) {
        this.parent = parent;
    }

    public void showAmountAdded(int amount) {
        if(!Objects.equals(unitName.getText(), "???")) {
            setAmount("Amount : " + unit.getCurAmount() + " + " + amount);
            unitPane.setStyle("-fx-background-color: #0416f9; -fx-border-color:black;-fx-border-width: 5;");
        }
    }

    public void setAmount(String msg) {
        unitAmount.setText(msg);
    }

    public void revertShowAmountAdded(Unit unit) {
        unitAmount.setText("Amount : " + unit.getCurAmount());
        unitPane.setStyle("-fx-background-color: #f4f4f4; -fx-border-color:black;-fx-border-width: 5;");
    }

    public void configure(Unit unit, boolean isRightPanel) {
        this.unit = unit;
        unitName.setText(unit.getClassName());
        unitHealth.setText("HP : " + unit.getHealth());
        unitAttack.setText("Attack : " + unit.getAttackValue());
        unitArmour.setText("Armour : " +unit.getDefenseSkill());
        unitMoveSpeed.setText("MP : " + unit.getCurMovementPoints());
        if(!isRightPanel) {
            unitPane.setOnMouseClicked(event -> unitSelected());
        }
        unitAmount.setText("Amount : " +unit.getCurAmount());
        unitImage.setImage(new Image(unit.getImage()));
       
    }

    public void configureEnemy(Unit unit) {
        unitName.setText("???");
        unitHealth.setText("HP : ???");
        unitAttack.setText("Attack : ???" );
        unitArmour.setText("Armour : ???");
        unitMoveSpeed.setText("MS : ???");
        unitAmount.setText("Amount : ???");
        unitImage.setImage(new Image(unit.getImage()));
    }
}
