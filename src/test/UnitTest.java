package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;
import unsw.gloriaromanus.region.Region;
import unsw.gloriaromanus.region.RegionTrainer;
import unsw.gloriaromanus.units.Archerman;
import unsw.gloriaromanus.units.Spearman;
import unsw.gloriaromanus.units.Swordsman;
import unsw.gloriaromanus.units.Unit;

public class UnitTest{
    @Test
    public void ArcherStatTest(){
        Unit archer = new Archerman(12, 2);
        assertEquals(2,           archer.getCurAmount());
        assertEquals(3,           archer.getAttackValue());
        assertEquals("Archerman", archer.getClassName());
        assertEquals(75,          archer.getCost());
        assertEquals(12,          archer.getCurMovementPoints());
        assertEquals(0,           archer.getDefenseSkill());
        assertEquals(10,          archer.getHealth());
        assertEquals(10,          archer.getMaxHealth());
        assertEquals(10,          archer.getMaxMovementPoints());
        assertEquals(5,           archer.getRange());
        assertEquals(0,           archer.getShieldDefense());
        assertEquals(10,          archer.getTrainAmount());

    }
    
    @Test
    public void RegionMoveTest(){
        List<Unit> SydUnits = new ArrayList<Unit>();

        Unit SydArcher = new Archerman(12, 12);
        Unit SydSwordsMan = new Swordsman(10, 10);
        Unit SydSpearman = new Spearman(9, 123);

        SydUnits.add(SydArcher);
        SydUnits.add(SydSpearman);
        SydUnits.add(SydSwordsMan);

        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);
        assertEquals(123, sydney.findUnit("Spearman").getCurAmount());
        assertEquals(12, sydney.findUnit("Archerman").getCurAmount());
        assertEquals(10, sydney.findUnit("Swordsman").getCurAmount());
        assertEquals(145, sydney.getTotalUnits());

        List<Unit> MelUnits = new ArrayList<Unit>();

        Unit MelArcher = new Archerman(12, 12);
        Unit MelSwordsMan = new Swordsman(10, 10);
        Unit MelSpearman = new Spearman(9, 123);

        MelUnits.add(MelArcher);
        MelUnits.add(MelSpearman);
        MelUnits.add(MelSwordsMan);

        Region melbourne = new Region("Melbourne", new GameTurn(4, 4, 4), MelUnits, 10, 10);
        assertEquals(123, melbourne.findUnit("Spearman").getCurAmount());
        assertEquals(12, melbourne.findUnit("Archerman").getCurAmount());
        assertEquals(10, melbourne.findUnit("Swordsman").getCurAmount());
        assertEquals(145, melbourne.getTotalUnits());

        List<String> moveSpears = new ArrayList<String>();
        String unit = "Spearman";
        moveSpears.add(unit);

        sydney.moveTroops(4, moveSpears, melbourne);
        assertEquals(22, sydney.getTotalUnits());
        assertEquals(268, melbourne.getTotalUnits());
        assertEquals(sydney.findUnit("Spearman").getCurAmount(), 0);
        assertEquals(246, melbourne.findUnit("Spearman").getCurAmount());
    }
    @Test
    public void regionTrainTest(){
        List<Unit> SydUnits = new ArrayList<Unit>();

        Unit SydArcher = new Archerman(12, 12);
        Unit SydSwordsMan = new Swordsman(10, 10);
        Unit SydSpearman = new Spearman(9, 123);

        SydUnits.add(SydArcher);
        SydUnits.add(SydSpearman);
        SydUnits.add(SydSwordsMan);

        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);
        assertEquals(123, sydney.findUnit("Spearman").getCurAmount());
        assertEquals(12, sydney.findUnit("Archerman").getCurAmount());
        assertEquals(10, sydney.findUnit("Swordsman").getCurAmount());
        assertEquals(0, sydney.findUnit("Cavalry").getCurAmount());
        assertEquals(0, sydney.findUnit("Slingerman").getCurAmount());
        assertEquals(145, sydney.getTotalUnits());

        List<String> trainUnits = new ArrayList<String>();
        String sling = "Slinger";
        String cavlry = "Cavalry";
        trainUnits.add(sling);
        trainUnits.add(cavlry);

        assertEquals("Units Training!", sydney.train(trainUnits));
        
        sydney.update();
        sydney.update();
        sydney.update();


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

