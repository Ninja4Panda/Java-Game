package unsw.gloriaromanus.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.Observer;
import unsw.gloriaromanus.phase.*;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;
import unsw.gloriaromanus.winCond.Check;
import unsw.gloriaromanus.winCond.ConquestCond;
import unsw.gloriaromanus.winCond.TreasuryCond;
import unsw.gloriaromanus.winCond.WealthCond;
import unsw.gloriaromanus.winCond.WinCond;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Game implements Observer {
    private GamePhase preparationPhase;
    private GamePhase movePhase;
    private GamePhase curPhase;
    private GameTurn gameTurn;
    private Player curPlayer;
    private List<Player> playerList;
    private Check campaignWinCond;

    public Game (List<String> factions) throws IOException {
        //Attach the subject
        BattleResolver resolver = BattleResolver.getINSTANCE();
        resolver.attach(this);

        //Set up the fields
        preparationPhase = new PreparationPhase(this);
        movePhase = new MovePhase(this);
        playerList = new ArrayList<>();
        curPhase = preparationPhase;
        gameTurn = new GameTurn(0,0, factions.size()-1);

        //Read the ownership
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
        JSONObject ownership = new JSONObject(content);

        for(String faction: factions) {
            Map<String, Region> regionMap = new HashMap<>();
            JSONArray regions = ownership.getJSONArray(faction);
            for(int i = 0; i<regions.length(); i++) {
                String regionName = regions.getString(i);
                Random rand = new Random();
                //Create new region
                int wealth = rand.nextInt(500)+500;
                Region region = new Region(regionName, gameTurn, wealth, 10);
                regionMap.put(regionName, region);
            }

            //Create new players
            Player player = new Player(regionMap, faction, gameTurn);
            if(curPlayer == null) curPlayer = player;
            playerList.add(player);
        }

        //Make new win condition
        WinCond conquest = new ConquestCond();
        WinCond treasury = new TreasuryCond();
        WinCond wealth = new WealthCond();

        List<WinCond> campaignVictory = new ArrayList<>();
        campaignVictory.add(conquest);
        campaignVictory.add(treasury);
        campaignVictory.add(wealth);
        campaignWinCond = new Check(campaignVictory);
    }

    public Game(String configFile) throws IOException, JSONException {
        //Attach the subject
        BattleResolver resolver = BattleResolver.getINSTANCE();
        resolver.attach(this);

        //Set up the fields
        preparationPhase = new PreparationPhase(this);
        movePhase = new MovePhase(this);
        playerList = new ArrayList<>();

        //Read state of game from config file
        String content = Files.readString(Paths.get(configFile));
        JSONObject config = new JSONObject(content);
        JSONObject game = config.getJSONObject("Game");

        //Load the CampaignWinCond
        JSONObject loadWinCond = game.getJSONObject("CampaignWinCond");
        campaignWinCond = new Check(loadWinCond.optString("Goal"), loadWinCond.optString("Junction"), loadWinCond.optJSONObject("SubCheck"));

        //Set up current phase
        String state = game.getString("Phase");
        if (preparationPhase.toString().equals(state)) {
            curPhase = preparationPhase;
        } else if(movePhase.toString().equals(state)) {
            curPhase = movePhase;
        } else {
            throw new JSONException("Corrupted config: Invalid State");
        }

        //Set up the players & turn
        JSONArray players = config.getJSONArray("Players");
        int turn = game.getInt("Turn");
        int subTurn = game.getInt("Subturn");
        gameTurn = new GameTurn(turn, subTurn, players.length());

        curPlayer = null;
        for(int i = 0; i<players.length(); i++) {
            JSONObject playerJson = players.getJSONObject(i);
            //Create new players
            Player player = new Player(playerJson, gameTurn);
            //Set current player
            if(subTurn == i) curPlayer = player;

            //Map each faction name to the player Object
            playerList.add(player);
        }
        if(curPlayer == null) throw new JSONException("Corrupted config: Invalid Subturn");
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
     * @return current phase
     */
    public GamePhase getCurPhase() {
        return curPhase;
    }

    /**
     * @return current player
     */
    public Player getCurPlayer() {
        return curPlayer;
    }

    /**
     * @return current player faction
     */
    public String getCurFaction() {
        return curPlayer.getFaction().toString();
    }

    /**
     * @return hashmap of players
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * @return game turn object
     */
    public GameTurn getGameTurn() {
        return gameTurn;
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
        Iterator<Player> it = playerList.iterator();
        Player firstPlayer = playerList.get(0);

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
    public String endPhase() {
        //Checks if player won/lost at the end of a phase
        String status = checkPlayerStatus();
        if(status!=null) {
            return status;
        }

        //EndPhase
        String msg = curPhase.endPhase();
        return msg;
    }

    /**
     * Checks the if current player conquered all region or lost
     */
    private String checkPlayerStatus() {
        if(curPlayer.getAllRegions().size()==0) {
            removePlayer();
            return "You lost the game! You have no regions left!";
        } else if(campaignWinCond.getGoal()!=null && campaignWinCond.player(getCurPlayer())) {
            //Advance the turn
            curPhase.endPhase();

            //Auto save
            try {
                //Make the saves directory if it doesn't exists
                File dir = new File(".","saves");
                dir.mkdir();

                String name = "Autosave";
                String json = ".json";
                File file = new File(dir,name+json);
                int i = 1;
                while (file.exists()) {
                    name = "Autosave"+i;
                    file = new File(dir, name+json);
                    i++;
                }
                campaignWinCond.reset();
                save(name);
                return "You won the game! Game is saved as "+name;
            } catch(IOException e) {
                e.printStackTrace();
                return "You won the game! But game cannot be saved!";
            }
        }
        return null;
    }

    /**
     * Removes the current player
     */
    public void removePlayer() {
        Player targetPlayer = curPlayer;
        gameTurn.removePlayer();
        curPhase.endPhase();
        curPhase.endPhase();
        playerList.remove(targetPlayer);
    }

    /**
     * Wrapper function to display region info
     * @param region region to view
     * @return list to display or null to indicate not player's region
     */
    public List<Unit> displayRegion(String region) {
        Region target = curPlayer.getRegion(region);
        if(target != null) return curPhase.getRegionData(target);
        return null;
    }

    /**
     * Wrapper function for troops training
     * @param originRegion origin region initiated the training
     * @param troops list of troops to train
     * @pre origin region is owned by current player
     * @return msg to display
     */
    public String train(String originRegion, List<String> troops) {
        return curPhase.train(originRegion,troops);
    }

    /**
     * Wrapper function for player movement.
     * Note that this function expects both origin region & target region to be current player's region.
     * @param originRegion origin region initiated the movement
     * @param troops list of troops moving
     * @param targetRegion target region to move to
     * @pre origin region is owned by current player
     * @return msg to display
     * @throws IOException throws IOException when adjacency matrix is not present
     */
    public String move(String originRegion, List<String> troops, String targetRegion) throws IOException {
        return curPhase.move(originRegion,troops,targetRegion);
    }

    /**
     * Wrapper function for player invade.
     * @param originRegion origin region initiated the invade
     * @param troops list of troops invading
     * @param targetRegion target region to invade
     * @pre origin region is owned by current player
     * @return msg to display
     * @throws IOException throws IOException when adjacency matrix is not present
     */
    public String invade(String originRegion, List<String> troops, String targetRegion) throws IOException {
        return curPhase.invade(originRegion,troops,targetRegion);
    }

    /**
     * Create a game save in saves directory
     * @param name filename
     * @throws IOException throws IOException when file couldn't be created
     */
    public void save(String name) throws IOException {
        //Make the saves directory if it doesn't exists
        File dir = new File(".","saves");
        dir.mkdir();

        //Get the date
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Date today = new Date();

        //Make the new save file
        String filename = name+".json";
        File file = new File(dir, filename);
        try (FileWriter writer = new FileWriter(file)) {
            //Construct the game json object
            JSONObject gameSave = new JSONObject();
            gameSave.put("SaveTime", df.format(today));
            gameSave.put("Phase", curPhase.toString());
            gameSave.put("Turn", gameTurn.getTurn());
            gameSave.put("Subturn", gameTurn.getSubTurn());
            gameSave.put("CampaignWinCond", campaignWinCond.getSave());

            //Construct the players json array
            JSONArray playerSave = new JSONArray();
            for (Player player : playerList) {
                playerSave.put(player.getSave());
            }

            //Construct the whole json object
            JSONObject save = new JSONObject();
            save.put("Game", gameSave);
            save.put("Players", playerSave);

            writer.write(save.toString(2));
        }
    }

    @Override
    public void update() {
        changeOwnership();
    }

    /**
     * Change the ownership of a region
     */
    private void changeOwnership() {
        BattleResolver resolver = BattleResolver.getINSTANCE();
        Region defeated = resolver.getDefender();
        for(Player player: playerList) {
            if(player.removeRegion(defeated)) break;
        }
        defeated.resetTrainer();
        curPlayer.addRegion(defeated);
    }

    /**
     * Find the player from the region
     * @param region target region
     * @return the player that owns the region
     */
    public Player findPlayer(String region) {
        for(Player player: playerList) {
            if(player.getRegion(region)!=null) return player;
        }
        return null;
    }

    public Check getCampaignWinCond() {
        return campaignWinCond;
    }

    public Region getRegion(String region) {
        for(Player p : playerList ) {
            if(p.getRegion(region) != null) {
                return p.getRegion(region);
            }
        }
        return null;
    }
}
