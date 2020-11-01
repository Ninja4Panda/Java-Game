package unsw.gloriaromanus.winCond;

public class AndCheck implements Junction{

    @Override
    public boolean conds(boolean goal1, boolean goal2) {
        return goal1 && goal2;
    }
    
}
