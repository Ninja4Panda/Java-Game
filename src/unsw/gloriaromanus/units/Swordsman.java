package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Melee;

import java.io.InputStream;

public class Swordsman extends Unit {
    public Swordsman() {
        this(10,0);
    }

    public Swordsman(int curMovementPoint, int curAmount) {
        super(curMovementPoint, 10, 1, 50, 10, curAmount, new Melee(5, 1), 5, 10, 10, 0, 0);
        // Swordsman is a unit with 
        // 10 movement points
        // 10 max movement points
        // 1 turn build time
        // 50 gold cost
        // 10 train amount
        // 0 initial amount
        // 5 attack value and armour
        // 10 health and maxhealth
        // 1 range
        // 0 defense skill and shieldDefense
    }

    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png");
    }
}
