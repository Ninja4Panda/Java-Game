package unsw.gloriaromanus.States;

import org.json.JSONObject;
import unsw.gloriaromanus.GameSystem;

public class PreparationPhase implements GameState {
    private GameSystem game;

    public PreparationPhase(GameSystem game) {
        this.game = game;
    }

    @Override
    public JSONObject getDisplayData() {

    }
}
