package pl.javanexus.day14;

import java.util.HashMap;
import java.util.Map;

public class NextPoints {

    private final Map<Integer, byte[]> nextPoints = new HashMap<>();

    public NextPoints() {
        initNextPoints();
    }

    public byte[] get(int index) {
        return nextPoints.get(index);
    }

    private void initNextPoints() {
        nextPoints.put(0, new byte[] {0});
        nextPoints.put(1, new byte[] {1});
        nextPoints.put(2, new byte[] {2});
        nextPoints.put(3, new byte[] {3});
        nextPoints.put(4, new byte[] {4});
        nextPoints.put(5, new byte[] {5});
        nextPoints.put(6, new byte[] {6});
        nextPoints.put(7, new byte[] {7});
        nextPoints.put(8, new byte[] {8});
        nextPoints.put(9, new byte[] {9});
        nextPoints.put(10, new byte[] {1, 0});
        nextPoints.put(11, new byte[] {1, 1});
        nextPoints.put(12, new byte[] {1, 2});
        nextPoints.put(13, new byte[] {1, 3});
        nextPoints.put(14, new byte[] {1, 4});
        nextPoints.put(15, new byte[] {1, 5});
        nextPoints.put(16, new byte[] {1, 6});
        nextPoints.put(17, new byte[] {1, 7});
        nextPoints.put(18, new byte[] {1, 8});
    }
}
