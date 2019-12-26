package pl.javanexus.year2019.day22;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SlamShuffleTest {

    public static final int[] DECK_10 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    private InputReader inputReader;
    private SlamShuffle slamShuffle;

    @Before
    public void setUp() {
        this.inputReader = new InputReader();
        this.slamShuffle = new SlamShuffle();
    }

    @Test
    public void testDealIntoNewStack() {
        assertArrayEquals(
                new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
                SlamShuffle.Technique.REVERSE.shuffle(DECK_10, -1));
        assertEquals(2,
                SlamShuffle.Technique.REVERSE.getNextIndex(7, 10, ));
    }

    @Test
    public void testCut() {
        assertArrayEquals(
                new int[] {3, 4, 5, 6, 7, 8, 9, 0, 1, 2},
                SlamShuffle.Technique.CUT.shuffle(DECK_10, 3));
        assertArrayEquals(
                new int[] {6, 7, 8, 9, 0, 1, 2, 3, 4, 5},
                SlamShuffle.Technique.CUT.shuffle(DECK_10, -4));

        assertEquals(1,
                SlamShuffle.Technique.REVERSE.getNextIndex(7, 10, ));
    }

    @Test
    public void testDelWithIncrement() {
        assertArrayEquals(
                new int[] {0, 7, 4, 1, 8, 5, 2, 9, 6, 3},
                SlamShuffle.Technique.INCREMENT.shuffle(DECK_10, 3));
    }

    @Test
    public void testShuffleDeck() {
        String[] instructions = {
                "deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"
        };

        assertArrayEquals(
                new int[] {9, 2, 5, 8, 1, 4, 7, 0, 3, 6},
                slamShuffle.shuffleDeck(Arrays.asList(instructions), 10));
    }

    @Test
    public void testInstructions() throws IOException {
        List<String> instructions =
                inputReader.readStringValues("year2019/day22/input1.csv");
        int[] shuffledDeck = slamShuffle.shuffleDeck(instructions, 10007);
        assertEquals(-1, slamShuffle.findIndex(shuffledDeck, 2019));
    }
}
