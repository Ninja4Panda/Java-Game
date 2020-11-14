package unsw.gloriaromanus.winCond;

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
    
}
