package pl.javanexus.year2019;

import com.google.common.collect.Collections2;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Amplifier {

    private final DiagnosticProgram program;
    public Amplifier() {
        this.program = new DiagnosticProgram();
    }

    public int execute(int[] instructions, int[] phaseSettings, int input) {
        for (int i = 0; i < phaseSettings.length; i++) {
            DiagnosticProgram.State state = program.execute(instructions, new int[]{phaseSettings[i], input});
            input = state.getOutput();
        }

        return input;
    }

    public int executeInFeedbackLoop(int[] instructions, int[] phaseSettings, int input) {
        DiagnosticProgram.State[] states = getInitialStates(instructions, phaseSettings, input);

        int loopIndex = 0;;
        int numberOfFinishedAmplifiers = 0;
        while (numberOfFinishedAmplifiers < phaseSettings.length) {
            System.out.println("LOOP:" + loopIndex);
            for (int i = 0; i < phaseSettings.length; i++) {
                System.out.println("STATE:" + i);
                DiagnosticProgram.State state = program.execute(states[i]);
                if (loopIndex > 0 || i + 1 >= phaseSettings.length) {
                    states[(i + 1) % phaseSettings.length].setInput(state.getOutput());
                } else {
                    states[(i + 1) % phaseSettings.length].updateInput(state.getOutput());
                }

                if (state.isFinished()) {
                    numberOfFinishedAmplifiers++;
                }
            }
            loopIndex++;
        }

        return states[phaseSettings.length - 1].getOutput();
    }

    private DiagnosticProgram.State[] getInitialStates(int[] instructions, int[] phaseSettings, int initialInput) {
        DiagnosticProgram.State[] states = new DiagnosticProgram.State[phaseSettings.length];
        for (int i = 0; i < states.length; i++) {
            states[i] = new DiagnosticProgram.State(new int[] {phaseSettings[i], initialInput}, instructions);
        }

        return states;
    }

    public int findHighestSignal(int[] instructions, int[] initialPhaseSettings, int input) {
        int maxOutputSignal = -1;
        for (List<Integer> permutation : getPermutation(initialPhaseSettings)) {
            int[] phaseSettings = permutation.stream().mapToInt(Integer::intValue).toArray();
            int signalOutput = executeInFeedbackLoop(instructions, phaseSettings, input);
            if (signalOutput > maxOutputSignal) {
                maxOutputSignal = signalOutput;
            }
        }

        return maxOutputSignal;
    }

    public Collection<List<Integer>> getPermutation(int[] input) {
        return Collections2.orderedPermutations(Arrays.stream(input)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toList()));
    }
}
