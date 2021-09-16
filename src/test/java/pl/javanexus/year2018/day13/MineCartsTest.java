package pl.javanexus.year2018.day13;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MineCartsTest {

    public static final char EMPTY = ' ';

    private InputReader inputReader;

    @Before
    public void init() {
        this.inputReader = new InputReader();
    }

    @Test
    public void testGetCartNextPosition() throws IOException {
        validateNextPosition(
                CartDirection.NORTH, TrackDirection.VERTICAL,
                CartDirection.NORTH);
        validateNextPosition(
                CartDirection.NORTH, TrackDirection.LEFT,
                CartDirection.WEST);
        validateNextPosition(
                CartDirection.NORTH, TrackDirection.RIGHT,
                CartDirection.EAST);

        validateNextPosition(
                CartDirection.SOUTH, TrackDirection.VERTICAL,
                CartDirection.SOUTH);
        validateNextPosition(
                CartDirection.SOUTH, TrackDirection.LEFT,
                CartDirection.EAST);
        validateNextPosition(
                CartDirection.SOUTH, TrackDirection.RIGHT,
                CartDirection.WEST);

        validateNextPosition(
                CartDirection.EAST, TrackDirection.HORIZONTAL,
                CartDirection.EAST);
        validateNextPosition(
                CartDirection.EAST, TrackDirection.LEFT,
                CartDirection.SOUTH);
        validateNextPosition(
                CartDirection.EAST, TrackDirection.RIGHT,
                CartDirection.NORTH);

        validateNextPosition(
                CartDirection.WEST, TrackDirection.HORIZONTAL,
                CartDirection.WEST);
        validateNextPosition(
                CartDirection.WEST, TrackDirection.LEFT,
                CartDirection.NORTH);
        validateNextPosition(
                CartDirection.WEST, TrackDirection.RIGHT,
                CartDirection.SOUTH);
    }

    private void validateNextPosition(CartDirection cartDirection, TrackDirection trackDirection,
                                      CartDirection expectedCartDirection) {
        assertEquals(expectedCartDirection, cartDirection.getNextCartDirection(trackDirection));
    }

    @Test
    public void testGetFirstCrash() throws IOException {
        MineCarts mineCarts = parseTracks("day13_test.input");
        Cart firstCrashCart = mineCarts.getFirstCrash();
        assertEquals(7, firstCrashCart.getX());
        assertEquals(3, firstCrashCart.getY());
    }

    @Test
    public void testGetLastCartRunning() throws IOException {
        MineCarts mineCarts = parseTracks("day13_test_part2.input");

        Cart lastCartRunning = mineCarts.getLastCartRunning();
        assertEquals(6, lastCartRunning.getX());
        assertEquals(4, lastCartRunning.getY());
    }

    @Test
    public void testGetFirstCrashFromInput() throws IOException {
        MineCarts mineCarts = parseTracks("day13_carts.input");
        Cart firstCrashCart = mineCarts.getFirstCrash();
        System.out.printf("FIRST: %d,%d\n", firstCrashCart.getX(), firstCrashCart.getY());

        Cart lastCartRunning = mineCarts.getLastCartRunning();
        System.out.printf("LAST: %d,%d\n", lastCartRunning.getX(), lastCartRunning.getY());
    }

    private MineCarts parseTracks(String fileName) throws IOException {
        List<Cart> carts = new LinkedList<>();

        List<Track[]> trackRows = inputReader.readValues(fileName, (y, line) -> {
            Track[] row = new Track[line.length()];
            for (int x = 0; x < line.length(); x++) {
                char symbol = line.charAt(x);
                if (symbol != EMPTY) {
                    Cart cart = null;
                    TrackDirection trackDirection = TrackDirection.getDirection(symbol);
                    if (trackDirection == null) {
                        CartDirection cartDirection = CartDirection.getDirection(symbol);

                        cart = new Cart(carts.size(), cartDirection);
                        cart.setX(x);
                        cart.setY(y);

                        carts.add(cart);

                        trackDirection = cartDirection.getTrackDirection();
                    }
                    row[x] = new Track(trackDirection);
                    row[x].setCart(cart);
                }
            }

            return row;
        });

        return new MineCarts(carts, trackRows.toArray(new Track[trackRows.size()][]));
    }
}
