package pl.javanexus.year2018.day17;

import pl.javanexus.Line;

import java.util.*;
import java.util.stream.Collectors;

public class ReservoirFactory {

    public Map<Point, Reservoir> createReservoirs(List<Line> lines) {
        final Map<Point, Point> verticalLines = lines.stream()
                .filter(Line::isVertical)
                .collect(Collectors.toMap(Line::getTo, Line::getFrom));

        final List<Line> enclosingLines = new LinkedList<>();
        final Map<Point, Reservoir> reservoirs = new HashMap<>();

        lines.stream()
                .filter(Line::isHorizontal)
                .forEach(horizontalLine -> {
                    Point topLeft = verticalLines.get(horizontalLine.getFrom());
                    Point topRight = verticalLines.get(horizontalLine.getTo());
                    if (topLeft == null || topRight == null) {
                        enclosingLines.add(horizontalLine);
                    } else {
                        Reservoir reservoir =
                                new Reservoir(topLeft, topRight, horizontalLine.getFrom(), horizontalLine.getTo());
                        reservoirs.put(reservoir.getTopLeft(), reservoir);
                    }
                });

        matchEnclosingLinesWithReservoirs(enclosingLines, reservoirs);
        // TODO: 2018-12-29 match inner reservoirs
        for (Reservoir outer : reservoirs.values()) {
            for (Reservoir inner : reservoirs.values()) {
                if (outer.isInner(inner)) {
                    outer.setInnerReservoir(Optional.of(inner));
                }
            }
        }

        return reservoirs;
    }

    private void matchEnclosingLinesWithReservoirs(List<Line> enclosingLines, Map<Point, Reservoir> reservoirs) {
        enclosingLines.stream()
                .map(Line::getFrom)
                .map(reservoirs::get)
                .forEach(reservoir -> reservoir.setOpen(false));
    }
}
