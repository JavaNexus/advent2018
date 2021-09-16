package pl.javanexus.year2018.day13;

import lombok.Data;

@Data
public class Track {

    private final TrackDirection direction;

    private Cart cart;
}
