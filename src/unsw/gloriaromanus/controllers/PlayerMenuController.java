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

    /**
     * Updates the top banner with the latest information on the player
     * the @param player is where the information will be taken from
     */
    public void updatePlayer(Player player) {

        Faction faction = player.getFaction();
        factionImage.setImage(new Image(faction.getFlagPath()));
        playerFaction.setText(faction.toString());

        playerGold.setText(Integer.toString(player.getGold()));

        treasuryCondProg.setText(player.getGold() + "/" + 100000 );
        conquestCondProg.setText(player.getAllRegions().size() + "/" + 53);
        int totalWealth = 0;
        for( Region region : player.getAllRegions() ) {
            totalWealth += region.getWealth();
        }
        wealthCondProg.setText(totalWealth + "/" + 400000);
    }

    /**
     * Reads and presents a conjunction/disjunction of the winconditions
     * made by the game.
     */
    public void initializeWinCond() {

        // get the game's win condition
        Check check = this.getParent().getCampaignWinCond();

        // loop through the win condition
        while(check.getGoal() != null) {
            // make a Text with the disjunction/conjunction
            Text conjunction = new Text( check.getCheckType().getName());
            conjunction.setFont(Font.font("", FontWeight.BOLD,20));

            // get an image of the wincondition
            ImageView image = new ImageView(new Image(check.getGoal().getImage(),70,70,false,false));

            // add it to the Hbox that will show the winconditions
            winCond.getChildren().add(image);
            if(check.getsubCheck().getGoal()!=null) winCond.getChildren().add(conjunction);

            // move to next wincond
            check = check.getsubCheck();
        }
    }

    @FXML
    private void initialize() {
        // Moves both grids away from the gold amount
        playerProgGrid.setTranslateX(500);
        winConGrid.setTranslateX(500);

        // make constraints
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMaxWidth(200);
        col0.setPrefWidth(150);
        winConGrid.getColumnConstraints().add(col0);
    }
}
