package pl.javanexus.day18;

import pl.javanexus.grid.Grid;

public class Ground extends Grid<Acre> {

    public Ground(Acre[][] acres) {
        super(acres);
    }

    public void simulateGrowth(int numberOfMinutes) {
        for (int minute = 0; minute < numberOfMinutes; minute++) {

        }
    }
}
