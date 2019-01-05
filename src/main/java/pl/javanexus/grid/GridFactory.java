package pl.javanexus.grid;

import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface GridFactory<G extends Grid, P extends Point> {

    P createPoint(int x, int y, char symbol);
    P[] createRow(int length);
    G createGrid(List<P[]> tiles);

    default G createGridFromInputFile(String inputFileName) throws IOException {
        List<P[]> tiles = new InputReader().readValues(inputFileName, (index, line) -> {
            final char[] symbolsInRow = line.toCharArray();
            return IntStream.range(0, symbolsInRow.length)
                    .mapToObj(x -> createPoint(x, index, symbolsInRow[x]))
                    .collect(Collectors.toList())
                    .toArray(createRow(symbolsInRow.length));
        });

        return createGrid(tiles);
    }
}
