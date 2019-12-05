package pl.javanexus.year2019.day5;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;

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
        int[] operations = inputReader.readIntArray("year2019/day5/input1.csv");
        program.execute(operations);
    }

    @Test
    public void testOpcode() {
        int[] instructions = getInstructions("3,225,4,225,99");
        program.execute(instructions);
        assertEquals(1, instructions[225]);
    }

    @Test
    public void testParameterModes() {
        int[] instructions = getInstructions("1002,4,3,4,33,99");
        program.execute(instructions);
        assertEquals(99, instructions[4]);
    }

    private int[] getInstructions(String input) {
        int[] instructions = new int[1024];
        Arrays.fill(instructions, 0);

        int index = 0;
        for (String instruction : input.split(",")) {
            instructions[index++] = Integer.parseInt(instruction);
        }

        return instructions;
    }
}
