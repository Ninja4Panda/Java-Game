package unsw.gloriaromanus.units.attack;

import unsw.gloriaromanus.units.Unit;

public class Melee implements AttackType {
    private int attackValue;
    private int range;
    public Melee(int attackValue, int range) {
        this.attackValue = attackValue;
        this.range = range;
    }

    public void attackUnit(Unit victim) {
        // do some calcs with victim
        
    }
}
