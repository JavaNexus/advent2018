package pl.javanexus.day3;

import lombok.Data;

@Data
public class Slice {

    private final int id;
    private final int left;
    private final int top;
    private final int width;
    private final int height;

    public int getRight() {
        return left + width;
    }

    public int getBottom() {
        return top + height;
    }

    public int getArea() {
        return width * height;
    }
}
