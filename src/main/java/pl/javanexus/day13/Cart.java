package pl.javanexus.day13;

import lombok.Data;

@Data
public class Cart {

    private final int id;

    private boolean isCrashed = false;
    private CartDirection direction;
    private int x;
    private int y;
    private int intersectionsVisited;

    public Cart(int id, CartDirection direction) {
        this.id = id;
        this.direction = direction;
    }

    public void changePosition(CartDirection nextCartDirection) {
        this.direction = nextCartDirection;
        this.x += nextCartDirection.getDx();
        this.y += nextCartDirection.getDy();
    }

    public CartDirection getNextCartDirection(TrackDirection trackDirection) {
        if (trackDirection == TrackDirection.INTERSECTION) {
            return this.direction.getCartDirectionAfterIntersection(intersectionsVisited++);
        } else {
            return this.direction.getNextCartDirection(trackDirection);
        }
    }

    public void setCrashed(boolean crashed) {
        isCrashed = crashed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Cart cart = (Cart) o;

        return id == cart.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }
}
