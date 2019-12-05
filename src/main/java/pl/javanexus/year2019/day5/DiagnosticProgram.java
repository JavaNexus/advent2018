package pl.javanexus.year2019.day5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiagnosticProgram {

    public static final int MAX_VALUE = 99;

    enum ParameterMode {
        POSITION(0) {
            @Override
            int getArgument(int[] input, int opcodeIndex, int offset) {
                int argumentIndex = input[opcodeIndex + offset];
                return input[argumentIndex];
            }
        },
        IMMEDIATE(1) {
            @Override
            int getArgument(int[] input, int opcodeIndex, int offset) {
                return input[opcodeIndex + offset];
            }
        };

        private static final ParameterMode[] MODES = new ParameterMode[ParameterMode.values().length];
        static {
            for (ParameterMode mode : ParameterMode.values()) {
                MODES[mode.code] = mode;
            }
        }

        public static ParameterMode getParameterMode(int code) {
            if (code > MODES.length) {
                throw new RuntimeException("Unknown parameter mode");
            }
            return MODES[code];
        }

        private final int code;

        ParameterMode(int code) {
            this.code = code;
        }

        abstract int getArgument(int[] input, int opcodeIndex, int offset);
    }

    enum Instruction {
        ADD(1, 4) {
            @Override
            public void execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
                int result = getFirstArgument(instructions, opcodeIndex, parameterModes)
                        + getSecondArgument(instructions, opcodeIndex, parameterModes);
                setResult(instructions, opcodeIndex, result);
            }
        },
        MULTIPLY(2, 4) {
            @Override
            public void execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
                int result = getFirstArgument(instructions, opcodeIndex, parameterModes)
                        * getSecondArgument(instructions, opcodeIndex, parameterModes);
                setResult(instructions, opcodeIndex, result);
            }
        },
        INPUT(3, 2) {
            @Override
            void execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
                //getFirstArgument(instructions, opcodeIndex);
                //todo: add input array
                setResult(instructions, opcodeIndex, 1);
            }
        },
        OUTPUT(4, 2) {
            @Override
            void execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
                //todo: add output array
                System.out.println("OUTPUT: " + getFirstArgument(instructions, opcodeIndex, parameterModes));
            }
        },
        HALT(99, 0) {
            @Override
            public void execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
                //halt and catch fire
            }
        };

        private final int opcode;
        private final int step;

        Instruction(int opcode, int step) {
            this.opcode = opcode;
            this.step = step;
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

        abstract void execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes);

        int getFirstArgument(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
            return getArgument(instructions, opcodeIndex, parameterModes, 1);
        }

        int getSecondArgument(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes) {
            return getArgument(instructions, opcodeIndex, parameterModes, 2);
        }

        int getArgument(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, int argumentIndex) {
            ParameterMode parameterMode = getParameterMode(parameterModes, argumentIndex - 1);
            return parameterMode.getArgument(instructions, opcodeIndex, argumentIndex);
        }

        private ParameterMode getParameterMode(ParameterMode[] modes, int argumentIndex) {
            if (argumentIndex >= modes.length) {
                return ParameterMode.POSITION;
            }
            return modes[argumentIndex];
        }

        void setResult(int[] input, int opcodeIndex, int result) {
            int resultIndex = input[opcodeIndex + step - 1];
            input[resultIndex] = result;
        }

        public int getOpcode() {
            return opcode;
        }

        public int getStep() {
            return step;
        }
    }

    public void execute(int[] input) {

        int i = 0;
        Instruction instruction;
        do {
            int value = input[i];
            int opcode = value % 100;
            int parameterModes = value / 100;

            instruction = Instruction.getInstruction(opcode);
            instruction.execute(input, i, getParameterModes(parameterModes));
            i += instruction.getStep();
        } while (instruction != null && instruction != Instruction.HALT);
    }

    private ParameterMode[] getParameterModes(int parameterModes) {
        int numberOfDigits = getNumberOfDigits(parameterModes);
        int[] digits = getDigits(parameterModes, numberOfDigits);

        ParameterMode[] modes = new ParameterMode[digits.length];
        for (int i = 0; i < digits.length; i++) {
            modes[i] = ParameterMode.getParameterMode(digits[i]);
        }

        return modes;
    }

    private int[] getDigits(int value, int numberOfDigits) {
        int[] digits = new int[numberOfDigits];

        while (numberOfDigits > 0) {
            digits[digits.length - numberOfDigits] =  value % 10;
            value /= 10;
            numberOfDigits--;
        }

        return digits;
    }

    private int getNumberOfDigits(int value) {
        int numberOfDigits = 0;
        while (value > 0) {
            value /= 10;
            numberOfDigits++;
        }

        return numberOfDigits;
    }
}
