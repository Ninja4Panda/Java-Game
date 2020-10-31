package unsw.gloriaromanus.Phase;

public class Dinode {
    private String id;
    private Dinode parent;
    private int cost;

    public Dinode(String id, Dinode parent, int cost) {
        this.setId(id);
        this.setParent(parent);
        this.setCost(cost);
    }
    public String getParentID() {
        return parent.getId();
    }
    public Dinode getParent() {
        return parent;
    }

    public void setParent(Dinode parent) {
        this.parent = parent;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
