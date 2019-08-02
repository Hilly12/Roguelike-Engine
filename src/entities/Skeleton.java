package entities;

import util.Coord;
import util.Stats;

public class Skeleton extends LivingEntity {

    public Skeleton(Coord pos, Stats stats) {
        super(pos, stats);
    }

    @Override
    public boolean isEnemy() {
        return true;
    }
}