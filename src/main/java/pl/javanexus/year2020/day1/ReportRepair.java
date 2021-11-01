package pl.javanexus.year2020.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportRepair {

    public int findTwoValues(List<Integer> numbers, int sum) {
        Set<Integer> results = new HashSet<>();
        for (Integer number : numbers) {
            if (results.contains(number)) {
                return number * (sum - number);
            }
            results.add(sum - number);
        }

        throw new IllegalStateException("Could not find 2 values that sum up to: " + sum);
    }
}
