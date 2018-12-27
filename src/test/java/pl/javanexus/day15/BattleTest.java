package pl.javanexus.day15;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class BattleTest {

    private BoardParser boardParser;

    @Before
    public void setUp() throws Exception {
        this.boardParser = new BoardParser();
    }

    @Test
    public void testGetResult() throws Exception {
        boardParser.parseInput("day15_move.input");

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        battle.getResult();
    }

    @Test
    public void testCalculateDistance_1() throws IOException {
        Point from = new Point(3, 1);

        Map<Point, Integer> expectedDistance = new HashMap<>();
        expectedDistance.put(new Point(3, 3), 2);
        expectedDistance.put(new Point(4, 4), 4);
        expectedDistance.put(new Point(5, 4), 5);

        testCalculatePath("day15_move_round2.input", from, expectedDistance);
    }

    @Test
    public void testCalculateDistance_2() throws Exception {
        Point from = new Point(1, 1);

        Map<Point, Integer> expectedDistance = new HashMap<>();
        expectedDistance.put(new Point(3, 1), 2);
        expectedDistance.put(new Point(5, 1), -1);
        expectedDistance.put(new Point(2, 2), 2);
        expectedDistance.put(new Point(5, 2), -1);
        expectedDistance.put(new Point(1, 3), 2);
        expectedDistance.put(new Point(3, 3), 4);

        testCalculatePath("day15_path.input", from, expectedDistance);
    }

    private void testCalculatePath(String inputFileName, Point from, Map<Point, Integer> expectedDistance)
            throws IOException {
        boardParser.parseInput(inputFileName);

        Board board = new Board(boardParser.getBoard());
        board.printMap();

        List<Tile> to = expectedDistance.keySet().stream()
                .map(point -> board.getTile(point.getX(), point.getY()))
                .collect(Collectors.toList());

        Map<Tile, Integer> reachableDestinations = expectedDistance.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .collect(Collectors.toMap(e -> {
                    Point point = e.getKey();
                    return board.getTile(point.getX(), point.getY());
                }, e -> e.getValue()));

        List<PathTile> reachableTargets = board.calculateDistance(board.getTile(from.getX(), from.getY()), to);
        assertEquals(reachableDestinations.size(), reachableTargets.size());

        reachableTargets.stream()
                .forEach(pathTile -> {
                    Tile tile = pathTile.getTile();
                    assertEquals(tile.toString(),
                            reachableDestinations.get(tile).intValue(),
                            pathTile.getDistance());
                });
    }

    @Data
    private class Point {
        private final int x;
        private final int y;
    }
}
