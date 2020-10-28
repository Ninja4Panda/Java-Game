package unsw.gloriaromanus;

import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.States.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Game {
    private Player curPlayer;
    private GameState preparationPhase;
    private GameState attackPhase;
    private GameState curState;
    private GameTurn turn;
    private Map<String, Player> playersMap; //Key:Faction Name, Value:Player object

    public Game(String configFile) throws IOException {
        //Set up the fields
        preparationPhase = new PreparationPhase(this);
        attackPhase = new AttackPhase(this);
        curState = preparationPhase;
        playersMap = new LinkedHashMap<>();

        //Read state of game from config file
        String content = Files.readString(Paths.get(configFile));
        JSONArray players = new JSONArray(content);

        for(int i = 0; i<players.length(); i++) {
            JSONObject playerJson = players.getJSONObject(i);
            String faction = playerJson.getString("Faction");

            //Create new players and set first player as current player
            Player player = new Player(playerJson);
            if(i==0) curPlayer = player;

            //Map each faction name to the player Object
            playersMap.put(faction, player);
        }
        turn = new GameTurn(players.length());
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
     * @return current player
     */
    public Player getCurPlayer() {
        return curPlayer;
    }

    /**
     * @return Hashmap of players
     */
    public Map<String, Player> getPlayersMap() {
        return playersMap;
    }

    /**
     * Set the current state of the game to another state
     * @param nextState next state to be set to
     */
    public void setCurState(GameState nextState) {
        this.curState = nextState;
    }

    /**
     * Move on to next player and advance to next game turn.
     * See GameTurn class for more details
     */
    public void nextPlayerTurn() {
        Iterator<Player> it = playersMap.values().iterator();
        Player firstPlayer = (Player)playersMap.values().toArray()[0];
        while (it.hasNext()) {
            Player player = it.next();
            //Set current player to next player
            if(player.equals(curPlayer)) {
                curPlayer = it.hasNext()? it.next():firstPlayer;
                break;
            }
        }

        turn.nextTurn();
    }

    /**
     * Wrapper function for front-end to call client request to end a phase
     */
    public void endPhase() {
        curState.endPhase();
    }

    /**
     * Wrapper function for preforming an action.
     * See GameState for more details.
     * @return true/false indicating action was successful or not
     */
    public Boolean action(String originRegion, Map<String, Integer> troops, String ... args) {
        return curState.action(originRegion, troops, args);
    }

}
