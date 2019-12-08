package pl.javanexus.year2019.day7;

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

    public int execute(int[] instructions, int[] phaseSettings, int initialInput) {
        DiagnosticProgram.State[] states = getInitialStates(instructions, phaseSettings);
        states[0].addInput(initialInput);

        for (int i = 0; i < phaseSettings.length; i++) {
            DiagnosticProgram.State state = program.execute(states[i]);
            states[(i + 1) % phaseSettings.length].addInput(state.getOutput());
        }

        return states[phaseSettings.length - 1].getOutput();
    }

    public int executeInFeedbackLoop(int[] instructions, int[] phaseSettings, int initialInput) {
        DiagnosticProgram.State[] states = getInitialStates(instructions, phaseSettings);
        states[0].addInput(initialInput);

        int numberOfFinishedAmplifiers = 0;
        while (numberOfFinishedAmplifiers < phaseSettings.length) {
            for (int i = 0; i < phaseSettings.length; i++) {
                DiagnosticProgram.State state = program.execute(states[i]);
                states[(i + 1) % phaseSettings.length].addInput(state.getOutput());

                if (state.isFinished()) {
                    numberOfFinishedAmplifiers++;
                }
            }
        }

        return states[phaseSettings.length - 1].getOutput();
    }

    private DiagnosticProgram.State[] getInitialStates(int[] instructions, int[] phaseSettings) {
        DiagnosticProgram.State[] states = new DiagnosticProgram.State[phaseSettings.length];
        for (int i = 0; i < states.length; i++) {
            states[i] = new DiagnosticProgram.State(phaseSettings[i], instructions);
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
