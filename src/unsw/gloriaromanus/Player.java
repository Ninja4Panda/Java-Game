package unsw.gloriaromanus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private Map<String, Region> regionsMap; //Key:Region Name, Value:Region object
    private Faction faction;
    private int gold;

    public Player(JSONObject playerData) {
        //TODO: set up faction
        String factionName = playerData.getString("Faction");
        gold = playerData.getInt("Gold");
        JSONArray regions = playerData.getJSONArray("Regions");
        regionsMap = new HashMap<>();

        for (int i = 0; i<regions.length(); i++) {
            JSONObject regionJson = regions.getJSONObject(i);
            String name = regionJson.getString("Id");
            Region region = new Region(regionJson);
            regionsMap.put(name, region);
        }
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
     * @param origin origin region object initiated the movement
     * @param troops hash map of troops moving
     * @param target target region object to move to
     * @return true/false indicating movement was successful or not
     */
    public Boolean move(Region origin, Map<String, Integer> troops, Region target) {
        return origin.move(troops, target);
    }

    /**
     * Invade target region from origin with troops
     * @param origin origin region object initiated the invade
     * @param troops hash map of troops invading
     * @param target target region object to invade
     * @return true/false indicating invade was successful or not
     */
    public Boolean invade(Region origin, Map<String, Integer> troops, Region target) {
        return region.invade(troops, target);
    }

    /**
     * Train troops in region
     * @param origin origin region object initiated the training
     * @param troops hashmap of troops to train
     * @return true/false indicating training request was successful or not
     */
    public Boolean train(Region origin, Map<String, Integer> troops) {
        return origin.train(troops);
    }


    /**
     * @return JsonObject according to the save
     */
    public JSONObject getSave() {
        JSONObject save = new JSONObject();
        save.put("Faction", fraction.toString());
        save.put("Gold", gold);

        //Construct the region json array
        JSONArray regionSave = new JSONArray();
        for (Region region: regionsMap.values()) {
            regionSave.put(region.getSave());
        }
        save.put("Regions", regionsMap);

    }
}
