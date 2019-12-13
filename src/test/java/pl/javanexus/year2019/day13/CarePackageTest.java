package pl.javanexus.year2019.day13;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CarePackageTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testInitialState() throws IOException {
        long[] instructions = inputReader.readLongArray("year2019/day13/input1.csv", ",");
        CarePackage carePackage = new CarePackage(instructions);
        carePackage.executeProgram();

        int[] numberOfTiles = carePackage.countTiles();
        assertEquals(348, numberOfTiles[CarePackage.Tile.BLOCK.getId()]);
    }
}
