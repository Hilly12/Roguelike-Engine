package util;

public class Stats {

    public int health;
    public int attack;
    public int defense;
    public int range;
    public int lineOfSight;
    public int luck;

    public Stats(int health, int attack, int defense, int range, int lineOfSight, int luck) {
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.range = range;
        this.lineOfSight = lineOfSight;
        this.luck = luck;
    }
}