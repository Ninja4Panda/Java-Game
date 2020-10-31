package unsw.gloriaromanus;

import java.util.HashMap;
import java.util.Map;

public class WinCond {
    public static HashMap<String, String> check( Map<String, Player> players ) {
        HashMap<String, String> playerStatus = new HashMap<String, String>();
        for( String p : players.keySet() ) {
            if(players.get(p).getAllRegions().size() == 0) {
                playerStatus.put(p, "You Lose");
            } else if( players.get(p).getAllRegions().size() == 58) {
                playerStatus.put(p, "You Win");
            }
        }

        return playerStatus;
    }
}
