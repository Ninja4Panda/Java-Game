package unsw.gloriaromanus.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.Faction.Faction;
import unsw.gloriaromanus.Observer;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player implements Observer {
    private Map<String, Region> regionsMap; //Key:Region Name, Value:Region object
    private List<Region> recentlyConquered;
    private Faction faction;
    private int gold;

    public Player(Map<String, Region> regionsMap, String faction, GameTurn gameTurn) {
        gameTurn.attach(this);

        this.regionsMap = regionsMap;
        recentlyConquered = new ArrayList<>();
        this.faction = Faction.find(faction);
        this.gold = 1000;
    }

    public Player(JSONObject playerData, GameTurn gameTurn) throws JSONException {
        gameTurn.attach(this);
        recentlyConquered = new ArrayList<>();
        JSONArray regions = playerData.getJSONArray("Regions");
        String factionName = playerData.getString("Faction");
        faction = Faction.find(factionName);
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
     * @return list of recently conquered region
     */
    public List<Region> getRecentlyConquered() {
        return recentlyConquered;
    }

    /**
     * @return the faction of the player
     */
    public Faction getFaction() {
        return faction;
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
     * Removes a region from the player
     * @param defeated region that needs to be removed
     * @return true/false to indicate if the region has been removed
     */
    public Boolean removeRegion(Region defeated) {
        return regionsMap.remove(defeated.getName())!=null;
    }

    /**
     * Add a region to the player
     * @param defeated region that needs to be added
     */
    public void addRegion(Region defeated) {
        regionsMap.put(defeated.getName(), defeated);
        recentlyConquered.add(defeated);
    }

    /**
     * Move troops from origin to target
     * @param movementPoints movement points needed to get to target
     * @param origin origin region object initiated the movement
     * @param troops list of troops moving
     * @param target target region object to move to
     * @return msg to display
     */
    public String move(List<Region> movementPoints, Region origin, List<String> troops, Region target) {
        return origin.move(movementPoints, troops, target);
    }

    /**
     * Invade target region from origin with troops
     * @param path the shortest path lists
     * @param origin origin region object initiated the invade
     * @param troops list of troops invading
     * @param target target region object to invade
     * @return msg to display
     */
    public String invade(List<String> path, Region origin, List<String> troops, Region target) {
        int attackFrom = path.size()-2;
        for(Region region: recentlyConquered) {
            if(region.getName().equals(path.get(attackFrom))) {
                return "Cannot attack from region that was recently conquered";
            }
        }
        int movementPoints = path.size()-4;
        return origin.invade(movementPoints, troops, target);
    }

    /**
     * Train troops in region
     * @param origin origin region object initiated the training
     * @param troops list of troops to train
     * @return msg to display
     */
    public String train(Region origin, List<String> troops) {
        int cost = getCost(origin, troops);
        if(cost>gold) return "Not enough gold!";
        String msg = origin.train(troops);
        if("Success".equals(msg)) gold -= cost;
        return msg;
    }

    /**
     * Get the total cost of train units
     * @param origin origin region
     * @param troops list of troops to train
     * @return the cost of it
     */
    private int getCost(Region origin, List<String> troops) {
        int cost = 0;
        for(String name: troops) {
            Unit troop = origin.findUnit(name);
             cost += troop.getCost();
        }
        return cost;
    }

    /**
     * @return the current state of player
     */
    public JSONObject getSave() {
        JSONObject save = new JSONObject();
        save.put("Faction", faction.toString());
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
        updateGold();
        updateRecent();
    }

    /**
     * Update the recently conquered list
     */
    private void updateRecent() {
        recentlyConquered.clear();
    }

    /**
     * Update the gold amount
     */
    private void updateGold(){
        for(String regionName : regionsMap.keySet()) {
            gold += regionsMap.get(regionName).calcGold();
        }
    }

    public List<Region> getAllRegions() {
        List<Region> playerRegions = new ArrayList<Region>();
        for(String regionName : regionsMap.keySet()) {
            playerRegions.add(regionsMap.get(regionName));
        }
        return playerRegions;
    }


}
