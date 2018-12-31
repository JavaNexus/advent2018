package pl.javanexus.day17;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@ToString(exclude = {"innerReservoir"})
@EqualsAndHashCode(exclude = {"innerReservoir"})
public class Reservoir implements SerializableToJson {

    public static final int SINGLE_BORDER = 1;
    public static final int DOUBLE_BORDERS = 2;

    private final Point topLeft;
    private final Point topRight;
    private final Point bottomLeft;
    private final Point bottomRight;

    private boolean isOpen = true;
    private boolean isFilled = false;

    private Optional<Reservoir> innerReservoir = Optional.empty();

    public boolean isUnderPoint(Point point) {
        return bottomLeft.getY() >= point.getY() &&
                bottomLeft.getX() <= point.getX() && bottomRight.getX() >= point.getX();
    }

    public List<Point> getNextWaterSources() {
        if (topLeft.getY() == topRight.getY()) {
            return Arrays.asList(
                    new Point(topLeft.getX() - 1, topLeft.getY() + 1),
                    new Point(topRight.getX() + 1, topRight.getY() + 1));
        } else if (topLeft.getY() > topRight.getY()) {
            return Arrays.asList(new Point(topLeft.getX() - 1, topLeft.getY() + 1));
        } else {
            return Arrays.asList(new Point(topRight.getX() + 1, topRight.getY() + 1));
        }
    }

    public Point getPointOnWaterSurface(Point waterSource) {
        return new Point(waterSource.getX(), Integer.max(topLeft.getY(), topRight.getY()));
    }

    public int getVolume() {
        int innerReservoirVolume = innerReservoir
                .map(r -> r.getVolumeWithBorders(this.topLeft, this.topRight))
                .orElse(0);
        int outerReservoirVolume =
                (getWidth() - DOUBLE_BORDERS) * (getHeight() - (isOpen ? SINGLE_BORDER : DOUBLE_BORDERS));
        return outerReservoirVolume - innerReservoirVolume;
    }

    public int getVolumeWithBorders(Point topLeft, Point topRight) {
        return getWidth() * getHeight();
    }

    public boolean isInner(Reservoir reservoir) {
        Point bottomLeft = reservoir.getBottomLeft();
        Point bottomRight = reservoir.getBottomLeft();

        return bottomLeft.getY() > this.topLeft.getY() &&
                bottomLeft.getY() < this.bottomLeft.getY() &&
                bottomLeft.getX() > this.bottomLeft.getX() &&
                bottomRight.getX() < this.bottomRight.getX();
    }

    private int getHeight() {
        return getHeight(0);
    }

    private int getHeight(int boundry) {
        int leftBorderHeight = bottomLeft.getY() - topLeft.getY() + 1;
        int rightBorderHeight = bottomRight.getY() - topRight.getY() + 1;

        return Integer.min(leftBorderHeight, rightBorderHeight);
    }

    private int getWidth() {
        return topRight.getX() - topLeft.getX() + 1;
    }

    @Override
    public String toJSON() {
        return String.format("{x: %d, y: %d, width: %d, height: %d}",
                topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
    }
}
