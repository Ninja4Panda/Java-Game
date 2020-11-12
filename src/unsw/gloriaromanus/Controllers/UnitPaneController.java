package unsw.gloriaromanus.Controllers;

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

    public void configure(Unit unit) {
        unitName.setText(unit.getClassName());
        unitHealth.setText("HP : " + Integer.toString(unit.getHealth()));
        unitAttack.setText("Attack : " + Integer.toString(unit.getAttackValue()));
        unitArmour.setText("Armour : " + Integer.toString(unit.getDefenseSkill()));
        unitMoveSpeed.setText("MS : " + Integer.toString(unit.getCurMovementPoints()));
        unitAmount.setText("Amount : " + Integer.toString(unit.getCurAmount()));

        switch (unit.getClassName()) {
            case "Archerman":
                unitImage.setImage(new Image(getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/ArcherMan/Archer_Man_NB.png")));
                
                break;
            case "Cavalry" :
                unitImage.setImage(new Image(getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry_NB.png")));
                break;
            case "Slingerman" :
                unitImage.setImage( new Image(getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png")));
               
                break;
            case "Spearman" :
                unitImage.setImage( new Image(getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png")));
                
                break;
            case "Swordsman" :
                unitImage.setImage(  new Image(getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png")));

                break;
            default:
                break;
        }
    }

}