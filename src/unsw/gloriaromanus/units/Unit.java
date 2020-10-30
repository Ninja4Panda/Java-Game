package unsw.gloriaromanus.units;

public interface Unit {
    void move(String start, String end);
    int trainTime();
    void addTrainedUnit();
    void addUnits(int numTroops);
    void minusUnits(int numTroops);
    String getClassName();
    int getMovementPoints();
    public int getMaxMovementSpeed();
    public void setMovementSpeed(int movementPoints);
    void reduceMovementPoints(int movementPoints);
    int strength();
}
