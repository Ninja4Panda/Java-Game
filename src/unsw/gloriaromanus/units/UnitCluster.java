package unsw.gloriaromanus.units;

public class UnitCluster{
    private int numTroops;
    private Unit unit;
    public UnitCluster(int numTroops, Unit unit) {
        this.numTroops = numTroops;
        this.unit = unit;
    }

    public int size() {
        return numTroops;
    }

    public void addUnits(int numTroops) {
        this.numTroops += numTroops;
    }

    public String getUnitName() {
        return unit.getClassName();
    }

    public void minusUnits(int numTroops) {
        this.numTroops -= numTroops;
    }
}
