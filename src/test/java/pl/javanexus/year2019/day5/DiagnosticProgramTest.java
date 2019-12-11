package pl.javanexus.year2019.day5;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DiagnosticProgramTest {

    private InputReader inputReader;
    private DiagnosticProgram program;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
        this.program = new DiagnosticProgram();
    }

    @Test
    public void testInstruction() throws IOException {
        long[] operations = inputReader.readLongArray("year2019/day5/input1.csv", ",");
        DiagnosticProgram.State state = program.execute(operations, 1);

        assertArrayEquals(new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 15426686}, state.getRemainingOutput());

        state = program.execute(operations, 5);
        assertEquals(11430197, state.getOutput());
    }

    @Test
    public void testInputAndOutputOpcodes() {
        long[] instructions = getInstructions("3,225,4,225,99");
        DiagnosticProgram.State state = program.execute(instructions, 42);
        assertEquals(42, state.getOutput());
    }

    @Test
    public void testParameterModes() {
        long[] instructions = getInstructions("1002,4,3,4,33,99");
        DiagnosticProgram.State state = program.execute(instructions, 1);
        assertEquals(99, state.getInstruction(4));
    }

    @Test
    public void testLessThen() {
        testExecute("3,9,7,9,10,9,4,9,99,-1,8", 7, 1);
        testExecute("3,3,1107,-1,8,3,4,3,99", 7, 1);

        testExecute("3,9,7,9,10,9,4,9,99,-1,8", 8, 0);
        testExecute("3,3,1107,-1,8,3,4,3,99", 8, 0);

        testExecute("3,9,7,9,10,9,4,9,99,-1,8", 18, 0);
        testExecute("3,3,1107,-1,8,3,4,3,99", 18, 0);
    }

    @Test
    public void testEquals() {
        testExecute("3,9,8,9,10,9,4,9,99,-1,8", 8, 1);
        testExecute("3,9,8,9,10,9,4,9,99,-1,8", 18, 0);
        testExecute("3,9,8,9,10,9,4,9,99,-1,8", 88, 0);

        testExecute("3,3,1108,-1,8,3,4,3,99", 8, 1);
        testExecute("3,3,1108,-1,8,3,4,3,99", 18, 0);
        testExecute("3,3,1108,-1,8,3,4,3,99", 88, 0);
    }

    @Test
    public void testJump() {
        testExecute("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", 0, 0);
        testExecute("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", 9, 1);

        testExecute("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", 0, 0);
        testExecute("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", 9, 1);
    }

    @Test
    public void testCompleteProgram() {
        String values = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31," +
                "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104," +
                "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
        testExecute(values, 7, 999);
        testExecute(values, 8, 1000);
        testExecute(values, 9, 1001);
    }

    private void testExecute(String values, int input, int expectedOutput) {
        long[] instructions = getInstructions(values);
        assertEquals("Failed: " + values, expectedOutput, program.execute(instructions, input).getOutput());
    }

    private long[] getInstructions(String input) {
        long[] instructions = new long[1024];
        Arrays.fill(instructions, 0);

        int index = 0;
        for (String instruction : input.split(",")) {
            instructions[index++] = Integer.parseInt(instruction);
        }

        return instructions;
    }
}
