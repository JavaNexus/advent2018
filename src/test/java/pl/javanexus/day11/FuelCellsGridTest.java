package pl.javanexus.day11;

import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class FuelCellsGridTest {

    public static final String JS_PATH = "D:\\Projekty\\Java\\advent2018\\src\\test\\resources\\fuel\\";
    public static final int SERIAL_NUMBER = 8868;

    @Test
    public void testSelectFuelCell() throws IOException {
        FuelCellsGrid grid = new FuelCellsGrid(18);
        FuelCellArea maxPowerLevelArea = IntStream.range(1, 300)
                .mapToObj(areaSize -> grid.selectFuelCellRegionWithTheHighestPowerLevel(areaSize))
                .max(Comparator.comparingInt(FuelCellArea::getAreaPowerLevel))
                .get();

        FuelCell cornerFuelCell = maxPowerLevelArea.getFuelCell();
        assertEquals(90, cornerFuelCell.getX());
        assertEquals(269, cornerFuelCell.getY());
        assertEquals(16, maxPowerLevelArea.getAreaSize());

        testPowerLevelArea(grid, 3, 33, 45);
        testPowerLevelArea(grid, 16, 90, 269);

//        writeToFile(grid.getGridJs(), "cells_test.js");
    }

    @Test
    public void testFoo() {
        FuelCellsGrid grid = new FuelCellsGrid(SERIAL_NUMBER);
        FuelCellArea maxPowerLevelArea = IntStream.range(1, 300)
                .mapToObj(areaSize -> grid.selectFuelCellRegionWithTheHighestPowerLevel(areaSize))
                .max(Comparator.comparingInt(FuelCellArea::getAreaPowerLevel))
                .get();
        FuelCell cornerFuelCell = maxPowerLevelArea.getFuelCell();

        System.out.printf("%d,%d,%d\n", cornerFuelCell.getX(), cornerFuelCell.getY(), maxPowerLevelArea.getAreaSize());
    }

    private void testPowerLevelArea(FuelCellsGrid grid, int areaSize, int expectedX, int expectedY) {
        FuelCellArea maxPowerArea = grid.selectFuelCellRegionWithTheHighestPowerLevel(areaSize);
        FuelCell cornerFuelCell = maxPowerArea.getFuelCell();

        System.out.printf("%d,%d,%d (%d)\n", cornerFuelCell.getX(), cornerFuelCell.getY(), maxPowerArea.getAreaSize(), maxPowerArea.getAreaPowerLevel());

        assertEquals(expectedX, cornerFuelCell.getX());
        assertEquals(expectedY, cornerFuelCell.getY());
    }

    @Test
    public void testCustomInputPowerLevel() throws IOException {
        FuelCellsGrid grid = new FuelCellsGrid(SERIAL_NUMBER);
        FuelCellArea maxPowerArea = grid.selectFuelCellRegionWithTheHighestPowerLevel(3);
        FuelCell topLeftFuelCell = maxPowerArea.getFuelCell();

        FuelCell max = grid.getMaxPowerLevelCell();
        System.out.printf("Max: %d [%d,%d]\n", max.getPowerLevel(), max.getX(), max.getY());

        FuelCell min = grid.getMinPowerLevelCell();
        System.out.printf("Min: %d [%d,%d]\n", min.getPowerLevel(), min.getX(), min.getY());

        System.out.printf("%d,%d\n", topLeftFuelCell.getX(), topLeftFuelCell.getY());

//        writeToFile(grid.getGridJs(), "cells.js");
    }

    @Test
    public void testCalculatePowerLevel() {
        assertEquals(4, new FuelCell(3, 5, 8).getPowerLevel());
        assertEquals(-5, new FuelCell(122,79, 57).getPowerLevel());
        assertEquals(0, new FuelCell(217,196, 39).getPowerLevel());
        assertEquals(4, new FuelCell(101,153, 71).getPowerLevel());
    }

    private void writeToFile(String starsJs, String outputFileName) throws IOException {
        FileWriter fileWriter = new FileWriter(JS_PATH + outputFileName);
        fileWriter.write(starsJs);
        fileWriter.close();
    }
}
