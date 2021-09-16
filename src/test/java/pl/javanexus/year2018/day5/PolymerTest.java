package pl.javanexus.year2018.day5;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PolymerTest {

    public static final String POLYMER = "dabAcCaCBAcCcaDA";

    @Test
    public void testReduce() {
        Polymer polymer = new Polymer(POLYMER);
//        assertEquals(10, polymer.reduce());
        System.out.println(polymer.getShortestPolymerLength());
    }

    @Test
    public void testPolymerWithoutD() {
        Polymer polymer = new Polymer("abAcCaCBAcCcaA");
        System.out.println(polymer.reduce());
    }

    @Test
    public void testReduceFromFile() throws IOException {
        String value = new InputReader().readStringValues("day5_polymer.input").get(0);
        Polymer polymer = new Polymer(value);
//        System.out.println(polymer.reduce());
        System.out.println(polymer.getShortestPolymerLength());
    }
}
