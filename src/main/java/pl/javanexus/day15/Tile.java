package pl.javanexus.day15;

import lombok.Data;

@Data
public class Tile {

    private final boolean isRock;
    private final String symbol;

    private Unit unit;

    public Tile(boolean isRock, char symbol) {
        this.isRock = isRock;
        this.symbol = String.valueOf(symbol);
    }

    public Tile(Unit unit, char symbol) {
        this(false, symbol);
        this.unit = unit;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public boolean isEmpty() {
        return !isRock && unit == null;
    }
}
