package pl.javanexus.day4;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Nap {

    private final int from;
    private final int to;

    public int getDuration() {
        return to - from;
    }
}
