package unsw.gloriaromanus.Phase;

import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.units.Unit;

import java.io.IOException;
import java.util.List;

public interface GamePhase {
    /**
     * Get the appropriate region data
     *
     * @param region region object
     * @return list of units
     */
    default List<Unit> getRegionData(Region region) {
        return region.getUnits();
    }

    /**
     * Ends current phase and move on to the next phase
     */
    void endPhase();

    /**
     * Wrapper function for troops training
     * @param originRegion origin region initiated the training
     * @param troops list of troops to train
     * @return msg to display
     */
    default String train(String originRegion, List<String> troops) {
        //Shouldn't get to this point
        return "Invalid operation";
    }

    /**
     * Wrapper function for player movement.
     * Note that this function expects both origin region & target region to be current player's region.
     * @param originRegion origin region initiated the movement
     * @param troops       list of troops moving
     * @param targetRegion target region to move to
     * @return msg to display
     * @throws IOException
     */
    default String move(String originRegion, List<String> troops, String targetRegion) throws IOException {
        //Shouldn't get to this point
        return "Invalid operation";
    }

    /**
     * Wrapper function for player invade.
     * @param originRegion origin region initiated the invade
     * @param troops list of troops invading
     * @param targetRegion target region to invade
     * @return msg to display
     * @throws IOException
     */
    default String invade(String originRegion, List<String> troops, String targetRegion) throws IOException {
        //Shouldn't get to this point
        return "Invalid operation";
    }
}
