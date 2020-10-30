package unsw.gloriaromanus.region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.units.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Region {
    private String name;
    private RegionTrainer trainer;
    private List<UnitCluster> units;

    public Region(JSONObject regionData) throws JSONException {
        name = regionData.getString("Id");

        //Set up region trainer
        JSONArray trainData = regionData.getJSONArray("Trainer");
        trainer = new RegionTrainer(trainData, this);

        //Set up the units according to config
        units = new ArrayList<>();
        JSONObject troops = regionData.getJSONObject("Troops");
        units.add( new UnitCluster(troops.getInt("Archerman"), new Archerman()));
        units.add( new UnitCluster(troops.getInt("Cavalry"), new Cavalry()));
        units.add( new UnitCluster(troops.getInt("Slingerman"), new Slingerman()));
        units.add( new UnitCluster(troops.getInt("Spearman"), new Spearman()));
        units.add( new UnitCluster(troops.getInt("Swordsman"), new Swordsman()));
    }

    public List<UnitCluster> getUnits() {
        return units;
    }

    /**
     * Forwards method to RegionTrainer.
     * @param numTroops amount of units put into training.
     * @param unit type of unit trained.
     * @return true if the units were put into training.
     */
    public boolean train(int numTroops, String unit) {
        return trainer.train(numTroops, unit);
    }

    /**
     * Gets the total amount of troops in the region
     * @return total amount of troops in the region
     */
    public int getTotalUnits() {
        int total = 0;
        for(UnitCluster unit : units) {
            total += unit.size();
        }
        return total;
    }


    public Boolean moveTroops(int movementPoints, Map<String,Integer> troops, Region end) {
        minusUnits(troopName, troopAmount);
        end.addUnits(troopName, troopAmount);
    }

    /**
     * Finds a unit based on its name
     * @param unit name of unit that needs to be found
     * @return UnitCluster of that unit
     */
    public UnitCluster findUnit(String unit) {
        for( UnitCluster u : units ) {
            if( u.getUnitName().compareTo(unit) == 0 ) {
                return u;
            }
        }
        return null;
    }

    /**
     * Reduces the number of troops of a chosen UnitCluster, selected by its name
     * @param unit is the unit that is going to have it's number of troops reduced
     * @param numTroops is the amount of troops reduced
     */
    public void minusUnits(String unit, int numTroops) {
        UnitCluster u = findUnit(unit);
        u.minusUnits(numTroops);
    }

    /**
     * Increases the number of troops of a chosen UnitCluster, selected by its name
     * @param unit is the unit that is going to have it's number of troops increased
     * @param numTroops is the amount of troops increased
     */
    public void addUnits(String unit, int numTroops) {
        UnitCluster u = findUnit(unit);
        u.addUnits(numTroops);
    }

    /**
     * @return the current state of region
     */
    public JSONObject getSave() {
        JSONObject save = new JSONObject();
        save.put("Id", name);

        //Troops json object
        JSONObject troops = new JSONObject();
        for (UnitCluster unit: units) {
            troops.put(unit.getUnitName(), unit.size());
        }
        save.put("Trainer", trainer.getSave());
        save.put("Troops", troops);
        return save;
    }
}
