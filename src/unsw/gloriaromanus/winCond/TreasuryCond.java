package unsw.gloriaromanus.winCond;

import java.io.InputStream;

import unsw.gloriaromanus.game.Player;

public class TreasuryCond implements WinCond {

    @Override
    public boolean playerWin(Player player) {
        return player.getGold() >= 100000;
    }

    @Override
    public String getName() {
        return "Treasury";
    }
    
    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Terrain and Buildings/Buildings/Mine/Mine_NB.png");
    } 
    
}
