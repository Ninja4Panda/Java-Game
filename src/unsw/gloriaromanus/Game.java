package unsw.gloriaromanus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.Phase.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Game {
    private GamePhase preparationPhase;
    private GamePhase movePhase;
    private GamePhase curPhase;
    private GameTurn gameTurn;
    private Player curPlayer;
    private Map<String, Player> playersMap; //Key:Faction Name, Value:Player object

    public Game(String configFile) throws IOException, JSONException {
        //Set up the fields
        preparationPhase = new PreparationPhase(this);
        movePhase = new MovePhase(this);
        playersMap = new LinkedHashMap<>();

        //Read state of game from config file
        String content = Files.readString(Paths.get(configFile));
        JSONObject config = new JSONObject(content);
        JSONObject game = config.getJSONObject("Game");

        //Set up current phase
        String state = game.getString("Phase");
        if (preparationPhase.toString().equals(state)) {
            curPhase = preparationPhase;
        } else if(movePhase.toString().equals(state)) {
            curPhase = movePhase;
        } else {
            throw new JSONException("Corrupted save at State");
        }

        //Set up the players & turn
        JSONArray players = config.getJSONArray("Players");
        int turn = game.getInt("Turn");
        int subTurn = game.getInt("Subturn");
        gameTurn = new GameTurn(turn, subTurn, players.length());

        curPlayer = null;
        for(int i = 0; i<players.length(); i++) {
            JSONObject playerJson = players.getJSONObject(i);
            String faction = playerJson.getString("Faction");

            //Create new players
            Player player = new Player(playerJson);
            //Set current player
            if(subTurn == i+1) curPlayer = player;

            //Map each faction name to the player Object
            playersMap.put(faction, player);
        }
        if(curPlayer == null) throw new JSONException("Corrupted save at Subturn");
    }

    /**
     * @return the AttackPhase object
     */
    public GamePhase getMovePhase() {
        return movePhase;
    }

    /**
     * @return the PreparationPhase object
     */
    public GamePhase getPreparationPhase() {
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
    public void setCurPhase(GamePhase nextState) {
        this.curPhase = nextState;
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

        gameTurn.nextTurn();
    }

    /**
     * Wrapper function to end a phase
     */
    public void endPhase() {
        curPhase.endPhase();
    }

    /**
     * Wrapper function to display region info
     * @param region region to view
     * @return list to display or null to indicate not player's region
     */
    public List display(String region) {
        Region target = curPlayer.getRegion(region);
        if(target !=null) return curPhase.getDisplayData(target);
        return null;
    }

    /**
     * Wrapper function for preforming an action.
     * See GameState for more details.
     */
    public Boolean action(String originRegion, Map<String, Integer> troops, String ... args) {
        return curPhase.action(originRegion, troops, args);
    }

    /**
     * Create a game save in saves directory
     * @throws IOException
     */
    public void save() throws IOException {
        //Make the saves directory if it doesn't exists
        File dir = new File(".","saves");
        dir.mkdir();

        //Make the new save file today's date
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date today = new Date();
        String filename = df.format(today)+".json";
        File file = new File(dir, filename);
        FileWriter writer = new FileWriter(file);

        //Construct the game json object
        JSONObject gameSave = new JSONObject();
        gameSave.put("Phase", curPhase.toString());
        gameSave.put("Turn", gameTurn.getTurn());
        gameSave.put("Subturn", gameTurn.getSubTurn());

        //Construct the players json array
        JSONArray playerSave = new JSONArray();
        for (Player player: playersMap.values()) {
            playerSave.put(player.getSave());
        }

        //Construct the whole json object
        JSONObject save = new JSONObject();
        save.put("Game",gameSave);
        save.put("Players",playerSave);
        writer.write(save.toString(2));
    }
}
