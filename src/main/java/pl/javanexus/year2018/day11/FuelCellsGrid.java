package pl.javanexus.year2018.day11;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FuelCellsGrid {

    public static final int GRID_SIZE = 300;

    private final int serialNumber;
    private final FuelCell[][] grid = new FuelCell[GRID_SIZE][];

    public FuelCellsGrid(int serialNumber) {
        this.serialNumber = serialNumber;
        this.initGrid();
    }

    private void initGrid() {
        for (int x = 0; x < grid.length; x++) {
            grid[x] = new FuelCell[GRID_SIZE];
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new FuelCell(x, y, serialNumber);
            }
        }
    }

    public FuelCellArea selectFuelCellRegionWithTheHighestPowerLevel(int areaSize) {
        FuelCell maxPowerFuelCell = grid[0][0];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (x < GRID_SIZE - areaSize && y < GRID_SIZE - areaSize) {
                    grid[x][y].setAreaPowerLevel(calculateAreaPowerLevel(x, y, areaSize));
                    if (grid[x][y].getAreaPowerLevel() > maxPowerFuelCell.getAreaPowerLevel()) {
                        maxPowerFuelCell = grid[x][y];
                    }
                }
            }
        }

        return new FuelCellArea(maxPowerFuelCell, areaSize, maxPowerFuelCell.getAreaPowerLevel());
    }


    public FuelCell getMaxPowerLevelCell() {
        FuelCell maxPowerCell = grid[0][0];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[x][y].getPowerLevel() > maxPowerCell.getPowerLevel()) {
                    maxPowerCell = grid[x][y];
                }
            }
        }

        return maxPowerCell;
    }

    public FuelCell getMinPowerLevelCell() {
        FuelCell minPowerCell = grid[0][0];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[x][y].getPowerLevel() < minPowerCell.getPowerLevel()) {
                    minPowerCell = grid[x][y];
                }
            }
        }

        return minPowerCell;
    }

    public String getGridJs() {
        StringBuilder builder = new StringBuilder("var grid = [");
        iterateOverGrid(
                (x, y) -> builder.append(String.format("{x:%d, y:%d, p:%d},", x, y, grid[x][y].getPowerLevel())),
                (y) -> {});
        builder.append("]");

        return builder.toString();
    }

    private int calculateAreaPowerLevel(Integer x, Integer y, int areaSize) {
        int areaPowerLevel = 0;
        for (int dx = 0; dx < areaSize; dx++) {
            for (int dy = 0; dy < areaSize; dy++) {
                areaPowerLevel += grid[x + dx][y + dy].getPowerLevel();
            }
        }

        return areaPowerLevel;
    }

    public void printGridPowerLevels() {
        iterateOverGrid(
                (x, y) -> {
                    System.out.printf("[%02d]", grid[x][y].getPowerLevel());
                },
                (y) -> {
                    System.out.printf("\n");
                });
    }

    private void iterateOverGrid(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(y);
        }
    }
}
