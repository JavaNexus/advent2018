package pl.javanexus.year2019.day3;

import lombok.Data;

@Data
public class Point {

    private final int x;
    private final int y;

    public int getDistance(Point to) {
        return Math.abs(x - to.getX()) + Math.abs(y - to.getY());
    }

    public boolean isBetweenPoints(Point from, Point to, Point centralPort) {
        int dX = x - centralPort.getX();
        int dY = y - centralPort.getY();

        if (from.getX() == to.getX()) {
            return dX >= from.getX() && dX < to.getX();
        } else if (from.getY() == to.getY()) {
            return dY >= from.getY() && dY < to.getY();
        }

        return false;
    }
}
