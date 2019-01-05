package pl.javanexus.day18;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.javanexus.grid.Point;

@EqualsAndHashCode
@ToString
public class Acre implements Point {

    private final int x;
    private final int y;
    private AcreType type;

    public Acre(int x, int y, AcreType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public char getSymbol() {
        return type.getSymbol();
    }
}
