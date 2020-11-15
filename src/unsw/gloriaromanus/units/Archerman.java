package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Range;

import java.io.InputStream;

public class Archerman extends Unit {
    public Archerman() {
        this(10,0);
    }

    public Archerman(int curMovementPoint, int curAmount) {
        super(curMovementPoint, 10,1, 75, 10, curAmount, new Range(3, 5), 5, 10, 10, 0, 0);
        // ArcherMan is a unit with
        // 10 initial movement points
        // 10 max movement points
        // 1 turn build time
        // 75 gold cost
        // 10 train amount
        // 0 initial amount
        // 3 attack value and 5 armour
        // 10 health and maxhealth
        // 5 range
        // 0 defense skill and shieldDefense
    }

    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/ArcherMan/Archer_Man_NB.png");
    }
}
