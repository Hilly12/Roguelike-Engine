package util;

import java.util.ArrayList;
import java.util.List;

public class MapGenerator {
    private int width;
    private int height;
    private int[][] walkableMap;
    private List<Room> rooms;

    public MapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.walkableMap = new int[width][height];
    }

    // The floor the player is on in order to adjust difficulty
    public void GenerateMap(int floor) {
        InitializeMap();
        placeRooms(floor);
        for (Room room : rooms) {
            for (int x = room.x1; x < room.x2; x++) {
                for (int y = room.y1; y < room.y2; y++) {
                    walkableMap[x][y] = 0;
                }
            }
        }
        placePlayerAndStairs();
        removeExcessWalls();
    }

    private void InitializeMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                walkableMap[x][y] = 1;
            }
        }
    }

    // For abyss floors assuming size is 128 x 72
    private void placeRooms(int floor) {
        rooms = new ArrayList<>();

        int minRooms = (int) (3.0F + (1.0F + floor / 10.0F));
        int maxRooms = (int) (4.0F * (1.0F + floor / 40.0F));
        int roomCount = getRandom(minRooms, maxRooms);
        for (int i = 0; i < roomCount; i++) {
            int w = getRandom(5, width / 7);
            int h = getRandom(5, height / 5);

            int x = getRandom(0, width - w - 1) + 1;
            int y = getRandom(0, height - h - 1) + 1;

            Room newRoom = new Room(x, y, w, h);

            boolean failed = false;
            for (Room otherRoom : rooms) {
                if (newRoom.intersects(otherRoom)) {
                    failed = true;
                    i--;
                    break;
                }
            }
            if (!failed) {
                Coord newCentre = newRoom.centre;
                if (rooms.size() != 0) {
                    Coord prevCentre = rooms.get(rooms.size() - 1).centre;
                    if (fiftyfifty()) {
                        hCorridor(prevCentre.x, newCentre.x, prevCentre.y);
                        vCorridor(prevCentre.y, newCentre.y, newCentre.x);
                    } else {
                        vCorridor(prevCentre.y, newCentre.y, prevCentre.x);
                        hCorridor(prevCentre.x, newCentre.x, newCentre.y);
                    }
                }
                rooms.add(newRoom);
            }
        }
    }

    private void placePlayerAndStairs() {
//        Room playerRoom = getRandom(rooms);
//        map.setPlayerPos(playerRoom.centre);
//        List<Coord> tiles = getRandom(rooms).getInteriorTiles();
//        map.setStairsPos(getRandom(tiles));
    }

    private void removeExcessWalls() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((walkableMap[x][y] == 1) &&
                        (getSurroundingFloorTiles(x, y) == 0)) {
                    walkableMap[x][y] = -1;
                }
            }
        }
    }

    private void hCorridor(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            walkableMap[x][y] = 0;
        }
    }

    private void vCorridor(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            walkableMap[x][y] = 0;
        }
    }

    private int getSurroundingFloorTiles(int hPos, int vPos) {
        int count = 0;
        for (int y = vPos - 1; y <= vPos + 1; y++) {
            for (int x = hPos - 1; x <= hPos + 1; x++) {
                if (0 <= x && x < width
                        && 0 <= y && y < height
                        && walkableMap[x][y] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private <T> T getRandom(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }

    private static int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    private boolean fiftyfifty() {
        return Math.random() > 0.5D;
    }
}

