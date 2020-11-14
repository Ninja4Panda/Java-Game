package unsw.gloriaromanus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.Game.Player;


public class PlayerMenuController extends MenuController{
    @FXML
    private Label playerFaction;

    @FXML
    private Label playerGold;

    public void updatePlayer(Player player) {
        playerFaction.setText(player.getFaction().toString());
        playerGold.setText(Integer.toString(player.getGold()));
    }
}
