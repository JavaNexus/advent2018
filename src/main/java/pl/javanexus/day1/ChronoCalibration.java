package pl.javanexus.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class ChronoCalibration {

    public static final int INITIAL_VALUE = 0;

    private final List<Integer> values = new LinkedList<>();

    public ChronoCalibration(int[] values) {
        this.values.addAll(Arrays.stream(values).mapToObj(Integer::new).collect(Collectors.toList()));
    }

    public ChronoCalibration(List<Integer> values) {
        this.values.addAll(values);
    }

    public int getFirstDuplicate() {
        int sum = INITIAL_VALUE;

        Set<Integer> partialSums = new HashSet<>();
        partialSums.add(sum);

        boolean isNewValue = true;
        Iterator<Integer> iterator = values.iterator();
        while (isNewValue) {
            if (!iterator.hasNext()) {
                iterator = values.iterator();
            }
            sum += iterator.next();
            isNewValue = partialSums.add(sum);
        }

        return sum;
    }
}
