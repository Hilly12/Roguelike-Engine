public class Map {
    public final int width;
    public final int height;
    public final int[][] walkableMap;
    public final int[][] blockedTileMap;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        walkableMap = new int[width][height];
        blockedTileMap = new int[width][height];
    }

    public Map(int[][] template) {
        this.width = template.length;
        this.height = template[0].length;
        walkableMap = new int[width][height];
        blockedTileMap = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                walkableMap[x][y] = template[x][y];
                blockedTileMap[x][y] = template[x][y];
            }
        }
    }

    // Helper Functions

    public boolean isWalkable(Coord c) {
        return walkableMap[c.x][c.y] == 0 && blockedTileMap[c.x][c.y] == 0;
    }

    void block(Coord c) {
        blockedTileMap[c.x][c.y] = 1;
    }

    void unblock(Coord c) {
        blockedTileMap[c.x][c.y] = 0;
    }

    public boolean isValidCoord(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    public boolean isValidCoord(Coord c) {
        return 0 <= c.x && c.x < width && 0 <= c.y && c.y < height;
    }
}
