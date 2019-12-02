package pl.javanexus.year2019.day2;

import java.util.HashMap;
import java.util.Map;

public class IntcodeProgram {

    public static final int STEP = 4;
    public static final int MAX_VALUE = 99;

    enum Instruction {
        ADD(1) {
            @Override
            public void execute(int[] input, int opcodeIndex) {
                int result = getFirstArgument(input, opcodeIndex) + getSecondArgument(input, opcodeIndex);
                setResult(input, opcodeIndex, result);
            }
        },
        MULTIPLY(2) {
            @Override
            public void execute(int[] input, int opcodeIndex) {
                int result = getFirstArgument(input, opcodeIndex) * getSecondArgument(input, opcodeIndex);
                setResult(input, opcodeIndex, result);
            }
        },
        HALT(99) {
            @Override
            public void execute(int[] input, int opcodeIndex) {

            }
        };

        private final int opcode;

        Instruction(int opcode) {
            this.opcode = opcode;
        }

        private static final Map<Integer, Instruction> opcodesMap = new HashMap<>();
        static {
            for (Instruction instruction : Instruction.values()) {
                opcodesMap.put(instruction.getOpcode(), instruction);
            }
        }

        public static Instruction getInstruction(int opcode) {
            //TODO: get rid of autoboxing
            if (opcodesMap.containsKey(opcode)) {
                return opcodesMap.get(opcode);
            } else {
                throw new RuntimeException("Unknown operation code: " + opcode);
            }
        }

        abstract void execute(int[] input, int opcodeIndex);

        int getFirstArgument(int[] input, int opcodeIndex) {
            return getArgument(input, opcodeIndex, 1);
        }

        int getSecondArgument(int[] input, int opcodeIndex) {
            return getArgument(input, opcodeIndex, 2);
        }

        int getArgument(int[] input, int opcodeIndex, int offset) {
            int argumentIndex = input[opcodeIndex + offset];
            return input[argumentIndex];
        }

        void setResult(int[] input, int opcodeIndex, int result) {
            int resultIndex = input[opcodeIndex + 3];
            input[resultIndex] = result;
        }

        public int getOpcode() {
            return opcode;
        }
    }

    public void execute(int[] input) {

        int i = 0;
        Instruction instruction;
        do {
            int currentOpcode = input[i];
            instruction = Instruction.getInstruction(currentOpcode);
            instruction.execute(input, i);
            i += STEP;
        } while (instruction != null && instruction != Instruction.HALT);
    }

    public int findInput(int[] input, int expectedOutput) {
        final int[] output = new int[input.length];

        for (int noun = 0; noun < MAX_VALUE; noun++) {
            for (int verb = 0; verb < MAX_VALUE; verb++) {
                resetOutput(input, output, noun, verb);
                execute(output);
                if (output[0] == expectedOutput) {
                    return 100 * noun + verb;
                }
            }
        }

        throw new RuntimeException("Could not find noun and verb that produces output: " + expectedOutput);
    }

    public void resetOutput(int[] input, int[] output, int noun, int verb) {
        System.arraycopy(input, 0, output, 0, input.length);
        output[1] = noun;
        output[2] = verb;
    }
}
