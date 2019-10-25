package entities;
import util.Coord;
import util.EntityType;

public abstract class Entity {

    public static int autoID = 0;

    public final Coord pos;
    private final int id;
    EntityType entityType;

    Entity (Coord pos) {
        this.pos = pos;
        id = autoID;
        autoID++;
    }

    public boolean isLivingEntity() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isNPC() {
        return false;
    }

    public boolean isFriendly() {
        return false;
    }

    public boolean isAlly() {
        return false;
    }

    public boolean isEnemy() {
        return false;
    }

    public boolean isStairs() {
        return false;
    }

    public int getImageID() {
        return entityType.ordinal();
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Entity)) {
            return false;
        }
        Entity entity = (Entity) other;
        return entity.id == id;
    }
}
