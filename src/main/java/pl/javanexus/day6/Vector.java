package pl.javanexus.day6;

import lombok.Data;

@Data
public class Vector {

    private final Point from;
    private final Point to;

    public double getCosin(Vector vector) {
        return getDotProduct(vector) / (this.getLength() * vector.getLength());
    }

    public int getDotProduct(Vector vector) {
        return getDX() * vector.getDX() + getDY() * vector.getDY();
    }

    public int getDX() {
        return to.getX() - from.getX();
    }

    public int getDY() {
        return to.getY() - from.getY();
    }

    public double getLength() {
        int dx = getDX();
        int dy = getDY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
