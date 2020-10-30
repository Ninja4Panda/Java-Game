package unsw.gloriaromanus.units;

public class UnitCluster{
    private int numTroops;
    private Unit unit;
    public UnitCluster(int numTroops, Unit unit) {
        this.numTroops = numTroops;
        this.unit = unit;
    }

    public void setMovementPoints(int movementPoints) {
        unit.setMovementSpeed(movementPoints);
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

    public Unit getUnit() {
        return unit;
    }

    public int armyStrength() {
        return numTroops*unit.strength();
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
