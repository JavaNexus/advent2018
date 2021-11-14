package pl.javanexus.year2020.day8;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HandheldHaltingTest {

    private InputReader inputReader;
    private HandheldHalting handheldHalting;

    @Before
    public void setUp() throws Exception {
        inputReader = new InputReader();
        handheldHalting = new HandheldHalting();
    }

    @Test
    public void shouldExecuteInstructions() throws IOException {
        assertEquals(5,
                handheldHalting.executeSingleLoop(inputReader.readStringValues("year2020/day8/input1.txt")));
        assertEquals(1939,
                handheldHalting.executeSingleLoop(inputReader.readStringValues("year2020/day8/input2.txt")));
    }
}
