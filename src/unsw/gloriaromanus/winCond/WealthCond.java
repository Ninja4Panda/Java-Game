package unsw.gloriaromanus.winCond;

import java.io.InputStream;
import java.util.List;

import unsw.gloriaromanus.game.Player;
import unsw.gloriaromanus.region.Region;

public class WealthCond implements WinCond {

    @Override
    public boolean playerWin(Player player) {
        int totalWealth = 0;
        List<Region> playerRegions = player.getAllRegions();
        for(Region r : playerRegions) {
            totalWealth += r.getWealth();
        }
        return totalWealth >= 400000;
    }

    @Override
    public String getName() {
        return "Wealth";
    }

    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Terrain and Buildings/Walls/Wood/Horizontal Wooden Wall_NB.png");
    } 
    
}
