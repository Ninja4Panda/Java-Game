package unsw.gloriaromanus.winCond;

import unsw.gloriaromanus.Game.Player;

public class TreasuryCond implements WinCond {

    @Override
    public boolean playerWin(Player player) {
        return player.getGold() >= 100000;
    }
    
}
