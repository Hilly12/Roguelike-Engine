package util;

import entities.Entity;

import java.util.HashMap;

public class Map {
    public final int width;
    public final int height;
    public final int[][] walkableMap;
    public final int[][] blockedTileMap;
    public final int[][] seenTiles;
    public final HashMap<Coord, Entity> entityMap;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        walkableMap = new int[width][height];
        blockedTileMap = new int[width][height];
        seenTiles = new int[width][height];
        entityMap = new HashMap<>();
    }

    // Helper Functions

    public boolean isWalkable(Coord c) {
        return walkableMap[c.x][c.y] == 0 && blockedTileMap[c.x][c.y] == 0;
    }

    public void see(Coord c) {
        seenTiles[c.x][c.y] = 1;
    }

    public void block(Coord c) {
        blockedTileMap[c.x][c.y] = 1;
    }

    public void unblock(Coord c) {
        blockedTileMap[c.x][c.y] = 0;
    }

    public boolean isValidCoord(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    public boolean isValidCoord(Coord c) {
        return 0 <= c.x && c.x < width && 0 <= c.y && c.y < height;
    }
}
