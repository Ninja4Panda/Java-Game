package unsw.gloriaromanus.Phase;

import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.UnitCluster;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GamePhase {
    /**
     * Get the appropriate region data
     * @param region region object
     * @return list of units
     */
    default List<UnitCluster> getRegionData(Region region) {
        return region.getUnits();
    }

    /**
     * Ends current phase and move on to the next phase
     */
    void endPhase();

    /**
     * Try to preform an action according to the phase
     * @param originRegion origin region initiated the action
     * @param troops hash map of troops
     * @param args string array expecting targetRegion,targetFaction in order when required
     * @return msg to display
     */
    String action(String originRegion, Map<String, Integer> troops, String ... args) throws IOException;
}
