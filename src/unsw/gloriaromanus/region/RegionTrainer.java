package unsw.gloriaromanus.region;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.units.*;

// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region
public class RegionTrainer {
    private Hashtable<String, Integer> trainingUnits;
    private Region region;

    public RegionTrainer(JSONArray trainData, Region region) throws JSONException {
        trainingUnits = new Hashtable<>();
        this.region = region;

        //Make sure there is only 2 training at a time
        for (int i=0; i<trainData.length() || i>1; i++) {
            JSONObject unitObject = trainData.getJSONObject(i);
            String type = unitObject.getString("Type");
            int turns = unitObject.getInt("TurnLeft");
            Unit troop = region.findUnit(type);
            if(troop==null) throw new JSONException("Corrupted config file: invalid unit type");
            trainingUnits.put(type, turns);
        }
    }

    public RegionTrainer(Region region) {
        trainingUnits = new Hashtable<String, Integer>();
        this.region = region;
    }

    /**
     * Adds the appropriate unit into the trainingUnits hashtable.
     * @param troops list of units
     * @return msg to display
     */
    public String train(List<String> troops) {
        if(troops.size()+trainingUnits.size()>2) return "Unsuccessful training too many troops are training already";
        for(String unit: troops) {
            if(trainingUnits.get(unit)!=null) return unit +" is already training!";
            Unit newUnit = region.findUnit(unit);
            //Check of the amount currently training unit & valid unit type
            if(newUnit != null) trainingUnits.put(unit, newUnit.getTrainTime());
        }
        return "Success";
    }

    /**
     * Checks for units that are trained and pushes all units that are trained.
     * "Pushing"  a unit implies that the region now has an additional numTroops
     * of the unit type trained.
     * This function also removes all units that it pushes from the hashtable.
     * @return int indicating how many units were pushed to the region.
     */
    public int pushUnits() {
        int unitsPushed = 0;
        Iterator <Map.Entry<String,Integer>> it = trainingUnits.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String,Integer> entry = it.next();
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() == 0) {
                region.addUnits(entry.getKey());
                it.remove();
            }
        }
        return unitsPushed;
    }

    /**
     * @return the current state of region trainer
     */
    public JSONArray getSave() {
        JSONArray save = new JSONArray();
        for (Map.Entry<String, Integer> entry : trainingUnits.entrySet()) {
            JSONObject unitJson = new JSONObject();
            unitJson.put("Type", entry.getKey());
            unitJson.put("TurnLeft", entry.getValue());
            save.put(unitJson);
        }
        return save;
    }

}
