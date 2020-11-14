package unsw.gloriaromanus.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import unsw.gloriaromanus.Observer;
import unsw.gloriaromanus.Subject;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

public class BattleResolver implements Subject {
    private static final BattleResolver INSTANCE = new BattleResolver();
    private List<Observer> listObs = new ArrayList<>();
    private Region defender;

    public static BattleResolver getINSTANCE() {
        return INSTANCE;
    }

    public Region getDefender() {
        return defender;
    }

    public void setDefender(Region defender) {
        this.defender = defender;
    }


    /**
     * Pass implementation of battle resolver
     * @param attackers List of units that the attacking region will send to attack
     * @param defending Region that is defending against the attack
     * @param attacking Region that sends the units to attack
     * @return the result of the battle
     */
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

        if(defendingStrength == 0) {
            for(Unit unit: attackers) {
                attacking.moveTroops(unit, defending) ;
            }
            BattleResolver resolver = getINSTANCE();
            resolver.setDefender(defending);
            resolver.notifyObservers();
            return "You won! Region has been conquered\n\nNo Lost and all unit was moved to "+defending.getName();
        }

        // will always generate a number that is less than 1
        double attackingWin = (double)attackingStrength/(attackingStrength + defendingStrength);
        double defendingWin = (double)defendingStrength/(attackingStrength + defendingStrength);

        // keep fighting with the armies until someone wins if not draw
        Random decider = new Random();
        if( decider.nextDouble() <= attackingWin ) {
            int randomExtraUnitsLoss = decider.nextInt(100 - (int) (attackingWin * 100));
            double losersLoss = attackingWin + (double) randomExtraUnitsLoss/100;
            AfterMath(defending.getUnits(), losersLoss);

            double winnersLoss = decider.nextDouble();
            String msg = "You won! Region has been conquered\n\nUnit lost:\n";
            msg += AfterMath(attackers, winnersLoss);

            msg+="\nUnit moved:\n";
            for(Unit unit: attackers) {
                msg += unit.getCurAmount()+" "+unit.getClassName()+" was moved to "+defending.getName()+"\n";
                attacking.moveTroops(unit, defending) ;
            }

            //Notify game to change ownership
            BattleResolver resolver = getINSTANCE();
            resolver.setDefender(defending);
            resolver.notifyObservers();
            return msg;
        } else {
            double winnersLoss = decider.nextDouble();
            AfterMath(defending.getUnits(), winnersLoss);

            System.out.println(attackingStrength);
            System.out.println(defendingStrength);
            System.out.println(defendingWin);
            int randomExtraUnitsLoss = decider.nextInt(100- (int) (defendingWin*100));

            double losersLoss = attackingWin + (double) randomExtraUnitsLoss/100;

            String msg = "You lost!\n\nUnit lost:\n";
            msg += AfterMath(attackers, losersLoss);
            return msg;
        }
    }

    /**
     * Responsible for stimulating loss of Units after a battle
     * @param units that participated in the battle
     * @param lossPercentage percentage of Units lost
     * @return string of lost units
     */
    public static String AfterMath(List<Unit> units, double lossPercentage) {
        //Hashmap to store the killed
        HashMap<String,Integer> killMap = new HashMap<>();
        int totalUnits = 0;
        for(Unit u : units) {
            totalUnits += u.getCurAmount();
            killMap.put(u.getClassName(),0);
        }

        // get how many units need to be killed off
        double exactUnitsLost = (double) totalUnits *lossPercentage;
        int unitsLost = (int) Math.ceil(exactUnitsLost);
        Random russianRoulette = new Random();

        // kill off a random unit one by one until the target is reached
        int unitsRemoved = 0;
        while(unitsRemoved < unitsLost) {
            int rand = russianRoulette.nextInt(units.size());
            Unit unitGone = units.get(rand);
            if(unitGone.getCurAmount() != 0) {
                int prevValue = killMap.get(unitGone.getClassName());
                killMap.put(unitGone.getClassName(),prevValue+1);
                unitGone.minusUnits(1);
                unitsRemoved ++;
            }
        }

        String msg = "";
        for(String unit: killMap.keySet()) {
            msg += "Lose "+killMap.get(unit)+" "+unit+" in the battle\n";
        }
        return msg;
    }

    @Override
    public void attach(Observer obs) {
        listObs.add(obs);
    }

    @Override
    public void notifyObservers() {
        for(Observer obs: listObs) {
            obs.update();
        }
    }
}
