package pl.javanexus.year2018.day13;

import lombok.Data;

@Data
public class CartMove {

    private final int dx;
    private final int dy;
    private final CartDirection direction;
}

