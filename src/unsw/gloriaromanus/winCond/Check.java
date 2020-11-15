package unsw.gloriaromanus.winCond;

import java.util.List;
import java.util.Random;

import org.json.JSONException;
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

    public Check(String goal, String checkType, JSONObject subCheck) throws JSONException {
        if("".equals(goal)) {
            this.subCheck = null;
            this.goal = null;
            this.checkType = null;
        } else {
            switch (goal) {
                case "Wealth":
                    this.goal = new WealthCond();
                    break;
                case "Treasury":
                    this.goal = new TreasuryCond();
                    break;
                case "Conquest":
                    this.goal = new ConquestCond();
                    break;
                default:
                    throw new JSONException("Invalid Goal");
            }

            if("or".equals(checkType)) {
                this.checkType = new OrCheck();
            } else if ("and".equals(checkType)) {
                this.checkType = new AndCheck();
            } else {
                throw new JSONException("Invalid Junction");
            }

            if(subCheck==null) throw new JSONException("Invalid subcheck");
            this.subCheck = new Check(subCheck.optString("Goal"), subCheck.optString("Junction"), subCheck.optJSONObject("SubCheck"));
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
        if(goal != null) {
            save.put("SubCheck", subCheck.getSave());
            save.put("Junction", checkType.getName());
            save.put("Goal", goal.getName());
        }
        return save;
    }

    public Junction getCheckType(){
        return checkType;
    }

    public WinCond getGoal() {
        return goal;
    }
    public Check getsubCheck() {
        return subCheck;
    }

    public void reset() {
        this.goal = null;
    }
}
