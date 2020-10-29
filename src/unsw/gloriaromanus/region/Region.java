package unsw.gloriaromanus.region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.units.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Region {
    private String name;
    private RegionTrainer trainer;
    private List<UnitCluster> units;

    public Region(JSONObject regionData) throws JSONException {
        name = regionData.getString("Id");
        trainer = new RegionTrainer(this);

        //Set up the units according to config
        units = new ArrayList<>();
        JSONObject troops = regionData.getJSONObject("Troops");
        units.add( new UnitCluster(troops.getInt("Archerman"), new Archerman()));
        units.add( new UnitCluster(troops.getInt("Cavalry"), new Cavalry()));
        units.add( new UnitCluster(troops.getInt("Slingerman"), new Slingerman()));
        units.add( new UnitCluster(troops.getInt("Spearman"), new Spearman()));
        units.add( new UnitCluster(troops.getInt("Swordsman"), new Swordsman()));
    }

    public List<UnitCluster> getUnits() {
        return units;
    }

    public int getTotalUnits() {
        int total = 0;
        for(UnitCluster unit : units) {
            total += unit.size();
        }
        return total;
    }

    public Boolean moveTroops(int movementPoints, Map<String,Integer> troops, Region end) {
        for () {
            UnitCluster unit = findUnit()
        }

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
