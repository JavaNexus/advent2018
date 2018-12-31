package pl.javanexus.day17;

import pl.javanexus.Line;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WaterFlow implements SerializableToJson {

    private final List<Reservoir> filledReservoirs = new LinkedList<>();
    private final List<Line> waterLines = new LinkedList<>();

    public void addFilledReservoirs(List<Reservoir> reservoirs) {
        filledReservoirs.addAll(reservoirs);
    }

    public void addWaterLine(Line waterLine) {
        waterLines.add(waterLine);
    }

    public int getWaterVolume() {
        return filledReservoirs.stream().mapToInt(Reservoir::getVolume).sum();
    }

    @Override
    public String toJSON() {
        return String.format("var filled = %s;\n" +
                        "var waterLines = %s;",
                toJSON(filledReservoirs),
                toJSON(waterLines));
    }
}
