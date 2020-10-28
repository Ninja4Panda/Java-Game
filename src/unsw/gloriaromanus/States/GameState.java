package unsw.gloriaromanus.States;

import org.json.JSONArray;

import java.util.Map;

public interface GameState {
    /**
     * Get the appropriate side-bar data according to the phase
     * @return JSONObject containing the data
     */
    JSONArray getDisplayData();

    /**
     * Ends current phase and move on to the next phase
     */
    void endPhase();

    /**
     * Try to preform an action according to the phase
     * @param originRegion origin region initiated the action
     * @param troops hash map of troops
     * @param args string array expecting targetRegion,targetFaction in order when required
     * @return true/false indicating action was successful or not
     */
    Boolean action(String originRegion, Map<String, Integer> troops, String ... args);
}
