package unsw.gloriaromanus.units;

public interface Unit {
    void move(String start, String end);
    int trainTime();
    void addTrainedUnit();
    void addUnits(int numTroops);
    void minusUnits(int numTroops);
    String getClassName();
    int getMovementPoints();
    void reduceMovementPoints(int movementPoints);
}
