package pl.javanexus.year2018.day3;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class FabricTest {

    public static final List<Slice> SLICES = Arrays.asList(
            new Slice(1, 1, 3, 4,4 ),
            new Slice(2, 3, 1, 4,4 ),
            new Slice(3, 5, 5, 2,2 )
    );

    @Test
    public void testCutFabric() {
        Fabric fabric = new Fabric();
        fabric.cutFabric(SLICES);

        assertEquals(4, fabric.getOverlappingArea());
        assertEquals(3, fabric.getNotOverlappedSliceId());
    }

    @Test
    public void testCutFabricFromFile() throws IOException {
        List<Slice> slices = new InputReader().readValues("day3_fabric.input", this::getSliceFromString);

        Fabric fabric = new Fabric();
        fabric.cutFabric(slices);

        System.out.println(fabric.getOverlappingArea());
        System.out.println(fabric.getNotOverlappedSliceId());
    }

    private Slice getSliceFromString(int index, String value) {
        Pattern pattern = Pattern.compile("#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return new Slice(
                    getInt(matcher,1),
                    getInt(matcher,2),
                    getInt(matcher,3),
                    getInt(matcher,4),
                    getInt(matcher,5));
        }

        throw new IllegalArgumentException("Unexpected pattern: " + value);
    }

    private int getInt(Matcher matcher, int groupId) {
        return Integer.parseInt(matcher.group(groupId));
    }
}
