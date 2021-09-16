package pl.javanexus.year2018.day19;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2018.day16.Device;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointerTest {

    public static final int REGISTER_SIZE = 6;
    public static final Pattern PATTERN = Pattern.compile("^([a-z]+) ([ 0-9]+)");

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testInstructionPointer() throws IOException {
        int instructionPointer = 0;
        int[] register = new int[REGISTER_SIZE];
        int[][] instructions = parseInstructions("day19/day19_simple.input");

        new Device().executeProgram(instructionPointer, register, instructions);
        Assert.assertArrayEquals(new int[] {6, 5, 6, 0, 0, 9}, register);
    }

    @Test
    public void testInstructionPointerFromInput() throws Exception {
        int instructionPointer = 3;
        int[] register = new int[REGISTER_SIZE];
        register[0] = 1;
        int[][] instructions = parseInstructions("day19/day19_test.input");

        new Device().executeProgram(instructionPointer, register, instructions);
        System.out.println(Arrays.toString(register));
        //256 is too low
        //[1056, 1, 990, 256, 989, 990]
    }

    private int[][] parseInstructions(String fileName) throws IOException {
        final List<int[]> instructions = inputReader.readValues(fileName,
                (index, line) -> {
                    Matcher matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        Device.Opcode opcode = Device.Opcode.valueOf(matcher.group(1).toUpperCase());
                        int[] instruction = new int[4];
                        instruction[0] = opcode.getOpcodeNumber();

                        String[] values = matcher.group(2).split(" ");
                        for (int i = 0; i < values.length; i++) {
                            instruction[i + 1] = Integer.parseInt(values[i]);
                        }

                        return instruction;
                    } else {
                        throw new IllegalArgumentException("Unexpected instruction: " + line);
                    }
                });
        return instructions.toArray(new int[instructions.size()][]);
    }
}
