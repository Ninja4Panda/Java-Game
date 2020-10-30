package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.AttackType;

public class BaseUnit implements Unit{
    private int movementPoints; // capability for the unit to move
    private int buildTime;      // how many turns it will take the unit to appear on the map
    private int trainAmount;    // how many troops it trains
    private int curAmount;      // how many troops are there currently
    private int cost;           // how much money is needed to make the unit
    private AttackType attack;  // has an int attackValue which will help determine how much damage the other unit takes
    private int armour;         // percentage decrease in the damage taken to the units health
    private int health;         // amount of health of the unit, this determines if the unit is dead or not
    private int maxHealth;      // amount of health the unit can get to
    private int defenseSkill;   // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense;  // a shield

    public BaseUnit( int movementPoints, int buildTime, int cost, int trainAmount, int curAmount, AttackType attack,
                int armour, int health, int maxHealth, int defenseSkill, int shieldDefense) {

        this.movementPoints = movementPoints;
        this.buildTime = buildTime;
        this.cost = cost;
        this.trainAmount = trainAmount;
        this.curAmount = curAmount;
        this.attack = attack;
        this.armour = armour;
        this.health = health;
        this.maxHealth = maxHealth;
        this.defenseSkill = defenseSkill;
        this.shieldDefense = shieldDefense;
    }

    @Override
    public int getMovementPoints() {
        return movementPoints;
    }

    @Override
    public void reduceMovementPoints(int movementPoints) {
        this.movementPoints -= movementPoints;
    }

    @Override
    public String getClassName() {
        String className = this.getClass().getName();
        String[] classNameParts = className.split("\\.");
        return classNameParts[classNameParts.length - 1];
    }

    @Override
    public int trainTime() {
        return buildTime;
    }

    public void addTrainedUnit() {
        curAmount+=trainAmount;
    }

    @Override
    public void addUnits(int numTroops) {
        curAmount+=numTroops;
    }

    public void minusUnit(int num) {
        curAmount -= num;
    }
}
