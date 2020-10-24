package unsw.gloriaromanus.units.attack;

import unsw.gloriaromanus.units.Unit;

public class Melee implements AttackType {
    private int attackValue;
    public Melee(int attackValue) {
        this.attackValue = attackValue;
    }

    public void attackUnit(Unit victim) {
        // do some calcs with victim
        
    }
}
