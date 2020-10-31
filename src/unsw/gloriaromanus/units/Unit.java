package unsw.gloriaromanus.units;

public interface Unit {
    void move(String start, String end);
    int trainTime();
    void addTrainedUnit();
    void addUnits(int numTroops);
    void minusUnits(int numTroops);
    String getClassName();
    int getCurMovementPoints();
    int getMaxMovementPoints();
    void setCurMovementPoints(int curMovementPoints);
    void reduceMovementPoints(int movementPoints);
    int strength();
    int getCurAmount();
    int getTrainAmount();
}
