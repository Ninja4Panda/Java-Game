package unsw.gloriaromanus.region;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import unsw.gloriaromanus.units.*;


// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region
public class RegionTrainer {
    private Hashtable<String, Integer> trainingUnits;
    private Region region;
    
    public RegionTrainer(Region region) {
        trainingUnits = new Hashtable<String, Integer>();
        this.region = region;
    }

    public void train(int numTroops, String unit) {
        String newUnit = null;
        switch (unit) {
            case "Swordsman":
                newUnit = "Swordsman";
                break;
            
            default:
                break;
        }
        if( newUnit != null ) { trainingUnits.put(newUnit, numTroops ); }
      
    }

    private void pushUnit(String trained) {
        int numTrained = trainingUnits.get(trained);
        region.addUnits(trained, numTrained);
    }

}
