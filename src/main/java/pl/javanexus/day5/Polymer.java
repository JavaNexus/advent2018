package pl.javanexus.day5;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Polymer {

    public static final int DISTANCE_BETWEEN_CAPITAL_AND_SMALL_LETTER = 'a' - 'A';
    private final String polymer;

    public Polymer(String polymer) {
        this.polymer = polymer;
    }

    public int length() {
        return polymer.length();
    }

    public int getShortestPolymerLength() {
        return createPolymersWithoutSingleUnit().stream()
                .map(Polymer::reduce)
                .min(Comparator.comparingInt(length -> length))
                .get();
    }

    private List<Polymer> createPolymersWithoutSingleUnit() {
        List<Polymer> polymers = new LinkedList<>();

        for (char unit = 'a'; unit < 'z'; unit++) {
            polymers.add(createPolymerWithoutUnit(unit));
        }

        return polymers;
    }

    private Polymer createPolymerWithoutUnit(char unitToRemove) {
        if (unitToRemove < 'a') {
            throw new IllegalArgumentException("Expected small letter; received: " + unitToRemove);
        }
        int capitalUnitToRemove = unitToRemove - DISTANCE_BETWEEN_CAPITAL_AND_SMALL_LETTER;

        StringBuilder polymerBuilder = new StringBuilder();
        for (char unit : polymer.toCharArray()) {
            if (unit != unitToRemove && unit != capitalUnitToRemove) {
                polymerBuilder.append(unit);
            }
        }

        return new Polymer(polymerBuilder.toString());
    }

    public int reduce() {
        Stack<Character> reduced = new Stack<>();

        char[] units = polymer.toCharArray();
        for (int i = 0; i < units.length; i++) {
            while (!reduced.isEmpty() && i < units.length && willReact(reduced.peek(), units[i])) {
                reduced.pop();
                i++;
            }
            if (i < units.length) {
                reduced.push(units[i]);
            }
        }

        return reduced.size();
    }

    private boolean willReact(char current, char previous) {
        return Math.abs(current - previous) == DISTANCE_BETWEEN_CAPITAL_AND_SMALL_LETTER;
    }

    @Override
    public String toString() {
        return polymer;
    }
}
