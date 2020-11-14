package unsw.gloriaromanus.winCond;

public class OrCheck implements Junction{

    @Override
    public boolean conds(boolean goal1, boolean goal2) {
        return goal1 || goal2;
    }

    @Override
    public String getName() {
        return "or";
    }
}
