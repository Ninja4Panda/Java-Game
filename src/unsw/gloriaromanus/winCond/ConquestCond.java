package unsw.gloriaromanus.winCond;

import java.io.InputStream;

import unsw.gloriaromanus.game.Player;

public class ConquestCond implements WinCond{

    @Override
    public boolean playerWin(Player player) {
        if (player.getAllRegions().size() == 53) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Conquest";
    }
    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Cannon/Cannon_NB.png");
    } 
}
