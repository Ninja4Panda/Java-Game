package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.UnitCluster;

public class BattleResolver {

    // Pass implementation of battle resolver
    public static String resolve(List<UnitCluster> attackers, Region defending, Region attacking) {
        int attackingStrength = 0;
        List<UnitCluster> defenders = defending.getUnits();
        for( UnitCluster u : attackers ) {
            attackingStrength += u.armyStrength();
        }

        int defendingStrength = 0;
        for( UnitCluster u : defenders ) {
            defendingStrength += u.armyStrength();
        }
        

        // will always generate a number that is less than 1
        double attackingWin = (double)attackingStrength/(attackingStrength + defendingStrength);
        double defendingWin = (double )defendingStrength/(attackingStrength + defendingStrength);

        // keep fighting with the armies until someone wins if not draw
        Random decider = new Random();
        for( int numCombats = 0; numCombats < 200; numCombats ++) {
            // System.out.println(attackingWin + " " + defendingWin + " ADS = " + ( attackingStrength + defendingStrength));
            if( decider.nextDouble() <= attackingWin ) {

                int randomExtraUnitsLoss = decider.nextInt(100- (int) attackingWin * 100);
                double losersLoss = attackingWin + (double) randomExtraUnitsLoss/100;
                AfterMath(defending.getUnits(), losersLoss);

                double winnersLoss = decider.nextDouble();
                AfterMath( attackers, winnersLoss);

                attacking.moveTroops(  4, unitArrayToString(attackers),   defending) ;
                
                return "Attackers win";
            } else if ( decider.nextDouble() <= defendingWin ) {
                double winnersLoss = decider.nextDouble();
                AfterMath(defending.getUnits(), winnersLoss);

                int randomExtraUnitsLoss = decider.nextInt(100- (int) defendingWin);
                double losersLoss = attackingWin + (double) randomExtraUnitsLoss/100;
                AfterMath( attackers, losersLoss);
                return "Defenders win";
            }

        } 

        return "Draw";
    }
    public static List<String> unitArrayToString(List<UnitCluster> units) {
        List<String> stringArray = new ArrayList<String>();
        for(UnitCluster u : units ) {
            stringArray.add(u.getUnitName());
        }
        return stringArray;
    }
    // public void DefendersAfterMath(List<UnitCluster> units, double percentage) {
    //     AfterMath(units, percentage);
    // }
    // public void AttackersAfterMath(Region attackers, List<UnitCluster> units, double percentage) {

    //     // Make region only have the units that attacked
    //     List<UnitCluster> attackersUnits = attackers.getUnits();
    //     List<UnitCluster> didntAttack = new ArrayList<UnitCluster>();
    //     for(UnitCluster u : attackersUnits ) {
    //         if(!attackersUnits.contains(u)) {
    //             didntAttack.add(u);
    //             attackers.minusUnits(u.getUnitName(), u.size());
    //         }
    //     }
    //     for(UnitCluster u : attackersUnits ) {
    //         UnitCluster unitInRegion = attackers.findUnit(u.getUnitName());
    //         if(unitInRegion.size() != u.size() ) {
    //             unitInRegion.minusUnits( unitInRegion.size() - u.size() );
    //         }
    //     }

    //     // Remove units that died
    //     AfterMath(attackersUnits, percentage);

    //     // Add back units that didnt attack
    //     for(UnitCluster u : didntAttack ) {
    //         attackers.addUnits(u.getUnitName(), u.size());
    //     }
        
    // }
    public static void AfterMath(List<UnitCluster> units, double lossPercentage) {
        int totalUnits = 0;
        for(UnitCluster u : units) {
            totalUnits += u.size();
        }
        
        // get how many units need to be killed off
        double exactUnitsLost = (double) totalUnits *lossPercentage;
        int unitsLost = (int) Math.ceil(exactUnitsLost);

        Random russianRoulette = new Random();

        // kill off a random unit one by one until the target is reached
        int unitsRemoved = 0;
        while(unitsRemoved < unitsLost) {
            UnitCluster unitGone = units.get( russianRoulette.nextInt( units.size() - 1 ) );
            if(unitGone.size() != 0 ) {
                unitGone.minusUnits(1);
                unitsRemoved ++;
            }

        }
        
    }



}
