import java.util.*;

public class Game {

    private enum NPC {
        FRIENDLY,
        ALLY,
        ENEMY
    }

    private Player player;
    private List<LivingEntity> allies;
    private int enemyCount;
    private List<LivingEntity> npcs;
    private List<LivingEntity> enemies;

    private List<Runnable> actions;

    private int gameState;
    private int mapType;
    private Map map;

    public Game() {
        player = new Player(new Coord(4, 5), Resource.newPlayerStats());
        actions = new ArrayList<>();
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
        npcs = new ArrayList<>();
    }

    public void InitMap() {

    }

    public void MakePlayerMove(Coord direction) {
        Coord newPos = Coord.add(player.pos, direction);
        if (map.isWalkable(newPos)) {
            MoveEntity(player, direction, newPos);
            for (LivingEntity ally : allies) {
                MakeNPCMove(ally, newPos, NPC.ALLY.ordinal());
            }
            for (LivingEntity enemy : enemies) {
                MakeNPCMove(enemy, newPos, NPC.ENEMY.ordinal());
            }
            for (LivingEntity npc : npcs) {
                MakeNPCMove(npc, newPos, NPC.FRIENDLY.ordinal());
            }
            // Change Game State to Animating
        }
    }

    private void MakeNPCMove(LivingEntity npc, Coord newPlayerPos, int npcType) {
        ArrayList<Coord> validMoveDirections = new ArrayList<>();
        Coord left = npc.pos.left();
        Coord right = npc.pos.right();
        Coord up = npc.pos.up();
        Coord down = npc.pos.down();
        if (map.isWalkable(left)) {
            validMoveDirections.add(new Coord(-1, 0));
        }
        if (map.isWalkable(right)) {
            validMoveDirections.add(new Coord(1, 0));
        }
        if (map.isWalkable(up)) {
            validMoveDirections.add(new Coord(0, -1));
        }
        if (map.isWalkable(down)) {
            validMoveDirections.add(new Coord(0, 1));
        }
        int distSq = Coord.distanceSquared(player.pos, npc.pos);
        if (npcType == NPC.ENEMY.ordinal() && distSq <= Math.pow(npc.stats.lineOfSight, 2)) {
            if (player.isMoving()) {
                if (Coord.areNeighbours(newPlayerPos, npc.pos)) {
                    // TODO: Attack the mofo
                } else {
                    MoveToward(npc, player.pos, distSq, validMoveDirections);
                }
            } else if (Coord.areNeighbours(player.pos, npc.pos)) {
                // TODO: Attack the mofo
            } else {
                MoveToward(npc, player.pos, distSq, validMoveDirections);
            }
        } else if (npcType == NPC.ALLY.ordinal()) {
            MoveToward(npc, player.pos, distSq, validMoveDirections);
        } else if (npcType == NPC.FRIENDLY.ordinal()) {
            if (Math.random() > 0.2) {
                MoveNPC(npc, Resource.getRandom(validMoveDirections));
            }
        } else if (validMoveDirections.size() > 0) {
            MoveNPC(npc, Resource.getRandom(validMoveDirections));
        }
    }

    private void MoveToward(LivingEntity npc, Coord target, int distSq,
                            ArrayList<Coord> validMoveDirections) {
        int index = -1;
        int min = distSq;
        for (Coord dir : validMoveDirections) {
            int newDistSq = Coord.distanceSquared(target, Coord.add(npc.pos, dir));
            if (newDistSq < min) {
                index = validMoveDirections.indexOf(dir);
                min = newDistSq;
            }
        }
        if (index != -1) {
            MoveNPC(npc, validMoveDirections.get(index));
        }
    }

    // TODO: Fix -- Enemies cannot walk into tiles directly vacated
    private void MoveNPC(LivingEntity npc, Coord direction) {
        Coord newPos = Coord.add(npc.pos, direction);
        MoveEntity(npc, direction, newPos);
    }

    private void MoveEntity(LivingEntity entity, Coord direction, Coord newPos) {
        actions.add(() -> entity.move(direction));
        actions.add(() -> entity.setMoving(false));
        entity.face(direction);
        entity.setMoving(true);
        map.unblock(entity.pos);
        map.block(newPos);
    }

    void FinishMove() {
        for (Runnable r : actions) {
            r.run();
        }
        actions = new ArrayList<>();
    }
}