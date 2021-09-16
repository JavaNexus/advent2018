package pl.javanexus.year2018.day3;

import java.util.List;

public class Fabric {

    public static final int MAX_SIZE = 1000;

    private final int[][] fabric;
    private List<Slice> slices;

    public Fabric() {
        this.fabric = new int[MAX_SIZE][];
        for (int row = 0; row < MAX_SIZE; row++) {
            fabric[row] = new int[MAX_SIZE];
        }
    }

    public void cutFabric(List<Slice> slices) {
        this.slices = slices;
        for (Slice slice : slices) {
            for (int x = slice.getLeft(); x < slice.getRight(); x++) {
                for (int y = slice.getTop(); y < slice.getBottom(); y++) {
                    fabric[x][y]++;
                }
            }
        }
    }

    public int getOverlappingArea() {
        int overlappingArea = 0;
        for (int x = 0; x < MAX_SIZE; x++) {
            for (int y = 0; y < MAX_SIZE; y++) {
                if (fabric[x][y] > 1) {
                    overlappingArea++;
                }
            }
        }

        return overlappingArea;
    }

    public int getNotOverlappedSliceId() {
        for (Slice slice : slices) {
            int usedArea = getUsedArea(slice);
            if (usedArea == slice.getArea()) {
                return slice.getId();
            }
        }

        throw new IllegalArgumentException("Couldn't find not overlapped slice");
    }

    private int getUsedArea(Slice slice) {
        int usedArea = 0;
        for (int x = slice.getLeft(); x < slice.getRight(); x++) {
            for (int y = slice.getTop(); y < slice.getBottom(); y++) {
                usedArea += fabric[x][y];
            }
        }
        return usedArea;
    }
}
