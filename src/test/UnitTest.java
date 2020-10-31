package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    @Test
    public void loadSaveTest() {
        try {
            Game game = new Game("src/test/resources/default.json");
            game.save();

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/saveAfterChange.json"));
            File dir = new File("saves");
            dir.mkdir();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy(HH:mm:ss)");
            Date today = new Date();
            String filename = df.format(today)+".json";
            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));

            //test if the output save is the same as config input
            Arrays.equals(f1,f2);
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void saveAfterChange() {
//        try {
//            Game game = new Game("src/test/resources/default.json");
//
//            game.save();
//
//            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/saveAfterChange.json"));
//            File dir = new File("saves");
//            dir.mkdir();
//            DateFormat df = new SimpleDateFormat("dd-MM-yyyy(HH:mm:ss)");
//            Date today = new Date();
//            String filename = df.format(today)+".json";
//            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));
//
//            //test if the output save is the same as config input
//            Arrays.equals(f1,f2);
//        } catch(JSONException e) {
//            e.printStackTrace();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
}

