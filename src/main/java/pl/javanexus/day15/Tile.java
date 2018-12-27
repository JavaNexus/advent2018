package pl.javanexus.day15;

import lombok.Data;

@Data
public class Tile {

    private final int x;
    private final int y;
    private final boolean isRock;

    private Unit unit;

    public Tile(int x, int y, boolean isRock) {
        this.x = x;
        this.y = y;
        this.isRock = isRock;
    }

    public boolean isEmpty() {
        return !isRock && unit == null;
    }

    @Override
    public String toString() {
        return String.format("%s [%d, %d]", getSymbol(), x, y);
    }

    public char getSymbol() {
        if (isRock) {
            return BoardParser.TileSymbol.ROCK.getSymbol();
        } else if (unit == null) {
            return BoardParser.TileSymbol.EMPTY.getSymbol();
        } else if (unit.getUnitType() == Unit.UnitType.ELF) {
            return BoardParser.TileSymbol.ELF.getSymbol();
        } else if (unit.getUnitType() == Unit.UnitType.GOBLIN) {
            return BoardParser.TileSymbol.GOBLIN.getSymbol();
        } else {
            throw new IllegalArgumentException(String.format("Unexpected tile state: %d", x, y));
        }
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
