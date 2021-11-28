package pl.javanexus.year2020.day8;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HandheldHalting {

    enum Operation {
        ACC("acc", (argument, context) -> {
            context.accumulate(argument);
        }, (argument) -> 1),
        JMP("jmp", (argument, context) -> {
            //no-op
        }, (argument) -> argument),
        NOP("nop", (argument, context) -> {
            //no-op
        }, (argument) -> 1),
        ;

        private static final Map<String, Operation> operations = Arrays.stream(Operation.values())
                .collect(Collectors.toMap(Operation::getOperationName, Function.identity()));

        @Getter
        private final String operationName;
        private final BiConsumer<Integer, Context> action;
        @Getter
        private final Function<Integer, Integer> nextInstructionOffsetProvider;

        Operation(String operation, BiConsumer<Integer, Context> action,
                  Function<Integer, Integer> nextInstructionOffsetProvider) {
            this.operationName = operation;
            this.action = action;
            this.nextInstructionOffsetProvider = nextInstructionOffsetProvider;
        }

        public void execute(int argument, Context context) {
            action.accept(argument, context);
        }

        public static Operation getOperation(String operationName) {
            return operations.get(operationName);
        }
    }

    private class Context {

        @Getter
        private int accumulator;
        @Getter
        private int nextInstruction;

        public void accumulate(int argument) {
            accumulator += argument;
        }

        public void updateNextInstructionIndex(int argument) {
            nextInstruction += argument;
        }
    }

    @Data
    private class Instruction {

        private final Operation operation;
        private final int argument;
        private final boolean changed;

        private int numberOfExecutions;

        public void execute(Context context) {
            numberOfExecutions++;
            operation.execute(argument, context);
            context.updateNextInstructionIndex(operation.getNextInstructionOffsetProvider().apply(argument));
        }

        public void revert(Context context) {
            operation.execute(-argument, context);
            context.updateNextInstructionIndex(-operation.getNextInstructionOffsetProvider().apply(argument));
        }

        public boolean hasBeenExecuted() {
            return numberOfExecutions > 0;
        }
    }

    private final Pattern instructionPattern = Pattern.compile("(acc|jmp|nop) ([\\-+0-9]+)");

    public int executeSingleLoop(List<String> lines) {
        final Context context = new Context();
        final Instruction[] instructions = parseInstructions(lines);

        Instruction instruction = instructions[context.getNextInstruction()];
        while (!instruction.hasBeenExecuted()) {
            instruction.execute(context);
            instruction = instructions[context.getNextInstruction()];
        }

        return context.getAccumulator();
    }

    public int findCorruptedInstruction(List<String> lines) {
        final Deque<Instruction> executed = new LinkedList<>();

        final Context context = new Context();
        final Instruction[] instructions = parseInstructions(lines);

        boolean hasChangedInstruction = false;

        Instruction instruction;
        do {
            instruction = instructions[context.getNextInstruction()];
            if (instruction.hasBeenExecuted()) {
                instruction =
                        changeOperation(getLastPossibleCorruptedInstruction(executed, context, hasChangedInstruction));
                hasChangedInstruction = true;
            }
            instruction.execute(context);
            executed.push(instruction);
        } while (context.getNextInstruction() < instructions.length);

        return context.getAccumulator();
    }

    private Instruction changeOperation(Instruction instruction) {
        if (instruction.getOperation() == Operation.NOP) {
            return new Instruction(Operation.JMP, instruction.getArgument(), true);
        } else if (instruction.getOperation() == Operation.JMP) {
            return new Instruction(Operation.NOP, instruction.getArgument(), true);
        } else {
            throw new IllegalArgumentException("Operation can't be converted: " + instruction.getOperation());
        }
    }

    private Instruction getLastPossibleCorruptedInstruction(Deque<Instruction> executed, Context context,
                                                            boolean hasChangedInstruction) {
        Instruction instruction;
        if (hasChangedInstruction) {
            do {
                instruction = executed.remove();
                instruction.revert(context);
            } while(!instruction.isChanged());
        }

        do {
            instruction = executed.remove();
            instruction.revert(context);
        } while(!isCorruptibleOperation(instruction.getOperation()));

        return instruction;
    }

    private boolean isCorruptibleOperation(Operation operation) {
        return operation == Operation.JMP || operation == Operation.NOP;
    }

    private Instruction[] parseInstructions(List<String> lines) {
        return lines.stream().map(instruction -> {
            Matcher matcher = instructionPattern.matcher(instruction);
            if (matcher.find()) {
                return new Instruction(
                        Operation.getOperation(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        false);
            }
            throw new IllegalArgumentException("Unknown instruction: " + instruction);
        }).toArray(Instruction[]::new);
    }
}
