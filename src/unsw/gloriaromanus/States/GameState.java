package unsw.gloriaromanus.States;

import org.json.JSONArray;

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
}
