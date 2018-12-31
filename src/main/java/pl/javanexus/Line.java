package pl.javanexus;

import lombok.Data;
import pl.javanexus.day17.Point;
import pl.javanexus.day17.SerializableToJson;

@Data
public class Line implements SerializableToJson {

    private final Point from;
    private final Point to;

    public boolean isHorizontal() {
        return from.getY() == to.getY();
    }

    public boolean isVertical() {
        return from.getX() == to.getX();
    }

    @Override
    public String toJSON() {
        return String.format(
                "{fromX: %d, fromY: %d, toX: %d, toY: %d}",
                from.getX(), from.getY(), to.getX(), to.getY());
    }
}
