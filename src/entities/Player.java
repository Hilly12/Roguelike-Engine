package entities;

import util.Coord;
import util.Stats;

public class Player extends LivingEntity {

    public Player(Coord pos, Stats stats) {
        super(pos, stats);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }
}