package pl.javanexus.day18;

import pl.javanexus.grid.Grid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Ground extends Grid<Acre> {

    public Ground(Acre[][] acres) {
        super(acres);
    }

    public void simulateGrowth(int numberOfMinutes) {
        for (int minute = 0; minute < numberOfMinutes; minute++) {
            simulateNextGrowth();
        }
    }

    private void simulateNextGrowth() {
        getPointsStream().forEach(acre -> {
            List<AcreType> adjacentTypes = getAdjacentPoints(acre).stream()
                    .map(Acre::getCurrentType)
                    .collect(Collectors.toList());
            acre.setNextType(acre.getCurrentType().getNextGroundType(adjacentTypes));
        });

        iterateOverGrid(
                (x, y) -> getPoint(x, y).ifPresent(Acre::changeTypeToNext),
                (y) -> {});
    }

    public Map<AcreType, Integer> countByType() {
        Map<AcreType, Integer> numberOfPointsByAcreType =
                Arrays.stream(AcreType.values())
                        .collect(Collectors.toMap(acreType -> acreType, acreType -> 0));
        getPointsStream()
                .forEach(point -> numberOfPointsByAcreType.compute(point.getCurrentType(), (type, sum) -> sum + 1));

        return numberOfPointsByAcreType;
    }
}
