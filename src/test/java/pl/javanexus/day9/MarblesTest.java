package pl.javanexus.day9;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MarblesTest {

    public static final String INPUT = "418 players; last marble is worth 71339 points";

    @Test
    public void testSingleCase() {
        assertEquals(2764, new Game(17, 1104).getWinningScore());
    }

    @Test
    public void testGetScore() {
        assertEquals(32, new Game(9, 25).getWinningScore());
        assertEquals(8317, new Game(10, 1618).getWinningScore());
        assertEquals(146373, new Game(13, 7999).getWinningScore());
        assertEquals(2764, new Game(17, 1104).getWinningScore());
        assertEquals(54718, new Game(21, 6111).getWinningScore());
        assertEquals(37305, new Game(30, 5807).getWinningScore());

        System.out.println(new Game(418, 71339).getWinningScore());
    }

    @Test
    public void testGetBigScore() {
        System.out.println(new Game(418, 100 * 71339).getWinningScore());
    }
}
