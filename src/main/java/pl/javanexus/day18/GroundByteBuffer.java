package pl.javanexus.day18;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GroundByteBuffer {

    public static final int NUMBER_OF_GRIDS = 2;

    public static final int CHARACTER_SIZE = 2;
    public static final char OPEN = '.';
    public static final char TREE = '|';
    public static final char LUMBER = '#';

    private CharBuffer[] grids;
    private final int size;
    final int[] byType = new int[TREE + 1];

    private static final Map<Character, Function<int[], Character>> TRANSITIONS = new HashMap<>();
    static {
        TRANSITIONS.put(OPEN,   (byType) -> byType[OPEN] >= 3 ? TREE : OPEN);
        TRANSITIONS.put(TREE,   (byType) -> byType[TREE] >= 3 ? LUMBER : TREE);
        TRANSITIONS.put(LUMBER, (byType) -> byType[TREE] >= 1 && byType[LUMBER] >= 1 ? LUMBER : OPEN);
    }

    public GroundByteBuffer(List<char[]> rows) {
        this.size = rows.size();
        this.grids = createBuffers();
        populateGrid(grids[0], rows);
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
        for (int minute = 0, currentGrid = 0, nextGrid = 1;
             minute < numberOfMinutes;
             minute++) {
            simulateNextGrowth(grids[currentGrid], grids[nextGrid]);

            previousGrid = currentGrid;
            currentGrid = nextGrid;
            nextGrid = previousGrid;
        }
    }

    public int[] countByType() {
        CharBuffer grid = grids[0];

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
//            countAdjacentTypes(currentGrid, byType);
            char currentSymbol = currentGrid.get();
            nextGrid.put(currentSymbol);
//
//            gridNextState.putChar(TRANSITIONS.get(currentSymbol).apply(byType));
        }

//        this.currentGrid = gridNextState;
    }

    private void countAdjacentTypes(CharBuffer grid, int[] numbersByType) {
        final int position = grid.position();
        final int[] adjacentIndexes = {
                position - size - CHARACTER_SIZE,   //[top][center]
                position - size,                    //[top][center]
                position - size + CHARACTER_SIZE,   //[top][center]
                position + size - CHARACTER_SIZE,   //[bottom][center]
                position + size,                    //[bottom][center]
                position + size + CHARACTER_SIZE,   //[bottom][center]
                position - CHARACTER_SIZE,          //[-][left]
                position + CHARACTER_SIZE           //[-][right]
        };
        for (int index : adjacentIndexes) {
            numbersByType[grid.get(index)]++;
        }
    }
}
