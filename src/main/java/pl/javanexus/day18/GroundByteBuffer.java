package pl.javanexus.day18;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GroundByteBuffer {

    public static final int NUMBER_OF_GRIDS = 2;

    public static final int CHARACTER_SIZE = 1;
    public static final char OPEN = '.';
    public static final char TREE = '|';
    public static final char LUMBER = '#';

    private CharBuffer[] grids;
    private final int size;
    private final int[] byType = new int[TREE + 1];
    private final int[][] adjacentOffsets;

    private static final Map<Character, Function<int[], Character>> TRANSITIONS = new HashMap<>();
    static {
        TRANSITIONS.put(OPEN,   (byType) -> byType[TREE] >= 3 ? TREE : OPEN);
        TRANSITIONS.put(TREE,   (byType) -> byType[LUMBER] >= 3 ? LUMBER : TREE);
        TRANSITIONS.put(LUMBER, (byType) -> byType[TREE] >= 1 && byType[LUMBER] >= 1 ? LUMBER : OPEN);
    }

    public GroundByteBuffer(List<char[]> rows) {
        this.size = rows.size();
        this.grids = createBuffers();
        populateGrid(grids[0], rows);

        this.adjacentOffsets = getAdjacentOffsets();
    }

    private int[][] getAdjacentOffsets() {
        int[][] offsets = new int[size * size][];

        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = new int[] {
                    -size - CHARACTER_SIZE,
                    -size,
                    -size + CHARACTER_SIZE,
                    size - CHARACTER_SIZE,
                    size,
                    size + CHARACTER_SIZE,
                    -CHARACTER_SIZE,
                    CHARACTER_SIZE
            };
        }

        //1..8
        for (int i = 1; i < size - CHARACTER_SIZE; i++) {
            offsets[i] = new int[]{
                    -CHARACTER_SIZE,
                    CHARACTER_SIZE,
                    size - CHARACTER_SIZE,
                    size,
                    size + CHARACTER_SIZE
            };
        }

        //91..98
        for (int i = size * (size - CHARACTER_SIZE) + CHARACTER_SIZE; i < size * size - CHARACTER_SIZE; i++) {
            offsets[i] = new int[]{
                    -size - CHARACTER_SIZE,
                    -size,
                    -size + CHARACTER_SIZE,
                    -CHARACTER_SIZE,
                    CHARACTER_SIZE
            };
        }

        //10..90
        for (int i = size; i < size * size; i += size) {
            offsets[i] = new int[] {
                    -size,
                    -size + CHARACTER_SIZE,
                    CHARACTER_SIZE,
                    size,
                    size + CHARACTER_SIZE
            };
        }

        //9..99
        for (int i = size + size - CHARACTER_SIZE; i < size * size - CHARACTER_SIZE; i += size) {
            offsets[i] = new int[] {
                    -size - CHARACTER_SIZE,
                    -size,
                    -CHARACTER_SIZE,
                    size - CHARACTER_SIZE,
                    size
            };
        }

        //0
        offsets[0] = new int[] {
                CHARACTER_SIZE,
                size,
                size + CHARACTER_SIZE
        };
        //9
        offsets[size - CHARACTER_SIZE] = new int[] {
                -CHARACTER_SIZE,
                size - CHARACTER_SIZE,
                size
        };
        //90
        offsets[size * (size - CHARACTER_SIZE)] = new int[] {
                -size - CHARACTER_SIZE,
                -size,
                -size + CHARACTER_SIZE,
                -CHARACTER_SIZE,
                CHARACTER_SIZE
        };
        //99
        offsets[size * size - CHARACTER_SIZE] = new int[] {
                -CHARACTER_SIZE,
                -size,
                -size - CHARACTER_SIZE
        };

        return offsets;
    }

    private CharBuffer[] createBuffers() {
        CharBuffer[] buffers = new CharBuffer[NUMBER_OF_GRIDS];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = CharBuffer.allocate(size * size);
        }

        return buffers;
    }

    private void populateGrid(CharBuffer grid, List<char[]> rows) {
        for (char[] row : rows) {
            for (char symbol : row) {
                grid.put(symbol);
            }
        }
    }

    public void simulateGrowth(int numberOfMinutes) {
        int previousGrid;
        for (int minute = 0, currentGrid = 0, nextGrid = 1; minute < numberOfMinutes; minute++) {
            simulateNextGrowth(grids[currentGrid], grids[nextGrid]);

//            print(nextGrid);

            previousGrid = currentGrid;
            currentGrid = nextGrid;
            nextGrid = previousGrid;
        }
    }

    public int[] countByType(int gridIndex) {
        CharBuffer grid = grids[gridIndex];

        final int[] byType = new int['|' + 1];
        final int lastPosition = grid.position();

        grid.position(0);
        while (grid.position() < lastPosition) {
            byType[grid.get()]++;
        }

        return byType;
    }

    private void simulateNextGrowth(CharBuffer currentGrid, CharBuffer nextGrid) {
        final int lastPosition = currentGrid.position();

        currentGrid.position(0);
        nextGrid.position(0);

        while (currentGrid.position() < lastPosition) {
            countAdjacentTypes(currentGrid, byType);
            char currentSymbol = currentGrid.get();
            if (currentSymbol == OPEN) {
                nextGrid.put(byType[TREE] >= 3 ? TREE : OPEN);
            } else if (currentSymbol == TREE) {
                nextGrid.put(byType[LUMBER] >= 3 ? LUMBER : TREE);
            } else {
                nextGrid.put(byType[TREE] >= 1 && byType[LUMBER] >= 1 ? LUMBER : OPEN);
            }

//            nextGrid.put(TRANSITIONS.get(currentSymbol).apply(byType));
        }
    }

    private void countAdjacentTypes(CharBuffer grid, int[] numbersByType) {
        numbersByType[OPEN] = 0;
        numbersByType[TREE] = 0;
        numbersByType[LUMBER] = 0;

        final int position = grid.position();
        for (int offset : adjacentOffsets[position]) {
            numbersByType[grid.get(position + offset)]++;
        }
    }

    public void print(int gridId) {
        final char[] printBuffer = new char[size];

        CharBuffer grid = grids[gridId];
        grid.position(0);
        for (int i = 0; i < size; i++) {
            grid.get(printBuffer);
            System.out.println(Arrays.toString(printBuffer));
        }
        System.out.println("");
    }
}
