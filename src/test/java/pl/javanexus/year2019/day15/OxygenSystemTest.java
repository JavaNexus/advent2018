package pl.javanexus.year2019.day15;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

public class OxygenSystemTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        inputReader = new InputReader();
    }

    @Test
    public void testFindOxygenTank() throws IOException {
        long[] instructions = inputReader.readLongArray("year2019/day15/input1.csv", ",");
        OxygenSystem oxygenSystem = new OxygenSystem();
        oxygenSystem.findOxygenTank(instructions);
    }

    @Test
    public void testFillOxygen() throws IOException {
        List<String> lines = inputReader.readStringValues("year2019/day15/map1.csv");

        OxygenSystem oxygenSystem = new OxygenSystem();
        oxygenSystem.populateGrid(getMap(lines));
        //...

        int time = oxygenSystem.fillWithOxygen(68, 68);
        System.out.println("Filled with oxygen in " + time + " seconds.");//313
    }

    private int[][] getMap(List<String> lines) {
        int[][] map = new int[lines.size()][];
        int y = 0;
        for (String line : lines) {
            String[] values = line.split("");
            map[y] = new int[values.length];
            for (int x = 0; x < values.length; x++) {
                map[y][x] = Integer.parseInt(values[x]);
            }
            y++;
        }
        return map;
    }
}
