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
    // private List<infrastructure> infrastructures; uncomment when made
    private List<UnitCluster> units;
    // private Road road; use this to determine troop movement, uncomment when made, for now will be replaced with int
    private int movementCost;
    private RegionTrainer trainer;
    private String name;

    public Region(int movementCost, String name) {
        this.movementCost = movementCost;
        this.name = name;
        this.trainer = new RegionTrainer(this);
        this.units = new ArrayList<UnitCluster>(); 
        this.units.add( new UnitCluster( 0, new Swordsman() ) );
        this.units.add( new UnitCluster(0, new Archerman() ) );
        this.units.add( new UnitCluster(0, new Cavalry() ) );
        this.units.add( new UnitCluster(0, new Spearman() ) );
        this.units.add( new UnitCluster(0, new Swordsman() ) );
    }


 
    
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

    public UnitCluster findUnit(String unit) {
        for( UnitCluster u : units ) {
            if( u.getUnitName().compareTo(unit) == 0 ) {
                return u;
            }
        }
        return null;
    }

    public void minusUnits(String unit, int numTroops) {
        UnitCluster u = findUnit(unit);
        u.minusUnits(numTroops);
    }

    public void addUnits(String unit, int numTroops) {
        UnitCluster u = findUnit(unit);
        u.addUnits(numTroops);
    }

}
