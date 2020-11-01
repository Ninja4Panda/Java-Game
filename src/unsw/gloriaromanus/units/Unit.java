package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.AttackType;

public class Unit {
    private int curMovementPoints; // capability for the unit to move (done)
    private int maxMovementPoints; // (done)
    private int buildTime; // how many turns it will take the unit to appear on the map (done)
    private int trainAmount; // how many troops it trains (done)
    private int curAmount; // how many troops are there currently (done)
    private int cost; // how much money is needed to make the unit(done)
    private AttackType attack; // has an int attackValue which will help determine how much damage the other
                               // unit takes
    private int armour; // percentage decrease in the damage taken to the units health
    private int health; // amount of health of the unit, this determines if the unit is dead or not
    private int maxHealth; // amount of health the unit can get to
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield

    public Unit(int curMovementPoints, int maxMovementPoints, int buildTime, int cost, int trainAmount, int curAmount,
            AttackType attack, int armour, int health, int maxHealth, int defenseSkill, int shieldDefense) {

        this.curMovementPoints = curMovementPoints;
        this.maxMovementPoints = maxMovementPoints;
        this.buildTime = buildTime;
        this.setCost(cost);
        this.trainAmount = trainAmount;
        this.curAmount = curAmount;
        this.attack = attack;
        this.armour = armour;
        this.setHealth(health);
        this.setMaxHealth(maxHealth);
        this.setDefenseSkill(defenseSkill);
        this.setShieldDefense(shieldDefense);
    }

    public int getAttackValue() {
        return attack.getAttackValue();
    }
    public int getRange() {
        return attack.getRange();
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

    public void setShieldDefense(int shieldDefense) {
        this.shieldDefense = shieldDefense;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public void setDefenseSkill(int defenseSkill) {
        this.defenseSkill = defenseSkill;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMaxMovementPoints() {
        return maxMovementPoints;
    }


    public void setCurMovementPoints(int curMovementPoints) {
        this.curMovementPoints = curMovementPoints;
    }


    public int getCurMovementPoints() {
        return curMovementPoints;
    }


    public void reduceMovementPoints(int movementPoints) {
        this.curMovementPoints -= movementPoints;
        if(this.curMovementPoints<0) this.curMovementPoints = 0;
    }


    public int getCurAmount() {
        return curAmount;
    }


    public int getTrainAmount() {
        return trainAmount;
    }


    public String getClassName() {
        String className = this.getClass().getName();
        String[] classNameParts = className.split("\\.");
        return classNameParts[classNameParts.length - 1];
    }




    public int trainTime() {
        return buildTime;
    }

    public void addTrainedUnit() {
        curAmount+=trainAmount;
    }

    public void addUnits(int numTroops) {
        curAmount+=numTroops;
    }


    public void minusUnits(int num) {
        curAmount -= num;
    }


    public int strength() {
        return armour * attack.getAttackValue() * getCurAmount();
    }
}
