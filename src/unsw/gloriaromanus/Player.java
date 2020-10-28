package unsw.gloriaromanus;

import org.json.JSONArray;
import org.json.JSONObject;

public class Player {

    public Player(JSONObject playerData) {
        JSONArray regions = playerData.getJSONArray("Regions");
    }

    public void remove() {

    }
}
