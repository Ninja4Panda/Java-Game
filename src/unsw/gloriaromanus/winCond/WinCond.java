package unsw.gloriaromanus.winCond;

import unsw.gloriaromanus.game.Player;

public interface WinCond {
    public boolean playerWin(Player player);

    default public String getName() {
        String className = this.getClass().getName();
        String[] classNameParts = className.split("\\.");
        return classNameParts[classNameParts.length - 1];
    
    }
}
