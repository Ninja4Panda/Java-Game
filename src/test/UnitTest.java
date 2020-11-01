package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.Game.BattleResolver;
import unsw.gloriaromanus.Game.Game;
import unsw.gloriaromanus.Game.GameTurn;
import unsw.gloriaromanus.Phase.MovePhase;
import unsw.gloriaromanus.Phase.PreparationPhase;
import unsw.gloriaromanus.region.Region;
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

    public void endPhaseTest() {
        List<String> factions = new ArrayList<>();
        factions.add("Rome");
        factions.add("Gaul");
        try {
            Game game = new Game(factions);
            assertTrue(game.getCurPhase() instanceof PreparationPhase);
            game.endPhase();
            assertTrue(game.getCurPhase() instanceof MovePhase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void moveTroopsTest(){
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

        sydney.moveTroops(sydney.findUnit("Spearman"), melbourne);
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
    }

    @Test
    public void attackAdjacentTest() {
        List<String> factions = new ArrayList<>();
        factions.add("Rome");
        factions.add("Gaul");
        try {
            Game game = new Game(factions);
            List<String> atkUnits = new ArrayList<>();
            atkUnits.add("Swordsman");
            assertEquals(game.action("Lusitania", atkUnits), "Success");
            game.endPhase();
            game.endPhase();

            List<String> defUnits = new ArrayList<>();
            defUnits.add("Archerman");
            defUnits.add("Swordsman");
            assertEquals(game.action("Baetica", defUnits), "Success");
            game.endPhase();
            game.endPhase();
            game.endPhase();
            String result = game.action("Lusitania", atkUnits, "Baetica", "Gaul");
            assertTrue("Defenders win".equals(result) || "Attackers win".equals(result));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void defWinMostTest() {
        List<String> factions = new ArrayList<>();
        factions.add("Rome");
        factions.add("Gaul");
        try {
            Game game = new Game(factions);
            List<String> atkUnits = new ArrayList<>();
            atkUnits.add("Swordsman");
            assertEquals(game.action("Lugdunensis", atkUnits), "Success");
            System.out.println(game.getCurPhase());
            //TODO
            System.out.println(game.getCurPlayer().getFaction());
            System.out.println(game.endPhase());
            System.out.println(game.getCurPhase());
            System.out.println(game.getCurPlayer().getFaction());
            System.out.println(game.endPhase());
            System.out.println(game.getCurPhase());
            System.out.println(game.getCurPlayer().getFaction());
            //Player2
            List<String> defUnits = new ArrayList<>();
            defUnits.add("Archerman");
            defUnits.add("Swordsman");
            assertEquals(game.action("Baetica", defUnits), "Success");
            game.endPhase();
            game.endPhase();

            //Player1
            game.endPhase();
            game.endPhase();
            //Player2
            assertEquals(game.action("Baetica", defUnits), "Success");
            game.endPhase();
            game.endPhase();
            //Player1
            game.endPhase();
            String result = game.action("Lugdunensis", atkUnits, "Belgica", "Gaul");
            assertTrue("Defenders win".equals(result) || "Attackers win".equals(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void atkWinMostTest() {
        List<String> factions = new ArrayList<>();
        factions.add("Rome");
        factions.add("Gaul");
        try {
            Game game = new Game(factions);
            List<String> atkUnits = new ArrayList<>();
            atkUnits.add("Swordsman");
            atkUnits.add("Archerman");
            assertEquals(game.action("Lugdunensis", atkUnits), "Success");
            game.endPhase();
            game.endPhase();
            //Player2
            List<String> defUnits = new ArrayList<>();
            defUnits.add("Archerman");
            assertEquals(game.action("Baetica", defUnits), "Success");
            game.endPhase();
            game.endPhase();

            //Player1
            assertEquals(game.action("Lugdunensis", atkUnits), "Success");
            game.endPhase();
            game.endPhase();
            //Player2
            game.endPhase();
            game.endPhase();

            //Player1
            assertEquals(game.action("Lugdunensis", atkUnits), "Success");
            game.endPhase();
            game.endPhase();
            //Player2
            game.endPhase();
            game.endPhase();
            //Player1
            game.endPhase();
            String result = game.action("Lugdunensis", atkUnits, "Belgica", "Gaul");
            assertTrue("Defenders win".equals(result) || "Attackers win".equals(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void attackNotAdjacentTest() {
        List<String> factions = new ArrayList<>();
        factions.add("Rome");
        factions.add("Gaul");
        try {
            Game game = new Game(factions);
            List<String> atkUnits = new ArrayList<>();
            atkUnits.add("Archerman");
            assertEquals(game.action("Lugdunensis", atkUnits), "Success");
            game.endPhase();
            game.endPhase();

            //Player2
            List<String> defUnits = new ArrayList<>();
            defUnits.add("Archerman");
            defUnits.add("Swordsman");
            assertEquals(game.action("Baetica", defUnits), "Success");
            game.endPhase();
            game.endPhase();

            //Player1
            game.endPhase();
            String result = game.action("Lugdunensis", atkUnits, "Belgica", "Gaul");
            assertTrue("Defenders win".equals(result) || "Attackers win".equals(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadSaveTest() {
        try {
            Game game = new Game("src/test/resources/default.json");
            game.save("defaultOutput");

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/default.json"));
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
            assertEquals(game.action("Cyprus", troops),"Unsuccessful training too many troops are training already");
            game.endPhase();
            game.endPhase();

            //Player2 training cavalry & swordsman
            troops = new ArrayList<>();
            troops.add("Cavalry");
            troops.add("Swordsman");
            assertEquals(game.action("Syria", troops), "Success");
            game.endPhase();
            game.endPhase();

            //Player1 preparation turn
            game.save("trainOutput");

            byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/trainExpected.json"));
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
        assertEquals(10, sydney.findUnit("Archerman").getCurMovementPoints());
        assertEquals(20, sydney.findUnit("Cavalry").getCurMovementPoints());
        assertEquals(15, sydney.findUnit("Slingerman").getCurMovementPoints());
        assertEquals(10, sydney.findUnit("Swordsman").getCurMovementPoints());
        assertEquals(10, sydney.findUnit("Spearman").getCurMovementPoints());

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
            String filename = "invadeOutput.json";
            byte[] f2 = Files.readAllBytes(Paths.get("saves/",filename));
            assertArrayEquals(f1, f2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
    }

   @Test
   public void newGameTest() {
       List<String> factions = new ArrayList<>();
       factions.add("Rome");
       factions.add("Chinese");
       factions.add("Airbender");
       factions.add("Gaul");
       try{
           Game game = new Game(factions);
           game.save("newGameOutput");
       } catch (IOException e) {
            e.printStackTrace();
       }
   }

    @Test
    public void wealthWinTest() {
        try {
            Game game = new Game("src/test/resources/wealthWin.json");
            game.endPhase();
            game.endPhase();
            game.save("wealthWinOut.json");

        } catch (IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }


}

