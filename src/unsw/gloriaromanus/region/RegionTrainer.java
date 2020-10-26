package unsw.gloriaromanus.region;

import java.util.ArrayList;
import java.util.List;

import unsw.gloriaromanus.units.*;


// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region
public class RegionTrainer {
    private List<Unit> trainingUnits;
    
    public RegionTrainer() {
        trainingUnits = new ArrayList<Unit>();
    }

    public void train(int numTroops, String unit) {
        Unit newUnit = null;
        switch (unit) {
            case "Swordsman":
                newUnit = new Swordsman(numTroops);
                break;
            
            default:
                break;
        }

        if(newUnit == null) {

        } else {
            trainingUnits.add(newUnit);
        }

    }

}
