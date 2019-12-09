package pl.javanexus.year2019.day9;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.math.BigInteger;

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
        DiagnosticProgram.State state = getState(new int[]{109, 2000, 204, -2000, 99});
        program.execute(state);
        assertEquals(109, state.getOutput());
    }

    @Test
    public void testSensorBoost() {

        DiagnosticProgram.State state = getState(new int[]{109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99});
        program.execute(state);
        System.out.println(state.getOutput());

        state = getState(new int[]{1102, 34915192, 34915192, 7, 4, 7, 99, 0});
        program.execute(state);
        System.out.println(state.getOutput());

//        System.out.println(new BigInteger("1125899906842624").add(BigInteger.TEN));
//        state = getState(new int[]{104, -1, 99});
//        program.execute(state);
//        System.out.println(state.getOutput());
    }

    private DiagnosticProgram.State getState(int[] instructions) {
        return new DiagnosticProgram.State(instructions);
    }
}
