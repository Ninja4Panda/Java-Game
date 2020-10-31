package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import unsw.gloriaromanus.BattleResolver;
import unsw.gloriaromanus.Game;
import unsw.gloriaromanus.Phase.MovePhase;
import unsw.gloriaromanus.units.Spearman;
import unsw.gloriaromanus.units.Swordsman;
import unsw.gloriaromanus.units.Unit;

public class Testing {
    public static void main(String[] args) {
        try {
            Game game = new Game("src/test/resources/moveTest.json");
            MovePhase test = new MovePhase(game);
            List<String> path = test.findShortestPath("Lusitania", "Lusitania");
            for(String p : path) {
                System.out.println(p);
            }
           
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
