package unsw.gloriaromanus.region;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import unsw.gloriaromanus.units.*;


// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region

/**
 * Class responsible for training units and adding it to the Region
 */
public class RegionTrainer {
    private Hashtable<UnitCluster, Integer> trainingUnits;
    private Region region;
    
    public RegionTrainer(Region region) {
        trainingUnits = new Hashtable<UnitCluster, Integer>();
        this.region = region;
    }

  
    /**
     * Adds the appropriate unit into the trainingUnits hashtable and sets the
     * amount of turns that unit needs to be trained for.
     * @param numTroops number of units that are being trained.
     * @param unit String that indicates type of unit.
     * @return true/false indicating if the unit was put into the traininngUnits Hashtable.
     */
    public boolean train(int numTroops, String unit) {
        UnitCluster newUnit = null;
        switch (unit) {
            case "Swordsman":
                newUnit = new UnitCluster(numTroops, new Swordsman());
                break;
            case "Archerman":
                newUnit = new UnitCluster(numTroops, new Archerman() );
                break;
            case "Cavalry":
                newUnit = new UnitCluster(numTroops, new Cavalry() );
                break;
            case "Spearman":
                newUnit = new UnitCluster(numTroops, new Spearman() ) ;
                break;
            default:
                break;
        }
        if( newUnit != null ) { 
            trainingUnits.put(newUnit, newUnit.trainTime() ); 
            return true;
        } 
        return false;
      
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

}
