package pl.javanexus.year2019;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.javanexus.year2019.day15.OxygenSystem;

import java.util.LinkedList;
import java.util.List;

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

    public List<Point> getAdjacentEmptyPanels(Point point) {
        List<Point> emptyPanels = new LinkedList<>();

        if (isEmpty(point.getX(), point.getY() - 1)) {//north
            emptyPanels.add(new Point(point.getX(), point.getY() - 1));
        }
        if (isEmpty(point.getX(), point.getY() + 1)) {//south
            emptyPanels.add(new Point(point.getX(), point.getY() + 1));
        }
        if (isEmpty(point.getX() - 1, point.getY())) {//west
            emptyPanels.add(new Point(point.getX() - 1, point.getY()));
        }
        if (isEmpty(point.getX() + 1, point.getY())) {//east
            emptyPanels.add(new Point(point.getX() + 1, point.getY()));
        }

        return emptyPanels;
    }

    private boolean isEmpty(int x, int y) {
        return grid[y][x].getTileType() == OxygenSystem.TileType.EMPTY.getStatus();
    }

    public int getTileType(Point position) {
        return getPanel(position).getTileType();
    }

    public void setTileType(Point position, int tileType) {
        getPanel(position).setTileType(tileType);
    }

    public void setTileType(int x, int y, int tileType) {
        grid[y][x].setTileType(tileType);
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
