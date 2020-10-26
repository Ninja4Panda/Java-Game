package unsw.gloriaromanus.States;

import org.json.JSONArray;
import unsw.gloriaromanus.Game;

public class AttackPhase implements GameState {
    private Game game;

    public AttackPhase (Game game) {
        this.game = game;
    }

    @Override
    public JSONArray getDisplayData() {
        game.getPlayers();
    }

    @Override
    public void endPhase() {
        game.setCurState(game.getAttackPhase());
        //Advance the turn as it is a new turn now
        game.advanceTurn();
    }
}
