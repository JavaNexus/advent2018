package pl.javanexus.year2019.day15;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

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
}
