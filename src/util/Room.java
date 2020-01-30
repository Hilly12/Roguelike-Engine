package util;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public final int x1;
    public final int x2;
    public final int y1;
    public final int y2;
    public final int width;
    public final int height;
    public final Coord centre;

    Room(int x, int y, int w, int h) {
        this.x1 = x;
        this.x2 = (x + w);
        this.y1 = y;
        this.y2 = (y + h);
        this.width = w;
        this.height = h;
        this.centre = new Coord((x1 + x2) / 2, (y1 + y2) / 2);
    }

    List<Coord> getTiles() {
        List<Coord> tiles = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new Coord(this.x1 + x, this.y1 + y));
            }
        }
        return tiles;
    }

    List<Coord> getInteriorTiles() {
        List<Coord> tiles = new ArrayList<>();
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                tiles.add(new Coord(x1 + x, y1 + y));
            }
        }
        return tiles;
    }

    boolean intersects(Room room) {
        return (x1 <= room.x2)
                && (x2 >= room.x1)
                && (y1 <= room.y2)
                && (y2 >= room.y1);
    }
}
