package entities;
import util.Coord;

public abstract class Entity {
    public final Coord pos;

    protected Entity (Coord pos) {
        this.pos = pos;
    }

    public boolean isLivingEntity() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isAlly() {
        return false;
    }

    public boolean isNPC() {
        return false;
    }

    public boolean isEnemy() {
        return false;
    }

    public boolean isStairs() {
        return false;
    }
}
