package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;

public class GameTurn implements Subject {
    private int turn;
    private int subTurn;
    private int numPlayers;
    private List<Observer> listObs;

    public GameTurn(int turn, int subTurn, int numPlayers) {
        this.turn = turn;
        this.subTurn = subTurn;
        this.numPlayers = numPlayers;
        listObs = new ArrayList<>();
    }

    /**
     * @return current subturn
     */
    public int getSubTurn() {
        return subTurn;
    }

    /**
     * @return current turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Advance to the turn if all players has moved already
     */
    public void nextTurn() {
        subTurn += 1;
        //One full turn has passed
        if(subTurn == numPlayers) {
            subTurn = 0;
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
