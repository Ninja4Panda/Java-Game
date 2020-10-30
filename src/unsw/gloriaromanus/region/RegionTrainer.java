package unsw.gloriaromanus.region;

import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import unsw.gloriaromanus.Observer;
import unsw.gloriaromanus.units.*;

// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region
public class RegionTrainer implements Observer {
    private Hashtable<UnitCluster, Integer> trainingUnits;
    private Region region;
    
    public RegionTrainer(JSONArray trainData, Region region) throws JSONException {
        trainingUnits = new Hashtable<UnitCluster, Integer>();
        this.region = region;

        //Make sure there is only 2 training at a time
        for (int i=0; i<trainData.length() || i>1; i++) {
            String type = trainData.getJSONObject(i).getString("Type");
            int turns = trainData.getJSONObject(i).getInt("Turns");
            int amount = trainData.getJSONObject(i).getInt("Amount");
            UnitCluster troop = getTrainingUnit(amount, type);
            if(troop==null) throw new JSONException("Corrupted config file: invalid unit type");
            trainingUnits.put(troop, turns);
        }
    }

    /**
     * Adds the appropriate unit into the trainingUnits hashtable.
     * @param troops hashmap of units
     * @return true/false indicating if the unit was put into the training Units Hashtable.
     */
    public boolean train(Map<String, Integer> troops) {
        if(troops.size()+trainingUnits.size()>2) return false;
        for(String unit: troops.keySet()) {
            int numTroops = troops.get(unit);
            UnitCluster newUnit = getTrainingUnit(numTroops, unit);
            //Check of the amount currently training unit & valid unit type
            if(newUnit != null) trainingUnits.put(newUnit, newUnit.trainTime());
        }
        return true;
    }

    /**
     * Get the target unit object based on the the unit name.
     * @param numTroops amount of troops
     * @param unit unit name
     * @return unit object
     */
    private UnitCluster getTrainingUnit(int numTroops, String unit) {
        UnitCluster newUnit = null;
        switch (unit) {
            case "Swordsman":
                newUnit = new UnitCluster(numTroops, new Swordsman());
                break;
            case "Archerman":
                newUnit = new UnitCluster(numTroops, new Archerman());
                break;
            case "Cavalry":
                newUnit = new UnitCluster(numTroops, new Cavalry());
                break;
            case "Spearman":
                newUnit = new UnitCluster(numTroops, new Spearman());
                break;
            case "Slingerman":
                newUnit = new UnitCluster(numTroops, new Slingerman());
                break;
            default:
                break;
        }
        return newUnit;
    }

    /**
     * Finds the first unit which is trained.
     * A unit is trained when the value in the key-value pair is equal to 0.
     * @return the first UnitCluster in the hashtable that is trained
     */
    private UnitCluster findTrained() {
        for( UnitCluster u : trainingUnits.keySet() ) {
            if( trainingUnits.get(u) == 0) {
                return u;
            }
        }
        return null;
    }

    /**
     * Checks for units that are trained and pushes all units that are trained.
     * "Pushing"  a unit implies that the region now has an additional numTroops
     * of the unit type trained.
     * This function also removes all units that it pushes from the hashtable.
     * @return int indicating how many units were pushed to the region.
     */
    private int pushUnits() {
        int unitsPushed = 0;
        UnitCluster trainedUnit = findTrained();
        while ( trainedUnit != null) {
            region.addUnits( trainedUnit.getUnitName(), trainedUnit.size() );
            trainingUnits.remove(trainedUnit);
            trainedUnit = findTrained();
            unitsPushed ++;
        }
        return unitsPushed;
    }

    public JSONArray getSave() {

    }

    @Override
    public void update() {

    }
}
