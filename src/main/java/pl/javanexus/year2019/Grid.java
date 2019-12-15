package pl.javanexus.year2019;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.javanexus.year2019.day15.OxygenSystem;

public class Grid {

    private final Panel[][] grid;

    public Grid(int width, int height, int initialValue) {
        this.grid = new Panel[height][];
        for (int y = 0; y < grid.length; y++) {
            grid[y] = new Panel[width];
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Panel(OxygenSystem.TileType.UNKNOWN.getStatus(), 0, 0);
            }
        }
    }

    public void printGrid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x].getTileType());
            }
            System.out.print("\n");
        }
    }

    public int getTileType(Point position) {
        return getPanel(position).getTileType();
    }

    public void setTileType(Point position, int tileType) {
        getPanel(position).setTileType(tileType);
    }

    public int getNumberOfVisits(Point position) {
        return getPanel(position).getNumberOfVisits();
    }

    public int addVisit(Point position, int distance) {
        return getPanel(position).addVisit(distance);
    }

    private Panel getPanel(Point position) {
        return grid[position.getY()][position.getX()];
    }

    @Data
    @AllArgsConstructor
    private class Panel {

        private int tileType;
        private int numberOfVisits;
        private int distanceFromStart;

        public int addVisit(int distance) {
            numberOfVisits++;
            if (numberOfVisits == 1) {
                this.distanceFromStart = distance;
            }

            return distanceFromStart;
        }
    }
}
