package unsw.gloriaromanus.Phase;

import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.Game;
import unsw.gloriaromanus.Player;
import unsw.gloriaromanus.region.Region;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class MovePhase implements GamePhase {
    private Game game;
    private final int MAX_NUM_PATH = 53;

    public MovePhase(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Move";
    }

    @Override
    public void endPhase() {
        //Advance to the next player's turn as this is the last phase of a player's turn
        game.nextPlayerTurn();
        game.setCurPhase(game.getPreparationPhase());
    }

    @Override
    public Boolean action(String originRegion, Map<String, Integer> troops, String ... args) throws IOException {
        Player curPlayer = game.getCurPlayer();
        //Safe to index as there should be args[0] for both action
        String targetRegion= args[0];

        //Both regions are current player's region
        if(curPlayer.getRegion(originRegion) != null && curPlayer.getRegion(targetRegion) != null)
            return move(originRegion, troops, targetRegion);

        //Safe to index as there should be args[1] for invade
        String targetFaction = args[1];
        return invade(originRegion, troops, targetFaction, targetRegion);
    }

    /**
     * Wrapper function for player movement.
     * Note that this function expects both origin region & target region to be current player's region.
     * @param originRegion origin region initiated the movement
     * @param troops hash map of troops moving
     * @param targetRegion target region to move to
     * @return true/false indicating movement was successful or not
     * @throws IOException
     */
    private Boolean move(String originRegion, Map<String, Integer> troops, String targetRegion) throws IOException {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        Region target = curPlayer.getRegion(targetRegion);

        ArrayList<String> visitedNode = new ArrayList<>();
        int movementPoints = findShortestPath(originRegion, targetRegion, visitedNode);
        return movementPoints < MAX_NUM_PATH && curPlayer.move(movementPoints, origin, troops, target);
    }

    /**
     * Wrapper function for player invade.
     * @param originRegion origin region initiated the invade
     * @param troops hash map of troops invading
     * @param targetRegion target region to invade
     * @param targetFaction target faction to invade
     * @return true/false indicating invade was successful or not
     * @throws IOException
     */
    private Boolean invade(String originRegion, Map<String, Integer> troops, String targetRegion, String targetFaction) throws IOException {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);

        //Obtain target player object
        Map<String, Player> playersMap = game.getPlayersMap();
        Player targetPlayer = playersMap.get(targetFaction);
        //Obtain target region object
        Region target = targetPlayer.getRegion(targetRegion);

        ArrayList<String> visitedNode = new ArrayList<>();
        int movementPoints = findShortestPath(originRegion, targetRegion, visitedNode);
        return movementPoints < MAX_NUM_PATH && curPlayer.invade(movementPoints, origin, troops, target);
    }

    /**
     * Find the shortest path between two regions
     * @param origin origin region
     * @param target target region
     * @param visited arraylist of visited nodes
     * @return amount of movement points needed or 1000 if cannot find shortest path
     * @throws IOException
     */
    private int findShortestPath(String origin, String target, ArrayList<String> visited) throws IOException {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject allAdjacencyMatrix = new JSONObject(content);

        //Get the adjacency matrix from origin as a list
        JSONObject adjacencyMatrix = allAdjacencyMatrix.getJSONObject(origin);
        JSONArray adjacentList = adjacencyMatrix.names();
        if(adjacencyMatrix.getBoolean(target)) return 1;

        //Set current node as visited
        visited.add(origin);

        //Loop to find the neighbour
        int shortest = MAX_NUM_PATH;
        for(int i=0; i<adjacentList.length(); i++) {
            String neighbour = adjacentList.getString(i);
            Player player = game.getCurPlayer();

            //Check if player owns neighbour region
            if (!neighbour.equals(target) && player.getRegion(neighbour)==null) continue;

            //Find the shortest path and avoid going back to original node
            if(!visited.contains(neighbour) && adjacencyMatrix.getBoolean(neighbour)) {
                int path = 1+findShortestPath(neighbour, target, visited);
                if(path<shortest) shortest=path;
            }
        }
        return shortest;
    }
}
