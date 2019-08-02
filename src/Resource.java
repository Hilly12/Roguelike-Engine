import util.Stats;
import java.util.List;

public class Resource {

    public final static int refreshRate = 40;
    public static final int spriteRefresh = 4; // Every n frames
    public static final int maxSteps = 4;

    public static final int visibleMapWidth = 16;
    public static final int visibleMapHeight = 9;

    // Will be modified and returned by function
    private static final Stats skeletonBase = new Stats(10, 2, 2, 0, 5, 2);

    public static Stats newPlayerStats() {
        return new Stats(30, 3, 3, 0, 0, 3);
    }

    public static <T> T getRandom(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
