package pl.javanexus.year2019.day9;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SensorBoostTest {

    private InputReader inputReader;
    private DiagnosticProgram program;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
        this.program = new DiagnosticProgram();
    }

    @Test
    public void testRelativeMode() {
        DiagnosticProgram.State state = getState(new long[]{109, 2000, 204, -2000, 99});
        program.execute(state);
        assertEquals(109, state.getOutput());
    }

    @Test
    public void testSensorBoost() {
        DiagnosticProgram.State state = getState(new long[]{109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99});
        program.execute(state);
        assertEquals(99, state.getOutput());

        state = getState(new long[]{1102, 34915192, 34915192, 7, 4, 7, 99, 0});
        program.execute(state);
        assertEquals(1219070632396864L, state.getOutput());

        state = getState(new long[]{104, 1125899906842624L, 99});
        program.execute(state);
        assertEquals(1125899906842624L, state.getOutput());
    }

    @Test
    public void testSensorBootFromInput() throws IOException {
        long[] instructions = inputReader.readLongArray("year2019/day9/input1.csv", ",");
        DiagnosticProgram.State state = new DiagnosticProgram.State(1, instructions);
        program.execute(state);
        assertEquals(2351176124L, state.getOutput());
    }

    private DiagnosticProgram.State getState(long[] instructions) {
        return new DiagnosticProgram.State(instructions);
    }
}
