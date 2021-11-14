package pl.javanexus.year2020.day8;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
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
            context.updateNextInstructionIndex(1);
        }),
        JMP("jmp", (argument, context) -> {
            context.updateNextInstructionIndex(argument);
        }),
        NOP("nop", (argument, context) -> {
            context.updateNextInstructionIndex(1);
        }),
        ;

        private static final Map<String, Operation> operations = Arrays.stream(Operation.values())
                .collect(Collectors.toMap(Operation::getOperationName, Function.identity()));

        @Getter
        private final String operationName;
        private final BiConsumer<Integer, Context> action;

        Operation(String operation, BiConsumer<Integer, Context> foo) {
            this.operationName = operation;
            this.action = foo;
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

        private int numberOfExecutions;

        public void execute(Context context) {
            numberOfExecutions++;
            operation.execute(argument, context);
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

    private Instruction[] parseInstructions(List<String> lines) {
        return lines.stream().map(instruction -> {
            Matcher matcher = instructionPattern.matcher(instruction);
            if (matcher.find()) {
                return new Instruction(Operation.getOperation(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            throw new IllegalArgumentException("Unknown instruction: " + instruction);
        }).toArray(size -> new Instruction[size]);
    }
}
