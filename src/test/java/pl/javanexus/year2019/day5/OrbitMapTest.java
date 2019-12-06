package pl.javanexus.year2019.day5;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day6.OrbitMap;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrbitMapTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testGetTotalNumberOfOrbits() throws IOException {
        testOrbitMap("year2019/day6/input_small.csv", 42);
        testOrbitMap("year2019/day6/input1.csv", 295936);
    }

    private void testOrbitMap(String filePath, int expectedTotalNumberOfOrbits) throws IOException {
        List<String> input = inputReader.readStringValues(filePath);

        OrbitMap orbitMap = new OrbitMap();
        orbitMap.readInput(input);
        assertEquals(expectedTotalNumberOfOrbits, orbitMap.getTotalNumberOfOrbits());
    }
}
