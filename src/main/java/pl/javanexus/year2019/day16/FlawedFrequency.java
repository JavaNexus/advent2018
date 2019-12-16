package pl.javanexus.year2019.day16;

import lombok.Getter;

import java.util.Arrays;

public class FlawedFrequency {

    private final int[] pattern = {0, 1, 0, -1};
    @Getter
    private int[] currentPhase;

    public FlawedFrequency(int[] inputPhase) {
        this(inputPhase, 1);
    }

    public FlawedFrequency(int[] inputPhase, int repeat) {
        this.currentPhase = new int[inputPhase.length * repeat];
        for (int i = 0; i < currentPhase.length; i+= inputPhase.length) {
            System.arraycopy(inputPhase, 0, currentPhase, i, inputPhase.length);
        }
    }

    public void calculateNextPhases(int numberOfPhases) {
        while (numberOfPhases > 0) {
            System.out.println("Phase: " + numberOfPhases);
            calculateNextPhase();
            numberOfPhases--;
        }
    }

    public void calculateNextPhase() {
        int[] nextPhase = new int[currentPhase.length];
        for (int i = 0; i < nextPhase.length; i++) {
            nextPhase[i] = calculateElement(i);
        }
        this.currentPhase = nextPhase;
    }

    private int calculateElement(int index) {
        int element = 0;
        for (int i = 0; i < currentPhase.length; i++) {
            element += pattern[Math.floorDiv(i + 1, index + 1) % pattern.length] * currentPhase[i];
        }

        return Math.abs(element % 10);
    }
}
