package pl.javanexus.day15;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class PathTile implements Comparable {

    private final Tile tile;

    private boolean isVisited;
    private int distance;
    private List<Tile> path = new LinkedList<>();

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

    @Override
    public int compareTo(Object o) {
        PathTile other = (PathTile) o;// TODO: 25.12.2018 check type

        int diff = this.getDistance() - other.getDistance();
        if (diff == 0) {
            Tile tile1 = this.getTile();
            Tile tile2 = other.getTile();
            diff = tile1.getY() - tile2.getY();
            if (diff == 0) {
                diff = tile1.getX() - tile2.getX();
            }
        }

        return diff;
    }
}
