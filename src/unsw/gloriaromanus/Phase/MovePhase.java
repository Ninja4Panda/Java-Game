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
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePhase implements GamePhase {
    private Game game;

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
    public String action(String originRegion, List<String> troops, String ... args) throws IOException {
        Player curPlayer = game.getCurPlayer();

        //Safe to index as there should be args[0] for both action
        String targetRegion= args[0];

        //Both regions are current player's region
        if(curPlayer.getRegion(originRegion) != null && curPlayer.getRegion(targetRegion) != null)
            return move(originRegion, troops, targetRegion);

        //Safe to index as there should be args[1] for invade
        String targetFaction = args[1];
        return invade(originRegion, troops, targetRegion, targetFaction);
    }

    /**
     * Wrapper function for player movement.
     * Note that this function expects both origin region & target region to be current player's region.
     * @param originRegion origin region initiated the movement
     * @param troops list of troops moving
     * @param targetRegion target region to move to
     * @return msg to display
     * @throws IOException
     */
    private String move(String originRegion, List<String> troops, String targetRegion) throws IOException {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        Region target = curPlayer.getRegion(targetRegion);

        List<String> path = findShortestPath(originRegion, targetRegion);
        List<Region> regions = new ArrayList<>();
        for(String name: path) {
            for (Player player : game.getPlayersMap().values()) {
                Region subRegion = player.getRegion(name);
                if (subRegion!=null) {
                    regions.add(subRegion);
                    break;
                }
            }
        }
        return curPlayer.move(regions, origin, troops, target);
    }

    /**
     * Wrapper function for player invade.
     * @param originRegion origin region initiated the invade
     * @param troops list of troops invading
     * @param targetRegion target region to invade
     * @param targetFaction target faction to invade
     * @return msg to display
     * @throws IOException
     */
    private String invade(String originRegion, List<String> troops, String targetRegion, String targetFaction) throws IOException {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);

        //Obtain target player object
        Map<String, Player> playersMap = game.getPlayersMap();
        Player targetPlayer = playersMap.get(targetFaction);
        //Obtain target region object
        Region target = targetPlayer.getRegion(targetRegion);

        int movementPoint = findShortestPath(originRegion, targetRegion).size();
        return curPlayer.invade(movementPoint, origin, troops, target);
    }

    /**
     * Find the shortest path between two regions
     * @param origin origin region
     * @param target target region
     * @return shortest path as a string
     * @throws IOException
     */
    private List<String> findShortestPath(String origin, String target) throws IOException {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject allAdjacencyMatrix = new JSONObject(content);
        
        List<Dinode> visited = new ArrayList<Dinode>();
        List<Dinode> opened = new ArrayList<Dinode>();
        Player player = game.getCurPlayer();

        Dinode start = new Dinode(origin, null, 0);
        opened.add(start);

        while(opened.size() > 0) {
            Dinode curRegion = opened.get(0);
            visited.add(curRegion);
            //Get the adjacency matrix from curRegion as a list
            JSONObject adjacencyMatrix = allAdjacencyMatrix.getJSONObject(curRegion.getId());
            JSONArray adjacentList = adjacencyMatrix.names();

            // If the target was found return the shortest path
            if(adjacencyMatrix.getBoolean(target)) {
                Dinode finish = new Dinode(target, curRegion, curRegion.getCost() + 4);
                visited.add(finish);
                // List<String> temp = new ArrayList<String>();
                // for(Dinode d: visited) {
                //     temp.add(d.getId());
                // }
                // return temp;
                return shortestPath(visited, origin, target);
            }


            // loop through neighbours
            for( int i = 0; i < adjacentList.length(); i ++) {
                Dinode neighbour = new Dinode(adjacentList.getString(i), curRegion, curRegion.getCost() + 4);
               if( !adjacencyMatrix.getBoolean(neighbour.getId()) ) {
                   continue;
               }
                // Check if player owns region & is not recently conquered
                if (!neighbour.getId().equals(target) && player.getRegion(neighbour.getId())==null && player.getRecentlyConquered().contains(neighbour)) continue;
                // add to opened if never visited and never opened 
                // or if visited and not in opened then see if we found a shorter way
                if( findDinode(opened, neighbour.getId()) == null && findDinode(visited, neighbour.getId()) == null) {
                    opened.add(neighbour);
                }else if( findDinode(opened, neighbour.getId()) == null && findDinode(visited, neighbour.getId()) != null) {
                    Dinode toCheck = findDinode(visited, neighbour.getId());
                    if(toCheck.getCost() > neighbour.getCost()) {
                        toCheck.setCost(neighbour.getCost());
                        toCheck.setParent(curRegion);
                    }
                }
            }

            opened.remove(curRegion);


        }
     
        return null;
    }

    private List<String> shortestPath(List<Dinode> Dipath, String origin, String target) {
        String currRegion = target;
        List<String> shortestPath = new ArrayList<String>();
        int i = 0;
        while(i < Dipath.size() ) {
            if(currRegion.compareTo(origin) == 0 ){
                shortestPath.add(currRegion);
                Collections.reverse(shortestPath);
                return shortestPath;                
            }
            if(Dipath.get(i).getId().compareTo(currRegion) == 0) {
                shortestPath.add(Dipath.get(i).getId());
                currRegion = Dipath.get(i).getParentID();
                i = 0;
            }
            i++;
        }
        return null;
    }

    private Dinode findDinode(List<Dinode> diList, String target) {
        for(Dinode node : diList) {
            if( node.getId().compareTo(target) == 0 ) {
                return node;
            }
        }
        return null;
    }
}
