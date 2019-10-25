package entities;

import util.Coord;
import util.EntityType;

public class Skeleton extends LivingEntity {

    public Skeleton(Coord pos, int level, NPC type) {
        super(pos, level, EntityType.Skeleton, type);
    }
}