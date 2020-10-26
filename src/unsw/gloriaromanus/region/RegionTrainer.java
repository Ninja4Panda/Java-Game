package unsw.gloriaromanus.region;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import unsw.gloriaromanus.units.*;


// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region
public class RegionTrainer {
    private Hashtable<Unit, Integer> trainingUnits;
    
    public RegionTrainer() {
        trainingUnits = new Hashtable<Unit, Integer>();
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

        if(newUnit == null ) {

        } else {
            trainingUnits.put( newUnit, newUnit.trainTime() );
        }

    }

}
