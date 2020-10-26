package unsw.gloriaromanus.States;

import org.json.JSONObject;

public interface GameState {
    /**
     * Get the appropriate side-bar data according to the phase
     * @return JSONObject containing the data
     */
    public JSONObject getDisplayData();
}
