package pl.javanexus.year2019.day6;

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

    @Test
    public void testGetMinNumberOfOrbitalTransfers() throws IOException {
        OrbitMap orbitMap = getOrbitMap("year2019/day6/input_transfers.csv");
        assertEquals(4, orbitMap.getMinNumberOfOrbitalTransfers("YOU", "SAN"));

        orbitMap = getOrbitMap("year2019/day6/input1.csv");
        assertEquals(457, orbitMap.getMinNumberOfOrbitalTransfers("YOU", "SAN"));
    }

    private void testOrbitMap(String filePath, int expectedTotalNumberOfOrbits) throws IOException {
        OrbitMap orbitMap = getOrbitMap(filePath);
        assertEquals(expectedTotalNumberOfOrbits, orbitMap.getTotalNumberOfOrbits());
    }

    private OrbitMap getOrbitMap(String inputFilePath) throws IOException {
        List<String> input = inputReader.readStringValues(inputFilePath);

        OrbitMap orbitMap = new OrbitMap();
        orbitMap.readInput(input);

        return orbitMap;
    }
}
