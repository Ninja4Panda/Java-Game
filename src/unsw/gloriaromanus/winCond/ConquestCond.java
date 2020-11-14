package unsw.gloriaromanus.winCond;


import unsw.gloriaromanus.game.Player;

public class ConquestCond implements WinCond{

    @Override
    public boolean playerWin(Player player) {
        if (player.getAllRegions().size() == 58) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Conquest";
    }

}
