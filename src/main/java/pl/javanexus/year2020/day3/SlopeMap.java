package pl.javanexus.year2020.day3;

public class SlopeMap {

    public static final char EMPTY = '.';
    public static final char TREE = '#';

    private final char[][] slope;
    private final int mapWidth;

    public SlopeMap(char[][] slope) {
        this.slope = slope;
        this.mapWidth = slope[0].length;
    }

    public char getField(int x, int y) {
        return slope[y][x % mapWidth];
    }

    public int getHeight() {
        return slope.length;
    }
}
