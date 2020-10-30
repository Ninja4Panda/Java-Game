package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class UnitTest{
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
//        Unit u = new Unit();
//        assertEquals(u.getNumTroops(), 50);
    }

    @Test
    public void loadDefaultFileTest() {
        try {
            Game game = new Game("src/test/resources/default.json");
            assertEquals(game.getCurPhase().toString(), "Preparation");
            assertEquals(game.getGameTurn().getTurn(), 0);
            assertEquals(game.getGameTurn().getSubTurn(), 1);
//            assertEquals(game.getMovePhase().toString(), );
            game.save();
        } catch(JSONException e) {
            System.out.println("JSON Error");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

