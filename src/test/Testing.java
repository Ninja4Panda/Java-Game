package test;
import unsw.gloriaromanus.units.Unit;
import unsw.gloriaromanus.units.UnitCluster;

import java.util.ArrayList;
import java.util.List;

import unsw.gloriaromanus.BattleResolver;
import unsw.gloriaromanus.units.Swordsman;

public class Testing {
    public static void main(String[] args) {
        Swordsman s = new Swordsman();
        Swordsman s1 = new Swordsman();
        Swordsman s2 = new Swordsman();
        Swordsman s3 = new Swordsman();

        UnitCluster u1 = new UnitCluster(1, s);
        UnitCluster u2 = new UnitCluster(550, s1);

        UnitCluster u3 = new UnitCluster(100, s3);
        UnitCluster u4 = new UnitCluster(200, s2);

        List<UnitCluster> attackers = new ArrayList<UnitCluster>();
        attackers.add(u1);
        attackers.add(u2);

        List<UnitCluster> defenders = new ArrayList<UnitCluster>();
        defenders.add(u3);
        defenders.add(u4);

        BattleResolver battle = new BattleResolver();

        String result = battle.resolve(attackers, defenders);


        System.out.println(result);
    }
}
