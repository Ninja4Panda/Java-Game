package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        String sling = "Slingerman";
        String cavlry = "Cavalry";
        trainUnits.add(sling);
        trainUnits.add(cavlry);

        assertEquals("Success", sydney.train(trainUnits));

        sydney.update();
        assertEquals(0, sydney.findUnit("Cavalry").getCurAmount());
        assertEquals(0, sydney.findUnit("Slingerman").getCurAmount());

        sydney.update();
        assertEquals(5, sydney.findUnit("Cavalry").getCurAmount());
        assertEquals(0, sydney.findUnit("Slingerman").getCurAmount());

        sydney.update();
        assertEquals(5, sydney.findUnit("Cavalry").getCurAmount());
        assertEquals(7, sydney.findUnit("Slingerman").getCurAmount());
    }

    @Test
    public void excessiveTrainTest() {
        List<Unit> SydUnits = new ArrayList<Unit>();
        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);

        List<String> trainUnits = new ArrayList<String>();
        String sling = "Slingerman";
        String cavlry = "Cavalry";
        String swordsman = "Swordsman";
        trainUnits.add(sling);
        trainUnits.add(cavlry);
        trainUnits.add(swordsman);

        assertEquals("Unsuccessful training too many troops are training already", sydney.train(trainUnits));

        trainUnits.remove(0);

        sydney.train(trainUnits);
        assertEquals("Unsuccessful training too many troops are training already", sydney.train(trainUnits));
    }

    @Test
    public void noDefenceTest() {
        List<Unit> SydUnits = new ArrayList<Unit>();

        Unit SydArcher = new Archerman(12, 12);
        Unit SydSwordsMan = new Swordsman(10, 10);
        Unit SydSpearman = new Spearman(9, 123);

        SydUnits.add(SydArcher);
        SydUnits.add(SydSpearman);
        SydUnits.add(SydSwordsMan);

        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);

        List<Unit> MelUnits = new ArrayList<Unit>();
        Region melbourne = new Region("Melbourne", new GameTurn(4, 4, 4), MelUnits, 10, 10);

        assertEquals(0, BattleResolver.getDefendingWin(SydUnits, melbourne, sydney));
        assertEquals(1, BattleResolver.getAttackingWin(SydUnits, melbourne, sydney));
        assertEquals("Attackers win", BattleResolver.resolve(SydUnits, melbourne ,sydney));
        assertEquals(145, melbourne.getTotalUnits());
        assertEquals(0, sydney.getTotalUnits());
    }

    @Test
    public void loadSaveTest() {
        try {
            Game game = new Game("src/test/resources/default.json");
            game.save("defaultOutput");

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/default.json"));
            File dir = new File("saves");
            dir.mkdir();
            String filename = "defaultOutput.json";
            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));

            //test if the output save is the same as config input
            assertArrayEquals(f1, f2);
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveAfterTrainTest() {
        try {
            Game game = new Game("src/test/resources/trainTest.json");
            //Player1 training Archerman & Spearman
            ArrayList<String> troops = new ArrayList<>();
            troops.add("Archerman");
            troops.add("Spearman");
            game.action("Cyprus", troops);
            game.endPhase();
            game.endPhase();

            //Player2 training cavalry & swordsman
            troops = new ArrayList<>();
            troops.add("Cavalry");
            troops.add("Swordsman");
            game.action("Syria", troops);
            game.endPhase();
            game.endPhase();

            //Player1 preparation turn
            game.save("trainOutput");

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/trainExpected.json"));
            File dir = new File("saves");
            dir.mkdir();
            String filename = "trainOutput.json";
            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));

            //test if the output save is the same as config input
            assertArrayEquals(f1, f2);
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wealthTest() {
        List<Unit> SydUnits = new ArrayList<Unit>();
        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);
        assertEquals(1, sydney.calcGold());
    }

    @Test
    public void wealthIncrementTest() {
        List<Unit> SydUnits = new ArrayList<Unit>();
        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);
        assertEquals(10, sydney.getWealth());
        
        sydney.update();
        assertEquals(20, sydney.getWealth());

        sydney.setTax(15);
        sydney.update();
        assertEquals(20, sydney.getWealth());

        sydney.setTax(20);
        sydney.update();
        assertEquals(10, sydney.getWealth());

        sydney.setTax(25);
        sydney.setWealth(50);
        sydney.update();
        assertEquals(20, sydney.getWealth());
    }

    @Test
    public void resetMovementPointTest() {
        List<Unit> SydUnits = new ArrayList<Unit>();
        Region sydney = new Region("Sydney", new GameTurn(4, 4, 4), SydUnits, 10, 10);
        assertEquals(1, sydney.findUnit("Archerman").getCurMovementPoints());
        assertEquals(1, sydney.findUnit("Cavalry").getCurMovementPoints());
        assertEquals(1, sydney.findUnit("Slingerman").getCurMovementPoints());
        assertEquals(1, sydney.findUnit("Swordsman").getCurMovementPoints());
        assertEquals(1, sydney.findUnit("Spearman").getCurMovementPoints());

        sydney.update();
        assertEquals(sydney.findUnit("Archerman").getMaxMovementPoints(), sydney.findUnit("Archerman").getCurMovementPoints());
        assertEquals(sydney.findUnit("Cavalry").getMaxMovementPoints(), sydney.findUnit("Cavalry").getCurMovementPoints());
        assertEquals(sydney.findUnit("Slingerman").getMaxMovementPoints(), sydney.findUnit("Slingerman").getCurMovementPoints());
        assertEquals(sydney.findUnit("Swordsman").getMaxMovementPoints(), sydney.findUnit("Swordsman").getCurMovementPoints());
        assertEquals(sydney.findUnit("Spearman").getMaxMovementPoints(), sydney.findUnit("Spearman").getCurMovementPoints());
        
    }
    @Test
    public void playerMoneyGainTest(){
        try {
            Game game = new Game("src/test/resources/default.json");
            assertEquals(400, game.getCurPlayer().getGold());
            game.getCurPlayer().update();
            assertEquals(402, game.getCurPlayer().getGold());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    @Test
    public void saveAfterMoveTest() {
        try {
            Game game = new Game("src/test/resources/moveTest.json");
            ArrayList<String> troops = new ArrayList<>();
            troops.add("Archerman");
            troops.add("Spearman");
            game.action("Cyprus", troops, "Lusitania");
            game.save("moveOutput");

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/moveExpected.json"));
            File dir = new File("saves");
            dir.mkdir();
            String filename = "moveOutput.json";
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
        try {
            Game game = new Game("src/test/resources/invadeTest.json");
            List<String> troops = new ArrayList<>();
            troops.add("Spearman");
            game.action("Cyprus", troops, "Cilicia", "Egypt");

            game.save("invadeOutput");

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/invadeExpected.json"));
            File dir = new File("saves");
            dir.mkdir();
            String filename = "invadeOutput.json";
            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));
            assertArrayEquals(f1, f2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

