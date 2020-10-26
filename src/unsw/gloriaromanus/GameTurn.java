package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;

public class GameTurn implements Subject {
    private int turn;
    private int subTurns;
    private int numPlayers;
    private List<Observer> listObs;

    public GameTurn(int numPlayers) {
        turn = 0;
        subTurns = 0;
        this.numPlayers = numPlayers;
        listObs = new ArrayList<>();
    }

    /**
     *
     */
    public void nextTurn() {
        subTurns += 1;
        //One full turn has passed
        if(subTurns == numPlayers) {
            subTurns = 0;
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
