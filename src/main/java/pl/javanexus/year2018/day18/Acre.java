package pl.javanexus.year2018.day18;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.javanexus.grid.Point;

@EqualsAndHashCode
@ToString
public class Acre implements Point {

    private final int x;
    private final int y;

    @Getter
    private AcreType currentType;
    @Getter @Setter
    private AcreType nextType;

    public Acre(int x, int y, AcreType currentType) {
        this.x = x;
        this.y = y;
        this.currentType = currentType;
    }

    public void changeTypeToNext() {
        this.currentType = nextType;
        this.nextType = null;
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
        return currentType.getSymbol();
    }
}
