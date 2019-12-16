package pl.javanexus.year2019.day16;

import lombok.Getter;

public class FlawedFrequency {

    private final int[] pattern = {0, 1, 0, -1};
    @Getter
    private int[] currentPhase;

    public FlawedFrequency(int[] inputPhase) {
        this.currentPhase = inputPhase;
    }

    public void calculateNextPhases(int numberOfPhases) {
        while (numberOfPhases > 0) {
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
