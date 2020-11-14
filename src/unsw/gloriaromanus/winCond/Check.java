package unsw.gloriaromanus.winCond;

import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import unsw.gloriaromanus.game.Player;

public class Check {
    private Check subCheck;
    private WinCond goal;
    private Junction checkType;

    public Check(List<WinCond> goals) {
        if(goals.size() == 0) {
            this.goal = null;
            this.subCheck = null;
            this.checkType = null;
        } else {
            Random randomiser = new Random();
            int randomGoal = randomiser.nextInt(goals.size());
            this.goal = goals.get(randomGoal);
            if(randomiser.nextDouble() <= 0.5 && goals.size()!=1) {
                this.checkType = new OrCheck();
            } else {
                this.checkType = new AndCheck();
            }
            goals.remove(randomGoal);
            this.subCheck = new Check(goals);
        }
    }

    public Check(String goal, String checkType, JSONObject subCheck) {
        if(goal.compareTo("null") == 0) {
           this.subCheck = null;
           this.goal = null;
           this.checkType = null;
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

            if(subCheck.getString("Goal").compareTo("null") == 0) {
                JSONObject stopRecursion = new JSONObject();
                stopRecursion.put("Goal", "null");
                stopRecursion.put("Junction", "null");
                stopRecursion.put("SubCheck", "null");
                this.subCheck = new Check(subCheck.getString("Goal"), subCheck.getString("Junction"), stopRecursion);
            } else {
                this.subCheck = new Check(subCheck.getString("Goal"), subCheck.getString("Junction"), subCheck.getJSONObject("SubCheck"));
            }
        }
    }

    /**
     * Checks if a player has met a random conjunction / disjunction
     * of different Win Conditions
     * @param gamer Player that the conditions are checked against
     * @return the success of the player meeting the random
     *         conjunction / disjunction of the condition
     */
    public boolean player(Player gamer) {
        if(goal == null) {
            return true;
        }
        return checkType.conds(goal.playerWin(gamer), subCheck.player(gamer));
    }

    public JSONObject getSave() {
        JSONObject save = new JSONObject();
        if(goal == null) {
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

    public boolean stopRecursion() {
        if(subCheck == null && checkType == null && goal == null) {
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        if(subCheck.stopRecursion()) {
            return goal.getName();
        }
        return " " + goal.getName() + " " + checkType.getName() +" " + subCheck.toString() ;
    }
}
