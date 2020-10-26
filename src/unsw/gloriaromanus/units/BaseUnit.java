package unsw.gloriaromanus.units;

import unsw.gloriaromanus.units.attack.AttackType;

public class BaseUnit implements Unit{
    private int numTroops;      // number of troops
    private int movementPoints; // capability for the unit to move
    private int buildTime;      // how many turns it will take the unit to appear on the map
    private int cost;           // how much money is needed to make the unit
    private AttackType attack;  // has an int attackValue which will help determine how much damage the other unit takes
    private int armour;         // percentage decrease in the damage taken to the units health
    private int health;         // amount of health of the unit, this determines if the unit is dead or not
    private int range;          // range of the unit
    private int defenseSkill;   // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense;  // a shield


    public BaseUnit(int numTroops, int movementPoints, int buildTime, int cost, AttackType attack,
                int armour, int health, int range, int defenseSkill, int shieldDefense) {

        this.numTroops = numTroops;
        this.movementPoints = movementPoints;
        this.buildTime = buildTime; 
        this.cost = cost;
        this.attack = attack;
        this.armour = armour;
        this.health = health;
        this.range = range;
        this.defenseSkill = defenseSkill;
        this.shieldDefense = shieldDefense;
    }

    public int getNumTroops(){
        return numTroops;
    }

    public String getClassName() {
        String className = this.getClass().getName();
        String[] classNameParts = className.split("\\.");
        return classNameParts[classNameParts.length - 1];
    }

    public void move(String start, String end) {

    }
}
