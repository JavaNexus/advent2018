package pl.javanexus.day18;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.grid.Grid;
import pl.javanexus.grid.GridFactory;

import java.util.List;

public class GroundTest {

    private GridFactory<Ground, Acre> gridFactory;

    @Before
    public void setUp() throws Exception {
        this.gridFactory = new GridFactory<Ground, Acre>() {
            @Override
            public Acre createPoint(int x, int y, char symbol) {
                return new Acre(x, y, AcreType.getBySymbol(symbol));
            }

            @Override
            public Acre[] createRow(int length) {
                return new Acre[length];
            }

            @Override
            public Ground createGrid(List<Acre[]> tiles) {
                return new Ground(tiles.toArray(new Acre[tiles.size()][]));
            }
        };
    }

    @Test
    public void testSimulateGrowth() throws Exception {
        Grid<Acre> grid = gridFactory.createGridFromInputFile("day18/day18_simple.input");
        grid.print();
    }


}
