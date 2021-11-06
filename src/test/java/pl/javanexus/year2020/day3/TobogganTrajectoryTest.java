package pl.javanexus.year2020.day3;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TobogganTrajectoryTest {

    private static final int[][] trajectories = {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        inputReader = new InputReader();
    }

    @Test
    public void shouldCountTrees() throws IOException {
        SlopeMap slopeMap = readMap("year2020/day3/input1.txt");

        TobogganTrajectory trajectory = new TobogganTrajectory();

        assertEquals(7, trajectory.countTreesAlongTrajectory(slopeMap, 3, 1));
        assertEquals(336, trajectory.countTreesAlongMultipleTrajectories(slopeMap, trajectories));
    }

    @Test
    public void shouldCountTreesFromFile() throws IOException {
        SlopeMap slopeMap = readMap("year2020/day3/input2.txt");

        TobogganTrajectory trajectory = new TobogganTrajectory();
        System.out.println(trajectory.countTreesAlongTrajectory(slopeMap, 3, 1));
        System.out.println(trajectory.countTreesAlongMultipleTrajectories(slopeMap, trajectories));
    }

    private SlopeMap readMap(String fileName) throws IOException {
        char[][] slope = inputReader.readValues(fileName, (index, line) -> line.toCharArray())
                .toArray(new char[][]{new char[0]});

        return new SlopeMap(slope);
    }
}
