package unsw.gloriaromanus;

import java.util.List;
import java.util.Random;

import javafx.scene.layout.Region;
import unsw.gloriaromanus.units.UnitCluster;

public class BattleResolver {

    // Pass implementation of battle resolver
    public String resolve(List<UnitCluster> attackers, List<UnitCluster> defenders) {
        int attackingStrength = 0;
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
                return "Attackers win";
            } else if ( decider.nextDouble() <= defendingWin ) {
                return "Defenders win";
            }

        } 

        return "Draw";
    }


}
