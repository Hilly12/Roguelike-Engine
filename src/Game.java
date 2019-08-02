import entities.LivingEntity;
import entities.Player;
import entities.Skeleton;
import util.Coord;
import util.Map;

import java.util.*;

public class Game {

    private enum State {
        IDLE,
        ANIMATING
    }

    private enum NPC {
        FRIENDLY,
        ALLY,
        ENEMY
    }

    private Player player;
    private List<LivingEntity> allies;
    private List<LivingEntity> npcs;
    private int enemyCount;
    private List<LivingEntity> enemies;

    private List<Runnable> actions;

    private State gameState;
    private int mapType;
    private Map map;

    public Game() {
        player = new Player(new Coord(4, 5), Resource.newPlayerStats());
        actions = new ArrayList<>();
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
        npcs = new ArrayList<>();
        gameState = State.IDLE;
    }

    public void InitializeMap() {
        // These should either be passed as parameters or gotten from somewhere

        // Map Generation
        map = new Map(30, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 || y == 0 || x == 9 || y == 9) {
                    map.walkableMap[x][y] = 1;
                    map.blockedTileMap[x][y] = 1;
                }
            }
        }
        for (int x = 10; x < 20; x++) {
            for (int y = 0; y < 10; y++) {
                map.walkableMap[x][y] = 1;
                map.blockedTileMap[x][y] = 1;
            }
        }
        for (int x = 20; x < 30; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 20 || y == 0 || x == 29 || y == 9) {
                    map.walkableMap[x][y] = 1;
                    map.blockedTileMap[x][y] = 1;
                }
            }
        }
        for (int x = 1; x < 29; x++) {
            map.walkableMap[x][5] = 0;
            map.blockedTileMap[x][5] = 0;
        }
        Skeleton sk = new Skeleton(new Coord(2, 2), Resource.newPlayerStats());
        map.entityMap.put(sk.pos, sk);
        map.block(sk.pos);
        sk.getStats().lineOfSight = 5;
        enemies.add(sk);

        Skeleton sk2 = new Skeleton(new Coord(3, 8), Resource.newPlayerStats());
        map.entityMap.put(sk2.pos, sk2);
        map.block(sk2.pos);
        sk2.getStats().lineOfSight = 5;
        enemies.add(sk2);

        Skeleton sk3 = new Skeleton(new Coord(4, 6), Resource.newPlayerStats());
        map.entityMap.put(sk3.pos, sk3);
        map.block(sk3.pos);
        sk3.getStats().lineOfSight = 5;
        enemies.add(sk3);

        map.entityMap.put(player.pos, player);
        map.block(player.pos);
    }

    public void StartMove(boolean left, boolean right, boolean up, boolean down) {
        if (left) {
            MakeMove(new Coord(-1, 0));
        } else if (right) {
            MakeMove(new Coord(1, 0));
        } else if (up) {
            MakeMove(new Coord(0, -1));
        } else if (down) {
            MakeMove(new Coord(0, 1));
        }
    }

    public void FinishMove() {
        for (Runnable r : actions) {
            r.run();
        }
        actions.clear();
    }

    private void MakeMove(Coord direction) {
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
            gameState = State.ANIMATING;
        }
    }

    private void MoveEntity(LivingEntity entity, Coord direction, Coord newPos) {
        Coord oldPos = new Coord(entity.pos.x, entity.pos.y);
        entity.setDirection(direction);
        entity.setMoving(true);
        map.unblock(oldPos);
        map.block(newPos);
        actions.add(() -> entity.move(direction));
        actions.add(() -> entity.setMoving(false));
        actions.add(() -> map.entityMap.remove(oldPos));
        actions.add(() -> map.entityMap.remove(newPos));
        actions.add(() -> map.entityMap.put(newPos, entity));
    }

    // Decision Maker
    private void MakeNPCMove(LivingEntity npc, Coord newPlayerPos, int npcType) {
        ArrayList<Coord> validMoveDirections = new ArrayList<>();
        if (map.isWalkable(npc.pos.left())) {
            validMoveDirections.add(new Coord(-1, 0));
        }
        if (map.isWalkable(npc.pos.right())) {
            validMoveDirections.add(new Coord(1, 0));
        }
        if (map.isWalkable(npc.pos.up())) {
            validMoveDirections.add(new Coord(0, -1));
        }
        if (map.isWalkable(npc.pos.down())) {
            validMoveDirections.add(new Coord(0, 1));
        }
        int distSq = Coord.distanceSquared(player.pos, npc.pos);
        if (npcType == NPC.ENEMY.ordinal() && distSq <= Math.pow(npc.getStats().lineOfSight, 2)) {
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

    // Path finder
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

    public boolean isIdle() {
        return gameState == State.IDLE;
    }

    public boolean isAnimating() {
        return gameState == State.ANIMATING;
    }

    public void setIdle() {
        gameState = State.IDLE;
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }
}