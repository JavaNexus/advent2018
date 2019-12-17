package pl.javanexus.day17;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day17.Scaffold;

import java.io.IOException;

public class ScaffoldTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testPrintScaffold() throws IOException {
        long[] instructions =
                inputReader.readLongArray("year2019/day17/input1.csv", ",");
        Scaffold scaffold = new Scaffold();
        scaffold.readInput(instructions);
        scaffold.printGrid();

        int intersections = scaffold.countIntersections();
        System.out.println("intersections: " + intersections);
    }
}
