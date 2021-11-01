package pl.javanexus.year2020.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportRepair {

    public int findTwoValues(List<Integer> numbers, int sum) {
        Set<Integer> results = new HashSet<>();
        for (Integer number : numbers) {
            if (results.contains(number)) {
                return number * (sum - number);
            } else if (sum > number) {
                results.add(sum - number);
            }
        }

        return -1;
    }

    public int findThreeValues(List<Integer> numbers, int sum) {
        List<Integer> partialSums = numbers.stream()
                .map(number -> sum - number)
                .collect(Collectors.toList());

        for (Integer partialSum : partialSums) {
            int partialResult = findTwoValues(numbers, partialSum);
            if (partialResult != -1) {
                return partialResult * (sum - partialSum);
            }
        }

        return -1;
    }
}
