package pl.javanexus.day16;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Device {

    public static final int NUMBER_OF_REGISTERS = 4;

    enum InputType {
        FROM_REGISTER {
            @Override
            public int getInputValue(int input, int[] register) {
                return register[input];
            }
        },
        LITERAL_VALUE {
            @Override
            public int getInputValue(int input, int[] register) {
                return input;
            }
        };

        public abstract int getInputValue(int input, int[] register);
    }

    enum Opcode {
        ADDR((inputA, inputB) -> inputA + inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        ADDI((inputA, inputB) -> inputA + inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        MULR((inputA, inputB) -> inputA * inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        MULI((inputA, inputB) -> inputA * inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        BANR((inputA, inputB) -> inputA & inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        BANI((inputA, inputB) -> inputA & inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        BORR((inputA, inputB) -> inputA | inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        BORI((inputA, inputB) -> inputA | inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        SETR((inputA, inputB) -> inputA, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        SETI((inputA, inputB) -> inputA, InputType.LITERAL_VALUE, InputType.FROM_REGISTER),
        GTIR((inputA, inputB) -> inputA > inputB ? 1 : 0, InputType.LITERAL_VALUE, InputType.FROM_REGISTER),
        GTRI((inputA, inputB) -> inputA > inputB ? 1 : 0, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        GTRR((inputA, inputB) -> inputA > inputB ? 1 : 0, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        EQIR((inputA, inputB) -> inputA.equals(inputB) ? 1 : 0, InputType.LITERAL_VALUE, InputType.FROM_REGISTER),
        EQRI((inputA, inputB) -> inputA.equals(inputB) ? 1 : 0, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        EQRR((inputA, inputB) -> inputA.equals(inputB) ? 1 : 0, InputType.FROM_REGISTER, InputType.FROM_REGISTER);

        @Getter
        private final BiFunction<Integer, Integer, Integer> instruction;
        @Getter
        private final InputType inputAType;
        @Getter
        private final InputType inputBType;

        Opcode(BiFunction<Integer, Integer, Integer> instruction, InputType inputAType, InputType inputBType) {
            this.instruction = instruction;
            this.inputAType = inputAType;
            this.inputBType = inputBType;
        }
    }

    private final int[] registers = new int[NUMBER_OF_REGISTERS];

    public int countInputValuesMatchingMultipleOpcodes(List<DeviceInput> inputValues,
                                                       int minNumberOfMatchingOpcodes) {
        int numberOfInputValuesMatchingMultipleOpcodes = 0;
        for (DeviceInput input : inputValues) {
            if (countMatchingOpcodes(input) >= minNumberOfMatchingOpcodes) {
                numberOfInputValuesMatchingMultipleOpcodes++;
            }
        }

        return numberOfInputValuesMatchingMultipleOpcodes;
    }

    public int countMatchingOpcodes(DeviceInput input) {
        int numberOfMatchingOpcodes = 0;
        for (Opcode opcode : Opcode.values()) {
            int[] register = Arrays.copyOf(input.getInitialRegisterValues(), input.getInitialRegisterValues().length);
            executeOpcode(register, opcode, input.getInputA(), input.getInputB(), input.getResultRegisterIndex());
            if (input.isExpectedRegister(register)) {
                numberOfMatchingOpcodes++;
            }
        }

        return numberOfMatchingOpcodes;
    }

    public void executeOpcode(int[] registerValues, Opcode oppCode, int inputA, int inputB, int resultRegisterIndex) {
        int inputAValue = oppCode.getInputAType().getInputValue(inputA, registerValues);
        int inputBValue = oppCode.getInputBType().getInputValue(inputB, registerValues);
        registerValues[resultRegisterIndex] = oppCode.getInstruction().apply(inputAValue, inputBValue);
    }
}
