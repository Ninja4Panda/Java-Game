package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

//    @Test
//    public void loadSaveTest() {
//        try {
//            Game game = new Game("src/test/resources/default.json");
//            game.save();
//
//            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/default.json"));
//            File dir = new File("saves");
//            dir.mkdir();
//            DateFormat df = new SimpleDateFormat("dd-MM-yyyy(HH:mm:ss)");
//            Date today = new Date();
//            String filename = df.format(today)+".json";
//            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));
//
//            //test if the output save is the same as config input
//            assertArrayEquals(f1, f2);
//        } catch(JSONException e) {
//            e.printStackTrace();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void saveAfterTrainTest() {
//        try {
//            Game game = new Game("src/test/resources/default.json");
//            //Player1 training Archerman & Spearman
//            ArrayList<String> troops = new ArrayList<>();
//            troops.add("Archerman");
//            troops.add("Spearman");
//            game.action("Cyprus", troops);
//            game.endPhase();
//            game.endPhase();
//
//            //Player2 training cavalry & swordsman
//            troops = new ArrayList<>();
//            troops.add("Cavalry");
//            troops.add("Swordsman");
//            game.action("Syria", troops);
//            game.endPhase();
//            game.endPhase();
//
//            //Player1 preparation turn
//            game.save();
//
//            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/trainExpected.json"));
//            File dir = new File("saves");
//            dir.mkdir();
//            DateFormat df = new SimpleDateFormat("dd-MM-yyyy(HH:mm:ss)");
//            Date today = new Date();
//            String filename = df.format(today)+".json";
//            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));
//
//            //test if the output save is the same as config input
//            assertArrayEquals(f1, f2);
//        } catch(JSONException e) {
//            e.printStackTrace();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void saveAfterMoveTest() {
        try {
            Game game = new Game("src/test/resources/moveTest.json");
            ArrayList<String> troops = new ArrayList<>();
            troops.add("Spearman");
            game.action("Cyprus", troops, "Lusitania");
            game.save();

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/moveExpected.json"));
            File dir = new File("saves");
            dir.mkdir();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy(HH:mm:ss)");
            Date today = new Date();
            String filename = df.format(today)+".json";
            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));
            assertArrayEquals(f1, f2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveAfterInvadeTest() {

    }

}

