package unsw.gloriaromanus.Phase;

import unsw.gloriaromanus.Game.Game;
import unsw.gloriaromanus.Game.Player;
import unsw.gloriaromanus.region.Region;

import java.util.List;

public class PreparationPhase implements GamePhase {
    private Game game;

    public PreparationPhase(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Preparation";
    }

    @Override
    public String endPhase() {
        game.setCurPhase(game.getMovePhase());
        return "Moving Phase begins";
    }

    /**
     * Wrapper function for troops training
     * @param originRegion origin region initiated the training
     * @param troops list of troops to train
     * @return msg to display
     */
    public String train(String originRegion, List<String> troops) {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        return curPlayer.train(origin, troops);
    }

}
