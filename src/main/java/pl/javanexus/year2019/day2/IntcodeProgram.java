package pl.javanexus.year2019.day2;

import java.util.HashMap;
import java.util.Map;

public class IntcodeProgram {

    public static final int STEP = 4;

    enum Operation {
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

        Operation(int opcode) {
            this.opcode = opcode;
        }

        private static final Map<Integer, Operation> opcodesMap = new HashMap<>();
        static {
            for (Operation operation : Operation.values()) {
                opcodesMap.put(operation.getOpcode(), operation);
            }
        }

        public static Operation getOperation(int code) {
            //TODO: get rid of autoboxing
            if (opcodesMap.containsKey(code)) {
                return opcodesMap.get(code);
            } else {
                throw new RuntimeException("Unknown operation code: " + code);
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
        Operation operation;
        do {
            int currentOpcode = input[i];
            operation = Operation.getOperation(currentOpcode);
            operation.execute(input, i);
            i += STEP;
        } while (operation != null && operation != Operation.HALT);
    }
}
