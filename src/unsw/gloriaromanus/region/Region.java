package unsw.gloriaromanus.region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import unsw.gloriaromanus.game.BattleResolver;
import unsw.gloriaromanus.game.GameTurn;
import unsw.gloriaromanus.Observer;
import unsw.gloriaromanus.units.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Region implements Observer {
    private String name;
    private RegionTrainer trainer;
    private List<Unit> units;
    private int wealth;
    private int tax;
    private List<String> recentlyTrained;

    public Region(String name, GameTurn gameTurn, int wealth, int tax) {
        this.name = name;
        gameTurn.attach(this);
        trainer = new RegionTrainer(this);
        this.wealth = wealth;
        this.tax = tax;
        recentlyTrained = new ArrayList<>();
        this.units = new ArrayList<>();
        this.units.add(new Archerman());
        this.units.add(new Cavalry());
        this.units.add(new Slingerman());
        this.units.add(new Spearman());
        this.units.add(new Swordsman());
    }

    //Testing
    public Region(String name, GameTurn gameTurn,  List<Unit> units, int wealth, int tax) {
        this(name, gameTurn, wealth, tax);
        for( Unit u : units) {
            findUnit(u.getClassName()).addUnits(u.getCurAmount());
            findUnit(u.getClassName()).setCurMovementPoints(u.getCurMovementPoints());
        }
    }

    public Region(JSONObject regionData, GameTurn gameTurn) throws JSONException {
        gameTurn.attach(this);
        name = regionData.getString("Id");
        wealth = regionData.getInt("Wealth");
        tax = regionData.getInt("Tax");
        JSONArray trainedList = regionData.getJSONArray("RecentlyTrained");
        recentlyTrained = new ArrayList<>();
        for(int i = 0; i<trainedList.length(); i++) {
            recentlyTrained.add(trainedList.getString(i));
        }

        //Set up the units according to config
        units = new ArrayList<>();
        JSONObject troops = regionData.getJSONObject("Troops");

        JSONObject archerman = troops.getJSONObject("Archerman");
        int movementPoint = archerman.getInt("Movement");
        int amount = archerman.getInt("Amount");
        units.add(new Archerman(movementPoint, amount));

        JSONObject cavalry = troops.getJSONObject("Cavalry");
        movementPoint = cavalry.getInt("Movement");
        amount = cavalry.getInt("Amount");
        units.add(new Cavalry(movementPoint, amount));

        JSONObject slingerman = troops.getJSONObject("Slingerman");
        movementPoint = slingerman.getInt("Movement");
        amount = slingerman.getInt("Amount");
        units.add(new Slingerman(movementPoint, amount));

        JSONObject spearman = troops.getJSONObject("Spearman");
        movementPoint = spearman.getInt("Movement");
        amount = spearman.getInt("Amount");
        units.add(new Spearman(movementPoint, amount));

        JSONObject swordsman = troops.getJSONObject("Swordsman");
        movementPoint = swordsman.getInt("Movement");
        amount = swordsman.getInt("Amount");
        units.add(new Swordsman(movementPoint, amount));

        //Set up region trainer
        JSONArray trainData = regionData.getJSONArray("Trainer");
        trainer = new RegionTrainer(trainData,this);
    }

    public List<String> getRecentlyTrained() {
        return recentlyTrained;
    }

    public String getName() {
        return name;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getTax() {
        return tax;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public int getWealth() {
        return wealth;
    }

    /**
     * @return Amount of gold that the owner of this region will gain
     */
    public int calcGold() {
        return (int) (wealth* (double) tax/100);
    }

    /**
     * @return all units object in the region
     */
    public List<Unit> getUnits() {
        return units;
    }

    /**
     * Gets the total amount of troops in the region
     * @return total amount of troops in the region
     */
    public int getTotalUnits() {
        int total = 0;
        for(Unit unit : units) {
            total += unit.getCurAmount();
        }
        return total;
    }

    /**
     * Adds the number of trained unit to the unit object in this region
     * @param type type of unit that needs to be added
     */
    public void addUnits(String type) {
        for(Unit unit: units) {
            if(unit.getClassName().equals(type)) unit.addTrainedUnit();
        }
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
     * Moves troops to end region
     * @param unit unit to be moved
     * @param end target region
     */
    public void moveTroops(Unit unit, Region end) {
        Unit target = end.findUnit(unit.getClassName());
        // System.out.println(end.getName()+" "+target.getClassName()+" amt:"+target.getCurAmount()+" MP:"+target.getCurMovementPoints());
        // System.out.println(this.getName()+" "+unit.getClassName()+" amt:"+unit.getCurAmount()+" MP:"+unit.getCurMovementPoints());
        // System.out.println("==========================================");

        //Set the MP of the troop
        if(target.getCurMovementPoints() > unit.getCurMovementPoints()) {
            // System.out.println("sss");
            target.setCurMovementPoints(unit.getCurMovementPoints());
        }
        int amt = unit.getCurAmount();
        target.addUnits(amt);
        unit.minusUnits(amt);
        // System.out.println(end.getName()+" "+target.getClassName()+" "+target.getCurAmount()+" "+target.getCurMovementPoints());
        // System.out.println(this.getName()+" "+unit.getClassName()+" "+unit.getCurAmount()+" "+unit.getCurMovementPoints());
    }

    /**
     * moves troops to other regions
     * @param path path taken to the region
     * @param troops to move to other region
     * @param target target region to move to
     * @return msg to display
     */
    public String move(List<Region> path, List<String> troops, Region target) {
        System.out.println("========= inside move ============");
        //Loop through the path and remove
        String msg = "";
        System.out.println(troops);
        for(String u : troops) {
            Unit unit = findUnit(u);
            for(Region region : path) {
                if(unit.canReduceMovespeed(4)) {
                    unit.reduceMovementPoints(4);
                } else {
                    moveTroops(unit, region);
                    if(Objects.equals(region, path.get(0))) {
                        msg += unit.getClassName() + " Does not have enough Movespeed\n" ;
                    }  else {
                        msg += unit.getClassName()+" was moved from "+this.getName()+" to "+region.getName()+"\n";
                    }
                    break;
                }
                
            }
        }


        System.out.println("========= finish move ============");

        return msg;
    }

    /**
     * Invade a region with troops
     * @param movementPoints cost of moving to other region
     * @param troops to move to other region
     * @param target where the troops will end up at
     * @return msg to display
     */
    public String invade(int movementPoints, List<String> troops, Region target) {
        List<Unit> attackers = new ArrayList<>();
        System.out.println("MP:"+movementPoints);

        //Check if every attackers has enough movement point
        for (String name: troops) {
            Unit unit = findUnit(name);
            if(unit.getCurMovementPoints()<movementPoints) return "Unsuccessful attack not enough movement Point";
            attackers.add(unit);
        }

        //Reduce the movement point
        for (Unit unit:attackers) {
            unit.reduceMovementPoints(movementPoints);
        }

        return BattleResolver.resolve(attackers, target, this);
    }

    /**
     * Finds a unit based on its name
     * @param unit name of unit that needs to be found
     * @return unit
     */
    public Unit findUnit(String unit) {
        for(Unit u : units) {
            if(u.getClassName().compareTo(unit) == 0 ) {
                return u;
            }
        }
        return null;
    }

    /**
     * @return the current state of region
     */
    public JSONObject getSave() {
        JSONObject save = new JSONObject();

        //Troops json object
        JSONObject troops = new JSONObject();
        for (Unit unit: units) {
            JSONObject troop = new JSONObject();
            troop.put("Movement",unit.getCurMovementPoints());
            troop.put("Amount",unit.getCurAmount());
            troops.put(unit.getClassName(), troop);
        }
        JSONArray trainedJson = new JSONArray(recentlyTrained);
        save.put("RecentlyTrained", trainedJson);
        save.put("Wealth", wealth);
        save.put("Tax",tax);
        save.put("Trainer", trainer.getSave());
        save.put("Troops", troops);
        save.put("Id", name);
        return save;
    }

    /**
     * Reset the trainer on ownership change
     */
    public void resetTrainer() {
        trainer.reset();
    }

    @Override
    public void update() {
        updateWealth();
        updateMovementPoints();
        trainer.pushUnits();
    }

    /**
     * Ensures all units in this region has the correct
     * movementPoints at the start of every turn
     */
    private void updateMovementPoints() {
        for(Unit u : units) {
            u.setCurMovementPoints(u.getMaxMovementPoints());
        }
    }

    /**
     * Ensures the region's wealth is properly incremented
     */
    private void updateWealth() {
        switch (tax) {
            case 10:
                wealth += 10;
                break; 
            case 15:
                break;
            case 20:
                wealth -= 10;
                break;
            case 25:
                wealth -= 30;
            default:
                break;
        }
    }

    public Hashtable<String, Integer> getUnitsTraining() {
        return trainer.getTrainingUnits();
    }
}
