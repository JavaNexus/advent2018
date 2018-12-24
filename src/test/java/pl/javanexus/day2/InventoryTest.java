package pl.javanexus.day2;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryTest {

    public static final String[] VALUES_1 = {
            "abcdef",
            "bababc",
            "abbcde",
            "abcccd",
            "aabcdd",
            "abcdee",
            "ababab"
    };

    public static final String[] VALUES_2 = {
            "abcde",
            "fghij",
            "klmno",
            "pqrst",
            "fguij",
            "axcye",
            "wvxyz"
    };

    @Test
    public void testGetChecksum() {
        Inventory inventory = new Inventory(VALUES_1);
        assertEquals(12, inventory.getChecksum());
    }

    @Test
    public void testGetMatchingValue() {
        assertEquals("abcde", new Inventory(VALUES_1).getMatchingValue());
        assertEquals("fgij", new Inventory(VALUES_2).getMatchingValue());
    }

    @Test
    public void testGetChecksumFromFile() throws IOException {
        List<String> values = new InputReader().readStringValues("day2_inventory.input");
        Inventory inventory = new Inventory(values);
        System.out.println(inventory.getChecksum());
    }

    @Test
    public void testGetMatchingValueFromFile() throws IOException {
        List<String> values = new InputReader().readStringValues("day2_inventory.input");
        Inventory inventory = new Inventory(values);
        System.out.println(inventory.getMatchingValue());
    }
}
