package pl.javanexus.day15;

import lombok.Data;

@Data
public class Tile {

    private final int x;
    private final int y;
    private final boolean isRock;
    private final String symbol;

    private Unit unit;

    public Tile(int x, int y, char symbol, boolean isRock) {
        this.x = x;
        this.y = y;
        this.isRock = isRock;
        this.symbol = String.valueOf(symbol);
    }

    public Tile(int x, int y, char symbol, Unit unit) {
        this(x, y, symbol, false);
        this.unit = unit;
    }

    public boolean isEmpty() {
        return !isRock && unit == null;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Tile tile = (Tile) o;

        if (x != tile.x) return false;
        return y == tile.y;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }
}
