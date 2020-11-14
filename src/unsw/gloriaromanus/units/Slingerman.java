package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Range;

import java.io.InputStream;

public class Slingerman extends Unit {
    public Slingerman() {
        this(15,0);
    }

    public Slingerman(int curMovementPoint, int curAmount) {
        super(curMovementPoint, 15, 3, 85, 7, curAmount, new Range(5, 4), 5, 5, 5, 5, 5);
        // Slingerman is a unit with 
        // 15 movementpoints
        // 15 max movementpoints
        // 3 turn build time
        // 85 gold cost
        // 7 train amount
        // 0 initial amount
        // 5 attack value and armour
        // 5 health and maxhealth
        // 4 range
        // 5 defense skill and shieldDefense
    }

    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png");
    }
}
