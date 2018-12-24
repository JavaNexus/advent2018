package pl.javanexus.day15;

import lombok.Data;

@Data
public class PathTile {

    private final Tile tile;

    private boolean isVisited;
    private int distance;

    public PathTile(Tile tile, int distance) {
        this.tile = tile;
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PathTile pathTile = (PathTile) o;

        return tile != null ? tile.equals(pathTile.tile) : pathTile.tile == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (tile != null ? tile.hashCode() : 0);
        return result;
    }
}
