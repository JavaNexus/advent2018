package pl.javanexus.year2018.day10;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Star {

    private final int id;
    private int x;
    private int y;
    private final int vx;
    private final int vy;

    public int getNextX() {
        return x + vx;
    }

    public int getNextY() {
        return y + vy;
    }

    public void moveOneStep() {
        this.x += vx;
        this.y += vy;
    }
}
