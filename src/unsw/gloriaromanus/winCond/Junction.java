package unsw.gloriaromanus.winCond;

public interface Junction {
    public boolean conds(boolean goal1, boolean goal2);
    default public String getClassName() {
        String className = this.getClass().getName();
        String[] classNameParts = className.split("\\.");
        return classNameParts[classNameParts.length - 1];
    }

    public String getName();
}
