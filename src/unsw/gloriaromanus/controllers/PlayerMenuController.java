package unsw.gloriaromanus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import unsw.gloriaromanus.faction.Faction;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.game.Player;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.winCond.Check;


public class PlayerMenuController extends MenuController{

    @FXML
    private VBox topBox;

    @FXML
    private Label playerFaction;

    @FXML
    private ImageView factionImage;

    @FXML
    private Label treasuryCondProg;

    @FXML
    private Label conquestCondProg;

    @FXML
    private Label wealthCondProg;

    @FXML
    private HBox winCond;

    @FXML
    private Label playerGold;

    @FXML
    private GridPane playerProgGrid;

    @FXML
    private GridPane winConGrid;

    public void updatePlayer(Player player) {
        Faction faction = player.getFaction();
        factionImage.setImage(new Image(faction.getFlagPath()));
        playerFaction.setText(faction.toString());
        playerGold.setText(Integer.toString(player.getGold()));
        treasuryCondProg.setText(player.getGold() + "/" + 100000 );
        conquestCondProg.setText(player.getAllRegions().size() + "/" + 58);
        int totalWealth = 0;
        for( Region region : player.getAllRegions() ) {
            totalWealth += region.getWealth();
        }
        wealthCondProg.setText(totalWealth + "/" + 400000);
    }

    public void initializeWinCond() {
        Check check = this.getParent().getCampaignWinCond();
        while(check.getGoal() != null) {
            Text conjunction = new Text( check.getCheckType().getName());
            conjunction.setFont(Font.font("", FontWeight.BOLD,20));
            ImageView image = new ImageView(new Image(check.getGoal().getImage(),70,70,false,false));
            winCond.getChildren().add(image);
            if(check.getsubCheck().getGoal()!=null) winCond.getChildren().add(conjunction);
            check = check.getsubCheck();
        }
    }

    @FXML
    private void initialize() {
        // double width = topBox.getWidth();
        playerProgGrid.setTranslateX(500);
        winConGrid.setTranslateX(500);
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMaxWidth(200);
        col0.setPrefWidth(150);
        // ColumnConstraints col1 = new ColumnConstraints();
        // col1.setMaxWidth(1000);
        // col1.setPrefWidth(1000);
        winConGrid.getColumnConstraints().add(col0);
        // winConGrid.getColumnConstraints().add(col1);
    }
}
