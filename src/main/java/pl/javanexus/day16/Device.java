package pl.javanexus.day16;

import lombok.Getter;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
        },
        IGNORE {
            @Override
            public int getInputValue(int input, int[] register) {
                return -1;
            }
        };

        public abstract int getInputValue(int input, int[] register);
    }

    enum Opcode {
        ADDR(0, (inputA, inputB) -> inputA + inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        ADDI(5, (inputA, inputB) -> inputA + inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        MULR(14, (inputA, inputB) -> inputA * inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        MULI(9, (inputA, inputB) -> inputA * inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        BANR(6, (inputA, inputB) -> inputA & inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        BANI(15, (inputA, inputB) -> inputA & inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        BORR(13, (inputA, inputB) -> inputA | inputB, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        BORI(8, (inputA, inputB) -> inputA | inputB, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        SETR(12, (inputA, inputB) -> inputA, InputType.FROM_REGISTER, InputType.IGNORE),
        SETI(10, (inputA, inputB) -> inputA, InputType.LITERAL_VALUE, InputType.IGNORE),
        GTIR(4, (inputA, inputB) -> inputA > inputB ? 1 : 0, InputType.LITERAL_VALUE, InputType.FROM_REGISTER),
        GTRI(7, (inputA, inputB) -> inputA > inputB ? 1 : 0, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        GTRR(11, (inputA, inputB) -> inputA > inputB ? 1 : 0, InputType.FROM_REGISTER, InputType.FROM_REGISTER),
        EQIR(2, (inputA, inputB) -> inputA.equals(inputB) ? 1 : 0, InputType.LITERAL_VALUE, InputType.FROM_REGISTER),
        EQRI(1, (inputA, inputB) -> inputA.equals(inputB) ? 1 : 0, InputType.FROM_REGISTER, InputType.LITERAL_VALUE),
        EQRR(3, (inputA, inputB) -> inputA.equals(inputB) ? 1 : 0, InputType.FROM_REGISTER, InputType.FROM_REGISTER);

        @Getter
        private final int opcodeNumber;
        @Getter
        private final BiFunction<Integer, Integer, Integer> instruction;
        @Getter
        private final InputType inputAType;
        @Getter
        private final InputType inputBType;

        Opcode(int opcodeNumber, BiFunction<Integer, Integer, Integer> instruction, InputType inputAType, InputType inputBType) {
            this.opcodeNumber = opcodeNumber;
            this.instruction = instruction;
            this.inputAType = inputAType;
            this.inputBType = inputBType;
        }

        private static final Map<Integer, Opcode> NUMBER_TO_OPCODE_MAPPING = new HashMap<>();
        static {
            for (Opcode opcode : values()) {
                NUMBER_TO_OPCODE_MAPPING.put(opcode.getOpcodeNumber(), opcode);
            }
        }

        public static Opcode getByNumber(int opcodeNumber) {
            return NUMBER_TO_OPCODE_MAPPING.get(opcodeNumber);
        }
    }

    public void executeProgram(int[] register, List<int[]> instructions) {
        for (int[] instruction : instructions) {
            Opcode opcode = Opcode.getByNumber(instruction[InputParser.OPCODE_INDEX]);
            executeOpcode(register, opcode,
                    instruction[InputParser.INPUT_A_INDEX],
                    instruction[InputParser.INPUT_B_INDEX],
                    instruction[InputParser.RESULT_INDEX]);
        }
    }

    public void resolveOpcodeNumbers(List<DeviceInput> inputValues) {
        matchNumbersWithOpcodes(inputValues);
    }

    private Map<Integer, Set<Opcode>> matchNumbersWithOpcodes(List<DeviceInput> inputValues) {
        Map<Integer, Set<Opcode>> opcodeNumbersMapping = new HashMap<>();
        for (DeviceInput input : inputValues) {
            opcodeNumbersMapping.compute(input.getOpcodeNumber(), (opcodeNumber, allMatchingOpcodes) -> {
                Set<Opcode> opcodes = getMatchingOpcodes(input);
                if (allMatchingOpcodes == null) {
                    allMatchingOpcodes = new HashSet<>();
                    allMatchingOpcodes.addAll(opcodes);
                } else {
                    allMatchingOpcodes = opcodes.stream()
                            .filter(allMatchingOpcodes::contains)
                            .collect(Collectors.toSet());
                }

                return allMatchingOpcodes;
            });
        }

        return opcodeNumbersMapping;
    }

    public int countInputValuesMatchingMultipleOpcodes(List<DeviceInput> inputValues,
                                                       int minNumberOfMatchingOpcodes) {
        int numberOfInputValuesMatchingMultipleOpcodes = 0;
        for (DeviceInput input : inputValues) {
            if (getMatchingOpcodes(input).size() >= minNumberOfMatchingOpcodes) {
                numberOfInputValuesMatchingMultipleOpcodes++;
            }
        }

        return numberOfInputValuesMatchingMultipleOpcodes;
    }

    public Set<Opcode> getMatchingOpcodes(DeviceInput input) {
        Set<Opcode> matchingOpcodes = new HashSet<>();
        for (Opcode opcode : Opcode.values()) {
            int[] register = Arrays.copyOf(input.getInitialRegisterValues(), input.getInitialRegisterValues().length);
            executeOpcode(register, opcode, input.getInputA(), input.getInputB(), input.getResultRegisterIndex());
            if (input.isExpectedRegisterValue(register)) {
                matchingOpcodes.add(opcode);
            }
        }

        return matchingOpcodes;
    }

    public int[] executeOpcode(Opcode opcode, DeviceInput input) {
        int[] register = input.getInitialRegisterValues();
        executeOpcode(register, opcode, input.getInputA(), input.getInputB(), input.getResultRegisterIndex());

        return register;
    }

    public void executeOpcode(int[] registerValues, Opcode opcode, int inputA, int inputB, int resultRegisterIndex) {
        int inputAValue = opcode.getInputAType().getInputValue(inputA, registerValues);
        int inputBValue = opcode.getInputBType().getInputValue(inputB, registerValues);
        registerValues[resultRegisterIndex] = opcode.getInstruction().apply(inputAValue, inputBValue);
    }
}
