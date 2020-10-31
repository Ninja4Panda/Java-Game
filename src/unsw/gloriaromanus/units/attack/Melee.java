package unsw.gloriaromanus.units.attack;


public class Melee implements AttackType {
    private int attackValue;
    private int range;
    public Melee(int attackValue, int range) {
        this.attackValue = attackValue;
        this.range = range;
    }
    
    public int getAttackValue() {
        return attackValue;
    }

    public int getRange() {
        return range;
    }
 
}
