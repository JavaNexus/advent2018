package pl.javanexus.year2019.day3;

import lombok.Data;

@Data
public class Point {

    private final int x;
    private final int y;

    public int getDistance(Point to) {
        return Math.abs(x - to.getX()) + Math.abs(y - to.getY());
    }
}
