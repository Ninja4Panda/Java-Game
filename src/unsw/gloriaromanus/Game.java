package unsw.gloriaromanus;

import unsw.gloriaromanus.States.*;

public class Game {
    private List<Player> players;
    private GameState preparationPhase;
    private GameState attackPhase;
    private GameState curState;
    private GameTurn turn;

    public Game(int numPlayers) {
        preparationPhase = new PreparationPhase(this);
        attackPhase = new AttackPhase(this);
        curState = preparationPhase;
        turn = new GameTurn(numPlayers);
    }

    /**
     * @return the AttackPhase object
     */
    public GameState getAttackPhase() {
        return attackPhase;
    }

    /**
     * @return the PreparationPhase object
     */
    public GameState getPreparationPhase() {
        return preparationPhase;
    }

    /**
     * Set the current state of the game to another state
     * @param nextState next state to be set to
     */
    public void setCurState(GameState nextState) {
        this.curState = nextState;
    }

    /**
     * Wrapper function for front-end to call client request to end a phase
     */
    public void endPhase() {
        curState.endPhase();
    }

    /**
     * Advance to next turn see GameTurn class for more details
     */
    public void advanceTurn() {
        turn.nextTurn();
    }

    public List<Player> getPlayers() {
        return players;
    }
}
