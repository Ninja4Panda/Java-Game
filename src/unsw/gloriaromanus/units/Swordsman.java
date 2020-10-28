package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Melee;

public class Swordsman extends BaseUnit {
    public Swordsman() {
        super(10, 1, 50, new Melee(5), 5, 10, 10, 1, 0, 0);
        // Swordsman is a unit with 
        // 10 movementpoints
        // 1 turn build time
        // 50 gold cost
        // 5 attack value and armour
        // 10 health and maxhealth
        // 1 range
        // 0 defense skill and shieldDefense
    }

    
}
