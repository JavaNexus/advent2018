package pl.javanexus.day17;

import lombok.Data;

@Data
public class Point {

    private final int x;
    private final int y;

    public Point getPointToTheLeft() {
        return new Point(x - 1, y);
    }

    public Point getPointToTheRight() {
        return new Point(x + 1, y);
    }

    public Point getPointAbove() {
        return new Point(x, y - 1);
    }

    public Point getPointBelow() {
        return new Point(x, y + 1);
    }
    
    public String toJSON() {
        return String.format("{x: %d, y: %d}", x, y);
    }
}
