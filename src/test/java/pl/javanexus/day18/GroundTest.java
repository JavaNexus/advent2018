package pl.javanexus.day18;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.grid.GridFactory;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class GroundTest {

    public static final int NUMBER_OF_MINUTES_PART_1 = 10;
    public static final int NUMBER_OF_MINUTES_PART_2 = 1000000000;
    public static final int ONE_MILLION = 1 * 1000000;
    public static final int TEN_MILLION = 10 * 1000000;

    private GridFactory<Ground, Acre> gridFactory;

    @Before
    public void setUp() throws Exception {
        this.gridFactory = new GridFactory<Ground, Acre>() {
            @Override
            public Acre createPoint(int x, int y, char symbol) {
                return new Acre(x, y, AcreType.getBySymbol(symbol));
            }

            @Override
            public Acre[] createRow(int length) {
                return new Acre[length];
            }

            @Override
            public Ground createGrid(List<Acre[]> tiles) {
                return new Ground(tiles.toArray(new Acre[tiles.size()][]));
            }
        };
    }

    @Test
    public void testPart2() throws IOException {
        String fileName = "day18/day18_test.input";//day18_simple
        List<char[]> rows =
                new InputReader().readValues(fileName, (index, line) -> line.toCharArray());
        GroundByteBuffer ground = new GroundByteBuffer(rows);
        ground.print(0);

        ground.simulateGrowth(NUMBER_OF_MINUTES_PART_2);

        System.out.println();
        ground.print(0);

        printResult(ground.countByType(0));//212176
        printResult(ground.countByType(1));
    }

    private void printResult(int[] byType) {
        System.out.println(" . " + byType['.']);
        System.out.println(" | " + byType['|']);
        System.out.println(" # " + byType['#']);
    }

    @Test
    public void testSimulateGrowth() throws Exception {
        Ground grid = gridFactory.createGridFromInputFile("day18/day18_simple.input");
        grid.print();

        grid.simulateGrowth(NUMBER_OF_MINUTES_PART_1);
        grid.print();

        Map<AcreType, Integer> numberOfAcresByType = grid.countByType();
        assertEquals(1147,
                numberOfAcresByType.get(AcreType.LUMBERYARD) * numberOfAcresByType.get(AcreType.TREES));
    }

    @Test
    public void testSimulateGrowthTestInput() throws Exception {
        Ground grid = gridFactory.createGridFromInputFile("day18/day18_test.input");
        grid.print();

        grid.simulateGrowth(NUMBER_OF_MINUTES_PART_2);
        grid.print();

        Map<AcreType, Integer> numberOfAcresByType = grid.countByType();
        System.out.println(numberOfAcresByType);
        System.out.println(numberOfAcresByType.get(AcreType.LUMBERYARD) * numberOfAcresByType.get(AcreType.TREES));
    }

    @Test
    public void testGetAdjacentPoints() throws IOException {
        Ground grid = gridFactory.createGridFromInputFile("day18/day18_simple.input");
        assertEquals(3, grid.getAdjacentPoints(grid.getPoint(0, 0).get()).size());
    }

    @Test
    public void testNextAcreType() {
        testNextAcreType(AcreType.OPEN, AcreType.OPEN,
                AcreType.LUMBERYARD, AcreType.LUMBERYARD, AcreType.TREES);
        testNextAcreType(AcreType.TREES, AcreType.LUMBERYARD,
                AcreType.LUMBERYARD, AcreType.LUMBERYARD, AcreType.LUMBERYARD, AcreType.OPEN, AcreType.OPEN);
        testNextAcreType(AcreType.TREES, AcreType.LUMBERYARD,
                AcreType.LUMBERYARD, AcreType.LUMBERYARD, AcreType.LUMBERYARD, AcreType.TREES, AcreType.OPEN);
    }

    private void testNextAcreType(AcreType currentType, AcreType expectedType, AcreType... adjacent) {
        List<AcreType> adjacentTypes = Stream.of(adjacent).collect(Collectors.toList());
        assertEquals(expectedType, currentType.getNextGroundType(adjacentTypes));
    }
}
