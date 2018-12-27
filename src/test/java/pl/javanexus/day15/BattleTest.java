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
    public void testGetResultFromTestInput() throws Exception {
        boardParser.parseInput("day15_test.input");

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        assertEquals(239010, battle.calculateOutcome().getResult());

        //1) That's not the right answer; your answer is too low. (You guessed 236440.)
    }

    @Test
    public void testGetResultWhenNoElfDies() throws IOException {
        findMinAttackStrength("day15_test.input");
        //That's not the right answer; your answer is too high. (You guessed 63826.)
    }

    private void findMinAttackStrength(String inputFileName) throws IOException {
        int elfAttackPower = 16;//UnitFactory.DEFAULT_ATTACK + 1;

        boolean hasAnyElfDied;
        do {
            BoardParser boardParser = new BoardParser(new UnitFactory(Unit.UnitType.ELF, elfAttackPower++));
            boardParser.parseInput(inputFileName);

            Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
            BattleOutcome battleOutcome = battle.calculateOutcome();

            hasAnyElfDied = !battleOutcome.areAllUnitsOfTypeAlive(Unit.UnitType.ELF);
            System.out.printf("%d : %d, elves: %d, goblins: %d\n",
                    elfAttackPower - 1,
                    battleOutcome.getResult(),
                    battleOutcome.getNumberOfUnitsAlive(Unit.UnitType.ELF),
                    battleOutcome.getNumberOfUnitsAlive(Unit.UnitType.GOBLIN));
        } while(hasAnyElfDied);
    }

    @Test
    public void testGetResult() throws Exception {
        BoardParser boardParser = new BoardParser(new UnitFactory(Unit.UnitType.ELF, 8));
        boardParser.parseInput("day15_attack.input");

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        assertEquals(46 * 590, battle.calculateOutcome().getResult());
    }

    @Test
    public void testGetResult_4() throws Exception {
        boardParser.parseInput("day15_sample4.input");

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        assertEquals(54 * 536, battle.calculateOutcome().getResult());
    }

    @Test
    public void testGetResult_5() throws Exception {
        boardParser.parseInput("day15_sample5.input");

        Battle battle = new Battle(boardParser.getBoard(), boardParser.getAllUnits());
        assertEquals(20 * 937, battle.calculateOutcome().getResult());
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
