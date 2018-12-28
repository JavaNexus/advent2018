package pl.javanexus.day16;

import org.junit.Assert;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class OpcodeTest {

    @Test
    public void testCountMatchingOpcodes() throws IOException {
        final List<String> lines = new InputReader().readValues("day16_test_part1.input", (index, line) -> line);
        final List<DeviceInput> inputValues = new InputParser().parseInput(lines);

        final Device device = new Device();
        System.out.println(device.countInputValuesMatchingMultipleOpcodes(inputValues, 3));
    }

    @Test
    public void testMatch() {
        final DeviceInput deviceInput = DeviceInput.builder()
                .initialRegisterValues(new int[]{3, 2, 1, 1})
                .inputA(2)
                .inputB(1)
                .resultRegisterIndex(2)
                .expectedRegisterValues(new int[] {3, 2, 2, 1})
                .build();

        final Device device = new Device();
        assertEquals(3, device.countMatchingOpcodes(deviceInput));
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
}
