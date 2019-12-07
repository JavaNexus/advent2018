package pl.javanexus.year2019.day5;

import lombok.Data;

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
            public int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int result = getFirstArgument(instructions, opcodeIndex, parameterModes)
                        + getSecondArgument(instructions, opcodeIndex, parameterModes);
                setResult(instructions, opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        MULTIPLY(2, 4) {
            @Override
            public int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int result = getFirstArgument(instructions, opcodeIndex, parameterModes)
                        * getSecondArgument(instructions, opcodeIndex, parameterModes);
                setResult(instructions, opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        INPUT(3, 2) {
            @Override
            int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                if (state.isInputAvailable()) {
                    int input = state.getInput();
                    setResult(instructions, opcodeIndex, input);
                    System.out.println(" >: INPUT: " + input);

                    return opcodeIndex + getStep();
                } else {
                    //TODO: if no input was read then return opcodeIndex;
                    return opcodeIndex;
                }
            }
        },
        OUTPUT(4, 2) {
            @Override
            int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int output = getFirstArgument(instructions, opcodeIndex, parameterModes);
                state.setOutput(output);
                System.out.println(" >: OUTPUT: " + output);

                return opcodeIndex + getStep();
            }
        },
        JUMP_IF_TRUE(5, 3) {
            @Override
            int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int firstArgument = getFirstArgument(instructions, opcodeIndex, parameterModes);
                if (firstArgument != 0) {
                    return getSecondArgument(instructions, opcodeIndex, parameterModes);
                }
                return opcodeIndex + getStep();
            }
        },
        JUMP_IF_FALSE(6, 3) {
            @Override
            int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int firstArgument = getFirstArgument(instructions, opcodeIndex, parameterModes);
                if (firstArgument == 0) {
                    return getSecondArgument(instructions, opcodeIndex, parameterModes);
                }
                return opcodeIndex + getStep();
            }
        },
        LESS_THAN(7, 4) {
            @Override
            int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int firstArgument = getFirstArgument(instructions, opcodeIndex, parameterModes);
                int secondArgument = getSecondArgument(instructions, opcodeIndex, parameterModes);

                int result = firstArgument < secondArgument ? 1 : 0;
                setResult(instructions, opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        EQUALS(8, 4) {
            @Override
            int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                int firstArgument = getFirstArgument(instructions, opcodeIndex, parameterModes);
                int secondArgument = getSecondArgument(instructions, opcodeIndex, parameterModes);

                int result = firstArgument == secondArgument ? 1 : 0;
                setResult(instructions, opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        HALT(99, 0) {
            @Override
            public int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state) {
                //halt and catch fire
                state.setFinished(true);

                return opcodeIndex + getStep();
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

        abstract int execute(int[] instructions, int opcodeIndex, ParameterMode[] parameterModes, State state);

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

    public State execute(int[] originalInstructions, int input) {
        return execute(originalInstructions, new int[] {input});
    }

    public State execute(int[] originalInstructions, int[] input) {
        return execute(new State(input, originalInstructions));
    }

    public State execute(State state) {
        int[] instructions = state.getInstructions();

        Instruction instruction;
        int instructionIndex = state.getInstructionIndex();
        int previousInstructionIndex;
        do {
            previousInstructionIndex = instructionIndex;

            int value = instructions[instructionIndex];
            int opcode = value % 100;
            int parameterModes = value / 100;

            instruction = Instruction.getInstruction(opcode);
            instructionIndex = instruction.execute(instructions, instructionIndex, getParameterModes(parameterModes), state);
        } while (instruction != null && instruction != Instruction.HALT && instructionIndex != previousInstructionIndex);
        state.setInstructionIndex(instructionIndex);

        return state;
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

    @Data
    public static class State {

        private final int[] instructions;
        private int[] input;
        private int output;

        private boolean isFinished = false;
        private boolean hasOutput = false;

        private int instructionIndex = 0;
        private int inputIndex = 0;

        public State(int[] input, int[] originalInstructions) {
            this.input = input;
            this.instructions = new int[originalInstructions.length];
            System.arraycopy(originalInstructions, 0, instructions, 0, originalInstructions.length);
        }

        public int getInstruction(int index) {
            return instructions[index];
        }

        public boolean isInputAvailable() {
            return inputIndex < input.length;
        }

        public int getInput() {
            return input[inputIndex++];
        }

        public void setInput(int input) {
            this.input = new int[] {input};
            this.inputIndex = 0;
        }

        public void updateInput(int input) {
            this.input[1] = input;
        }

        public void setOutput(int output) {
            this.output = output;
            this.hasOutput = true;
        }
    }
}
