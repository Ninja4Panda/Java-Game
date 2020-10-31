package unsw.gloriaromanus.region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import unsw.gloriaromanus.BattleResolver;
import unsw.gloriaromanus.GameTurn;
import unsw.gloriaromanus.Observer;
import unsw.gloriaromanus.units.*;

import java.util.ArrayList;
import java.util.List;

public class Region implements Observer {
    private String name;
    private GameTurn gameTurn;
    private RegionTrainer trainer;
    private List<Unit> units;
    private int wealth;
    private int tax;

    public Region(String name, GameTurn gameTurn,  List<Unit> units, int wealth, int tax) {
        this.name = name;
        this.gameTurn = gameTurn;
        gameTurn.attach(this);
        trainer = new RegionTrainer(this);
        this.wealth = wealth;
        this.tax = tax;
        this.units = new ArrayList<Unit>();
        this.units.add(new Archerman(1, 0));
        this.units.add(new Cavalry(1, 0));
        this.units.add(new Slingerman(1, 0));
        this.units.add(new Spearman(1, 0));
        this.units.add(new Swordsman(1, 0));
        for( Unit u : units) {
            findUnit(u.getClassName()).addUnits(u.getCurAmount());
            findUnit(u.getClassName()).setCurMovementPoints(u.getCurMovementPoints());
        }
    }

    public Region(JSONObject regionData, GameTurn gameTurn) throws JSONException {
        this.gameTurn = gameTurn;
        gameTurn.attach(this);
        name = regionData.getString("Id");
        wealth = regionData.getInt("Wealth");
        tax = regionData.getInt("Tax");

        //Set up region trainer
        JSONArray trainData = regionData.getJSONArray("Trainer");
        trainer = new RegionTrainer(trainData,this);

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
     * Forwards method to RegionTrainer.
     * @param troops list of units
     * @return true if the units were put into training.
     */
    public String train(List<String> troops) {
        return trainer.train(troops);
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
     * moves troops to other regions
     * @param movementPoints cost of moving to other region
     * @param troops to move to other region
     * @param end where the troops will end up at
     * @return msg to display
     */
    public String moveTroops(int movementPoints, List<String> troops, Region end) {
        for(Unit u : units) {
            if(troops.contains(u.getClassName())) {
                Unit compareTo = end.findUnit(u.getClassName());
                if(compareTo.getCurMovementPoints() > u.getCurMovementPoints()) {
                    compareTo.setCurMovementPoints(u.getCurMovementPoints());
                }
                compareTo.addUnits(u.getCurAmount());
                u.minusUnits(u.getCurAmount());
            }
        }
        return "Troops moved";
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
     * Reduces the number of troops of a chosen UnitCluster, selected by its name
     * @param unit is the unit that is going to have it's number of troops reduced
     * @param numTroops is the amount of troops reduced
     */
    public void minusUnits(String unit, int numTroops) {
        Unit u = findUnit(unit);
        u.minusUnits(numTroops);
    }

    /**
     * Increases the number of troops of a chosen UnitCluster, selected by its name
     * @param unit is the unit that is going to have it's number of troops increased
     * @param numTroops is the amount of troops increased
     */
    public void addUnits(String unit, int numTroops) {
        Unit u = findUnit(unit);
        u.addUnits(numTroops);
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

        save.put("Wealth", wealth);
        save.put("Tax",tax);
        save.put("Trainer", trainer.getSave());
        save.put("Troops", troops);
        save.put("Id", name);
        return save;
    }

    public String invade(int movementPoints, List<String> troops, Region target) {
        List<Unit> attackers = new ArrayList<>();
        for (Unit unit: units) {
            if(troops.contains(unit.getClassName())) attackers.add(unit);
        }
        return BattleResolver.resolve(attackers, target, this);
    }

    @Override
    public void update() {
        updateWealth();
        updateMovementPoints();
        trainer.pushUnits();
    }

    private void updateMovementPoints() {
        for(Unit u : units) {
            u.setCurMovementPoints(u.getMaxMovementPoints());
        }
    }

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
}
