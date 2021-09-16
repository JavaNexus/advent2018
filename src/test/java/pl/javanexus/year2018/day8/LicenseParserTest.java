package pl.javanexus.year2018.day8;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LicenseParserTest {

    public static final String LICENSE = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

    @Test
    public void testGetMetadataSum() {
        Node root = new LicenseParser().parseLicense(LICENSE);
        assertEquals(138, root.getTotalMetadataSum() - 1);
        assertEquals(66, root.getValue());
    }

    @Test
    public void testGetMetadataSumFromFile() throws IOException {
        String license = new InputReader().readStringValues("day8_license.input").get(0);
        Node root = new LicenseParser().parseLicense(license);
        System.out.println(root.getTotalMetadataSum() - 1);//49180
        System.out.println(root.getValue());
    }
}
