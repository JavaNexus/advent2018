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

    public long execute(long[] instructions, long[] phaseSettings, int initialInput) {
        DiagnosticProgram.State[] states = getInitialStates(instructions, phaseSettings);
        states[0].addInput(initialInput);

        for (int i = 0; i < phaseSettings.length; i++) {
            DiagnosticProgram.State state = program.execute(states[i]);
            states[(i + 1) % phaseSettings.length].addInput(state.getOutput());
        }

        return states[phaseSettings.length - 1].getOutput();
    }

    public long executeInFeedbackLoop(long[] instructions, long[] phaseSettings, long initialInput) {
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

    private DiagnosticProgram.State[] getInitialStates(long[] instructions, long[] phaseSettings) {
        DiagnosticProgram.State[] states = new DiagnosticProgram.State[phaseSettings.length];
        for (int i = 0; i < states.length; i++) {
            states[i] = new DiagnosticProgram.State(phaseSettings[i], instructions);
        }

        return states;
    }

    public long findHighestSignal(long[] instructions, long[] initialPhaseSettings, long input) {
        long maxOutputSignal = -1;
        for (List<Long> permutation : getPermutation(initialPhaseSettings)) {
            long[] phaseSettings = permutation.stream().mapToLong(Long::longValue).toArray();
            long signalOutput = executeInFeedbackLoop(instructions, phaseSettings, input);
            if (signalOutput > maxOutputSignal) {
                maxOutputSignal = signalOutput;
            }
        }

        return maxOutputSignal;
    }

    public Collection<List<Long>> getPermutation(long[] input) {
        return Collections2.orderedPermutations(Arrays.stream(input)
                .mapToObj(Long::valueOf)
                .collect(Collectors.toList()));
    }
}
