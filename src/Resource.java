import java.util.List;

public class Resource {

    private static final Stats skeletonBase = new Stats(10, 2, 2, 0, 5, 2);

    public static Stats newPlayerStats() {
        return new Stats(30, 3, 3, 0, 0, 3);
    }

    public static <T> T getRandom(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
