package pl.javanexus.year2020.day10;

import java.util.Collections;
import java.util.List;

public class AdapterArray {

    public int[] findAdapterDistribution(List<Integer> adapters) {
        final int[] distributions = {0, 0, 0};

        Collections.sort(adapters);
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        int previousAdapter = 0;
        for (Integer adapter : adapters) {
            distributions[adapter - previousAdapter - 1]++;
            previousAdapter = adapter;
        }


        return distributions;
    }
}
