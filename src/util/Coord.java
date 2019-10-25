package util;

import java.util.Objects;

public class Coord {
    public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Coord other) {
        x += other.x;
        y += other.y;
    }

    public void subtract(Coord other) {
        x -= other.x;
        y -= other.y;
    }

    public static Coord add(Coord c1, Coord c2) {
        return new Coord(c1.x + c2.x, c1.y + c2.y);
    }

    public static Coord subtract(Coord c1, Coord c2) {
        return new Coord(c1.x - c2.x, c1.y - c2.y);
    }

    public int getIndex(Map map) {
        return x + y * map.width;
    }

    public static Coord indexToCoord(int index, Map map) {
        return new Coord(index % map.width, index / map.width);
    }

    public Coord left() {
        return new Coord(x - 1, y);
    }

    public Coord right() {
        return new Coord(x + 1, y);
    }

    public Coord up() {
        return new Coord(x, y - 1);
    }

    public Coord down() {
        return new Coord(x, y + 1);
    }

    public Coord virtualToReal(Coord base, int hCent, int vCent) {
        return new Coord(x + base.x - hCent, y + base.y - vCent);
    }

    public Coord realToVirtual(Coord base, int hCent, int vCent) {
        return new Coord(x - base.x + hCent, y - base.y + vCent);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coord)) {
            return false;
        }
        Coord other = (Coord) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static boolean areAdjacent(Coord a, Coord b) {
        return ((Math.abs(a.x - b.x) == 1) && (a.y == b.y)) || ((a.x == b.x) && (Math.abs(a.y - b.y) == 1));
    }

    public static boolean areNeighbours(Coord a, Coord b) {
        return Math.abs(a.x - b.x) <= 1 && Math.abs(a.y - b.y) <= 1;
    }

    public static int distanceSquared(Coord a, Coord b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
