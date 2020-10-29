package unsw.gloriaromanus.Phase;

import org.json.JSONArray;
import unsw.gloriaromanus.Game;
import unsw.gloriaromanus.Player;

import java.util.Map;

public class PreparationPhase implements GamePhase {
    private Game game;

    public PreparationPhase(Game game) {
        this.game = game;
    }

    @Override
    public JSONArray getDisplayData() {
    }

    @Override
    public String toString() {
        return "Preparation";
    }

    @Override
    public void endPhase() {
        game.setCurPhase(game.getMovePhase());
    }

    @Override
    public Boolean action(String originRegion, Map<String, Integer> troops, String ... args) {
        return train(originRegion, troops);
    }

    /**
     * Wrapper function for troops training
     * @param originRegion origin region initiated the training
     * @param troops hashmap of troops to train
     * @return true/false indicating training request was successful or not
     */
    private Boolean train(String originRegion, Map<String, Integer> troops) {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        return curPlayer.train(origin, troops);
    }

}
