package pl.javanexus.day17;

import pl.javanexus.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Spring {

    private final Point springSource;
    private final Map<Point, Reservoir> reservoirs;

    public Spring(Point springSource, Map<Point, Reservoir> reservoirs) {
        this.springSource = springSource;
        this.reservoirs = reservoirs;
    }

    public int measureWaterFlow() {
        WaterFlow waterFlow = simulateWaterFlow(springSource);
        return waterFlow.getWaterVolume();
    }

    private WaterFlow simulateWaterFlow(Point springSource) {
        final WaterFlow waterFlow = new WaterFlow();

        List<Reservoir> sortedTopToBottom = reservoirs.values().stream()
                .sorted(Comparator.comparingInt(reservoir -> reservoir.getBottomLeft().getY()))
                .collect(Collectors.toList());

        Set<Point> waterSources = new HashSet<>();
        waterSources.add(springSource);
        do {
            Set<Point> nextWaterSources = new HashSet<>();

            for (Point waterSource : waterSources) {
                List<Reservoir> nextReservoirs = getNextReservoir(waterSource, sortedTopToBottom);
                waterFlow.addFilledReservoirs(nextReservoirs);

                for (Reservoir reservoir : nextReservoirs) {
                    waterFlow.addWaterLine(new Line(waterSource, reservoir.getPointOnWaterSurface(waterSource)));
                    nextWaterSources.addAll(reservoir.getNextWaterSources());
                }
            }
            waterSources = nextWaterSources;
        } while (!waterSources.isEmpty());

        return waterFlow;
    }

    private List<Reservoir> getNextReservoir(Point waterSource, List<Reservoir> sortedTopToBottom) {
        final Iterator<Reservoir> iterator = sortedTopToBottom.iterator();

        List<Reservoir> nextReservoirs = new LinkedList<>();
        do {
            Reservoir reservoir = iterator.next();
            if (reservoir.isUnderPoint(waterSource)) {
                nextReservoirs.add(reservoir);
            }
        } while (iterator.hasNext() && nextReservoirs.isEmpty());

        return nextReservoirs;
    }
}
