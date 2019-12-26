package pl.javanexus.year2019.day22;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SlamShuffleTest {

    public static final int[] DECK_10 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void testDealIntoNewStack() {
        int[] shuffledDeck = SlamShuffle.Technique.REVERSE.shuffle(DECK_10, -1);
        assertArrayEquals(new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, shuffledDeck);
    }

    @Test
    public void testCut() {
        assertArrayEquals(
                new int[] {3, 4, 5, 6, 7, 8, 9, 0, 1, 2},
                SlamShuffle.Technique.CUT.shuffle(DECK_10, 3));
        assertArrayEquals(
                new int[] {6, 7, 8, 9, 0, 1, 2, 3, 4, 5},
                SlamShuffle.Technique.CUT.shuffle(DECK_10, -4));
    }

    @Test
    public void testDelWithIncrement() {
        assertArrayEquals(
                new int[] {0, 7, 4, 1, 8, 5, 2, 9, 6, 3},
                SlamShuffle.Technique.INCREMENT.shuffle(DECK_10, 3));
    }
}
