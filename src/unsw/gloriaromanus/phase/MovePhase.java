package unsw.gloriaromanus.phase;

import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.game.Player;
import unsw.gloriaromanus.region.Region;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovePhase implements GamePhase {
    private Game game;

    public MovePhase(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Move Phase";
    }

    @Override
    public String endPhase() {
        //Advance to the next player's turn as this is the last phase of a player's turn
        game.nextPlayerTurn();
        game.setCurPhase(game.getPreparationPhase());

        String trainedMsg = "";
        for (Region region: game.getCurPlayer().getAllRegions()) {
            for(String unit: region.getRecentlyTrained()) {
                trainedMsg += unit+" in "+region.getName()+" is ready to join the battle\n";
            }
            region.getRecentlyTrained().clear();
        }
        return game.getCurFaction()+"'s turn\n\n"+"Unit Trained: \n"+trainedMsg;
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
    public String move(String originRegion, List<String> troops, String targetRegion) throws IOException {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        if(origin==null) return "Cannot move from non-friendly region";
        Region target = curPlayer.getRegion(targetRegion);

        //Convert string to regions
        List<String> path = findShortestPath(originRegion, targetRegion);
        List<Region> regions = new ArrayList<>();
        if(path==null) return "No path found\n";
        for(String name: path) {
            Region subRegion = game.getCurPlayer().getRegion(name);
            regions.add(subRegion);
        }
        return curPlayer.move(regions, origin, troops, target);
    }

    /**
     * Wrapper function for player invade.
     * @param originRegion origin region initiated the invade
     * @param troops list of troops invading
     * @param targetRegion target region to invade
     * @return msg to display
     * @throws IOException
     */
    public String invade(String originRegion, List<String> troops, String targetRegion) throws IOException {
        Player curPlayer = game.getCurPlayer();
        Region origin = curPlayer.getRegion(originRegion);
        if(origin==null) return "Cannot initiate attack from non-friendly region";

        //Obtain target player object
        Player targetPlayer = game.findPlayer(targetRegion);
        //Obtain target region object
        Region target = targetPlayer.getRegion(targetRegion);

        List<String> path = findShortestPath(originRegion, targetRegion);
        return curPlayer.invade(path, origin, troops, target);
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
                if (!neighbour.getId().equals(target) && player.getRegion(neighbour.getId())==null) continue;
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

    /**
     * Takes a List of Dinodes and attempts to see if they form a path
     * from the origin to the target
     * @param Dipath List of Dinodes
     * @param origin start of the desired path
     * @param target end of the desired path
     * @return the shortest path between origin and target that can be
     *         created by the Dipath
     */
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

    /**
     * Finds a Dinode in a List of Dinodes
     * @param diList List of Dinodes
     * @param target Dinode wanted
     * @return Dinode wanted
     */
    private Dinode findDinode(List<Dinode> diList, String target) {
        for(Dinode node : diList) {
            if( node.getId().compareTo(target) == 0 ) {
                return node;
            }
        }
        return null;
    }
}
