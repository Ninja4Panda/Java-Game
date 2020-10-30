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

    public void addUnits() {
        unit.addTrainedUnit();
    }

    public String getUnitName() {
        return unit.getClassName();
    }

    public void minusUnits(int numTroops) {
        this.numTroops -= numTroops;
    }

    public int trainTime() {
        return unit.trainTime();
    }

    public int getMovementPoints() {
        return unit.getMovementPoints();
    }

    public void reduceMovementPoints(int movementPoints) {
        unit.reduceMovementPoints(movementPoints);
    }
}
