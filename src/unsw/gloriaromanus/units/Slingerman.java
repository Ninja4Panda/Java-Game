package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Range;

public class Slingerman extends BaseUnit {
    public Slingerman() {
        super(15, 3, 85, new Range(5, 4), 5, 5, 5, 5, 5);
        // Slingerman is a unit with 
        // 15 movementpoints
        // 3 turn build time
        // 85 gold cost
        // 5 attack value and armour
        // 5 health and maxhealth
        // 4 range
        // 5 defense skill and shieldDefense
    }
}
