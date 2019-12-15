package pl.javanexus.year2019;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    private int x;
    private int y;

    public void setCoordinates(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y +"]";
    }
}
