package pl.javanexus.day6;

import lombok.Data;

@Data
public class Point {

    private final int id;
    private final int x;
    private final int y;

    private int area = 0;

    public void incrementArea() {
        this.area++;
    }

    public int getDistance(Point point) {
        return getDistance(point.getX(), point.getY());
    }

    public int getDistance(int x, int y) {
        return Math.abs(this.x - x) + Math.abs(this.y - y);
    }

    public double getCosin(Point point) {
        int dx = point.getX() - this.x;
        int dy = point.getY() - this.y;
        return dx / Math.sqrt(dx * dx + dy * dy);
    }
}
