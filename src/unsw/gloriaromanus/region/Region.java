package unsw.gloriaromanus.region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.GameTurn;
import unsw.gloriaromanus.units.*;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private String name;
    private GameTurn gameTurn;
    private RegionTrainer trainer;
    private List<UnitCluster> units;

    public Region(JSONObject regionData, GameTurn gameTurn) throws JSONException {
        name = regionData.getString("Id");
        this.gameTurn = gameTurn;
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

    /**
     * @return all units object in the region
     */
    public List<UnitCluster> getUnits() {
        return units;
    }

    /**
     * Forwards method to RegionTrainer.
     * @param troops list of units
     * @return true if the units were put into training.
     */
    public String train(List<String> troops) {
        return trainer.train(troops);
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

    /**
     * Adds a new object of UnitsCluster to the units list
     * @param type type of unit that needs to be added
     */
    public void addUnits(String type) {
        for(UnitCluster unit: units) {
            if(unit.getUnitName().equals(type)) unit.addUnits();
        }
    }

    /**
     * moves troops to other regions
     * @param movementPoints cost of moving to other region
     * @param troops to move to other region
     * @param end where the troops will end up at
     * @return if the move is succesful
     */
    public String moveTroops(int movementPoints, List<String> troops, Region end) {
        // minusUnits(troopName, troopAmount);
        // end.addUnits(troopName, troopAmount);
        for( UnitCluster u : units ) {
            if( troops.contains(u.getUnitName()) ) {
                units.remove(u);
            }
        }
        return "yes";
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

        //Troops json object
        JSONObject troops = new JSONObject();
        for (UnitCluster unit: units) {
            troops.put(unit.getUnitName(), unit.size());
        }

        //TODO:Wealth?
        save.put("Trainer", trainer.getSave());
        save.put("Troops", troops);
        save.put("Id", name);
        return save;
    }

    public String invade(int movementPoints, List<String> troops, Region target) {
        return "Nothing";
    }
}
