package test;
import unsw.gloriaromanus.units.Unit;
import unsw.gloriaromanus.units.Swordsman;

public class Testing {
    public static void main(String[] args) {
        Unit unitSword = new Unit(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        Swordsman realSword = new Swordsman(1);

        System.out.println(realSword.getClass());
        
    }
}
