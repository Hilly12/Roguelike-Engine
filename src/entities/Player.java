package entities;
import util.Coord;
import util.EntityType;
import util.Stats;

public class Player extends LivingEntity {

    public Player(Coord pos, int level, int sp, Stats stats) {
        super(pos, level, sp, stats, EntityType.Player, NPC.PLAYER);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isHostile(LivingEntity entity) {
        return entity.isEnemy();
    }
}