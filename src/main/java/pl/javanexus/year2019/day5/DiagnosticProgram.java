package pl.javanexus.year2019.day5;

import lombok.Data;
import lombok.Getter;

import java.util.*;

public class DiagnosticProgram {

    private static void logCasting(long value) {
        if (value >= Integer.MAX_VALUE) {
            System.out.println(" >: CAST: " + value + " to: " + ((int) (value)));
        }
    }

    enum ParameterMode {
        POSITION(0) {
            @Override
            int getArgumentIndex(State state, int opcodeIndex, int offset) {
                long argumentIndex = state.getInstruction(opcodeIndex + offset);
                logCasting(argumentIndex);
                return (int)argumentIndex;
            }
        },
        IMMEDIATE(1) {
            @Override
            int getArgumentIndex(State state, int opcodeIndex, int offset) {
                return opcodeIndex + offset;
            }
        },
        RELATIVE(2) {
            @Override
            int getArgumentIndex(State state, int opcodeIndex, int offset) {
                long relativeOffset = state.getInstruction(opcodeIndex + offset);
                logCasting(state.getRelativeBase() + relativeOffset);
                return (int)(state.getRelativeBase() + relativeOffset);
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

        abstract int getArgumentIndex(State state, int opcodeIndex, int offset);
    }

    enum Instruction {
        ADD(1, 4) {
            @Override
            public int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long result = getFirstArgument(state, opcodeIndex, parameterModes)
                        + getSecondArgument(state, opcodeIndex, parameterModes);
                setResult(state, opcodeIndex, parameterModes, result);

                return opcodeIndex + getStep();
            }
        },
        MULTIPLY(2, 4) {
            @Override
            public int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long result = getFirstArgument(state, opcodeIndex, parameterModes)
                        * getSecondArgument(state, opcodeIndex, parameterModes);
                setResult(state, opcodeIndex, parameterModes, result);

                return opcodeIndex + getStep();
            }
        },
        INPUT(3, 2) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                if (state.isInputAvailable()) {
                    long input = state.getInput();
                    setResult(state, opcodeIndex, parameterModes, input);
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
                long output = getFirstArgument(state, opcodeIndex, parameterModes);
                state.setOutput(output);
                System.out.println(" >: OUTPUT: " + output);

                return opcodeIndex + getStep();
            }
        },
        JUMP_IF_TRUE(5, 3) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                if (firstArgument != 0) {
                    long secondArgument = getSecondArgument(state, opcodeIndex, parameterModes);
                    logCasting(secondArgument);
                    return (int) secondArgument;
                }
                return opcodeIndex + getStep();
            }
        },
        JUMP_IF_FALSE(6, 3) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                if (firstArgument == 0) {
                    long secondArgument = getSecondArgument(state, opcodeIndex, parameterModes);
                    logCasting(secondArgument);
                    return (int) secondArgument;
                }
                return opcodeIndex + getStep();
            }
        },
        LESS_THAN(7, 4) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                long secondArgument = getSecondArgument(state, opcodeIndex, parameterModes);

                int result = firstArgument < secondArgument ? 1 : 0;
                setResult(state, opcodeIndex, parameterModes, result);

                return opcodeIndex + getStep();
            }
        },
        EQUALS(8, 4) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
                long secondArgument = getSecondArgument(state, opcodeIndex, parameterModes);

                int result = firstArgument == secondArgument ? 1 : 0;
                setResult(state, opcodeIndex, parameterModes, result);

                return opcodeIndex + getStep();
            }
        },
        ADJUST_BASE(9, 2) {
            @Override
            int execute(State state, int opcodeIndex, ParameterMode[] parameterModes) {
                long firstArgument = getFirstArgument(state, opcodeIndex, parameterModes);
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

        long getFirstArgument(State state, int opcodeIndex, ParameterMode[] parameterModes) {
            return getArgument(state, opcodeIndex, parameterModes, 1);
        }

        long getSecondArgument(State state, int opcodeIndex, ParameterMode[] parameterModes) {
            return getArgument(state, opcodeIndex, parameterModes, 2);
        }

        long getArgument(State state, int opcodeIndex, ParameterMode[] parameterModes, int argumentIndex) {
            ParameterMode parameterMode = getParameterMode(parameterModes, argumentIndex - 1);
            return state.getInstruction(parameterMode.getArgumentIndex(state, opcodeIndex, argumentIndex));
        }

        private ParameterMode getParameterMode(ParameterMode[] modes, int argumentIndex) {
            if (argumentIndex >= modes.length) {
                return ParameterMode.POSITION;
            }
            return modes[argumentIndex];
        }

        void setResult(State state, int opcodeIndex, ParameterMode[] parameterModes, long result) {
            ParameterMode parameterMode = getParameterMode(parameterModes, step - 2);
            int resultIndex = parameterMode.getArgumentIndex(state, opcodeIndex, step - 1);
            state.setInstruction(resultIndex, result);
        }

        public int getOpcode() {
            return opcode;
        }

        public int getStep() {
            return step;
        }
    }

    public State execute(long[] originalInstructions, long input) {
        return execute(new State(input, originalInstructions));
    }

    public State execute(State state) {
        Instruction instruction;
        int instructionIndex = state.getInstructionIndex();
        int previousInstructionIndex;
        do {
            previousInstructionIndex = instructionIndex;

            long value = state.getInstruction(instructionIndex);
            logCasting(value % 100);
            int opcode = (int) (value % 100);
            long parameterModes = value / 100;

            instruction = Instruction.getInstruction(opcode);
            instructionIndex = instruction.execute(state, instructionIndex, getParameterModes(parameterModes));
        } while (instruction != null && instruction != Instruction.HALT && instructionIndex != previousInstructionIndex);
        state.setInstructionIndex(instructionIndex);

        return state;
    }

    private ParameterMode[] getParameterModes(long parameterModes) {
        int numberOfDigits = getNumberOfDigits(parameterModes);
        int[] digits = getDigits(parameterModes, numberOfDigits);

        ParameterMode[] modes = new ParameterMode[digits.length];
        for (int i = 0; i < digits.length; i++) {
            modes[i] = ParameterMode.getParameterMode(digits[i]);
        }

        return modes;
    }

    private int[] getDigits(long value, int numberOfDigits) {
        int[] digits = new int[numberOfDigits];

        while (numberOfDigits > 0) {
            digits[digits.length - numberOfDigits] = (int)(value % 10);
            value /= 10;
            numberOfDigits--;
        }

        return digits;
    }

    private int getNumberOfDigits(long value) {
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

        private final long[] instructions;
        private final Queue<Long> inputQueue;
        private final Queue<Long> outputQueue;
        @Getter
        private long relativeBase = 0;

        private boolean isFinished = false;

        private int instructionIndex = 0;

        public State(long[] originalInstructions) {
            this.inputQueue = new LinkedList<>();
            this.outputQueue = new LinkedList<>();
            this.instructions = new long[BUFFER_SIZE];
            Arrays.fill(instructions, 0);
            System.arraycopy(originalInstructions, 0, instructions, 0, originalInstructions.length);
        }

        public State(long input, long[] originalInstructions) {
            this(originalInstructions);
            inputQueue.offer(input);
        }

        public void setInstruction(int index, long value) {
            instructions[index] = value;
        }

        public long getInstruction(int index) {
            return instructions[index];
        }

        public boolean isInputAvailable() {
            return !inputQueue.isEmpty();
        }

        public long getInput() {
            return inputQueue.poll();
        }

        public void addInput(long input) {
            this.inputQueue.offer(input);
        }

        public long getOutput() {
            return outputQueue.poll();
        }

        public void setOutput(long output) {
            outputQueue.offer(output);
        }

        public long[] getRemainingOutput() {
            List<Long> output = new LinkedList<>();
            while (!outputQueue.isEmpty()) {
                output.add(outputQueue.poll());
            }

            return output.stream().mapToLong(Long::longValue).toArray();
        }

        public void adjustRelativeBase(long value) {
            this.relativeBase += value;
        }
    }
}
