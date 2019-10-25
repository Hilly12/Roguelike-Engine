package util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Resource {

    public final static int refreshRate = 40;
    public static final int spriteRefresh = 4; // Every n frames
    public static final int maxSteps = 4;

    public static final int visibleMapWidth = 16;
    public static final int visibleMapHeight = 9;

    public static List<List<BufferedImage>> livingEntitySprites;

    public static void init() {
        livingEntitySprites = new ArrayList<>();
    }

    public static final int[][] entityBaseStats = {
            { 30, 3, 3, 3, 0, 0 },
            { 20, 2, 2, 2, 0, 5 },
    };

    public static Stats fromTable(int index) {
        int[] stats = entityBaseStats[index];
        return new Stats(stats[0], stats[1], stats[2], stats[3], stats[4], stats[5]);
    }


    public static <T> T getRandom(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
