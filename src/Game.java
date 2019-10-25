import entities.*;
import util.Coord;
import util.Map;
import util.Resource;

import java.util.*;

public class Game {

    private enum State {
        IDLE,
        ANIMATING
    }

    private Player player;
    /*private*/ List<LivingEntity> allies;
    private List<LivingEntity> npcs;
    private int enemyCount;
    /*private*/ List<LivingEntity> enemies;

    private List<Runnable> actions;

    private State gameState;
    private Map map;

    public Game() {
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

        player = new Player(new Coord(4, 5), 1, 2, Resource.fromTable(0));
        map.entityMap.put(player.pos, player);
        map.block(player.pos);

        Skeleton sk = new Skeleton(new Coord(2, 2), 1, NPC.ALLY);
        map.entityMap.put(sk.pos, sk);
        map.block(sk.pos);
        allies.add(sk);

        Skeleton sk2 = new Skeleton(new Coord(3, 8), 1, NPC.ENEMY);
        map.entityMap.put(sk2.pos, sk2);
        map.block(sk2.pos);
        enemies.add(sk2);

        Skeleton sk3 = new Skeleton(new Coord(4, 6), 1, NPC.ENEMY);
        map.entityMap.put(sk3.pos, sk3);
        map.block(sk3.pos);
        enemies.add(sk3);
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
            MoveNPCs();
            gameState = State.ANIMATING;
        } else {
            if (AttemptAttack(player, newPos)) {
                MoveNPCs();
                gameState = State.ANIMATING;
            }
        }
    }

    private void MoveNPCs() {
        for (LivingEntity ally : allies) {
            MakeAllyMove(ally);
        }
        for (LivingEntity enemy : enemies) {
            MakeEnemyMove(enemy);
        }
        for (LivingEntity npc : npcs) {
            MakeFriendlyNPCMove(npc);
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

    // Decision Making

    private void MakeEnemyMove(LivingEntity enemy) {
        // Check if can attack anything
        if (TryRandomAttack(enemy)) {
            return;
        }

        // Move
        ArrayList<Coord> validMoveDirections = new ArrayList<>();
        GetValidMoves(enemy, validMoveDirections);
        int distSq = Coord.distanceSquared(player.pos, enemy.pos);
        if (distSq <= Math.pow(enemy.getStats().lineOfSight, 2)) {
            MoveToward(enemy, player.pos, distSq, validMoveDirections);
        } else if (validMoveDirections.size() > 0) {
            MoveNPC(enemy, Resource.getRandom(validMoveDirections));
        }
    }

    private void MakeAllyMove(LivingEntity ally) {
        // Check if can attack anything
        // Could add different types of personalities depending on ally ie. hostile, lazy, follower
        if (player.isAttacking()) {
            if (TryRandomAttack(ally)) {
                return;
            }
        }

        // Move
        ArrayList<Coord> validMoveDirections = new ArrayList<>();
        GetValidMoves(ally, validMoveDirections);
        int distSq = Coord.distanceSquared(player.pos, ally.pos);
        if (distSq <= Math.pow(ally.getStats().lineOfSight, 2)) {
            MoveToward(ally, player.pos, distSq, validMoveDirections);
        } else if (validMoveDirections.size() > 0) {
            MoveNPC(ally, Resource.getRandom(validMoveDirections));
        }
    }

    private void MakeFriendlyNPCMove(LivingEntity friend) {
        ArrayList<Coord> validMoveDirections = new ArrayList<>();
        GetValidMoves(friend, validMoveDirections);
        if (Math.random() > 0.2) {
            MoveNPC(friend, Resource.getRandom(validMoveDirections));
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

    private boolean TryRandomAttack(LivingEntity attacker) {
        for (int x = attacker.pos.x - 1; x <= attacker.pos.x + 1; x++) {
            for (int y = attacker.pos.y - 1; y <= attacker.pos.y + 1; y++) {
                Coord c = new Coord(x, y);
                if (AttemptAttack(attacker, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean AttemptAttack(LivingEntity attacker, Coord target) {
        Entity occupier = map.entityMap.get(target);
        if (occupier != null && occupier.isLivingEntity()) {
            System.out.println(occupier.getImageID());
            LivingEntity victim = (LivingEntity) occupier;
            if (attacker.isHostile(victim)) {
                Coord newPos = Coord.add(victim.getDirection(), victim.pos);
                if (victim.isMoving() && !Coord.areAdjacent(newPos, attacker.pos)) {
                    return false;
                }
                attacker.setAttacking(true);
                // TODO: Play attack sound
                actions.add(() -> {
                    attacker.face(Coord.subtract(victim.pos, attacker.pos));
                    attacker.setAttacking(false);
                    if (!victim.takeDamage(attacker.getStats().attack)) {
                        if (victim.isEnemy()) {
                            enemies.remove(victim);
                        } else if (victim.isAlly()) {
                            allies.remove(victim);
                        }
                        // unblock occupied tile
                        if (victim.equals(map.entityMap.get(target))) {
                            map.entityMap.remove(target);
                        }
                        if (victim.equals(map.entityMap.get(newPos))) {
                            map.entityMap.remove(newPos);
                        }
                        // TODO: Add fade effect at death position
                    }
                });
                return true;
            }
        }
        return false;
    }

    private void GetValidMoves(LivingEntity entity, List<Coord> validMoveDirections) {
        if (map.isWalkable(entity.pos.left())) {
            validMoveDirections.add(new Coord(-1, 0));
        }
        if (map.isWalkable(entity.pos.right())) {
            validMoveDirections.add(new Coord(1, 0));
        }
        if (map.isWalkable(entity.pos.up())) {
            validMoveDirections.add(new Coord(0, -1));
        }
        if (map.isWalkable(entity.pos.down())) {
            validMoveDirections.add(new Coord(0, 1));
        }
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