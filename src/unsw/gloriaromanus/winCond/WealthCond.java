package unsw.gloriaromanus.winCond;

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
    
}
