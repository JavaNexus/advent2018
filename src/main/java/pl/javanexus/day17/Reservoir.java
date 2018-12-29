package pl.javanexus.day17;

import lombok.Data;

import java.util.Optional;

@Data
public class Reservoir {

    private final Point topLeft;
    private final Point topRight;
    private final Point bottomLeft;
    private final Point bottomRight;

    private boolean isOpen = true;
    private Optional<Reservoir> innerReservoir;

    public int getVolume() {
        throw new RuntimeException("Not implemented yet!");
    }
}
