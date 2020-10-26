package unsw.gloriaromanus.States;

import org.json.JSONArray;
import unsw.gloriaromanus.Game;

public class PreparationPhase implements GameState {
    private Game game;

    public PreparationPhase(Game game) {
        this.game = game;
    }

    @Override
    public JSONArray getDisplayData() {
    }

    @Override
    public void endPhase() {
        game.setCurState(game.getAttackPhase());
    }
}
