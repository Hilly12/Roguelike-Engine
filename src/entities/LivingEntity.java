package entities;

import util.Coord;
import util.Stats;

public abstract class LivingEntity extends Entity {
    private Stats stats;
    private Coord direction;
    private boolean facingLeft;
    private boolean moving;
    private boolean attacking;

    public LivingEntity(Coord pos, Stats stats) {
        super(pos);
        this.stats = stats;
        facingLeft = false;
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

    private void face(Coord direction) {
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

    public void takeDamage(int damage) {
        stats.health -= damage;
    }

    @Override
    public boolean isLivingEntity() {
        return true;
    }
}
