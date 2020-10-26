package unsw.gloriaromanus;

import unsw.gloriaromanus.States.*;

public class GameSystem {
    private List<Player> players;
    private GameState preparationPhase;
    private GameState attackPhase;
    private GameState curState;

    public GameSystem() {
        preparationPhase = new PreparationPhase(this);
        attackPhase = new AttackPhase(this);
    }
}
