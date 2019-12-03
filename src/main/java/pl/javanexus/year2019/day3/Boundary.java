package pl.javanexus.year2019.day3;

import lombok.Data;

@Data
public class Boundary {

    private int minX;
    private int maxX;

    private int minY;
    private int maxY;

    public void update(Point point) {
        if (point.getX() < minX) {
            this.minX = point.getX();
        }
        if (point.getX() > maxX) {
            this.maxX = point.getX();
        }
        if (point.getY() < minY) {
            this.minY = point.getY();
        }
        if (point.getY() > maxY) {
            this.maxY = point.getY();
        }
    }

    public int getWidth() {
        return Math.abs(minX) + Math.abs(maxX);
    }

    public int getHeight() {
        return Math.abs(minY) + Math.abs(maxY);
    }

    public Point getCentralPort() {
        return new Point(Math.abs(minX), Math.abs(minY));
    }
}
