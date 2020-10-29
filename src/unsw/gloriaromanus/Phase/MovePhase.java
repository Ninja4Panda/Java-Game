package unsw.gloriaromanus.Phase;

import org.json.JSONArray;
import unsw.gloriaromanus.Game;
import unsw.gloriaromanus.Player;

import java.util.Map;

public class MovePhase implements GamePhase {
    private Game game;

    public MovePhase(Game game) {
        this.game = game;
    }

    @Override
    public JSONArray getDisplayData() {
    }

    @Override
    public String toString() {
        return "Move";
    }

    @Override
    public void endPhase() {
        //Advance to the next player's turn as this is the last phase of a player's turn
        game.nextPlayerTurn();
        game.setCurPhase(game.getPreparationPhase());
    }

    @Override
    public Boolean action(String originRegion, Map<String, Integer> troops, String ... args) {
        Player curPlayer = game.getCurPlayer();
        //Safe to index as there should be args[0] for both action
        String targetRegion= args[0];

        //Both regions are current player's region
        if(curPlayer.getRegion(originRegion) != null && curPlayer.getRegion(targetRegion) != null)
            return move(originRegion, troops, targetRegion);

        //Safe to index as there should be args[1] for invade
        String targetFaction = args[1];
        return invade(originRegion, troops, targetFaction, targetRegion);
    }

    /**
     * Wrapper function for player movement.
     * Note that this function expects both origin region & target region to be current player's region.
     * @param originRegion origin region initiated the movement
     * @param troops hash map of troops moving
     * @param targetRegion target region to move to
     * @return true/false indicating movement was successful or not
     */
    public Boolean move(String originRegion, Map<String, Integer> troops, String targetRegion) {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        Region target = curPlayer.getRegion(targetRegion);
        return curPlayer.move(origin, troops, target);
    }

    /**
     * Wrapper function for player invade.
     * @param originRegion origin region initiated the invade
     * @param troops hash map of troops invading
     * @param targetRegion target region to invade
     * @param targetFaction target faction to invade
     * @return true/false indicating invade was successful or not
     */
    public Boolean invade(String originRegion, Map<String, Integer> troops, String targetRegion, String targetFaction) {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);

        //Obtain target player object
        Map<String, Player> playersMap = game.getPlayersMap();
        Player targetPlayer = playersMap.get(targetFaction);
        //Obtain target region object
        Region target = targetPlayer.getRegion(targetRegion);

        return curPlayer.invade(origin, troops, target);
    }
}
