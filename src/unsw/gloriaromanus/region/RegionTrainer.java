package unsw.gloriaromanus.region;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import unsw.gloriaromanus.Observer;

// train the proper units and put it in the list
// should observe the turn counter and when the appropriate 
// amount of turns passes it will send it into Region
public class RegionTrainer implements Observer {
    private Hashtable<String, Integer> trainingUnits;
    private Region region;
    
    public RegionTrainer(JSONArray trainData, Region region) throws JSONException{
        trainingUnits = new Hashtable<String, Integer>();

        //Make sure there is only 2 training at a time
        for (int i=0; i<trainData.length() || i>1; i++) {
            String type = trainData.getJSONObject(i).getString("Type");
            if(region.findUnit(type)==null) throw new JSONException("Corrupted config file: invalid unit type");
            int amount = trainData.getJSONObject(i).getInt("Amount");

            trainingUnits.put(type, amount);
        }
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

    public JSONArray getSave() {

    }

    @Override
    public void update() {

    }
}
