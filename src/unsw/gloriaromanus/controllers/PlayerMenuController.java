package unsw.gloriaromanus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import unsw.gloriaromanus.faction.Faction;
import unsw.gloriaromanus.MenuController;
import unsw.gloriaromanus.game.Player;


public class PlayerMenuController extends MenuController{
    @FXML
    private Label playerFaction;

    @FXML
    private ImageView factionImage;

    @FXML
    private Label playerGold;

    public void updatePlayer(Player player) {
        Faction faction = player.getFaction();
        factionImage.setImage(new Image(faction.getFlagPath()));
        playerFaction.setText(faction.toString());
        playerGold.setText(Integer.toString(player.getGold()));
    }
}
