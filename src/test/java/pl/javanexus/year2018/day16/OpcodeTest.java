package pl.javanexus.year2018.day16;

import org.junit.Assert;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class OpcodeTest {

    public static final int MIN_NUMBER_OF_MATCHING_OPCODES = 3;
    public static final int REGISTER_SIZE = 4;

    @Test
    public void testExecuteProgram() throws IOException {
        final int[] register = new int[REGISTER_SIZE];
        final List<int[]> instructions = new InputReader()
                .readValues("day16_test_part2.input",
                        (index, line) -> Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray());

        final Device device = new Device();
        device.executeProgram(register, instructions);

        System.out.println(Arrays.toString(register));
    }

    @Test
    public void TestResolveOpcodeNumbers() throws IOException {
        final List<String> lines = new InputReader().readValues("day16_test_part1.input", (index, line) -> line);
        final List<DeviceInput> inputValues = new InputParser().parseInput(lines);

        final Device device = new Device();
        device.resolveOpcodeNumbers(inputValues);
    }

    @Test
    public void testCountInputValuesMatchingMultipleOpcodes() throws IOException {
        final List<String> lines = new InputReader().readValues("day16_test_part1.input", (index, line) -> line);
        final List<DeviceInput> inputValues = new InputParser().parseInput(lines);

        final Device device = new Device();
        System.out.println(device.countInputValuesMatchingMultipleOpcodes(inputValues, MIN_NUMBER_OF_MATCHING_OPCODES));
        //That's not the right answer; your answer is too low. (You guessed 528.)
    }

    @Test
    public void testCountMatchingOpcodes() {
        final Device device = new Device();
        DeviceInput deviceInput = new DeviceInput(new int[] {3, 2, 1, 1}, new int[] {9, 2, 1, 2}, new int[] {3, 2, 2, 1});
        assertEquals(3, device.getMatchingOpcodes(deviceInput).size());

        deviceInput = new DeviceInput(new int[] {3, 0, 0, 1}, new int[] {8, 2, 0, 1}, new int[] {3, 0, 0, 1});
        System.out.println(device.getMatchingOpcodes(deviceInput));
    }

    /**
     * Before: [3, 2, 1, 1]
     * 9 2 1 2
     * After:  [3, 2, 2, 1]
     */
    @Test
    public void testOpcodes() {
        final Device device = new Device();

        for (Device.Opcode opcode : new Device.Opcode[] {Device.Opcode.MULR, Device.Opcode.ADDI, Device.Opcode.SETI}) {
            int[] registerValues = {3, 2, 1, 1};
            device.executeOpcode(registerValues, opcode, 2, 1, 2);
            Assert.assertArrayEquals(new int[] {3, 2, 2, 1}, registerValues);
        }
    }

    @Test
    public void testAllOpcodes() {
        final Device device = new Device();

        DeviceInput input;
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 0, 3, 1}, new int[] {1, 5, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.ADDR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 0, 3, 1}, new int[] {1, 4, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.ADDI, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 2, 3, 1}, new int[] {1, 12, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.MULR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 2, 3, 1}, new int[] {1, 9, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.MULI, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 2, 1}, new int[] {1, 2, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.BANR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 4, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.BANI, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 2, 1}, new int[] {1, 3, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.BORR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 4, 1}, new int[] {1, 6, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.BORI, input));

        //set
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, -1, 3}, new int[] {1, 2, 3, 2});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.SETR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 9, -1, 1}, new int[] {1, 9, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.SETI, input));

        //greater than
        //value A is greater than register B ? 1 : 0
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 5, 3, 1}, new int[] {1, 1, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTIR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 4, 3, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTIR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 3, 3, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTIR, input));

        //register A is greater than value B ? 1 : 0
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 3, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTRI, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 2, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTRI, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 1, 1}, new int[] {1, 1, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTRI, input));

        //register A is greater than register B ? 1 : 0
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 2, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTRR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 2, 1, 1}, new int[] {1, 1, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTRR, input));

        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 2, 2, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.GTRR, input));

        //equal
        //value A is equal to register B
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 3, 2, 1}, new int[] {1, 1, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.EQIR, input));

        //register A is equal to value B
        input = new DeviceInput(new int[] {1, 2, 3, 4}, new int[] {-1, 1, 2, 1}, new int[] {1, 1, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.EQRI, input));

        //register A is equal to register B
        input = new DeviceInput(new int[] {1, 4, 3, 4}, new int[] {-1, 1, 3, 1}, new int[] {1, 1, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.EQRR, input));

        //register A is NOT equal to register B
        input = new DeviceInput(new int[] {1, 1, 3, 4}, new int[] {-1, 1, 3, 1}, new int[] {1, 0, 3, 4});
        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.EQRR, input));

//        input = new DeviceInput(new int[] {0, 0, 0, 4}, new int[] {-1, 1, 4, 1}, new int[] {1, 1, 3, 4});
//        assertArrayEquals(input.getExpectedRegisterValues(), device.executeOpcode(Device.Opcode.EQRR, input));
    }
}
