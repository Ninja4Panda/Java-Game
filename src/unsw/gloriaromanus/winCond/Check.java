package unsw.gloriaromanus.winCond;

import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import unsw.gloriaromanus.Player;

public class Check {
    private Check subCheck;
    private WinCond goal;
    private Junction checkType;
    public Check(List<WinCond> goals) {
        if( !(goals.size() > 0) ) {
            this.goal = null;
            this.subCheck = null;
        } else {
            Random randomiser = new Random();
            int randomGoal = randomiser.nextInt(goals.size());
            this.goal = goals.get(randomGoal);
            goals.remove(randomGoal);
            this.subCheck = new Check(goals);
            if(randomiser.nextDouble() <= 0.5) {
                this.checkType = new OrCheck();
            } else {
                this.checkType = new AndCheck();
            }
        }
        
    }
    public Check(String goal, String checkType, JSONObject subCheck) {
        if(goal.compareTo("null") == 0 && checkType.compareTo("null") == 0) {
           this.subCheck = null;
           this.checkType = null;
           this.goal = null;
        } else {
            switch (goal) {
                case "WealthCond":
                    this.goal = new WealthCond();
                    break;
                case "TreasuryCond":
                    this.goal = new TreasuryCond();
                    break;
                case "ConquestCond":
                    this.goal = new ConquestCond();
                    break;
                default:
                    break;
            }
            if(checkType.compareTo("OrCheck") == 0) {
                this.checkType = new OrCheck();
            } else if (checkType.compareTo("AndCheck") == 0){
                this.checkType = new AndCheck();
            } 
            if(subCheck.getString("Goal").compareTo("null") == 0 && subCheck.getString("Junction").compareTo("null") == 0) {
                JSONObject stopRecursion = new JSONObject();
                stopRecursion.put("Goal", "null");
                stopRecursion.put("Junction", "null");
                stopRecursion.put("SubCheck", "null");
                this.subCheck = new Check(subCheck.getString("Goal"),
                                         subCheck.getString("Junction"),stopRecursion);

            } else {
                this.subCheck = new Check(subCheck.getString("Goal"),
                                         subCheck.getString("Junction"), subCheck.getJSONObject("SubCheck"));
            }
        }
    }

    public boolean player(Player gamer) {
        if(goal ==  null && subCheck == null) {
            return true;
        }
        return checkType.conds(goal.playerWin(gamer), subCheck.player(gamer));
    }

    public JSONObject getSave() {
        JSONObject save = new JSONObject();
        if(subCheck == null || goal == null) {
            save.put("SubCheck", "null");
            save.put("Goal", "null");
            save.put("Junction", "null");
        } else {
            save.put("SubCheck", subCheck.getSave());
            save.put("Junction", checkType.getName());
            save.put("Goal", goal.getName());
        }
        return save;

    }
}
 
