package entities;

import util.Coord;
import util.EntityType;
import util.Resource;
import util.Stats;

public abstract class LivingEntity extends Entity {
    private Stats stats;
    private int level;
    private int sp;
    private Coord direction;
    private boolean facingLeft;
    private boolean moving;
    private boolean attacking;
    private NPC type;

    LivingEntity(Coord pos, int level, EntityType entityType, NPC type) {
        super(pos);
        this.level = level;
        this.entityType = entityType;
        this.type = type;
        GenerateStats();
        facingLeft = false;
        direction = new Coord(0, 1);
        moving = attacking = false;
    }

    LivingEntity(Coord pos, int level, int sp, Stats stats, EntityType entityType, NPC type) {
        super(pos);
        this.level = level;
        this.sp = sp;
        this.stats = stats;
        this.entityType = entityType;
        this.type = type;
        facingLeft = false;
        direction = new Coord(0, 1);
        moving = attacking = false;
    }

    public Coord getDirection() {
        return direction;
    }

    public void setDirection(Coord c) {
        direction = c;
        face(direction);
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void face(Coord direction) {
        if (direction.x != 0) {
            facingLeft = direction.x < 0;
        }
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setMoving(boolean b) {
        moving = b;
    }

    public void setAttacking(boolean b) {
        attacking = b;
    }

    public Stats getStats() {
        return stats;
    }

    public void move(Coord direction) {
        pos.add(direction);
    }

    public boolean takeDamage(int damage) {
        stats.health -= damage;
        return stats.health > 0;
    }

    @Override
    public boolean isLivingEntity() {
        return true;
    }

    @Override
    public boolean isPlayer() {
        return type == NPC.PLAYER;
    }

    @Override
    public boolean isNPC() {
        return !isPlayer();
    }

    @Override
    public boolean isFriendly() {
        return type == NPC.FRIENDLY;
    }

    @Override
    public boolean isAlly() {
        return type == NPC.ALLY;
    }

    @Override
    public boolean isEnemy() {
        return type == NPC.ENEMY;
    }

    public boolean isHostile(LivingEntity entity) {
        return (isEnemy() && (entity.isAlly() || entity.isPlayer()))
                || ((isPlayer() || isAlly()) && entity.isEnemy());
    }

    // TODO: WILL NOT RUN IF YOU DO NOT IMPLEMENT
    private void GenerateStats() {
        sp = level * 2;
        int[] stats = Resource.entityBaseStats[entityType.ordinal()];
        for (int i = 1; i <= level; i++) {

        }
        this.stats = new Stats(30, 3, 3, 3, 1, 5);
    }
}
