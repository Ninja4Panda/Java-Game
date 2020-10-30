package unsw.gloriaromanus.region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.GameTurn;
import unsw.gloriaromanus.units.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        trainer = new RegionTrainer(trainData, this, gameTurn);

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
     * @param troops hashmap of units
     * @return true if the units were put into training.
     */
    public boolean train(Map<String, Integer> troops) {
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
     * @param newUnit that needs to be added
     */
    public void addUnits(UnitCluster newUnit ) {
        units.add(newUnit);
    }

    /**
     * moves troops to other regions
     * @param movementPoints cost of moving to other region
     * @param troops to move to other region
     * @param end where the troops will end up at
     * @return if the move is succesful
     */
    public Boolean moveTroops(int movementPoints, Map<String,Integer> troops, Region end) {

        // loop through all the units that need to be moved and move them
        // it will either remove the troop from the starting region and add 
        // an instance of that same troop but with less movement points to the 
        // new region or reduce the amount in current region and add the desired amount
        // to the new region, once again the moved units will have less movementpoints.
        for( String unit : troops.keySet() ) {
            UnitCluster unitToMove = getLeavingTroops(unit, troops.get(unit) );
            if(unitToMove == null) {
                return false;
            }
            if(unitToMove.getMovementPoints() < movementPoints ) {
                return false;
            }
            unitToMove.reduceMovementPoints(movementPoints);
            end.addUnits(unitToMove);
        }
        return true;
    }

    /**
     * Creates a new unit 
     * Should be only called to make another copy of an existing troop in the region 
     * but with less troopamount
     * @param units type of unit to be made
     * @param troopAmount amount of the troop needed
     * @return UnitCluster of the troop
     */
    private UnitCluster prepareLeavingTroops(String units, int troopAmount) {
        UnitCluster leavingUnits = null;
        switch (units) {
            case "Swordsman":
                leavingUnits = new UnitCluster(troopAmount, new Swordsman());
                break;
            case "Archerman":
                leavingUnits = new UnitCluster(troopAmount, new Archerman());
                break;
            case "Cavalry":
                leavingUnits = new UnitCluster(troopAmount, new Cavalry());
                break;
            case "Spearman":
                leavingUnits = new UnitCluster(troopAmount, new Spearman());
                break;
            case "Slingerman":
                leavingUnits = new UnitCluster(troopAmount, new Slingerman());
                break;
            default:
                break;
        }
        return leavingUnits;
    }

    /**
     *  
     *  it will either remove the troop from the starting region and create 
     *  an instance of that same troop but with less movement points  
     *  or reduce the amount in current region, once again the moved units will have less movementpoints.
     * @param troops
     * @param troopAmount
     * @return
     */
    private UnitCluster getLeavingTroops(String troops, int troopAmount) {
        UnitCluster regionTroops = findUnit(troops);
        if( regionTroops.size() == troopAmount ) {
            units.remove(regionTroops);
            return regionTroops;
        } else if ( regionTroops.size() > troopAmount ) {
            regionTroops.minusUnits(troopAmount);
            UnitCluster leavingTroops = prepareLeavingTroops(troops, troopAmount);
            return leavingTroops;
        }

        return null;
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

        //TODO:Wealth?
        save.put("Trainer", trainer.getSave());
        save.put("Troops", troops);
        return save;
    }
}
