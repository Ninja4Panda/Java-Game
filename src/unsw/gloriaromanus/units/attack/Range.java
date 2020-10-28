package unsw.gloriaromanus.units.attack;

public class Range implements AttackType{
    private int attackValue;
    private int range;
    public Range(int attackValue, int range) {
        this.attackValue = attackValue;
        this.range = range;
    }
    public int getAttackValue() {
        return attackValue;
    }
}
