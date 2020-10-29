package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Range;

public class Archerman extends BaseUnit {
    public Archerman() {
        super(10, 1, 75, new Range(3, 5), 5, 10, 10, 0, 0);
        // ArcherMan is a unit with 
        // 10 movementpoints
        // 1 turn build time
        // 75 gold cost
        // 3 attack value and 5 armour
        // 10 health and maxhealth
        // 5 range
        // 0 defense skill and shieldDefense
    }
}
