package test;
import unsw.gloriaromanus.units.Unit;

import java.util.ArrayList;
import java.util.List;

import unsw.gloriaromanus.BattleResolver;
import unsw.gloriaromanus.units.Spearman;
import unsw.gloriaromanus.units.Swordsman;

public class Testing {
    public static void main(String[] args) {
        // int curMP = 4;
        // int curAmount = 10;
        // Unit u1 = new Swordsman(curMP,curAmount);
        // Unit u2 = new Swordsman(curMP,curAmount);
        // Unit u3 = new Swordsman(curMP,curAmount);
        // Unit u4 = new Swordsman(curMP,curAmount);

        // List<Unit> attackers = new ArrayList<>();
        // attackers.add(u1);
        // attackers.add(u2);

        // List<Unit> defenders = new ArrayList<>();
        // defenders.add(u3);
        // defenders.add(u4);

        // BattleResolver battle = new BattleResolver();

        Unit spearDudes = new Spearman(12, 12);
        System.out.println(spearDudes.getClassName());


    }
}
