package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;

public class GameTurn implements Subject {
    private int turn;
    private int subTurns;
    private int numPlayers;
    private List<Observer> listObs;

    public GameTurn(int turn, int subTurns, int numPlayers) {
        this.turn = turn;
        this.subTurns = subTurns;
        this.numPlayers = numPlayers;
        listObs = new ArrayList<>();
    }

    /**
     * Advance to the turn if all players has moved already
     */
    public void nextTurn() {
        subTurns += 1;
        //One full turn has passed
        if(subTurns == numPlayers) {
            subTurns = 1;
            turn++;
            notifyObservers();
        }
    }

    @Override
    public void attach(Observer obs) {
        listObs.add(obs);
    }

    @Override
    public void detach(Observer obs) {
        listObs.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for(Observer obs: listObs) {
            obs.update();
        }
    }

}
