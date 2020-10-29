package unsw.gloriaromanus.region;

import unsw.gloriaromanus.units.Archerman;
import unsw.gloriaromanus.units.BaseUnit;
import unsw.gloriaromanus.units.Cavalry;
import unsw.gloriaromanus.units.Spearman;
import unsw.gloriaromanus.units.Swordsman;
import unsw.gloriaromanus.units.Unit;
import unsw.gloriaromanus.units.UnitCluster;

import java.util.ArrayList;
import java.util.List;


public class Region {

    private List<UnitCluster> units;
    private RegionTrainer trainer;
    private String name;

    public Region(String name) {
        this.name = name;
        this.trainer = new RegionTrainer(this);
        this.units = new ArrayList<UnitCluster>(); 
        this.units.add( new UnitCluster( 0, new Swordsman() ) );
        this.units.add( new UnitCluster(0, new Archerman() ) );
        this.units.add( new UnitCluster(0, new Cavalry() ) );
        this.units.add( new UnitCluster(0, new Spearman() ) );
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
    // public void moveTroops(String troopName, int troopAmount, Region end) <- use this
    // Should only be called by Player class and assume player class already checked if unit has enough movement points
    public void moveTroops(String troopName, int troopAmount, Region end) {
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
    

    

}
