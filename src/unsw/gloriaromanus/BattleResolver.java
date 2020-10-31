package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

public class BattleResolver implements Subject {
    private static final BattleResolver INSTANCE = new BattleResolver();
    private List<Observer> listObs = new ArrayList<>();

    public static BattleResolver getINSTANCE() {
        return INSTANCE;
    }

    // For Testing
    public static double getAttackingWin(List<Unit> attackers, Region defending, Region attacking){
        int attackingStrength = 0;
        List<Unit> defenders = defending.getUnits();
        for(Unit u : attackers) {
            attackingStrength += u.strength();
        }

        int defendingStrength = 0;
        for(Unit u : defenders ) {
            defendingStrength += u.strength();
        }

        // will always generate a number that is less than 1
        double attackingWin = (double)attackingStrength/(attackingStrength + defendingStrength);
        return attackingWin;

    }

    public static double getDefendingWin(List<Unit> attackers, Region defending, Region attacking){
        int attackingStrength = 0;
        List<Unit> defenders = defending.getUnits();
        for(Unit u : attackers) {
            attackingStrength += u.strength();
        }

        int defendingStrength = 0;
        for(Unit u : defenders ) {
            defendingStrength += u.strength();
        }

        // will always generate a number that is less than 1
        double defendingWin = (double )defendingStrength/(attackingStrength + defendingStrength);
        return defendingWin;
    }

    // Pass implementation of battle resolver
    public static String resolve(List<Unit> attackers, Region defending, Region attacking) {
        int attackingStrength = 0;
        List<Unit> defenders = defending.getUnits();
        for(Unit u : attackers) {
            attackingStrength += u.strength();
        }

        int defendingStrength = 0;
        for(Unit u : defenders ) {
            defendingStrength += u.strength();
        }

        // will always generate a number that is less than 1
        double attackingWin = (double)attackingStrength/(attackingStrength + defendingStrength);
        double defendingWin = (double)defendingStrength/(attackingStrength + defendingStrength);
        if(defendingWin == 0) {
            attacking.moveTroops(  4, unitArrayToString(attackers),   defending) ;
            BattleResolver resolver = getINSTANCE();
            resolver.notifyObservers();
            return "Attackers win";
        }

        System.out.println(attackingWin);
        System.out.println(defendingWin);

        // keep fighting with the armies until someone wins if not draw
        Random decider = new Random();
        for( int numCombats = 0; numCombats < 200; numCombats ++) {
            // System.out.println(attackingWin + " " + defendingWin + " ADS = " + ( attackingStrength + defendingStrength));
            if( decider.nextDouble() <= attackingWin ) {

                int randomExtraUnitsLoss = decider.nextInt(100  - (int) attackingWin * 100);
                double losersLoss = attackingWin + (double) randomExtraUnitsLoss/100;
                AfterMath(defending.getUnits(), losersLoss);

                double winnersLoss = decider.nextDouble();
                AfterMath(attackers, winnersLoss);

                attacking.moveTroops(  4, unitArrayToString(attackers),   defending) ;

                //Notify game to change ownership
                BattleResolver resolver = getINSTANCE();
                resolver.notifyObservers();
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

    public static List<String> unitArrayToString(List<Unit> units) {
        List<String> stringArray = new ArrayList<>();
        for(Unit u : units) {
            stringArray.add(u.getClassName());
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

    public static void AfterMath(List<Unit> units, double lossPercentage) {
        int totalUnits = 0;
        for(Unit u : units) {
            totalUnits += u.getCurAmount();
        }
        
        // get how many units need to be killed off
        double exactUnitsLost = (double) totalUnits *lossPercentage;
        int unitsLost = (int) Math.ceil(exactUnitsLost);

        Random russianRoulette = new Random();

        // kill off a random unit one by one until the target is reached
        int unitsRemoved = 0;
        while(unitsRemoved < unitsLost) {
            Unit unitGone = units.get(russianRoulette.nextInt( units.size() - 1 ));
            if(unitGone.getCurAmount() != 0 ) {
                unitGone.minusUnits(1);
                unitsRemoved ++;
            }
        }
    }

    @Override
    public void attach(Observer obs) {
        listObs.add(obs);
    }

    @Override
    public void detach(Observer obs) {
        listObs.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for(Observer obs: listObs) {
            obs.update();
        }
    }
}
