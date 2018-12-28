package pl.javanexus.day16;

import lombok.Data;

@Data
public class DeviceInput {

    private final int[] initialRegisterValues;
    private final int[] expectedRegisterValues;
    private final int opcodeNumber;
    private final int inputA;
    private final int inputB;
    private final int resultRegisterIndex;

    public DeviceInput(int[] initialRegisterValues, int[] opcode, int[] expectedRegisterValues) {
        this.initialRegisterValues = initialRegisterValues;
        this.expectedRegisterValues = expectedRegisterValues;
        this.opcodeNumber = opcode[InputParser.OPCODE_INDEX];
        this.inputA = opcode[InputParser.INPUT_A_INDEX];
        this.inputB = opcode[InputParser.INPUT_B_INDEX];
        this.resultRegisterIndex = opcode[InputParser.RESULT_INDEX];
    }

    public boolean isExpectedRegisterValue(int[] actualRegisterValues) {
        boolean isMatch = true;
        for (int i = 0; i < actualRegisterValues.length; i++) {
            isMatch &= (expectedRegisterValues[i] == actualRegisterValues[i]);
        }

        return isMatch;
    }
}
