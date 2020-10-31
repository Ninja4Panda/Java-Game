package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Melee;

public class Cavalry extends Unit {
    public Cavalry(int curMovementPoint, int curAmount) {
        super(curMovementPoint, 20, 2, 100, 5, curAmount, new Melee(7, 2), 5, 15, 15, 0, 1);
        // Cavalry is a unit with 
        // 20 movementpoints
        // 20 max movementpoints
        // 2 turn build time
        // 100 gold cost
        // 5 train amount
        // 0 initial amount
        // 8 attack value and 5 armour
        // 15 health and maxhealth
        // 3 range
        // 0 defense skill and 1 shieldDefense
    }
}
