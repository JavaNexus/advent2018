package pl.javanexus.day13;

import lombok.Data;

@Data
public class CartMove {

    private final int dx;
    private final int dy;
    private final CartDirection direction;
}

