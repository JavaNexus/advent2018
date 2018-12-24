package pl.javanexus.day11;

import lombok.Data;

@Data
public class FuelCellArea {

    private final FuelCell fuelCell;
    private final int areaSize;
    private final int areaPowerLevel;
}
