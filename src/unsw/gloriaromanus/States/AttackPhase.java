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
    }

    @Override
    public void endPhase() {
        game.setCurState(game.getAttackPhase());
        //Advance to the next player's turn as this is the last phase of a player's turn
        game.nextPlayerTurn();
    }
}
