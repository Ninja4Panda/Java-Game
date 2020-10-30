package unsw.gloriaromanus.units;

public interface Unit {
    public void move(String start, String end);
    public int trainTime();
    public String getClassName();
    public int getMovementPoints();
    public void reduceMovementPoints(int movementPoints);
    public void setMovementSpeed(int movementPoints);
    public int strength();
}
