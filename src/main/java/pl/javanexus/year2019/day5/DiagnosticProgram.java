package pl.javanexus.year2019.day5;

import lombok.Data;
import lombok.Getter;

import java.util.*;

public class DiagnosticProgram {

    public static final int MAX_VALUE = 99;

    enum ParameterMode {
        POSITION(0) {
            @Override
            int getArgument(State state, int opcodeIndex, int offset) {
                int argumentIndex = state.getInstruction(opcodeIndex + offset);
                return state.getInstruction(argumentIndex);
            }
        },
        IMMEDIATE(1) {
            @Override
            int getArgument(State state, int opcodeIndex, int offset) {
                return state.getInstruction(opcodeIndex + offset);
            }
        },
        RELATIVE(2) {
            @Override
            int getArgument(State state, int opcodeIndex, int offset) {
                int relativeOffset = state.getInstruction(opcodeIndex + offset);
                return state.getInstruction(state.getRelativeBase() + relativeOffset);
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

        abstract int getArgument(State state, int opcodeIndex, int offset);
    }

    enum Instruction {
        ADD(1, 4) {
            @Override
            public int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int result = getFirstArgument(state, opcodeIndex, parameterModes)
                        + getSecondArgument(state, opcodeIndex, parameterModes);
                setResult(state.getInstructions(), opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        MULTIPLY(2, 4) {
            @Override
            public int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int result = getFirstArgument(state, opcodeIndex, parameterModes)
                        * getSecondArgument(state, opcodeIndex, parameterModes);
                setResult(state.getInstructions(), opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        INPUT(3, 2) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                if (state.isInputAvailable()) {
                    int input = state.getInput();
                    setResult(state.getInstructions(), opcodeIndex, input);
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
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int output = getFirstArgument(state, opcodeIndex, parameterModes);
                state.setOutput(output);
                System.out.println(" >: OUTPUT: " + output);

                return opcodeIndex + getStep();
            }
        },
        JUMP_IF_TRUE(5, 3) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                if (firstArgument != 0) {
                    return getSecondArgument(state, opcodeIndex, parameterModes);
                }
                return opcodeIndex + getStep();
            }
        },
        JUMP_IF_FALSE(6, 3) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                if (firstArgument == 0) {
                    return getSecondArgument(state, opcodeIndex, parameterModes);
                }
                return opcodeIndex + getStep();
            }
        },
        LESS_THAN(7, 4) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                int secondArgument = getSecondArgument(state, opcodeIndex, parameterModes);

                int result = firstArgument < secondArgument ? 1 : 0;
                setResult(state.getInstructions(), opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        EQUALS(8, 4) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                int secondArgument = getSecondArgument(state, opcodeIndex, parameterModes);

                int result = firstArgument == secondArgument ? 1 : 0;
                setResult(state.getInstructions(), opcodeIndex, result);

                return opcodeIndex + getStep();
            }
        },
        ADJUST_BASE(9, 2) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                int firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                state.adjustRelativeBase(firstArgument);

                return opcodeIndex + getStep();
            }
        },
        HALT(99, 0) {
            @Override
            public int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
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

        abstract int execute(State state, int opcodeIndex, ParameterMode[] parameterModes);

        int getFirstArgument(State state, int opcodeIndex, ParameterMode[] parameterModes) {
            return getArgument(state, opcodeIndex, parameterModes, 1);
        }

        int getSecondArgument(State state, int opcodeIndex, ParameterMode[] parameterModes) {
            return getArgument(state, opcodeIndex, parameterModes, 2);
        }

        int getArgument(State state, int opcodeIndex, ParameterMode[] parameterModes, int argumentIndex) {
            ParameterMode parameterMode = getParameterMode(parameterModes, argumentIndex - 1);
            return parameterMode.getArgument(state, opcodeIndex, argumentIndex);
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
        return execute(new State(input, originalInstructions));
    }

    public State execute(State state) {
        Instruction instruction;
        int instructionIndex = state.getInstructionIndex();
        int previousInstructionIndex;
        do {
            previousInstructionIndex = instructionIndex;

            int value = state.getInstruction(instructionIndex);
            int opcode = value % 100;
            int parameterModes = value / 100;

            instruction = Instruction.getInstruction(opcode);
            instructionIndex = instruction.execute(state, instructionIndex, getParameterModes(parameterModes));
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

        public static final int BUFFER_SIZE = 100000;

        private final int[] instructions;
        private final Queue<Integer> inputQueue;
        private int output;
        @Getter
        private int relativeBase = 0;

        private boolean isFinished = false;
        private boolean hasOutput = false;

        private int instructionIndex = 0;

        public State(int[] originalInstructions) {
            this.inputQueue = new LinkedList<>();
            this.instructions = new int[BUFFER_SIZE];
            Arrays.fill(instructions, 0);
            System.arraycopy(originalInstructions, 0, instructions, 0, originalInstructions.length);
        }

        public State(int phaseSetting, int[] originalInstructions) {
            this(originalInstructions);
            inputQueue.offer(phaseSetting);
        }

        public void setInstruction(int index, int value) {
            instructions[index] = value;
        }

        public int getInstruction(int index) {
            return instructions[index];
        }

        public boolean isInputAvailable() {
            return !inputQueue.isEmpty();
        }

        public int getInput() {
            return inputQueue.poll();
        }

        public void addInput(int input) {
            this.inputQueue.offer(input);
        }

        public void setOutput(int output) {
            this.output = output;
            this.hasOutput = true;
        }

        public void adjustRelativeBase(int value) {
            this.relativeBase += value;
        }
    }
}
