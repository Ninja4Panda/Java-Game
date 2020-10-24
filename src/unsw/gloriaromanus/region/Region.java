package unsw.gloriaromanus.region;

import unsw.gloriaromanus.units.BaseUnit;
import unsw.gloriaromanus.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class Region {
    // private List<infrastructure> infrastructures; uncomment when made
    private List<Unit> units;
    // private Road road; use this to determine troop movement, uncomment when made, for now will be replaced with int
    private int movementCost;
    private String name;

    public Region(int movementCost, String name) {
        this.movementCost = movementCost;
        this.name = name;
        this.units = new ArrayList<Unit>(); // for some reason List<Unit>() doesnt work
    }


 
    
    public int getTotalUnits() {
        int total = 0;
        for(Unit unit : units) {
            total += unit.getNumTroops();
        }
        return total;
    }

    // Should only be called by Player class and assume player class already checked if unit has enough movement points
    public void moving(String troopName, String start, String end){
        units.get(0).move(start, end);
    }

}
