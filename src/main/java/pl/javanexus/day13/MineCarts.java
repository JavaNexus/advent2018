package pl.javanexus.day13;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MineCarts {

    private final List<Cart> carts;
    private final Track[][] tracks;

    public MineCarts(List<Cart> carts, Track[][] tracks) {
        this.carts = carts;
        this.tracks = tracks;
    }

    public Cart getFirstCrash() {
        printTracks();

        Optional<Cart> collision = Optional.empty();
        while (!collision.isPresent()) {
            //printTracks();
            collision = updateCartPositions();
        }

        return collision.get();
    }

    public Cart getLastCartRunning() {
        while (this.carts.size() > 1) {
            //printTracks();
            updateCartPositions();
        }

        return this.carts.get(0);
    }

    private Optional<Cart> updateCartPositions() {
        List<Cart> sortedCarts = carts.stream().sorted((cart1, cart2) -> {
            int dy = cart1.getY() - cart2.getY();
            return dy == 0 ? cart1.getX() - cart1.getX() : dy;
        }).collect(Collectors.toList());

        List<Cart> crashedCarts = new LinkedList<>();
        for (Cart cart : sortedCarts) {
            if (!cart.isCrashed()) {
                crashedCarts.addAll(updateCartPosition(cart));
            }
        }
        this.carts.removeAll(crashedCarts);

        return crashedCarts.isEmpty() ? Optional.empty() : Optional.of(crashedCarts.get(0));
    }

    private List<Cart> updateCartPosition(Cart cart) {
        List<Cart> crashedCarts = new LinkedList<>();

        Track currentPosition = tracks[cart.getY()][cart.getX()];
        tracks[cart.getY()][cart.getX()].setCart(null);

        CartDirection nextCartDirection = cart.getNextCartDirection(currentPosition.getDirection());
        cart.changePosition(nextCartDirection);

        Track nextPosition = tracks[cart.getY()][cart.getX()];
        if (nextPosition.getCart() == null) {
            nextPosition.setCart(cart);
        } else {
            nextPosition.getCart().setCrashed(true);
            cart.setCrashed(true);

            crashedCarts.add(nextPosition.getCart());
            crashedCarts.add(cart);

            nextPosition.setCart(null);
        }

        return crashedCarts;
    }

    public void printTracks() {
        iterateOverTracks(
                (x, y) -> {
                    Track track = tracks[x][y];
                    if (track != null) {
                        char symbol = Optional.ofNullable(track.getCart())
                                .map(cart -> cart.getDirection().getSymbol())
                                .orElse(track.getDirection().getSymbol());
                        System.out.printf("%s", symbol);
                    } else {
                        System.out.printf(" ");
                    }
                },
                (y) -> {
                    System.out.printf("\n");
                });
    }

    private void iterateOverTracks(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer) {
        for (int x = 0; x < tracks.length; x++) {
            for (int y = 0; y < tracks[x].length; y++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(x);
        }
    }
}
