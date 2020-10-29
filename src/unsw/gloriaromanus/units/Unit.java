package unsw.gloriaromanus.units;

public interface Unit {
    public void move(String start, String end);
    public int trainTime();
    public String getClassName();
}
