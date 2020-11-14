package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.Melee;

import java.io.InputStream;

public class Spearman extends Unit{
    public Spearman() {
        this(10,0);
    }

    public Spearman(int curMovementPoint, int curAmount) {
        super(curMovementPoint, 10, 2, 75, 10, curAmount, new Melee(5, 3), 8, 15, 15, 1, 1);
        // Spearman is a unit with 
        // 10 movementpoints
        // 10 max movementpoints
        // 2 turn build time
        // 75 gold cost
        // 10 train amount
        // 0 initial amount
        // 5 attack value and 8 armour
        // 15 health and maxhealth
        // 3 range
        // 1 defense skill and shieldDefense
    }

    @Override
    public InputStream getImage() {
        return getClass().getResourceAsStream("../scenes/images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png");
    }
}
