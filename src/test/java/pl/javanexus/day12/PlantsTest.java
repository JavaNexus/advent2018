package pl.javanexus.day12;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class PlantsTest {

    public static final String INITIAL_STATE = "##...#......##......#.####.##.#..#..####.#.######.##..#.####...##....#.#.####.####.#..#.######.##...";
    public static final String[] SPREAD_PATTERNS = {
            "#.... => .",
            "#..## => #",
            "....# => .",
            "...#. => .",
            "...## => #",
            "#.#.# => .",
            ".#... => #",
            "##.#. => .",
            "..#.# => .",
            ".##.# => #",
            "###.# => #",
            ".#.## => .",
            "..... => .",
            "##### => #",
            "###.. => .",
            "##..# => #",
            "#.### => #",
            "#.#.. => .",
            "..### => .",
            "..#.. => .",
            ".#..# => #",
            ".##.. => #",
            "##... => #",
            ".#.#. => #",
            ".###. => #",
            "#..#. => .",
            "####. => .",
            ".#### => #",
            "#.##. => #",
            "##.## => .",
            "..##. => .",
            "#...# => #"
    };

    public static final String INITIAL_STATE_TEST = "#..#.#..##......###...###";
    public static final String[] SPREAD_PATTERNS_TEST = {
            "...## => #",
            "..#.. => #",
            ".#... => #",
            ".#.#. => #",
            ".#.## => #",
            ".##.. => #",
            ".#### => #",
            "#.#.# => #",
            "#.### => #",
            "##.#. => #",
            "##.## => #",
            "###.. => #",
            "###.# => #",
            "####. => #",
    };
    public static final int NUMBER_OF_GENERATIONS_SMALL = 200;
    public static final BigInteger NUMBER_OF_GENERATIONS_LARGE = new BigInteger("50000000000");

    @Test
    public void testGrowPlants() {
        Plants plants = new Plants(INITIAL_STATE_TEST, SPREAD_PATTERNS_TEST);
        assertEquals(325, plants.grow(NUMBER_OF_GENERATIONS_SMALL));
    }

    @Test
    public void testGrowPlantsFromInput() {
        Plants plants = new Plants(INITIAL_STATE, SPREAD_PATTERNS);
        System.out.println(plants.grow(NUMBER_OF_GENERATIONS_SMALL));
    }

    @Test
    public void testBigInput() {
        BigInteger generationId = BigInteger.ZERO;
        while (generationId.compareTo(NUMBER_OF_GENERATIONS_LARGE) < 0) {
            generationId = generationId.add(BigInteger.ONE);
        }
    }
}
