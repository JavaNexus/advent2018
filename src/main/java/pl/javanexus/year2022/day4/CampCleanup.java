package pl.javanexus.year2022.day4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CampCleanup {

    private static final Pattern assignmentPattern = Pattern.compile("(\\d+)-(\\d+)");

    public int countFullyOverlappingSections(Stream<String> assignments) {
        return assignments
                .map(this::parseAssignmentPair)
                .map(Pair::areFullyOverlapping)
                .mapToInt(result -> result ? 1 : 0)
                .sum();
    }

    public int countOverlappingSections(Stream<String> assignments) {
        return assignments
                .map(this::parseAssignmentPair)
                .map(Pair::areOverlapping)
                .mapToInt(result -> result ? 1 : 0)
                .sum();
    }

    private Pair parseAssignmentPair(String assignmentPair) {
        String[] assignments = assignmentPair.split(",");
        if (assignments.length != 2) {
            throw new IllegalArgumentException("Unexpected assignment format: " + assignmentPair);
        }

        return new Pair(parseAssignment(assignments[0]), parseAssignment(assignments[1]));
    }

    private Assignment parseAssignment(String assignment) {
        Matcher matcher = assignmentPattern.matcher(assignment);
        if (matcher.find()) {
            return new Assignment(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        } else {
            throw new IllegalArgumentException("Unexpected assignment format: " + assignment);
        }
    }

    public static class Pair {
        private final Assignment left;
        private final Assignment right;

        public Pair(Assignment left, Assignment right) {
            this.left = left;
            this.right = right;
        }

        public boolean areFullyOverlapping() {
            return left.isFullyOverlapping(right) || right.isFullyOverlapping(left);
        }

        public boolean areOverlapping() {
            return left.isOverlapping(right) || right.isOverlapping(left);
        }
    }

    public static class Assignment {
        private final int from;
        private final int to;

        public Assignment(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public boolean isFullyOverlapping(Assignment assignment) {
            return this.from <= assignment.from && this.to >= assignment.to;
        }

        public boolean isOverlapping(Assignment assignment) {
            if (this.from < assignment.from) {
                return !(this.to < assignment.from);
            } else if (this.from > assignment.from) {
                return !(this.to > assignment.to);
            }
            return true;
        }
    }
}
