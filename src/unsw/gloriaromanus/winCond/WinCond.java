package unsw.gloriaromanus.winCond;

import java.io.InputStream;

import unsw.gloriaromanus.game.Player;

public interface WinCond {
    public boolean playerWin(Player player);

    default public String getName() {
        String className = this.getClass().getName();
        String[] classNameParts = className.split("\\.");
        return classNameParts[classNameParts.length - 1];
    
    }
    public InputStream getImage();
}
