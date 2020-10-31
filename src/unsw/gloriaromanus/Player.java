package unsw.gloriaromanus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player implements Observer{
    private GameTurn gameTurn;
    private Map<String, Region> regionsMap; //Key:Region Name, Value:Region object
    private Region recentlyConquered;
    private String faction;
    private int gold;

    public Player(JSONObject playerData, GameTurn gameTurn) throws JSONException {
        gameTurn.attach(this);
        this.gameTurn = gameTurn;
        JSONArray regions = playerData.getJSONArray("Regions");
        //TODO: set up faction
        String factionName = playerData.getString("Faction");
        faction = factionName;
        gold = playerData.getInt("Gold");
        regionsMap = new HashMap<>();

        for (int i = 0; i<regions.length(); i++) {
            JSONObject regionJson = regions.getJSONObject(i);
            String name = regionJson.getString("Id");
            Region region = new Region(regionJson, gameTurn);
            regionsMap.put(name, region);
        }
    }

    /**
     * @return amount of gold player owns
     */
    public int getGold() {
        return gold;
    }

    /**
     * @param targetRegion region name
     * @return region object based on the targetRegion, null if doesn't exists
     */
    public Region getRegion(String targetRegion) {
        return regionsMap.get(targetRegion);
    }

    /**
     * Move troops from origin to target
     * @param movementPoints movement points needed to get to target
     * @param origin origin region object initiated the movement
     * @param troops list of troops moving
     * @param target target region object to move to
     * @return msg to display
     */
    public String move(int movementPoints, Region origin, List<String> troops, Region target) {
        return origin.moveTroops(movementPoints, troops, target);
    }

    /**
     * Invade target region from origin with troops
     * @param movementPoints movement points needed to get to target
     * @param origin origin region object initiated the invade
     * @param troops list of troops invading
     * @param target target region object to invade
     * @return msg to display
     */
    public String invade(int movementPoints, Region origin, List<String> troops, Region target) {
         return origin.invade(movementPoints, troops, target);
    }

    /**
     * Train troops in region
     * @param origin origin region object initiated the training
     * @param troops list of troops to train
     * @return msg to display
     */
    public String train(Region origin, List<String> troops) {
        if(!enoughGold(origin,troops)) return "not enough gold!";
        return origin.train(troops);
    }

    /**
     * Check if player has enough gold to train units
     * @param origin origin region
     * @param troops list of troops to train
     * @return true/false to indicate enough gold or not
     */
    private Boolean enoughGold(Region origin, List<String> troops) {
        int cost = 0;
        for(String name: troops) {
            Unit troop = origin.findUnit(name);
            // cost += troop.getCost();
        }
        return cost <= gold;
    }

    /**
     * @return the current state of player
     */
    public JSONObject getSave() {
        JSONObject save = new JSONObject();
        save.put("Faction", faction);
        save.put("Gold", gold);

        //Construct the region json array
        JSONArray regionSave = new JSONArray();
        for (Region region: regionsMap.values()) {
            regionSave.put(region.getSave());
        }
        save.put("Regions", regionSave);

        return save;
    }
    
    @Override
    public void update() {
        for(String regionName : regionsMap.keySet()) {
            gold += regionsMap.get(regionName).calcGold();
        }
    }
}
